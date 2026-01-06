import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent';
import { NgxUiLoaderService } from "ngx-ui-loader";
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-payment-completed-edit',
  templateUrl: './payment-completed-edit.component.html',
  styleUrls: ['./payment-completed-edit.component.css']
})
export class PaymentCompletedEditComponent extends BaseComponent implements OnInit {

  paymentDetails: any = {}
  // datePipe: any;
  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService, public datepipe: DatePipe) {
    super(inj);

    this.activatedRoute.params.subscribe((params) => {

      this.paymentDetails = params['id'];
      this.fetchPaymentDetails(this.paymentDetails);
    })
  }

  ngOnInit(): void {
  }
  fetchPaymentDetails(id) {
    this.commonService.callApi('payment/' + id, '', 'get', false, false, 'REG').then(success => {
      this.paymentDetails = success
      if (this.paymentDetails.status === 'PC') {
        this.paymentDetails.status = "Payment Completed"
      }
      this.paymentDetails.createdDate = this.datepipe.transform(this.paymentDetails.createdDate, "dd-MM-yyyy HH:mm:ss")
      this.paymentDetails.updatedDate = this.datepipe.transform(this.paymentDetails.updatedDate, "dd-MM-yyyy HH:mm:ss")
    })
  }
}
