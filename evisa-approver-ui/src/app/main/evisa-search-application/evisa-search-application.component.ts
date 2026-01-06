import { Component, Injector, OnInit } from '@angular/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-evisa-search-application',
  templateUrl: './evisa-search-application.component.html',
  styleUrls: ['./evisa-search-application.component.css']
})
export class EvisaSearchApplicationComponent extends BaseComponent implements OnInit {
  public pagesize = 10;
  public page = 1;

  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,

  }

  status = [
    {
      'code': 'CLS',
      'description': 'Clôturer'
    },
    {
      'code': 'PEN',
      'description': 'En attente d\'approbation'
    },

    {
      'code': 'VAL',
      'description': 'En attente de validation'
    },
  ]

  statusDetails = [
    {
      code: "PEN",
      description: "Dossier en attente d'approbation",
    },
    {
      code: "CLS",
      description: "Dossier clôturer",
    },
    {
      code: "VAL",
      description: "Dossier en besoin de validation",
    }
  ];

  visaStatus = [
    {
      'name': 'Oui',
      'code': 'Y'
    },
    {
      'name': 'Non',
      'code': 'N'
    }
  ]

  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj)
  }
  public Applications: any = []
  ngOnInit(): void {
    this.filterObj.docStatus = "PEN";
    this.data.docStatus = "PEN";
    this.getsearchFile(this.data)

    if (this.getToken("Role") === "DM") {
      this.getsearchFile(this.data);
      if (this.getToken("dashboardAllocation") === "CLS") {
        this.filterObj.docStatus = "CLS";
        this.isFilterApplied = true;
      } else if (this.getToken("dashboardAllocation") === "PEN") {
        this.filterObj.docStatus = "PEN";
        this.isFilterApplied = true;
      } else if (this.getToken("dashboardAllocation") === "SUB") {
        this.filterObj.docStatus = "SUB";
        this.isFilterApplied = true;
      } else if (this.getToken("dashboardAllocation") === "ALL") {
        this.filterObj.docStatus = null;
        this.isFilterApplied = true;
      } else if (this.getToken("dashboardAllocation") === "REJ") {
        this.filterObj.docStatus = "CLS";
        this.isFilterApplied = true;
      }
    }
  }


  public fileNumber: any;
  clickApp(file) {
    this.setToken('fileNo', file)
  }

  /****************************************************************************
       @PURPOSE      : To get Allocated Files.
       @PARAMETERS   : NA
       @RETURN       : List of Allocated Details
 ****************************************************************************/
  public number = 0;
  public size = 0;
  public noofelements = 0;
  public totalElements = 0;
  public loading: boolean;
  public totalItem: any;
  getsearchFile(data) {
    data.loggedUser = this.getToken('username');
    /* data.docStatus = '' */
    
    if (data.docStatus === "CLS") {
      data.docStatus = "CLS";
      data.visaStatus = "REJ";
      data.arrDepIndicator = null;
    }

    //console.log("visaStatus"+this.data.docStatus);
    this.ngxLoader.start()
    setTimeout(() => {
      this.commonService.callApi('searchfilenumbers', data, 'post', false, false, 'APPR').then(success => {
        let successData: any = success;
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
    }, 1000)

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
      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      this.getsearchFile(this.filterObj);

    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getsearchFile(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      this.filterObj.pageNumber = 1;
      this.filterObj.pageSize = e;
      this.getsearchFile(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getsearchFile(this.data);
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
    this.isFilterApplied = true;
    this.currentPage = 1;
    this.filterObj.pageNumber = 1;
    this.filterObj.pageSize = this.pagesize;
    this.removeToken('dashboardAllocation')
    this.getsearchFile(this.filterObj);
  }
  resetFilter() {
    this.isFilterApplied = false;
    this.currentPage = 1;
    this.filterObj = {};
    //console.log(this.data);
    this.removeToken('dashboardAllocation')
    if (this.data.docStatus) {
      delete this.data.docStatus
    }
    //console.log(this.data);
    this.getsearchFile(this.data);
  }
  /****************************************************************************/
  spaceTrim(event) {
    this.filterObj.fileNumber = this.filterObj.fileNumber.trim()
  }
  /****************************************************************************
       @PURPOSE      : To get Allocated Files.
       @PARAMETERS   : NA
       @RETURN       : List of Allocated Details
 ****************************************************************************/
  getNextSetOfFiles() {
    this.ngxLoader.start()
    setTimeout(() => {
      this.commonService.callApi('nextsetoffiles/' + this.getToken('username'), '', 'get', false, false, 'APPR').then(success => {
        let successData: any = success;
        this.getsearchFile(this.data)
        if (successData.apiStatusCode === "SUCCESS") {
          this.ngxLoader.stop()
          this.toastr.successToastr(successData.apiStatusDesc, 'Success')
        } else {
          this.ngxLoader.stop()
          this.toastr.errorToastr(successData.apiStatusDesc, 'Error');
        }
      }).catch(e => {
        this.ngxLoader.stop()
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }, 1000)
  }
  /****************************************************************************/
}
