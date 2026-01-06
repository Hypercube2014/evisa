import { Component, ElementRef, Injectable, Injector, OnInit, ViewChild } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent';
import { NgxUiLoaderService } from "ngx-ui-loader";
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
declare var require: any
const FileSaver = require('file-saver');

@Injectable({ providedIn: 'root' })
@Component({
  selector: 'app-payment-completed-list',
  templateUrl: './payment-completed-list.component.html',
  styleUrls: ['./payment-completed-list.component.css']
})
export class PaymentCompletedListComponent extends BaseComponent implements OnInit {

  public pagesize = 10;
  public totalItem: any;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }
  @ViewChild("pdfTable", { static: false }) pdfTable: ElementRef;
  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService, public http: HttpClient) {
    super(inj);
  }

  ngOnInit(): void {
    this.getpaymentCompleted(this.data);
  }
  refreshPage() {
    this.getpaymentCompleted(this.data);
  }

  /****************************************************************************
        @PURPOSE      : To retrive the pending paymentFAQ List
        @PARAMETERS   : pageNumber,PageSize, loggeduser, status
        @RETURN       : pendingPaymentDTO
     ****************************************************************************/
  public number: 0;
  public size: 0;
  public noofelements: 0;
  public totalElements: 0;
  public loading: boolean = false;
  paymentCompleted: any = []


  getpaymentCompleted(data) {
    this.loading = true;
    this.ngxLoader.start();
    setTimeout(() => {
      this.data.loggedUser = this.getToken('username'),
        this.data.status = 'PC';
      this.commonService.callApi('payment/search', data, 'post', false, false, 'REG').then(success => {
        let successData: any = success;
        this.totalItem = successData.totalElements;
        this.number = successData.number;
        this.size = successData.size;
        this.loading = false;
        this.ngxLoader.stop();
        this.noofelements = successData.numberOfElements;
        this.totalElements = successData.totalElements;

        if (successData.content.length) {
          this.paymentCompleted = successData.content
          // //console.log(this.paymentCompleted);

        } else {
          this.paymentCompleted = []
        }
      }).catch(e => {
        this.ngxLoader.stop();
        this.toastr.error(e.message, 'Oops!')
      });
    });
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
      this.getpaymentCompleted(this.filterObj);
    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getpaymentCompleted(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      this.filterObj.pageSize = e;
      this.getpaymentCompleted(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getpaymentCompleted(this.data);
    }
  }
  /*******************************************************************************/
  public url: any
  public recieptInfo: any
  onDownload1(data) {
    this.commonService.callApi('payment/recieptinfo/' + data, '', 'get', false, false, 'REG').then(success => {
      this.recieptInfo = success
      if (this.recieptInfo.statusCode === "SUCCESS") {
        FileSaver.saveAs(this.recieptInfo.recieptUrl, "sample.pdf");

      } else {
        this.toastr.error(this.recieptInfo.statusDesc, 'Error')
      }
      // //console.log(this.recieptInfo);
    }).catch(e => {
      this.toastr.error(e.message, 'Oops!')
    });

  }

  //   }).catch(e => {
  //     this.toastr.error(e.message, 'Oops!')
  //   });
  // }






  public resource: any;
  onDownload(data, fileNumber) {

    //  let  headers=new HttpHeaders({'Authorization': 'Basic Y3JpbXNvbjpjUmlNczBuJGRjMW5EMUE=' });

    // this.http.get('http://localhost:8081/applicant-api/v1/api/payment/recieptinfo/'+data, {observe: 'response',	responseType: 'arraybuffer' as 'json',headers:headers})

    // .subscribe(
    //   (data: HttpResponse<any>) => {
    //     //console.log(data.headers.get('filename'));
    //   },
    //   error => {
    //     this.loading = false;
    //   });

    // .subscribe(resp => 
    //   // //console.log(resp)
    //   //console.log(resp.headers.get('fileName'))
    //   )
    this.commonService.downloadReciept(data).subscribe((res) => {
      // //console.log(res);
      this.resource = res;
      let blob = new Blob([this.resource], { type: "application/pdf;charset=UTF-8" });
      FileSaver.saveAs(blob, fileNumber + '_Payment_Receipt');
    });
  }


  /****************************************************************************
       @PURPOSE      : filters.
       @PARAMETERS   : NA
       @RETURN       : NA
    ****************************************************************************/
  applyFilter() {
    if (JSON.stringify(this.filterObj) == "{}") {
      if (localStorage.getItem('Language') === 'en') {
        this.toastr.error("Please Select Any Value  Data to Filter", 'Oops!');
        return 0;
      } else {
        this.toastr.error("Veuillez filtrer par categorie", 'Oops!');
        return 0;
      }
    }
    this.isFilterApplied = true;
    this.currentPage = 1;
    this.filterObj.pageNumber = 1;
    this.filterObj.pageSize = this.pagesize;
    this.filterObj.loggedUser = this.getToken('username');
    this.filterObj.status = 'PC'
    this.getpaymentCompleted(this.filterObj);

  }
  resetFilter() {
    this.isFilterApplied = false;
    this.currentPage = 1;
    this.filterObj = {};
    this.getpaymentCompleted(this.data);
  }
  /****************************************************************************/


}


