import { Component, Injector, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { loadStripe } from '@stripe/stripe-js';
import * as moment from 'moment';
import { NgPopupsService } from 'ng-popups';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { NgxUiLoaderService } from "ngx-ui-loader";
import { environment } from 'src/environments/environment';
import { BaseComponent } from "../common/commonComponent";



@Component({
    selector: 'app-visaoverstay-view',
    templateUrl: './visaoverstay-view.component.html',
    styleUrls: ['./visaoverstay-view.component.css']
})
export class VisaoverstayViewComponent extends BaseComponent implements OnInit {
    depConfig: Partial<BsDatepickerConfig>;
    colorTheme = 'theme-blue';
    public data: any = {
        applicationNumber: 1,
        loggedUser: this.getToken('username'),
        departedestimate: '',
        visa_expiry: '',
        penalityAmount: ''
    }

    isValid: boolean = false;
    isissueValid: boolean = false;
    diplayPenalitedEstimate: number;

    applicationNumber: string;
    penalityamount: any;
    public overstayDto: any;
    error: any;
    constructor(inj: Injector, private ngxLoader: NgxUiLoaderService, private ngPopups: NgPopupsService, private activedRoute: ActivatedRoute) {
        super(inj);
        this.new_date = moment(new Date(), "DD-MM-YYYY").add(1, 'days').toDate();
        this.applicationNumber = this.activedRoute.snapshot.params['applicationNumber'];
        this.penalityamount = this.activatedRoute.snapshot.params['amount'];
        this.depConfig = Object.assign({}, { containerClass: this.colorTheme, customTodayClass: 'custom-today-class', showWeekNumbers: false, dateInputFormat: 'DD-MM-YYYY', minDate: this.new_date });
    }
    message: any;
    moneyRefund: any;
    ngOnInit(): void {
        this.getVisaExtension();

        this.diplayPenalitedEstimate = 0;
        if (localStorage.getItem('Language') === 'en') {
            this.message = "Are you sure, want to pay ?"
            this.moneyRefund = "Payment of the application fee in no way guarantees that you will receive your visa. These fees, which cover administrative procedures, are non-refundable, even in the event of visa refusal or withdrawal of the application."


        } else {
            this.message = "Êtes-vous sûr de vouloir payer?"
            this.moneyRefund = "Le paiement des frais de dossier ne garantit en aucun cas l'obtention du visa. Ces frais, qui couvrent les démarches administratives, ne sont pas remboursables, même en cas de refus de visa ou de retrait de la demande."
        }
    }

    public today = new Date();
    new_date: any;
    datePickerConfig = {
        dateInputFormat: 'DD-MM-YYYY',
        containerClass: this.colorTheme,
        maxDate: new Date(),
        showWeekNumbers: false
    }

    getVisaExtension() {
        this.data.applicationNumber = this.applicationNumber;
        setTimeout(() => {
            this.commonService.callApi('searchavailableoverstayview', this.data, 'post', false, false, 'REG').then(success => {
                let successData: any = success;
                successData.penalityAmount = this.penalityamount;
                this.overstayDto = successData;
                ////console.log(this.overstayDto);
            }).catch(e => {
                this.toastr.error(e.message, 'Oops!');
            });
        });
    }

    getEstimatePenality() {
        this.data.penalityAmount = this.penalityamount;
        if (this.data.departedestimate > 0) {
            this.error = '';
            if (this.data.departedestimate > this.today) {
                this.error = '';
                setTimeout(() => {
                    this.commonService.callApi('estimatePenality', this.data, 'post', false, false, 'REG').then(success => {
                        let successData: any = success;
                        this.diplayPenalitedEstimate = successData.penalityAmount;
                        ////console.log(this.diplayPenalitedEstimate);
                    }).catch(e => {
                        this.toastr.error(e.message, 'Oops!');
                    });
                });
            } else {
                this.error = 'Vous ne pouvez pas sélectionner une date antérieure ou egale à la date du jour.';
            }
        } else {
            this.error = 'veuillez saisir une date';

        }

    }

    stripePromise = loadStripe(environment.stripe_key);
    async checkout(file, amount) {
        ////console.log(file);
        ////console.log(amount);
        this.data.applicationNumber = file;
        this.data.penalityAmount = amount;
        const stripe = await this.stripePromise;
        this.ngPopups.confirm(this.moneyRefund, { title: this.message }).subscribe(res => {
            if (res) {

                const payment = {
                    amount: this.data.penalityAmount,
                    referenceNumber: this.data.applicationNumber,
                    username: 'Hamda',
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
                ////console.log('environment.serverUrl',environment.serverUrl);
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
                        ////console.log(result);
                    });
            }
        });

        ////console.log("dehors");
    }

    getCancel() {
        this.diplayPenalitedEstimate = 0;
        this.data.departedestimate = '';
    }
}
