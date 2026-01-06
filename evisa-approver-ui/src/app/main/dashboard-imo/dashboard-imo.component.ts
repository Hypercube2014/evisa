import { Component, Injector, OnInit, ViewChild } from "@angular/core";
import * as Highcharts from "highcharts";
import { BaseComponent } from "../../common/commonComponent";
import { BsModalService, BsModalRef } from "ngx-bootstrap/modal";
import moment from "moment";
declare var $: any;
@Component({
  selector: "app-dashboard-imo",
  templateUrl: "./dashboard-imo.component.html",
  styleUrls: ["./dashboard-imo.component.css"],
})
export class DashboardImoComponent extends BaseComponent implements OnInit {
  selectedAttributes = [];
  dmmDashboard: any = {};
  pendingaplication: any = {};
  counterOverstaydeparted: any;
  public dataa: any = {
    docstatus: "PEN",
  };

  periodDetails = [
    {
      code: "T",
      desc: "Aujourd'hui",
    },
    {
      code: "W",
      desc: "Hebdomadaire",
    },
    {
      code: "M",
      desc: "Mensuel",
    },
    {
      code: "Y",
      desc: "Annuel",
    },
  ];
  public data: any = {
    docstatus: "PEN",
  };

  constructor(inj: Injector) {
    super(inj);
  }

  public avarage: any;
  ngOnInit() {
    this.dmmDashboard.period = "T";
    this.dmmPeriod = "T";
    this.getCountryList();
    this.getCountOverstayDeparted();
    // this.getDashboardStatisticsTop5();
    this.getDmmDashboard();
    this.getAgentTracker(this.dmmPeriod);
    if ($("li.menu.active")) {
      $("li.open").removeClass("open");
      $("ul.sub-menu").css("display", "none");
    }
    this.getApplicationDetails(this.dmmPeriod);
  }

  average: any;
  getHumainDate(minutes) {
    var date = new Date();
    date.setMinutes(minutes);
    var current_time_milliseconds = date.getTime();

    this.average = moment.duration(current_time_milliseconds);
  }

  dmmPeriod: any;
  selectPeriod(event) {
    //console.log(event.code);
    this.dmmPeriod = event.code;
  }

  countryData: any = [];
  getCountryList() {
    this.commonService
      .callApi("mastercode/active/CNTRY", "", "get", false, false, "APPR")
      .then((success) => {
        this.countryData = success;
        this.countryData = this.countryData.masterCodeResultDTOs;
      })
      .catch((e) => {
        this.toastr.errorToastr(e.message, "Oops!");
      });
  }
  /****************************************************************************
  @PURPOSE      : To navigate to evisa search when we click view more.
  @PARAMETERS   : Closed/Pending
  @RETURN       : NA
  ****************************************************************************/
  viewMoreDetails(data) {
    this.setToken("dashboardAllocation", data);
    this.router.navigate(["/main/evisasearch"]);
  }

  /****************************************************************************/
  dashboard: any = {};
  counArray: any;
  onKeySearchUser(event) {
    this.counArray = event;
    //console.log(event);
    //console.log("event counArray");
  }

  searchResults() {
    this.getDmmDashboard();
    // this.getDashboardStatisticsTop5();
    this.getAgentTracker(this.dmmPeriod);
    this.getApplicationDetails(this.dmmPeriod);
  }

  /****************************************************************************
  @PURPOSE      : To get the Dashboard Allocation Details.
  @PARAMETERS   : NA
  @RETURN       : allocation Details Numbers
  ****************************************************************************/
  dmmAllocateDetails: any = {};
  dmmAllocateD: any = {};
  agentsDTO: any = {};
  value: any = {};

