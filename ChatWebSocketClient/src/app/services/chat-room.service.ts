import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ChatRoomModel } from '@app/models';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';
import { ChatMessageModel } from '@app/models/chat-message.model';
import { ChatRoomType } from '@app/shared/enums/chat-room-type.enum';

@Injectable({
    providedIn: 'root'
})
export class ChatRoomService {
    constructor(private httpClient: HttpClient) {}

    getChatRooms(params: { type: ChatRoomType } = { type: ChatRoomType.CHANNEL }): Observable<ChatRoomModel[]> {
        return this.httpClient.get<ChatRoomModel[]>(`${environment.apiEndpoint}/api/chat-rooms`, {
            params: {
                type: params.type.toString()
            }
        });
    }

    getChatMessages(chatRoomId: number): Observable<ChatMessageModel[]> {
        return this.httpClient.get<ChatMessageModel[]>(`${environment.apiEndpoint}/api/chat-rooms/${chatRoomId}/messages`);
    }

    createChatRoom(chatRoom: ChatRoomModel): Observable<ChatRoomModel> {
        return this.httpClient.post<ChatRoomModel>(`${environment.apiEndpoint}/api/chat-rooms/create`, chatRoom);
    }

    getUserChatRoom(ownerId: number): Observable<ChatRoomModel> {
        return this.httpClient.get<ChatRoomModel>(`${environment.apiEndpoint}/api/chat-rooms/dm/${ownerId}`);
    }
}
