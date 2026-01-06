import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-role-view',
  templateUrl: './role-view.component.html',
  styleUrls: ['./role-view.component.css']
})
export class RoleViewComponent extends BaseComponent implements OnInit {

  public roleDetails: any = {}
  public id: number

  constructor(inj: Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      this.id = params['id'];
      this.getRoleDetails(this.id)
    })
  }

  ngOnInit(): void {
  }

  /****************************************************************************
  @PURPOSE      : To retrive Role Details by ID.
  @PARAMETERS   : ID
  @RETURN       : Details of Role Code
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

}