  getDmmDashboard() {
    let data: any = {
      period: this.dmmPeriod,
      loggeduser: this.getToken("username"),
    };
    if (this.counArray != null || this.counArray != undefined) {
      //console.log(this.dashboard);
      let countries = this.counArray.map((value) => value.code);
      data.countryList = countries;
    }

    //console.log(this.dmmPeriod, "dmmperiod");

    this.commonService
      .callApi("dmmdashboard", data, "post", false, false, "APPR")
      .then((success) => {
        this.dmmAllocateDetails = success;
        this.agentsDTO = this.dmmAllocateDetails.agentTrackerDTO;
        this.getHumainDate(
          this.agentsDTO?.average !== null ? this.agentsDTO?.average : 0
        );
        this.getDashboardStatistics();
        this.getborderDashboard();
        this.getPendingApplication();
        //console.log("regardant ici", success);
      })
      .catch((e) => {
        this.toastr.errorToastr(e.message, "Oops!");
      });
  }

  agentDTOs: any = {};
  getAgentTracker(period) {
    let data: any = {
      period: period,
      pageNumber: 1,
      pageSize: 10,
      reportType: "S",
      agentList: null,
      endDate: null,
      loggedUser: "idriss.djama",
      oprType: "BCO",
      role: "DMM",
      startDate: null,
    };
    this.commonService
      .callApi("performancereport", data, "post", false, false, "APPR")
      .then((success) => {
        let successData: any = success;
        const arrivals = successData.content
          .map((x) => x.arrival)
          .reduce((partialSum, a) => partialSum + a, 0);
        const departure = successData.content
          .map((x) => x.departure)
          .reduce((partialSum, a) => partialSum + a, 0);
        this.agentDTOs.arrival = arrivals;
        this.agentDTOs.departure = departure;
      });
  }

  /****************************************************************************
@PURPOSE      : To get the Dashboard Application Details.
@PARAMETERS   : NA
@RETURN       : Application Details Numbers
****************************************************************************/
  applicationDetails: any = {};
  getApplicationDetails(period) {
    let data = {
      period: period,
    };
    //console.log("merde par ici " + period);
    this.commonService
      .callApi("applicationDetails/", data, "post", false, false, "APPR")
      .then((success) => {
        this.applicationDetails = success[0];
      })
      .catch((e) => {
        this.toastr.errorToastr(e.message, "Oops!");
      });
  }
  /****************************************************************************/
  dashboardStatistics: any;
  visaStatus: any;
  ageStatus: any;
  ageStatusPeriod: any;
  countryStatus: any;
  countryStatusPeriod: any;
  month: any;
  age: any;
  count = [];
  donutchart: any;
  approved: any = [];
  rejected: any = [];
  pending: any = [];
  double: any;

