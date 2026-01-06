import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { APP_ID, Component, Injector, PLATFORM_ID } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from "@angular/router";
import { ToastrService } from 'ngx-toastr';
import { Broadcaster } from "../common/BroadCaster";
import { CommonService } from './common.service';
import { ErrorMessages } from './errorMessages';

import { TranslateService } from '@ngx-translate/core';


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

    this.translate = injector.get(TranslateService)
    this.translate.use(localStorage.getItem('Language'));
    this.brodcaster = injector.get(Broadcaster);
    this.toastr = injector.get(ToastrService);
    // this.brodcaster = injector.get(Broadcaster)

    this.baseUrl = this.commonService._apiUrl;
    ////console.log('Your current Environment is :', environment)
    //this.getMasterDetails();
  }
  public http: HttpClient;
  public activatedRoute: ActivatedRoute;
  public errorMessage: ErrorMessages
  // public swal = swal;
  public titleService: Title
  public metaService: Meta
  public platformId: any;
  public appId: any;
  public brodcaster: Broadcaster;
  public toastr: ToastrService
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


  omit_special_char(event)
{   
   var k;  
   k = event.charCode;  //         k = event.keyCode;  (Both can be used)
   return((k > 64 && k < 91) || (k > 96 && k < 123) || k == 8 || k == 32 || (k >= 48 && k <= 57)); 
}
  /****************************************************************************/

  logout() {
    this.clearToken();
    localStorage.removeItem("accessToken")
    localStorage.removeItem("getPage")
    // //console.log('logout called ');
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
//For Date Format (YYYY-MM-DD)
/****************************************************************************/
  convertDateFormat(date: Date) {
    try {
      var day = date.getUTCDate();
      var month = date.getMonth() + 1;
      var year = date.getFullYear();
      var displayDay;
      var displayMonth;
      if (day < 10) {
        displayDay = '0' + '' + day.toString();
      }
      else {
        displayDay = day;
      }
      if (month < 10) {
        displayMonth = '0' + '' + month.toString();
      }
      else {
        displayMonth = month;
      }
      return year + '-' + displayMonth + '-' + displayDay;
    } catch (e) {
      return date;
    }
  }
  /****************************************************************************/


  /****************************************************************************
   @PURPOSE      : To Retrive Master Visa types
   @PARAMETERS   : type
   @RETURN       : NA
****************************************************************************/
  public visaDescription: any;
  getVisaDescription(code) {
    this.commonService.callApi('visadetailsbyvisatype/' + code, '', 'get', false, false, 'REG').then(success => {
      let successData: any = success;
      this.visaDescription = successData.description;

    }
    ).catch(e => {
      this.toastr.error(e.message, 'Oops!')
    });

  }
  /****************************************************************************/

  /****************************************************************************
   @PURPOSE      : To Retrive Country codes
   @PARAMETERS   : type
   @RETURN       : NA
****************************************************************************/
public countryDetails: any;
getCountrys(code) {
  this.commonService.callApi('mastercode/active/' + code, '', 'get', false, false, 'REG').then(success => {
    let successData: any = success;
    this.countryDetails = successData.masterCodeResultDTOs;

  }
  ).catch(e => {
    this.toastr.error(e.message, 'Oops!')
  });

}
/****************************************************************************/


 /****************************************************************************
   @PURPOSE      : To Retrive Nationality codes
   @PARAMETERS   : type
   @RETURN       : NA
****************************************************************************/
public nationalityDetails: any;
getNationalities(code) {
  this.commonService.callApi('mastercode/active/' + code, '', 'get', false, false, 'REG').then(success => {
    let successData: any = success;
    this.nationalityDetails = successData.masterCodeResultDTOs;

  }
  ).catch(e => {
    this.toastr.error(e.message, 'Oops!')
  });

}
/****************************************************************************/


/****************************************************************************
   @PURPOSE      : To Retrive Language codes
   @PARAMETERS   : type
   @RETURN       : NA
****************************************************************************/
public languageDetails: any;
getLanguages(code) {
  this.commonService.callApi('mastercode/active/' + code, '', 'get', false, false, 'REG').then(success => {
    let successData: any = success;
    this.languageDetails = successData.masterCodeResultDTOs;

  }
  ).catch(e => {
    this.toastr.error(e.message, 'Oops!')
  });

}
/****************************************************************************/


/****************************************************************************
   @PURPOSE      : To Retrive Purpose of travel
   @PARAMETERS   : type
   @RETURN       : NA
****************************************************************************/
public travelDetails: any;
getTraveldetails(code) {
  this.commonService.callApi('mastercode/active/' + code, '', 'get', false, false, 'REG').then(success => {
    let successData: any = success;
    this.travelDetails = successData.masterCodeResultDTOs;

  }
  ).catch(e => {
    this.toastr.error(e.message, 'Oops!')
  });

}
/****************************************************************************/

/****************************************************************************
   @PURPOSE      : To Retrive Purpose of travel
   @PARAMETERS   : type
   @RETURN       : NA
****************************************************************************/
public locationDetails: any;
getLocationdetails(code) {
  this.commonService.callApi('mastercode/active/' + code, '', 'get', false, false, 'REG').then(success => {
    let successData: any = success;
    this.locationDetails = successData.masterCodeResultDTOs;

  }
  ).catch(e => {
    this.toastr.error(e.message, 'Oops!')
  });

}
/****************************************************************************/


/****************************************************************************
   @PURPOSE      : To Retrive Language codes
   @PARAMETERS   : type
   @RETURN       : NA
****************************************************************************/
public maritalDetails: any;
getMarital(code) {
  this.commonService.callApi('mastercode/active/' + code, '', 'get', false, false, 'REG').then(success => {
    let successData: any = success;
    this.maritalDetails = successData.masterCodeResultDTOs;

  }
  ).catch(e => {
    this.toastr.error(e.message, 'Oops!')
  });

}
/****************************************************************************/



 /****************************************************************************
     @PURPOSE      : To Retrive Country codes
     @PARAMETERS   : type
     @RETURN       : NA
    ****************************************************************************/
     public extensionDetails: any;
     getExtension(code) {
       this.commonService.callApi('mastercode/active/' + code, '', 'get', false, false, 'REG').then(success => {
         let successData: any = success;
         this.extensionDetails = successData.masterCodeResultDTOs;
   
       }
       ).catch(e => {
         this.toastr.error(e.message, 'Oops!')
       });
   
     }
     /****************************************************************************/
}