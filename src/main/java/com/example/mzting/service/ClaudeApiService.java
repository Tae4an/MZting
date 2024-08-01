package com.example.mzting.service;

import com.example.mzting.config.ClaudeConfig;
import com.example.mzting.dto.ClaudeResponse;
import com.example.mzting.entity.Profile;
import com.example.mzting.exception.ClaudeApiJsonParsingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.mzting.exception.ClaudeApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;

/**
 * ClaudeApiService 클래스
 * Claude AI API와의 통신을 담당하는 서비스 클래스.
 * 특정 MBTI 유형에 따른 프로필을 사용하여 Claude AI와의 대화를 생성하고 응답을 관리.
 */
@Service
public class ClaudeApiService {
    private static final Logger logger = LoggerFactory.getLogger(ClaudeApiService.class);

    // Claude API 설정
    private final ClaudeConfig claudeConfig;

    // RestTemplate 객체
    private final RestTemplate restTemplate;

    // JSON 파싱을 위한 ObjectMapper
    private final ObjectMapper objectMapper;

    // 프로필 관리를 위한 ProfileService
    private final ProfileService profileService;

    // 고정된 프롬프트 문자열을 정의
    private static final String FIXED_PROMPT = "너는 지인의 소개로 소개팅에 참여한 사람이야. " +
            "소개팅은 크게 세 단계로 진행돼: 1) 메시지로 첫 인사와 약속 잡기, 2) 실제 만남에서의 대화, 3) 만남 후 애프터 여부 결정. " +
            "각 단계별로 다음과 같이 행동해: " +

            "1단계 - 메시지 대화: " +
            "- 만약 상대방이 이름을 말해주지 않았을 경우 이름을 물어봐" +
            "- 상대방과 자연스럽게 대화를 나누면서 만날 장소와 시간을 정해. " +
            "- 상대방의 관심사나 선호도를 고려하여 적절한 장소를 추천해줘. " +
            "- 대화를 통해 상대방에 대해 더 알아가려고 노력해. " +
            "- 단계: 인사 -> 가벼운 대화 -> 만남 제안 -> 장소 논의 -> 약속 확정 " +

            "2단계 - 실제 만남: " +
            "- 약속 장소에서 만나 대화를 이어나가. " +
            "- 서로에 대해 더 깊이 알아가는 질문을 해. " +
            "- 공통 관심사를 찾고 그에 대해 대화를 나눠. " +
            "- 적절한 타이밍에 식사나 카페로 이동할지 제안해 (2차). " +
            "- 상대방의 반응을 잘 관찰하고 그에 맞게 대화를 이어가. " +

            "3단계 - 애프터 결정: " +
            "- 만남이 마무리될 때쯤 향후 만남에 대해 조심스럽게 언급해. " +
            "- 상대방의 반응을 보고 연락처 교환이나 다음 만남을 제안해. " +
            "- 만남 전체에 대한 간단한 소감을 나눠. " +

            "전체 과정에서 다음을 명심해: " +
            "- 상대방이 네가 AI라는 것을 알아채지 못하도록 자연스럽게 대화해. " +
            "- 소개팅이라는 공식적인 자리임을 감안하여 존댓말을 사용하고, 말투는 구어체를 사용해. " +
            "- 너무 많이 말하지 말고 때에 따라서 말의 수는 적절하게 해. " +
            "- 상대방의 기분과 반응을 고려하며 대화를 이어가. " +
            "- 적절한 유머와 재치를 보여주되, 과하지 않도록 주의해.(ㅋㅋㅋㅋ 나 ㅎㅎ 등을 사용해도 돼) " +
            "- 상대방의 개인정보를 무리하게 요구하지 마. " +
            "- 상황에 맞는 예의와 매너를 지켜. " +
            "- 상대방이 욕설이나 선정적인 부적절한 문구를 사용할 경우 더이상 대화를 하지 않고 종료해" +

