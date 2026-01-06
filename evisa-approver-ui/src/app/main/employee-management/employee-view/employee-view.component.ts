import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-employee-view',
  templateUrl: './employee-view.component.html',
  styleUrls: ['./employee-view.component.css']
})
export class EmployeeViewComponent extends BaseComponent implements OnInit {

  employeeDetails: any = {}

  public id: string
  roleDesc: any;

  constructor(inj: Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      this.id = params['id'];
      this.getEmployeeDetails(this.id)
    })
  }

  ngOnInit(): void {
  }

  /****************************************************************************
  @PURPOSE      : To retrive Role Details by ID.
  @PARAMETERS   : ID
  @RETURN       : Details of Role Code
****************************************************************************/
  getEmployeeDetails(id) {
    this.commonService.callApi('employeedesc/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.employeeDetails = success;     
      if (this.employeeDetails.reportingTo) {
        this.getRoleCode()
      }
    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /****************************************************************************/


  /****************************************************************************
     @PURPOSE      : To retrive Reporting To 
     @PARAMETERS   : NA
     @RETURN       : Full Name
   ****************************************************************************/
  public roleCodeDetails: any = {}
  public role: any;
  getRoleCode() {
    this.commonService.callApi('employee/' + this.employeeDetails.reportingTo, '', 'get', false, false, 'APPR').then(success => {
      this.roleCodeDetails = success;
      //console.log(this.roleCodeDetails.fullName);
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /***************************************************************************/
}
