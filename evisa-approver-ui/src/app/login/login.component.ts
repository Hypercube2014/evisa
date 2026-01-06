import { Component, OnInit, Injector, Inject, Renderer2, Input, EventEmitter, Output } from '@angular/core';
import { BaseComponent } from './../common/commonComponent';
import { EnvService } from '../common/env.service';
import { TranslateService } from '@ngx-translate/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { DOCUMENT, } from '@angular/common';
import { CaptchaService } from './../common/captcha/captcha.service'
import { HttpHeaders, HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent extends BaseComponent implements OnInit {


  @Input("config") config: any = {};
  @Output() captchaCode = new EventEmitter();
  code: any = null;
  resultCode: any = null;

  public registerDetails: any = {
    'organisation': false
  };
  public loginDetails: any = {};
  public genderList = [
    {
      code: 'M',
      desc: 'Male'
    },
    {
      code: 'F',
      desc: 'Female'
    }
  ]
  public buildDate: any;
  public buildVersion: any;
  public submitted: boolean = false;
  @Inject(DOCUMENT) private document: Document
  constructor(inj: Injector, private envservice: EnvService, public translate: TranslateService, private ngxLoader: NgxUiLoaderService
    , public _http: HttpClient, private captchaService: CaptchaService,
    private renderer: Renderer2) {
    super(inj);
    translate.use(localStorage.getItem('Language'));
    //console.log(this.getToken('Language'))
    this.buildDate = this.envservice.build_Date;
    this.buildVersion = this.envservice.build_Version;
    // this.translate.addLangs(['en', 'fr']);
    // this.translate.setDefaultLang('en');

    // const browserLang = this.translate.getBrowserLang();
    // this.translate.use(browserLang.match(/en|fr/) ? browserLang : 'en');
    // localStorage.setItem("Language", browserLang.match(/fr|fr-FR/) ? 'fr' : 'en')
    // this.setToken("Language", browserLang.match(/fr|fr-FR/) ? 'fr' : 'en');
    window.addEventListener("beforeunload", (event) => {
      event.preventDefault();
      localStorage.removeItem("evisaCount");
      localStorage.removeItem("accessToken");
     

    })
    this.translate.get('DEFAULT_TITLE').subscribe((translatedTitle) => {
      this.titleService.setTitle(translatedTitle);
    })
  }

  ngOnInit(): void {
    this.renderer.addClass(document.body, 'login');
    if (window.opener == null) {
      localStorage.removeItem("evisaCount")
      this.router.navigate(["/home"])
    }
  }



  /****************************************************************************
     @PURPOSE      : user Login
     @PARAMETERS   : form,formdata
     @RETURN       : NA
  ****************************************************************************/
  public successData: any
  userLogin(form, loginDetails) {

    if (form.valid) {
      if (localStorage.getItem('accessToken')) {
        localStorage.setItem("isguilty", "YES");
        this.router.navigate(["/error-page"]);
      } else {
        this.ngxLoader.start();
        // this.loading = true;
        setTimeout(() => {
          this.commonService.callApi('validateemployee', loginDetails, 'post', false, true, 'LOG').then(success => {
            this.successData = success;
            //console.log(this.successData);

            if (this.successData.status === "SUCCESS") {
              if (this.successData.changePasswordRequired) {
                this.setToken("cpflag", this.successData.changePasswordRequired);
                this.setToken('username', this.successData.username);
                this.router.navigate(["/update-password"]);
                this.ngxLoader.stop();
              } else {
                this.ngxLoader.stop();
                this.setToken("accessToken", "Bearer" + ' ' + this.successData.accessToken);
                localStorage.setItem("accessToken", this.successData.accessToken);
                this.setToken('Uname', this.successData.completeName);
                this.setToken('username', this.successData.username);
                this.setToken("Role", this.successData.roles);
                this.setToken('profilePic', this.successData.profilePic);
                if (this.successData.roles === 'ADM') {
                  this.router.navigate(['/main/employee-mgnt']);
                } else if(this.successData.roles === 'DMM'){
                  this.router.navigate(['/main/dashboardimo']);
                }else{
                  this.router.navigate(['/main/dashboard'])
                }
                document.querySelector('body').classList.remove('login');
              }

            } else {
              // this.loading = false;
              this.ngxLoader.stop();
              this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error');
            }
          }
          ).catch(e => {
            //console.log(e);
            this.ngxLoader.stop();
            this.toastr.errorToastr(e.error.statusDescription, 'Oops!')
          });
        }, 1000)
      }

    } else {
      //console.log("Invalid")
    }
  }

  backToLogin(type) {
    if (type === 'R') {
      this.isLogin = true;
    } else {
      this.isLogin = true;
      this.isForgot = false;
    }

  }
  /****************************************************************************/


  // translateLang(language) {
  //   localStorage.setItem("Language", language)
  //   this.setToken('Language', language)
  // }
  public isForgot: boolean = false;
  public isLogin: boolean = true;
  ipAddress: any
  forgotPassword() {
    this.isForgot = true;
    this.isLogin = false;

    this._http.get("http://api.ipify.org/?format=json").subscribe((res: any) => {
      this.ipAddress = res.ip;
    });


    if (this.config) {
      if (!this.config.font || !this.config.fontsize) {
        this.config["fontsize"] = "40px";
      }
      if (!this.config.font || !this.config.fontfamily) {
        this.config["fontfamily"] = "Arial";
      }
      if (!this.config.strokeColor) {
        this.config["strokeColor"] = "#f20c6c";
      }
      if (!this.config.length) {
        this.config["length"] = 6;
      }
      if (!this.config.cssClass) {
        this.config["cssClass"] = '';
      }

      if (!this.config.type) {
        this.config["type"] = 1;
      }

      if (!this.config.back || !this.config.backstroke) {
        this.config["backstroke"] = "#2F9688";
      }
      if (!this.config.back || !this.config.backsolid) {
        this.config["backsolid"] = "#f2efd2";
      }

      this.createCaptcha();
    }
  }
  createCaptcha() {
    this.passwordDetails.captch_input = ''
    switch (this.config.type) {
      case 1: // only alpha numaric degits to type
        let char =
          Math.random()
            .toString(24)
            .substring(2, this.config.length) +
          Math.random()
            .toString(24)
            .substring(2, 4);
        this.code = this.resultCode = char.toUpperCase();

        break;
      case 2: // solve the calculation 
        let num1 = Math.floor(Math.random() * 99);
        let num2 = Math.floor(Math.random() * 9);
        let operators = ['+', '-'];
        let operator = operators[(Math.floor(Math.random() * operators.length))];
        this.code = num1 + operator + num2 + '=?';
        this.resultCode = (operator == '+') ? (num1 + num2) : (num1 - num2);
        break;
    }
    setTimeout(() => {

      let captcahCanvas: any = document.getElementById("captcahCanvas");
      var ctx = captcahCanvas.getContext("2d");
      ctx.fillStyle = this.config.backsolid;
      ctx.fillRect(0, 0, captcahCanvas.width, captcahCanvas.height);
      ctx.beginPath();
      captcahCanvas.style.letterSpacing = 15 + "px";
      ctx.font = this.config.fontsize + " " + this.config.fontfamily;
      ctx.fillStyle = "black";
      ctx.textBaseline = "middle";
      ctx.fillText(this.code, 40, 50);
      if (this.config.backstroke) {
        ctx.strokeStyle = this.config.backstroke;
        for (var i = 0; i < 150; i++) {
          ctx.moveTo(Math.random() * 300, Math.random() * 300);
          ctx.lineTo(Math.random() * 300, Math.random() * 300);
        }
        ctx.stroke();
      }

      // this.captchaCode.emit(char);
    }, 100);
  }

  playCaptcha() {
    var msg = new SpeechSynthesisUtterance(this.code.split('').join(' '));
    msg.pitch = 1;
    window.speechSynthesis.speak(msg);
  }

  isValidCaptcha: boolean = false;
  checkCaptcha() {
    //console.log(this.resultCode, "result code");

    if (this.passwordDetails.captch_input != this.resultCode) {
      this.isValidCaptcha = false
    } else {
      this.isValidCaptcha = true;
    }
  }




  /****************************************************************************
     @PURPOSE      : forger password
     @PARAMETERS   : form,formdata
     @RETURN       : NA
  ****************************************************************************/
  passwordDetails: any = {}
  userForgotPassword(form, passwordDetails) {    
    if (form.valid && this.isValidCaptcha) {
      passwordDetails.address = this.ipAddress
      if (passwordDetails.personalEmail != undefined) {
        passwordDetails.emailid = passwordDetails.emailid.toLowerCase()
      }
      //console.log(passwordDetails);
      this.commonService.callApi('employee/forgetpassword/' + passwordDetails.emailid + '/' + passwordDetails.address, '', 'get', false, true, 'APPR').then(success => {
        let successData: any = success
        if (successData.apiStatusCode === "SUCCESS") {
          this.toastr.successToastr(successData.apiStatusDesc, 'Success')
          form.reset();
          this.isForgot = false;
          this.ngxLoader.stop();
          this.isLogin = true;
        } else {
          this.ngxLoader.stop();
          this.toastr.errorToastr(successData.apiStatusDesc, 'Error');
        }
      }).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    } else {
      this.submitted = true
    }
  }
  /************************************************************************************/


  password: any;
  show = false;
  
  onClick(type) {
    //console.log(type);
    if (type === 'secret') {
      if (this.password === 'password') {
        this.password = 'text';
        this.show = true;
      } else {
        this.password = 'password';
        this.show = false;
      }
    }
    
    
  }

}