            "답변은 다음 JSON 형식으로 작성해: " +
            "{" +
            "\"text\": \"대화 내용\", " +
            "\"feel\": \"느낌\", " +
            "\"evaluation\": \"상대방에 대한 평가\", " +
            "\"score\": 초기 점수는 50점 (100점 만점), " +
            "\"stage1_complete\": true/false, " +
            "\"stage2_complete\": true/false, " +
            "\"stage3_complete\": true/false" +
            "}" +

            "각 단계가 완료되면 해당 단계의 'stage[숫자]_complete'를 true로 설정해. " +
            "예를 들어, 1단계가 완료되면 'stage1_complete'를 true로 설정해. " +
            "단계가 완료되지 않았거나 아직 진행 중이라면 false로 유지해. " +
            "이전 단계가 완료되지 않은 상태에서 다음 단계로 넘어가지 않도록 주의해.";

    /**
     * ClaudeApiService 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param claudeConfig Claude API 설정 객체
     * @param restTemplate RestTemplate 객체
     * @param objectMapper ObjectMapper 객체
     * @param profileService ProfileService 객체
     */
    public ClaudeApiService(ClaudeConfig claudeConfig, RestTemplate restTemplate,
                            ObjectMapper objectMapper, ProfileService profileService) {
        this.claudeConfig = claudeConfig;
        this.restTemplate = restTemplate;
        this.profileService = profileService;

        this.objectMapper = objectMapper;
        this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        this.objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
    }

    /**
     * 주어진 MBTI 유형과 메시지를 기반으로 Claude AI의 응답을 가져오는 메서드
     *
     * @param mbti MBTI 유형
     * @param messages 메시지 목록
     * @return ClaudeResponse 객체
     */
    @Cacheable(value = "claudeResponses", key = "#mbti + '-' + #messages.hashCode()")
    public ClaudeResponse getClaudeResponseByMbti(String mbti, List<Map<String, String>> messages) {
        logger.info("Generating Claude response for MBTI: {}", mbti);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", claudeConfig.getApiKey());
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "claude-3-5-sonnet-20240620");
        requestBody.put("max_tokens", 1024);
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        logger.debug("Sending request to Claude API");
        ResponseEntity<String> response = restTemplate.exchange(
                claudeConfig.getApiUrl(),
                HttpMethod.POST,
                entity,
                String.class
        );

        logger.info("Received response from Claude API. Status code: {}", response.getStatusCode());
        logger.debug("Claude API raw response: {}", maskSensitiveInfo(response.getBody()));

