import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent'

@Component({
  selector: 'app-visa-detail',
  templateUrl: './visa-detail.component.html',
  styleUrls: ['./visa-detail.component.css']
})
export class VisaDetailComponent extends BaseComponent implements OnInit {
  public value: boolean;
  public visaDetails: any = {};
  public submitted: boolean = false;
  public isNew: boolean = false;
  visaId: any;
  currencyData: any = [];


  constructor(inj: Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      if (params['id'] === 'new') {
        this.isNew = true;
      } else {
        this.visaId = params['id'];
        this.getVisaId(this.visaId);
        this.isNew = false;
      }
    })

  }
  public entryTypeDetails = [
    {
      'name': 'Single Entry',
      'code': 'S'
    },
    {
      'name': 'Multiple  Entry',
      'code': 'M'
    },
    {
      'name': 'Extension  Entry',
      'code': 'E'
    }
  ]

  public statusDetails = [
    {
      'code': 'Y',
      'name': "Active"
    },
    {
      "code": "N",
      "name": "Inactive"
    }
  ]
  public expressVisa = {
    "code": true,
    "name": "Yes"
  }


  public currencyDetails = [
    {
      "name": "DJF"
    },
    {
      "name": "USD"
    }
  ]

  ngOnInit(): void {
    this.visaDetails.currency;
    this.getCurrency('CRNCY');
  }
  onChange(event) {
    if (event.target.checked) {
      this.value = true;
    } else {
      this.value = false;
    }
  }
  

  /****************************************************************************
    @PURPOSE      : To submit visa details.
    @PARAMETERS   : form,formdata
    @RETURN       : NA
 ****************************************************************************/
  public visaData: any;

  submitForm(form, visaDetails) {
    if (this.isNew) {
      if (form.valid) {
        if (this.value) {
          this.visaDetails.expressVisaFee = this.visaDetails.expressVisaFee
        } else {
          this.visaDetails.expressVisaFee = null;
        }
        this.visaDetails.createdBy = this.getToken('username');
        this.commonService.callApi('visadetails', visaDetails, 'post', false, false, 'APPR').then(success => {
          this.visaData = success;
          if (this.visaData.apiStatusCode === "SUCCESS") {
            this.router.navigate(["/main/visa/visa-list"]);
            this.toastr.successToastr(this.visaData.apiStatusDesc, 'Success')
          } else {
            this.toastr.errorToastr(this.visaData.apiStatusDesc, 'Error')
          }
        }).catch(e => {
          this.toastr.errorToastr(e.message, 'Oops!')
        });

      } else {
        this.submitted = true;
      }
    } else {
      if (this.value === false) {
        this.visaDetails.expressVisaFee = null
      }
      this.visaDetails.visaId = this.visaId;
      this.visaDetails.updatedBy = this.getToken('username')
      this.commonService.callApi('visadetails', visaDetails, 'put', false, false, 'APPR').then(success => {
        this.visaData = success;
        if (this.visaData.apiStatusCode === "SUCCESS") {
          this.router.navigate(["/main/visa/visa-list"]);
          this.toastr.successToastr(this.visaData.apiStatusDesc, 'Success')
        } else {
          this.toastr.errorToastr(this.visaData.apiStatusDesc, 'Error')
        }
      }).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }
  }

  /***********************************************************************************************/


  /****************************************************************************
    @PURPOSE      : search the visa details based on visaId
    @PARAMETERS   :visaId
    @RETURN       : visa Details Object
 ****************************************************************************/
  public successData: any;
  getVisaId(id) {
    this.commonService.callApi('visadetails/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.visaDetails = success;
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /***********************************************************************************************/


  donotAllowSpace(e) {
    if (e.keyCode == 32) {
      return false;
    } else {
      return true
    }
  }


  public result: boolean = true;
  public select: boolean = true;
  selectMessage: string;
  allowNumberAndSpace(e, type) {
    if ((e.keyCode > 47 && e.keyCode < 58) || (e.keyCode === 190) || (e.keyCode === 8)) {
      if (type === 'V') {
        this.result = true;
        return true;
      } else {
        this.select = true;
        return true;
      }

    } else {
      if (type === 'V') {
        this.result = false;
        return false;
      } else {
        this.select = false;
        return false;
      }

    }

  }
}
