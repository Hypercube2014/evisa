import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent';
import { NgxUiLoaderService } from "ngx-ui-loader";

@Component({
  selector: 'app-visa-list',
  templateUrl: './visa-list.component.html',
  styleUrls: ['./visa-list.component.css']
})
export class VisaListComponent extends BaseComponent implements OnInit {

  public successData: any = {};
  public pagesize = 10;
  public totalItem: any;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }

  public visaAllowed = [
    {
      'code': true,
      'name': "Yes"
    },
    {
      "code": false,
      "name": "No"
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
  public visaDetails: any = [];

  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj);
  }


  ngOnInit(): void {
    this.getVisaDetails(this.data);
  }

  refreshPage() {
    this.getVisaDetails(this.data);
  }


  /****************************************************************************
       @PURPOSE      : To retrive the Visa List
       @PARAMETERS   : pageNumber,PageSize
       @RETURN       : system VisaSearchDTO object
    ****************************************************************************/

  public number: 0;
  public size: 0;
  public noofelements: 0;
  public totalElements: 0;
  public loading: boolean;

  getVisaDetails(data) {
    this.loading = true;
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('searchvisadetails', data, 'post', false, false, 'APPR').then(success => {
        this.successData = success;
        this.totalItem = this.successData.totalElements;
        this.number = this.successData.number;
        this.size = this.successData.size;
        this.loading = false;
        this.ngxLoader.stop();
        this.noofelements = this.successData.numberOfElements;
        this.totalElements = this.successData.totalElements;

        if (this.successData.content.length) {
          this.visaDetails = this.successData.content;
        } else {
          this.visaDetails = []
        }
      }).catch(e => {
        this.ngxLoader.stop();
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }, 1500);
  }
  /******************************************************************************/

  /****************************************************************************
      @PURPOSE      : Applying Paginations
      @PARAMETERS   : pageNumber,PageSize,loggedinuse
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
      this.getVisaDetails(this.filterObj);
    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getVisaDetails(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      this.filterObj.pageSize = e;
      this.getVisaDetails(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getVisaDetails(this.data);
    }
  }
  /*******************************************************************************/

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
    this.getVisaDetails(this.filterObj);

  }
  resetFilter() {
    this.isFilterApplied = false;
    this.currentPage = 1;
    this.filterObj = {};
    this.getVisaDetails(this.data);
  }
}