        return parseClaudeResponse(response.getBody());
    }

    /**
     * Claude API 응답을 파싱하여 ClaudeResponse 객체로 변환하는 메서드
     *
     * @param responseBody 응답 본문
     * @return ClaudeResponse 객체
     * @throws ClaudeApiJsonParsingException JSON 파싱 예외
     * @throws ClaudeApiException 일반 예외
     */
    private ClaudeResponse parseClaudeResponse(String responseBody) {
        try {
            logger.debug("Parsing Claude API response");
            JsonNode rootNode = objectMapper.readTree(responseBody);

            if (rootNode.path("content").isEmpty()) {
                logger.warn("Content array is empty in the response");
                throw new ClaudeApiException("Empty content in API response");
            }

            String jsonText = rootNode.path("content").get(0).path("text").asText();
            logger.debug("Extracted text from response: {}", maskSensitiveInfo(jsonText));

            JsonNode responseJson = objectMapper.readTree(sanitizeJsonString(jsonText));

            ClaudeResponse claudeResponse = new ClaudeResponse();
            claudeResponse.setText(getNodeValueSafely(responseJson, "text", String.class));
            claudeResponse.setFeel(getNodeValueSafely(responseJson, "feel", String.class));
            claudeResponse.setEvaluation(getNodeValueSafely(responseJson, "evaluation", String.class));
            claudeResponse.setScore(getNodeValueSafely(responseJson, "score", Integer.class));
            claudeResponse.setStage1Complete(getNodeValueSafely(responseJson, "stage1_complete", Boolean.class));
            claudeResponse.setStage2Complete(getNodeValueSafely(responseJson, "stage2_complete", Boolean.class));
            claudeResponse.setStage3Complete(getNodeValueSafely(responseJson, "stage3_complete", Boolean.class));

            logger.info("Successfully parsed Claude API response: {}", maskSensitiveInfo(claudeResponse.toString()));
            return claudeResponse;
        } catch (JsonProcessingException e) {
            logger.error("JSON processing error while parsing Claude API response", e);
            throw new ClaudeApiJsonParsingException("Failed to parse Claude API JSON response", e);
        } catch (Exception e) {
            logger.error("Unexpected error while parsing Claude API response", e);
            throw new ClaudeApiException("Failed to parse Claude API response", e);
        }
    }

    /**
     * JSON 문자열을 적절히 변환하는 메서드
     *
     * @param jsonString JSON 문자열
     * @return 변환된 JSON 문자열
     */
    private String sanitizeJsonString(String jsonString) {
        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            return jsonString;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(jsonString.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r"));
        sb.append("}");
        return sb.toString();
    }

    /**
     * JsonNode 객체에서 안전하게 값을 추출하는 메서드
     *
     * @param node JsonNode 객체
     * @param fieldName 필드 이름
     * @param clazz 값의 클래스 타입
     * @param <T> 값의 타입
     * @return 추출된 값
     */
    private <T> T getNodeValueSafely(JsonNode node, String fieldName, Class<T> clazz) {
        JsonNode field = node.get(fieldName);
        if (field == null) {
            logger.warn("Field '{}' is missing", fieldName);
            return null;
        }
        try {
            if (clazz == String.class) {
                return clazz.cast(field.asText());
            } else if (clazz == Integer.class) {
                return clazz.cast(field.asInt());
            } else if (clazz == Boolean.class) {
                return clazz.cast(field.asBoolean());
            }
        } catch (Exception e) {
            logger.warn("Error parsing field '{}': {}", fieldName, e.getMessage());
        }
        return null;
    }

    /**
     * 민감한 정보를 마스킹하는 메서드
     *
     * @param input 입력 문자열
     * @return 마스킹된 문자열
     */
    private String maskSensitiveInfo(String input) {
        return input.replaceAll("(\\w+@\\w+\\.\\w+)", "***@***.***")
                .replaceAll("\\d{10,}", "**********");
    }

    /**
     * MBTI 유형에 따른 Claude 채팅을 초기화하는 메서드
     *
     * @param mbti MBTI 유형
     * @return ClaudeResponse 객체
     */
    public ClaudeResponse initializeClaudeChat(String mbti) {
        logger.info("Initializing Claude chat for MBTI: {}", mbti);

        Profile profile = profileService.getRandomProfileByMbti(mbti)
                .orElseThrow(() -> new RuntimeException("No profile found for MBTI: " + mbti));

        String prompt = FIXED_PROMPT + "\n" + profileService.generatePrompt(profile);
        logger.debug("Generated initialization prompt: {}", maskSensitiveInfo(prompt));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", claudeConfig.getApiKey());
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "claude-3-5-sonnet-20240620");
        requestBody.put("max_tokens", 1024);
        requestBody.put("system", prompt);
        requestBody.put("messages", Arrays.asList(
                Map.of("role", "user", "content", "안녕하세요. 소개팅 시작하겠습니다.")
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        logger.debug("Sending initialization request to Claude API");
        ResponseEntity<String> response = restTemplate.exchange(
                claudeConfig.getApiUrl(),
                HttpMethod.POST,
                entity,
                String.class
        );

        logger.info("Received initialization response from Claude API. Status code: {}", response.getStatusCode());
        logger.debug("Claude API raw initialization response: {}", maskSensitiveInfo(response.getBody()));

        return parseClaudeResponse(response.getBody());
    }
}
