import { Component, OnInit, Injector } from "@angular/core";
import { BaseComponent } from "../../common/commonComponent";
import { NgxUiLoaderService } from "ngx-ui-loader";

declare var $: any;
@Component({
  selector: "app-dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.css"],
})
export class DashboardComponent extends BaseComponent implements OnInit {
  borderDashboard: any = {};
  dmDashboard: any = {};
  extensiondashboard: any = {};
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

  public pagesize = 10;
  public statistiquecountData: number;
  public statistiquesumData: number;
  public page = 1;
  public message = null;
  public totalItem: any;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
    loggedUser: this.getToken("username"),
    date1: new Date(),
    date2: new Date(),
  };

  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj);
  }

  periodValue = "Y";
  ngOnInit(): void {
    this.extensiondashboard.datePeriod = "T";

    this.borderDashboard.period = "T";

    this.dmDashboard.period = "T";
    this.value.period = "T";
    this.dmPeriod = "T";
    this.getDashboardAllocations();
    this.removeToken("dashboardAllocation");
    this.removeToken("extensionEvisa");
    this.getDmmDashboard();
    this.getExtensionStatistics();
    this.getApplicantions();

    if ($("li.menu.active")) {
      $("li.open").removeClass("open");
      $("ul.sub-menu").css("display", "none");
    }

    this.value.loggedUser = this.getToken("username");
    this.value.role = this.getToken("Role");

    this.getborderDashboard(this.value);

    this.statistiqueSum(this.data.date1, this.data.date2);
    this.getArrdephistory(this.dmPeriod);
  }
  refreshPage() {}

  /****************************************************************************
        @PURPOSE      : To retrive List of Applicants.
        @PARAMETERS   : data
        @RETURN       : List of Applicants Details
  ****************************************************************************/
  applicantList: any = [];
  public number: 0;
  public size: 0;
  public noofelements: 0;
  public totalElements: 0;
  public loading: boolean = false;
  public sendApi: any;
  getApplicantList(data) {
    this.loading = true;
    if (this.getToken("Role") === "DM") {
      data.role = "DM";
      data.loggedUser = this.getToken("username");
    }

    if (this.getToken("Role") === "BCO" || this.getToken("Role") === "SBCO") {
      this.sendApi = "searchapplicationforarrivaldeparture";
      data.oprType = "A";
    } else {
      this.sendApi = "searchapplicationtracker";
    }
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService
        .callApi(this.sendApi, data, "post", false, false, "APPR")
        .then((success) => {
          let successData: any = success;
          this.totalItem = successData.totalElements;
          this.number = successData.number;
          this.size = successData.size;
          this.loading = false;
          this.ngxLoader.stop();
          this.noofelements = successData.numberOfElements;
          this.totalElements = successData.totalElements;
          if (successData.content.length) {
            this.applicantList = successData.content;
          } else {
            this.applicantList = [];
          }
        })
        .catch((e) => {
          this.ngxLoader.stop();
          this.toastr.errorToastr(e.message, "Oops!");
        });
    });
  }

  SBCOTracker: any = {};
  getArrdephistory(period) {
    let data: any = {
      loggedUser: this.getToken("username"),
      period: period,
    };
    this.commonService
      .callApi("arrdephistory", data, "post", false, false, "APPR")
      .then((success) => {
        let successData: any = success;
        this.SBCOTracker = successData[0];
      });
  }

  /****************************************************************************/
  /****************************************************************************
        @PURPOSE      : To get Allocated Files.
        @PARAMETERS   : NA
        @RETURN       : List of Allocated Details
  ****************************************************************************/
  getNextSetOfFiles() {
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService
        .callApi(
          "nextsetoffiles/" + this.getToken("username"),
          "",
          "get",
          false,
          false,
          "APPR"
        )
        .then((success) => {
          let successData: any = success;
          this.getDashboardAllocations();
          if (successData.apiStatusCode === "SUCCESS") {
            this.ngxLoader.stop();
            this.toastr.successToastr(successData.apiStatusDesc, "Success");
            this.getApplicantList(this.data);
          } else {
            this.ngxLoader.stop();
            this.toastr.errorToastr(successData.apiStatusDesc, "Error");
          }
        })
        .catch((e) => {
          this.ngxLoader.stop();
          this.toastr.errorToastr(e.message, "Oops!");
        });
    }, 1000);
  }
  /****************************************************************************/

  /****************************************************************************
  @PURPOSE      : To send the Application Number to Applicant Exists Method.
  @PARAMETERS   : $Event
  @RETURN       : Boolean
  ****************************************************************************/
  timeout: any = null;
  onKeySearchApplicant(event: any) {
    clearTimeout(this.timeout);
    let $this = this;
    this.timeout = setTimeout(function () {
      if (event.keyCode != 13) {
        $this.getApplicantExists(event.target.value);
        if (event.target.value) {
          event.target.value;
          $this.getApplicantExists(event.target.value);
        } else {
          this.isApplicantExists = true;
        }
      }
    }, 1000);
  }
  /****************************************************************************/

  /****************************************************************************
@PURPOSE      : To verify the Application Number Exists or not in DB.
@PARAMETERS   : Application Number
@RETURN       : Boolean
****************************************************************************/
  getApplicantExists(applicationNumber) {
    this.data.applicationNumber = applicationNumber;
    this.commonService
      .callApi(
        "searchapplicationtracker/",
        this.data,
        "post",
        false,
        false,
        "APPR"
      )
      .then((success) => {
        this.getApplicantList(this.data);
      })
      .catch((e) => {
        this.toastr.errorToastr(e.message, "Oops!");
      });
  }
  /****************************************************************************/

  /****************************************************************************
@PURPOSE      : To get the Dashboard Allocation Details.
@PARAMETERS   : NA
@RETURN       : allocation Details Numbers
****************************************************************************/
  allocationDetails: any = {};
  getDashboardAllocations() {
    if (this.getToken("Role") === "DM") {
      this.commonService
        .callApi(
          "dmdashboard/" + this.getToken("username") + "/" + this.dmPeriod,
          "",
          "get",
          false,
          false,
          "APPR"
        )
        .then((success) => {
          this.allocationDetails = success;
        })
        .catch((e) => {
          this.toastr.errorToastr(e.message, "Oops!");
        });
    }
  }
  /****************************************************************************/

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
  public Applications: any = [];
  public ArrNeedValidation: any = [];
  getApplicantions() {
    let data: any = {
      pageNumber: 1,
      pageSize: 100,
    };
    data.loggedUser = this.getToken("username");
    data.oprType = "A";
    this.commonService
      .callApi(
        "searchapplicationforarrivaldeparture",
        data,
        "post",
        false,
        false,
        "APPR"
      )
      .then((success) => {
        let successData: any = success;
        if (successData.content.length) {
          this.Applications = successData.content;
          this.Applications.forEach((element) => {
            if (element.arrDepIndicator === "AV") {
              this.ArrNeedValidation.push(element);
            }
          });
          //console.log(this.ArrNeedValidation);
        } else {
          this.Applications = [];
        }
      });
  }

  /****************************************************************************
@PURPOSE      : To get the Dashboard Allocation Details.
@PARAMETERS   : NA
@RETURN       : allocation Details Numbers
****************************************************************************/
  dmmAllocateDetails: any = {};
  getDmmDashboard() {
    if (this.getToken("Role") === "DMM") {
      this.commonService
        .callApi(
          "dmmdashboard/" + this.getToken("username"),
          "",
          "get",
          false,
          false,
          "APPR"
        )
        .then((success) => {
          this.dmmAllocateDetails = success;
        })
        .catch((e) => {
          this.toastr.errorToastr(e.message, "Oops!");
        });
    }
  }

  /****************************************************************************/
  dmPeriod: any;
  dmDashboardPeriod(event) {
    this.dmPeriod = event.code;
    this.getDashboardAllocations();
    this.getExtensionStatistics();
  }

  value: any = {};
  dashboardPeriod(event) {
    this.value.period = event.code;

    this.getborderDashboard(this.value);
    this.getArrdephistory(this.value.period);
  }

  getborderDashboard(value) {
    this.commonService
      .callApi("bordercontroldashboard", value, "post", false, false, "APPR")
      .then((success) => {
        this.dmmAllocateDetails = success;
      })
      .catch((e) => {
        this.toastr.errorToastr(e.message, "Oops!");
      });
  }

  nextSetOfExtensions() {
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService
        .callApi(
          "nextsetofextensionfiles/" + this.getToken("username"),
          "",
          "get",
          false,
          false,
          "APPR"
        )
        .then((success) => {
          let successData: any = success;
          if (successData.apiStatusCode === "SUCCESS") {
            this.ngxLoader.stop();
            this.toastr.successToastr(successData.apiStatusDesc, "Success");
          } else {
            this.ngxLoader.stop();
            this.toastr.errorToastr(successData.apiStatusDesc, "Error");
          }
        })
        .catch((e) => {
          this.ngxLoader.stop();
          this.toastr.errorToastr(e.message, "Oops!");
        });
    }, 1000);
  }

  ExtensionVisaPeriod(event) {
    this.extensiondashboard.datePeriod = event.code;
  }

  /****************************************************************************
@PURPOSE      : To get the Dashboard Allocation Details.
@PARAMETERS   : NA
@RETURN       : allocation Details Numbers
****************************************************************************/
  extensionStatistics: any = {};
  getExtensionStatistics() {
    if (this.getToken("Role") === "DM") {
      this.commonService
        .callApi(
          "dmdashboard/extension/" +
            this.getToken("username") +
            "/" +
            this.dmPeriod,
          "",
          "get",
          false,
          false,
          "APPR"
        )
        .then((success) => {
          this.extensionStatistics = success;
        })
        .catch((e) => {
          this.toastr.errorToastr(e.message, "Oops!");
        });
    }
  }

  /****************************************************************************/

  viewMoreExtensionDetails(data) {
    this.setToken("extensionEvisa", data);
    this.router.navigate(["/main/extensionevisasearch"]);
  }

  /*******************************************************************************/
  statistiqueCount(date1, date2) {
    this.data.date1 = date1;
    this.data.date2 = date2;
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService
        .callApi(
          "statictiquecountBysecretaire",
          this.data,
          "post",
          false,
          false,
          "APPR"
        )
        .then((success) => {
          let successData: any = success;

          this.statistiquecountData = successData.idPayementcash;
          if (this.statistiquecountData === null) {
            this.statistiquecountData = 0;
          }
          this.ngxLoader.stop();
        })
        .catch((e) => {
          this.ngxLoader.stop();
          this.toastr.errorToastr(e.message, "Oops!");
        });
    }, 1000);
  }

  statistiqueSum(date1, date2) {
    this.data.date1 = date1;
    this.data.date2 = date2;
    //console.log(this.data.date1, this.data.date2);
    if (this.data.date2 >= this.data.date1) {
      this.message = "";
      this.ngxLoader.start();
      setTimeout(() => {
        this.commonService
          .callApi(
            "statictiquesumBysecretaire",
            this.data,
            "post",
            false,
            false,
            "APPR"
          )
          .then((success) => {
            let successData: any = success;

            if (successData != null) {
              this.statistiquesumData = successData.amount;
            } else {
              this.statistiquesumData = 0;
            }
            //console.log(successData, "successData.amount");

            //console.log(this.statistiquesumData);

            this.statistiqueCount(this.data.date1, this.data.date2);
            this.ngxLoader.stop();
          })
          .catch((e) => {
            this.ngxLoader.stop();
            this.toastr.errorToastr(e.message, "Oops!");
          });
      }, 1000);
    } else {
      this.message = "La date fin est invalide";
    }
  }
}
