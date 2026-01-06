import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-agents-details',
  templateUrl: './agents-details.component.html',
  styleUrls: ['./agents-details.component.css']
})
export class AgentsDetailsComponent extends BaseComponent implements OnInit {
  employeeDetails:any={}
  submitted:boolean=false

  constructor(inj:Injector) {
    super(inj);
  }

  ngOnInit(): void {
    this.getRoles()
  }

  submitForm(form, employeeDetails) {
    if(form.valid){
      //console.log(employeeDetails);
    }else{
      this.submitted=true
    }
    
    
  }


}
