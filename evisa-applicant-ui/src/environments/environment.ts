// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

// export const environment = {
//   production: false,
//   stripe: 'pk_test_51IS2RDErV2ejINDYYLbdrNrP7MtaU3XYSlDl2U5FbZKCnMe3mmjF0jeRIJmW8WDM9my7o5gaXavu3eYelp3VFjzi00nd696K5Q',
//   serverUrl: 'http://localhost:8081/applicant-api/v1/api/payment/charge',
// };

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.

export const environment = {
  production: true,
  /* stripe_key: 'pk_test_51IS2RDErV2ejINDYYLbdrNrP7MtaU3XYSlDl2U5FbZKCnMe3mmjF0jeRIJmW8WDM9my7o5gaXavu3eYelp3VFjzi00nd696K5Q', */
  
//   stripe_key: 'pk_test_51PKbmaCk3phQxdKyfmbV0UYyYoXWTOkRPH5I25W4paQsxvNscwuZeJkgCHSarru1pS2bNjUbzUU9uS4dIxqldDSD00Dvq69s1j',
  
//   stripe_key: 'pk_test_51MzbEFHhAOKhLI4yrvoddWpsZY8eEIKL28XZ3kXUtggxjdvLnkEW1MnCjUiMzc9l2LTuOZHMpbwbqMad7YjM5abQ00cKAVXu1V',

  //Strip EvisaDGPN Prod Mode
  // stripe_key: 'pk_live_51PKbmaCk3phQxdKylkJZlUl13H6rTr9aEIWmmF1frQn2lcKBzp3fsar0JCZMs7UWZ58wWN6NExtumqAhzgSObG6H00gTCtdXVI',
  
  
  //Strip Evisa version 2 PROD
  //stripe_key:fetchStripeKey(),


  serverUrl: 'http://localhost:8081/applicant-api/v1/api/payment/checkout' //localhost
//   serverUrl: window.location.href.split('#')[0]+'v1/api/payment/checkout' // server
   // urlpayementPenality: 'http://localhost:8081/applicant-api/v1/api/applyoverstay'
};

// function getBaseUrl(): string {slclearc
//   const { protocol, hostname, port } = window.location;
//   return `${protocol}//${hostname}${port ? `:${port}` : ''}/`;
// }
//
// function fetchStripeKey(): string {
//   const stripeKeyEndpoint = `${getBaseUrl()}applicant-api/v1/api/vf/check`;
//   let stripeKey = 'pk_live_51LcpdGIn53euoVYlqrPY2pkiwWinFKx5oKwrPBT3SUcP0uPge2ND0WGDSG17MD7gqnB2pDLrSlPS1u9ur7T0sabO00n3n2dsdl'; // Valeur par défaut (si la requête échoue)
//
//   try {
//     const xhr = new XMLHttpRequest();
//     xhr.send(null);
//
//     if (xhr.status === 200) {
//       const response = JSON.parse(xhr.responseText);
//       stripeKey = response.stripe_key || stripeKey;
//     }
//   } catch (error) {
//     console.error('Erreur lors de la récupération de la clé Stripe :', error);
//   }
//
//   return stripeKey;
// }
