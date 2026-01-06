import { isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
// import { BaseComponent } from './../common/commonComponent';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EnvService } from '../common/env.service';

// import { environment} from '../environments/environment.ts';
// import swal from 'sweetalert';
// import {NgxCoolDialogsService} from 'ngx-cool-dialogs'

@Injectable({
	providedIn: 'root'
})

export class CommonService {
	authorised: any = false;
	constructor(public _http: HttpClient, @Inject(PLATFORM_ID) platformId: Object, private envservice: EnvService) {
		// super(inj);
		this.platformId = platformId;
		// this._apiUrl = this.environment.apiUrl;
// 		this._apiUrl = window.location.href.split('#')[0];
		this._apiUrl = 'http://localhost:8082/approver-api/';
		// this._apiUrl = 'http://194.164.200.188/approver-api/';  // front

		//console.log(this._apiUrl);
	}



	public _apiUrl;
	public platformId;

	public getToken(key) {
		if (isPlatformBrowser(this.platformId)) {
			return window.localStorage.getItem(key);
		}
	}
	public setToken(key, value) {
		if (isPlatformBrowser(this.platformId)) {
			window.localStorage.setItem(key, value);
		}
	}


	/*******************************************************************************************
	@PURPOSE        :   Call api
	@Parameters     :   {
							url : <url of api>
							data : <data object (JSON)>
							method : String (get, post)
							isForm (Optional) : Boolean - to call api with form data
							isPublic (Optional) : Boolean - to call api without auth header
						}
	/*****************************************************************************************/

	callApi(url, data, method, isForm?, isPublic?, islogin?) {
		let headers;
		if (isPublic) {
			headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'loggeduser': 'Admin', 'content-Type': 'application/json', 'Authorization': 'Basic Y3JpbXNvbjpjUmlNczBuJGRjMW5EMUE=' });
		} else {
			headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'loggeduser': 'Admin', 'Content-Type': 'application/json', 'Authorization': 'Basic Y3JpbXNvbjpjUmlNczBuJGRjMW5EMUE=' });
			//console.log("non public");
		}
		if (isForm) {
			headers = new HttpHeaders({ 'Accept-Language': localStorage.getItem('Language'), 'loggeduser': 'Admin', 'Authorization': 'Basic Y3JpbXNvbjpjUmlNczBuJGRjMW5EMUE=' });
		}
		if (islogin === 'APPR') {
			//console.log("orr public");
			if (method === 'post') {
				//console.log("oui public");
				return this._http.post(this._apiUrl + 'v1/api/' + url, data, { headers }).toPromise().then(res => {
					return res;


				});

			} else if (method == 'get') {
				//console.log("extene public");
				return this._http.get(this._apiUrl + 'v1/api/' + url, { headers }).toPromise().then(res => {
					return res;
				});
			} else if (method == 'put') {
				return this._http.put(this._apiUrl + 'v1/api/' + url, data, { headers }).toPromise().then(res => {
					return res;
				});

			} else {
				return this._http.delete(this._apiUrl + 'v1/api/' + url, data).toPromise().then(res => {
					return res;
				});
			}

		} else if (islogin === 'LOG') {
			return this._http.post(this._apiUrl + url, data, { headers }).toPromise().then(res => {
				return res;
			});
		}
	}

	/*****************************************************************************************/
	// @PURPOSE      	: 	To show server error
	/*****************************************************************************************/
	// public swal = swal;
	showServerError(e) {
		//console.log(e);
		//this.coolDialogs.alert('Whoa boy, be careful!');
		// this.coolDialogs.alert(e.error.errorDescription, {
		// 	theme: 'default',
		// 	okButtonText: 'OK',
		// 	title: 'Response Description'
		//   });
		//swal("Response Description",e.error.errorDescription);
		// //console.log('Internal server error',e)
	}
	/****************************************************************************/

}


