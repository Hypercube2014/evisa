import { Component, Injector, OnInit } from '@angular/core';
import { BsDatepickerConfig } from "ngx-bootstrap/datepicker";
import { BaseComponent } from "../../../common/commonComponent";
import { ActivatedRoute } from "@angular/router";
import { NgPopupsService } from "ng-popups";
import { NgxUiLoaderService } from "ngx-ui-loader";
import * as moment from 'moment';

@Component({
    selector: 'app-overstay-details',
    templateUrl: './overstay-details.component.html',
    styleUrls: ['./overstay-details.component.css']
})
export class OverstayDetailsComponent extends BaseComponent implements OnInit {

    depConfig: Partial<BsDatepickerConfig>;
    colorTheme = 'theme-blue';

    public data: any = {
        applicationNumber: 1,
        loggedUser: this.getToken('username'),
        departedestimate: '',
        visa_expiry: '',
        penalityAmount: '',
        fileNumber: ''
    }

    isValid: boolean = false;
    isissueValid: boolean = false;
    diplayPenalitedEstimate: number;

    applicationNumber: string;
    penalityamount: any;
    public overstayDto: any;
    new_date: any;

    constructor(inj: Injector, private ngxLoader: NgxUiLoaderService, private ngPopups: NgPopupsService, private activedRoute: ActivatedRoute) {
        super(inj);
        this.new_date = moment(new Date(), "DD-MM-YYYY").add(1, 'days').toDate();
        this.applicationNumber = this.activedRoute.snapshot.params['applicationNumber'];
        this.penalityamount = this.activatedRoute.snapshot.params['amount'];
        this.depConfig = Object.assign({}, {
            containerClass: this.colorTheme,
            customTodayClass: 'custom-today-class',
            showWeekNumbers: false,
            dateInputFormat: 'DD-MM-YYYY',
            minDate: this.new_date
        });
    }
    message: any;
    moneyRefund: any;
    ngOnInit(): void {
        this.diplayPenalitedEstimate = 0;
        this.getVisaExtension();

        if (localStorage.getItem('Language') === 'en') {
            this.message = "Are you sure, want to pay ?"
            this.moneyRefund = "Payment of the application fee in no way guarantees that you will receive your visa. These fees, which cover administrative procedures, are non-refundable, even in the event of visa refusal or withdrawal of the application."


        } else {
            this.message = "Êtes-vous sûr de vouloir payer?"
            this.moneyRefund = "Le paiement des frais de dossier ne garantit en aucun cas l'obtention du visa. Ces frais, qui couvrent les démarches administratives, ne sont pas remboursables, même en cas de refus de visa ou de retrait de la demande."
        }
    }

    getVisaExtension() {
        this.data.applicationNumber = this.applicationNumber;
        setTimeout(() => {
            this.commonService.callApi('searchavailableoverstayview', this.data, 'post', false, false, 'APPR').then(success => {
                let successData: any = success;
                successData.penalityAmount = this.penalityamount;
                this.overstayDto = successData;
                ////console.log(this.overstayDto);
            }).catch(e => {

            });
        });
    }

    public today = new Date();
    error: string;

    getEstimatePenality() {
        this.data.penalityAmount = this.penalityamount;
        ////console.log(this.data.departedestimate, 'this.data.departedestimate');
        if (this.data.departedestimate > 0) {
            this.error = '';
            if (this.data.departedestimate > this.today) {
                this.error = '';
                setTimeout(() => {
                    this.commonService.callApi('estimatePenality', this.data, 'post', false, false, 'APPR').then(success => {
                        let successData: any = success;
                        this.diplayPenalitedEstimate = successData.penalityAmount;
                        ////console.log(this.diplayPenalitedEstimate, 'this.diplayPenalitedEstimate');
                    }).catch(e => {

                    });
                });
            } else {
                this.error = 'Vous ne pouvez pas sélectionner une date antérieure ou egale à la date du jour.';
            }
        } else {
            this.error = 'veuillez saisir une date';

        }

    }

    getCancel() {
        this.diplayPenalitedEstimate = 0;
        this.data.departedestimate = '';
    }


    checkout(file, amount) {
        ////console.log(file);
        ////console.log(amount);
        this.data.applicationNumber = file;
        this.data.penalityAmount = amount;
        this.data.loggedUser = this.getToken('username');
        this.data.fileNumber = this.overstayDto.fileNumber;
        this.ngPopups.confirm(this.moneyRefund, { title: this.message }).subscribe(res => {
            if (res) {
                setTimeout(() => {
                    this.commonService.callApi('applypayementpenality', this.data, 'post', false, false, 'APPR').then(success => {
                        let successData: any = success;

                        ////console.log(successData);
                        this.toastr.successToastr("success", successData.apiStatusDesc)
                        this.router.navigate(["main/overstayList"]);
                    }).catch(e => {

                    });
                });
            }
        });
    }
}
