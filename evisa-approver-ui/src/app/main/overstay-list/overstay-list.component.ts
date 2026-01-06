import { Component, OnInit,Injector } from '@angular/core';
import { BaseComponent } from '../../common/commonComponent';
import { NgPopupsService } from 'ng-popups';
import { NgxUiLoaderService } from "ngx-ui-loader";




@Component({
    selector: 'app-overstay-list',
    templateUrl: './overstay-list.component.html',
    styleUrls: ['./overstay-list.component.css']
})
export class OverstayListComponent  extends BaseComponent implements OnInit {

    public pagesize = 10;
    public totalItem: any;
    public visaCheckOverstayDetails: any;
    public data: any = {
        pageNumber: 1,
        pageSize: this.pagesize,
        loggedUser: this.getToken('username')
    }
    public loading: boolean;
    public number: 0;
    public size: 0;
    public noofelements: 0;
    public totalElements: 0;
    public dataa: any = {
        ReferenceNumber: 1,
        Amount: this.pagesize,
    }


    constructor(inj: Injector, private ngxLoader: NgxUiLoaderService, private ngPopups: NgPopupsService) {
        super(inj);
    }


    ngOnInit(): void {
        this.getVisaExtension();
    }

    getVisaExtension() {
        //console.log(this.data);
        setTimeout(() => {
            this.commonService.callApi('searchavailableoverstay', this.data, 'post', false, false, 'APPR').then(success => {
                let successData: any = success;
                this.totalItem = successData.totalElements;
                this.number = successData.number;
                this.size = successData.size;
                this.noofelements = successData.numberOfElements;
                this.totalElements = successData.totalElements;

                //console.log(successData);
                if (successData.content.length) {
                    this.visaCheckOverstayDetails = successData.content;
                }
            }).catch(e => {

            });
        });
    }



}