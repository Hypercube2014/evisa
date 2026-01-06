
import { Component, Injector, OnInit } from "@angular/core";
import { NgxUiLoaderService } from "ngx-ui-loader";
import { TranslateService } from "@ngx-translate/core";
import { BaseComponent } from "src/app/common/commonComponent";


@Component({
  selector: 'app-evisa-search-dm',
  templateUrl: './evisa-search-dm.component.html',
  styleUrls: ['./evisa-search-dm.component.css']
})
export class EvisaSearchDmComponent extends BaseComponent implements OnInit {

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
 

    if ( this.getToken("Role") === "DM") {
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

    //console.log("getVisaList");

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

 
  this.sendApi = "searchapplicationtracker";
    
    data.role = this.getToken("Role");
    data.docStatus = "CLS";
    
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
           // Limiter le nombre d'éléments à 1000
      this.Applications = successData.content.slice(0, 1000);
      
          //console.log(this.Applications.length, "length (limité à 1000)");
          data.docStatus = this.capteur;
          //console.log(data.docStatus, "capteurrrrrrrrrrrrrrrrrrrrr"); 
      
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

  spaceTrim(event) {
    this.filterObj.applicationNumber = this.filterObj.applicationNumber.trim();
  }
  // Propriétés pour le téléchargement de visa
  public downloadApplicationNumber: string = '';
  public isDownloading: boolean = false;
  public downloadError: string = '';

  /**
   * Valider le numéro de dossier
   */
  validateApplicationNumber(event: any) {
    const value = event.target.value;
    this.downloadApplicationNumber = value.trim().toUpperCase();
    this.downloadError = '';
  }

  /**
   * Effacer le formulaire de téléchargement
   */
  clearDownloadForm() {
    this.downloadApplicationNumber = '';
    this.downloadError = '';
  }


}




