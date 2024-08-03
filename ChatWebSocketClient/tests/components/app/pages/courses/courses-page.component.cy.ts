import { HttpClientModule } from '@angular/common/http';
import { RouterModule, RouterOutlet } from '@angular/router';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { AppComponent } from '@app/app.component';
import { SidebarComponent } from '@app/layout/sidebar/sidebar.component';
import { CoursesPageComponent } from '@app/pages';
import { CourseService, ToastService } from '@app/services';
import { SharedModule } from '@app/shared';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { MenuModule } from 'primeng/menu';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { fixturesApi } from '../../../../utils/fixtures';

describe('CoursesPageComponent', () => {
    beforeEach(() => {
        cy.intercept('GET', 'http://localhost:8002/courses', { body: fixturesApi.courses.getAll() }).as('getCourses')
        cy.intercept('DELETE', 'http://localhost:8002/courses/1', { body: '', statusCode: 200 }).as('deleteCourse');
    });

    it('mounts', () => {
        cy.mount(CoursesPageComponent, {
            imports: [
                RouterModule.forRoot([]),
                HttpClientModule,
                BrowserAnimationsModule,
                NoopAnimationsModule,
                SharedModule,
                TableModule,
                ButtonModule,
                MenuModule,
            ],
            providers: [ConfirmationService, MessageService, ToastService, CourseService],
        });

        cy.wait('@getCourses');
        cy.get('table').should('exist');
        cy.get('table tbody tr').should('have.length', 2);
        cy.get('table tbody tr:first-child td').should('have.length', 5);
        cy.get('table tbody tr:first-child td:first-child').should('contain.text', '1');
        cy.get('table tbody tr:first-child td:nth-child(2)').should('contain.text', 'Arquitectura de software');
    });
});
