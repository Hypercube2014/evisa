import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../common/commonComponent'
import { NgxUiLoaderService } from 'ngx-ui-loader';

@Component({
  selector: 'app-agent-tracker',
  templateUrl: './agent-tracker.component.html',
  styleUrls: ['./agent-tracker.component.css']
})
export class AgentTrackerComponent extends BaseComponent implements OnInit {
  agentDetails: any = {}
  roleCode: any

  public totalItem: any;
  pagesize: number = 4
  public page = 1;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }

  periodDetails = [
    {
      code: 'T',
      desc: 'Aujourd\'hui'
    }, {
      code: 'W',
      desc: 'Hebdomadaire'
    }, {
      code: 'M',
      desc: 'Mensuel'
    }, {
      code: 'Y',
      desc: 'Annuel'
    }
  ]
  public rolesList = [
    {
      'code': "DM",
      'name': 'Decision Maker'
    },
    {
      'code': 'BCO',
      'name': 'Border Control Officer'
    },
    {
      'code': "SBCO",
      'name': 'Senoir Border Control Officer'
    }
  ]
  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj)
  }

  ngOnInit(): void {
    if (this.getToken('Role') === 'SBCO') {
      this.roleCode = 'BCO'
    }
    // this.agentsTracker(this.data)
  }


  onChangeRole(event: any) {
    this.isShow = false
    if (this.getToken('Role') === 'DMM') {
      this.roleCode = event
      this.roleCode = this.roleCode.code
    }


    // this.agentsTracker(this.data)
    this.result = false
  }

  isShow: boolean = false
  applyFilter(form, agentDetails) {
    if (form.valid) {
      this.data.period = agentDetails.period
      this.isShow = true
      this.result = false
      this.agentsTracker(this.data)

    } else if (!this.agentDetails.role && !this.agentDetails.period) {
      if (localStorage.getItem('Language') === 'en') {
        this.toastr.errorToastr("Please select any value to filter", 'Oops!');
        return 0;
      } else {
        this.toastr.errorToastr("Veuillez sélectionner n'importe quelle valeur de données pour filtrer", 'Oops!');
        return 0;
      }

    } else if (!this.agentDetails.role) {
      if (localStorage.getItem('Language') === 'en') {
        this.toastr.errorToastr("Please select agent role data to filter", 'Oops!');
        return 0;
      } else {
        this.toastr.errorToastr("Veuillez sélectionner un agent pour filtrer les données", 'Oops!');
        return 0;
      }


    } else if (!this.agentDetails.period) {
      if (localStorage.getItem('Language') === 'en') {
        this.toastr.errorToastr("Please select period  data to filter", 'Oops!');
        return 0;
      } else {
        this.toastr.errorToastr("Veuillez sélectionner de période à filtrer les données",'Oops!');
        return 0;
      }
    }
  }

  /****************************************************************************
  @PURPOSE      : Get all Agents based on Role 
  @PARAMETERS   : pageNumber,PageSize,loggeuser
  @RETURN       : NA
  ****************************************************************************/
  public details: any;
  public loading: boolean = false;
  public noofelements: 0;
  public totalElements: 0;

  agentsTracker(data) {
    //console.log(data, "agents tracker");

    data.loggedUser = this.getToken('username')
    data.oprType = this.roleCode;
    data.role = this.getToken('Role')
    this.loading = true;
    this.commonService.callApi('agenttracker', data, 'post', 'false', 'false', 'APPR').then(success => {
      let successData: any = success
      this.totalItem = successData.totalElements;
      this.loading = false;
      this.ngxLoader.stop();
      this.noofelements = successData.numberOfElements;
      this.totalElements = successData.totalElements;
      if (successData.content.length) {
        //console.log(successData.content, "content")
        this.details = successData.content;
        //console.log(this.details);

      } else {
        this.details = []
      }

    }).catch(e => {
      this.ngxLoader.stop()
      this.toastr.errorToastr(e.message, 'Oops!')
    });

  }
  /***********************************************************************************/
  Applications: any = []
  public filterObj: any = {};
  public numbers: 0;
  public sizes: 0;
  public numofelements: 0;
  public totelements: 0;
  public result: boolean = false
  public totalitem: any;
  pagenumber = 10
  public pages = 1;

  public value: any = {
    pageNumber: 1,
    pageSize: this.pagenumber,
  }
  load: boolean = false
  username: any;
  role: any
  callAgentDisplay(username, role) {
    this.load = true
    this.result = true
    this.username = username
    this.role = role
    this.isFilterApplied = true;
    this.currentPage = 1;
    this.filterObj.pageNumber = 1;
    this.filterObj.pageSize = this.pagenumber;
    this.getApplicantList(this.value);
  }

  callAgentDisplaySbco(username) {
    this.load = true
    this.result = true
    this.username = username
    this.isFilterApplied = true;
    this.currentPage = 1;
    this.getApplicantList(this.value);
  }

  public sendApi: any;
  getApplicantList(value) {
    value.loggedUser = this.username;
    value.period = this.agentDetails.period
    //console.log(value);

    if (this.roleCode === 'SBCO' || this.roleCode === 'BCO') {
      this.sendApi = 'agenttrackerforsbco'
    }

    if (this.roleCode === 'DM') {
      value.role = this.role
      this.sendApi = 'searchapplicationtracker'
    }


    this.ngxLoader.start()
    setTimeout(() => {
      this.commonService.callApi(this.sendApi, value, 'post', false, false, 'APPR').then(success => {
        let successData: any = success
        this.totalitem = successData.totalElements;
        this.numbers = successData.number;
        this.sizes = successData.size;
        this.load = false;
        this.ngxLoader.stop();
        this.numofelements = successData.numberOfElements;
        this.totelements = successData.totalElements;
        if (successData.content.length) {
          this.Applications = successData.content;
          //console.log(successData.content, "successData.content");
          //console.log(this.Applications);
        } else {
          this.Applications = []
        }

      }).catch(e => {
        this.ngxLoader.stop()
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    })
  }





  /****************************************************************************
  @PURPOSE      : Paginations 
  @PARAMETERS   : pageNumber,PageSize,loggedinuse
  @RETURN       : NA
  ****************************************************************************/
  public currentPage = 1;
  public isFilterApplied: boolean = false;
  pageChanged(e) {
    if (this.isFilterApplied) {
      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      this.agentsTracker(this.filterObj);

    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.agentsTracker(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      //console.log(e)
      this.filterObj.pageSize = e;
      this.agentsTracker(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.agentsTracker(this.data);
    }

  }
  /***************************************************************/

  public showBoundaryLinks = true;
  public currentpage = 1;
  public rangelist = [5, 10, 25, 100];
  public isFilterapplied: boolean = false;
  public filterobj: any = {};
  pageChange(e) {
    this.Applications = [];
    if (this.isFilterApplied) {

      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      this.getApplicantList(this.filterObj);

    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getApplicantList(this.value);
    }
  }
  rangeChange(e) {
    if (this.isFilterApplied) {
      //console.log(e)
      this.filterObj.pageSize = e;
      this.filterObj.pageNumber = 1;
      this.getApplicantList(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getApplicantList(this.value);
    }

  }

  clearFields() {
    this.agentDetails = {};
    this.isShow = false;
    this.result = false
  }
}
