import { Component, Injector, OnInit } from "@angular/core";
import { BaseComponent } from "src/app/common/commonComponent";
import { NgxUiLoaderService } from "ngx-ui-loader";
import { TranslateService } from "@ngx-translate/core";
@Component({
  selector: "app-evisa-search",
  templateUrl: "./evisa-search.component.html",
  styleUrls: ["./evisa-search.component.css"],
})
export class EvisaSearchComponent extends BaseComponent implements OnInit {
  public pagesize = 100;
  public page = 1;
  isCollapsed = false;
  public totalItem: any;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  };

  constructor(
    inj: Injector,
    public translate: TranslateService,
    private ngxLoader: NgxUiLoaderService
  ) {
    super(inj);
    translate.use("fr");
    localStorage.setItem("Language", "fr");
    this.setToken("Language", "fr");
    //console.log("evisa search const");
  }

  visaDetails: any = {};

  visaStatus = [
    {
      name: "Yes",
      code: "Y",
    },
    {
      name: "No",
      code: "N",
    },
  ];

  visaTypeDetails = [
    {
      code: "Short Term Visa",
    },
    {
      code: "Long Term Visa",
    },
  ];

  statusDetails = [
    {
      code: "PEN",
      description: "Approbation en cours",
    },
    {
      code: "PAR",
      description: "Arriver en cours",
    },
    {
      code: "PD",
      description: "Départ en cours",
    },
    {
      code: "CLO",
      description: "Cloturer",
    },
    {
      code: "REJAR",
      description: "Rejeter à l'arrivée",
    },
    {
      code: "REJ",
      description: "Rejeter",
    },
    {
      code: "SUB",
      description: "Allocation en cours",
    },
    {
      code: "VAL",
      description: "Validation en cours",
    }
  ];

  applicanttypes = [
    {
      name: "Individual",
      code: "I",
    },
    {
      code: "G",
      name: "Group",
    },
  ];
  status = [
    {
      code: "PD",
      description: "Pending Departed",
    },

    {
      code: "CLS",
      description: "Closed",
    },
    {
      code: "PEN",
      description: "Pending Approval",
    },
    {
      code: "VAL",
      description: "Pending Validation",
    },
  ];
  sbcoStatus = [
    {
      code: "PA",
      description: "Pending Arrival",
    },

    {
      code: "VAL",
      description: "Pending Validation",
    },
  ];
  public result: boolean = false;
  ngOnInit(): void {
    if (this.getToken("Role") === "DMM") {
      this.data.docStatus = "VAL";
      this.filterObj.docStatus = "VAL";
      this.isFilterApplied = true;
    }

    if (this.getToken("Role") === "SBCO" || this.getToken("Role") === "BCO") {
      this.filterObj.arrDepIndicator = "PA";
      this.isFilterApplied = true;
      this.getVisaList(this.data);
    }

    if (this.getToken("Role") === "DM") {
      this.data.docStatus = "";
      this.filterObj.docStatus = "PEN";
      this.isFilterApplied = true;
    }

    if (this.getToken("Role") === "DMM" || this.getToken("Role") === "DM") {
      this.getVisaList(this.data);
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

  submitForm(visaDetailForm, visaDetails) {
    //console.log(visaDetails);
  }

  /****************************************************************************
       @PURPOSE      : To retrive the  applications List
       @PARAMETERS   : pageNumber,PageSize,loggedinuser
       @RETURN       : NA
    ****************************************************************************/

  public Applications: any = {};
  public number = 0;
  public size = 0;
  public numberOfElements = 0;
  public totalElements = 0;
  public capteur: any;
  public loading: boolean;
  public sendApi: any;
  numbero = 0;
  public demandes = [];

  getVisaList(data) {
    //console.log(data.docStatus);
    this.result = true;

    if (this.getToken("Role") === "DM") {
      data.loggedUser = this.getToken("username");
      data.fileNumber = this.getToken("fileNo");
    }

    if (this.getToken("dashboardAllocation") === "CLS") {
      data.docStatus = "CLS";
    } else if (this.getToken("dashboardAllocation") === "PEN") {
      data.docStatus = "PEN";
    } else if (this.getToken("dashboardAllocation") === "ALL") {
      data.docStatus = null;
    } else if (this.getToken("dashboardAllocation") === "SUB") {
      data.docStatus = "SUB";
    } else if (this.getToken("dashboardAllocation") === "REJ") {
      data.visaStatus = "REJ";
    }

    if (this.getToken("Role") === "BCO" || this.getToken("Role") === "SBCO") {
      this.sendApi = "searchapplicationforarrivaldeparture";
      data.oprType = "A";
    } else {
      this.sendApi = "searchapplicationtracker";
    }
    data.role = this.getToken("Role");
    if (data.docStatus === "PD") {
      this.capteur = "PD";
      data.docStatus = "CLS";
      data.arrDepIndicator = "PD";
      data.visaStatus = "";
    } else if (data.docStatus === "PAR") {
      this.capteur = "PAR";
      data.docStatus = "CLS";
      data.arrDepIndicator = "PA";
      data.visaStatus = "";
    } else if (data.docStatus === "CLO") {
      this.capteur = "CLO";
      data.docStatus = "CLS";
      data.arrDepIndicator = "CO";
      data.visaStatus = "";
    } else if (data.docStatus === "REJAR") {
      this.capteur = "REJAR";
      data.docStatus = "CLS";
      data.arrDepIndicator = "AR";
      data.visaStatus = "";
    } else if (data.docStatus === "REJ") {
      this.capteur = "REJ";
      data.docStatus = "CLS";
      data.visaStatus = "REJ";
      data.arrDepIndicator = null;
    } else {
      data.arrDepIndicator = "";
    }

    this.loading = true;
    this.ngxLoader.start();
    //console.log("aloo");
    setTimeout(() => {
      this.commonService
        .callApi(this.sendApi, data, "post", false, false, "APPR")
        .then((success) => {
          let successData: any = success;
          //console.log("addddd");
          this.totalItem = successData.totalElements;
          this.number = successData.number;
          this.size = successData.size;
          this.loading = false;
          this.ngxLoader.stop();
          this.numberOfElements = successData.numberOfElements;
          this.totalElements = successData.totalElements;
          //console.log(successData.content);
          let items: any = [];
          let numberzie = 0;
          this.Applications = successData.content;
          //console.log(this.Applications.length, "length");
          data.docStatus = this.capteur;
          //console.log(data.docStatus, "capteurrrrrrrrrrrrrrrrrrrrr");
          /** pour separe le dossier cls ( pinding arrival, p*/
          /*  if (successData.content.length) {
            if (data.docStatus === 'CLS'){
                 if (this.numbero === 1){
                     successData.content.forEach(function(item){
                         if (item.arrDepIndicator === 'PD') {
                             items.push(item);
                             numberzie++;
                         }
                     });
                 }
                 else if ( this.numbero === 2){
                     successData.content.forEach(function(item){
                         if (item.arrDepIndicator === 'PA' || item.arrDepIndicator === 'AV') {
                             items.push(item);
                             numberzie++;
                         }
                     });
                 }
                 else if (this.numbero === 3){
                     successData.content.forEach(function(item){
                         if (item.arrDepIndicator === 'CO' || item.arrDepIndicator === 'AR' || item.visaStatus === 'REJ') {
                             items.push(item);
                             numberzie++;
                         }
                     });
                 }
                 else  {
                     this.Applications = [];
                 }
                this.Applications = items;
            }else{
                this.Applications = successData.content;
            }
        } else {

          this.Applications = [];
        }
        //console.log(this.Applications);  */
        })
        .catch((e) => {
          this.ngxLoader.stop();
          this.toastr.errorToastr(e.message, "Oops!");
        });
    }, 1000);
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
      this.getVisaList(this.filterObj);
      //console.log(this.getVisaList(this.filterObj), "rangeChanged");
    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getVisaList(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      this.filterObj.pageNumber = 1;
      this.filterObj.pageSize = e;
      this.getVisaList(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getVisaList(this.data);
    }

    //console.log(this.getVisaList(this.filterObj), "rangeChanged");
  }
  /***************************************************************/

  /****************************************************************************
      @PURPOSE      : filters.
      @PARAMETERS   : NA
      @RETURN       : NA
   ****************************************************************************/
  applyFilter() {
    if (JSON.stringify(this.filterObj) == "{}") {
      if (localStorage.getItem("Language") === "en") {
        this.toastr.errorToastr(
          "Please Select Any Value  Data to Filter",
          "Oops!"
        );
        return 0;
      } else {
        this.toastr.errorToastr("Veuillez filtrer par categorie", "Oops!");
        return 0;
      }
    }
    this.isFilterApplied = true;
    this.currentPage = 1;
    this.filterObj.pageNumber = 1;
    this.filterObj.pageSize = this.pagesize;
    //console.log(this.filterObj, "filtresobjeeeeeeeeeeee");
    this.getVisaList(this.filterObj);
    this.removeToken("dashboardAllocation");
  }
  resetFilter() {
    this.isFilterApplied = false;
    this.currentPage = 1;
    this.filterObj = {};
    //console.log(this.data);
    this.removeToken("dashboardAllocation");
    if (this.data.docStatus) {
      delete this.data.docStatus;
      //console.log(this.data.docStatus);
    }
    //console.log(this.data);
    this.getVisaList(this.data);
  }
  /****************************************************************************/
  public fileNumber: any;
  clickApp(file, status, arrdepid, arrdepind) {
    this.setToken("fileNo", file);
    this.setToken("docStatus", status);
    this.setToken("arrDepId", arrdepid);
    this.setToken("arrDepIndicator", arrdepind);
  }

  spaceTrim(event) {
    this.filterObj.applicationNumber = this.filterObj.applicationNumber.trim();
  }

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
          this.getVisaList(this.data);
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
  /****************************************************************************/
}
