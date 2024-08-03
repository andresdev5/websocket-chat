import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptors } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { routes } from '@app/app.routes';
import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { AuthService } from './services/auth.service';
import { RxStompService, rxStompServiceFactory } from './shared/rx-stomp';

export const appConfig: ApplicationConfig = {
    providers: [
        {
            provide : HTTP_INTERCEPTORS,
            useFactory: JwtInterceptor,
            multi   : true,
        },
        {
            provide: RxStompService,
            useFactory: rxStompServiceFactory,
        },
        AuthService,
        provideHttpClient(withInterceptors([JwtInterceptor])),
        provideRouter(routes, withComponentInputBinding()),
        provideAnimationsAsync(),
    ],
};
