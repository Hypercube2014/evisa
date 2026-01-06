import { Injectable } from '@angular/core';

// import * as messages from './errorMessagesdata.ts/index.js';

import {MessagesData} from './errorMessagesdata'

@Injectable({
  providedIn: 'root'
})

export class ErrorMessages {
  public MSG = (<any>MessagesData)

  constructor() {}
  getError(field, error){
    var message;
    if (error) {
        //console.log("error",error,field);
        if (error.required) {
          var required= this.MSG.ERROR.REQUIRED;
          switch(field){ 
            case 'email' : { message =required.Email } break;
            case 'field' : { message =required.Field } break;
            case 'mobile' : { message =required.mobile } break;
            case 'password' : { message =required.Password } break;
            case 'oldPassword' : { message =required.oldPassword } break;
            case 'newPassword' : { message =required.newPassword } break;
            case 'confirmPassword' : { message =required.confirmPassword } break;
            case 'firstname' : { message =required.firstname } break;
            case 'lastname' : { message =required.lastname } break;
            case 'username' : { message =required.username } break;
            case 'Title' : { message =required.Title } break;
            case 'content':{ message =required.content} break;
            case 'metaTitle' : { message =required.metaTitle } break;
            case 'metaKeyword':{ message =required.metaKeyword} break;
            case 'metaDescription':{message =required.metaDescription} break;
            case 'pageId' :{message =required.Pageid} break;
            case 'pageName':{message =required.pageName} break;
            case 'pageUrl' :{message =required.pageUrl} break;
          }
        } else if (error.pattern) {
          var pattern= this.MSG.ERROR.PATTERN;
          switch(field){ 
            case 'email' : { message = pattern.Email } break;
          }
        } else if (error.minlength) {
          var minlength= this.MSG.ERROR.MINLENGTH;
          switch(field){ 
            case 'mobile' : { message = minlength.mobile } break;
          }
        } 
        return message;
      }
    }
  }