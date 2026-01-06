import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';
import { NgxUiLoaderService } from 'ngx-ui-loader';
@Component({
  selector: 'app-search-departuree-visa',
  templateUrl: './search-departuree-visa.component.html',
  styleUrls: ['./search-departuree-visa.component.css']
})
export class SearchDepartureeVisaComponent extends BaseComponent implements OnInit {




  public pagesize = 10;
  public page = 1;
  isCollapsed = false;
  public totalItem: any;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }
  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj);
  }

  visaDetails: any = {}



  visaStatus = [
    {
      'name': 'Yes',
      'code': 'Y'
    },
    {
      'name': 'No',
      'code': 'N'
    }
  ]

  sbcoStatus = [

    {
      'code': 'PD',
      'description': 'Pending Departure'
    },

    {
      'code': 'VAL',
      'description': 'Pending Validation'
    },
  ]

  public result: boolean = false
  ngOnInit(): void {
    this.filterObj.arrDepIndicator = 'PD';
    this.isFilterApplied = true;
    this.getVisaList(this.data)

  }






  /****************************************************************************
       @PURPOSE      : To retrive the  applications List
       @PARAMETERS   : pageNumber,PageSize,loggedinuser
       @RETURN       : NA
    ****************************************************************************/
  public Applications = [];
  public number = 0;
  public size = 0;
  public noofelements = 0;
  public totalElements = 0;
  public loading: boolean;
  getVisaList(data) {
    this.result = true
    this.loading = true;
    this.ngxLoader.start();
    //console.log(data);
    data.oprType = 'D'
    setTimeout(() => {
      this.commonService.callApi('searchapplicationforarrivaldeparture', data, 'post', false, false, 'APPR').then(success => {
        let successData: any = success;
        //console.log(success);

        this.totalItem = successData.totalElements;
        this.number = successData.number;
        this.size = successData.size;
        this.loading = false;
        this.ngxLoader.stop();
        this.noofelements = successData.numberOfElements;
        this.totalElements = successData.totalElements;
        if (successData.content.length) {
          this.Applications = successData.content;
        } else {
          this.Applications = [];
        }
        //console.log(this.Applications)
      }
      ).catch(e => {
        this.ngxLoader.stop();
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }, 1000);
  }

  /****************************************************************************/



  /****************************************************************************
@PURPOSE      : Paginations 
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
      //console.log(e);

      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      this.getVisaList(this.filterObj);

    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getVisaList(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      //console.log(e)
      this.filterObj.pageSize = e;
      this.filterObj.pageNumber = 1;
      this.getVisaList(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getVisaList(this.data);
    }

  }
  /***************************************************************/



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

    // this.filterObj.applicationNumber = this.filterObj.applicationNumber.trim();
    this.isFilterApplied = true;
    this.currentPage = 1;
    this.filterObj.pageNumber = 1;
    this.filterObj.pageSize = this.pagesize;
    this.removeToken('dashboardAllocation')
    this.getVisaList(this.filterObj);

  }
  resetFilter() {
    this.isFilterApplied = false;
    this.currentPage = 1;
    this.filterObj = {};
    //console.log(this.data);
    this.removeToken('dashboardAllocation')
    if (this.data.docStatus) {
      delete this.data.docStatus
      //console.log(this.data.docStatus);

    }
    //console.log(this.data);
    this.getVisaList(this.data);
  }
  /****************************************************************************/
  public fileNumber: any;
  clickApp(file, status, arrdepid) {
    //console.log("==tset", arrdepid)
    this.setToken('fileNo', file)
    this.setToken('docStatus', status)
    this.setToken('arrDepId', arrdepid)
  }


  spaceTrim(event) {
    this.filterObj.applicationNumber = this.filterObj.applicationNumber.trim()
  }

}
