import { Component, OnInit,Injector} from '@angular/core';
import { BaseComponent } from '../../common/commonComponent';
@Component({
  selector: 'app-success-page',
  templateUrl: './success-page.component.html',
  styleUrls: ['./success-page.component.css']
})
export class SuccessPageComponent extends BaseComponent implements OnInit {

  public token:any;
  constructor(inj:Injector) { 
    super(inj)
    this.token = this.router.parseUrl(this.router.url).queryParams["session_id"];
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(){
    // //console.log('Token ', this.token)
    this.verifyUser();
    
}

verifyUser(){
  this.commonService.callApi('payment/success?session_id=' + this.token, '', 'get', false, false, 'REG').then(success => {
    let successData: any = success;
    // //console.log(successData);
    
  
  }
  ).catch(e => {
    // //console.log(e)
   // this.toastr.error(e.message, 'Oops!')
  });
}

}
