import { Component, OnInit, Injector } from '@angular/core';
import { BaseComponent } from '../../../common/commonComponent';
import { NgxSpinnerService } from "ngx-spinner";
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { TranslateService } from '@ngx-translate/core';
@Component({
  selector: 'app-applications-list',
  templateUrl: './applications-list.component.html',
  styleUrls: ['./applications-list.component.css']
})
export class ApplicationsListComponent extends BaseComponent implements OnInit {
  public pagesize = 10;
  public totalItem: any;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
  }
  constructor(inj: Injector,private spinner: NgxSpinnerService, private ngxLoader: NgxUiLoaderService,
    public translate: TranslateService) {
    super(inj);
  }

  ngOnInit(): void {
    this.getDraftApplications(this.data);
    this.translate.use(localStorage.getItem('Language'));
  }

  refreshPage(){
    this.getDraftApplications(this.data);
  }

  /****************************************************************************
       @PURPOSE      : To retrive the draft applications List
       @PARAMETERS   : pageNumber,PageSize,loggedinuse
       @RETURN       : NA
    ****************************************************************************/
  public draftApplications = [];
  public number=0;
  public size=0;
  public noofelements=0;
  public totalElements=0;
  public loading: boolean;
  getDraftApplications(data) {
    data.loggedUser = this.getToken('username');
    this.loading = true;
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('searchapplicationfile', data, 'post', false, false, 'REG').then(success => {
        // //console.log("coucuoucouuuuuuuuuuuuu")
        let successData: any = success;
        this.totalItem = successData.totalElements;
        this.number=successData.number;
        this.size=successData.size;
        this.loading=false;
        this.ngxLoader.stop();
        this.noofelements=successData.numberOfElements;
        this.totalElements=successData.totalElements;
        if (successData.content.length) {
          this.draftApplications = successData.content;
        }else{
          this.draftApplications=[];
        }
      }
      ).catch(e => {
        this.ngxLoader.stop();
        this.toastr.error(e.message, 'Oops!')
      });
    },1500);
  }
  /****************************************************************************/




  /****************************************************************************
      @PURPOSE      : To retrive the draft applications List
      @PARAMETERS   : pageNumber,PageSize,loggedinuse
      @RETURN       : NA
   ****************************************************************************/
  public currentPage = 1;
  public showBoundaryLinks = true;
  public rangeList = [5, 10, 25, 100];
  public isFilterApplied: boolean = false;
  public filterObj: any = {};
  pageChanged(e) {
    if (this.isFilterApplied) {
      this.filterObj.pageNumber = e.page;
      this.filterObj.pageSize = e.itemsPerPage;
      this.getDraftApplications(this.filterObj);

    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.getDraftApplications(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      // //console.log(e)
      this.filterObj.pageSize = e;
      this.getDraftApplications(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.getDraftApplications(this.data);
    }
   
  }
  /***************************************************************/

}
