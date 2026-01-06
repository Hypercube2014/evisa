import { HttpClient } from "@angular/common/http";
import {
  Component,
  ElementRef,
  Injector,
  OnInit,
  ViewChild,
} from "@angular/core";
import * as moment from "moment";
import { BsDatepickerConfig, BsLocaleService } from "ngx-bootstrap/datepicker";
import { BsModalRef, BsModalService } from "ngx-bootstrap/modal";
import { TabsetComponent } from "ngx-bootstrap/tabs";
import { NgxUiLoaderService } from "ngx-ui-loader";
import { BaseComponent } from "../../../common/commonComponent";
declare var require: any;
declare var $: any;
var FileSaver = require("file-saver");
@Component({
  selector: "app-visa-details",
  templateUrl: "./visa-details.component.html",
  styleUrls: ["./visa-details.component.css"],
})
export class VisaDetailsComponent extends BaseComponent implements OnInit {
  locale = localStorage.getItem("Language");

  @ViewChild("staticTabs", { static: true }) staticTabs: TabsetComponent;
  @ViewChild("passportFile") passportVariable: ElementRef;
  @ViewChild("photographFile") photographVariable: ElementRef;
  @ViewChild("ticketFile") ticketVariable: ElementRef;
  @ViewChild("hotelBookingFile") hotelBookingVariable: ElementRef;
  @ViewChild("newAttachFile") newAttachmentVariable: ElementRef;
  @ViewChild("TransitBook") transitAttachmentVariable: ElementRef;
  @ViewChild("closebutton") closebutton;
  modalRef: BsModalRef;
  public personalInfo: any = {};
  public attachmentsInfo: any = {};
  public travelInfo: any = {};
  public isNew: boolean = false;
  public appNumber: any;
  sixMonthsFromNow = moment().add(6, "months").toDate();
  ThreeMonthFromNow = moment().add(1, "months").toDate();
  FromNow = moment().toDate();
  bsConfig: Partial<BsDatepickerConfig>;
  arrConfig: Partial<BsDatepickerConfig>;
  depConfig: Partial<BsDatepickerConfig>;
  expConfig: Partial<BsDatepickerConfig>;
  colorTheme = "theme-blue";
  public visaDetails = [
    {
      code: "Self",
      value: "S",
    },
    {
      code: "Others",
      value: "O",
    },
  ];

  public countryCode = [
    {
      code: +91,
      value: "India",
      numLength: 10,
    },
    {
      code: +1,
      value: "United States",
      numLength: 10,
    },
    {
      code: +355,
      value: "Albania ",
      numLength: 9,
    },
    {
      code: +592,
      value: "Guyana",
      numLength: 7,
    },
  ];

  fileNumber: any;

  public today = new Date();
  public internetdate = this.commonService
    .callApi("", "", "", "", "", "", "DateTime")
    .then((success) => {
      let successData: any = success;
      return successData.date;
    });

  new_date: any;
  datePickerConfig = {
    dateInputFormat: "DD-MM-YYYY",
    containerClass: this.colorTheme,
    maxDate: new Date(),
    showWeekNumbers: false,
  };
  showText: boolean = false;
  constructor(
    inj: Injector,
    private modalService: BsModalService,
    private ngxLoader: NgxUiLoaderService,
    private localeService: BsLocaleService,
    private _http: HttpClient
  ) {
    super(inj);
    this.localeService.use(this.locale);
    ////console.log('merd',this.internetdate)
    this.activatedRoute.params.subscribe((params) => {
      if (params["id"] === "new") {
        this.isNew = true;
      } else {
        this.appNumber = params["id"];
        this.setToken("applicationNumber", this.appNumber);
        this.getPersonalData(this.appNumber);
        this.isNew = false;
      }
    });

    if (this.getToken("expressVisa") === "true") {
      this.new_date = moment(new Date(), "DD-MM-YYYY").add(2, "days").toDate();
    } else {
      this.new_date = moment(new Date(), "DD-MM-YYYY").add(7, "days").toDate();
    }

    //this.getInternetDate();

    this.arrConfig = Object.assign(
      {},
      {
        containerClass: this.colorTheme,
        customTodayClass: "custom-today-class",
        showWeekNumbers: false,
        dateInputFormat: "DD-MM-YYYY",
        minDate: this.new_date,
        //maxDate: this.travelInfo.expiryDate,
      }
    );

    this.expConfig = Object.assign(
      {},
      {
        containerClass: this.colorTheme,
        customTodayClass: "custom-today-class",
        showWeekNumbers: false,
        dateInputFormat: "DD-MM-YYYY",
        minDate: this.sixMonthsFromNow,
      }
    );
    this.depConfig = Object.assign(
      {},
      {
        containerClass: this.colorTheme,
        customTodayClass: "custom-today-class",
        showWeekNumbers: false,
        dateInputFormat: "DD-MM-YYYY",
      }
    );
  }

  ngOnInit(): void {
    this.personalInfo.appliedFor = "";
    this.personalInfo.title = "";
    this.personalInfo.gender = "";
    this.personalInfo.birthCountry = "";
    this.personalInfo.maritalStatus = "";
    this.personalInfo.nationality = "";
    this.personalInfo.currentNationality = "";
    this.personalInfo.preferredLanguage = "";
    this.personalInfo.residenceCountry = "";
    this.personalInfo.originCountry = "";
    this.personalInfo.countryofCode = "";

    this.travelInfo.issuedCountry = "";
    this.travelInfo.expiryDate = "";
    this.travelInfo.travelPurpose = "";
    this.travelInfo.arrivalLocation = "JIB";
    this.travelInfo.lastVisitedCountries = [];
    this.staticTabs.tabs[1].disabled = true;
    this.staticTabs.tabs[2].disabled = true;
    this.staticTabs.tabs[3].disabled = true;
    this.getInternetDate();
    this.getCountrys("CNTRY");
    this.getNationalities("CNTRY");
    this.getLanguages("PRLNG");
    this.getTraveldetails("POT");
    this.getLocationdetails("ARLOC");
    this.getMarital("MRT");

    this.fileNumber = this.getToken("fileNumber");

    // //console.log('rrrrrrrrrrrrr', this.getToken('visaType'), this.getToken('travelDetails'))
  }

