import { Component, Injector, OnInit } from '@angular/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-search-extension-visa',
  templateUrl: './search-extension-visa.component.html',
  styleUrls: ['./search-extension-visa.component.css']
})
export class SearchExtensionVisaComponent extends BaseComponent implements OnInit {

  public loading: boolean;
  public pagesize = 10;
  public page = 1;
  isCollapsed = false;
  public totalItem: any;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }

  status = [
    {
      'code': 'CLS',
      'description': 'Closed'
    },
    {
      'code': 'PEN',
      'description': 'Pending Approval'
    },

    {
      'code': 'VAL',
      'description': 'Pending Validation'
    },
  ]

  constructor(public inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj)
    this.removeToken('documentStatus')
  }

  ngOnInit(): void {
    if (this.getToken('Role') === 'DM') {
      this.data.documentStatus = 'PEN';
      this.filterObj.documentStatus = 'PEN';
      this.isFilterApplied = true;
    }
    if (this.getToken('Role') === 'DMM') {
      this.data.documentStatus = 'VAL';
      this.filterObj.documentStatus = 'VAL';
      this.isFilterApplied = true;
    }
    if (this.getToken('extensionEvisa') === 'CLS') {
      this.filterObj.documentStatus = 'CLS'
      this.isFilterApplied = true;
    } else if (this.getToken('extensionEvisa') === 'ALL') {
      this.filterObj.documentStatus = null
      this.isFilterApplied = true;
    } else if (this.getToken('extensionEvisa') === 'PEN') {
      this.filterObj.documentStatus = 'PEN'
      this.isFilterApplied = true;
    } else if (this.getToken('extensionEvisa') === 'REJ') {
      this.filterObj.documentStatus = 'CLS'
      this.isFilterApplied = true;
    }else if (this.getToken('extensionEvisa') === 'VAL') {
      this.filterObj.documentStatus = 'VAL'
      this.isFilterApplied = true;
    }
    this.getExtensionVisaList(this.data)
  }

  /****************************************************************************
      @PURPOSE      : To retrive the  applications List
      @PARAMETERS   : pageNumber,PageSize,loggedinuser
      @RETURN       : NA
   ****************************************************************************/
  public evisaExtension: any = []
  public number = 0;
  public size = 0;
  public noofelements = 0;
  public totalElements = 0;
  public result: boolean = false
  getExtensionVisaList(data) {
    this.result = true

    if (this.getToken('Role') === 'DM') {
      data.loggedUser = this.getToken('username');
    }
    if (this.getToken('extensionEvisa') === 'CLS') {
      data.documentStatus = 'CLS';
    } else if (this.getToken('extensionEvisa') === 'PEN') {
      data.documentStatus = 'PEN'
    } else if (this.getToken('extensionEvisa') === 'ALL') {
      data.documentStatus = null
    } else if (this.getToken('extensionEvisa') === 'REJ') {
      data.docStatus = 'CLS'
      data.visaStatus = 'REJ'
    }else if (this.getToken('extensionEvisa') === 'VAL') {
      data.docStatus = 'VAL'
    }
    data.role = this.getToken('Role')
    this.loading = true;
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('searchapplicantvisaextension', data, 'post', false, false, 'APPR').then(success => {
        let successData: any = success;
        this.totalItem = successData.totalElements;
        this.number = successData.number;
        this.size = successData.size;
        this.noofelements = successData.numberOfElements;
        this.totalElements = successData.totalElements;
        this.loading = false;
        this.ngxLoader.stop();
        if (successData.content.length) {
          this.evisaExtension = successData.content;
        } else {
          this.evisaExtension = [];
        }
        //console.log(this.evisaExtension)
      }
      ).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }, 1000);
  }


  /****************************************************************************
      @PURPOSE      : filters.
      @PARAMETERS   : NA
      @RETURN       : NA
   ****************************************************************************/
  public filterObj: any = {};
  public isFilterApplied: boolean = false;
  public currentPage = 1;
  public showBoundaryLinks = true;
  public rangeList = [5, 10, 25, 100];

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
    this.removeToken('extensionEvisa')
    this.getExtensionVisaList(this.filterObj);

  }
  resetFilter() {
    this.isFilterApplied = false;
    this.currentPage = 1;
    this.filterObj = {};
    this.removeToken('extensionEvisa')
    if (this.data.documentStatus) {
      delete this.data.documentStatus
      //console.log(this.data.documentStatus);

    }
    //console.log(this.data);
    this.getExtensionVisaList(this.data);
  }
  /****************************************************************************/

  spaceTrim(event) {
    this.filterObj.applicationNumber = this.filterObj.applicationNumber.trim()
  }

  /****************************************************************************
@PURPOSE      : Paginations 
@PARAMETERS   : pageNumber,PageSize,loggedinuse
@RETURN       : NA
****************************************************************************/
  pageChanged(e) {
    if (this.isFilterApplied) {

      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      this.getExtensionVisaList(this.filterObj);

    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getExtensionVisaList(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      //console.log(e)
      this.filterObj.pageSize = e;
      this.getExtensionVisaList(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getExtensionVisaList(this.data);
    }

  }
  /***************************************************************/
  clickApp(status) {
    this.setToken('documentStatus', status);
  }

  /****************************************************************************
       @PURPOSE      : To get Allocated Files.
       @PARAMETERS   : NA
       @RETURN       : List of Allocated Details
 ****************************************************************************/
  nextSetOfExtensions() {
    this.ngxLoader.start()
    setTimeout(() => {
      this.commonService.callApi('nextsetofextensionfiles/' + this.getToken('username'), '', 'get', false, false, 'APPR').then(success => {
        let successData: any = success;
        this.getExtensionVisaList(this.data)
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
  /*******************************************************************/
}
