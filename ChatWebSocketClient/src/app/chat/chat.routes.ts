import { Route } from '@angular/router';
import { ChatLayoutComponent } from './chat-layout/chat-layout.component';
import { ChatPageComponent } from './chat-page/chat-page.component';

export const CHAT_ROUTES = {
    path: '',
    component: ChatLayoutComponent,
    children: [
        {
            path: '',
            component: ChatPageComponent
        }
    ]
} as Route;
