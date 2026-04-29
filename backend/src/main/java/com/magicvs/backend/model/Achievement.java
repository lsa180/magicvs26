package com.magicvs.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "achievements", indexes = {@Index(columnList = "code", name = "idx_achievement_code")})
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AchievementTrigger trigger;

    @Column(name = "threshold")
    private Integer threshold;

    @Column(name = "badge_uri")
    private String badgeUri;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Achievement() {}

    // getters and setters
    public Long getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public AchievementTrigger getTrigger() { return trigger; }
    public void setTrigger(AchievementTrigger trigger) { this.trigger = trigger; }
    public Integer getThreshold() { return threshold; }
    public void setThreshold(Integer threshold) { this.threshold = threshold; }
    public String getBadgeUri() { return badgeUri; }
    public void setBadgeUri(String badgeUri) { this.badgeUri = badgeUri; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
