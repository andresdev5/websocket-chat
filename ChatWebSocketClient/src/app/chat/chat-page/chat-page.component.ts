import { Component, computed, HostListener, OnInit, Signal, signal, ViewChild, ViewEncapsulation } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ChatRoomModel, UserModel } from '@app/models';
import { ChatMessageModel } from '@app/models/chat-message.model';
import { AuthService } from '@app/services/auth.service';
import { ChatRoomService } from '@app/services/chat-room.service';
import { InformationService } from '@app/services/information.service';
import { SharedModule } from '@app/shared';
import { UserRole, UserStatus } from '@app/shared/enums';
import { RxStompService } from '@app/shared/rx-stomp';
import { MenuItem, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { Observable, Subject, Subscription } from 'rxjs';
import { AuthModalComponent } from '@app/shared/components';
import { ButtonGroupModule } from 'primeng/buttongroup';
import { ToastModule } from 'primeng/toast';
import { NotificationService, ToastService, UserService } from '@app/services';
import { MessagesModule } from 'primeng/messages';
import { EmojiParserPipe, MarkdownParserPipe, SafeHtmlPipe } from '@app/shared/pipes';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { UserPreferences } from '@app/models/UserPreferences.model';
import { Title } from '@angular/platform-browser';
import { InputTextModule } from 'primeng/inputtext';

@Component({
    standalone: true,
    selector: 'chat-page',
    templateUrl: './chat-page.component.html',
    styleUrls: ['./chat-page.component.scss'],
    encapsulation: ViewEncapsulation.None,
    imports: [
        SharedModule,
        FormsModule,
        ReactiveFormsModule,
        TieredMenuModule,
        ButtonModule,
        DialogModule,
        AuthModalComponent,
        ButtonGroupModule,
        ToastModule,
        MessagesModule,
        EmojiParserPipe,
        MarkdownParserPipe,
        SafeHtmlPipe,
        InputTextareaModule,
        InputTextModule,
    ],
    providers: [
        ToastService,
        NotificationService,
    ]
})
export class ChatPageComponent implements OnInit {
    currentUser: UserModel | null = null;
    receivedMessages: ChatMessageModel[] = [];
    chatRooms: ChatRoomModel[] = [];
    selectedChatRoom: ChatRoomModel | null = null;
    connections: UserModel[] = [];
    unreadChannelMessages: Record<number, number> = {};
    message: string = '';
    currentUserMenu: MenuItem[] = [
        {
            label: 'Cerrar sesión',
            icon: 'pi pi-sign-out',
            command: () => this.logout()
        }
    ];
    @ViewChild('messageInput') messageInput: any;
    @ViewChild('chatContainer') chatContainer: any;
    $showLogin: Subject<void> = new Subject<void>();
    $showSignup: Subject<void> = new Subject<void>();
    $onLoggedIn: Observable<void> | null = null;
    $onSignedUp: Observable<void> | null = null;
    isDocumentHidden: boolean = false;
    totalOnlineUsers: number = 0;
    firstUnredMessage: ChatMessageModel | null = null;
    chatRoomModalIsVisible: boolean = false;
    chatRoomForm: FormGroup = new FormGroup({
        name: new FormControl('', [Validators.required]),
        description: new FormControl('', [Validators.required])
    });

    private getChatRoomsSubscription: Subscription = new Subscription();
    private stompSubscription: Subscription = new Subscription();


    constructor(
        private rxStompService: RxStompService,
        private informationService: InformationService,
        private chatRoomService: ChatRoomService,
        private authService: AuthService,
        private userService: UserService,
        private toastService: ToastService,
        private notificationService: NotificationService,
        private titleService: Title
    ) {}

    ngOnInit(): void {
        setTimeout(() => {
            this.loadConnections();
        }, 2000);

        this.loadChatRooms();

        this.chatRoomService.getChatRooms().subscribe((chatRooms) => {
            this.chatRooms = chatRooms;

            for (const chatRoom of chatRooms) {
                this.unreadChannelMessages[chatRoom.id!] = 0;
            }

            if (chatRooms.length > 0) {
                this.selectChatRoom(chatRooms[0]);
            }
        });

        this.rxStompService.watch('/topic/event').subscribe((message) => {
            console.log('/topic/event', message);
            const chatEvent = JSON.parse(message.body);

            if (chatEvent && ['CONNECTED', 'DISCONNECTED'].includes(chatEvent.event)) {
                this.loadConnections();

                if (!this.currentUser || this.currentUser.username !== chatEvent.data.username) {
                    this.receivedMessages.push({
                        id: 0,
                        chatRoom: this.selectedChatRoom!,
                        user: {
                            id: 0,
                            username: 'System',
                            role: UserRole.SYSTEM
                        },
                        message: `${chatEvent.data.username} ha ${chatEvent.event === 'CONNECTED' ? 'entrado' : 'salido'} a la sala`,
                        createdAt: new Date()
                    });

                    this.scrollChatToBottom();
                }
            }

            if (chatEvent && chatEvent.event === 'MESSAGE') {
                const roomId = parseInt(chatEvent.data['roomId']);
                const senderId = parseInt(chatEvent.data['senderId']);

                if (this.currentUser?.id && this.selectedChatRoom?.id
                    && roomId && senderId
                    && senderId !== this.currentUser?.id
                    && (roomId !== this.selectedChatRoom?.id || this.isDocumentHidden)) {
                    this.unreadChannelMessages[roomId]++;
                    this.titleService.setTitle(`(${this.unreadChannelMessages[roomId]}) Chat`);
                    const message = this.receivedMessages.find((m) => m.id === chatEvent.data['messageId']);

                    if (message) {
                        message.unread = true;

                        if (!this.firstUnredMessage) {
                            this.firstUnredMessage = message;
                        }
                    }
                }

                this.scrollChatToBottom();
            }

            if (chatEvent && chatEvent.event === 'UPDATE_USER_PREFERENCES') {
                for (const connection of this.connections) {
                    if (connection.id === chatEvent.data.userId) {
                        connection.preferences = chatEvent.data['updatedPreferences'];
                        this.updateTotalOnlineUsers();
                    }
                }
            }
        });

        this.authService.getCurrentUser().subscribe((user) => {
            this.currentUser = user;
            this.currentUserMenu = [
                {
                    label: 'Estado de conexión',
                    icon: 'pi pi-users',
                    items: [
                        {
                            'label': 'En línea',
                            'icon': 'pi pi-check',
                            'command': () => this.changeUserPreferences({ status: UserStatus.ONLINE })
                        },
                        {
                            separator: true
                        },
                        {
                            label: 'Ocupado',
                            icon: 'pi pi-times',
                            command: () => this.changeUserPreferences({ status: UserStatus.BUSY })
                        },
                        {
                            label: 'Ausente',
                            icon: 'pi pi-clock',
                            command: () => this.changeUserPreferences({ status: UserStatus.AWAY })
                        },
                        {
                            label: 'Desconectado',
                            icon: 'pi pi-power-off',
                            command: () => this.changeUserPreferences({ status: UserStatus.OFFLINE })
                        }
                    ]
                },
                {
                    label: 'Cerrar sesión',
                    icon: 'pi pi-sign-out',
                    command: () => this.logout()
                }
            ]
        });
    }

    sendMessage(): void {
        if (this.message.trim() === '') {
            this.messageInput.nativeElement.focus();
            return;
        }

        this.rxStompService.publish({
            destination: '/app/message/' + this.selectedChatRoom!.id,
            body: JSON.stringify({ message: this.message })
        })

        this.message = '';
        this.scrollChatToBottom();
    }

    selectChatRoom(chatRoom: ChatRoomModel): void {
        this.selectedChatRoom = chatRoom;
        this.subcribeToChatRoom(chatRoom.id!);
        this.chatRoomService.getChatMessages(chatRoom.id!).subscribe((messages) => {
            this.receivedMessages = messages;
            this.scrollChatToBottom();

            if (chatRoom.id! in this.unreadChannelMessages) {
                this.unreadChannelMessages[chatRoom.id!] = 0;
            }
        });
    }

    showLoginModal() {
        this.$showLogin.next();
    }

    showSignupModal() {
        this.$showSignup.next();
    }

    logout() {
        this.authService.logout();
    }

    onLoggedIn($event: any) {
        if ($event.error?.status === 401) {
            this.toastService.showError('Error', 'Usuario o contraseña incorrectos');
            return;
        }

        this.toastService.showSuccess('Bienvenido al chat', `Bienvenido ${$event.response?.username ?? ''}`);
        setTimeout(() => window.location.reload(), 1000);
    }

    onSignedUp($event: any) {
        if ($event.error?.status === 400) {
            this.toastService.showError('Error', $event.error.message ?? 'Error al registrarse');
            return;
        }

        this.toastService.showSuccess('Registro', 'Usuario registrado correctamente');
    }

    createChatRoom() {
        if (this.chatRoomForm.invalid) {
            return;
        }

        this.chatRoomService.createChatRoom({
            name: this.chatRoomForm.get('name')?.value,
            description: this.chatRoomForm.get('description')?.value
        }).subscribe({
            next: (chatRoom) => {
                this.chatRooms.push(chatRoom);
                this.chatRoomModalIsVisible = false;
                this.chatRoomForm.reset();
                this.loadChatRooms().subscribe(() => {
                    this.selectChatRoom(chatRoom);
                });
                this.toastService.showSuccess('Atención', 'Sala de chat creada correctamente');
            },
            error: (error) => {
                this.toastService.showError('Error', 'Error al crear la sala de chat');
            }
        });
    }

    private loadChatRooms(): Observable<ChatRoomModel[]> {
        this.getChatRoomsSubscription.unsubscribe();
        const observable = this.chatRoomService.getChatRooms();

        this.getChatRoomsSubscription = observable.subscribe((chatRooms) => {
            this.chatRooms = chatRooms;

            for (const chatRoom of chatRooms) {
                this.unreadChannelMessages[chatRoom.id!] = 0;
            }

            if (chatRooms.length > 0) {
                this.selectChatRoom(chatRooms[0]);
            }
        });

        return observable;
    }

    private subcribeToChatRoom(chatRoomId: number): void {
        this.stompSubscription.unsubscribe();
        this.stompSubscription = this.rxStompService.watch(`/queue/reply/${chatRoomId}`).subscribe((message) => {
            this.receivedMessages.push(JSON.parse(message.body));
            this.notificationService.sendSoundNotification();
        });
    }

    private loadConnections(): void {
        this.informationService.getConnections().subscribe((connections) => {
            this.connections = connections;
            this.updateTotalOnlineUsers();
            this.connections = this.connections.sort((a, b) => {
                if (a.username < b.username) {
                    return -1;
                }

                if (a.username > b.username) {
                    return 1;
                }

                return 0;
            });
        });
    }

    private async scrollChatToBottom() {
        await new Promise((resolve) => setTimeout(resolve, 200));
        this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
    }

    private changeUserPreferences(preferences: UserPreferences): void {
        if (!this.currentUser) {
            return;
        }

        this.userService.updatePreferences({
            userId: this.currentUser.id,
            ...{
                ...this.currentUser.preferences,
                ...preferences
            }
        }).subscribe((updated) => {
            if (this.currentUser) {
                this.currentUser.preferences = updated;
            }
        });
    }

    private updateTotalOnlineUsers(): void {
        this.totalOnlineUsers = this.connections.filter((c) => c.preferences?.status === UserStatus.ONLINE).length;
    }

    @HostListener('document:visibilitychange', ['$event'])
    onVisibilityChange(event: Event) {
        this.notificationService.setDocumentVisibility(document.visibilityState);
        this.isDocumentHidden = document.visibilityState === 'hidden';

        if (document.visibilityState === 'visible') {
            this.scrollChatToBottom();

            setTimeout(() => {
                if (this.selectedChatRoom) {
                    this.unreadChannelMessages[this.selectedChatRoom.id!] = 0;
                }

                this.titleService.setTitle('Chat');
            }, 500);

            setTimeout(() => {
                if (this.selectedChatRoom) {
                    for (const message of this.receivedMessages) {
                        if (message.chatRoom.id === this.selectedChatRoom?.id) {
                            message.unread = false;
                        }
                    }

                    this.firstUnredMessage = null;
                }
            }, 5000);
        }
    }
}
