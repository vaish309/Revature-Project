import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { NotificationService } from '../../services/notification.service';
import { Notification } from '../../models/notification.model';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit, OnDestroy {
  notifications: Notification[] = [];
  unreadCount = 0;
  showDropdown = false;
  loading = false;

  constructor(
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.notificationService.startPolling();
    this.loadNotifications();
    
    this.notificationService.unreadCount$.subscribe(count => {
      this.unreadCount = count;
    });
  }

  ngOnDestroy(): void {
    this.notificationService.stopPolling();
  }

  loadNotifications(): void {
    this.loading = true;
    this.notificationService.getAllNotifications().subscribe({
      next: (response) => {
        if (response.success) {
          this.notifications = response.data.slice(0, 10); // Show latest 10
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  toggleDropdown(): void {
    this.showDropdown = !this.showDropdown;
    if (this.showDropdown) {
      this.loadNotifications();
    }
  }

  markAsRead(notification: Notification, event: Event): void {
    event.stopPropagation();
    if (!notification.read) {
      this.notificationService.markAsRead(notification.id).subscribe({
        next: () => {
          notification.read = true;
          this.handleNotificationClick(notification);
        }
      });
    } else {
      this.handleNotificationClick(notification);
    }
  }

  handleNotificationClick(notification: Notification): void {
    this.showDropdown = false;
    
    // Navigate based on notification type
    if (notification.relatedEntityType === 'APPLICATION' && notification.relatedEntityId) {
      this.router.navigate(['/jobseeker/applications']);
    } else if (notification.relatedEntityType === 'JOB' && notification.relatedEntityId) {
      this.router.navigate(['/jobs', notification.relatedEntityId]);
    }
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead().subscribe({
      next: () => {
        this.notifications.forEach(n => n.read = true);
      }
    });
  }

  deleteNotification(id: number, event: Event): void {
    event.stopPropagation();
    this.notificationService.deleteNotification(id).subscribe({
      next: () => {
        this.notifications = this.notifications.filter(n => n.id !== id);
      }
    });
  }

  getNotificationIcon(type: string): string {
    const icons: { [key: string]: string } = {
      'APPLICATION_STATUS_UPDATE': '📋',
      'JOB_RECOMMENDATION': '💼',
      'APPLICATION_RECEIVED': '📨',
      'APPLICATION_SHORTLISTED': '⭐',
      'APPLICATION_REJECTED': '❌',
      'APPLICATION_UNDER_REVIEW': '👀'
    };
    return icons[type] || '🔔';
  }

  getNotificationClass(type: string): string {
    const classes: { [key: string]: string } = {
      'APPLICATION_SHORTLISTED': 'notification-success',
      'APPLICATION_REJECTED': 'notification-danger',
      'APPLICATION_UNDER_REVIEW': 'notification-warning',
      'JOB_RECOMMENDATION': 'notification-info'
    };
    return classes[type] || 'notification-default';
  }

  getTimeAgo(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const seconds = Math.floor((now.getTime() - date.getTime()) / 1000);

    if (seconds < 60) return 'Just now';
    if (seconds < 3600) return `${Math.floor(seconds / 60)}m ago`;
    if (seconds < 86400) return `${Math.floor(seconds / 3600)}h ago`;
    if (seconds < 604800) return `${Math.floor(seconds / 86400)}d ago`;
    return date.toLocaleDateString();
  }
}