  public cnfmMail: boolean = false;
  onValidation() {
    if (
      this.personalInfo.emailAddress.toLowerCase() ===
      this.personalInfo.cemail.toLowerCase()
    ) {
      this.cnfmMail = true;
    } else {
      this.cnfmMail = false;
    }
  }

  /****************************************************************************
      @PURPOSE      : Getting date time from internet
      @RETURN       : NA
   ****************************************************************************/

  getInternetDate() {
    this.commonService
      .callApi("", "", "", "", "", "", "DateTime")
      .then((success) => {
        let successData: any = success;
        if (this.getToken("expressVisa") === "true") {
          this.new_date = moment(new Date(), "DD-MM-YYYY")
            .add(2, "days")
            .toDate();
        } else {
          this.new_date = moment(new Date(), "DD-MM-YYYY")
            .add(7, "days")
            .toDate();
        }
        // //console.log(this.new_date + ' rrrrrrrrrrrrrrrrrrrrrrr')
        this.arrConfig = Object.assign(
          {},
          {
            containerClass: this.colorTheme,
            customTodayClass: "custom-today-class",
            showWeekNumbers: false,
            dateInputFormat: "DD-MM-YYYY",
            minDate: this.new_date,
            //maxDate: this.ThreeMonthFromNow,
          }
        );
      })
      .catch((e) => {
        this.ngxLoader.stop();
        this.toastr.error(e.message, "Oops!");
      });
  }

  formatDateWithoutTimeZone = (date: Date | string): string => {
    if (!date) return null;
    
    const localDate = date instanceof Date ? date : new Date(date);
    
    const year = localDate.getFullYear();
    const month = String(localDate.getMonth() + 1).padStart(2, "0");
    const day = String(localDate.getDate()).padStart(2, "0");
    
    return `${year}-${month}-${day}`;
  };
  /****************************************************************************
      @PURPOSE      : saving personal Information
      @PARAMETERS   :form and form data
      @RETURN       : NA
   ****************************************************************************/
  submitted: Boolean = false;
  phoneLength: boolean = false;
  submitPersonalInfo(form, personalInfo) {
    personalInfo.fileNumber = this.getToken("fileNumber");

    if (personalInfo.emailAddress != undefined) {
      personalInfo.emailAddress = personalInfo.emailAddress.toLowerCase();
    }
    /* if (this.personalInfo.dateOfBirth != undefined) {
      this.personalInfo.dateOfBirth = this.formatDateWithoutTimeZone(this.personalInfo.dateOfBirth)
    } */
    this.phoneLength = true;
    // if (this.personalInfo.phoneNumber.length == this.numLength) {
    //   this.phoneLength = true;
    // } else {
    //   this.phoneLength = false;
    // }
    this.submitted = true;
    if (form.valid && !this.isValid) {
      //personalInfo.dateOfBirth = this.convertDateFormat(personalInfo.dateOfBirth);
      this.ngxLoader.start();
      setTimeout(() => {
        this.personalInfo.dateOfBirth = this.formatDateWithoutTimeZone(
          this.personalInfo.dateOfBirth
        );
        this.commonService
          .callApi(
            "applicantpersonalinfo/",
            personalInfo,
            "post",
            false,
            false,
            "REG"
          )
          .then((success) => {
            let successData: any = success;
            // //console.log(successData);
            if (successData.apiStatusCode === "SUCCESS") {
              this.setToken("applicationNumber", successData.applicationNumber);
              this.appNumber = successData.applicationNumber;
              form.reset();
              form.submitted = false;
              this.ngxLoader.stop();
              this.staticTabs.tabs[1].disabled = false;
              this.staticTabs.tabs[0].disabled = true;
              this.staticTabs.tabs[2].disabled = true;
              this.staticTabs.tabs[3].disabled = true;
              this.staticTabs.tabs[1].active = true;
              //this.toastr.success("success", successData.apiStatusDesc)
              this.toastr.success(
                "Mise à jour effectuée avec succès",
                "Success"
              );
            } else {
              this.ngxLoader.stop();
              this.submitted = false;
              this.toastr.error("error", successData.apiStatusDesc);
            }
          })
          .catch((e) => {
            this.ngxLoader.stop();
            this.toastr.error(e.message, "Oops!");
          });
      }, 1000);
      this.gotoTop();
    } else {
      this.submitted = true;
      setTimeout(() => {
        $("html, body").animate(
          {
            scrollTop: $(".is-invalid").offset().top - 70,
          },
          1000
        );
      });
    }
  }
  /****************************************************************************/

  /****************************************************************************
      @PURPOSE      : saving travel Information
      @PARAMETERS   :form and form data
      @RETURN       : NA
   ****************************************************************************/
  // submitted: Boolean = false;
  submitTravelInfo(form, travelInfo) {
    travelInfo.applicationNumber = this.getToken("applicationNumber")
      ? this.getToken("applicationNumber")
      : this.appNumber;
    this.fileNumber = this.getToken("fileNumber");
    travelInfo.lastVisitedCountries =
      travelInfo.lastVisitedCountries.toString();

    // travelInfo.issuedDate = this.convertDateFormat(travelInfo.issuedDate);
    // travelInfo.expiryDate = this.convertDateFormat(travelInfo.expiryDate);
    // travelInfo.arrivalDate = this.convertDateFormat(travelInfo.arrivalDate);
    // travelInfo.departureDate = this.convertDateFormat(travelInfo.departureDate);
    this.submitted = true;
    if (
      form.valid &&
      !this.dateError &&
      !this.isissueValid &&
      !this.isexpValid &&
      !this.isarrValid
    ) {
      this.ngxLoader.start();
      setTimeout(() => {
        if (this.travelInfo.issuedDate != undefined) {
          this.travelInfo.issuedDate = this.formatDateWithoutTimeZone(
            this.travelInfo.issuedDate
          );
        }

        if (this.travelInfo.expiryDate != undefined) {
          this.travelInfo.expiryDate = this.formatDateWithoutTimeZone(
            this.travelInfo.expiryDate
          );
        }
        if (this.travelInfo.departureDate != undefined) {
          travelInfo.departureDate = this.formatDateWithoutTimeZone(travelInfo.departureDate);
        }
        this.commonService
          .callApi(
            "applicanttravelinfo/",
            travelInfo,
            "post",
            false,
            false,
            "REG"
          )
          .then((success) => {
            let successData: any = success;
            if (successData.apiStatusCode === "SUCCESS") {
              form.reset();
              this.ngxLoader.stop();
              this.staticTabs.tabs[2].disabled = false;
              this.staticTabs.tabs[0].disabled = true;
              this.staticTabs.tabs[1].disabled = true;
              this.staticTabs.tabs[3].disabled = true;
              this.staticTabs.tabs[2].active = true;
              //this.toastr.success("success", successData.apiStatusDesc)
              this.toastr.success(
                "Mise à jour effectuée avec succès",
                "Success"
              );
            }  else {
              this.ngxLoader.stop();
              this.submitted = false;
              //console.log(successData, "merde c'est ici!")
              this.toastr.error("error", successData.apiStatusDesc);
            }
          })
          .catch((e) => {
            this.toastr.error(e.message, "Oops!");
          });
      }, 1000);
    } else {
      setTimeout(() => {
        $("html, body").animate({
          scrollTop: $(".is-invalid").offset().top - 70,
        });
      });
    }

    this.gotoTop();
  }
  /****************************************************************************/

