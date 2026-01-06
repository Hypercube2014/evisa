import { Component, OnInit, Injector } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';
@Component({
  selector: 'app-transfer-agent',
  templateUrl: './transfer-agent.component.html',
  styleUrls: ['./transfer-agent.component.css']
})
export class TransferAgentComponent extends BaseComponent implements OnInit {
  selectedItems: any = [];
  transferAgentDetails: any = {}
  submitted: boolean = false;
  agentsList: any[] = [
    // { name: 'Movie 1' },
    // {  name: 'Movie 2'  },
    // {  name: 'Movie 3'  },
    // {  name: 'Movie 4'  },
    // { name: 'Movie 5'}
  ];

  selectedAgents: any[] = [];

  selectedRoles: any[] = [];


  roleTypes = [
    {
      "name": "Decision Maker",
      "code": "DM"
    },
    {
      "name": "senior Border Control Officer",
      "code": "SBCO"
    }

  ]

  groupsArray =
    [{ 'name': 'Hemanth' },
    { 'name': 'Sai' },
    { 'name': 'Ravi' },
    { 'name': 'Murali' },
    { 'name': 'Hemanth' },
    { 'name': 'Sai' },
    { 'name': 'Ravi' },
    { 'name': 'Murali' }]

  constructor(inj: Injector) {
    super(inj)
  }

  ngOnInit(): void {
    this.getManagers('F');
  }

  submitForm(form, transferAgentDetails) {
    if (form.valid) {

      if (!this.selectedAgents.length) {
        this.toastr.errorToastr("error", "Please select atleast one agent");
        return 0;
      }


      let names = this.selectedAgents.map(function (item) {
        return item['name'];
      });


      let data = {
        agentList: names,
        reportType: transferAgentDetails.toManager
      }
      this.commonService.callApi('transferagents', data, 'post', false, false, 'APPR').then(success => {
        let successData: any = success;
        //console.log(successData)
        if (successData.apiStatusCode === 'SUCCESS') {
          form.reset();
          this.agentsList = [];
          this.selectedAgents = [];
          this.submitted = false;
          this.isagentList = false;
          this.toastr.successToastr("success", successData.apiStatusDesc)
        } else {
          this.toastr.errorToastr("error", successData.apiStatusDesc)
        }

      }
      ).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });

    } else {

      this.submitted = true
    }
  }

  clearFields(form) {
    form.reset();
    this.selectedAgents = [];
    this.isagentList = false;
  }


  assign() {
    if (this.transferAgentDetails.selectedToAdd.length === 1) {
      if (this.selectedItems.length) {
        //console.log("haii");
        //console.log(this.selectedItems)
        this.selectedItems.forEach((ele) => {
          if (this.transferAgentDetails.selectedToAdd[0] != ele) {
            this.selectedItems = this.selectedItems.concat(this.transferAgentDetails.selectedToAdd);

            this.groupsArray = this.groupsArray.filter(selectedData => {
              return this.selectedItems.indexOf(selectedData) < 0;
            });
            this.transferAgentDetails.selectedToAdd = this.selectedItems;
          }
        })
      } else {
        //console.log("haii")
        this.selectedItems = this.selectedItems.concat(this.transferAgentDetails.selectedToAdd);
        //console.log(this.selectedItems)
        this.groupsArray = this.groupsArray.filter(selectedData => {
          return this.selectedItems.indexOf(selectedData) < 0;
        });
        this.transferAgentDetails.selectedToAdd = this.selectedItems;
      }
    }
  }

  unassign() {
    this.groupsArray = this.groupsArray.concat(this.transferAgentDetails.selectedToRemove);
    this.selectedItems = this.selectedItems.filter(selectedData => {
      return this.groupsArray.indexOf(selectedData) < 0;
    });
    this.transferAgentDetails.selectedToRemove = this.selectedItems;
  }

  public selectedRole: any = [];
  fromRole(e) {
    this.selectedRole = e.code
    //console.log(this.selectedRole);
    this.isagentList = false;
    this.agentsList = [];
    //console.log(this.selectedManager);
    this.getAgents(this.selectedManager, 'DMM', this.selectedRole);
    if (e.username === this.transferAgentDetails.toManager) {
      this.transferAgentDetails.toManager = '';
    }
  }


  managersList: any;
  getManagers(type) {
    if (this.getToken('Role') == 'ADM') {
      this.commonService.callApi('employeesbyrolecode/' + 'DMM', '', 'get', false, false, 'APPR').then(success => {
        let successData: any = success;
        if (type === 'F') {
          this.managersList = successData.employeeDetailsDTOs;
        } else {
          successData.employeeDetailsDTOs.forEach((ele) => {
            if (ele.username === this.transferAgentDetails.fromManger) {
              ele['disabled'] = true;
            }
          })
          this.tomanagersList = successData.employeeDetailsDTOs
        }
        //console.log(this.tomanagersList);

      }
      ).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }
    else if (this.getToken('Role') == 'DMM') {
      this.commonService.callApi('fetchagents/' + this.getToken('username') + '/' + 'DMM' + '/' + 'SBCO', '', 'get', false, false, 'APPR').then(success => {
        let successData: any = success;
        //console.log(successData);
        if (type === 'F') {
          this.managersList = successData.agentList;
        } else {
          let managerList = successData.agentList
          this.tomanagersList = managerList.filter(x => x != this.selectedManager);
        }
      }
      ).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }
  }




  isagentList: boolean = false;
  roleType: any
  getAgents(loggedUser, role, oprType) {
    this.commonService.callApi('fetchagents/' + loggedUser + '/' + role + '/' + oprType, '', 'get', false, false, 'APPR').then(success => {
      let successData: any = success;

      //console.log(successData.agentList)
      if (successData.agentList.length) {
        this.isagentList = true;

        successData.agentList.forEach(element => {
          let data = {
            "name": element
          }
          this.agentsList.push(data)
        });
        //console.log(this.agentsList)
      } else {
        this.agentsList = [];
        this.isagentList = false;
        this.toastr.errorToastr("error", "Selected manager don't have any agents to transfer")
      }
      //console.log(this.agentsList);

    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }


  toManagersList: any;
  tomanagersList = [];
  selectedManager: any
  fromManager(event) {
    if (this.getToken('Role') == 'DMM') {
      //console.log(event);
      this.isagentList = false;
      this.agentsList = [];
      this.getAgents(event, 'SBCO', 'BCO');
      this.selectedManager = event
      if (event === this.transferAgentDetails.toManager) {
        this.transferAgentDetails.toManager = '';
      }
    } else if (this.getToken('Role') == 'ADM') {
      if (event) {
        //console.log(event);
        this.isagentList = false;
        this.agentsList = [];
        this.selectedManager = event.username
      }
    }
  }
}
