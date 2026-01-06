import { Injectable, Injector } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot } from '@angular/router';
import { BaseComponent } from './../common/commonComponent';

/****************************************************************************
@PURPOSE      : To allow public pages can be activated. (After Login)
@PARAMETERS   : N/A
@RETURN       : <boolean>
/****************************************************************************/
@Injectable()
export class CanLoginActivate extends BaseComponent implements CanActivate {
    constructor(inj : Injector) { super(inj)}
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot){
        //////console.log('can login activate called', this.getToken("accessToken"))
        if(!this.getToken("accessToken")){
          //////console.log('returning true', this.getToken("accessToken"))
          return true;
        }
        this.router.navigate(['/main']);
        return false
    }
}
/****************************************************************************/

/****************************************************************************
@PURPOSE      : To allow authorized pages can be activated. (Before Login)
@PARAMETERS   : N/A
@RETURN       : <boolean>
/****************************************************************************/
@Injectable()
export class CanAuthActivate extends BaseComponent implements CanActivate {
    constructor(inj : Injector) { super(inj)}
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot){
        //////console.log('can login activate called', this.getToken("accessToken"))
        if(this.getToken("accessToken")){
          //////console.log('returning true', this.getToken("accessToken"))
          return true;
        }
        //////console.log('returning false', this.getToken("accessToken"))
        this.router.navigate(['/home']);
        return false
    }
}
/****************************************************************************/



 