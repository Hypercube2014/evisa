import { Component, Injector, OnInit } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';
import { NgPopupsService } from 'ng-popups';
import { NgxUiLoaderService } from "ngx-ui-loader";
import { environment } from 'src/environments/environment';
import { BaseComponent } from '../../common/commonComponent';




@Component({
  selector: 'app-visa-overstay',
  templateUrl: './visa-overstay.component.html',
  styleUrls: ['./visa-overstay.component.css']
})
export class VisaOverstayComponent  extends BaseComponent implements OnInit {

    public pagesize = 10;
    public totalItem: any;
    public visaCheckOverstayDetails: any;
    public data: any = {
        pageNumber: 1,
        pageSize: this.pagesize,
        loggedUser: this.getToken('username')
    }
    public loading: boolean;
    public number: 0;
    public size: 0;
    public noofelements: 0;
    public totalElements: 0;
    public dataa: any = {
        ReferenceNumber: 1,
        Amount: this.pagesize,
    }


    constructor(inj: Injector, private ngxLoader: NgxUiLoaderService, private ngPopups: NgPopupsService) {
        super(inj);
    }

    message: any;
    moneyRefund: any;

    ngOnInit(): void {
        this.getVisaExtension();


    }

    getVisaExtension() {
        // //console.log(this.data);
        setTimeout(() => {
            this.commonService.callApi('searchavailableoverstay', this.data, 'post', false, false, 'REG').then(success => {
                let successData: any = success;
                this.totalItem = successData.totalElements;
                this.number = successData.number;
                this.size = successData.size;
                this.noofelements = successData.numberOfElements;
                this.totalElements = successData.totalElements;

                // //console.log(successData);
                if (successData.content.length) {
                    this.visaCheckOverstayDetails = successData.content;
                }
            }).catch(e => {
                this.toastr.error(e.message, 'Oops!');
            });
        });
    }

    stripePromise = loadStripe(environment.stripe_key);

    async checkout(file, amount) {
        // //console.log(file);
        // //console.log(amount);
        this.dataa.applicationNumber = file;
        this.dataa.penalityAmount = amount;
        const stripe = await this.stripePromise;
        this.ngPopups.confirm(this.moneyRefund, {title: this.message}).subscribe(res => {
            if (res) {

                let tmpamt: any = this.dataa.penalityAmount;
                const payment = {
                    amount: tmpamt * 100,
                    referenceNumber: this.dataa.applicationNumber,
                    username:'Hamda',
                    // cancelUrl: 'http://87.106.107.227/applicant-api/#/main/dashboard',
                    // successUrl: 'http://87.106.107.227/applicant-api/#/main/success-payment',
                    // cancelUrl: 'https://evisav2.gouv.dj/applicant-api/#/main/dashboard',
                    // successUrl: 'https://evisav2.gouv.dj/applicant-api/#/main/success-payment',
//                     cancelUrl: 'http://localhost:4200/#/main/dashboard',
//                     successUrl: 'http://localhost:4200/#/main/success-payment',
                    // cancelUrl: 'http://217.160.99.180/applicant-api/cancel',
                    // successUrl: 'http://217.160.99.180/applicant-api/#/main/success-payment', 
                    cancelUrl: 'https://www.evisa.gouv.dj/applicant-api/cancel',
                    successUrl: 'https://www.evisa.gouv.dj/applicant-api/#/main/success-payment',
                    instrType: 'EVISA_PEN'
                };

                // this is a normal http calls for a backend api
                // //console.log('environment.serverUrl',environment.serverUrl);
                this.http
                    .post(`${environment.serverUrl}`, payment)
                    .subscribe((data: any) => {
                        // I use stripe to redirect To Checkout page of Stripe platform
                        // stripe.redirectToCheckout({
                        //   sessionId: data.refId,
                        // });
                        const result = stripe.redirectToCheckout({
                            sessionId: data.refId,
                        });
                        // //console.log(result);
                    });
            }
        });

        // //console.log("dehors");
    }

}