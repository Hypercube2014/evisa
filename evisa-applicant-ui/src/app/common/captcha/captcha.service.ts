import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class CaptchaService {
  captchSource = new BehaviorSubject(null);
  captchStatus = this.captchSource.asObservable();
  constructor() {}

  setCaptchaStatus(code) {
    this.captchSource.next(code);
  }

  // private siteKey = '6LdGH-IqAAAAAA5MuYZKI6MNSPPvu7qrhm6WS8r9'; // Remplacez par votre clé du site reCAPTCHA v3

  // constructor() { }

  // execute(action: string): Promise<string> {
  //   return new Promise((resolve, reject) => {
  //     grecaptcha.ready(() => {
  //       grecaptcha.execute(this.siteKey, { action })
  //         .then((token: string) => {
  //           resolve(token);
  //         })
  //         .catch((error: any) => {
  //           reject(error);
  //         });
  //     });
  //   });
  // }
}
