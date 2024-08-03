export interface LoginResponseModel {
    token: string;
    expiresIn: number;
    username?: string;
}