  /*getDashboardStatisticsTop5() {
        let data: any = {
            "period": this.dmmPeriod,
            "loggeduser": this.getToken('username')
        }
     alert('oijforijroi');
     if (this.dashboard.countryList === '' ) {
            alert('counArrray null');
            this.commonService.callApi('top5',data,'post', false, false, 'APPR').then(success => {
                let successData: any = success;
                let ageStatus : any;
                ageStatus= successData.ageStats;
                 let countryData = successData.countryStats;
                //console.log('top 5 statistique')
                //console.log(countryData);
                //console.log(ageStatus);
                this.selectedAttributes = []
                //console.log(this.selectedAttributes, "144");

                //country status highcharts
                let countryList = []
                let res = []
                let  countryda = []
                if (countryData != null) {
                        //console.log('dans  data country')
                    //console.log(countryList);
                    for (var i = 0; i < 5 ; i++) {
                      //console.log('dans foreach')

                                res.push(countryData[i].statusCode);
                        countryda.push(countryData[i].count);

                    }
                }

                // pour iterer les resultat des noms
                this.selectedAttributes = res;
                //console.log(this.selectedAttributes, "country status");
                this.countryStatus = countryda;
                this.double=Object.values(ageStatus);
                //console.log('contry satus')
                //console.log(this.selectedAttributes);
                //console.log(this.countryStatus)
                this.getCountryStatus()
                this.getAgeStatus();

            });

        }

    }*/
  getborderDashboard() {
    this.commonService
      .callApi("overstay", "", "get", false, false, "APPR")
      .then((success) => {
        this.dmmAllocateD = success;
      })
      .catch((e) => {
        this.toastr.errorToastr(e.message, "Oops!");
      });

    //console.log(this.dmmAllocateD);
    //console.log(this.dmmAllocateD.pending, "pinding sursejourner");
  }
  getCountOverstayDeparted() {
    this.commonService
      .callApi("countoverstayDeparted", "", "post", false, false, "APPR")
      .then((success) => {
        this.counterOverstaydeparted = success;
      })
      .catch((e) => {
        this.toastr.errorToastr(e.message, "Oops!");
      });
  }
  getPendingApplication() {
    //console.log(this.dataa, " this.dataa");
    this.commonService
      .callApi(
        "applicationPendingdmm",
        this.dataa,
        "post",
        false,
        false,
        "APPR"
      )
      .then((success) => {
        this.pendingaplication = success;
      })
      .catch((e) => {
        this.toastr.errorToastr(e.message, "Oops!");
      });
  }
  getDashboardStatistics() {
    this.approved = [];
    this.rejected = [];
    this.pending = [];
    let successData: any = this.dmmAllocateDetails.dmmStatistics;
    let sucessDataB: any = this.dmmAllocateDetails.statsAgeContry;
    this.dashboardStatistics = successData.travelPurposeStats;
    this.visaStatus = successData.visaProcessingDTOs;
    this.ageStatus = successData.ageStats;
    let countryData = successData.countryStats;

    if (
      this.counArray == null ||
      this.counArray == undefined ||
      this.counArray.length == 0
    ) {
      this.ageStatus = sucessDataB.ageStatsPeriod;
      countryData = sucessDataB.countryStatsPeriod;
    }
    //console.log(this.ageStatus, "age");
    //console.log(countryData, "country data");
    this.selectedAttributes = [];
    //console.log(this.selectedAttributes, "144");

    //console.log(this.countryData);
    //country status highcharts
    //console.log(countryData, "au dessus");
    let countryList = [];
    //console.log(this.counArray, "array");
    if (
      this.counArray == null ||
      this.counArray == undefined ||
      this.counArray.length == 0
    ) {
      //console.log("au dans if");
      for (var i = 0; i < countryData.length; i++) {
        /* //console.log("au 2 dans fort 1");
            //console.log(this.countryData[i].code);
            //console.log(countryData[i].statusCode);*/
        for (let s = 0; s < this.countryData.length; s++) {
          //console.log(s);
          if (this.countryData[s].code === countryData[i].statusCode) {
            /* //console.log("au 2 dans fort 2");
                    //console.log(countryData[i].statusCode);*/
            //console.log(this.countryData[s].code);
            countryList.push(this.countryData[s]);
            break;
          }
        }
      }
    } else {
      countryList = this.counArray;
    }

    //console.log(countryList, " country list");

    let res = [];
    if (countryData != null) {
      for (var i = 0; i < countryData.length; i++) {
        for (let s = 0; s < countryList.length; s++) {
          if (countryData[i].statusCode == countryList[s].code) {
            countryData[i].description = countryList[s].description;
            var obj = {};
            obj[countryList[s].description] = countryData[i].count;
            //console.log(countryData[s].count, "country count");
            res.push(obj);
          }
        }
      }
    }
    var result = countryList.filter(function (o1) {
      return !countryData.some(function (o2) {
        return o1.code == o2.statusCode;
      });
    });

    for (var h = 0; h < result.length; h++) {
      var obj = {};
      obj[result[h].description] = 0;
      res.push(obj);
    }

    this.countryStatus = countryData;
    // pour iterer les resultat des noms
    this.selectedAttributes = res.map((x) => Object.keys(x)[0]);
    //console.log(this.countryStatus, "country status");
    this.countryStatus = res.map((x) => Object.values(x)[0]);

    //console.log(this.selectedAttributes, "mes seleectattribute");
    //console.log(this.countryStatus, "mes contry status");
    this.getCountryStatus();
    //age status highchart
    this.double = Object.values(this.ageStatus);
    //visa status highchart
    this.month = this.visaStatus.map(function (el) {
      return el.month;
    });
    if (this.visaStatus != null) {
      for (let i = 0; i < this.visaStatus.length; i++) {
        let fine = this.visaStatus[i].visaProcessed;
        for (let j = 0; j < 3; j++) {
          if (fine[j] != undefined) {
            fine[j].statusCode == "UP" && this.pending.push(fine[j].count);
            fine[j].statusCode == "APR" && this.approved.push(fine[j].count);
            fine[j].statusCode == "REJ" && this.rejected.push(fine[j].count);
          } else {
            let array2 = ["APR", "UP", "REJ"];
            let array1 = fine.map((a) => a.statusCode);
            var array3 = array2.filter(function (obj) {
              return array1.indexOf(obj) == -1;
            });
            for (let k = 0; k < array3.length; k++) {
              array3[k] == "UP" && this.pending.push(0);
              array3[k] == "APR" && this.approved.push(0);
              array3[k] == "REJ" && this.rejected.push(0);
            }
            break;
          }
        }
      }
    }
    //console.log(this.double, "double");
    //console.log(this.visaStatus, "visa status");
    //visa type highchart
    let final = this.dashboardStatistics;
    if (final != null) {
      for (let d = 0; d < final.length; d++) {
        final[d]["name"] = final[d].statusCode;
        final[d]["y"] = final[d].count;
      }
    }
    this.donutchart = final;
    this.getDonutPieChart();
    this.getVisaStatus();
    this.getAgeStatus();
    this.getCountryStatus();
  }
  /*************************************************************************************/

