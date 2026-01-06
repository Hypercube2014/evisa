import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent extends BaseComponent implements OnInit {
  public submitted: boolean = false
  public productConfigDetails: any = {}
  public id: number
  public isNew: boolean = false;
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

  public systemDetails = [
    {
      'code': 'ALL',
      'name': "All"
    },
    {
      'code': 'APL',
      'name': "Applicant Portal"
    },
    {
      'code': 'APR',
      'name': "Approver Portal"
    }
  ]
  constructor(inj: Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      if (params['id'] === 'new') {
        this.isNew = true;
      } else {
        this.id = params['id'];
        this.getProductConfig(this.id)
        this.isNew = false;
      }
    })
  }

  ngOnInit(): void {
  }

  /****************************************************************************
    @PURPOSE      : To Submit and Edit Product Configuration details.
    @PARAMETERS   : form,productConfigDetails
    @RETURN       : API Status
 ****************************************************************************/
  submitForm(form, productConfigDetails) {
    if (this.isNew) {
      if (form.valid) {
        //console.log(productConfigDetails)
        productConfigDetails.createdBy=this.getToken('username')
        this.commonService.callApi('productconfig', productConfigDetails, 'post', false, false, 'APPR').then(success => {
          this.successData = success
          if (this.successData.apiStatusCode === "SUCCESS") {
            this.router.navigate(["main/product-config/"]);
            this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
          } else {
            this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error');
          }
        }).catch(e => {
          this.toastr.errorToastr(e.message, 'Oops!')
        });

      } else {
        this.submitted = true;
      }
    } else {
      this.productConfigDetails.updatedBy = this.getToken('username');
      this.productConfigDetails.configId = this.id;
      this.commonService.callApi('productconfig', productConfigDetails, 'put', false, false, 'APPR').then(success => {
        this.successData = success;
        if (this.successData.apiStatusCode === "SUCCESS") {
          this.router.navigate(["main/product-config/"]);
          this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
        } else {
          this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error');
        }
      }
      ).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }
  }
  /****************************************************************************/



/****************************************************************************
    @PURPOSE      : To get the Product based on Id.
    @PARAMETERS   : configId
    @RETURN       : ProductConfigDTO
 ****************************************************************************/
    

  getProductConfig(id) {
    this.commonService.callApi('productconfig/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.productConfigDetails = success
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
}
