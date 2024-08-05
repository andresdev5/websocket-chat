import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ChatRoomModel } from '@app/models';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';
import { ChatMessageModel } from '@app/models/chat-message.model';

@Injectable({
    providedIn: 'root'
})
export class ChatRoomService {
    constructor(private httpClient: HttpClient) {}

    getChatRooms(): Observable<ChatRoomModel[]> {
        return this.httpClient.get<ChatRoomModel[]>(`${environment.apiEndpoint}/api/chat-rooms`);
    }

    getChatMessages(chatRoomId: number): Observable<ChatMessageModel[]> {
        return this.httpClient.get<ChatMessageModel[]>(`${environment.apiEndpoint}/api/chat-rooms/${chatRoomId}/messages`);
    }

    createChatRoom(chatRoom: ChatRoomModel): Observable<ChatRoomModel> {
        return this.httpClient.post<ChatRoomModel>(`${environment.apiEndpoint}/api/chat-rooms`, chatRoom);
    }
}
