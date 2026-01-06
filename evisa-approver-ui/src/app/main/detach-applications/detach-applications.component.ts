import { Component, OnInit, Injector } from '@angular/core';
import { BaseComponent } from '../../common/commonComponent'
@Component({
  selector: 'app-detach-applications',
  templateUrl: './detach-applications.component.html',
  styleUrls: ['./detach-applications.component.css']
})
export class DetachApplicationsComponent extends BaseComponent implements OnInit {

  agentDetails: any = {};
  constructor(inj: Injector) {
    super(inj)
  }

  ngOnInit(): void {
    this.getAgents("DM")
  }


  agentsList: any
  getAgents(oprType) {
    this.commonService.callApi('fetchagents/' + this.getToken('username') + '/' + this.getToken('Role') + '/' + oprType, '', 'get', false, false, 'APPR').then(success => {
      let successData: any = success;
      this.agentsList = successData.agentList;
      //console.log(this.agentsList);

    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }


  onChangeRole(agentName) {
    this.fetchApplications(agentName)
  }


  applicationList = [];
  appList = [];
  isList: boolean = false;
  fetchApplications(agentName) {

    this.commonService.callApi('fetchapplications/' + agentName, '', 'get', false, false, 'APPR').then(success => {
      let successData: any = success;
      //console.log(successData);
      this.appList = successData.agentList;
      if (successData.agentList.length) {
        successData.agentList.forEach((data) => {
          let tempdata = {
            "appNo": data,
            "isSelected": false
          }
          this.applicationList.push(tempdata)
        })

        this.isList = true;
      } else {
        this.applicationList = successData.agentList;
        this.isList = true;

      }



    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });

  }




  allSelected: boolean = false;
  public temprarySelected = [];

  selectAll() {
    if (this.allSelected) {

      this
        .applicationList
        .forEach(x => {
          this
            .selectedExams
            .push(x.appNo)
          x.isSelected = true;
        })
    } else {
      this
        .applicationList
        .forEach(x => {
          this.selectedExams = []
          x.isSelected = false;
        })
    }
    //console.log("Selected Item : ", this.selectedExams);
    //console.log(this.applicationList)
    this.temprarySelected = this.selectedExams;
  }
  selectedExams: Array<any> = []
  // *************************************************************//

  // event *************************************************************//
  isrow: boolean = false;
  rowSelected(i) {
    //console.log(i);
    //console.log(this.selectedExams)
    this.isrow = true;
    let count = 0;
    let index;
    for (let k = 0; k < this.selectedExams.length; k++) {
      if (i.appNo == this.selectedExams[k]) {
        count++;
        index = k;
      }
    }
    if (count) {
      this
        .selectedExams
        .splice(index, 1)
      if (this.allSelected) {
        this.allSelected = false;
      }
    } else {
      this
        .selectedExams
        .push(i.appNo)
      if (this.selectedExams.length == this.applicationList.length) {
        this.allSelected = true;
      }
    }
    //console.log("Selected Item : ", this.selectedExams)

  }
  // *************************************************************//


  sendData: any;
  detachAll(type) {
    if (type === 'A') {
      this.sendData = {
        agentList: this.appList
      }
      this.detachApps(this.sendData)
    } else {
      if (this.selectedExams.length) {
        this.sendData = {
          agentList: this.selectedExams
        }
        this.detachApps(this.sendData)
      } else {
        if (localStorage.getItem('Language') === 'en') {
          this.toastr.errorToastr("error!", "please select atleast one application to detach")
        } else {
          this.toastr.errorToastr("erreur!", "veuillez sélectionner au moins une application à détacher")
        }
      }

    }


  }




  detachApps(data) {
    this.commonService.callApi('deallocateapplications', data, 'post', false, false, 'APPR').then(success => {
      let successData: any = success;
      if (successData.apiStatusCode === 'SUCCESS') {
        this.toastr.successToastr("success", successData.apiStatusDesc);
        this.applicationList = [];
        this.fetchApplications(this.agentDetails.role)
      } else {
        this.toastr.errorToastr("error", successData.apiStatusDesc)
      }

    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
}
