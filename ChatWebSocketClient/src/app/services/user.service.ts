import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PreferencesRequestModel } from '@app/models';
import { UserPreferences } from '@app/models/UserPreferences.model';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    constructor(private httpClient: HttpClient) {}

    updatePreferences(preferences: PreferencesRequestModel): Observable<UserPreferences> {
        return this.httpClient.put(`${environment.apiEndpoint}/api/user/preferences`, preferences);
    }
}
