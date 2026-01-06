import { Component, Injector, OnInit } from '@angular/core';
import { NgPopupsService } from 'ng-popups';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent extends BaseComponent implements OnInit {

  public totalItem: any;
  pagesize: number = 10
  searchText: {}

  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }

  constructor(inj: Injector, private ngPopups: NgPopupsService) {
    super(inj);
  }

  ngOnInit(): void {

  }
  /****************************************************************************
  @PURPOSE      : To Show popup on Clicking Reset Passsword 
  @PARAMETERS   : NA
  @RETURN       : NA
****************************************************************************/
  onResetPassword() {
    this.ngPopups.confirm('Vous êtes sûr, vous voulez réinitialiser le mot de passe?')
      .subscribe(res => {
        if (res) {
          //console.log('You clicked OK.');
        }
      });
  }
  /****************************************************************************/

  /****************************************************************************
  @PURPOSE      : To Show popup on Clicking Activate 
  @PARAMETERS   : NA
  @RETURN       : NA
****************************************************************************/
  onAcivate() {
    this.ngPopups.confirm('Vous êtes sûr, vous voulez activer?')
      .subscribe(res => {
        if (res) {
          //console.log('You clicked OK.');
        }
      });
  }
  /****************************************************************************/

  /****************************************************************************
      @PURPOSE      : To retrive the codes List
      @PARAMETERS   : pageNumber,PageSize,loggedinuse
      @RETURN       : NA
   ****************************************************************************/
  public currentPage = 1;
  public showBoundaryLinks = true;
  public rangeList = [5, 10, 25, 100];
  public isFilterApplied: boolean = false;
  public filterObj: any = {};

  pageChanged(e) {
    // this.userListData = [];
    if (this.isFilterApplied) {
      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      // this.userListData(this.filterObj);
    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      // this.userListData(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      //console.log(e)
      this.filterObj.pageSize = e;
      // this.userListData(this.filterObj);
    } else {
      this.data.pageSize = e;
      // this.userListData(this.data);
    }
  }
  /****************************************************************************/

}
