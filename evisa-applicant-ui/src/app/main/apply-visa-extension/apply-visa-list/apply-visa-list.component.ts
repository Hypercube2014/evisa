import { Component, Injector, OnInit } from '@angular/core';
import { NgxUiLoaderService } from "ngx-ui-loader";
import { BaseComponent } from '../../../common/commonComponent';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
const FileSaver = require('file-saver');
declare var $: any;
@Component({
  selector: 'app-apply-visa-list',
  templateUrl: './apply-visa-list.component.html',
  styleUrls: ['./apply-visa-list.component.css']
})
export class ApplyVisaListComponent extends BaseComponent implements OnInit {
  typeRef: any;

  modalRef: BsModalRef;
  public pagesize = 10;
  public totalItem: any;
  public data: any = {
    pageNumber: 1,
    pageSize: this.pagesize,
    loggedUser: this.getToken('username')
  }
  visaExtensionDetails: any = []
  visaExtensionfilters: any = null

  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService, public modalService: BsModalService) {
    super(inj)
  }

  ngOnInit(): void {
    this.searchVisaExtension(this.data)
  }
  refreshPage() {
    this.searchVisaExtension(this.data)
  }

  public number: 0;
  public size: 0;
  public noofelements: 0;
  public totalElements: 0;
  public loading: boolean;

  serchVisa: any = []
  searchVisaExtension(data) {
    data.role = "APPLICANT"
    this.loading = true;
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('searchapplicantvisaextension', data, 'post', false, false, 'REG').then(success => {
        let successData: any = success;
        this.totalItem = successData.totalElements;
        this.number = successData.number;
        this.size = successData.size;
        this.noofelements = successData.numberOfElements;
        this.totalElements = successData.totalElements;
        this.loading = false;
        // //console.log(successData.content);
        this.ngxLoader.stop();
        if (successData.content.length) {
          this.serchVisa = successData.content;
          //  //console.log(this.serchVisa);
        } else {
          this.serchVisa = [];
        }

      }).catch(e => {
        this.ngxLoader.stop();
        this.toastr.error(e.message, 'Oops!')
      });
    }, 1500);
  }


  getVisaExtension(data, type) {
    this.typeRef = type
    this.loading = true;
    // //console.log(data);
    setTimeout(() => {
      this.commonService.callApi('searchavailableextensions', data, 'post', false, false, 'REG').then(success => {
        let successData: any = success;
        this.totalitem = successData.totalElements;
        this.numbers = successData.number;
        this.sizes = successData.size;
        this.loading = false;
        this.numofelements = successData.numberOfElements;
        this.totalElements = successData.totalElements;
        // //console.log(this.totalitem);
        let items: any = [];
        if (successData.content.length) {
          if (this.typeRef == 'modal') {
             successData.content.forEach(function(item){
                  if (item.username === data.loggedUser) {
                      items.push(item)
                  }
              });
              this.modalVisaExtensionDetails = items;
          } else {
              successData.content.forEach(function(item){
                  if (item.username === data.loggedUser) {
                      items.push(item);
                  }
              });
            this.visaExtensionDetails = items;
          }
        } else {
          this.visaExtensionDetails = [];
        }

      }).catch(e => {
        this.ngxLoader.stop();
        this.toastr.error(e.message, 'Oops!');
      });
    });
  }



  onClose(applicantNo, fileNumber, givenName, emailId, passport, previousVisaDate) {
    this.modalRef.hide()
    this.setToken('applicantNo', applicantNo);
    this.setToken('givenName', givenName);
    this.setToken('fileNumber', fileNumber);
    this.setToken('passport', passport);
    this.setToken('emailId', emailId);
    this.setToken('VisaDate', previousVisaDate)
  }
  /****************************************************************************
      @PURPOSE      : Paginations 
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
      this.searchVisaExtension(this.filterObj);

    } else {
      this.data.pageNumber = e.page;
      this.data.pageSize = e.itemsPerPage;
      this.searchVisaExtension(this.data);
    }
  }
  rangeChanged(e) {
    if (this.isFilterApplied) {
      // //console.log(e)
      this.filterObj.pageSize = e;
      this.searchVisaExtension(this.filterObj);
    } else {
      this.data.pageSize = e;
      this.searchVisaExtension(this.data);
    }

  }
  /***************************************************************/
  modalVisaExtensionDetails: any = []
  public pages = 1;
  pagesized = 10;

  public numbers: 0;
  public sizes: 0;
  public numofelements: 0;
  public totelements: 0;
  public totalitem: any;

  showModal(template) {
      this.pagesized = 10;
      this.visaExtensionfilters = null;

      this.modalVisaExtensionDetails = this.visaExtensionDetails;
      // //console.log(this.data);
      this.getVisaExtension(this.data, 'modal');
      this.modalRef = this.modalService.show(template);
  }

  onSearch(event: any) {
        let value = event.target.value;
        const filtersss = this.modalVisaExtensionDetails.filter(o =>
            String(o.applicationNumber).toLowerCase().includes(value.toLowerCase())
        );
        this.visaExtensionfilters = filtersss

        if (value == '') {
            this.visaExtensionfilters = null
        }
    }

  /****************************************************************************
      @PURPOSE      : paginations for modal
      @PARAMETERS   : pageNumber,PageSize,loggedinuse
      @RETURN       : NA
   ****************************************************************************/

  public currentpage = 1;
  rangelists = [5, 10, 25, 100]
  public isFilterapplieed: boolean = false;
  public filterobject: any = {};
  pageChange(e) {
    if (this.isFilterapplieed) {
      this.filterobject.pageNumber = e.page;
      this.filterobject.pageSized = e.itemsPerPage;
      this.getVisaExtension(this.filterobject, 'modal');

    } else {
      this.data.pageNumber = e.page;
      this.data.pageSized = e.itemsPerPage;
      this.getVisaExtension(this.data, 'modal');
    }
  }

  rangeChange(e) {
    if (this.isFilterapplieed) {
      // //console.log(e)
      this.filterobject.pageSized = e;
      this.getVisaExtension(this.filterobject, 'modal');
    } else {
      this.data.pageSized = e;
      this.getVisaExtension(this.data, 'modal');
    }

  }
  /**********************************************************************************/

  public resource: any;
  downloadApplication(data, extension) {
    this.commonService.downloadApproval(data).subscribe((res) => {
      // //console.log(res);
      this.resource = res;
      let blob = new Blob([this.resource], { type: "application/pdf;charset=UTF-8" });
      FileSaver.saveAs(blob, extension);
    });
  }
}
