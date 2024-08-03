import { Injectable } from '@angular/core';
import { Message, MessageService } from 'primeng/api';

class ToastBuilder {
    private message: Message = {};

    public severity(severity: 'success' | 'info' | 'warn' | 'error'): ToastBuilder {
        this.message.severity = severity;
        return this;
    }

    public detail(detail: string): ToastBuilder {
        this.message.detail = detail;
        return this;
    }

    public summary(summary: string): ToastBuilder {
        this.message.summary = summary;
        return this;
    }

    public key(key: string): ToastBuilder {
        this.message.key = key;
        return this;
    }

    public closable(closable: boolean): ToastBuilder {
        this.message.closable = closable;
        return this;
    }

    public icon(icon: string): ToastBuilder {
        this.message.icon = icon;
        return this;
    }

    public styleClass(styleClass: string): ToastBuilder {
        this.message.styleClass = styleClass;
        return this;
    }

    public contentStyleClass(contentStyleClass: string): ToastBuilder {
        this.message.contentStyleClass = contentStyleClass;
        return this;
    }

    public life(life: number): ToastBuilder {
        this.message.life = life;
        return this;
    }

    public build(): Message {
        return this.message;
    }
}

@Injectable({
    providedIn: 'root',
    deps: [MessageService],
})
export class ToastService {
    public constructor(private messageService: MessageService) {}

    public builder(): ToastBuilder {
        return new ToastBuilder();
    }

    public show(message: Message): void {
        this.messageService.add(message);
    }

    public showSuccess(title: string, message: string): void {
        this.show(this.success(title, message).build());
    }

    public showError(title: string, message: string): void {
        this.show(this.error(title, message).build());
    }

    public showInfo(title: string, message: string): void {
        this.show(this.info(title, message).build());
    }

    public showWarn(title: string, message: string): void {
        this.show(this.warn(title, message).build());
    }

    public success(title: string, message: string): ToastBuilder {
        return this.builder().severity('success').summary(title).detail(message).life(4000);
    }

    public info(title: string, message: string): ToastBuilder {
        return this.builder().severity('info').summary(title).detail(message).life(4000);
    }

    public warn(title: string, message: string): ToastBuilder {
        return this.builder().severity('warn').summary(title).detail(message).life(4000);
    }

    public error(title: string, message: string): ToastBuilder {
        return this.builder().severity('error').summary(title).detail(message).life(4000);
    }
}
