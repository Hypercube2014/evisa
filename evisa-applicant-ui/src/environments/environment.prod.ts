export const environment = {
  production: false,
  /* stripe_key: 'pk_test_51IS2RDErV2ejINDYYLbdrNrP7MtaU3XYSlDl2U5FbZKCnMe3mmjF0jeRIJmW8WDM9my7o5gaXavu3eYelp3VFjzi00nd696K5Q', */
  //stripe_key: 'pk_live_51QclGMDkTELGuZ01fSak3yGnFbkDeRKuq7zolp3OAsWZHmGBpkN4pWCiSGnYyVZCN62PgjRtLspjtIZCML0qdjHF00PPQdIzK',
  // serverUrl: 'http://localhost:8081/applicant-api/v1/api/payment/checkout'
  serverUrl: window.location.href.split('#')[0]+'v1/api/payment/checkout',
//   serverUrl: 'http://localhost:8081/applicant-api/v1/api/payment/checkout' ,//localhost
//   stripe_key: 'pk_test_51MzbEFHhAOKhLI4yrvoddWpsZY8eEIKL28XZ3kXUtggxjdvLnkEW1MnCjUiMzc9l2LTuOZHMpbwbqMad7YjM5abQ00cKAVXu1V',
  stripe_key: 'pk_live_51LcpdGIn53euoVYlqrPY2pkiwWinFKx5oKwrPBT3SUcP0uPge2ND0WGDSG17MD7gqnB2pDLrSlPS1u9ur7T0sabO00n3n2dsdl',
     //urlpayementPenality: 'http://localhost:8081/applicant-api/v1/api/applyoverstay'
};