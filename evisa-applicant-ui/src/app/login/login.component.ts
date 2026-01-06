import {
  Component,
  OnInit,
  Injector,
  Inject,
  Renderer2,
  Input,
  Output,
  EventEmitter,
} from "@angular/core";
import { BaseComponent } from "./../common/commonComponent";
import { EnvService } from "../common/env.service";
import { TranslateService } from "@ngx-translate/core";
import { NgxUiLoaderService } from "ngx-ui-loader";
import { DOCUMENT, formatCurrency } from "@angular/common";
import { HttpHeaders, HttpClient } from "@angular/common/http";
import { CaptchaService } from "./../common/captcha/captcha.service";
@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.css"],
})
export class LoginComponent extends BaseComponent implements OnInit {
  @Input("config") config: any = {};
  @Output() captchaCode = new EventEmitter();
  code: any = null;
  resultCode: any = null;
  // declare const grecaptcha: any;

  public registerDetails: any = {
    organisation: false,
  };
  public loginDetails: any = {};
  public genderList = [
    {
      code: "M",
      desc: "Male",
    },
    {
      code: "F",
      desc: "Female",
    },
  ];

  public buildDate: any;
  public buildVersion: any;
  public submitted: boolean = false;
  @Inject(DOCUMENT) private document: Document;
  constructor(
    inj: Injector,
    private envservice: EnvService,
    public translate: TranslateService,
    private ngxLoader: NgxUiLoaderService,
    private captchaService: CaptchaService,
    public _http: HttpClient,
    // private captchaService: NgxCaptchaService,
    private renderer: Renderer2
  ) {
    super(inj);
    translate.use(localStorage.getItem("Language"));
    this.buildDate = this.envservice.build_Date;
    this.buildVersion = this.envservice.build_Version;
    // this.translate.addLangs(['en', 'fr']);
    // this.translate.setDefaultLang('en');

    // const browserLang = this.translate.getBrowserLang();
    // this.translate.use(browserLang.match(/en|fr/) ? browserLang : 'en');
    // localStorage.setItem("Language", browserLang.match(/fr|fr-FR/) ? 'fr' : 'en')
    // this.setToken('Language', browserLang.match(/fr|fr-FR/) ? 'fr' : 'en')
    //this.detectLanguage();
    window.addEventListener("beforeunload", (event) => {
      localStorage.removeItem("evisaCount");
      //localStorage.removeItem("accessToken");
    });
  }

  ngOnInit(): void {
    this.renderer.addClass(document.body, "login");
    if (this.getToken("evisaCount")) {
      // this.router.navigate(['/home'])
    } else {
      // this.router.navigate(['/home'])
    }

    if (window.opener == null) {
      localStorage.removeItem("evisaCount");
      this.router.navigate(["/home"]);
    }

    let signupPage = localStorage.getItem("getPage");
    if (signupPage == "signupTrue") {
      this.isRegister = true;
      this.isLogin = false;
    } else {
      this.isRegister = false;
      this.isLogin = true;
    }

    ////console.log(this.getToken("Language"), '123456789');
  }

  /****************************************************************************
      @PURPOSE      : user registration
      @PARAMETERS   : form,formdata
      @RETURN       : NA
   ****************************************************************************/
  public successData: any;
  public loading: boolean;
  public capitalize(sentence) {
    //
  }
  userRegister(form, regDetails) {
    (regDetails.active = "Y"), (regDetails.role = "APPLICANT");
    if (form.valid) {
      // this.loading = true;
      this.ngxLoader.start();
      setTimeout(() => {
        this.commonService
          .callApi(
            "authenticaate/registeruser",
            regDetails,
            "post",
            false,
            true,
            "REG"
          )
          .then((success) => {
            this.successData = success;
            if (this.successData.apiStatusCode === "SUCCESS") {
              this.toastr.success(this.successData.apiStatusDesc, "Success");
              form.reset();
              this.isRegister = false;
              // this.loading = false;
              this.ngxLoader.stop();
              this.isLogin = true;
            } else {
              // this.loading = false;
              this.ngxLoader.stop();
              this.toastr.error(this.successData.apiStatusDesc, "Error");
            }
          })
          .catch((e) => {
            // //console.log(e);
            // this.loading = false;
            this.ngxLoader.stop();
            this.toastr.error(e.message, "Oops!");
          });
      }, 1000);
    } else {
      // //console.log("Invalid");
    }
  }
  /****************************************************************************/

