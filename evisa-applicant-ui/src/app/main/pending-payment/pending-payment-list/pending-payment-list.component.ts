import { Component, Injector, OnInit } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';
import { NgPopupsService } from 'ng-popups';
import { NgxUiLoaderService } from "ngx-ui-loader";
import { environment } from 'src/environments/environment';
import { BaseComponent } from '../../../common/commonComponent';
import { HttpHeaders } from '@angular/common/http';
declare var $: any
@Component({
  selector: 'app-pending-payment-list',
  templateUrl: './pending-payment-list.component.html',
  styleUrls: ['./pending-payment-list.component.css']
})


export class PendingPaymentListComponent extends BaseComponent implements OnInit {

  public pagesize = 10;
  public totalItem: any;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }

  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService, private ngPopups: NgPopupsService) {
    super(inj);
  }

  message: any;
  moneyRefund: any;

  ngOnInit(): void {
    this.getPendingPayment(this.data);
    this.getDateToday();

    if (localStorage.getItem('Language') === 'en') {
      this.message = "Are you sure, want to pay ?"
      this.moneyRefund = "Payment of the application fee in no way guarantees that you will receive your visa. These fees, which cover administrative procedures, are non-refundable, even in the event of visa refusal or withdrawal of the application."


    } else {
      this.message = "Êtes-vous sûr de vouloir payer?"
      this.moneyRefund = "Le paiement des frais de dossier ne garantit en aucun cas l'obtention du visa. Ces frais, qui couvrent les démarches administratives, ne sont pas remboursables, même en cas de refus de visa ou de retrait de la demande."
    }
  }
  refreshPage() {
    this.getPendingPayment(this.data);
  }

  /****************************************************************************
      @PURPOSE      : To retrive the pending paymentFAQ List
      @PARAMETERS   : pageNumber,PageSize, loggeduser, status
      @RETURN       : pendingPaymentDTO
   ****************************************************************************/
  public number: 0;
  public size: 0;
  public noofelements: 0;
  public totalElements: 0;
  public loading: boolean = false;
  public datetoday: any;
  pendingPayment: any = []


  getPendingPayment(data) {
    this.loading = true;
    this.ngxLoader.start();
    setTimeout(() => {
      this.data.loggedUser = this.getToken('username'),
        this.data.status = 'PP';
      // this.data.instrType = "EVISA_PAY"
      this.commonService.callApi('payment/search', data, 'post', false, false, 'REG').then(success => {
        let successData: any = success;
        this.totalItem = successData.totalElements;
        this.number = successData.number;
        this.size = successData.size;
        this.loading = false;
        this.ngxLoader.stop();
        this.noofelements = successData.numberOfElements;
        this.totalElements = successData.totalElements;
        ////console.log(successData);
        if (successData.content.length) {
          this.pendingPayment = successData.content
          ////console.log(this.pendingPayment);

        } else {
          this.pendingPayment = [];
        }
      }).catch(e => {
        this.ngxLoader.stop();
        this.toastr.error(e.message, 'Oops!');
      });
    });
  }

  getDateToday() {
    this.loading = true;
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




  /******************************************************************************/

  /****************************************************************************
      @PURPOSE      : Pagination
      @PARAMETERS   : pageNumber,PageSize,loggedinuse
      @RETURN       : NA
   ****************************************************************************/
  public currentPage = 1;
  public showBoundaryLinks = true;
  public rangeList = [5, 10, 25, 100];
  public isFilterApplied: boolean = false;
  public filterObj: any = {};
  pageChanged(e) {

    if (this.isFilterApplied) {
      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      this.getPendingPayment(this.filterObj);
    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getPendingPayment(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      this.filterObj.pageSize = e;
      this.getPendingPayment(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getPendingPayment(this.data);
    }
  }
  /*******************************************************************************/
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
         cancelUrl: 'https://www.evisa.gouv.dj/applicant-api/#/main/dashboard',
          successUrl: 'https://www.evisa.gouv.dj/applicant-api/#/main/success-payment',
//           cancelUrl: 'http://localhost:4200/#/main/dashboard',
//           successUrl: 'http://localhost:4200/#/main/success-payment',
          //  cancelUrl: 'http://217.160.99.180/applicant-api/cancel',
          // successUrl: 'http://217.160.99.180/applicant-api/#/main/success-payment', 
//            cancelUrl: 'https://evisav2.gouv.dj/applicant-api/#/main/dashboard',
//            successUrl: 'https://evisav2.gouv.dj/applicant-api/#/main/success-payment',
          // cancelUrl: 'http://194.164.200.188/applicant-api/#/main/dashboard',
          // successUrl: 'http://194.164.200.188/applicant-api/#/main/success-payment',
          instrType: instrType
        };

        let token = localStorage.getItem("accessToken");
    
        if (!token) {
          return;
        }


        let headers = new HttpHeaders({ 
          'Accept-Language': localStorage.getItem('Language'),
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json' // Ajout du content-type si nécessaire
        });

        // this is a normal http calls for a backend api
        ////console.log('environment.serverUrl',environment.serverUrl);
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
