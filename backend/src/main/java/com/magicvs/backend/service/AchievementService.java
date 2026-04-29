package com.magicvs.backend.service;

import com.magicvs.backend.dto.UserAchievementDto;
import com.magicvs.backend.model.Achievement;
import com.magicvs.backend.model.AchievementTrigger;
import com.magicvs.backend.model.User;
import com.magicvs.backend.model.UserAchievement;
import com.magicvs.backend.repository.AchievementRepository;
import com.magicvs.backend.repository.DeckRepository;
import com.magicvs.backend.repository.RegistroRepository;
import com.magicvs.backend.repository.UserAchievementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AchievementService {

    private static final Logger logger = LoggerFactory.getLogger(AchievementService.class);

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final RegistroRepository registroRepository;
    private final DeckRepository deckRepository;
    private final NotificationService notificationService;

    public AchievementService(AchievementRepository achievementRepository,
                              UserAchievementRepository userAchievementRepository,
                              RegistroRepository registroRepository,
                              DeckRepository deckRepository,
                              NotificationService notificationService) {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.registroRepository = registroRepository;
        this.deckRepository = deckRepository;
        this.notificationService = notificationService;
    }

    public List<UserAchievementDto> getUserAchievements(Long userId) {
        return userAchievementRepository.findByUserIdOrderByUnlockedAtDesc(userId)
                .stream().map(UserAchievementDto::fromEntity).collect(Collectors.toList());
    }

    public void checkAndUnlockForTrigger(Long userId, AchievementTrigger trigger) {
        User user = registroRepository.findById(userId).orElse(null);
        if (user == null) return;

        List<Achievement> candidates = achievementRepository.findByTriggerAndActive(trigger, true);
        if (candidates.isEmpty()) return;

        for (Achievement a : candidates) {
            boolean already = userAchievementRepository.existsByUserIdAndAchievementId(userId, a.getId());
            if (already) continue;

            int current = 0;
            if (trigger == AchievementTrigger.DECK_CREATED) {
                current = (int) deckRepository.countByUserId(userId);
            } else if (trigger == AchievementTrigger.GAMES_WON) {
                current = user.getGamesWon() != null ? user.getGamesWon() : 0;
            }

            if (a.getThreshold() != null && current >= a.getThreshold()) {
                unlock(user, a);
            }
        }
    }

    private void unlock(User user, Achievement a) {
        UserAchievement ua = new UserAchievement();
        ua.setUser(user);
        ua.setAchievement(a);
        userAchievementRepository.save(ua);

        logger.info("User {} unlocked achievement {}", user.getId(), a.getCode());

        Map<String, Object> data = Map.of(
            "title", "Logro desbloqueado: " + a.getName(),
            "message", a.getDescription(),
            "badgeUri", a.getBadgeUri(),
            "achievementCode", a.getCode()
        );

        notificationService.createNotification(user.getId(), com.magicvs.backend.model.NotificationType.ACHIEVEMENT, data);
    }
}
