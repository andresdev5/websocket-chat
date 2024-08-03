import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { LoginResponseModel } from '@app/models/login-response.model';
import { ToastService } from '@app/services';
import { AuthService } from '@app/services/auth.service';
import { SharedModule } from '@app/shared';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { Observable, Subject } from 'rxjs';

const validateConfirmPassword: ((passwordControl: AbstractControl) => ValidatorFn) = (passwordControl: AbstractControl) => {
    return (control: AbstractControl) => {
        console.log('validating password2', control.value, passwordControl?.value);
        if (control.value !== passwordControl?.value) {
            return { mismatch: true };
        }

        return null;
    };
}

interface OnLoggedInEvent {
    error?: Error | null;
    response: LoginResponseModel | null;
}

interface OnSignedUpEvent {
    error?: Error;
}

@Component({
    standalone: true,
    selector: 'auth-modal',
    templateUrl: './auth-modal.component.html',
    imports: [
        SharedModule,
        FormsModule,
        ReactiveFormsModule,
        ButtonModule,
        DialogModule,
        InputTextModule,
        PasswordModule,
    ],
    providers: [
        AuthService,
        MessageService,
        ToastService,
    ]
})
export class AuthModalComponent implements OnInit {
    @Input() doShowLogin: Observable<any> | null = null;
    @Input() doShowSignup: Observable<any> | null = null;
    @Output() onLoggedIn: EventEmitter<OnLoggedInEvent> = new EventEmitter<OnLoggedInEvent>();
    @Output() onSignedUp: EventEmitter<OnSignedUpEvent> = new EventEmitter<OnSignedUpEvent>();

    isLoginModalVisible: boolean = false;
    isSignupModalVisible: boolean = false;

    loginForm: FormGroup = new FormGroup({
        'username': new FormControl('', Validators.required),
        'password': new FormControl('', Validators.required),
    });

    signupForm: FormGroup;

    constructor(private authService: AuthService, private toastService: ToastService) {
        const passwordControl = new FormControl('', [
            Validators.required,
            Validators.minLength(4),
        ]);

        this.signupForm = new FormGroup({
            'username': new FormControl('', [
                Validators.required,
                Validators.minLength(4),
                Validators.maxLength(16),
                Validators.pattern(/^[a-zA-Z0-9_\-.]+$/),
            ]),
            'password': passwordControl,
            'password2': new FormControl('', [
                Validators.required,
                validateConfirmPassword(passwordControl),
            ])
        });
    }

    ngOnInit(): void {
        this.toastService.showSuccess('Bienvenido al chat', 'Bienvenido');
        this.doShowLogin?.subscribe(() => {
            this.isLoginModalVisible = true;
        });

        this.doShowSignup?.subscribe(() => {
            this.isSignupModalVisible = true;
        });
    }

    showLoginModal() {
        this.isLoginModalVisible = true;
    }

    showSignupModal() {
        this.isSignupModalVisible = true;
    }

    closeLoginModal() {
        this.isLoginModalVisible = false;
        this.loginForm.reset();
    }

    closeSignupModal() {
        this.isSignupModalVisible = false;
        this.signupForm.reset();
    }

    doLogin() {
        if (!this.loginForm.valid) {
            return;
        }

        const username = this.loginForm.get('username')?.value;
        const password = this.loginForm.get('password')?.value;

        this.authService.login(username, password).subscribe({
            next: (response) => {
                this.isLoginModalVisible = false;
                this.onLoggedIn?.next({
                    error: null,
                    response,
                });
                this.loginForm.reset();
            },
            error: (error) => {
                this.toastService.showError('Error', error.message);
                this.onLoggedIn?.next({
                    error,
                    response: null,
                });
            }
        });
    }

    doSignup() {
        if (!this.signupForm.valid) {
            return;
        }

        const username = this.signupForm.get('username')?.value;
        const password = this.signupForm.get('password')?.value;

        this.authService.signup(username, password).subscribe({
            next: () => {
                this.isSignupModalVisible = false;
                this.onSignedUp.next({});
                this.signupForm.reset();
            },
            error: (error) => {
                this.toastService.showError('Error', error.message);
                this.onSignedUp.next({ error });
            }
        });
    }
}
