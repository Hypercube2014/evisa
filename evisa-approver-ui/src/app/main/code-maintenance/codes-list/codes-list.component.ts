import { Component, Injector, OnInit } from '@angular/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-codes-list',
  templateUrl: './codes-list.component.html',
  styleUrls: ['./codes-list.component.css']
})
export class CodesListComponent extends BaseComponent implements OnInit {

  public totalItem: any;
  pagesize: number = 10

  selectCode: any = {
    "codeType": this.getToken('codeType')
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

  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }

  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj);

  }


  ngOnInit(): void {
    this.getCodes();
    this.getCodesList(this.data);
  }


  refreshPage() {
    this.getCodesList(this.data);
  }

  /****************************************************************************
    @PURPOSE      : To retrive List of Countries.
    @PARAMETERS   : data
    @RETURN       : List of Country Details
 ****************************************************************************/
  codesList: any = []
  public number: 0;
  public size: 0;
  public noofelements: 0;
  public totalElements: 0;
  public loading: boolean = false;
  public codeType: any;

  getCodesList(data) {
    if (this.getToken('codeType')) {
      this.loading = true
      this.listHeader = this.getToken('description');
      //console.log(this.listHeader)
      this.ngxLoader.start()
      data.codeType = this.getToken('codeType')
      setTimeout(() => {
        this.commonService.callApi('searchmastercode', data, 'post', false, false, 'APPR').then(success => {
          let successData: any = success
          this.totalItem = successData.totalElements;
          this.number = successData.number;
          this.size = successData.size;
          this.isSelected = true
          this.loading = false;
          this.ngxLoader.stop()
          this.noofelements = successData.numberOfElements;
          this.totalElements = successData.totalElements;
          if (successData.content.length) {
            this.codesList = successData.content;
          } else {
            this.codesList = []
          }

        }).catch(e => {
          this.ngxLoader.stop()
          this.toastr.errorToastr(e.message, 'Oops!')
        });
      }, 1500)
    }
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
    if (this.isFilterApplied) {
      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      this.getCodesList(this.filterObj);
    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getCodesList(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      //console.log(e)
      this.filterObj.pageSize = e;
      this.getCodesList(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getCodesList(this.data);
    }
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
        this.toastr.errorToastr("Please Select Any Value Data to Filter", 'Oops!');
        return 0;
      } else {
        this.toastr.errorToastr("Veuillez sélectionner n'importe quelle valeur de données pour filtrer", 'Oops!');
        return 0;
      }

    }
    this.isFilterApplied = true;
    this.currentPage = 1;
    this.filterObj.pageNumber = 1;
    this.filterObj.pageSize = this.pagesize;
    this.getCodesList(this.filterObj);
  }

  resetFilter() {
    this.isFilterApplied = false;
    this.currentPage = 1;
    this.filterObj = {};
    this.getCodesList(this.data);
  }
  /****************************************************************************/

  /****************************************************************************
      @PURPOSE      : To retreive the data of all Code Types
      @PARAMETERS   : NA
      @RETURN       : Codes Array
   ****************************************************************************/

  codesArray: any = []
  getCodes() {
    this.commonService.callApi('mastercodetype/active', '', 'get', false, false, 'APPR').then(success => {
      this.codesArray = success
      this.codesArray = this.codesArray.masterCodeResultDTOs
    })
  }
  /****************************************************************************/

  /****************************************************************************
      @PURPOSE      : To display the List of selected code
      @PARAMETERS   : Event selected in Dropdwon
      @RETURN       : NA
   ****************************************************************************/
  isSelected: boolean = false
  listHeader: any;

  onChange(data) {
    //console.log(data)
    this.listHeader = data.description;
    this.data.codeType = data.code
    this.codeType = data.code
    this.setToken('codeType', data.code)
    //console.log(this.listHeader)
    this.setToken('description', this.listHeader)
    this.isSelected = true;
    this.getCodesList(this.data);
  }
  /****************************************************************************/
}
