import { Component, OnInit, PLATFORM_ID, Injector, NgZone, APP_ID } from '@angular/core';
import { TransferState, makeStateKey, Title, Meta } from '@angular/platform-browser';
import { isPlatformBrowser, isPlatformServer } from '@angular/common';
import { Router, ActivatedRoute, NavigationStart } from "@angular/router";
import { CommonService } from './common.service';
import { ErrorMessages } from './errorMessages';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { Broadcaster } from "../common/BroadCaster";
import { ToastrManager } from 'ng6-toastr-notifications';

import { TranslateService } from '@ngx-translate/core';

// import { Broadcaster } from "../common/BroadCaster";
// import swal from 'sweetalert2';


@Component({
  selector: 'parent-comp',
  template: ``,
  providers: []
})

export class BaseComponent {

  constructor(injector: Injector) {

    this.router = injector.get(Router)
    this.platformId = injector.get(PLATFORM_ID)
    this.appId = injector.get(APP_ID)
    this.commonService = injector.get(CommonService)
    this.errorMessage = injector.get(ErrorMessages)
    this.http = injector.get(HttpClient)
    this.titleService = injector.get(Title)
    this.metaService = injector.get(Meta)
    this.activatedRoute = injector.get(ActivatedRoute)
    this.modalService = injector.get(BsModalService)
    this.brodcaster = injector.get(Broadcaster);
    this.toastr = injector.get(ToastrManager);
    this.translate = injector.get(TranslateService)
    this.translate.use(localStorage.getItem('Language'));
    // this.brodcaster = injector.get(Broadcaster)

    this.baseUrl = this.commonService._apiUrl;
    ////console.log('Your current Environment is :', environment)
    //this.getMasterDetails();
  }
  public http: HttpClient;
  public modalService: BsModalService
  public activatedRoute: ActivatedRoute;
  public errorMessage: ErrorMessages
  // public swal = swal;
  public titleService: Title
  public metaService: Meta
  public platformId: any;
  public appId: any;
  public brodcaster: Broadcaster;
  public toastr: ToastrManager
  public router: Router;
  public commonService: CommonService;
  public baseUrl;
  public Environments = [];
  public Categories = [];
  public Status = [];
  public projects = [];
  public AlertGroups = [];
  public Fprojects = [];
  public FCategories = [];
  public FEnvironments = [];
  public FStatus = [];
  public translate: TranslateService

  // *************************************************************//
  //@Purpose : We can use following function to use localstorage
  //*************************************************************//
  setToken(key, value) {
    if (isPlatformBrowser(this.platformId)) {
      window.sessionStorage.setItem(key, value);
    }
  }
  getToken(key) {
    if (isPlatformBrowser(this.platformId)) {
      return window.sessionStorage.getItem(key);
    }
  }
  removeToken(key) {
    if (isPlatformBrowser(this.platformId)) {
      window.sessionStorage.removeItem(key);
    }
  }
  clearToken() {
    if (isPlatformBrowser(this.platformId)) {
      window.sessionStorage.clear()
    }
  }
  //*************************************************************//

  //*************************************************************//
  //@Purpose : We can use following function to use Toaster Service.
  //*************************************************************//
  popToast(type, title, refer?) {
    if (refer) {
      // swal.fire({
      //   position: 'center',
      //   type: type,
      //   text: title,
      //   showConfirmButton: false,
      //   timer: 4000,
      //   customClass :'custom-toaster'
      // })
    } else {
      // swal.fire({
      //   position:'center',
      //   type: type,
      //   text: title,
      //   showConfirmButton: false,
      //   timer: 1800,
      //   customClass :'custom-toaster'
      // })
    }

  }

  /****************************************************************************
  @PURPOSE      : To restrict or allow some values in input.
  @PARAMETERS   : $event
  @RETURN       : Boolen
  ****************************************************************************/
  RestrictSpace(e) {
    if (e.keyCode == 32) {
      return false;
    } else {
      return true;
    }
  }

  AllowNumbers(e) {
    var input;
    if (e.metaKey || e.ctrlKey) {
      return true;
    }
    if (e.which === 32) {
      return false;
    }
    if (e.which === 0) {
      return true;
    }
    if (e.which < 33) {
      return true;
    }
    if (e.which === 43 || e.which === 45) {
      return true;
    }
    input = String.fromCharCode(e.which);
    return !!/[\d\s]/.test(input);
  }

