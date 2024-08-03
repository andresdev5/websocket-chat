import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
    standalone: true,
    selector: 'chat-layout',
    templateUrl: './chat-layout.component.html',
    imports: [
        RouterModule,
    ]
})
export class ChatLayoutComponent implements OnInit {
    constructor() { }
    ngOnInit(): void { }
}
