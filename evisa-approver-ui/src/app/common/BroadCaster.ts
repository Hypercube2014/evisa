import { Subject } from 'rxjs';
import { Observable } from 'rxjs';
// import 'rxjs/add/operator/filter';
// import 'rxjs/add/operator/map';

interface BroadcastEvent {
    key: any;
    data?: any;
}
// *************************************************************//
//@Purpose : For Broadcasting the event
//*************************************************************//
export class Broadcaster {
    private _eventBus: Subject<BroadcastEvent>;

    constructor() {
        this._eventBus = new Subject<BroadcastEvent>();
    }

    broadcast(key: any, data?: any) {
        this._eventBus.next({ key, data });
    }

    // on<T>(key: any): Observable<T> {
    //     return this._eventBus.asObservable()
    //         .filter(event => event.key === key)
    //         .map(event => <T>event.data);
    // }
    unsubscribe() {
        this._eventBus.unsubscribe();
    }
}