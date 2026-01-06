import { Component, Injector, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { NgPopupsService } from 'ng-popups';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css']
})
export class EmployeeListComponent extends BaseComponent implements OnInit {

  @ViewChild('template1') template1: TemplateRef<HTMLDivElement>;

  public statusEmployee: any;

  public totalItem: any;
  pagesize: number = 10

  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }

  constructor(inj: Injector, private ngPopups: NgPopupsService, private ngxLoader: NgxUiLoaderService) {
    super(inj);
  }

  ngOnInit(): void {
    this.getEmployeeList(this.data)

  }
  /****************************************************************************
  @PURPOSE      : To Show popup on Clicking Reset Passsword 
  @PARAMETERS   : NA
  @RETURN       : NA
****************************************************************************/
  password: any
  onResetPassword(username) {
    if (localStorage.getItem('Language') === 'en') {
      this.password = "Are you sure, Want to reset password?"
    } else {
      this.password = "Êtes-vous sûr,voulez-vous réinitialiser le mot de passe?"
    }

    this.ngPopups.confirm(this.password)
      .subscribe(res => {
        if (res) {
          //console.log(username);
          this.commonService.callApi('resetsecretkey/' + username, '', 'get', false, false, 'APPR').then(success => {
            let successData: any = success
            if (successData.status === "SUCCESS") {
              this.toastr.successToastr(successData.statusDescription, 'Success')
            } else {
              this.toastr.errorToastr(successData.statusDescription, 'Error');
            }
          }).catch(e => {
            this.toastr.errorToastr(e.message, 'Oops!');
          });
        }
      });
  }
  /****************************************************************************/

  /****************************************************************************
  @PURPOSE      : To Show popup on Clicking Activate 
  @PARAMETERS   : NA
  @RETURN       : NA
****************************************************************************/
  userName: any;
  employeeStatus: any
  statusValue: any
  statusCode: any
  onAcivate(username, status) {
    let statusValueFr: any
    this.userName = username;
    if (status === 'Y') {
      this.employeeStatus = false;
      this.statusValue = "Deactivate"
      statusValueFr = "désactiver",
      this.statusCode = "DATV";
    } else {
      this.employeeStatus = true;
      this.statusValue = "Activate"
      this.statusCode = "ATV";
      statusValueFr = "activer"

    }
    if (localStorage.getItem('Language') === 'en') {
      this.ngPopups.confirm('Are you sure, Want to ' + this.statusValue + '?').subscribe(res => {
        if (res) {
          this.modalPopup()
        }
      });
    } else {
      this.ngPopups.confirm('Êtes-vous sûr,voulez-vous' + statusValueFr + '?').subscribe(res => {
        if (res) {
          this.modalPopup()
        }
      });
    }

  }
  /****************************************************************************/

  /****************************************************************************
      @PURPOSE      : To retrive List of Roles.
      @PARAMETERS   : data
      @RETURN       : List of Role Details
   ****************************************************************************/
  employeeList: any = []
  public number: 0;
  public size: 0;
  public noofelements: 0;
  public totalElements: 0;
  public loading: boolean = false;
  getEmployeeList(data) {
    this.loading = true
    data.loggedUser = this.getToken('username')

    if (this.getToken('Role') === 'ADM') {
      data.loggedUser = null
    }
    //console.log(data);

    this.ngxLoader.start()
    setTimeout(() => {
      this.commonService.callApi('searchemployeedetails', data, 'post', false, false, 'APPR').then(success => {
        let successData: any = success
        this.totalItem = successData.totalElements;
        this.number = successData.number;
        this.size = successData.size;
        this.loading = false;
        this.ngxLoader.stop();
        this.noofelements = successData.numberOfElements;
        this.totalElements = successData.totalElements;
        if (successData.content.length) {
          this.employeeList = successData.content;
          //console.log(this.employeeList);

        } else {
          this.employeeList = []
        }

      }).catch(e => {
        this.ngxLoader.stop()
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    })
  }

  /****************************************************************************/


  /****************************************************************************
      @PURPOSE      : To retrive the roles List
      @PARAMETERS   : pageNumber,PageSize,loggedinuser
      @RETURN       : NA
   ****************************************************************************/
  public currentPage = 1;
  public showBoundaryLinks = true;
  public rangeList = [5, 10, 25, 100];
  public isFilterApplied: boolean = false;
  public filterObj: any = {};

  pageChanged(e) {
    if (this.isFilterApplied) {
      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      this.getEmployeeList(this.filterObj);
    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getEmployeeList(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      //console.log(e)
      this.filterObj.pageSize = e;
      this.getEmployeeList(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getEmployeeList(this.data);
    }
  }
  /****************************************************************************/


  /****************************************************************************
  @PURPOSE      : To send the username to User Exists Method.
  @PARAMETERS   : $Event
  @RETURN       : Boolean
  ****************************************************************************/
  timeout: any = null;
  onKeySearchUser(event: any) {
    clearTimeout(this.timeout);
    let $this = this;
    this.timeout = setTimeout(function () {
      if (event.keyCode != 13) {
        $this.getUserExists(event.target.value);
        if (event.target.value) {
          event.target.value;
          $this.getUserExists(event.target.value);
        } else {
          this.isUserExists = true;
        }
      }
    }, 1000);
  }
  /****************************************************************************/

  /****************************************************************************
  @PURPOSE      : To verify the username to User Exists or not in DB.
  @PARAMETERS   : username
  @RETURN       : Boolean
  ****************************************************************************/
  getUserExists(username) {
    this.data.username = username
    this.commonService.callApi('searchemployeedetails/', this.data, 'post', false, false, 'APPR').then(success => {
      this.getEmployeeList(this.data)
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!');
    });
  }
  /****************************************************************************/



  /****************************************************************************
    @PURPOSE      : To Show another popup on Clicking Activate 
    @PARAMETERS   : NA
    @RETURN       : NA
  ****************************************************************************/
  modalRef: BsModalRef;
  modalPopup() {
    this.formDetails = {};
    this.getReasons()
    this.modalRef = this.modalService.show(this.template1);

  }

  submitted: boolean = false;
  changeStatus: any;
  formDetails: any = {}
  submitForm(form, formDetails) {
    if (form.valid) {
      formDetails.username = this.userName;
      formDetails.loggedUserRole = this.getToken('Role')
      formDetails.loggeduser = this.getToken('username')
      formDetails.status = this.employeeStatus
      this.commonService.callApi('changestatus', formDetails, 'post', 'false', 'false', 'APPR').then(success => {
        this.changeStatus = success;

        this.getEmployeeList(this.data);
        this.toastr.successToastr(this.changeStatus.apiStatusDesc, this.changeStatus.apiStatusCode)
      }).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
      this.modalRef.hide()
    } else {
      this.submitted = true;
    }

  }
  /********************************************************************************/

  /****************************************************************************
    @PURPOSE      : To get reasons for Activate/ De-Activate
    @PARAMETERS   : NA
    @RETURN       : NA
  ****************************************************************************/
  public statusChange: any
  getReasons() {
    this.commonService.callApi('mastercode/active/' + this.statusCode, '', 'get', 'false', 'false', 'APPR').then(success => {
      let successData: any = success
      this.statusChange = successData.masterCodeResultDTOs
      //console.log(this.statusChange);
      if (this.employeeStatus) {
        if (localStorage.getItem('Language') === 'en') {
          this.statusEmployee = "Reason for Activate"
        } else {
          this.statusEmployee = "Motif de l'activation."
        }
      } else {
        if (localStorage.getItem('Language') === 'en') {
          this.statusEmployee = "Reason for Deactivate"
        } else {
          this.statusEmployee = "Motif de la désactivation."
        }
      }

    })
  }
  /**************************************************************************/
}
