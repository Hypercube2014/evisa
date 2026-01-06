import { Component, OnInit, Injector, Renderer2 } from '@angular/core';
import { BaseComponent } from '../../common/commonComponent';
import { NgxSpinnerService } from "ngx-spinner";
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { DatePipe } from '@angular/common';
import { TranslateService } from "@ngx-translate/core";
declare var $: any;
@Component({
    selector: 'app-new-dashboard',
    templateUrl: './new-dashboard.component.html',
    styleUrls: ['./new-dashboard.component.css']
})

export class NewDashboardComponent extends BaseComponent implements OnInit {
    constructor(inj: Injector, public translate: TranslateService, private spinner: NgxSpinnerService, private renderer: Renderer2, private ngxLoader: NgxUiLoaderService, public datepipe: DatePipe) {
        super(inj);
        translate.use(localStorage.getItem("Language"));
        this.bsConfig = Object.assign({}, { containerClass: this.colorTheme, customTodayClass: 'custom-today-class' });
    }

    public pagesize = 10;
    public page = 1;
    isCollapsed = true;
    public totalItem: any;
    public campaignData = [];
    bsConfig: Partial<BsDatepickerConfig>;
    colorTheme = 'theme-blue'
    public data: any = {
        pageNumber: 1,
        pageSize: this.pagesize,
    }

    ngOnInit(): void {
        this.getApplications(this.data);

        if ($('li.main.active')) {
            $('li.open').removeClass('open')
            $('ul.sub-menu').css('display', 'none')
        }
    }
    refreshPage() {
        this.getApplications(this.data);
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
    public upStatus: boolean = false
    getApplications(data) {
        data.loggedUser = this.getToken('username');
        this.loading = true;
        this.ngxLoader.start();
        setTimeout(() => {
            this.commonService.callApi('searchapplicationtracker', data, 'post', false, false, 'REG').then(success => {
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

                    let double = this.Applications.filter(x => x.visaStatus == 'UP');
                    if (double.length > 0) {
                        this.upStatus = true
                    } else {
                        this.upStatus = false
                    }
                } else {
                    this.Applications = [];
                }
                // //console.log(this.Applications)
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

        } else {
            this.data.pageNumber = e.page;
            this.data.pageSize = e.itemsPerPage;
            this.getApplications(this.data);
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
}