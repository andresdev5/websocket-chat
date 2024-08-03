import { RouterModule, RouterOutlet } from '@angular/router';
import { AppComponent } from '@app/app.component';
import { SidebarComponent } from '@app/layout/sidebar/sidebar.component';
import { ToastService } from '@app/services';
import { SharedModule } from '@app/shared';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';

it('mounts', () => {
    cy.mount(AppComponent, {
        imports: [
            RouterModule.forRoot([]),
            SharedModule,
            RouterOutlet,
            SidebarComponent,
            LoadingBarRouterModule,
            ToastModule,
            ConfirmDialogModule,
            DialogModule,
        ],
        providers: [ConfirmationService, MessageService, ToastService],
    })
})
