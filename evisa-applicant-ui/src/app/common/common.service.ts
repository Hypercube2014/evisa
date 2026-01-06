import { isPlatformBrowser } from "@angular/common";
import { Inject, Injectable, PLATFORM_ID } from "@angular/core";
// import { BaseComponent } from './../common/commonComponent';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { EnvService } from "../common/env.service";
// import swal from 'sweetalert';
// import {NgxCoolDialogsService} from 'ngx-cool-dialogs'

@Injectable({
  providedIn: "root",
})
export class CommonService {
  authorised: any = false;
  constructor(
    public _http: HttpClient,
    @Inject(PLATFORM_ID) platformId: Object,
    private envservice: EnvService
  ) {
    // super(inj);
    this.platformId = platformId;
    //this._apiUrl = this.envservice.apiUrl;
    this._apiUrl = window.location.href.split("#")[0];
//   this._apiUrl = 'http://localhost:8081/applicant-api/';
    //this._apiUrl = 'https://evisav2.gouv.dj/applicant-api/';
    // this._apiUrl = 'https://www.evisa.gouv.dj/applicant-api/';
    //console.log(this._apiUrl,"coucoooooooooooooooooooooo");
  }

  public _apiUrl;
  public platformId;

  public getToken(key) {
    if (isPlatformBrowser(this.platformId)) {
      return window.localStorage.getItem(key);
    }
  }

  public getTokenVa(key) {
    if (isPlatformBrowser(this.platformId)) {
      return window.sessionStorage.getItem(key);
    }
  }
  public setToken(key, value) {
    if (isPlatformBrowser(this.platformId)) {
      // //console.log("Stockage du token:", value);
      window.localStorage.setItem(key, value);
    }
  }

  /*******************************************************************************************
	@PURPOSE      	: 	Call api.
	@Parameters 	: 	{
							url : <url of api>
							data : <data object (JSON)>
							method : String (get, post)
							isForm (Optional) : Boolean - to call api with form data
							isPublic (Optional) : Boolean - to call api without auth header
						}
	/*****************************************************************************************/

  // callApi(url, data, method, isForm?, isPublic?, islogin?, isDateTime?) {
  // 	let tokenValue = this.getToken('accessToken');
  // 	let token = tokenValue ? 'Bearer ' + tokenValue : '';
  // 	let loggedUser = this.getToken('username');

  // 	//console.log("Token utilisé dans callApi:", token);
  // 	//console.log("Utilisateur connecté:", loggedUser);
  // 	//console.log("URL appelée:", url);

  // 	let headers = new HttpHeaders({
  // 		'Accept-Language': localStorage.getItem('Language'),
  // 		'Authorization': token,
  // 		'username': loggedUser || ''
  // 	});

  // 	if (isPublic) {
  // 		headers = new HttpHeaders({
  // 			'Accept-Language': localStorage.getItem('Language'),
  // 			'content-Type': 'application/json',
  // 			'Authorization': token,
  // 			'username': loggedUser || ''
  // 		});
  // 	}

  // 	if (isForm) {
  // 		headers = new HttpHeaders({
  // 			'Accept-Language': localStorage.getItem('Language'),
  // 			'Authorization': token,
  // 			'username': loggedUser || ''
  // 		});
  // 	}

  // 	let apiUrl = this._apiUrl + 'v1/api/' + url;

  // 	if (islogin === 'REG') {
  // 		if (method === 'post') {
  // 			headers = new HttpHeaders({
  // 				'Accept-Language': localStorage.getItem('Language'),
  // 				'Authorization': '',
  // 				'username': loggedUser || ''
  // 			});

  // 			//console.log(apiUrl);
  // 			return this._http.post(this._apiUrl + 'v1/api/' + url, data, { headers })
  // 				.toPromise()
  // 				.then(res => res);
  // 		} else if (method === 'get') {

  // 			return this._http.get(this._apiUrl + 'v1/api/' + url, { headers })
  // 				.toPromise()
  // 				.then(res => res);
  // 		} else if (method === 'put') {
  // 			return this._http.put(this._apiUrl + 'v1/api/' + url, data, { headers })
  // 				.toPromise()
  // 				.then(res => res);
  // 		} else {
  // 			return this._http.delete(this._apiUrl + 'v1/api/' + url, { headers })
  // 				.toPromise()
  // 				.then(res => res);
  // 		}
  // 	} else if (islogin === 'LOG') {
  // 		headers = new HttpHeaders({
  // 			'Accept-Language': localStorage.getItem('Language'),
  // 			'Authorization': '',
  // 			'username': loggedUser || ''
  // 		});
  // 		//console.log(apiUrl);
  // 		return this._http.post(this._apiUrl + url, data, { headers })
  // 			.toPromise()
  // 			.then(res => res);
  // 	} else if (isDateTime === 'DateTime') {
  // 		const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
  // 		return this._http.get(`https://timeapi.io/api/time/current/zone?timeZone=${timeZone}`)
  // 			.toPromise()
  // 			.then(res => res);
  // 	}
  // }

