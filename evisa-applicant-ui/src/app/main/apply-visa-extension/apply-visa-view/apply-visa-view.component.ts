import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-apply-visa-view',
  templateUrl: './apply-visa-view.component.html',
  styleUrls: ['./apply-visa-view.component.css']
})
export class ApplyVisaViewComponent extends BaseComponent implements OnInit {
  public visaExtension: any = {};

  public visaType: any

  extensionId: number
  constructor(public inj: Injector) {
    super(inj)
    this.activatedRoute.params.subscribe((params) => {
      this.extensionId = params['id'];
      this.getVisaExtensionType()
      this.getVisaExtensionById(this.extensionId);
    })
  }

  ngOnInit(): void {
    
  }

  getVisaExtensionById(id) {


    this.commonService.callApi('visaextension/' + id, '', 'get', false, false, 'REG').then(success => {
      this.visaExtension = success
      // //console.log(this.visaExtension.visaType);

      this.visaType = this.applyVisaTypes.filter(x => x.code == this.visaExtension.visaType)
      // //console.log(this.visaType);
      
    }).catch(e => {
      this.toastr.error(e.message, 'Oops!')
    })

  }
  applyVisaTypes: any = []
  getVisaExtensionType() {
    this.commonService.callApi('visadetails/extension', '', 'get', false, false, 'REG').then(success => {
      let successData: any = success
      this.applyVisaTypes = successData.masterCodeResultDTOs;
      // //console.log(this.applyVisaTypes);

    }).catch(e => {
      this.toastr.error(e.message, 'Oops!')
    });
  }
}
