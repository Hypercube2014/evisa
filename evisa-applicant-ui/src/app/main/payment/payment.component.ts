import { Component, Injector, OnInit } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';
import { environment } from 'src/environments/environment';
import { BaseComponent } from '../../common/commonComponent';
import { HttpHeaders } from '@angular/common/http';


declare var $: any;
@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent extends BaseComponent implements OnInit {

  constructor(inj: Injector) {
    super(inj);

  }

  stripePromise = loadStripe(environment.stripe_key);
  async checkout(e) {

    // Call your backend to create the Checkout session.
    // When the customer clicks on the button, redirect them to Checkout.
    const stripe = await this.stripePromise;
    let tmpamt: any = this.getToken('charge').split(" ")[0]
    // //console.log(this.getToken("visaType"))
    // //console.log(this.getToken("expressVisa"))
    // const payment = {
    //   amount: tmpamt * 100,
    //   referenceNumber: this.getToken('fileNumber'),
    //   // cancelUrl: 'http://87.106.107.227/applicant-api/#/main/dashboard',
    //   // successUrl: 'http://87.106.107.227/applicant-api/#/main/success-payment',
    //   // cancelUrl: 'https://evisav2.gouv.dj/applicant-api/#/main/dashboard',
    //   // successUrl: 'https://evisav2.gouv.dj/applicant-api/#/main/success-payment',
    //   // cancelUrl: 'http://localhost:4200/#/main/dashboard',
    //   // successUrl: 'http://localhost:4200/#/main/success-payment',
    //   // cancelUrl: 'http://217.160.99.180/applicant-api/cancel',
    //   // successUrl: 'http://217.160.99.180/applicant-api/#/main/success-payment',
    //   cancelUrl: 'https://www.evisa.gouv.dj/applicant-api/#/main/dashboard',
    //   successUrl: 'https://www.evisa.gouv.dj/applicant-api/#/main/success-payment',
    //   instrType: 'EVISA_PAY'
    // };
    // let token = localStorage.getItem("accessToken");
    // let headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'Authorization': `Bearer ${token}` });
    // // this is a normal http calls for a backend api
    // this.http
    //   .post(`${environment.serverUrl}`, payment, { headers })
    //   .subscribe((data: any) => {
    //     // I use stripe to redirect To Checkout page of Stripe platform
    //     // stripe.redirectToCheckout({
    //     //   sessionId: data.refId,
    //     // }); 
    //     const result = stripe.redirectToCheckout({
    //       sessionId: data.refId,
    //     });
    //     ////console.log(result);
    //   });


    const payment = {
      amount: tmpamt * 100, // montant en centimes
      referenceNumber: this.getToken('fileNumber'), // ou un autre moyen d'obtenir la référence
//       cancelUrl: 'https://evisav2.gouv.dj/applicant-api/#/main/dashboard',
//       successUrl: 'https://evisav2.gouv.dj/applicant-api/#/main/success-payment',
//       cancelUrl: 'http://localhost:4200/#/main/dashboard',
//       successUrl: 'http://localhost:4200/#/main/success-payment',
      cancelUrl: 'https://www.evisa.gouv.dj/applicant-api/#/main/dashboard',
      successUrl: 'https://www.evisa.gouv.dj/applicant-api/#/main/success-payment',
      instrType: 'EVISA_PAY'
    };

    // Récupérer le token d'authentification dans le localStorage
    let token = localStorage.getItem("accessToken");
    // //console.log(token, 'TOKEN PAYMENT')

    // Vérifier si le token existe
    if (!token) {
      console.error('Access token is missing!');
      return; // Arrêter l'exécution si le token est absent
    }

    // Créer les en-têtes HTTP avec le token
    let headers = new HttpHeaders({
      'Accept-Language': localStorage.getItem('Language'),
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json' // Ajout du content-type si nécessaire
    });

    // Effectuer la requête HTTP POST avec les données et les en-têtes
    this.http.post(`${environment.serverUrl}`, payment, { headers })
      .subscribe(
        (data: any) => {
          // Si la requête réussit, rediriger vers la page de checkout Stripe
          if (data && data.refId) {
            const result = stripe.redirectToCheckout({
              sessionId: data.refId, // Utiliser la référence de session retournée par l'API
            });

            // Gérer le résultat de Stripe
            result.then((result) => {
              if (result.error) {
                console.error('Stripe redirection error:', result.error.message);
              }
            });
          } else {
            console.error('Invalid response from API. Missing refId.');
          }
        },
        (error) => {
          // Gérer les erreurs de la requête HTTP
          console.error('Error from API:', error);
        }
      );


  }


  ngOnInit(): void {
    //this.invokeStripe();
    if (this.getToken("visaType") == "CS30") {
      if (this.getToken("expressVisa")) {
        this.setToken("expressTva", (173 * 0.10).toFixed(1));
      } else {
        this.setToken("tva", (23 * 0.10).toFixed(1))
      }
    } else {
      if (this.getToken("expressVisa")) {
        this.setToken("expressTva", (164 * 0.10).toFixed(1));
      } else {
        this.setToken("tva", (14 * 0.10).toFixed(1))
      }
    }
  }


  invokeStripe() {
    if (!window.document.getElementById('checkout-button')) {
      const script = window.document.createElement('script');
      script.id = 'checkout-button';
      script.type = 'text/javascript';
      script.src = 'https://js.stripe.com/v3/';
      window.document.body.appendChild(script);
    }
  }

  public tokenData: any;
  makePayment(amount: any) {
    // const paymentHandler = (<any>window).StripeCheckout.configure({
    //   key:
    //     'pk_test_51IS2RDErV2ejINDYYLbdrNrP7MtaU3XYSlDl2U5FbZKCnMe3mmjF0jeRIJmW8WDM9my7o5gaXavu3eYelp3VFjzi00nd696K5Q',

    //   locale: 'auto',
    //   token: function (stripeToken: any) {
    //     ////console.log(stripeToken.card);
    //     ////console.log(stripeToken)
    //     this.tokenData = stripeToken.card
    //     const url = 'http://localhost:8081/applicant-api/v1/api/payment/charge';
    //      let amt:any=sessionStorage.getItem("charge").split(" ")[0]
    //      let senddata = {

    //         "amount":amt*100,
    //         "currency": "USD",
    //         "referenceNumber":sessionStorage.getItem("fileNumber"),
    //         "recieptMail":stripeToken.card.name,
    //         "stripeEmail":stripeToken.card.name,
    //         "stripeToken":stripeToken.id

    //     }

    //     var request = new Request(url,{
    //       method: 'POST',
    //       body: JSON.stringify(senddata),
    //       headers: new Headers({'content-type': 'application/json'}),
    //     });

    //     fetch(request)
    //       .then(function (res) {
    //         // Handle response we get from the AP
    //         ////console.log(res);
    //       })
    //   },
    // });

    // paymentHandler.open({
    //   name: 'Evisa Payment',
    //   description: '',
    //   amount: amount * 100,
    // });
    // ////console.log(this.tokenData)
  }







}
