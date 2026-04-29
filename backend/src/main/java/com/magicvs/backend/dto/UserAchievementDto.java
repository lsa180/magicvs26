package com.magicvs.backend.dto;

import com.magicvs.backend.model.UserAchievement;

public class UserAchievementDto {
    public String code;
    public String name;
    public String description;
    public String badgeUri;
    public java.time.LocalDateTime unlockedAt;

    public static UserAchievementDto fromEntity(UserAchievement ua) {
        UserAchievementDto d = new UserAchievementDto();
        d.code = ua.getAchievement().getCode();
        d.name = ua.getAchievement().getName();
        d.description = ua.getAchievement().getDescription();
        d.badgeUri = ua.getAchievement().getBadgeUri();
        d.unlockedAt = ua.getUnlockedAt();
        return d;
    }
}