  public isOrg: boolean = false;
  isOrganisation(e) {
    if (e) {
      this.isOrg = true;
    } else {
      this.isOrg = false;
    }
    this.registerDetails.organisation = e;
  }

  public isRegister: boolean = false;
  public isForgot: boolean = false;
  public isLogin: boolean = true;
  public isTermsConditions: boolean = false;
  public isFrenchInterface: boolean = false;
  public isEnglishInterface: boolean = false; // Déterminer si l'interface est en français

  register() {
    this.isRegister = true;
    this.isLogin = false;
  }

  ipAddress: any;
  forgotPassword() {
    this.isForgot = true;
    this.isLogin = false;

    this._http
      .get("http://api.ipify.org/?format=json")
      .subscribe((res: any) => {
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
        this.config["cssClass"] = "";
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

  termsConditions() {
    this.isTermsConditions = true;
    if (this.getToken("Language") == "en") {
      this.isEnglishInterface = true;
      this.isFrenchInterface = false;
    } else {
      this.isEnglishInterface = false;
      this.isFrenchInterface = true;
    }
    this.isRegister = false;
  }

  backToLogin(type) {
    if (type === "R") {
      this.isRegister = false;
      this.isLogin = true;
      this.check = false;
    } else {
      this.isLogin = true;
      this.isForgot = false;
    }
  }

  backToRegister() {
    this.isTermsConditions = false;
    this.isFrenchInterface = false;
    this.isEnglishInterface = false;
    this.isRegister = true;
  }

  /****************************************************************************
     @PURPOSE      : user Login
     @PARAMETERS   : form,formdata
     @RETURN       : NA
  ****************************************************************************/
  userLogin(form, loginDetails) {
    if (form.valid) {
      if(localStorage.getItem("accessToken")){
        localStorage.setItem("accessToken", "");
      }
      if (localStorage.getItem("accessToken")) {
        localStorage.setItem("isguilty", "YES");
        this.router.navigate(["/error-page"]);
      } else {
        this.ngxLoader.start();
        setTimeout(() => {
          this.commonService
            .callApi("validateuser", loginDetails, "post", false, true, "LOG")
            .then((success) => {
              this.successData = success;
              if (this.successData.status === "SUCCESS") {
                if (this.successData.changePasswordRequired) {
                  this.setToken(
                    "cpflag",
                    this.successData.changePasswordRequired
                  );
                  this.setToken("username", this.successData.username);
                  this.router.navigate(["/update-password"]);
                  this.setToken("accessToken", this.successData.accessToken);
                  this.setToken("refreshToken", this.successData.refreshToken);
                  localStorage.setItem(
                    "accessToken",
                    this.successData.accessToken
                  );
                  localStorage.setItem(
                    "refreshToken",
                    this.successData.refreshToken
                  );
                  this.ngxLoader.stop();
                } else {
                  form.reset();
                  this.ngxLoader.stop();
                  this.setToken("accessToken", this.successData.accessToken);
                  this.setToken("refreshToken", this.successData.refreshToken);
                  localStorage.setItem(
                    "accessToken",
                    this.successData.accessToken
                  );
                  localStorage.setItem(
                    "refreshToken",
                    this.successData.refreshToken
                  );
                  this.setToken("profilePic", this.successData.profilePic);
                  this.setToken("role", this.successData.roles);
                  this.setToken("username", this.successData.username);
                  this.router.navigate(["/main/dashboard"]);
                }
              } else {
                this.ngxLoader.stop();
                this.toastr.error(this.successData.apiStatusDesc, "Error");
                alert("errorr");
                // //console.log(this.successData);
              }
            })
            .catch((e) => {
              // //console.log(e);
              this.loading = false;
              this.ngxLoader.stop();
              // //console.log('rsterfts', e.error.statusDescription)
              if (e.error.statusDescription == "Bad credentials") {
                if (localStorage.getItem("Language") === "en") {
                  this.toastr.error("Bad credentials", "Opps!");
                } else {
                  this.toastr.error(
                    "Mauvaises informations d'identification",
                    "Opps!"
                  );
                }
              } else if (e.error.statusDescription == "User is disabled") {
                if (localStorage.getItem("Language") === "en") {
                  this.toastr.error(
                    "Please check your email inbox in order to activate your account",
                    "Opps!"
                  );
                } else {
                  this.toastr.error(
                    "Nous vous invitons à vérifier votre boîte d'email afin d'activer votre compte",
                    "Opps!"
                  );
                }
              } else {
                if (localStorage.getItem("Language") === "en") {
                  this.toastr.error(
                    "Your account is locked, please wait 25 minutes",
                    "Opps!"
                  );
                } else {
                  this.toastr.error(
                    "Votre compte est verrouillé, veuillez attendre 25 minutess",
                    "Opps!"
                  );
                }
              }
            });
        }, 1000);
      }
    } else {
      // //console.log("Invalid");
    }
  }

  /****************************************************************************/

  // translateLang(language) {
  //   localStorage.setItem("Language", language)
  //   this.setToken('Language', language)
  // }

  public check: boolean;
  onSelect(event) {
    if (event.target.checked) {
      this.check = true;
    } else {
      this.check = false;
    }
  }

  /****************************************************************************
     @PURPOSE      : forger password
     @PARAMETERS   : form,formdata
     @RETURN       : NA
  ****************************************************************************/

  passwordDetails: any = {};

  userForgotPassword(form, passwordDetails) {
    if (form.valid && this.isValidCaptcha) {
      passwordDetails.address = this.ipAddress;

      if (passwordDetails.personalEmail != undefined) {
        passwordDetails.emailid = passwordDetails.emailid.toLowerCase();
      }
      this.commonService
        .callApi(
          "applicant/forgetpassword/" +
            passwordDetails.emailid +
            "/" +
            passwordDetails.address,
          "",
          "get",
          false,
          true,
          "REG"
        )
        .then((success) => {
          let successData: any = success;
          if (successData.apiStatusCode === "SUCCESS") {
            this.toastr.success(successData.apiStatusDesc, "Success");
            form.reset();
            this.isForgot = false;
            this.ngxLoader.stop();
            this.isLogin = true;
          } else {
            this.ngxLoader.stop();
            this.toastr.error(successData.apiStatusDesc, "Error");
          }
        })
        .catch((e) => {
          this.toastr.error(e.message, "Oops!");
        });
    } else {
      this.submitted = true;
    }
  }

  // userForgotPassword(form, passwordDetails) {
  //   if (form.valid) {
  //     this.ngxLoader.start();

  //     // Récupérer le token reCAPTCHA
  //     this.recaptchaV3Service.execute('forgot_password').subscribe({
  //       next: (token) => {
  //         const payload = {
  //           email: passwordDetails.emailid,
  //           recaptchaToken: token,
  //           address: this.ipAddress
  //         };

  //         this.commonService
  //           .callApi("applicant/forgetpassword", payload, "get", false, true, "REG")
  //           .then((success) => {
  //             // ... reste du code existant
  //             let successData: any = success;
  //             if (successData.apiStatusCode === "SUCCESS") {
  //               this.toastr.success(successData.apiStatusDesc, "Success");
  //               form.reset();
  //               this.isForgot = false;
  //               this.ngxLoader.stop();
  //               this.isLogin = true;
  //           })
  //           .catch((e) => {
  //             this.handleRecaptchaError(e);
  //           });
  //       },
  //       error: (err) => {
  //         this.handleRecaptchaError(err);
  //       }
  //     });
  //   } else {
  //     this.submitted = true;
  //   }
  // }

  // private handleRecaptchaError(error: any) {
  //   this.ngxLoader.stop();
  //   if (error?.error?.statusDescription?.toLowerCase().includes('recaptcha')) {
  //     this.translate.get('ERRORS.recaptchaFailed').subscribe((translated) => {
  //       this.toastr.error(translated, "Error");
  //     });
  //   } else {
  //     this.toastr.error(error.message, "Oops!");
  //   }
  // }

  /****************************************************************************************/
  createCaptcha() {
    this.passwordDetails.captch_input = "";
    switch (this.config.type) {
      case 1: // only alpha numaric degits to type
        let char =
          Math.random().toString(24).substring(2, this.config.length) +
          Math.random().toString(24).substring(2, 4);
        this.code = this.resultCode = char.toUpperCase();

        break;
      case 2: // solve the calculation
        let num1 = Math.floor(Math.random() * 99);
        let num2 = Math.floor(Math.random() * 9);
        let operators = ["+", "-"];
        let operator = operators[Math.floor(Math.random() * operators.length)];
        this.code = num1 + operator + num2 + "=?";
        this.resultCode = operator == "+" ? num1 + num2 : num1 - num2;
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
    var msg = new SpeechSynthesisUtterance(this.code.split("").join(" "));
    msg.pitch = 1;
    window.speechSynthesis.speak(msg);
  }

  isValidCaptcha: boolean = false;
  checkCaptcha() {
    if (this.passwordDetails.captch_input != this.resultCode) {
      this.isValidCaptcha = false;
    } else {
      this.isValidCaptcha = true;
    }
  }

  password: any = "password";
  rePassword: any = "password";
  show = false;
  reshow = false;

  onClick(type) {
    // //console.log(type);
    if (type === "secret") {
      if (this.password === "password") {
        this.password = "text";
        this.show = true;
      } else {
        this.password = "password";
        this.show = false;
      }
    } else if (type === "regPassword") {
      if (this.password === "password") {
        this.password = "text";
        this.show = true;
      } else {
        this.password = "password";
        this.show = false;
      }
    } else if (type === "regRePassword") {
      if (this.rePassword === "password") {
        this.rePassword = "text";
        this.reshow = true;
      } else {
        this.rePassword = "password";
        this.reshow = false;
      }
    }
  }

  validateInput(
    event: Event,
    type: "string" | "number" | "address" | "passport"
  ): void {
    // Vérifier si l'élément cible est un input ou une textarea
    const inputElement = event.target as HTMLInputElement | HTMLTextAreaElement;
    const input = inputElement.value; // Obtenir la valeur de l'élément

    // Définir une expression régulière selon le type
    let regex: RegExp;

    switch (type) {
      case "string": // Autoriser uniquement les lettres
        regex = /^[a-zA-Z\s-]*$/;
        break;
      case "number": // Autoriser uniquement les chiffres
        regex = /^[0-9]*$/;
        break;
      case "address": // Autoriser lettres, chiffres et quelques caractères spéciaux
        regex = /^[a-zA-Z0-9\s,.'-/]*$/;
        break;
      case "passport": // Numéro de passeport
        regex = /^[A-Z0-9-]{6,12}$/;
        break;
      default:
        regex = /.*/; // Autoriser tout (fallback)
    }

    // Remplacer les caractères invalides
    if (!regex.test(input)) {
      inputElement.value = input.replace(
        type === "string"
          ? /[^a-zA-Z\s-]/g // Pour les lettres, autoriser les espaces et les tirets
          : type === "number"
          ? /[^0-9]/g // Pour les chiffres, supprimer les caractères non numériques
          : /[^a-zA-Z0-9\s,.'-/]/g, // Pour les adresses, supprimer les caractères non autorisés
        ""
      );
    }
  }
  blockInvalidCharacters(event: KeyboardEvent): void {
    const allowedPattern = /^[a-zA-Z\d@!$*]+$/;
    const inputChar = event.key;

    // Bloque le caractère s'il ne correspond pas au modèle autorisé
    if (!allowedPattern.test(inputChar)) {
      event.preventDefault();
    }
  }
}
