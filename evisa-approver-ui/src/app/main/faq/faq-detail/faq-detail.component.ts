import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent'

@Component({
  selector: 'app-faq-detail',
  templateUrl: './faq-detail.component.html',
  styleUrls: ['./faq-detail.component.css']
})
export class FaqDetailComponent extends BaseComponent implements OnInit {

  public faqDetails: any = {};
  public submitted: boolean = false;
  public isNew: boolean = false;
  public faqId: any;
  public successData: any;


  public statusDetails = [
    {
      'code': 'Y',
      'name': "Active"
    },
    {
      'code': 'N',
      'name': "Inactive"
    }
  ]

  constructor(inj: Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      if (params['id'] === 'new') {
        this.isNew = true;
      } else {
        this.faqId = params['id'];
        this.fetchSystemFaqId(this.faqId);
        this.isNew = false
      }
    })
  }

  ngOnInit(): void {
  }

  /****************************************************************************
    @PURPOSE      : To submit FAQ details.
    @PARAMETERS   : form,formdata
    @RETURN       : NA
 ****************************************************************************/

  submitForm(form, faqDetails) {
    if (this.isNew) {
      if (form.valid) {
        this.faqDetails.createdBy = this.getToken('username')
        this.commonService.callApi('systemfaq', faqDetails, 'post', false, false, "APPR").then(success => {
          this.successData = success;
          if (this.successData.apiStatusCode === "SUCCESS") {
            this.router.navigate(["/main/faq/faq-list"]);
            this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
          } else {
            this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error')
          }
        }).catch(e => {
          this.toastr.errorToastr(e.message, 'Oops!')
        });
      } else {
        this.submitted = true;
      }
    } else {
      this.faqDetails.updatedBy = this.getToken('username');
      this.faqDetails.faqId = this.faqId;
      this.commonService.callApi('systemfaq', faqDetails, 'put', false, false, 'APPR').then(success => {
        this.successData = success;
        if (this.successData.apiStatusCode === "SUCCESS") {
          this.router.navigate(["/main/faq/faq-list"]);
          this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
        } else {
          this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error')
        }
      }).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }
  }

  /******************************************************************************/


  /****************************************************************************
    @PURPOSE      : Fetch FAQ details.
    @PARAMETERS   : form,formdata
    @RETURN       : SystemFAQDTO object
 ****************************************************************************/

  fetchSystemFaqId(id) {
    this.commonService.callApi('systemfaq/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.faqDetails = success;
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }

  /******************************************************************************/

}