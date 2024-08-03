import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
import { fab } from '@fortawesome/free-brands-svg-icons';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { ToastService } from '@app/services';
import { ConfirmationService, MessageService } from 'primeng/api';

@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        FontAwesomeModule,
        ConfirmDialogModule,
        ToastModule,
    ],
    exports: [
        CommonModule,
        FontAwesomeModule,
        ConfirmDialogModule,
        ToastModule,
    ],
    providers: [
        MessageService,
        ConfirmationService,
        ToastService
    ],
})
export class SharedModule {
    constructor(faLibrary: FaIconLibrary) {
        faLibrary.addIconPacks(
            fas,
            far,
            fab,
        );
    }
}
