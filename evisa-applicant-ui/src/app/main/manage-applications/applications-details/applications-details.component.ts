
import { Component, OnInit, Injector } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent';
import { NgxUiLoaderService } from 'ngx-ui-loader';

@Component({
  selector: 'app-applications-details',
  templateUrl: './applications-details.component.html',
  styleUrls: ['./applications-details.component.css']
})
export class ApplicationsDetailsComponent extends BaseComponent implements OnInit {
  public appHeaderDetails: any = {
    expressVisa: false
  };
  public isNew: boolean = false;
  public pagesize = 10;
  public totalItem: any;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }

  public fileNumber: any;
  public visaTypes = [
    {
      code: "1M",
      value: "1 Month"
    },
    {
      code: "6M",
      value: "6 Month"
    },
    {
      code: "1Y",
      value: "1 Year"
    }
  ]
  public applyForDetails = [
    {
      code: "I",
      value: "Individual"
    },
    {
      code: "G",
      value: "Group"
    }
  ]

  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      if (params['id'] === 'new') {
        this.isNew = true;
      } else {
        this.fileNumber = params['id'];
        this.setToken('fileNumber', this.fileNumber)
        this.getFileInfo(this.fileNumber);
        this.getFileApplications(this.fileNumber);
        this.isNew = false;
        this.isFileCreated = true;
        this.showData = true;
      }
    })
  }

  ngOnInit(): void {
    this.getVisaTypes('Y');
  }


  /****************************************************************************
    @PURPOSE      : To Retrive Master Visa types
    @PARAMETERS   : type
    @RETURN       : NA
 ****************************************************************************/
  getVisaTypes(type) {
    this.commonService.callApi('visadetails/' + type, '', 'get', false, false, 'REG').then(success => {
      let successData: any = success;
      this.visaTypes = successData.masterCodeResultDTOs;

    }
    ).catch(e => {
      this.toastr.error(e.message, 'Oops!')
    });
  }
  /****************************************************************************/

  /****************************************************************************
      @PURPOSE      : file number creation
      @PARAMETERS   : form,formdata
      @RETURN       : NA
   ****************************************************************************/
  public submitted: boolean = false;
  public isFileCreated: boolean = false;
  public generatedFile: any;
  public successData: any;
  public showData: boolean = false;

  addHeader(form, appHeaderDetails) {
    appHeaderDetails.status = "DRA";
    appHeaderDetails.username = this.getToken('username');
    if (this.iseditHeader) {
      appHeaderDetails.fileNumber = this.getToken('fileNumber');
    }
    if (form.valid) {
      this.setToken('visaType', appHeaderDetails.visaType);
      if (appHeaderDetails.expressVisa) {
        this.setToken('expressVisa', 'true')
      } else {
        this.setToken('expressVisa', 'false');
      }

      this.ngxLoader.start();
      setTimeout(() => {
        this.commonService.callApi('applicationfile', appHeaderDetails, 'post', false, false, 'REG').then(success => {
          this.successData = success;
          this.setToken('applicationType', this.successData.applicantType);
          this.getVisaDescription(this.successData.visaType);
          if (this.successData) {
            form.reset();
            let fileNumber = this.successData.fileNumber;
            this.generatedFile = this.successData.fileNumber;
            this.isFileCreated = true;
            this.showData = true;
            this.ngxLoader.stop();
            this.getFileApplications(this.successData.fileNumber);
            this.setToken('fileNumber', fileNumber);
              if (this.iseditHeader) {

              }
              else{
                  this.router.navigate(['/main/manage-applications/visa-details/new']);
              }
          }
        }
        ).catch(e => {
          this.ngxLoader.stop();
          this.toastr.error(e.message, 'Oops!')
        });
      }, 1000)

    } else {
      this.submitted = true;
    }


  }
  /****************************************************************************/


  public isExp: boolean = false;
  isExpress(e) {
    if (e) {
      this.isExp = true;

    } else {
      this.isExp = false

    }

    this.appHeaderDetails.expressVisa = e;

  }



  /****************************************************************************
    @PURPOSE      : get file data
    @PARAMETERS   : form,formdata
    @RETURN       : NA
 ****************************************************************************/

  getFileInfo(fileNumber) {
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('applicationfile/' + fileNumber, '', 'post', false, false, 'REG').then(success => {
        let successData: any = success;

        this.setToken('applicationType', successData.applicantType);
        this.setToken('expressVisa', successData.expressVisa)

        if (successData) {
          this.successData = successData
          this.appHeaderDetails = successData;
          this.setToken('visaType', this.appHeaderDetails.visaType);
          this.ngxLoader.stop();
          if (!this.isNew && this.iseditHeader) {
            this.isFileCreated = false;
            this.getVisaDescription(this.appHeaderDetails.visaType);
            this.generatedFile = this.appHeaderDetails.fileNumber;
          } else {
            this.getVisaDescription(this.appHeaderDetails.visaType);
            this.generatedFile = this.appHeaderDetails.fileNumber;
          }
        }
      }
      ).catch(e => {
        this.toastr.error(e.message, 'Oops!')
      });
    })

  }
  /****************************************************************************/

  disableButton: boolean = false;

  public fileApplications = [];
  public loading: boolean;
  getFileApplications(fileNumber) {
    this.data.pageNumber = 1;
    this.data.fileNumber = fileNumber;
    this.loading = true;
    setTimeout(() => {
      this.commonService.callApi('seachapplications/', this.data, 'post', false, false, 'REG').then(success => {
        let successData: any = success;
        this.totalItem = successData.totalElements;
        if (successData.content.length === 1 && this.getToken('applicationType') === 'I') {
          this.disableButton = true
        } else {
          this.disableButton = false
        }

        if (successData.content.length) {
          // if (!this.isNew) {
          //   this.appHeaderDetaivisa this.fileDetails;
          // }
          this.loading = false;
          this.fileApplications = successData.content;
        } else {
          this.loading = false;
        }
      }
      ).catch(e => {
        this.toastr.error(e.message, 'Oops!')
      });
    })

  }

  public submitCheckBox: boolean

  clickCheckBox(event) {
    if (event.target.checked) {
      this.submitCheckBox = true;
    } else {
      this.submitCheckBox = false;
    }
  }

  submitApplications(form, sendData) {
    sendData = {
      "applicantType": this.appHeaderDetails.applicantType ? this.appHeaderDetails.applicantType : this.successData.applicantType,
      "expressVisa": this.appHeaderDetails.expressVisa ? this.appHeaderDetails.expressVisa : this.successData.expressVisa,
      "fileNumber": this.fileNumber,
      "username": this.getToken('username'),
      "visaType": this.appHeaderDetails.visaType ? this.appHeaderDetails.visaType : this.successData.visaType
    }
    this.ngxLoader.start();
    this.setToken("visaType", this.appHeaderDetails.visaType)
    this.setToken("expressVisa", this.appHeaderDetails.expressVisa)
    setTimeout(() => {
      this.commonService.callApi('applicationfile/submission/', sendData, 'post', false, false, 'REG').then(success => {
        let successData: any = success;
        if (successData.apiStatusCode === 'SUCCESS') {
          //this.toastr.success("success", successData.apiStatusDesc);
          this.toastr.success('Mise à jour effectuée avec succès', 'Success');
          ////console.log(this.successData.apiStatusDesc);
          this.setToken("appNo", successData.applicationNumber);
          this.setToken("charge", successData.amountDetails)
          this.router.navigate(['/main/payment']);
          this.ngxLoader.stop();
        } else {
          this.ngxLoader.stop();
          // this.toastr.info(successData.errorDetails[0] + '</br>' + successData.errorDetails[1], 'Info!',

          // )

          this.toastr.info(successData.errorDetails[0] + '</br></br>' + successData.errorDetails[1], '', { closeButton: true, timeOut: 4000, progressBar: true, enableHtml: true });

        }
      }
      ).catch(e => {
        this.toastr.error(e.message, 'Oops!')
      });
    }, 1000)

  }

  public iseditHeader: boolean = false;
  public isedit: boolean = false;
  editHeader() {
    this.isedit = true;
    if (this.isNew) {

      this.isFileCreated = false;
      this.getFileInfo(this.generatedFile);
    } else {
      this.iseditHeader = true;
      this.getFileInfo(this.fileNumber);
    }


  }
}
