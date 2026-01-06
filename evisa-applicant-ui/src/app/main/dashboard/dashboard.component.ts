import { Component, OnInit, Injector, Renderer2 } from '@angular/core';
import { BaseComponent } from '../../common/commonComponent';
import { NgxSpinnerService } from "ngx-spinner";
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { DatePipe } from '@angular/common';
declare var require: any
const FileSaver = require('file-saver');

declare var $: any;
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent extends BaseComponent implements OnInit {

  public pagesize = 10;
  public page = 1;
  isCollapsed = true;
  public totalItem: any;
  public campaignData = [];
  bsConfig: Partial<BsDatepickerConfig>;
  colorTheme = 'theme-blue'

  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize
  }

  disabledDates = [
    new Date('2020-02-05'),
    new Date('2020-02-09')
  ];
  constructor(inj: Injector, private spinner: NgxSpinnerService, private renderer: Renderer2, private ngxLoader: NgxUiLoaderService, public datepipe: DatePipe) {
    super(inj);

    this.bsConfig = Object.assign({}, { containerClass: this.colorTheme, customTodayClass: 'custom-today-class' });
  }

  ngOnInit(): void {
    this.getApplications(this.data);
    this.getCountApplications(this.data);
    this.searchVisaExtension(this.data);
      if ($('li.main.active')) {
      $('li.open').removeClass('open')
      $('ul.sub-menu').css('display', 'none')
    }
  }


  refreshPage() {
    this.getApplications(this.data);
      this.getCountApplications(this.data);
      this.searchVisaExtension(this.data);
  }

  /****************************************************************************
        @PURPOSE      : To retrive the  applications List
        @PARAMETERS   : pageNumber,PageSize,loggedinuser
        @RETURN       : NA
     ****************************************************************************/
  public Applications = [];
  public CountNumberApplication = [];
  public number = 0;
  public size = 0;
  public noofelements = 0;
  public totalElements = 0;
  public loading: boolean;
  public upStatus: boolean = false
  getApplications(data) {
    data.loggedUser = this.getToken('username');
    this.loading = true;
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('searchapplicationtracker', data, 'post', false, false, 'REG').then(success => {
        // //console.log("call api entered !!!!");
        let successData: any = success;
        this.totalItem = successData.totalElements;
        this.number = successData.number;
        this.size = successData.size;
        this.loading = false;
        this.ngxLoader.stop();
        this.noofelements = successData.numberOfElements;
        this.totalElements = successData.totalElements;

        // //console.log("fhtthth");
        // //console.log(successData);

        if (successData.content.length) {
          this.Applications = successData.content;

          let double = this.Applications.filter(x => x.visaStatus == 'UP');
          if (double.length > 0) {
            this.upStatus = true
          } else {
            this.upStatus = false
          }
        } else {
          this.Applications = [];
        }
        // //console.log(this.Applications,"coucocucouc")
      }
      ).catch(e => {
        this.ngxLoader.stop();
        this.toastr.error(e.message, 'Oops!')
      });
    });
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
    this.Applications = [];
    if (this.isFilterApplied) {

      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      this.getApplications(this.filterObj);
        this.getCountApplications(this.data);
        this.searchVisaExtension(this.data);
    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getApplications(this.data);
        this.getCountApplications(this.data);
        this.searchVisaExtension(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      // //console.log(e)
      this.filterObj.pageSize = e;
      this.getApplications(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getApplications(this.data);
    }

  }
  /***************************************************************/

  spaceTrim(event) {
    this.filterObj.applicationNumber = this.filterObj.applicationNumber.trim();
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
    this.getApplications(this.filterObj);

  }
  resetFilter() {
    this.isFilterApplied = false;
    this.currentPage = 1;
    this.filterObj = {};
    this.getApplications(this.data);
  }
  /****************************************************************************/
    public countApplicationsEnded: number = 0;
    public countApplicationProcess: number = 0;

    getCountApplications(data) {
        data.loggedUser = this.getToken('username');
        this.loading = true;
        this.ngxLoader.start();
        setTimeout(() => {
            this.commonService.callApi('searchapplicationtracker', data, 'post', false, false, 'REG')
                .then(success => {
                    let successData: any = success;
                    this.loading = false;
                    this.ngxLoader.stop();
            // //console.log(successData.content);
                    this.CountNumberApplication = successData.content;
                    this.CountNumberApplication.forEach(account => {
                           if(account.documentStatus === "CLS"){
                               this.countApplicationsEnded++;
                          }
                          else{
                               this.countApplicationProcess++;
                           }
                    });
                   // alert(this.countApplicationsEnded);
                })
                .catch(e => {
                    this.ngxLoader.stop();
                    this.toastr.error(e.message, 'Oops!');
                });
        });
    }
    public countExtensionsEnded: number = 0;
    public countExtensionProcess: number = 0;
    serchVisa: any = []
    searchVisaExtension(data) {
        data.role = "APPLICANT"
        data.loggedUser= this.getToken('username')
        this.loading = true;
        this.ngxLoader.start();
        setTimeout(() => {
            this.commonService.callApi('searchapplicantvisaextension', data, 'post', false, false, 'REG').then(success => {
                let successData: any = success;

                    this.serchVisa = successData.content;
                this.serchVisa.forEach(item => {
                    if (item.extensionStatus === "REJ" && item.extensionStatus === "APR") {
                        this.countExtensionsEnded++
                    }
                    else {
                        this.countExtensionProcess++
                    }
                });
            }).catch(e => {
                this.ngxLoader.stop();
                this.toastr.error(e.message, 'Oops!')
            });
        }, 1500);
    }
  public resource: any;
  downloadApplication(data) {
    this.commonService.downloadApproval(data).subscribe((res) => {
      // //console.log(res);
      this.resource = res;
      let blob = new Blob([this.resource], { type: "application/pdf;charset=UTF-8" });
      FileSaver.saveAs(blob, data);
    });
  }
}