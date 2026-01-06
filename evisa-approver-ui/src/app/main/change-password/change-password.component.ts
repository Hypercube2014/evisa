import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../common/commonComponent';
import { NgxUiLoaderService } from "ngx-ui-loader";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent extends BaseComponent implements OnInit {
  public submitted: boolean = false;
  public data: any = {};
  public va

  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj)
  }
  ngOnInit(): void {
  }

  public successData: any = {};
  resetPassword(form, data) {

    if (form.valid && this.cnfrmPswd && this.newpswd) {
      this.ngxLoader.start();
      setTimeout(() => {
        data.username = this.getToken('username')
        this.commonService.callApi('updateemployeecredentials', data, 'post', false, false, 'LOG').then(success => {
          this.successData = success
          this.ngxLoader.stop();
          if (this.successData.status === "SUCCESS") {
            if(this.getToken('Role')=='DMM'){
              this.router.navigate(["/main/dashboardimo"]);
            }
           else if(this.getToken('Role')=='ADM'){
              this.router.navigate(["/main/code-maintenance"]);
            }else{
              this.router.navigate(["/main/dashboard"]);
            }
            
            this.toastr.successToastr(this.successData.statusDescription, 'Success')
          } else {
            this.toastr.errorToastr(this.successData.statusDescription, 'Error')
          }
        }).catch(e => {
          this.ngxLoader.stop();
          this.toastr.errorToastr(e.error.statusDescription, 'Oops!')
        });

      }, 1500)


    } else {
      this.submitted = true;
    }
  }
  cnfrmPswd: boolean
  newpswd: boolean;
  checkPassword(type) {

    if (type == 'CNP') {
      if (this.data.confirmNewsecretkey != this.data.newsecretkey) {
        this.cnfrmPswd = false
        //console.log(this.cnfrmPswd);
      } else {
        this.cnfrmPswd = true;
        //console.log(this.cnfrmPswd);
      }
    }
    if (type == 'PSW') {
      if (this.data.secretKey == this.data.newsecretkey) {
        this.newpswd = false
        //console.log(this.newpswd);
      } else {
        this.newpswd = true;
        this.cnfrmPswd = false
        //console.log(this.newpswd);
      }
    }
  }


  password: any;
  show = false;
  isValue = false;
  isShow = false;
  onClick(type) {
    if (type === 'secret') {
      if (this.password === 'password') {
        this.password = 'text';
        this.isValue = true;
      } else {
        this.password = 'password';
        this.isValue = false;
      }
    }
    if (type === 'newsecret') {
      if (this.password === 'password') {
        this.password = 'text';
        this.show = true;
      } else {
        this.password = 'password';
        this.show = false;
      }
    }
    if (type === 'cnfrmnew') {
      if (this.password === 'password') {
        this.password = 'text';
        this.isShow = true;
      } else {
        this.password = 'password';
        this.isShow = false;
      }
    }
  }

}
