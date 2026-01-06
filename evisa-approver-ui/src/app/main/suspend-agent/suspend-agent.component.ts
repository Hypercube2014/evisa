import { Component, Injector, OnInit } from '@angular/core';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-suspend-agent',
  templateUrl: './suspend-agent.component.html',
  styleUrls: ['./suspend-agent.component.css']
})
export class SuspendAgentComponent extends BaseComponent implements OnInit {
  public suspenAgentDetails: any = {}
  public submitted: boolean = false

  constructor(inj: Injector) {
    super(inj);
  }

  ngOnInit(): void {
  }

  bsConfig: Partial<BsDatepickerConfig>;
  maxDate = new Date();

  colorTheme = 'theme-blue'
  datePickerConfig = {
    dateInputFormat: 'DD-MM-YYYY',
    containerClass: this.colorTheme,
    maxDate: new Date()
  }

  suspendAgent: any = [{
    "name": "Hemanth",
    "code": "Hemanth"
  }]

  /****************************************************************************
   @PURPOSE      : To Submit and Suspend Agent details.
   @PARAMETERS   : form,suspendAgentDetails
   @RETURN       : API Status
****************************************************************************/
  submitForm(form, suspendAgentDetails) {
    if(form.valid){
      //console.log(suspendAgentDetails);
    }else{
      this.submitted=true
    }

  }

/****************************************************************************/
}
