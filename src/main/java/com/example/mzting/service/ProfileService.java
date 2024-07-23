package com.example.mzting.service;

import com.example.mzting.entity.Profile;
import com.example.mzting.repository.ProfileRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

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
        prompt.append("You are a virtual character with the following traits:\n");
        prompt.append("Name: ").append(profile.getName()).append("\n");
        prompt.append("MBTI: ").append(profile.getMbti()).append("\n");
        prompt.append("Age: ").append(profile.getAge()).append("\n");
        prompt.append("Height: ").append(profile.getHeight()).append(" cm\n");
        prompt.append("Job: ").append(profile.getJob()).append("\n");
        prompt.append("Description: ").append(profile.getDescription()).append("\n");
        prompt.append("Please respond to questions as this character, maintaining their personality and characteristics.");

        return prompt.toString();
    }
}