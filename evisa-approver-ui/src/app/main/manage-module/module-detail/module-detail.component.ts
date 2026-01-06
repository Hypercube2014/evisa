import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent'

@Component({
  selector: 'app-module-detail',
  templateUrl: './module-detail.component.html',
  styleUrls: ['./module-detail.component.css']
})
export class ModuleDetailComponent extends BaseComponent implements OnInit {

  public moduleDetails: any = {};
  public isNew: boolean = false
  public submitted: boolean = false;
  public moduleId: any;
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
  successData: any;

  constructor(inj: Injector) {
    super(inj);


    this.activatedRoute.params.subscribe((params) => {
      if (params['id'] === 'new') {
        this.isNew = true
      } else {
        this.moduleId = params['id']
        this.fetchModule(this.moduleId)
        this.isNew = false
      }
    })
  }

  ngOnInit(): void {
  }

  /****************************************************************************
   @PURPOSE      : To submit Module details.
   @PARAMETERS   : form,formdata
   @RETURN       : NA
****************************************************************************/
  submitForm(form, moduleDetails) {
    if (this.isNew) {
      if (form.valid) {
        this.moduleDetails.createdBy = this.getToken('username');
        this.commonService.callApi('savemoduledetails', moduleDetails, 'post', false, false, "APPR").then(success => {
          this.successData = success
          if (this.successData.apiStatusCode === 'SUCCESS') {
            this.router.navigate(['/main/module/module-list']);
            this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
          } else {
            this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error')
          }
        }).catch(e => {
          this.toastr.errorToastr(e.message, 'Oops!')
        });

      } else {
        this.submitted = true;
      }
    } else {
      this.moduleDetails.moduleId = this.moduleId
      this.moduleDetails.updatedBy = this.getToken('username');
      this.commonService.callApi('updatemoduledetails', moduleDetails, 'put', false, false, 'APPR').then(success => {
        this.successData = success
        if (this.successData.apiStatusCode === 'SUCCESS') {
          this.router.navigate(['/main/module/module-list']);
          this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
        } else {
          this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error')
        }
      }).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });

    }

  }


  /****************************************************************************
    @PURPOSE      : fetch Module details.
    @PARAMETERS   : form,formdata
    @RETURN       : Module Management object
 ****************************************************************************/
  fetchModule(id) {
    this.commonService.callApi('viewmoduledetails/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.moduleDetails = success;
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }



}