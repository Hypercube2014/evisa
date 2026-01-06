import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-codes-detail',
  templateUrl: './codes-detail.component.html',
  styleUrls: ['./codes-detail.component.css']
})
export class CodesDetailComponent extends BaseComponent implements OnInit {

  public submitted: boolean = false
  public codeDetails: any = {}
  public id: number
  public isNew: boolean = false;
  public successData: any;

  public statusDetails = [
    {
      'code': 'Y',
      'name': "Active"
    },
    {
      'code': 'N',
      'name': "Inactive"
    }
  ]


  constructor(inj: Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      if (params['id'] === 'new') {
        this.isNew = true;
      } else {
        this.id = params['id'];
        this.getCountryDetails(this.id)
        this.isNew = false;
      }
    })
  }

  codeType: any
  description: any

  ngOnInit(): void {
    this.codeType = this.getToken('codeType')
    this.description = this.getToken('description')
  }


  /****************************************************************************
  @PURPOSE      : To retrive Country Details by ID.
  @PARAMETERS   : ID
  @RETURN       : Details of Country Code
****************************************************************************/
  getCountryDetails(id) {
    this.commonService.callApi('mastercode/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.codeDetails = success;
    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /****************************************************************************/


  /****************************************************************************
   @PURPOSE      : To Submit and Edit Country details.
   @PARAMETERS   : form,countryDetails
   @RETURN       : API Status
****************************************************************************/
  submitForm(form, codeDetails) {
    if (this.isNew) {
      if (form.valid) {
        this.codeDetails.createdBy = this.getToken('username')
        this.codeDetails.codeType = this.codeType
        this.commonService.callApi('mastercode', codeDetails, 'post', false, false, 'APPR').then(success => {
          this.successData = success;
          if (this.successData.apiStatusCode === "SUCCESS") {
            this.router.navigate(["main/code-maintenance/"]);
            this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
          } else {
            this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error');
          }
        }
        ).catch(e => {
          this.toastr.errorToastr(e.message, 'Oops!')
        });

      } else {
        this.submitted = true;
      }
    } else {
      this.codeDetails.updatedBy = this.getToken('username')
      this.codeDetails.id = this.id
      this.commonService.callApi('mastercode', codeDetails, 'put', false, false, 'APPR').then(success => {
        this.successData = success;
        if (this.successData.apiStatusCode === "SUCCESS") {
          this.router.navigate(["main/code-maintenance/"]);
          this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
        } else {
          this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error');
        }
      }
      ).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }
  }
  /****************************************************************************/


}
