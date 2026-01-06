import { Component, Injector, OnInit } from '@angular/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BaseComponent } from 'src/app/common/commonComponent';
const FileSaver = require('file-saver');

@Component({
  selector: 'app-dashboard-view',
  templateUrl: './dashboard-view.component.html',
  styleUrls: ['./dashboard-view.component.css']
})
export class DashboardViewComponent extends BaseComponent implements OnInit {
  public dashboardVisaStatus: any = {}
  public appNumber: any
  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj)
    this.activatedRoute.params.subscribe((params) => {
      // //console.log(params['id']);

      this.appNumber = params['id'];
      this.setToken('applicationNumber', this.appNumber)
      this.getVisaDetails(this.appNumber)
    })
  }

  ngOnInit(): void {
  }
  successContent: any = {}
  getVisaDetails(data) {
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('application/checkstatus/' + data, '', 'get', false, false, 'REG').then(success => {
        let successData: any = success
        this.successContent = successData
        this.dashboardVisaStatus = successData.applicationTracker
        this.ngxLoader.stop();
      }).catch(e => {
        this.ngxLoader.stop();
        this.toastr.error(e.message, 'Oops!')
      });
    }, 1000)
  }

  public resource: any;
  downloadApplication(data) {
    this.commonService.downloadApproval(data).subscribe((res) => {
      // //console.log(res);
      this.resource = res;
      let blob = new Blob([this.resource], { type: "application/pdf;charset=UTF-8" });
      FileSaver.saveAs(blob, data);
    });
  }
}
