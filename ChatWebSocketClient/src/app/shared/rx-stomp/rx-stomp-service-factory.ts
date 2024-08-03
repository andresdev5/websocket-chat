import { RxStompService } from './rx-stomp.service';
import { CustomRxStompConfig } from './rx-stomp.config';

export function rxStompServiceFactory() {
    const rxStomp = new RxStompService();

    rxStomp.configure(CustomRxStompConfig);
    rxStomp.activate();
    return rxStomp;
}
