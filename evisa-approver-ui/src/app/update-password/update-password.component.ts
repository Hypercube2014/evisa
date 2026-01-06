import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../common/commonComponent'

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.css']
})
export class UpdatePasswordComponent extends BaseComponent implements OnInit {

  public submitted: boolean = false;
  public passwordData: any = {};
  successData: any;

  constructor(inj: Injector) {
    super(inj)
  }

  ngOnInit(): void {

    if (window.opener == null) {
      localStorage.removeItem("evisaCount")
      this.router.navigate(["/home"])
    }
  }

  updatePassword(form, passwordData) {

    if (form.valid && (this.passwordData.newsecretkey === this.passwordData.confirmNewsecretkey)) {
      let resetpasswordData = {
        username: this.getToken('username'),
        secretKey: btoa(passwordData.secretKey),
        newsecretkey: btoa(passwordData.newsecretkey)
      }

      setTimeout(() => {

        passwordData.username = this.getToken('username')
        this.commonService.callApi('updateemployeecredentials', passwordData, 'post', false, false, 'LOG').then(success => {
          this.successData = success


          if (this.successData.status === "SUCCESS") {
            this.router.navigate(["/login"]);
            this.toastr.successToastr(this.successData.statusDescription, 'Success')
          } else {
            this.toastr.errorToastr(this.successData.statusDescription, 'Error')
          }
        }).catch(e => {
          this.toastr.errorToastr(e.error.statusDescription, 'Oops!')
        });
      }, 500)
    } else {
      this.submitted = true;
    }
  }


  password: any;
  show = false;
  isValue = false;
  isShow = false;
  onClick(type) {
    //console.log(type);
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


