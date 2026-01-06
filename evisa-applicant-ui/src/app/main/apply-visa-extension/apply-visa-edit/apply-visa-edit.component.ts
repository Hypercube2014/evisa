import { DatePipe } from '@angular/common';
import { DepFlags } from '@angular/compiler/src/core';
import { Component, Injector, OnInit ,ElementRef,ViewChild} from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { environment } from 'src/environments/environment';
import { BaseComponent } from '../../../common/commonComponent';

@Component({
  selector: 'app-apply-visa-edit',
  templateUrl: './apply-visa-edit.component.html',
  styleUrls: ['./apply-visa-edit.component.css']
})
export class ApplyVisaEditComponent extends BaseComponent implements OnInit {
  @ViewChild('takeInput')  AttachVar: ElementRef;
  attachmentCount=0;
  extensionVisa: any = {}
  attachedFileSize:boolean=false;
  AttachedFiles=[];
  fileInfo:any={};
  submitted: boolean = false
  colorTheme = 'theme-blue'

  datePickerConfig = {
    dateInputFormat: 'DD-MM-YYYY',
    containerClass: this.colorTheme,
    showWeekNumbers: false,
  }
  applyVisaTypes: any = []
  public loading: boolean;
  public applicantNo = this.getToken('applicantNo');
  passport = this.getToken('passport');
  givenName = this.getToken('givenName');
  fileNumber = this.getToken('fileNumber');
  emailId = this.getToken('emailId');
  VisaDate: any = this.getToken('VisaDate')

  constructor(inj: Injector, public datepipe: DatePipe, private ngxLoader: NgxUiLoaderService) {
    super(inj)
  }

  ngOnInit(): void {
    this.VisaDate = new Date(this.VisaDate)
    this.extensionVisa.totalAmount = 0
    this.extensionVisa.penalityAmount = 0
    this.getVisaExtensionType();
    this.getExtension('EXTRS');

  }

  submitForm(form, extensionVisa) {
    extensionVisa = {
      "applicationNumber": this.applicantNo,
      "daysOfExtension": this.duration,
      "penalityAmount": 0,
      "reasonsForExtension": extensionVisa.reasonsForExtension,
      "status": "PP",
      "totalAmount": extensionVisa.totalAmount,
      "username": this.getToken('username'),
      "visaType": this.extensionVisa.daysOfExtension,
      "previousExpiryDate": this.VisaDate
    }
    if (form.valid) {
      this.loading = true;
      this.ngxLoader.start();

      const uploadData = new FormData();
      const sendData = {

        "applicationNumber": this.applicantNo,
        "daysOfExtension": this.duration,
        "penalityAmount": 0,
        "reasonsForExtension": extensionVisa.reasonsForExtension,
        "totalAmount": extensionVisa.totalAmount,
        "username": this.getToken('username'),
        "visaType": this.extensionVisa.daysOfExtension,
        "previousExpiryDate": this.VisaDate,

      }
      if(extensionVisa.reasonsForExtension === 'OTH'){
        sendData['otherRemarks']=extensionVisa.otherRemarks
      }
   
        
      
      
      const resdata = new Blob([JSON.stringify(sendData)], {
        type: 'application/json',
      });
      uploadData.append("file", this.AttachedFiles[0]); 
      uploadData.append('ApplicantVisaExtension', resdata);
      setTimeout(() => {
        this.commonService.callApi('applyvisaextension', uploadData, 'post', false, false, 'REG').then(async success => {
          let successData: any = success;
          this.loading = false;
          this.ngxLoader.stop();
          if (successData.statusCode === "SUCCESS") {
            this.toastr.success('Success')
            let stripePromise = loadStripe(environment.stripe_key);
            // Call your backend to create the Checkout session.
            // When the customer clicks on the button, redirect them to Checkout.
            const stripe = await stripePromise;
            const result = stripe.redirectToCheckout({
              sessionId: successData.refId,
            });
          }else{
            this.toastr.error(successData.statusDesc,'Oops!')
          }
        }).catch(e => {
          this.toastr.error(e.message, 'Oops!')
        });
      }, 1500);
    } else {
      this.submitted = true
      // //console.log("else block ");

    }
  }


  public duration: any
  selectVisa(e) {
    // //console.log(e);
    this.extensionVisa.totalAmount = e.visaFee
    this.duration = e.visaDuration
  }

  getVisaExtensionType() {
    this.commonService.callApi('visadetails/extension', '', 'get', false, false, 'REG').then(success => {
      let successData: any = success
      this.applyVisaTypes = successData.masterCodeResultDTOs;
      // //console.log(this.applyVisaTypes);

    }).catch(e => {
      this.toastr.error(e.message, 'Oops!')
    });
  }

  isOther: boolean = false;
  selectReason(e) {
    if (e.code === 'OTH') {
      this.isOther = true;
    } else {
      this.isOther = false;
    }
  }


  selectedFiles: any = [];
 
  payloadUpload(event) {
    let reader = new FileReader();
    // when the load event is fired and the file not empty
    if (event.target.files && event.target.files.length > 0) {
      // Fill file variable with the file content

      if (!this.validateFile(event.target.files[0].name)) {
        this.toastr.error("error", "selected file format is not supported");
        return false;
      }

      if(this.validateSize(event.target.files[0].size)){
        this.toastr.error("error", "file size exceeds limit");
        return false;
      }

      for (let i = 0; i < event.target.files.length; i++) {
        this.AttachedFiles.push(event.target.files[i])

      }

    }
  }

  validateFile(name: String) {
  
      var ext = name.substring(name.lastIndexOf('.') + 1);
      if ((ext.toLowerCase() == 'jpeg') || (ext.toLowerCase() == 'png') || (ext.toLowerCase() == 'pdf') ||  (ext.toLowerCase() == 'jpg')) {
        return true;
      }
      else {
        return false;
      }
    
  }

  validateSize(size) {
    this.attachmentCount=0;
    this.attachmentCount = this.attachmentCount+size;
    // //console.log(this.attachmentCount);
    if( this.attachmentCount > 2000000 ){
      
      return true;
    }else{
      return false;
    }
    
}


  deleteDoc(index) {

    //this.payloadCount = 0;
    this.AttachVar.nativeElement.value = "";
    this.fileInfo.fileattach='';
   // this.payLoadFileSize=false;
    this.AttachedFiles.splice(index, 1);
  }
}
