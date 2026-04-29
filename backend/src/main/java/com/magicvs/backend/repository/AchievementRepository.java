package com.magicvs.backend.repository;

import com.magicvs.backend.model.Achievement;
import com.magicvs.backend.model.AchievementTrigger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByTriggerAndActive(AchievementTrigger trigger, Boolean active);
    Achievement findByCode(String code);
}
