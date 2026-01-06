import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';
import { NgxUiLoaderService } from 'ngx-ui-loader';

@Component({
  selector: 'app-exit-bco',
  templateUrl: './exit-bco.component.html',
  styleUrls: ['./exit-bco.component.css']
})
export class ExitBcoComponent extends BaseComponent implements OnInit {

  public loading: boolean = false;

  public submitted: boolean = false;
  applicationNumber: any;
  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      if (params['id']) {
        //console.log(params['id'])
        this.applicationNumber = params['id']
        this.getAppDetails(params['id'])
        this.getArrivalDeparture(params['id'])
      }
    })
  }
  processingInfo: any = {};

  ngOnInit(): void {
    this.getLocationdetails('ARLOC');
    this.processingInfo.exitDate = new Date();
    this.processingInfo.arrivalLocation = 'JIB';
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
  public successData: any;
  public Photograph: any;
  getAppDetails(appNo) {
    this.loading = true
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('applicationdeparturepreview?applicationNumber=' + appNo, '', 'post', false, false, 'APPR').then(success => {
        this.successData = success;
        //console.log(this.successData, "1234");
        
        this.loading = false
        this.personalPreview = this.successData.applicantPersonalDetails;
        this.passportPreview = this.successData.applicantPassportTravelDetails;
        this.Photograph = this.successData.applicationAttachmentDetailsList.attchPreviewDTOList[0].attachmentUrl;
        this.ngxLoader.stop()
        //console.log(this.approvalHistoryList);
      }
      ).catch(e => {
        this.ngxLoader.stop()
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    })

  }
  /****************************************************************************/

  submitExit(form, processingInfo) {
    if (form.valid) {
      let data = {
        "applicationNumber": this.applicationNumber,
        "carrierNo": this.processingInfo.carrierNo,
        "location": this.processingInfo.arrivalLocation,
        "id":parseInt(this.getToken('arrDepId')),
        "loggeduser": this.getToken('username'),
        "oprType": "D",
        "remarks": this.processingInfo.remarks,
        "role": this.getToken('Role'),
        "status":'APR'
      }

      setTimeout(() => {
        this.commonService.callApi('processdeparture', data, 'post', false, false, 'APPR').then(success => {
          let successData: any = success;
          //console.log(successData);
          
          if (successData.apiStatusCode === 'SUCCESS') {

            this.toastr.successToastr("success", successData.apiStatusDesc)
            this.router.navigate(['/main/search-departure'])
          } else {

            this.toastr.errorToastr("error", successData.apiStatusDesc)
          }

          //console.log(successData)
        }
        ).catch(e => {

          this.toastr.errorToastr(e.message, 'Oops!')
        });
      }, 1000)
    } else {
      this.submitted = true;
    }
  }

   /****************************************************************************
    @PURPOSE      : Retriving arrival/departure data
    @PARAMETERS   : application number
    @RETURN       : arrivalDepartureDTO
 ****************************************************************************/
    arrivalDepartureList: any = [];
    getArrivalDeparture(id) {
      this.commonService.callApi('arrivaldeparture/history/' + id, '', 'get', false, false, 'APPR').then(success => {
        let successData: any = success;
        this.arrivalDepartureList = successData.arrivalDepartureDetailsDTOs
        //console.log(this.arrivalDepartureList);
  
      }).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }
  
    /*****************************************************************************/

}
