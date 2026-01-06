import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent'

@Component({
  selector: 'app-module-view',
  templateUrl: './module-view.component.html',
  styleUrls: ['./module-view.component.css']
})
export class ModuleViewComponent extends BaseComponent implements OnInit {
  public moduleId: any;
  public moduleDetails: any = {};

  constructor(inj: Injector) {
    super(inj)
    this.activatedRoute.params.subscribe((params) => {
      this.moduleId = params['id']
      this.fetchModule(this.moduleId)
    })

  }

  ngOnInit(): void {
  }

  /****************************************************************************
   @PURPOSE      : fetch Module details.
   @PARAMETERS   : form,formdata
   @RETURN       : Module Management object
****************************************************************************/
  fetchModule(id) {
    this.commonService.callApi('viewmoduledetails/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.moduleDetails = success;
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /**********************************************************************************/

}
