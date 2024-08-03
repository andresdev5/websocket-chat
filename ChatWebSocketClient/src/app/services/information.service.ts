import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserModel } from '@app/models';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class InformationService {
    constructor(private httpClient: HttpClient) {}

    getConnections(): Observable<UserModel[]> {
        return this.httpClient.get<UserModel[]>(`${environment.apiEndpoint}/api/information/connections`);
    }
}
