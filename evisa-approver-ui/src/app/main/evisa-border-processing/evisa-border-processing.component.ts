import { Component, Injector, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BaseComponent } from 'src/app/common/commonComponent';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { formatCurrency } from '@angular/common';
import { trigger, state, style, animate, transition } from '@angular/animations';
@Component({
  selector: 'app-evisa-border-processing',
  templateUrl: './evisa-border-processing.component.html',
  styleUrls: ['./evisa-border-processing.component.css'],
  animations: [
    trigger('rotatedState', [
      state('default', style({ transform: 'rotate(0)' })),
      state('rotated', style({ transform: 'rotate(90deg)' })),
      // transition('rotated => default', animate('400ms ease-out')),
      // transition('default => rotated', animate('400ms ease-in'))
    ]),
    trigger('rotatedState1', [
      state('default', style({ transform: 'rotate(0)' })),
      state('rotated', style({ transform: 'rotate(-90deg)' })),
      // transition('rotated => default', animate('400ms ease-out')),
      // transition('default => rotated', animate('400ms ease-in'))
    ])
  ]
})
export class EvisaBorderProcessingComponent extends BaseComponent implements OnInit {
  samples = [];
  public loading: boolean = false;
  processingInfo: any = {};
  canvasWidth = 800;
  canvasHeight = 600;
  imageSrc: any;
  noWrapSlides = false;
  myInterval = 0;
  fileExtension: any;
  /* public base64Image = 'data:image/png;base64,'; */
  public submitted: boolean = false;
  applicationNumber: any;
  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      if (params['id']) {
        //console.log(params['id']);
        this.applicationNumber = params['id'];
        this.getAppDetails(params['id']);
        this.getArrivalDeparture(params['id']);
        this.getArrivalDepartureHistory(params['id']);
        this.getNeedValidationHistory(params['id']);
        this.getTravelHistory();
      }
    });
  }

  ngOnInit(): void {
    this.processingInfo.arrivalLocation = 'JIB';
  }

  i = 1;

  setFileExtension(filePath: string) {
    const fileExtension = filePath.split('.').pop()?.toLowerCase();
    this.fileExtension = fileExtension || '';
  }


  next() {
    //console.log("hai")
    //console.log(this.samples)
    if (this.i < this.samples.length) {
      //console.log(this.samples.length)
      this.imageSrc = this.samples[this.i].url
      this.setFileExtension(this.imageSrc);
      this.imageType = this.samples[this.i].docType
      //console.log("coucccccccccccccc",this.imageType)
      //console.log(this.imageSrc.split('.')[1], "extension")
      //console.log("coucccccccccccccc",this.imageSrc)
      return this.i++;
    } else {
      return this.i = 0;
    }

  }

  prev() {
    if (this.i > 1) {
      this.imageSrc = this.samples[this.i - 2].url
      this.setFileExtension(this.imageSrc);
      
      this.imageType = this.samples[this.i - 2].docType
      return this.i--;
    } else {
      return this.i = this.samples.length + 1;
    }
  }
  modalRef: BsModalRef;
  company: any
  openImage(template) {
    this.modalRef = this.modalService.show(template);
  }

  

  /****************************************************************************
      @PURPOSE      : Retriving application data
      @PARAMETERS   : NA
      @RETURN       : NA
   ****************************************************************************/
  public personalPreview: any;
  public passportPreview: any;
  public senData: any;
  public photoObj: any;
  public approvalHistoryList: any = []
  public previousHistoryList: any = [];
  public Photograph: any;
  public imageType: any;
  getAppDetails(appNo) {
    this.loading = true
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('applicationpreview?applicationNumber=' + appNo, '', 'post', false, false, 'APPR').then(success => {
        let successData: any = success;
        this.loading = false;
        this.personalPreview = successData.applicantPersonalDetails;
        this.passportPreview = successData.applicantPassportTravelDetails;
        if (this.getToken('Role') === 'DMM') {
          this.approvalHistoryList = successData.approverHistoryDetailsDTOList.approverHistoryDetailsDTOs;
        }
        this.approvalHistoryList = successData.approverHistoryDetailsDTOList.approverHistoryDetailsDTOs;
        this.photoObj = successData.applicationAttachmentDetailsList.attchPreviewDTOList.filter(
          data => data.docType === 'PG');
        this.Photograph = this.photoObj[0].attachmentUrl;
        this.ngxLoader.stop()
        //console.log(this.approvalHistoryList);
        successData.applicationAttachmentDetailsList.attchPreviewDTOList.forEach((data) => {

          if (data.docType != 'PG') {
            let tempData = {
              "url": data.attachmentUrl,
              "docType": data.docType
            }
            this.samples.push(tempData);
          }

        })
        this.imageSrc = this.samples[0].url;
        this.setFileExtension(this.imageSrc);
        this.imageType = this.samples[0].docType;
        //console.log(this.samples);
      }

      ).catch(e => {
        this.ngxLoader.stop()
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }, 1000)

  }



  /****************************************************************************/



  /****************************************************************************
     @PURPOSE      : Retriving application data
     @PARAMETERS   : NA
     @RETURN       : NA
  ****************************************************************************/
  public isStatus: any;
  public isShow: boolean = false;
  processApplication(status, template) {
    if (this.getToken('arrDepIndicator') === 'AV') {
      this.commonService.callApi('arrivaldeparture/' + this.getToken('arrDepId'), '', 'get', false, false, 'APPR').then(success => {
        let successData: any = success;
        this.submitted = false;
        this.processingInfo.remarks = '';
        this.getLocationdetails('ARLOC');
        this.processingInfo.arrivalLocation = successData.arrLocation;
        this.processingInfo.carrierNo = successData.arrCarrierNo;
        this.modalRef = this.modalService.show(template);
        this.isStatus = status;
        this.processingInfo.status = this.isStatus === 'APR' ? 'Granted' : this.isStatus === 'REJ' ? 'Rejected' : 'Need Validation'
      }

      ).catch(e => {
        this.ngxLoader.stop()
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    } else {
      this.submitted = false;
      this.getLocationdetails('ARLOC');
      this.processingInfo.arrivalLocation = '';
      this.processingInfo.remarks = '';
      this.modalRef = this.modalService.show(template);
      this.isStatus = status;
      this.processingInfo.status = this.isStatus === 'APR' ? 'Granted' : this.isStatus === 'REJ' ? 'Rejected' : 'Need Validation'
    }
  }

  submitRemarks(form, processingInfo) {
    if (form.valid) {

      if ((this.getToken('Role') === 'DM') || (this.getToken('Role') === 'DMM')) {
        //console.log(this.processingInfo.remarks)
        let data = {
          "applicationNumber": this.applicationNumber,
          "approver": this.getToken('username'),
          "approverRole": this.getToken('Role'),
          "fileNumber": this.getToken('fileNo'),
          "remarks": this.processingInfo.remarks,
          "status": this.isStatus
        }
        this.ngxLoader.start();
        setTimeout(() => {
          this.commonService.callApi('processapproval', data, 'post', false, false, 'APPR').then(success => {
            let successData: any = success;
            this.ngxLoader.stop()
            this.modalRef.hide();
            this.router.navigate(['/main/evisasearch'])
            //console.log(successData)
          }
          ).catch(e => {
            this.ngxLoader.stop()
            this.toastr.errorToastr(e.message, 'Oops!')
          });
        }, 1000)
      } else if ((this.getToken('Role') === 'BCO') || (this.getToken('Role') === 'SBCO')) {
        let data = {
          "applicationNumber": this.applicationNumber,
          "carrierNo": this.processingInfo.carrierNo,
          "location": this.processingInfo.arrivalLocation,
          "id": parseInt(this.getToken('arrDepId')),
          "loggeduser": this.getToken('username'),
          "oprType": "A",
          "remarks": this.processingInfo.remarks,
          "role": this.getToken('Role'),
          "status": this.isStatus
        }
        this.ngxLoader.start();
        setTimeout(() => {
          this.commonService.callApi('processarrival', data, 'post', false, false, 'APPR').then(success => {
            let successData: any = success;
            if (successData.apiStatusCode === 'SUCCESS') {
              this.ngxLoader.stop()
              this.modalRef.hide();
              this.toastr.successToastr("success", successData.apiStatusDesc)
              this.router.navigate(['/main/evisasearch'])
            } else {
              this.ngxLoader.stop()
              this.modalRef.hide();
              this.toastr.errorToastr("error", successData.apiStatusDesc)
            }

            //console.log(successData)
          }
          ).catch(e => {
            this.ngxLoader.stop()
            this.toastr.errorToastr(e.message, 'Oops!');
          });
        }, 1000)
      }

    } else {
      this.submitted = true;
    }

  }
  /************************************************************************/

  /****************************************************************************
    @PURPOSE      : Retriving arrival/departure data
    @PARAMETERS   : application number
    @RETURN       : arrivalDepartureDTO
 ****************************************************************************/
 arrivalDepartureList: any = [];
  getArrivalDeparture(id) {
    this.commonService.callApi('arrivaldeparture/history/' + id, '', 'get', false, false, 'APPR').then(success => {
      let successData: any = success;
      this.arrivalDepartureList = successData.arrivalDepartureDetailsDTOs;
      //console.log(this.arrivalDepartureList);

    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!');
    });
  }

  /*****************************************************************************/
    arrivaldeparturehistory: any = [];
  getArrivalDepartureHistory(id) {
        this.commonService.callApi('arrivaldeparturehistory/'+ id, '', 'get', false, false,'APPR').then(success => {
            let successData: any = success;
            this.arrivaldeparturehistory = successData.arrivalDepartureHistoryDetailsDTOs;

        }).catch(e => {
            this.toastr.errorToastr(e.message, 'Oops!');
        });
  }
  /*****************************************************************************/
  needValidationHistory: any = [];
  getNeedValidationHistory(id) {
        this.commonService.callApi('arrivaldeparturehistory/'+ id, '', 'get', false, false,'APPR').then(success => {
            let successData: any = success;
            this.needValidationHistory = successData.arrivalDepartureHistoryDetailsDTOs;

        }).catch(e => {
            this.toastr.errorToastr(e.message, 'Oops!');
        });
  }
  /****************************************************************************/

  onSelect(type) {
    if (type === 'PH') {
      this.getTravelHistory();
    }
    else if (type === 'AD') {
        //alert('daans type AD');
      this.getArrivalDeparture(this.applicationNumber);
      this.getArrivalDepartureHistory(this.applicationNumber);
    }
  }
  getTravelHistory() {
    let data = {
      "pageNumber": 1,
      "pageSize": 10,
      "applicationNumber": this.applicationNumber
    }
    this.commonService.callApi('travelhistory/search', data, 'post', false, false, 'APPR').then(success => {
      let successData: any = success;
      if (successData.content.length) {
        this.previousHistoryList = successData.content;
      }

      //console.log(successData);
    }
    ).catch(e => {
      this.ngxLoader.stop()
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }

  state: string = 'default';
  state1: string = 'default';
  rotateright: string = 'default';
  rotate: string = 'default'
  rotateLeft(type) {
    //console.log("rotate left");
    if (type == "L") {
      this.rotate = (this.rotate === 'default' ? 'rotated' : 'default');
    } else {
      this.state = (this.state === 'default' ? 'rotated' : 'default');
    }
  }
  rotateRight(type) {
    //console.log("rotate right");
    if (type == 'RIGHT') {
      this.state1 = (this.state1 === 'default' ? 'rotated' : 'default');
    }else{
      this.rotateright = (this.rotateright === 'default' ? 'rotated' : 'default');
    }
  }

}
