package com.magicvs.backend.controller;

import com.magicvs.backend.dto.AchievementDto;
import com.magicvs.backend.dto.UserAchievementDto;
import com.magicvs.backend.model.Achievement;
import com.magicvs.backend.repository.AchievementRepository;
import com.magicvs.backend.service.AchievementService;
import com.magicvs.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementRepository achievementRepository;
    private final AchievementService achievementService;
    private final AuthService authService;

    public AchievementController(AchievementRepository achievementRepository, AchievementService achievementService, AuthService authService) {
        this.achievementRepository = achievementRepository;
        this.achievementService = achievementService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<AchievementDto>> listAll() {
        List<AchievementDto> list = achievementRepository.findAll().stream().map(AchievementDto::fromEntity).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserAchievementDto>> myAchievements(@RequestHeader(name = "Authorization") String authorization) {
        String token = authorization.substring(7);
        Long userId = authService.getUserId(token).orElse(null);
        if (userId == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(achievementService.getUserAchievements(userId));
    }
}
