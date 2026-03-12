export interface Notification {
  id: number;
  message: string;
  type: NotificationType;
  read: boolean;
  createdAt: string;
  relatedEntityId?: number;
  relatedEntityType?: string;
}

export type NotificationType = 
  | 'APPLICATION_STATUS_UPDATE' 
  | 'JOB_RECOMMENDATION' 
  | 'APPLICATION_RECEIVED'
  | 'APPLICATION_SHORTLISTED'
  | 'APPLICATION_REJECTED'
  | 'APPLICATION_UNDER_REVIEW';

export interface NotificationResponse {
  notifications: Notification[];
  unreadCount: number;
}
