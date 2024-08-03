import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserModel } from '@app/models';
import { jwtDecode } from 'jwt-decode';
import { environment } from '@env/environment';
import { map, Observable, of, tap } from 'rxjs';
import { LoginResponseModel } from '@app/models/login-response.model';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    constructor(private httpClient: HttpClient) {}

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    setToken(token: string): void {
        localStorage.setItem('token', token);
    }

    removeToken(): void {
        localStorage.removeItem('token');
    }

    getCurrentUser(): Observable<UserModel | null> {
        const token = this.getToken();

        console.log('token', token);

        if (!token) {
            return of(null);
        }

        try {
            const decoded = jwtDecode(token);
            const username = decoded['sub'];
            return this.httpClient.get<UserModel>(`${environment.apiEndpoint}/api/auth/user/${username}`);
        } catch (error) {
            console.error(error);
            return of(null);
        }
    }

    signup(username: string, password: string): Observable<any> {
        return this.httpClient.post(`${environment.apiEndpoint}/api/auth/signup`, { username, password });
    }

    login(username: string, password: string): Observable<LoginResponseModel> {
        const observable = this.httpClient.post<LoginResponseModel>(`${environment.apiEndpoint}/api/auth/login`, { username, password })

        return observable.pipe(
            map((response: LoginResponseModel) => {
                this.setToken(response.token);
                response.username = this.getUsernameFromToken(response.token);
                return response;
            })
        );
    }

    logout() {
        this.removeToken();
        window.location.reload();
    }

    private getUsernameFromToken(token: string): string | undefined {
        try {
            const decoded = jwtDecode(token);
            return decoded['sub'];
        } catch (error) {
            console.error(error);
            return '';
        }
    }
}
