import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class NotificationService {
    private enabledNotifications: boolean = false;
    private lastTimeNotificationSent: number = 0;
    private notificationAudio: HTMLAudioElement = new Audio();

    constructor() {
        this.notificationAudio.src = '/assets/sounds/notification.mp3';
        this.notificationAudio.load();
        this.notificationAudio.volume = 0.8;
    }

    setDocumentVisibility(visibilityState: DocumentVisibilityState) {
        this.enabledNotifications = visibilityState === 'hidden';
    }

    sendSoundNotification() {
        if (this.enabledNotifications && (Date.now() - this.lastTimeNotificationSent) > 4000) {
            this.notificationAudio.play();
            this.lastTimeNotificationSent = Date.now();
        }
    }

    enableNotifications() {
        this.enabledNotifications = true;
    }

    disableNotifications() {
        this.enabledNotifications = false;
    }
}
