import { UserRole, UserStatus } from '@app/shared/enums';
import { UserPreferences } from './UserPreferences.model';

export interface UserModel {
    id: number;
    username: string;
    role: UserRole;
    accountNonExpired?: boolean;
    accountNonLocked?: boolean;
    credentialsNonExpired?: boolean;
    enabled?: boolean;
    authorities?: string[];
    preferences?: UserPreferences
}
