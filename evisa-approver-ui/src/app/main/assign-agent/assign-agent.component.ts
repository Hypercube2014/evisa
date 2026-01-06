import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-assign-agent',
  templateUrl: './assign-agent.component.html',
  styleUrls: ['./assign-agent.component.css']
})
export class AssignAgentComponent implements OnInit {

  agentDetails: any = {}
  submitted: boolean = false

  suspendAgent: any = [{
    "name": "Hemanth",
    "code": "Hemanth"
  }]

  constructor() { }

  ngOnInit(): void {
  }

  submitForm(form, agentDetails) {
    if (form.valid) {
      //console.log(agentDetails);
    } else[
      this.submitted = true
    ]
  }

}