  /******************** Visa Type highchart****************************/
  getDonutPieChart() {
    //console.log("donut pie");

    Highcharts.chart("donutPie", {
      chart: {
        type: "pie",
      },
      title: {
        text: "",
      },
      credits: {
        enabled: false,
      },
      plotOptions: {
        pie: {
          innerSize: 80,
          depth: 60,
          dataLabels: {
            enabled: false,
          },
          showInLegend: true,
        },
      },
      series: [
        {
          type: "pie",
          name: "Demande de visa(s)",
          data: this.donutchart,
        },
      ],
    });
  }

  /******************** Visa status highchart****************************/
  getVisaStatus() {
    Highcharts.chart("container", {
      chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: "column",
      },
      title: {
        text: "",
      },
      credits: {
        enabled: false,
      },
      xAxis: {
        categories: this.month,
        crosshair: true,
      },
      yAxis: {
        min: 0,
        title: {
          text: "Nombre des demandes",
        },
      },
      plotOptions: {
        column: {
          pointPadding: 0.2,
          borderWidth: 0,
        },
      },
      series: [
        {
          name: "Approuvé",
          data: this.approved,
        },
        {
          name: "Rejeté",
          data: this.rejected,
        },
        {
          name: "En attente",
          data: this.pending,
        },
      ],
    });
  }

  /******************** age status highchart****************************/
  getAgeStatus() {
    Highcharts.chart("ageCharts", {
      chart: {
        type: "bar",
      },
      title: {
        text: "",
      },

      labels: {
        title: "India",
      },
      credits: {
        enabled: false,
      },
      xAxis: {
        categories: ["0-20", "21-40", "41-60", "60+"],
        crosshair: true,
      },
      yAxis: {
        min: 0,
        labels: {
          overflow: "justify",
        },
      },

      plotOptions: {
        column: {
          pointPadding: 0.2,
          borderWidth: 0,
        },
      },

      series: [
        {
          name: "Nombre de demandes",
          data: this.double,
        },
      ],
    });
  }
  isEmpty(val) {
    return val === undefined || val == null || val.length <= 0 ? true : false;
  }
  /******************** country status highchart****************************/
  getCountryStatus() {
    Highcharts.chart("horizontalBar", {
      chart: {
        type: "bar",
      },
      title: {
        text: "",
      },

      labels: {
        title: "India",
      },
      credits: {
        enabled: false,
      },
      xAxis: {
        categories: this.selectedAttributes,
        crosshair: true,
      },
      yAxis: {
        min: 0,
        labels: {
          overflow: "justify",
        },
      },

      plotOptions: {
        column: {
          pointPadding: 0.2,
          borderWidth: 0,
        },
      },

      series: [
        {
          name: "Nombre de demandes",
          data: this.countryStatus,
        },
      ],
    });
  }

  public modalType: any;
  modalRef: BsModalRef;
  onVisaType(template, type) {
    this.modalType = type;
    //console.log(this.modalType, "327");

    this.modalRef = this.modalService.show(template);
    //console.log(this.modalType, "329");

    if (this.modalType == "D") {
      //console.log(this.modalType, "donut");

      Highcharts.chart("popupDonutPie", {
        chart: {
          type: "pie",
        },
        title: {
          text: "Demande de visa(s)",
        },
        credits: {
          enabled: false,
        },
        plotOptions: {
          pie: {
            innerSize: 80,
            depth: 60,
            dataLabels: {
              enabled: false,
            },
            showInLegend: true,
          },
        },
        series: [
          {
            type: "pie",
            name: "Demande de visa(s)",
            data: this.donutchart,
          },
        ],
      });
    }
    if (this.modalType == "C") {
      //console.log(this.modalType, "container");

      Highcharts.chart("popupcontainer", {
        chart: {
          plotBackgroundColor: null,
          plotBorderWidth: null,
          plotShadow: false,
          type: "column",
        },
        title: {
          text: "Nombre de demandes",
        },
        credits: {
          enabled: false,
        },
        xAxis: {
          categories: this.month,
          crosshair: true,
        },
        yAxis: {
          min: 0,
          title: {
            text: "Applications",
          },
        },
        plotOptions: {
          column: {
            pointPadding: 0.2,
            borderWidth: 0,
          },
        },
        series: [
          {
            name: "Approuver",
            data: this.approved,
          },
          {
            name: "Rejeter",
            data: this.rejected,
          },
          {
            name: "En attente",
            data: this.pending,
          },
        ],
      });
    }

    if (this.modalType == "H") {
      Highcharts.chart("popuphorizontalBar", {
        chart: {
          type: "bar",
        },
        title: {
          text: "Countrywise Statistics",
        },

        labels: {
          title: "India",
        },
        credits: {
          enabled: false,
        },
        xAxis: {
          categories: this.selectedAttributes,
          crosshair: true,
        },
        yAxis: {
          min: 0,
          labels: {
            overflow: "justify",
          },
        },

        plotOptions: {
          column: {
            pointPadding: 0.2,
            borderWidth: 0,
          },
        },

        series: [
          {
            name: "Nombre de demandes",
            data: this.countryStatus,
          },
        ],
      });
    }

    if (this.modalType == "A") {
      Highcharts.chart("popupageCharts", {
        chart: {
          type: "bar",
        },
        title: {
          text: "",
        },

        labels: {
          title: "India",
        },
        credits: {
          enabled: false,
        },
        xAxis: {
          categories: ["0-20", "21-40", "41-60", "60+"],
          crosshair: true,
        },
        yAxis: {
          min: 0,
          labels: {
            overflow: "justify",
          },
        },

        plotOptions: {
          column: {
            pointPadding: 0.2,
            borderWidth: 0,
          },
        },

        series: [
          {
            name: "",
            data: this.double,
          },
        ],
      });
    }
  }
}
