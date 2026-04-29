package com.magicvs.backend.dto;

import com.magicvs.backend.model.Achievement;

public class AchievementDto {
    public String code;
    public String name;
    public String description;
    public String badgeUri;
    public Integer threshold;
    public String trigger;

    public static AchievementDto fromEntity(Achievement a) {
        AchievementDto d = new AchievementDto();
        d.code = a.getCode();
        d.name = a.getName();
        d.description = a.getDescription();
        d.badgeUri = a.getBadgeUri();
        d.threshold = a.getThreshold();
        d.trigger = a.getTrigger() != null ? a.getTrigger().name() : null;
        return d;
    }
}
