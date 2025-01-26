import { UserModel } from '@app/models/user.model';

export interface ChatRoomModel {
    id?: number;
    name: string;
    description: string;
    createdAt?: Date;
    owner?: UserModel;
}