  AllowChar(e) {
    if ((e.keyCode > 64 && e.keyCode < 91) || (e.keyCode > 96 && e.keyCode < 123) || e.keyCode == 8) {
      return true
    } else {
      return false
    }
  }
  /****************************************************************************/

  /****************************************************************************
@PURPOSE      : To Allow Characters and Undescore in Code.
@PARAMETERS   : Event
@RETURN       : Booelan value
****************************************************************************/
  result: boolean
  AllowCharAndUnderScore(e) {
    if ((e.keyCode > 64 && e.keyCode < 91) || (e.keyCode > 96 && e.keyCode < 123) || e.keyCode == 8 || e.keyCode == 189 || e.keyCode == 16 || e.keyCode == 9) {
      this.result = false
      return true
    } else {
      this.result = true
      return false
    }
  }
  /****************************************************************************/

  /****************************************************************************
@PURPOSE      : To Allow Characters and Numbers.
@PARAMETERS   : Event
@RETURN       : Booelan value
****************************************************************************/

  AllowCharAndNumbers(e) {

    if ((e.keyCode > 64 && e.keyCode < 91) || (e.keyCode > 96 && e.keyCode < 123) || (e.keyCode > 47 && e.keyCode < 58) || e.keyCode == 8 || e.keyCode == 32) {
      this.result = false
      return true
    } else {
      this.result = true
      return false
    }
  }
  /****************************************************************************/

  logout() {
    this.clearToken()
    localStorage.removeItem("accessToken")
    //this.removeCookie('authToken')
    //console.log('logout called ') //call logout api here.
    this.router.navigate(["/login"]);
  }

  /****************************************************************************
  @PURPOSE      : To show validation message
  @PARAMETERS   : <field_name, errorObj?>
  @RETURN       : error message.
  ****************************************************************************/
  showError(field, errorObj?) {
    return this.errorMessage.getError(field, errorObj)
  }
  /****************************************************************************/
  getProfile() {

    var url = this.getToken('profilePic');
    if (url == null || url == ' ' || url == 'null') {
      return 'assets/images/no-image-user.png';
    } else {
      return url;
    }
  }
  /****************************************************************************
  @PURPOSE      : To get List of Countries
  @PARAMETERS   : NA
  @RETURN       : Country List
  ****************************************************************************/
  countryList: any = []
  getCountrList() {
    this.commonService.callApi('mastercode/active/CNTRY', '', 'get', false, false, 'APPR').then(success => {
      this.countryList = success;
      this.countryList = this.countryList.masterCodeResultDTOs;
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /****************************************************************************/

  /****************************************************************************
 @PURPOSE      : To get List of Nationalities
 @PARAMETERS   : NA
 @RETURN       : Nationality List
 ****************************************************************************/
  nationalityList: any = []
  getNationalityList() {
    this.commonService.callApi('mastercode/active/NTNLT', '', 'get', false, false, 'APPR').then(success => {
      this.nationalityList = success;
      this.nationalityList = this.nationalityList.masterCodeResultDTOs;
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /****************************************************************************/


  /****************************************************************************
@PURPOSE      : To get List of Roles
@PARAMETERS   : NA
@RETURN       : Roles List
****************************************************************************/
  rolesList: any = []
  public roleDetails: any = [];
  getRoles() {
    this.commonService.callApi('activeroledetails', '', 'get', false, false, 'APPR').then(success => {
      this.rolesList = success;
      this.roleDetails = this.rolesList.find(x => x.roleCode == this.getToken('Role'));
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }



  /****************************************************************************
   @PURPOSE      : To Retrive Purpose of travel
   @PARAMETERS   : type
   @RETURN       : NA
****************************************************************************/
  public locationDetails: any;
  getLocationdetails(code) {
    this.commonService.callApi('mastercode/active/' + code, '', 'get', false, false, 'APPR').then(success => {
      let successData: any = success;
      this.locationDetails = successData.masterCodeResultDTOs;

    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });

  }
  /****************************************************************************/

  /****************************************************************************
     @PURPOSE      : To Retrive Country codes
     @PARAMETERS   : type
     @RETURN       : NA
    ****************************************************************************/
  public currencyDetails: any;
  getCurrency(code) {
    this.commonService.callApi('mastercode/active/' + code, '', 'get', false, false, 'APPR').then(success => {
      let successData: any = success;
      this.currencyDetails = successData.masterCodeResultDTOs;

    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });

  }
  /****************************************************************************/
}