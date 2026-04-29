package com.magicvs.backend.repository;

import com.magicvs.backend.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    boolean existsByUserIdAndAchievementId(Long userId, Long achievementId);
    List<UserAchievement> findByUserIdOrderByUnlockedAtDesc(Long userId);
}
