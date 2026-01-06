import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent'

@Component({
  selector: 'app-menu-details',
  templateUrl: './menu-details.component.html',
  styleUrls: ['./menu-details.component.css']
})
export class MenuDetailsComponent extends BaseComponent implements OnInit {

  public menuDetails: any = {};
  public isNew: boolean = false;
  public menuId: any;
  submitted: boolean = false;

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
    super(inj)


    this.activatedRoute.params.subscribe((params) => {
      if (params['id'] === 'new') {
        this.isNew = true;
      } else {
        this.menuId = params['id']
        this.fetchMenu(this.menuId)
        this.isNew = false
      }
    })
  }

  ngOnInit(): void {

    this.fetchModule()
  }

  /****************************************************************************
   @PURPOSE      : To submit menu details.
   @PARAMETERS   : form,menuDetails
   @RETURN       : NA
****************************************************************************/

  public successData: any;
  submitForm(form, menuDetails) {
    if (this.isNew) {
      if (form.valid) {
        this.menuDetails.createdBy = this.getToken('username')
        this.commonService.callApi('savemenu', menuDetails, 'post', false, false, 'APPR').then(success => {
          this.successData = success;
          if (this.successData.apiStatusCode === "SUCCESS") {
            this.router.navigate(["/main/menu/menu-list"]);
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
      this.menuDetails.updatedBy = this.getToken('username');
      this.menuDetails.menuId = this.menuId;
      this.commonService.callApi('updatemenu', menuDetails, 'put', false, false, 'APPR').then(success => {
        this.successData = success;
        if (this.successData.apiStatusCode === "SUCCESS") {
          this.router.navigate(["/main/menu/menu-list"]);
          this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
        } else {
          this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error')
        }
      }).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }
  }
  /**********************************************************************************/



  /****************************************************************************
    @PURPOSE      : Fetch menu details.
    @PARAMETERS   : form,menuDetails
    @RETURN       : MenuManagement object
 ****************************************************************************/
  fetchMenu(id) {
    this.commonService.callApi('viewmenu/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.menuDetails = success;
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /******************************************************************************/



  data: any = {};
  moduleDetails: any = [];

  fetchModule() {
    this.data.pageNumber = 1;
    this.data.pageSize = 10;
    this.commonService.callApi('searchmoduledetails', this.data, 'post', false, false, 'APPR').then(success => {
      this.moduleDetails = success;
      //console.log(this.moduleDetails);
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
}
