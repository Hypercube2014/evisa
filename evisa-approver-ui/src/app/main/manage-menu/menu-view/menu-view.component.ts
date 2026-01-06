import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent'

@Component({
  selector: 'app-menu-view',
  templateUrl: './menu-view.component.html',
  styleUrls: ['./menu-view.component.css']
})
export class MenuViewComponent extends BaseComponent implements OnInit {

  public menuId: any;
  menuDetails: any={};

  constructor(inj: Injector) {
    super(inj)
    this.activatedRoute.params.subscribe((params) => {
      this.menuId = params['id'];
      this.fetchMenu(this.menuId);
    })
  }

  ngOnInit(): void {
    
  }


  /****************************************************************************
    @PURPOSE      : Fetch menu details.
    @PARAMETERS   : form,menuDetails
    @RETURN       : MenuManagement object
 ****************************************************************************/
  fetchMenu(id) {
    this.commonService.callApi('viewmenu/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.menuDetails = success;
      //console.log(this.menuDetails.moduleCode);
      this.fetchModule(this.data)
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /*********************************************************/

  data: any = {};
  moduleDetails: any=[];

  fetchModule(data) {
    this.data.pageNumber = 1;
    this.data.pageSize = 10;
    this.data.moduleCode=this.menuDetails.moduleCode
    //console.log(this.data);
    
    this.commonService.callApi('searchmoduledetails', this.data, 'post', false, false, 'APPR').then(success => {
      let successData: any = success;
      //console.log(success);
      if (successData.content.length) {
      this.moduleDetails = successData.content;
      //console.log(this.moduleDetails);      
      }
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }

}
