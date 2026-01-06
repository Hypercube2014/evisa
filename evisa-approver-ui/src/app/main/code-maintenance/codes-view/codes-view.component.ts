import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-codes-view',
  templateUrl: './codes-view.component.html',
  styleUrls: ['./codes-view.component.css']
})
export class CodesViewComponent extends BaseComponent implements OnInit {

  public id: number

  constructor(inj: Injector) {
    super(inj);

    this.activatedRoute.params.subscribe((params) => {
      this.id = params['id'];
      this.getCountryDetails(this.id)
    })
  }

  ngOnInit(): void {
  }

  /****************************************************************************
  @PURPOSE      : To retrive Country Details by ID.
  @PARAMETERS   : URL, ,methodname
  @RETURN       : Details of Country Code
****************************************************************************/
  public countryDetails: any = {}
  getCountryDetails(id) {
    this.commonService.callApi('mastercode/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.countryDetails = success;
    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /****************************************************************************/
}
