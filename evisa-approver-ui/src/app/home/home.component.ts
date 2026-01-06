import { Component, OnInit, Injector, Renderer2 } from '@angular/core';
import { BaseComponent } from './../common/commonComponent';
import { TranslateService } from '@ngx-translate/core';
import { EnvService } from '../common/env.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent extends BaseComponent implements OnInit {

  public homeInfo: any = {};
  constructor(inj: Injector, public translate: TranslateService, private renderer: Renderer2, private envservice: EnvService) {
    super(inj);


    const browserLang = translate.getBrowserLang();
    this.homeInfo.lang = browserLang;
    /* translate.use(browserLang.match(/en|fr/) ? browserLang : 'en');
    localStorage.setItem("Language", browserLang.match(/fr|fr-FR/) ? 'fr' : 'en')
    this.setToken("Language", browserLang.match(/fr|fr-FR/) ? 'fr' : 'en') */
    translate.use(browserLang.match(/en|fr/) ? 'fr' : 'fr');
    localStorage.setItem("Language", browserLang.match(/fr|fr-FR/) ? 'fr' : 'fr')
    this.setToken("Language", browserLang.match(/fr|fr-FR/) ? 'fr' : 'fr')
    this.translate.get('DEFAULT_TITLE').subscribe((translatedTitle) => {
      this.titleService.setTitle(translatedTitle);
    })
  }
  public buildVersion
  public buildDate

  ngOnInit(): void {
    this.renderer.removeClass(document.body, 'login');
    this.buildVersion = this.envservice.build_Version;
    this.buildDate = this.envservice.build_Date

    this.loadScript('assets/home/js/theme-core.js');
  }

  public windowObjectReference = null;
  public evisaCount: any;
  public routerUrl: any;
  launchApplication() {

    if (localStorage.getItem("isguilty") == "YES") {
      alert("You have been caught!! close all the windows and clear the browser cache to proceed");
      return 0;
    }


    this.routerUrl = window.location.href.split('#')[0];
    let endUrl = this.routerUrl + '#/login';

    this.evisaCount = localStorage.getItem("evisaCount")
    if (this.evisaCount != null) {
      this.evisaCount = Number(this.evisaCount) + 1;

    } else {
      this.evisaCount = 1;
    }
    // this.setToken("evisaCount", this.evisaCount);
    localStorage.setItem("evisaCount", this.evisaCount)
    if ((this.windowObjectReference == null || this.windowObjectReference.closed) && this.evisaCount == 1) {

      this.windowObjectReference = window.open(endUrl, "myWindow", "width=" + screen.width + ",height=" + screen.height + ",left=0,top=0,resizable=no,scrollbars=yes,toolbar=no,menubar=no,titlebar=no");
    } else if (this.windowObjectReference != null && !this.windowObjectReference.closed) {
      this.windowObjectReference.focus();
    } else if (this.evisaCount > 0) {
        this.windowObjectReference = window.open(endUrl, "myWindow", "width=" + screen.width + ",height=" + screen.height + ",left=0,top=0,resizable=no,scrollbars=yes,toolbar=no,menubar=no,titlebar=no");
    //  alert("Application is already opened in another browser or tab. If not opened, clear the browser cache and try again.\n L'application est déjà ouverte dans un autre navigateur ou un autre onglet. Si elle n’est pas ouverte, videz le cache de votre navigateur et réessayez'");
    }

  }



  translateLanguage(language) {
    this.setToken("Language", language);
    localStorage.setItem("Language", language)
  }


  public loadScript(url: string) {
    const body = <HTMLDivElement>document.body;
    const script = document.createElement('script');
    script.innerHTML = '';
    script.src = url;
    script.async = false;
    script.defer = true;
    body.appendChild(script);
  }

  sendMessage: any = {};
  submitted: boolean = false;
  submitForm(form, sendMessage) {
    this.submitted = true;
    if (form.valid) {
      //console.log(sendMessage);
    } else {
      this.submitted = true
    }
  }
}