  /****************************************************************************
      @PURPOSE      : Retriving preview data
      @PARAMETERS   : NA
      @RETURN       : NA
   ****************************************************************************/
  public passportPreview: any;

  getTravelData() {
    this.commonService
      .callApi(
        "applicanttravelinfo/" + this.getToken("applicationNumber"),
        "",
        "get",
        false,
        false,
        "REG"
      )
      .then((success) => {
        let successData: any = success;
        if (successData) {
          let visitedCountries = successData.lastVisitedCountries.split(",");
          this.passportPreview = successData;
          this.travelInfo = successData;
          let countries = visitedCountries.filter((item) => item);
          this.travelInfo.lastVisitedCountries =
            countries.length > 0 ? countries : "";

          // this.travelInfo.lastVisitedCountries = successData.lastVisitedCountries.split(',')
          this.travelInfo.issuedDate = new Date(successData.issuedDate);
          // this.arrConfig.maxDate=new Date(successData.expiryDate)
          this.travelInfo.expiryDate = new Date(successData.expiryDate);
          this.travelInfo.arrivalDate = new Date(successData.arrivalDate);
          this.arrivalChange(this.travelInfo.arrivalDate);
          /*this.travelInfo.departureDate =
            successData.departureDate === null
              ? ""
              : new Date(successData.departureDate);*/
          this.travelInfo.departureDate = new Date(successData.departureDate);
          let startDate = moment(this.travelInfo.arrivalDate, "DD-MM-YYYY");
          let endDate = moment(this.travelInfo.departureDate, "DD-MM-YYYY");

          let dayDiff = endDate.diff(startDate, "days");
          // let id = this.getToken('visaType');
          // let lastChar = id.substr(id.length - 1);
          // let visaType = this.getToken('visaType').replace(/[^\d.-]/g, '');
          // let final=this.daysCalculate(lastChar,visaType);
          // //console.log(final);
          // if (dayDiff > parseInt(visaType)) {
          //   this.dateError = true;
          // } else {
          //   this.dateError = false;
          // }
          // //let dayDiff = this.travelInfo.departureDate.diff(this.travelInfo.arrivalDate, 'days');
          // //console.log(dayDiff)
        }
      })
      .catch((e) => {
        this.toastr.error(e.message, "Oops!");
      });
  }
  /****************************************************************************/

  /****************************************************************************
      @PURPOSE      : Retriving personal data
      @PARAMETERS   : NA
      @RETURN       : NA
   ****************************************************************************/

  /****************************************************************************
      @PURPOSE      : Retriving personal data
      @PARAMETERS   : NA
      @RETURN       : NA
   ****************************************************************************/
  public personalPreview: any;
  public getpurpose: any;
  public senData: any;
  getPersonalData(appNo?) {
    if (appNo) {
      this.senData = appNo;
    } else {
      this.senData = this.getToken("applicationNumber");
    }
    // this.ngxLoader.start();
    setTimeout(() => {
      this.commonService
        .callApi(
          "applicantpersonalinfo/" + this.senData,
          "",
          "get",
          false,
          false,
          "REG"
        )
        .then((success) => {
          let successData: any = success;
          //this.personalPreview = successData;
          this.personalInfo = successData;
          // //console.log("date birth day1")
          // //console.log(successData.dateOfBirth);
          this.personalInfo.dateOfBirth = new Date(successData.dateOfBirth);
          // //console.log("date birth day2")
          // //console.log(this.personalInfo.dateOfBirth);
          // this.ngxLoader.stop();
        })
        .catch((e) => {
          this.toastr.error(e.message, "Oops!");
        });
    });
  }
  /****************************************************************************/

  /****************************************************************************
      @PURPOSE      : date picker value change
      @PARAMETERS   : NA
      @RETURN       : NA
   ****************************************************************************/
  isValid: boolean = false;
  isissueValid: boolean = false;
  isexpValid: boolean = false;
  isarrValid: boolean = false;
  isdepValid: boolean = false;
  onValueChange(e, type) {
    if (type === "DOB") {
      if (e > this.datePickerConfig.maxDate) {
        this.isValid = true;
      } else {
        this.isValid = false;
      }
    } else if (type === "DOE") {
      this.arrConfig.maxDate = e;
      const date1 = moment(e).format("DD/MM/YYYY");
      const date2 = moment(this.expConfig.minDate).format("DD/MM/YYYY");

      if (e) {
        if (this.process(date1) < this.process(date2)) {
          this.isexpValid = true;
        } else {
          this.isexpValid = false;
        }
      }
    } else if (type === "DOI") {
      if (e) {
        if (e > this.datePickerConfig.maxDate) {
          this.isissueValid = true;
        } else {
          this.isissueValid = false;
        }
      }
    } else if (type === "DOA") {
      const date1 = moment(e).format("DD/MM/YYYY");
      const date2 = moment(this.arrConfig.minDate).format("DD/MM/YYYY");
      const date3 = moment(this.travelInfo.expiryDate).format("DD/MM/YYYY");

      if (e) {
        if (this.process(date1) < this.process(date2)) {
          this.isarrValid = true;
        } else if (this.process(date1) > this.process(date3)) {
          this.isarrValid = true;
        } else {
          this.isarrValid = false;
        }
      }

      // Mettre à jour la date minimale et maximale pour departureDate pour empêcher la même date
      this.depConfig.minDate = moment(e).add(1, "day").toDate();
      let visaType = this.getToken("visaType").replace(/[^\d.-]/g, "");
      console.log(visaType + " efefer mzerde");
      if (parseInt(visaType) != 15) {
        this.depConfig.maxDate = moment(e).add(30, "days").toDate();
      } else {
        this.depConfig.maxDate = moment(e).add(visaType, "days").toDate();
      }
    } else if (type === "DOD") {
      const date1 = moment(e).format("DD/MM/YYYY");
      const date2 = moment(this.travelInfo.arrivalDate).format("DD/MM/YYYY");

      if (e) {
        // Vérifier si departureDate est identique à arrivalDate
        if (this.process(date1) === this.process(date2)) {
          this.dateError = true;
        } else {
          this.dateError = false;
        }
      }
    }

    // //console.log(`${e} mis à jour pour le type ${type}`);
  }

