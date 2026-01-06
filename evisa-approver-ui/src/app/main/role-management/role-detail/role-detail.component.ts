import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-role-detail',
  templateUrl: './role-detail.component.html',
  styleUrls: ['./role-detail.component.css']
})
export class RoleDetailComponent extends BaseComponent implements OnInit {
  public isNew: boolean = false;
  public roleDetails: any = {}
  public submitted: boolean = false
  public id: any
  public successData: any = {};

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
        this.getRoleDetails(this.id)
        this.isNew = false;
      }
    })
  }

  ngOnInit(): void {
  }

  /****************************************************************************
  @PURPOSE      : To retrive Role Details by ID.
  @PARAMETERS   : URL,data,methodname
  @RETURN       : Details of Role
  ****************************************************************************/
  getRoleDetails(id) {
    this.commonService.callApi('viewroledetails/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.roleDetails = success;
    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /****************************************************************************/

  /****************************************************************************
    @PURPOSE      : To Submit and Edit Role details.
    @PARAMETERS   : form,roleDetails
    @RETURN       : API Status
 ****************************************************************************/
  submitForm(form, roleDetails) {
    if (this.isNew) {
      if (form.valid) {
        this.roleDetails.createdBy = this.getToken('username')
        this.commonService.callApi('saveroledetails', roleDetails, 'post', false, false, 'APPR').then(success => {
          this.successData = success;
          if (this.successData.apiStatusCode === "SUCCESS") {
            this.router.navigate(["main/role-mgnt/"]);
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
      this.roleDetails.updatedBy =this.getToken('username')
      this.roleDetails.id = this.id
      this.commonService.callApi('updateroledetails', roleDetails, 'put', false, false, 'APPR').then(success => {
        this.successData = success;
        if (this.successData.apiStatusCode === "SUCCESS") {
          this.router.navigate(["main/role-mgnt/"]);
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
