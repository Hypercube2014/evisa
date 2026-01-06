import { Component, Injector, OnInit, Renderer2 } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from './../common/commonComponent';
// import { LoginComponent } from '../login/login.component';
import { EnvService } from '../common/env.service';
// import * as $ from 'jquery';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent extends BaseComponent implements OnInit {

  // errorMessage: string = '';

  public homeInfo: any = {}
  constructor(inj: Injector, public translate: TranslateService, private renderer: Renderer2, private envservice: EnvService) {
    super(inj);
    const browserLang = translate.getBrowserLang();
    this.homeInfo.lang = browserLang;
    translate.use(browserLang.match(/en|fr/) ? browserLang : 'en');
    localStorage.setItem("Language", browserLang.match(/fr|fr-FR/) ? 'fr' : 'en')
    this.setToken("Language", browserLang.match(/fr|fr-FR/) ? 'fr' : 'en')
    this.translate.get('DEFAULT_TITLE').subscribe((translatedTitle) => {
      this.titleService.setTitle(translatedTitle);
    })
  }
  public buildVersion
  public buildDate



  noWrapSlides = true;
  showIndicator = true;
  images = [
    {
      image: 'assets/img/tourists/1.jpg',
      thumbImage: 'assets/img/tourists/1.jpg'
    },
    {
      image: 'assets/img/tourists/2.jpg',
      thumbImage: 'assets/img/tourists/2.jpg'
    }, {
      image: 'assets/img/tourists/3.jpg',
      thumbImage: 'assets/img/tourists/3.jpg'
    }, {
      image: 'assets/img/tourists/4.jpg',
      thumbImage: 'assets/img/tourists/4.jpg'
    }, {
      image: 'assets/img/tourists/5.jpg',
      thumbImage: 'assets/img/tourists/5.jpg'
    }, {
      image: 'assets/img/tourists/6.jpg',
      thumbImage: 'assets/img/tourists/6.jpg'
    },
    {
      image: 'assets/img/tourists/7.jpg',
      thumbImage: 'assets/img/tourists/7.jpg'
    },
  ];



  ngOnInit(): void {
    this.renderer.removeClass(document.body, 'login');

    this.buildVersion = this.envservice.build_Version;
    // //console.log(this.buildVersion);

    this.buildDate = this.envservice.build_Date
    this.loadScript('assets/home/js/theme-core.js');



  }

  public windowObjectReference = null;
  public evisaCount: any;
  public routerUrl: any;
  launchApplication(type) {
    if (type == 'L') {
      localStorage.setItem("getPage", 'loginTrue')
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
        //alert('Application is already opened in another browser or tab. If not opened, clear the browser cache and try again.');
      }
    } else {
      localStorage.setItem("getPage", 'signupTrue')
      this.routerUrl = window.location.href.split('#')[0];
      let endUrl = this.routerUrl + '#/login';
      this.windowObjectReference = window.open(endUrl, "myWindow", "width=" + screen.width + ",height=" + screen.height + ",left=0,top=0,resizable=no,scrollbars=yes,toolbar=no,menubar=no,titlebar=no");
    }
  }



  translateLanguage(language) {
    this.setToken("Language", language);
    localStorage.setItem("Language", language)
  }

  // loadJS(){
  //    $.getScript('src/assets/home/js/theme-core.js');
  // }

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
    if (form.valid) {
      // //console.log(sendMessage);
      this.commonService.callApi('contactus', sendMessage, 'post', false, true, 'REG').then(success => {
        let successData: any = success;
        if (successData.apiStatusCode === "SUCCESS") {
          this.toastr.success(successData.apiStatusDesc, 'Success')
          this.sendMessage = {}
          this.submitted = false
        } else {
          this.toastr.error(successData.apiStatusDesc, 'Error')
        }
      })
    } else {
      this.submitted = true
    }

  }




  validateInput(event: Event, type: 'string' | 'number' | 'address' | 'passport'): void {
    // Vérifier si l'élément cible est un input ou une textarea
    const inputElement = event.target as HTMLInputElement | HTMLTextAreaElement;
    const input = inputElement.value; // Obtenir la valeur de l'élément

    // Définir une expression régulière selon le type
    let regex: RegExp;

    switch (type) {
      case 'string': // Autoriser uniquement les lettres
        regex = /^[a-zA-Z\s-]*$/;
        break;
      case 'number': // Autoriser uniquement les chiffres
        regex = /^[0-9]*$/;
        break;
      case 'address': // Autoriser lettres, chiffres et quelques caractères spéciaux
        regex = /^[a-zA-Z0-9\s,.'-/]*$/;
        break;
      case 'passport': // Numéro de passeport
        regex = /^[A-Z0-9-]{6,12}$/;
        break;
      default:
        regex = /.*/; // Autoriser tout (fallback)
    }

    // Remplacer les caractères invalides
    if (!regex.test(input)) {
      inputElement.value = input.replace(
        type === 'string'
          ? /[^a-zA-Z\s-]/g // Pour les lettres, autoriser les espaces et les tirets
          : type === 'number'
            ? /[^0-9]/g // Pour les chiffres, supprimer les caractères non numériques
            : /[^a-zA-Z0-9\s,.'-/]/g, // Pour les adresses, supprimer les caractères non autorisés
        ''
      );
    }
  }
}