  callApi(url, data, method, isForm?, isPublic?, islogin?, isDateTime?) {
    let token = this.getTokenVa("accessToken");
    // //console.log("Token used in callApi:", token);

    let loggedUser = this.getTokenVa("username");
    // //console.log("Logged user:", loggedUser);

    // //console.log("URL being called:", url);
    // //console.log("HTTP Method:", method);
    // //console.log("Request Data:", data);

    let headers;
    if (isPublic) {
      headers = new HttpHeaders({
        "Accept-Language": localStorage.getItem("Language"),
        "content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      });
    } else {
      headers = new HttpHeaders({
        "Accept-Language": localStorage.getItem("Language"),
        Authorization: `Bearer ${token}`,
      });
    }

    if (isForm) {
      headers = new HttpHeaders({
        "Accept-Language": localStorage.getItem("Language"),
        Authorization: `Bearer ${token}`,
      });
    }

    // //console.log("Request Headers:", headers);

    if (islogin === "REG") {
      if (method === "post") {
        headers = new HttpHeaders({
          "Accept-Language": localStorage.getItem("Language"),
          Authorization: `Bearer ${token}`,
        });
        // //console.log(url);
        return this._http
          .post(this._apiUrl + "v1/api/" + url, data, { headers })
          .toPromise()
          .then((res) => {
            // //console.log("Response from API:", res);
            return res;
          })
          .catch((error) => {
            // console.error("Error from API:", error);
            throw error;
          });
      } else if (method == "get") {
        // headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'Authorization': `Bearer ${token}` });
        headers = token
          ? new HttpHeaders({
              "Accept-Language": localStorage.getItem("Language"),
              Authorization: `Bearer ${token}`,
            })
          : new HttpHeaders({
              "Accept-Language": localStorage.getItem("Language"),
              Authorization: `Bearer `,
            });

        return this._http
          .get(this._apiUrl + "v1/api/" + url, { headers })
          .toPromise()
          .then((res) => {
            // //console.log("Response from API:", res);
            return res;
          })
          .catch((error) => {
            // console.error("Error from API:", error);
            throw error;
          });
      } else if (method == "put") {
        headers = new HttpHeaders({
          "Accept-Language": localStorage.getItem("Language"),
          Authorization: `Bearer ${token}`,
        });

        return this._http
          .put(this._apiUrl + "v1/api/" + url, data, { headers })
          .toPromise()
          .then((res) => {
            // //console.log("Response from API:", res);
            return res;
          })
          .catch((error) => {
            // console.error("Error from API:", error);
            throw error;
          });
      } else {
        headers = new HttpHeaders({
          "Accept-Language": localStorage.getItem("Language"),
          Authorization: `Bearer ${token}`,
        });

        return this._http
          .delete(this._apiUrl + "v1/api/" + url, { headers })
          .toPromise()
          .then((res) => {
            // //console.log("Response from API:", res);
            return res;
          })
          .catch((error) => {
            // console.error("Error from API:", error);
            throw error;
          });
      }
    } else if (islogin === "LOG") {
      const headers = new HttpHeaders({
        "Accept-Language": localStorage.getItem("Language"),
        Authorization: `Bearer ` + "",
      });
      // //console.log("from loging", url);
      return this._http
        .post(this._apiUrl + url, data, { headers })
        .toPromise()
        .then((res) => {
          //console.log("Response from API:", res, headers);
          return res;
        })
        .catch((error) => {
          //console.error("Error from API:", error, headers);
          throw error;
        });
    } else if (isDateTime === "DateTime") {
      //const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;

      return this._http
        //.get("https://timeapi.io/api/time/current/zone?timeZone=" + timeZone)
        .get("https://www.evisa.gouv.dj/api/timezone")
        .toPromise()
        .then((res) => {
          // //console.log("Response from API (DateTime):", res);
          return res;
        })
        .catch((error) => {
          // console.error("Error from API (DateTime):", error);
          throw error;
        });
    }
  }

  // callApi(url, data, method, isForm?, isPublic?, islogin?, isDateTime?) {

  // 	let token = this.getTokenVa('accessToken');
  // 	// let token = tokenValue ? 'Bearer ' + tokenValue : '';

  // 	// let token = 'Bearer ' + localStorage.getItem('accessToken');

  // 	// let token = this.getToken('accessToken');
  // 	// let token = tokenValue ? 'Bearer ' + tokenValue : '';
  // 	//console.log("Token utilisé dans callApi:", token);

  // 	let loggedUser = this.getTokenVa('username');

  // 	// //console.log(loggedUser);

  // 	// //console.log(localStorage.getItem('accessToken'), "cccccccccccccccccccccccccccccc");
  // 	//console.log("url", url);
  // 	//console.log(method);
  // 	// //console.log("token", token);

  // 	let headers;
  // 	if (isPublic) {

  // 		headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'content-Type': 'application/json', 'Authorization': token });
  // 	} else {
  // 		headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'Authorization': token });
  // 		////console.log("haiii");
  // 		////console.log(headers)
  // 	}

  // 	if (isForm) {
  // 		headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'Authorization': token });
  // 	}
  // 	//console.log("trying one on manually");
  // 	this._http.post(this._apiUrl + 'v1/api/searchapplicationfile', {
  // 		"docstatus": "CLS",
  // 		"isExpressVisa": "false",
  // 		"loggedUser": "momo",
  // 		"pageNumber": 1,
  // 		"pageSize": 10,
  // 		"visaType": "CS30"
  // 	  }, headers)
  // 	  .toPromise()
  // 	  .then(re => {
  // 		return re;
  // 	});
  // 	if (islogin === 'REG') {

  // 		if (method === 'post') {
  // 			headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'Authorization': token });

  // 			return this._http.post(this._apiUrl + 'v1/api/' + url, data, { headers })
  // 				.toPromise()
  // 				.then(res => {

  // 					return res;
  // 				});

  // 		} else if (method == 'get') {
  // 			headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'Authorization': '' });
  // 			return this._http.get(this._apiUrl + 'v1/api/' + url, { headers })
  // 				.toPromise()
  // 				.then(res => {

  // 					return res;

  // 				});
  // 		} else if (method == 'put') {
  // 			headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'Authorization': token });
  // 			return this._http.put(this._apiUrl + 'v1/api/' + url, data, { headers })

  // 				.toPromise()
  // 				.then(res => {

  // 					return res;
  // 				});

  // 		} else {
  // 			headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'Authorization': token });
  // 			return this._http.delete(this._apiUrl + 'v1/api/' + url, data)

  // 				.toPromise()
  // 				.then(res => {

  // 					return res;

  // 				});
  // 		}

  // 	} else if (islogin === 'LOG') {

  // 		headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'Authorization': '' });
  // 		return this._http.post(this._apiUrl + url, data, { headers })

  // 			.toPromise()
  // 			.then(res => {

  // 				return res;

  // 			});

  // 	} else if (isDateTime === 'DateTime') {
  // 		const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;

  // 		return this._http.get("https://timeapi.io/api/time/current/zone?timeZone=" + timeZone)

  // 			.toPromise()

  // 			.then(res => {

  // 				return res;

  // 			});

  // 	}

  // }

  downloadAttachment(url, data) {
    let token = this.getTokenVa("accessToken");
    let headers = new HttpHeaders({
      "Accept-Language": localStorage.getItem("Language"),
      "content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    });
    return this._http.post(this._apiUrl + "v1/api/" + url, data, {
      responseType: "arraybuffer" as "json",
      headers: headers,
    });
  }

  downloadReciept(data) {
    let token = this.getTokenVa("accessToken");
    let headers = new HttpHeaders({
      "Accept-Language": localStorage.getItem("Language"),
      "content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    });
    return this._http.get(this._apiUrl + "v1/api/payment/recieptinfo/" + data, {
      responseType: "arraybuffer" as "json",
      headers: headers,
    });
  }

  downloadApproval(data) {
    let token = this.getTokenVa("accessToken");
    let headers = new HttpHeaders({
      "Accept-Language": localStorage.getItem("Language"),
      "content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    });
    return this._http.get(
      this._apiUrl + "v1/api/application/download/" + data,
      {
        responseType: "arraybuffer" as "json",
        headers: headers,
      }
    );
  }

  /*****************************************************************************************/
  // @PURPOSE      	: 	To show server error
  /*****************************************************************************************/
  // public swal = swal;
  showServerError(e) {
    ////console.log(e);
    //this.coolDialogs.alert('Whoa boy, be careful!');
    // this.coolDialogs.alert(e.error.errorDescription, {
    // 	theme: 'default',
    // 	okButtonText: 'OK',
    // 	title: 'Response Description'
    //   });
    //swal("Response Description",e.error.errorDescription);
    // ////console.log('Internal server error',e)
  }
  /****************************************************************************/
}
