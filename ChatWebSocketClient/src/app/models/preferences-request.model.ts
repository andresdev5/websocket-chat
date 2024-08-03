export interface PreferencesRequestModel {
    userId: number;
    about?: string;
    avatar?: string;
    color?: string;
    status?: 'ONLINE' | 'OFFLINE' | 'AWAY' | 'BUSY';
    theme?: 'dark' | 'light';
}
