import { UserStatus } from '@app/shared/enums';

export interface UserPreferences {
    about?: string;
    avatar?: string;
    color?: string;
    status?: UserStatus;
    theme?: 'dark' | 'light';
}
