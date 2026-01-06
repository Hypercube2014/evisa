import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent';

@Component({
  selector: 'app-view-visa',
  templateUrl: './visa-view.component.html',
  styleUrls: ['./visa-view.component.css']
})
export class VisaViewComponent extends BaseComponent implements OnInit {
  successData: any;
  visaId: any;

  constructor(inj: Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      this.visaId = params['id'];
      this.fetchVisaDetails(this.visaId);
    })
   
  }

  ngOnInit(): void {
  }

  fetchVisaDetails(id) {
    this.commonService.callApi('visadetails/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.successData = success;
      this.getCurrency()
      //console.log(this.successData.currency);
      
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }


  /****************************************************************************
     @PURPOSE      : To Retrive Country codes
     @PARAMETERS   : type
     @RETURN       : NA
    ****************************************************************************/
     public currencyDetails: any;
     getCurrency() {

       this.commonService.callApi('mastercode/active/CRNCY', '', 'get', false, false, 'APPR').then(success => {
         let successData: any = success;
         this.currencyDetails = successData.masterCodeResultDTOs;
         for (let value of this.currencyDetails.values()) {
          if (value.code === this.successData.currency) {
            this.currencyDetails = value.description
            //console.log(this.currencyDetails);
            
          }
        }
        
        
       }
       ).catch(e => {
         this.toastr.errorToastr(e.message, 'Oops!')
       });
   
     }
     /****************************************************************************/

}
