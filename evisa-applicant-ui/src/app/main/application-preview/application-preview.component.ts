import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../common/commonComponent';
var FileSaver = require('file-saver');
@Component({
  selector: 'app-application-preview',
  templateUrl: './application-preview.component.html',
  styleUrls: ['./application-preview.component.css']
})
export class ApplicationPreviewComponent extends BaseComponent implements OnInit {

  public applicationId:any;
  constructor(inj:Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      if (params['id']) {
           this.applicationId=params['id']   
      }
    })
   }

  ngOnInit(): void {
    this.getPreview();
  }


   /****************************************************************************
       @PURPOSE      : Retriving Attachments data
       @PARAMETERS   : NA
       @RETURN       : NA
    ****************************************************************************/
       public previewData: any;
       public personalPreview:any;
       public passportPreview:any;
       public attachmentsPreview = [];
       getPreview() {
         this.commonService.callApi('applicationpreview?applicationNumber=' + this.applicationId, '', 'post', false, false, 'REG').then(success => {
           let successData: any = success;
     
           this.personalPreview = successData.applicantPersonalDetails;
           this.passportPreview = successData.applicantPassportTravelDetails;
           this.attachmentsPreview = successData.applicantAttachmentDetailsDTOList.applicantAttachmentDTOs;
           this.attachmentsPreview.forEach((data)=>{
             if(data.attachmentType === 'PP'){
               data['order']=2;
              
             }else if(data.attachmentType === 'PG'){
              data['order']=1;
            
             }else if(data.attachmentType === 'TK'){
              data['order']=3;
             
             }else if(data.attachmentType === 'HI'){
               data['order']=4;
            
             }
            })
            this.attachmentsPreview.sort(function(a, b){
             return a.order - b.order;
         })
          //  //console.log(this.personalPreview)
     
         }
         ).catch(e => {
           this.toastr.error(e.message, 'Oops!')
         });
       }
       /****************************************************************************/



       public resource: any;
       downloadAttachment(data) {
         let sendData = {
           "applicationNumber": this.getToken('applicationNumber'),
           "attachmentType": data.attachmentType,
           "attachmentId": data.attachmentId
         }
     
         this.commonService.downloadAttachment('applicantattachments/attachmentid', sendData).subscribe((res) => {
           this.resource = res;
           let blob = new Blob([this.resource], { type: "application/xml;charset=UTF-8" });
           FileSaver.saveAs(blob, data.fileName);
         });
       }

}
