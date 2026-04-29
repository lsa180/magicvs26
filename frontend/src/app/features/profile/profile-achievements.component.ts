import { CommonModule } from '@angular/common';
import { Component, Input, OnInit, inject } from '@angular/core';
import { AchievementService } from './achievement.service';
import { ProfileResponse } from './profile.service';

@Component({
  selector: 'app-profile-achievements',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile-achievements.component.html',
  styleUrls: ['./profile-achievements.component.scss'],
})
export class ProfileAchievementsComponent implements OnInit {
  private readonly achievementService = inject(AchievementService);

  @Input() profile: ProfileResponse | null = null;
  @Input() isOwnProfile = false;

  all: any[] = [];
  unlockedCodes = new Set<string>();
  loading = true;

  ngOnInit(): void {
    this.loadAll();
  }

  private loadAll(): void {
    this.loading = true;
    this.achievementService.listAll().subscribe({
      next: (all) => {
        this.all = all;
        const unlocked = this.profile?.achievements ?? [];
        this.unlockedCodes = new Set(unlocked.map(a => a.code));
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  isUnlocked(code: string): boolean {
    return this.unlockedCodes.has(code);
  }
}
