import { Component, Injector, OnInit } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent';

@Component({
  selector: 'app-view-faq',
  templateUrl: './faq-view.component.html',
  styleUrls: ['./faq-view.component.css']
})
export class FaqViewComponent extends BaseComponent implements OnInit {

  public faqId: any;

  constructor(inj: Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      this.faqId = params['id'];
      this.viewFaqDetails(this.faqId);
    })
  }

  ngOnInit(): void {
  }

  successData: any;
  viewFaqDetails(id) {
    this.commonService.callApi('systemfaq/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.successData = success
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
}