  /****************************************************************************/

  /****************************************************************************
     @PURPOSE      : file upload
     @PARAMETERS   : $event
     @RETURN       : NA
  ****************************************************************************/
  public passportFile: any;
  public photographFile: any;
  public ticketBookingFile: any;
  public hotelBookingFile: any;
  public transitBook: any;
  public newAttachment: any;
  public clonedFile: any;
  fileUpload(event, type) {
    const file = event.target.files[0];

    const slice = file.slice(0, file.size);
    let uint8Array = null;

    const isTextFile = (uint8Array) => {
      return uint8Array.every(
        (byte) => (byte >= 32 && byte <= 126) || byte === 10 || byte === 13
      );
    };
    const readFileAsArrayBuffer = new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.onload = function () {
        resolve(reader.result);
      };

      reader.onerror = function () {
        reject(new Error("File read failed"));
      };

      reader.readAsArrayBuffer(slice);
    });

    if (event.target.files && event.target.files.length > 0) {
      if (type === "PP") {
        readFileAsArrayBuffer
          .then((arrayBuffer: ArrayBufferLike) => {
            uint8Array = new Uint8Array(arrayBuffer);
            if (isTextFile(uint8Array)) {
              this.toastr.error(
                "Format Fichier incorrect, veuillez télécharger un fichier au format approprié."
              );

              this.passportVariable.nativeElement.value = "";
              this.attachmentsInfo.passPort = "";
            } else {
              if (
                event.target.files[0].size > 250000 ||
                !this.validateFile(
                  event.target.files[0].name,
                  type,
                  event.target.files[0].type
                )
              ) {
                if (localStorage.getItem("Language") === "en") {
                  this.toastr.error("File size is too long");
                } else {
                  this.toastr.error("La taille du fichier est trop long");
                }
                this.passportVariable.nativeElement.value = "";
                this.attachmentsInfo.passPort = "";
              } else {
                this.passportFile = event.target.files[0];
                this.clonedFile = { ...event.target.files[0] };
              }
              // ----------
            }
          })
          .catch((error) => {
            console.error(error);
          });
      } else if (type === "PG") {
        readFileAsArrayBuffer
          .then((arrayBuffer: ArrayBufferLike) => {
            uint8Array = new Uint8Array(arrayBuffer);
            if (isTextFile(uint8Array)) {
              this.toastr.error(
                "Format Fichier incorrect, veuillez télécharger un fichier au format approprié."
              );

              this.photographVariable.nativeElement.value = "";
              this.attachmentsInfo.Photograph = "";
            } else {
              // ----------
              if (
                event.target.files[0].size > 250000 ||
                !this.validateFile(
                  event.target.files[0].name,
                  type,
                  event.target.files[0].type
                )
              ) {
                if (localStorage.getItem("Language") === "en") {
                  this.toastr.error("The file size is too large..");
                } else {
                  this.toastr.error("La taille du fichier est trop grande.");
                }
                this.photographVariable.nativeElement.value = "";
                this.attachmentsInfo.Photograph = "";
              } else {
                this.photographFile = event.target.files[0];
              }
              // ----------
            }
          })
          .catch((error) => {
            console.error(error);
          });
      } else if (type === "TR") {
        readFileAsArrayBuffer
          .then((arrayBuffer: ArrayBufferLike) => {
            uint8Array = new Uint8Array(arrayBuffer);
            if (isTextFile(uint8Array)) {
              this.toastr.error(
                "Format Fichier incorrect, veuillez télécharger un fichier au format approprié."
              );

              this.ticketVariable.nativeElement.value = "";
              this.attachmentsInfo.ticketReservation = "";
            } else {
              // ----------
              if (
                event.target.files[0].size > 250000 ||
                !this.validateFile(
                  event.target.files[0].name,
                  type,
                  event.target.files[0].type
                )
              ) {
                // this.isTRSize=true;
                if (localStorage.getItem("Language") === "en") {
                  this.toastr.error("The file size is too large.");
                } else {
                  this.toastr.error("La taille du fichier est trop grande.");
                }
                this.ticketVariable.nativeElement.value = "";
                this.attachmentsInfo.ticketReservation = "";
              } else {
                this.ticketBookingFile = event.target.files[0];
              }
              // ----------
            }
          })
          .catch((error) => {
            console.error(error);
          });
      } else if (type === "HB") {
        readFileAsArrayBuffer
          .then((arrayBuffer: ArrayBufferLike) => {
            uint8Array = new Uint8Array(arrayBuffer);
            if (isTextFile(uint8Array)) {
              this.toastr.error(
                "Format Fichier incorrect, veuillez télécharger un fichier au format approprié."
              );

              this.hotelBookingVariable.nativeElement.value = "";
              this.attachmentsInfo.hotelBooking = "";
            } else {
              // ----------
              if (
                event.target.files[0].size > 250000 ||
                !this.validateFile(
                  event.target.files[0].name,
                  type,
                  event.target.files[0].type
                )
              ) {
                // this.isHBSize=true;
                if (localStorage.getItem("Language") === "en") {
                  this.toastr.error("The file size is too large.");
                } else {
                  this.toastr.error("La taille du fichier est trop grande.");
                }
                this.hotelBookingVariable.nativeElement.value = "";
                this.attachmentsInfo.hotelBooking = "";
              } else {
                this.hotelBookingFile = event.target.files[0];
              }
              // ----------
            }
          })
          .catch((error) => {
            console.error(error);
          });
      } else if (type === "TS") {
        readFileAsArrayBuffer
          .then((arrayBuffer: ArrayBufferLike) => {
            uint8Array = new Uint8Array(arrayBuffer);
            if (isTextFile(uint8Array)) {
              this.toastr.error(
                "Format Fichier incorrect, veuillez télécharger un fichier au format approprié."
              );

              this.transitAttachmentVariable.nativeElement.value = "";
              this.attachmentsInfo.transitBook = "";
            } else {
              // ----------
              if (
                event.target.files[0].size > 250000 ||
                !this.validateFile(
                  event.target.files[0].name,
                  type,
                  event.target.files[0].type
                )
              ) {
                // this.isHBSize=true;
                if (localStorage.getItem("Language") === "en") {
                  this.toastr.error("File size is too long.");
                } else {
                  this.toastr.error("La taille du fichier est trop long.");
                }
                this.transitAttachmentVariable.nativeElement.value = "";
                this.attachmentsInfo.transitBook = "";
              } else {
                this.transitBook = event.target.files[0];
              }
              // ----------
            }
          })
          .catch((error) => {
            console.error(error);
          });
      } else if (type === "NA") {
        readFileAsArrayBuffer
          .then((arrayBuffer: ArrayBufferLike) => {
            uint8Array = new Uint8Array(arrayBuffer);
            if (isTextFile(uint8Array)) {
              this.newAttachment = "";
            } else {
              // ----------
              if (event.target.files[0].size > 250000) {
                // this.isNASize=true;
              } else {
                this.newAttachment = event.target.files[0];
              }
              // ----------
            }
          })
          .catch((error) => {
            console.error(error);
          });
      }
    }
  }

  /****************************************************************************/

  // onClick(event){
  //   var fileElement = event.target;
  //   //console.log(fileElement)
  //   if (fileElement.value != "") {
  //     this.hotelBookingFile.nativeElement.click();
  //     this.hotelBookingFile.nativeElement.value =this.clonedFile;
  //   }
  // }

  validateFile(name: String, type: String, mimetype: String) {
    const getFileExtension = (filename) => {
      return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    };

    const ext = getFileExtension(name);

    if (type === "PG") {
      if (
        (ext === "jpg" || ext === "jpeg" || ext === "png") &&
        (mimetype === "image/jpeg" ||
          mimetype === "image/png" ||
          mimetype === "image/jpg")
      ) {
        return true;
      }
      return false;
    } else {
      if (
        (ext === "pdf" && mimetype === "application/pdf") ||
        ((ext === "jpg" || ext === "jpeg" || ext === "png") &&
          (mimetype === "image/jpeg" ||
            mimetype === "image/png" ||
            mimetype === "image/jpg"))
      ) {
        return true;
      }
      return false;
    }
  }

  /* validateFile(name: String, type) {
    if (type === "PG") {
      var ext = name.substring(name.lastIndexOf(".") + 1);
      if (
        ext.toLowerCase() == "jpg" ||
        ext.toLowerCase() == "png" ||
        ext.toLowerCase() == "jpeg"
      ) {
        return true;
      } else {
        return false;
      }
    } else {
      var ext = name.substring(name.lastIndexOf(".") + 1);
      if (
        ext.toLowerCase() == "pdf" ||
        ext.toLowerCase() == "png" ||
        ext.toLowerCase() == "jpg" ||
        ext.toLowerCase() == "jpeg"
      ) {
        return true;
      } else {
        return false;
      }
    }
  } */

  /****************************************************************************
     @PURPOSE      : Submit Attachments
     @PARAMETERS   : $event
     @RETURN       : NA
  ****************************************************************************/
  submitAttachemntsInfo(form, attchInfo) {
    if (form.valid) {
      this.ngxLoader.start();
      let sendData = {
        applicationNumber: this.getToken("applicationNumber"),
      };
      const uploadData = new FormData();
      uploadData.append("passport", this.passportFile);
      uploadData.append("photograph", this.photographFile);
      uploadData.append("ticket", this.ticketBookingFile);
      uploadData.append("hotelInvitation", this.hotelBookingFile);
      uploadData.append("transitBook", this.transitBook);
      const resdata = new Blob([JSON.stringify(sendData)], {
        type: "application/json",
      });
      uploadData.append("AttachmentDTO", resdata);
      setTimeout(() => {
        this.commonService
          .callApi(
            "applicantattachments",
            uploadData,
            "post",
            true,
            false,
            "REG"
          )
          .then((success) => {
            let successData: any = success;
            if (successData.apiStatusCode === "SUCCESS") {
              form.reset();
              this.ngxLoader.stop();
              this.staticTabs.tabs[0].disabled = true;
              this.staticTabs.tabs[1].disabled = true;
              this.staticTabs.tabs[2].disabled = true;
              this.staticTabs.tabs[3].disabled = false;
              this.staticTabs.tabs[3].active = true;
              //this.toastr.success("success", successData.apiStatusDesc)
              this.toastr.success(
                "Mise à jour effectuée avec succès",
                "Success"
              );

              // this.getTravelData();
              // this.getPersonalData();
            }
          })
          .catch((e) => {
            this.ngxLoader.stop();
            this.toastr.error(e.message, "Oops!");
          });
      }, 1000);
    } else {
      this.toastr.error("error", "Fields are Required");
    }
  }
  /****************************************************************************/

  continueToPreview() {
    this.staticTabs.tabs[2].disabled = true;
    this.staticTabs.tabs[3].disabled = false;
    this.staticTabs.tabs[3].active = true;
  }

  onSelect(e, type) {
    if (this.getToken("applicationNumber") && this.appNumber) {
      if (type === "PI") {
        this.getPersonalData();
      } else if (type === "TI") {
        this.getInternetDate();
        /* if (this.getToken("expressVisa") === "true") {
          this.new_date = moment(new Date(), "DD-MM-YYYY")
            .add(1, "days")
            .toDate();
        } else {
          this.new_date = moment(new Date(), "DD-MM-YYYY")
            .add(4, "days")
            .toDate();
        }

        this.arrConfig = Object.assign(
          {},
          {
            containerClass: this.colorTheme,
            customTodayClass: "custom-today-class",
            showWeekNumbers: false,
            dateInputFormat: "DD-MM-YYYY",
            minDate: this.new_date,
            maxDate: this.travelInfo.expiryDate,
          }
        ); */
        this.getTravelData();
      } else if (type === "A") {
        this.getPurposetravel();
        this.getAttchmentsData();
      } else if (type === "P") {
        this.getPreview();
        // this.getPersonalData();
        // this.getTravelData();
      }
    }
    this.gotoTop();
  }

  onPrevious(type) {
    if (this.getToken("applicationNumber") || this.appNumber) {
      if (type === "TI") {
        this.staticTabs.tabs[0].disabled = false;
        this.staticTabs.tabs[1].disabled = true;
        this.staticTabs.tabs[2].disabled = true;
        this.staticTabs.tabs[3].disabled = true;
        this.staticTabs.tabs[0].active = true;
        this.getPersonalData();
      } else if (type === "A") {
        this.staticTabs.tabs[1].disabled = false;
        this.staticTabs.tabs[0].disabled = true;
        this.staticTabs.tabs[2].disabled = true;
        this.staticTabs.tabs[3].disabled = true;
        this.staticTabs.tabs[1].active = true;
        this.getTravelData();
      } else if (type === "P") {
        this.staticTabs.tabs[2].disabled = false;
        this.staticTabs.tabs[0].disabled = true;
        this.staticTabs.tabs[1].disabled = true;
        this.staticTabs.tabs[3].disabled = true;
        this.staticTabs.tabs[2].active = true;
        this.getAttchmentsData();
      }
    }
  }

  /****************************************************************************
      @PURPOSE      : Retriving Attachments data
      @PARAMETERS   : NA
      @RETURN       : NA
   ****************************************************************************/
  public attachments = [];
  public fakePath = "C:/fakepath/";
  getAttchmentsData() {
    let data = {
      applicationNumber: this.getToken("applicationNumber"),
    };
    this.commonService
      .callApi(
        "applicantattachments/applicantnumber",
        data,
        "post",
        false,
        false,
        "REG"
      )
      .then((success) => {
        let successData: any = success;
        this.attachments = successData.applicantAttachmentDTOs;
        this.attachments.forEach((data) => {
          if (data.attachmentType === "PP") {
            data["order"] = 2;
            this.attachmentsInfo.passPort = data.fileName;
          } else if (data.attachmentType === "PG") {
            data["order"] = 1;
            this.attachmentsInfo.Photograph = data.fileName;
          } else if (data.attachmentType === "TK") {
            data["order"] = 3;
            this.attachmentsInfo.ticketReservation = data.fileName;
          } else if (data.attachmentType === "HI") {
            data["order"] = 4;
            this.attachmentsInfo.hotelBooking = data.fileName;
          } else if (data.attachmentType === "TS") {
            this.attachmentsInfo.transitBook = data.fileName;
          }
        });

        if (this.attachments.length === 3) {
          let data = {
            attachmentType: "HI",
            fileName: "",
            order: 4,
          };
          this.attachments.push(data);
        }

        this.attachments.sort(function (a, b) {
          return a.order - b.order;
        });
      })
      .catch((e) => {
        this.toastr.error(e.message, "Oops!");
      });
  }
  /****************************************************************************/

  removeAttchment(type) {
    if (type === "PI") {
      this.passportVariable.nativeElement.value = "";
      this.attachmentsInfo.passPort = "";
    } else if (type === "PG") {
      this.photographVariable.nativeElement.value = "";
      this.attachmentsInfo.Photograph = "";
    } else if (type === "T") {
      this.ticketVariable.nativeElement.value = "";
      this.attachmentsInfo.ticketReservation = "";
    } else if (type === "HB") {
      this.hotelBookingVariable.nativeElement.value = "";
      this.attachmentsInfo.hotelBooking = "";
      this.hotelBookingFile = "";
    } else if (type === "TS") {
      this.transitAttachmentVariable.nativeElement.value = "";
      this.attachmentsInfo.hotelBooking = "";
      this.hotelBookingFile = "";
    }
  }

  public attachmentType: any;
  public attachmentId: any;
  editAttchment(data) {
    this.attachmentType = data.attachmentType;
    this.attachmentId = data.attachmentId;
  }

  updateAttachment(form) {
    let sendData = {
      applicationNumber: this.getToken("applicationNumber"),
      attachmentType: this.attachmentType,
      attachmentId: this.attachmentId,
    };
    const uploadData = new FormData();
    const resdata = new Blob([JSON.stringify(sendData)], {
      type: "application/json",
    });
    uploadData.append("file", this.newAttachment);
    uploadData.append("AttachmentDTO", resdata);
    this.commonService
      .callApi("applicantattachment", uploadData, "put", true, false, "REG")
      .then((success) => {
        let successData: any = success;
        if (successData.apiStatusCode === "SUCCESS") {
          //this.toastr.success("success", successData.apiStatusDesc);
          this.toastr.success("Mise à jour effectuée avec succès", "Success");
          form.reset();
          this.closebutton.nativeElement.click();
          this.getAttchmentsData();
        }
      })
      .catch((e) => {
        this.toastr.error(e.message, "Oops!");
      });
  }

  public resource: any;
  downloadAttachment(data) {
    let sendData = {
      applicationNumber: this.getToken("applicationNumber"),
      attachmentType: data.attachmentType,
      attachmentId: data.attachmentId,
    };

    this.commonService
      .downloadAttachment("applicantattachments/attachmentid", sendData)
      .subscribe((res) => {
        this.resource = res;
        let blob = new Blob([this.resource], {
          type: "application/xml;charset=UTF-8",
        });
        FileSaver.saveAs(blob, data.fileName);
      });
  }

  process(date) {
    var parts = date.split("/");
    return new Date(parts[2], parts[1] - 1, parts[0]);
  }
  /****************************************************************************
   //Compare Start Date and End Date
   /****************************************************************************/
  public dateError: boolean = false;
  compareDate(arrivalDate, depDate) {
    // var date1 = '25/02/1985';  /*february 25th*/
    // var date2 = '26/01/1985';  /*february 26th*/
    // /*this dates are results form datepicker*/
    // //console.log(this.process(date2) >this. process(date1))
    // //console.log(this.process(date2) <this. process(date1))
    // if (this.process(date2) >this. process(date1)) {
    //   alert(date2 + 'is later than ' + date1);
    // }

    var d = new Date(this.depConfig.maxDate);
    d.setHours(0, 0, 0, 0);
    var d1 = moment(depDate).format("DD/MM/YYYY");
    var d2 = moment(d).format("DD/MM/YYYY");
    if (arrivalDate && depDate) {
      if (Date.parse(depDate) < Date.parse(arrivalDate)) {
        this.dateError = true;
        // this.dateErrorMessage = "End date should not be greater than Start date";
      } else if (Date.parse(arrivalDate) == Date.parse(depDate)) {
        this.dateError = true;
        // this.dateErrorMessage = "End date and Start date should not be same";
      } else if (this.process(d1) > this.process(d2)) {
        this.dateError = true;
      } else {
        this.dateError = false;
      }
    }
  }
  /****************************************************************************/

  getDisabledDates(): Date[] {
    if (this.travelInfo.arrivalDate) {
      // Retourne la date sélectionnée comme une liste pour désactivation
      return [new Date(this.travelInfo.arrivalDate)];
    }
    return [];
  }

  /****************************************************************************/

  arrivalChange(arrDate) {
    let id = this.getToken("visaType");
    // let lastChar = id.substr(id.length - 1);
    let visaType = this.getToken("visaType").replace(/[^\d.-]/g, "");
    let new_date = moment(arrDate, "DD-MM-YYYY").add(visaType, "days").toDate();
    if (parseInt(visaType) != 15) {
      new_date = moment(arrDate, "DD-MM-YYYY").add(30, "days").toDate();
    }
    // let final = this.daysCalculate(lastChar, visaType)

    this.depConfig.maxDate = new_date;
  }

  modo(e) {}
  getPurposetravel() {
    this.commonService
      .callApi(
        "applicationpreview?applicationNumber=" +
          this.getToken("applicationNumber"),
        "",
        "post",
        false,
        false,
        "REG"
      )
      .then((success) => {
        let successData: any = success;
        this.getpurpose =
          successData.applicantPassportTravelDetails.travelPurpose;
      });
  }
  /****************************************************************************
       @PURPOSE      : Retriving Attachments data
       @PARAMETERS   : NA
       @RETURN       : NA
    ****************************************************************************/
  public previewData: any;
  public attachmentsPreview = [];
  getPreview() {
    this.commonService
      .callApi(
        "applicationpreview?applicationNumber=" +
          this.getToken("applicationNumber"),
        "",
        "post",
        false,
        false,
        "REG"
      )
      .then((success) => {
        let successData: any = success;
        this.personalPreview = successData.applicantPersonalDetails;
        this.passportPreview = successData.applicantPassportTravelDetails;
        if (this.passportPreview != null) {
          let countries = this.passportPreview.lastVisitedCountries;
          let countryList = countries == "null" ? "" : countries;
          this.passportPreview.lastVisitedCountries = countryList;
        }
        this.attachmentsPreview =
          successData.applicantAttachmentDetailsDTOList.applicantAttachmentDTOs;
        this.attachmentsPreview.forEach((data) => {
          if (data.attachmentType === "PP") {
            data["order"] = 2;
          } else if (data.attachmentType === "PG") {
            data["order"] = 1;
          } else if (data.attachmentType === "TK") {
            data["order"] = 3;
          } else if (data.attachmentType === "HI") {
            data["order"] = 4;
          } else if (data.attachmentType === "TS") {
            data["order"] = 5;
          }
        });
        this.attachmentsPreview.sort(function (a, b) {
          return a.order - b.order;
        });
      })
      .catch((e) => {
        this.toastr.error(e.message, "Oops!");
      });
  }
  /****************************************************************************/

  //Days calculator
  public finalValue = 0;
  daysCalculate(lastChar, visaType) {
    this.finalValue = 0;
    if (lastChar === "D") {
      this.finalValue = this.finalValue + parseInt(visaType);
    } else if (lastChar === "M") {
      this.finalValue = this.finalValue + parseInt(visaType) * 30;
    } else if (lastChar === "Y") {
      this.finalValue = this.finalValue + parseInt(visaType) * 365;
    }
    return this.finalValue;
  }

  /****************************************************************************
    @PURPOSE      : file upload
    @PARAMETERS   : $event
    @RETURN       : NA
 ****************************************************************************/

  updateFile(event, type) {
    const file = event.target.files[0];

    const slice = file.slice(0, file.size);
    let uint8Array = null;

    const isTextFile = (uint8Array) => {
      return uint8Array.every(
        (byte) => (byte >= 32 && byte <= 126) || byte === 10 || byte === 13
      );
    };
    const readFileAsArrayBuffer = new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.onload = function () {
        resolve(reader.result);
      };

      reader.onerror = function () {
        reject(new Error("File read failed"));
      };

      reader.readAsArrayBuffer(slice);
    });

    if (event.target.files && event.target.files.length > 0) {
      if (type === "PP") {
        readFileAsArrayBuffer
          .then((arrayBuffer: ArrayBufferLike) => {
            uint8Array = new Uint8Array(arrayBuffer);
            if (isTextFile(uint8Array)) {
              this.toastr.error(
                "error!!",
                "File size is too long or incorrect format"
              );

              this.newAttachmentVariable.nativeElement.value = "";
              this.attachmentsInfo.passPort = "";
            } else {
              // ----------
              if (
                event.target.files[0].size > 250000 ||
                !this.validateFile(
                  event.target.files[0].name,
                  type,
                  event.target.files[0].type
                )
              ) {
                this.toastr.error(
                  "error!!",
                  "File size is too long or incorrect format"
                );
                this.newAttachmentVariable.nativeElement.value = "";
                this.attachmentsInfo.passPort = "";
              } else {
                this.newAttachment = event.target.files[0];
              }
              // ----------
            }
          })
          .catch((error) => {
            console.error(error);
          });
      } else if (type === "PG") {
        readFileAsArrayBuffer
          .then((arrayBuffer: ArrayBufferLike) => {
            uint8Array = new Uint8Array(arrayBuffer);
            if (isTextFile(uint8Array)) {
              this.toastr.error(
                "error!!",
                "File size is too long or incorrect format"
              );

              this.newAttachmentVariable.nativeElement.value = "";
              this.attachmentsInfo.passPort = "";
            } else {
              // ----------
              if (
                event.target.files[0].size > 250000 ||
                !this.validateFile(
                  event.target.files[0].name,
                  type,
                  event.target.files[0].type
                )
              ) {
                this.toastr.error(
                  "error!!",
                  "File size is too long or incorrect format"
                );
                this.newAttachmentVariable.nativeElement.value = "";
                this.attachmentsInfo.Photograph = "";
              } else {
                this.newAttachment = event.target.files[0];
              }
              // ----------
            }
          })
          .catch((error) => {
            console.error(error);
          });
      } else if (type === "TK") {
        // //console.log("aujjjjjjjjjjj", this.attachmentsInfo.passPort)
        readFileAsArrayBuffer
          .then((arrayBuffer: ArrayBufferLike) => {
            uint8Array = new Uint8Array(arrayBuffer);
            if (isTextFile(uint8Array)) {
              this.toastr.error("error!!", "File size is too long or ");

              this.newAttachmentVariable.nativeElement.value = "";
              this.attachmentsInfo.passPort = "";
              // //console.log("aujjjjjjjjjjj", this.attachmentsInfo.passPort)
            } else {
              // ----------
              if (
                event.target.files[0].size > 250000 ||
                !this.validateFile(
                  event.target.files[0].name,
                  type,
                  event.target.files[0].type
                )
              ) {
                // this.isTRSize=true;
                this.toastr.error(
                  "error!!",
                  "File size is too long or incorrect format"
                );
                this.newAttachmentVariable.nativeElement.value = "";
                this.attachmentsInfo.ticketReservation = "";
              } else {
                this.newAttachment = event.target.files[0];
              }
              // ----------
            }
          })
          .catch((error) => {
            console.error(error);
          });
      } else if (type === "HI") {
        readFileAsArrayBuffer
          .then((arrayBuffer: ArrayBufferLike) => {
            uint8Array = new Uint8Array(arrayBuffer);
            if (isTextFile(uint8Array)) {
              this.toastr.error(
                "error!!",
                "File size is too long or incorrect format"
              );

              this.newAttachmentVariable.nativeElement.value = "";
              this.attachmentsInfo.passPort = "";
            } else {
              // ----------
              if (
                event.target.files[0].size > 250000 ||
                !this.validateFile(
                  event.target.files[0].name,
                  type,
                  event.target.files[0].type
                )
              ) {
                // this.isHBSize=true;
                this.toastr.error(
                  "error!!",
                  "File size is too long or incorrect format"
                );
                this.newAttachmentVariable.nativeElement.value = "";
                this.attachmentsInfo.hotelBooking = "";
              } else {
                this.newAttachment = event.target.files[0];
              }
              // ----------
            }
          })
          .catch((error) => {
            console.error(error);
          });
      }
    }
  }
  /****************************************************************************/

  gotoTop() {
    window.scroll({
      top: 0,
      left: 0,
      behavior: "smooth",
    });
  }

  public numLength;
  selectCountryCode(e) {
    let code = e.target.value;
    let result = this.countryCode.filter((c) => c.code == code);
    this.numLength = result[0].numLength;
  }

  // validation des input

  // validateInput(event: Event): void {
  //   const input = (event.target as HTMLInputElement).value;

  //   // Expression régulière pour autoriser uniquement les lettres et chiffres
  //   const regex = /^[a-zA-Z0-9]*$/;
  //   if (!regex.test(input)) {
  //     // Si l'entrée contient des caractères spéciaux, on enlève ces caractères
  //     (event.target as HTMLInputElement).value = input.replace(/[^a-zA-Z0-9]/g, '');
  //   }
  // }

  //

  omit_numbers(event: KeyboardEvent): void {
    const input = event.key;
    const regex = /^[a-zA-Z\s'-]$/; // Autoriser uniquement les lettres, espaces, apostrophes et tirets

    // Si la touche pressée ne correspond pas à la regex, empêcher la saisie
    if (!regex.test(input)) {
      event.preventDefault();
    }
  }

  // validateInput(event: Event, type: 'string' | 'number' | 'address'): void {
  //   const input = (event.target as HTMLInputElement).value;

  //   // Définir une expression régulière selon le type
  //   let regex: RegExp;

  //   switch (type) {
  //     case 'string': // Autoriser uniquement les lettres
  //       regex = /^[a-zA-Z\s-]*$/;
  //       break;
  //     case 'number': // Autoriser uniquement les chiffres
  //       regex = /^[0-9]*$/;
  //       break;
  //     case 'address': // Autoriser lettres, chiffres et quelques caractères spéciaux
  //       regex = /^[a-zA-Z0-9\s,.'-/]*$/;
  //       break;
  //     default:
  //       regex = /.*/; // Autoriser tout (fallback)
  //   }

  //   // Remplacer les caractères invalides
  //   if (!regex.test(input)) {
  //     (event.target as HTMLInputElement).value = input.replace(
  //       type === 'string'
  //         ? /[^a-zA-Z\s-]/g // Pour les lettres, autoriser les espaces
  //         : type === 'number'
  //         ? /[^0-9]/g // Pour les chiffres
  //         : /[^a-zA-Z0-9\s,.'-/]/g, // Pour les adresses
  //       '' // Remplace les caractères non valides par une chaîne vide
  //     );
  //   }
  // }

  validateInput(
    event: Event,
    type: "string" | "number" | "address" | "passport"
  ): void {
    // Vérifier si l'élément cible est un input ou une textarea
    const inputElement = event.target as HTMLInputElement | HTMLTextAreaElement;
    const input = inputElement.value; // Obtenir la valeur de l'élément

    // Définir une expression régulière selon le type
    let regex: RegExp;

    switch (type) {
      case "string": // Autoriser uniquement les lettres
        regex = /^[a-zA-Z\s-]*$/;
        break;
      case "number": // Autoriser uniquement les chiffres
        regex = /^[0-9]*$/;
        break;
      case "address": // Autoriser lettres, chiffres et quelques caractères spéciaux
        regex = /^[a-zA-Z0-9\s,.'-/]*$/;
        break;
      case "passport": // Numéro de passeport
        regex = /^[A-Z0-9-]{6,12}$/;
        break;
      default:
        regex = /.*/; // Autoriser tout (fallback)
    }

    // Remplacer les caractères invalides
    if (!regex.test(input)) {
      inputElement.value = input.replace(
        type === "string"
          ? /[^a-zA-Z\s-]/g // Pour les lettres, autoriser les espaces et les tirets
          : type === "number"
          ? /[^0-9]/g // Pour les chiffres, supprimer les caractères non numériques
          : /[^a-zA-Z0-9\s,.'-/]/g, // Pour les adresses, supprimer les caractères non autorisés
        ""
      );
    }
  }
}

