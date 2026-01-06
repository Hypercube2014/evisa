import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent';
import { NgxUiLoaderService } from "ngx-ui-loader";

@Component({
  selector: 'app-faq-list',
  templateUrl: './faq-list.component.html',
  styleUrls: ['./faq-list.component.css']
})
export class FaqListComponent extends BaseComponent implements OnInit {
  public pagesize = 10;
  public totalItem: any;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }
  faqdata: any = [];

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
    this.getFaqDetails(this.data);
  }

  refreshPage() {
    this.getFaqDetails(this.data);
  }



  /****************************************************************************
       @PURPOSE      : To retrive the FAQ List
       @PARAMETERS   : pageNumber,PageSize
       @RETURN       : systemFAQSearchDTO object
    ****************************************************************************/
  public number: 0;
  public size: 0;
  public noofelements: 0;
  public totalElements: 0;
  public loading: boolean;


  getFaqDetails(data) {   
    this.loading = true;
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('searchsystemfaq', data, 'post', false, false, 'APPR').then(success => {
        let successData: any = success;
        this.totalItem = successData.totalElements;
        this.number = successData.number;
        this.size = successData.size;
        this.loading = false;
        this.ngxLoader.stop();
        this.noofelements = successData.numberOfElements;
        this.totalElements = successData.totalElements;

        if (successData.content.length) {
          this.faqdata = successData.content
        }else{
          this.faqdata=[]
        }
      }).catch(e => {
        this.ngxLoader.stop();
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }, 1500);
  }

  /******************************************************************************/

  /****************************************************************************
      @PURPOSE      : Pagination
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
      this.getFaqDetails(this.filterObj);
    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getFaqDetails(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      this.filterObj.pageSize = e;
      this.getFaqDetails(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getFaqDetails(this.data);
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
    this.getFaqDetails(this.filterObj);

  }
  resetFilter() {
    this.isFilterApplied = false;
    this.currentPage = 1;
    this.filterObj = {};
    this.getFaqDetails(this.data);
  }

}

