
import { Component, Injector, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { BaseComponent } from '../../common/commonComponent';
@Component({
    selector: 'app-process-reports',
    templateUrl: './process-reports.component.html',
    styleUrls: ['./process-reports.component.css']
})
export class ProcessReportsComponent extends BaseComponent implements OnInit {
    reportDetails: any = {}

    public isReport: boolean = false;
    agents = [
        {
            code: 'Hemanth'
        }
    ]

    rolesList = [
        {
            roleCode: 'DM',
            roleDesc: 'Decision Manager'
        },
        {
            roleCode: 'BCO',
            roleDesc: 'Border Control Officer'
        },
        {
            roleCode: 'SBCO',
            roleDesc: 'Senior Border Control Officer'
        }
    ]

    reportTypes = [
        {
            code: 'S',
            desc: 'Summary'
        }, {
            code: 'I',
            desc: 'Individual'
        }
    ]

    periodDetails = [
        {
            code: 'T',
            desc: 'Aujourd\'hui'
          }, {
            code: 'W',
            desc: 'Hebdomadaire'
          }, {
            code: 'M',
            desc: 'Mensuel'
          }, {
            code: 'Y',
            desc: 'Annuel'
          }, {
            code: 'D',
            desc: 'Plage de dates'
        }
    ]
    bsConfig: Partial<BsDatepickerConfig>;
    maxDate = new Date();
    colorTheme = 'theme-blue'
    datePickerConfig = {
        dateInputFormat: 'DD-MM-YYYY',
        containerClass: this.colorTheme,
        maxDate: new Date(),
        showWeekNumbers: false,

    }

    constructor(inj: Injector) {
        super(inj)
    }

    ngOnInit(): void {
        this.reportDetails.reporterType = 'Decision Maker'
    }
    public pagesize = 10;
    public page = 1;

    public totalItem: any;
    public data: any = {
        pageNumber: 1,
        pageSize: this.pagesize,
    }
    public number: 0;
    public size: 0;
    public loading: boolean = false;
    public noofelements: 0;
    public totalElements: 0;
    perfomanceReports = [];
    usersCategory = []
    totalAllocated = [];
    turnAround = [];
    closed = []
    pending = []

    applyFilter(statisticalReportForm, reportDetails) {

        if (JSON.stringify(this.filterObj) == "{}") {
            if (localStorage.getItem('Language') === 'en') {
                this.toastr.errorToastr("Please Select Any Value  Data to Filter", 'Oops!');
                return 0;
            } else {
                this.toastr.errorToastr("Veuillez filtrer par categorie", 'Oops!');
                return 0;
            }

        }
        this.isReport = true;
        if ((this.filterObj.reportType === 'I') && ((this.filterObj.agentList === undefined) || (this.filterObj.agentList === null))) {
            if (localStorage.getItem('Language') === 'en') {
                this.toastr.errorToastr("Please Select Agent to filter data", 'Oops!');
                return 0;
            } else {
                this.toastr.errorToastr("Veuillez sélectionner un agent pour filtrer les données", 'Oops!');
                return 0;
            }
        }
        //console.log(this.filterObj.startDate)
        if (this.filterObj.period === 'D' && ((!this.filterObj.startDate) || (!this.filterObj.endDate))) {
            if (localStorage.getItem('Language') === 'en') {
                this.toastr.errorToastr("Please Select Dates to filter data", 'Oops!');
                return 0;
            } else {
                this.toastr.errorToastr("Veuillez sélectionner les dates pour filtrer les données", 'Oops!');
                return 0;
            }
        }

        if (this.filterObj.reportType === 'S') {
            this.filterObj.agentList = null
        }
        if (this.filterObj.period != 'D') {
            //console.log("test")
            this.filterObj.startDate = null;
            this.filterObj.endDate = null
        }
        if (this.reportDetails.reporterType === 'Decision Maker') {
            this.filterObj.oprType = 'DM'
            // delete this.filterObj.reporterType;
        }
        this.isFilterApplied = true;
        this.currentPage = 1;
        this.filterObj.pageNumber = 1;
        this.filterObj.pageSize = this.pagesize;
        this.filterObj.loggedUser = this.getToken('username'),

            this.filterObj.role = this.getToken('Role')

        this.getFetchAgents(this.filterObj);


    }

    getFetchAgents(data) {
        //console.log(this.data)
        this.commonService.callApi('processreport/', data, 'post', false, false, 'APPR').then(success => {
            let successData: any = success;

            //console.log(successData);
            this.number = successData.number;
            this.size = successData.size;
            this.totalItem = successData.totalElements;
            this.loading = false;
            this.noofelements = successData.numberOfElements;
            this.totalElements = successData.totalElements;
            if (successData.content.length) {

                this.perfomanceReports = successData.content
                this.usersCategory = this.perfomanceReports.map(function (el) { return el.username; });
                this.totalAllocated = this.perfomanceReports.map(function (el) { return el.totalAllocated; })
                this.turnAround = this.perfomanceReports.map(function (el) { return el.average; })
                this.pending = this.perfomanceReports.map(function (el) { return el.pending; })
                this.isReport = true;
                this.getReportChart()

                //console.log(this.perfomanceReports);
                // //console.log(this.totalAllocated);

                // this.filterObj.oprType = 'Decision Maker'


            } else {

                this.perfomanceReports = [];
                this.usersCategory = []
                this.totalAllocated = [];
                this.turnAround = [];
                this.closed = []
                this.pending = []
                this.getReportChart()
            }

        }
        ).catch(e => {
            this.toastr.errorToastr(e.message, 'Oops!')
        });


    }


    isRange: boolean = false;
    selectPeriod(e) {
        if (e.code === 'D') {
            this.isRange = true;
        } else {
            this.isRange = false;
        }
    }


    isIndividual: boolean = false;
    selectReport(e) {
        if (e.code === 'I') {
            this.isIndividual = true;
            this.getAgents(this.filterObj.oprType);
        } else {
            this.isIndividual = false;
        }
    }




    agentsList: any
    getAgents(oprType) {
        this.commonService.callApi('fetchagents/' + this.getToken('username') + '/' + this.getToken('Role') + '/' + 'DM', '', 'get', false, false, 'APPR').then(success => {
            let successData: any = success;
            this.agentsList = successData.agentList;
            //console.log(this.agentsList);

        }
        ).catch(e => {
            this.toastr.errorToastr(e.message, 'Oops!')
        });
    }


    getReportChart() {
        Highcharts.chart('container', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'Applications'
            },

            xAxis: {
                categories: this.usersCategory,
                crosshair: true
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Average Turn Around Time'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y}</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: [{
                name: 'Average Turnaround Time',
                data: this.turnAround

            }, {
                name: 'Total Applications Processed',
                data: this.totalAllocated

            },]
        });
    }


    /****************************************************************************
  @PURPOSE      : Paginations 
  @PARAMETERS   : pageNumber,PageSize,loggedinuse
  @RETURN       : NA
  ****************************************************************************/
    public pagenumber = 5;
    public currentPage = 1;
    public showBoundaryLinks = true;
    public rangelist = [5, 10, 25, 100];
    public isFilterApplied: boolean = false;
    public filterObj: any = {};
    pageChanged(e) {
        if (this.isFilterApplied) {
            this.filterObj.pageNumber = e.page;
            this.filterObj.pageSize = e.itemsPerPage;
            this.filterObj.loggedUser = this.getToken('username'),
                this.filterObj.role = this.getToken('Role')
            this.getFetchAgents(this.filterObj)
        } else {
            this.data.pageNumber = e.page;
            this.data.pageSize = e.itemsPerPage;
            this.getFetchAgents(this.data)
        }
    }
    rangeChanged(e) {
        if (this.isFilterApplied) {
            //console.log(e)
            this.filterObj.pageSize = e;
            this.filterObj.loggedUser = this.getToken('username'),
                this.filterObj.role = this.getToken('Role')
            this.getFetchAgents(this.filterObj)
        } else {
            this.data.pageSize = e;
            this.getFetchAgents(this.data)
        }

    }
    /***************************************************************/
}
