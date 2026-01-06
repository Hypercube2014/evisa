import { Component, Injector, OnInit } from '@angular/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-role-list',
  templateUrl: './role-list.component.html',
  styleUrls: ['./role-list.component.css']
})
export class RoleListComponent extends BaseComponent implements OnInit {


  public totalItem: any;
  pagesize: number = 10

  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }

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

  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj);
  }

  ngOnInit(): void {
    this.getRolesList(this.data);
  }

  refreshPage() {
    this.getRolesList(this.data);
  }

  /****************************************************************************
    @PURPOSE      : To retrive List of Roles.
    @PARAMETERS   : data
    @RETURN       : List of Role Details
 ****************************************************************************/
  rolesList: any = []
  public number: 0;
  public size: 0;
  public noofelements: 0;
  public totalElements: 0;
  public loading: boolean = false;
  getRolesList(data) {
    this.loading = true
    this.ngxLoader.start()
    setTimeout(() => {
      this.commonService.callApi('searchroledetails', data, 'post', false, false, 'APPR').then(success => {
        let successData: any = success
        this.totalItem = successData.totalElements;
        this.number = successData.number;
        this.size = successData.size;
        this.loading = false;
        this.ngxLoader.stop();
        this.noofelements = successData.numberOfElements;
        this.totalElements = successData.totalElements;
        if (successData.content.length) {
          this.rolesList = successData.content;
        } else {
          this.rolesList = []
        }

      }).catch(e => {
        this.ngxLoader.stop()
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }, 1500)
  }

  /****************************************************************************/

  /****************************************************************************
      @PURPOSE      : filters.
      @PARAMETERS   : NA
      @RETURN       : NA
  ****************************************************************************/

  applyFilter() {
    if (JSON.stringify(this.filterObj) == "{}") {
      if (localStorage.getItem('Language') === 'en') {
        this.toastr.errorToastr("Please Select Any Value  Data to Filter", 'Oops!');
        return 0;
      } else {
        this.toastr.errorToastr("Veuillez filtrer par categorie", 'Oops!');
        return 0;
      }

    }
    this.isFilterApplied = true;
    this.currentPage = 1;
    this.filterObj.pageNumber = 1;
    this.filterObj.pageSize = this.pagesize;
    this.getRolesList(this.filterObj);
  }

  resetFilter() {
    this.isFilterApplied = false;
    this.currentPage = 1;
    this.filterObj = {};
    this.getRolesList(this.data);
  }
  /****************************************************************************/

  /****************************************************************************
      @PURPOSE      : To retrive the roles List
      @PARAMETERS   : pageNumber,PageSize,loggedinuser
      @RETURN       : NA
   ****************************************************************************/
  public currentPage = 1;
  public showBoundaryLinks = true;
  public rangeList = [5, 10, 25, 100];
  public isFilterApplied: boolean = false;
  public filterObj: any = {};

  pageChanged(e) {
    if (this.isFilterApplied) {
      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      this.getRolesList(this.filterObj);
    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getRolesList(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      //console.log(e)
      this.filterObj.pageSize = e;
      this.getRolesList(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getRolesList(this.data);
    }
  }
  /****************************************************************************/

}
