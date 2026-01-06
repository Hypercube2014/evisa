import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent extends BaseComponent implements OnInit {

  userDetails: any = {}
  submitted: boolean = false
  isNew: boolean = false
  id: any

  constructor(inj: Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      if (params['id'] === 'new') {
        this.isNew = true;
      } else {
        this.id = params['id'];
        this.isNew = false;
      }
    })
  }

  ngOnInit(): void {
  }
  public roleDetails = [
    {
      'code': 'Admin',
      'name': "Admin"
    }
  ]
  /****************************************************************************
   @PURPOSE      : To Submit and Edit User details.
   @PARAMETERS   : form,userDetails
   @RETURN       : API Status
****************************************************************************/
  submitForm(form, userDetails) {
    if (form.valid) {
      //console.log(userDetails);

    } else {
      this.submitted = true
    }

  }
  /****************************************************************************/
}
