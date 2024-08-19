package com.example.mzting.service;

import com.example.mzting.entity.Profile;
import com.example.mzting.entity.User;
import com.example.mzting.entity.UserCustomImage;
import com.example.mzting.repository.ProfileRepository;
import com.example.mzting.repository.UserCustomImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ProfileService 클래스
 * 사용자 프로필 관리를 위한 서비스 클래스
 */
@Service
public class ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    // 프로필 저장소
    private final ProfileRepository profileRepository;
    private final UserCustomImageRepository userCustomImageRepository;

    /**
     * ProfileService 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param profileRepository 프로필 저장소
     */
    public ProfileService(ProfileRepository profileRepository, UserCustomImageRepository userCustomImageRepository) {
        this.profileRepository = profileRepository;
        this.userCustomImageRepository = userCustomImageRepository;
    }

    /**
     * 프로필 ID로 프로필을 조회하는 메서드
     *
     * @param profileId 프로필 ID
     * @return 프로필 객체의 Optional
     */
    @Cacheable(value = "profiles", key = "#profileId")
    public Optional<Profile> getProfileById(Integer profileId) {
        return profileRepository.findById(profileId);
    }

    /**
     * 모든 프로필을 조회하는 메서드
     *
     * @return 프로필 목록
     */
    @Cacheable(value = "profiles", key = "'all'")
    public List<Profile> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll();
        Map<Long, UserCustomImage> userCustomImages = getUserCustomImages(profiles);

        for (int i = 0; i < profiles.size(); i++) {
            Profile profile = profiles.get(i);
            UserCustomImage customImage = userCustomImages.get(profile.getProfileId().longValue());
            if (customImage != null) {
                String customImageField = getCustomImageField(customImage, i + 1);
                profile.setCharacterImage(customImageField);
            }
        }

        return profiles;
    }

    private Map<Long, UserCustomImage> getUserCustomImages(List<Profile> profiles) {
        List<Long> profileIds = profiles.stream()
                .map(profile -> profile.getProfileId().longValue())
                .collect(Collectors.toList());

        List<UserCustomImage> customImages = userCustomImageRepository.findAllById(profileIds);

        return customImages.stream()
                .collect(Collectors.toMap(UserCustomImage::getId, Function.identity()));
    }

    private String getCustomImageField(UserCustomImage customImage, int index) {
        switch (index) {
            case 1: return customImage.getProfileImage1();
            case 2: return customImage.getProfileImage2();
            case 3: return customImage.getProfileImage3();
            case 4: return customImage.getProfileImage4();
            case 5: return customImage.getProfileImage5();
            case 6: return customImage.getProfileImage6();
            case 7: return customImage.getProfileImage7();
            case 8: return customImage.getProfileImage8();
            case 9: return customImage.getProfileImage9();
            case 10: return customImage.getProfileImage10();
            case 11: return customImage.getProfileImage11();
            case 12: return customImage.getProfileImage12();
            case 13: return customImage.getProfileImage13();
            case 14: return customImage.getProfileImage14();
            case 15: return customImage.getProfileImage15();
            case 16: return customImage.getProfileImage16();
            default: throw new IllegalArgumentException("Invalid profile image index: " + index);
        }
    }

    /**
     * 프로필을 저장하는 메서드
     *
     * @param profile 저장할 프로필 객체
     * @return 저장된 프로필 객체
     */
    @Transactional
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    /**
     * 프로필을 삭제하는 메서드
     *
     * @param profileId 삭제할 프로필 ID
     */
    @Transactional
    public void deleteProfile(Integer profileId) {
        profileRepository.deleteById(profileId);
    }

    /**
     * 특정 MBTI 유형의 무작위 프로필을 조회하는 메서드
     *
     * @param mbti MBTI 유형
     * @return 프로필 객체의 Optional
     */
    @Cacheable(value = "profiles", key = "'randomByMbti:' + #mbti")
    public Optional<Profile> getRandomProfileByMbti(String mbti) {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<Profile> profilePage = profileRepository.findRandomProfileByMbti(mbti, pageRequest);
        return profilePage.stream().findFirst();
    }

    /**
     * 특정 MBTI 유형의 프로필 목록을 조회하는 메서드
     *
     * @param mbti MBTI 유형
     * @return 프로필 목록
     */
    @Cacheable(value = "profiles", key = "#mbti")
    public List<Profile> getProfilesByMbti(String mbti) {
        return profileRepository.findByMbti(mbti);
    }

    /**
     * 프로필 정보를 기반으로 프롬프트를 생성하는 메서드
     *
     * @param profile 프로필 객체
     * @return 생성된 프롬프트 문자열
     */
    public String generatePrompt(Profile profile) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("당신은 다음과 같은 특성을 가진 캐릭터입니다:\n");
        prompt.append("이름: ").append(profile.getName()).append("\n");
        prompt.append("MBTI: ").append(profile.getMbti()).append("\n");
        prompt.append("나이: ").append(profile.getAge()).append("\n");
        prompt.append("키: ").append(profile.getHeight()).append("cm\n");
        prompt.append("직업: ").append(profile.getJob()).append("\n");

        prompt.append("취미: ");
        List<String> hobbies = profile.getCharacterHobbies().stream()
                .map(ch -> ch.getHobby().getHobby())
                .collect(Collectors.toList());
        prompt.append(String.join(", ", hobbies)).append("\n");

        prompt.append("성격 키워드: ");
        List<String> keywords = profile.getCharacterKeywords().stream()
                .map(ck -> ck.getKeyword().getKeyword())
                .collect(Collectors.toList());
        prompt.append(String.join(", ", keywords)).append("\n");

        prompt.append("상세 설명: ").append(profile.getDescription()).append("\n");

        if (profile.getPrompt() != null && !profile.getPrompt().isEmpty()) {
            prompt.append("상세 프롬포트: ").append(profile.getPrompt()).append("\n");
        }

        // 프롬프트 정보 출력
        logger.info("Generated prompt for character:\n{}", prompt.toString());

        return prompt.toString();
    }

    public UserCustomImage getUserCustomImage(Long uid) {
        return userCustomImageRepository.findById(uid)
                .orElseGet(this::createDefaultUserCustomImage);
    }

    private UserCustomImage createDefaultUserCustomImage() {
        return new UserCustomImage();
    }
}
