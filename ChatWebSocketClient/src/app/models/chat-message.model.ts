import { ChatRoomModel } from './chat-room.model';
import { UserModel } from './user.model';

export interface ChatMessageModel {
    id: number;
    chatRoom: ChatRoomModel;
    user: UserModel;
    message: string;
    deleted?: boolean;
    edited?: boolean;
    createdAt: Date;
    updatedAt?: Date;
    deletedAt?: Date;
    unread?: boolean;
}
