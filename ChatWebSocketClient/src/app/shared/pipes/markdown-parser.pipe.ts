import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { marked } from 'marked';

@Pipe({
    standalone: true,
    name: 'markdownParser'
})
export class MarkdownParserPipe implements PipeTransform {
    constructor(private sanitizer: DomSanitizer) {}

    async transform(value: any) {
        const parsed = await marked(value);
        //return this.sanitizer.bypassSecurityTrustHtml(parsed);
        return Promise.resolve(parsed);
    }
}
