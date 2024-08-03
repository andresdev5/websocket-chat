import { Component, HostListener } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SharedModule } from '@app/shared/shared.module';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';

import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { MessageService } from 'primeng/api';
import { AuthModalComponent } from './shared/components';
import { NotificationService, ToastService } from './services';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [
        SharedModule,
        RouterOutlet,
        LoadingBarRouterModule,
        ToastModule,
        ConfirmDialogModule,
        DialogModule,
        AuthModalComponent,
    ],
    templateUrl: './app.component.html',
    providers: [
        ToastService,
        MessageService,
    ]
})
export class AppComponent {
    constructor() {}
}
