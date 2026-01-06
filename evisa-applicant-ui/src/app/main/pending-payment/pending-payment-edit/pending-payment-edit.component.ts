import { DatePipe } from '@angular/common';
import { Component, Injector, OnInit } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';
import { NgPopupsService } from "ng-popups";
import { NgxUiLoaderService } from "ngx-ui-loader";
import { environment } from 'src/environments/environment';
import { BaseComponent } from '../../../common/commonComponent';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-pending-payment-edit',
  templateUrl: './pending-payment-edit.component.html',
  styleUrls: ['./pending-payment-edit.component.css']
})
export class PendingPaymentEditComponent extends BaseComponent implements OnInit {

  paymentDetails: any = {}
    datetoday : any;
  // datePipe: any;
  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService, public datepipe: DatePipe,private ngPopups: NgPopupsService) {
    super(inj);

    this.activatedRoute.params.subscribe((params) => {

      this.paymentDetails = params['id'];
      this.fetchPaymentDetails(this.paymentDetails);
    })
  }

    message: any;
    moneyRefund: any;
  ngOnInit(): void {
      this.getDateToday();
      if (localStorage.getItem('Language') === 'en') {
          this.message = "Are you sure, want to pay ?"
          this.moneyRefund = "Payment of the application fee in no way guarantees that you will receive your visa. These fees, which cover administrative procedures, are non-refundable, even in the event of visa refusal or withdrawal of the application."


      } else {
          this.message = "Êtes-vous sûr de vouloir payer?"
          this.moneyRefund = "Le paiement des frais de dossier ne garantit en aucun cas l'obtention du visa. Ces frais, qui couvrent les démarches administratives, ne sont pas remboursables, même en cas de refus de visa ou de retrait de la demande."
      }
  }
  fetchPaymentDetails(id) {
    this.commonService.callApi('payment/' + id, '', 'get', false, false, 'REG').then(success => {
      this.paymentDetails = success
      this.paymentDetails.status = "Pending Payment"
      ////console.log(this.paymentDetails.createdDate);

      this.paymentDetails.createdDate = this.datepipe.transform(this.paymentDetails.createdDate, "dd-MM-yyyy HH:mm:ss")
    }).catch(e => {
      this.toastr.error(e.message, 'Oops!')
    })
  }

    getDateToday() {
        this.ngxLoader.start();
        setTimeout(() => {
            // this.data.instrType = "EVISA_PAY"
            this.commonService.callApi('dateressource', '', 'post', false, false, 'REG').then(success => {
                let successData: any = success;
                let currentDate = new Date(successData);

                // Ajouter une semaine à la date actuelle
                currentDate.setDate(currentDate.getDate() + 7);

                // Mettre à jour successData avec la nouvelle date
                successData = currentDate.toISOString();

                this.datetoday = successData;

                this.ngxLoader.stop();
            }).catch(e => {
                this.ngxLoader.stop();
                this.toastr.error(e.message, 'Oops!');
            });
        });
    }

  /* -------------------------------------------------------------------------------------*/
    stripePromise = loadStripe(environment.stripe_key);
    async checkout(file, amount, instrType) {
        ////console.log(amount)

        // Call your backend to create the Checkout session.
        // When the customer clicks on the button, redirect them to Checkout.
        const stripe = await this.stripePromise;
        this.ngPopups.confirm(this.moneyRefund, { title: this.message }).subscribe(res => {
            if (res) {

                const payment = {
                    amount: amount * 100,
                    referenceNumber: file,
                    // cancelUrl: 'http://87.106.107.227/applicant-api/#/main/dashboard',
                    // successUrl: 'http://87.106.107.227/applicant-api/#/main/success-payment',
                   // cancelUrl: 'https://www.evisa.gouv.dj/applicant-api/#/main/dashboard',
                   // successUrl: 'https://www.evisa.gouv.dj/applicant-api/#/main/success-payment',
//                     cancelUrl: 'http://localhost:4200/#/main/dashboard',
//                     successUrl: 'http://localhost:4200/#/main/success-payment',
                    // cancelUrl: 'http://217.160.99.180/applicant-api/cancel',
                    // successUrl: 'http://217.160.99.180/applicant-api/#/main/success-payment', 
                   cancelUrl: 'https://www.evisa.gouv.dj/applicant-api/#/main/dashboard',
                   successUrl: 'https://www.evisa.gouv.dj/applicant-api/#/main/success-payment',
//                      cancelUrl: 'https://evisav2.gouv.dj/applicant-api/#/main/dashboard',
//                      successUrl: 'https://evisav2.gouv.dj/applicant-api/#/main/success-payment',
                    // cancelUrl: 'http://194.164.200.188/applicant-api/#/main/dashboard',
                    // successUrl: 'http://194.164.200.188/applicant-api/#/main/success-payment',
                    instrType: instrType
                };

                let token = localStorage.getItem("accessToken");
                // //console.log(token,'TOKEN PAYMENT');


                if (!token) {
                  console.error('Access token is missing!');
                  return;
                }

                let headers = new HttpHeaders({ 
                  'Accept-Language': localStorage.getItem('Language'),
                  'Authorization': `Bearer ${token}`,
                  'Content-Type': 'application/json' // Ajout du content-type si nécessaire
                });

                // this is a normal http calls for a backend api
                this.http
                    .post(`${environment.serverUrl}`, payment, { headers })
                    .subscribe((data: any) => {
                        // I use stripe to redirect To Checkout page of Stripe platform
                        // stripe.redirectToCheckout({
                        //   sessionId: data.refId,
                        // });
                        const result = stripe.redirectToCheckout({
                            sessionId: data.refId,
                        });
                        ////console.log(result);
                    });

            }
        });
    }
}
