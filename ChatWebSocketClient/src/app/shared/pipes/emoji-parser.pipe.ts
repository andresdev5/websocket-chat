import { Pipe, PipeTransform } from '@angular/core';
import uEmojiParser from 'universal-emoji-parser';
import escapeStringRegexp from 'escape-string-regexp';

@Pipe({
    standalone: true,
    name: 'emojiParser'
})
export class EmojiParserPipe implements PipeTransform {
    private readonly asciiEmojis: any[] = [
        [[':)'], '😊'],
        [[':D'], '😀'],
        [[':('], '😞'],
        [[':o'], '😲'],
        [[':p'], '😛'],
        [[';)'], '😉'],
        [[':s'], '😖'],
        [[':x'], '😵'],
        [[':z'], '🤐'],
        [[':o'], '😮'],
        [[':3'], '🐱'],
    ]

    transform(value: any): any {
        for (const [expressions, replace] of this.asciiEmojis) {
            for (const expression of expressions) {
                const regex = new RegExp([
                    '^((<[^>]+>.*)|\s+)',
                    escapeStringRegexp(expression),
                    '(.*<\/[^>]+>|\s+|$)'
                ].join(''), 'gi');

                value = value.replace(regex, `$1${replace}$3`);
            }
        }

        return uEmojiParser.parse(value, {
            parseToHtml: true,
        });
    }
}
