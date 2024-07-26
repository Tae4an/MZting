package com.example.mzting.service;

import com.example.mzting.entity.Profile;
import com.example.mzting.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);


    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Cacheable(value = "profiles", key = "#profileId")
    public Optional<Profile> getProfileById(Integer profileId) {
        return profileRepository.findById(profileId);
    }

    @Cacheable(value = "profiles", key = "'all'")
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @Transactional
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    @Transactional
    public void deleteProfile(Integer profileId) {
        profileRepository.deleteById(profileId);
    }

    @Cacheable(value = "profiles", key = "'randomByMbti:' + #mbti")
    public Optional<Profile> getRandomProfileByMbti(String mbti) {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<Profile> profilePage = profileRepository.findRandomProfileByMbti(mbti, pageRequest);
        return profilePage.stream().findFirst();
    }

    @Cacheable(value = "profiles", key = "#mbti")
    public List<Profile> getProfilesByMbti(String mbti) {
        return profileRepository.findByMbti(mbti);
    }

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

        // 프롬프트 정보 출력
        logger.info("Generated prompt for character:\n{}", prompt.toString());


        return prompt.toString();
    }
}