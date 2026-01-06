import { Component, OnInit, Injector } from '@angular/core';
import { BaseComponent } from './../common/commonComponent';
import { faqData } from '../../assets/json/faq_en';
import { TranslateService } from '@ngx-translate/core';
@Component({
  selector: 'app-faq',
  templateUrl: './faq.component.html',
  styleUrls: ['./faq.component.css']
})
export class FaqComponent extends BaseComponent implements OnInit {

  oneAtATime: boolean = true;
  public FaqList = [];
  public homeInfo: any = {}
  constructor(inj: Injector, public translate: TranslateService) {
    super(inj)
    // const browserLang = translate.getBrowserLang();
    // this.homeInfo.lang = browserLang;
    // translate.use(browserLang.match(/en|fr/) ? browserLang : 'en');
    // localStorage.setItem("Language", browserLang.match(/fr|fr-FR/) ? 'fr' : 'en')
    // this.setToken("Language", browserLang.match(/fr|fr-FR/) ? 'fr' : 'en')
    this.homeInfo.lang = this.getToken('Language');
    this.translate.get('DEFAULT_TITLE').subscribe((translatedTitle) => {
      this.titleService.setTitle(translatedTitle);
    })
  }

  ngOnInit(): void {
    // //console.log(faqData);
    this.getFaqList();
    //this.FaqList = faqData.data;
    // //console.log(this.FaqList)
  }


  translateLanguage(language) {
    this.setToken("Language", language);
    localStorage.setItem("Language", language)
  }


  public windowObjectReference = null;
  public evisaCount: any;
  public routerUrl: any;
  launchApplication(type) {
    if (type == 'L') {
      // //console.log("test")
      localStorage.setItem("getPage", 'loginTrue')
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
    }else{
      localStorage.setItem("getPage", 'signupTrue')
      // this.routerUrl = window.location.href.split('#')[0];
      let endUrl = this.routerUrl + '#/login';
      this.windowObjectReference = window.open(endUrl, "myWindow", "width=" + screen.width + ",height=" + screen.height + ",left=0,top=0,resizable=no,scrollbars=yes,toolbar=no,menubar=no,titlebar=no");
    }

    }


    getFaqList(){
      this.commonService.callApi('applicant/getFaqdetails', '', 'get', false, false, 'REG').then(success => {
        let successData: any = success;
        // //console.log(successData);
        this.FaqList=successData;
      
      }
      ).catch(e => {
        
        this.toastr.error(e.message, 'Oops!')
      });
    }


  }
