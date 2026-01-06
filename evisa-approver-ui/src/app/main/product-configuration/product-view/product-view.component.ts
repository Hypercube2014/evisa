import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-product-view',
  templateUrl: './product-view.component.html',
  styleUrls: ['./product-view.component.css']
})
export class ProductViewComponent extends BaseComponent implements OnInit {

  productConfigDetails: any = []
  public configId: any
  constructor(inj: Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      this.configId = params['id'];
      this.getProductConfig(this.configId);
    })
  }


  ngOnInit(): void {
  }

  getProductConfig(id) {
    this.commonService.callApi('productconfig/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.productConfigDetails = success
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
}
