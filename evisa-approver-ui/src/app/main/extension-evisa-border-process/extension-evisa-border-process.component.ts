import { Component, Injector, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BaseComponent } from 'src/app/common/commonComponent';
import { trigger, state, style, animate, transition } from '@angular/animations';

@Component({
  selector: 'app-extension-evisa-border-process',
  templateUrl: './extension-evisa-border-process.component.html',
  styleUrls: ['./extension-evisa-border-process.component.css'],
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
export class ExtensionEvisaBorderProcessComponent extends BaseComponent implements OnInit {
  public refNumber: any
  public loading = false
  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj)
    this.activatedRoute.params.subscribe((params) => {
      this.refNumber = params['id'];
      this.getExtensionDetails(params['id']);
        this.getAppDetails(params['details']);
    });
  }

  ngOnInit(): void {
  }

  /****************************************************************************
   @PURPOSE      : Retriving applicant data 
   @PARAMETERS   : extension number
   @RETURN       : ExtensioneVisa
****************************************************************************/
  public Photograph: any;
  public photoObj: any;
  public imageType: any;
  public imageSrc: any;
  public samples = [];
  public extensionDetails: any = {};
  public extensionHistory: any = [];
  getExtensionDetails(refNo) {
    this.loading = true
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('visaextension/preview?extensionNumber=' + refNo, '', 'post', false, false, 'APPR').then(success => {
        let successData: any = success;
        this.extensionDetails = successData
        this.photoObj = successData.applicationAttachmentDetailsList.attchPreviewDTOList.filter(
          data => data.docType === 'PG');
        this.Photograph = this.photoObj[0].attachmentUrl;

        this.loading = false;
        this.ngxLoader.stop()
        successData.applicationAttachmentDetailsList.attchPreviewDTOList.forEach((data) => {
          this.extensionHistory =successData.applicantVisaExtension;

          if (data.docType != 'PG') {
            let tempData = {
              "url": data.attachmentUrl,
              "docType": data.docType
            }
            this.samples.push(tempData)
          }
        })
        this.imageSrc = this.samples[0].url;
        this.imageType = this.samples[0].docType;
        //console.log(this.imageSrc,"les photos")
        //console.log(this.samples)
          //console.log(this.imageSrc, "image src")
          //console.log(this.imageType, "image type")
          //console.log(this.extensionHistory);
      }).catch(e => {
        this.ngxLoader.stop()
        this.toastr.errorToastr(e.message, 'Oops!')
      })
    }, 1000)
  }
  /****************************************************************************************************************/

  /**************************************************************************************************
     @PURPOSE      : open template for processing
     @PARAMETERS   : status and template id
     @RETURN       : NA
  **************************************************************************************************/
  public modalRef: BsModalRef
  public isStatus: any;
  processApplication(status, template) {
    this.isStatus = status;
    this.processingInfo.status = this.isStatus === 'APR' ? 'Granted' : this.isStatus === 'REJ' ? 'Rejected' : 'Need Validation'
    this.modalRef = this.modalService.show(template)
  }

  /*********************************************************************************************/


  /**************************************************************************************************
    @PURPOSE      : submitted the extension evisa
    @PARAMETERS   : formData
    @RETURN       : NA
 **************************************************************************************************/
  public submitted: boolean = false
  public processingInfo: any = {}
  submitRemarks(form, processingInfo) {
    if (form.valid) {
      let data = {
        "actionBy": this.getToken('username'),
        "remarks": this.processingInfo.remarks,
        "role": this.getToken('Role'),
        "status": this.isStatus,
        "visaExtensionId": this.refNumber
      }
      //console.log(data);

      this.ngxLoader.start();
      setTimeout(() => {
        this.commonService.callApi('visaextension/processapproval', data, 'post', false, false, 'APPR').then(success => {
          let successData: any = success;
          this.ngxLoader.stop()
          this.modalRef.hide();
          this.router.navigate(['/main/extensionevisasearch'])
          //console.log(successData)
        }).catch(e => {
          this.ngxLoader.stop()
          this.toastr.errorToastr(e.message, 'Oops!')
        });
      }, 1000)

    } else {
      this.submitted = true
    }
  }
  /***********************************************************************************/


  /**************************************************************************************************
      @PURPOSE      : Image animations
      @PARAMETERS   : NA
      @RETURN       : NA
   **************************************************************************************************/
  i = 1

  next() {
    //console.log("hai")
    //console.log(this.samples)
    if (this.i < this.samples.length) {
      //console.log(this.samples.length)
      this.imageSrc = this.samples[this.i].url
      this.imageType = this.samples[this.i].docType
      //console.log(this.imageType)
      //console.log(this.imageSrc)
      return this.i++
    } else {
      return this.i = 0
    }

  }

  prev() {
    if (this.i > 1) {
      this.imageSrc = this.samples[this.i - 2].url
      this.imageType = this.samples[this.i - 2].docType
      return this.i--
    } else {
      return this.i = this.samples.length + 1
    }
  }


  modalRefImage: BsModalRef;
  company: any
  openImage(template1) {
    this.modalRefImage = this.modalService.show(template1)
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
    } else {
      this.rotateright = (this.rotateright === 'default' ? 'rotated' : 'default');
    }
  }
  /*******************************************************************************************/
    approvalHistoryList: any = [];
    getAppDetails(appNo) {
        this.commonService.callApi('applicationpreview?applicationNumber=' + appNo, '', 'post', false, false, 'APPR').then(success => {
            let successData: any = success;
            this.loading = false;
            this.approvalHistoryList = successData.approverHistoryDetailsDTOList.approverHistoryDetailsDTOs;

        }).catch(e => {
            this.toastr.errorToastr(e.message, 'Oops!');
        });
    }
}
