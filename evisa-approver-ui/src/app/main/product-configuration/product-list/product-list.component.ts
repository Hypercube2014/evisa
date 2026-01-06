import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';
import { NgxUiLoaderService } from "ngx-ui-loader";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent extends BaseComponent implements OnInit {

  public totalItem: any;
  pagesize: number = 10
  searchText: {}

  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }

  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj);
  }

  ngOnInit(): void {
    this.getProductConfig(this.data)
  }

  /****************************************************************************
       @PURPOSE      : To retrive the product_config List
       @PARAMETERS   : pageNumber,PageSize
       @RETURN       : ProductConfigSearchDTO object
    ****************************************************************************/
  public productConfig: any
  public number: 0;
  public size: 0;
  public noofelements: 0;
  public totalElements: 0;
  public loading: boolean;

  getProductConfig(data) {
    this.loading = true;
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('searchproductconfig', data, 'post', false, false, 'APPR').then(success => {
        let successData: any = success;
        //console.log(successData);

        this.totalItem = successData.totalElements;
        this.number = successData.number;
        this.size = successData.size;
        this.loading = false;
        this.ngxLoader.stop();
        this.noofelements = successData.numberOfElements;
        this.totalElements = successData.totalElements;

        if (successData.content.length) {
          this.productConfig = successData.content
          //console.log(this.productConfig);

        } else {
          this.productConfig = []
        }
      }).catch(e => {
        this.ngxLoader.stop();
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }, 1500);

  }

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
      this.getProductConfig(this.filterObj);
    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getProductConfig(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      //console.log(e)
      this.filterObj.pageSize = e;
      this.getProductConfig(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getProductConfig(this.data);
    }
  }
  /****************************************************************************/

  refreshPage() {
    this.getProductConfig(this.data)
  }



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
    this.getProductConfig(this.filterObj);
  }

  resetFilter() {
    this.isFilterApplied = false;
    this.currentPage = 1;
    this.filterObj = {};
    this.getProductConfig(this.data);
  }
  /****************************************************************************/

}
