import { Component, OnInit, Injector } from '@angular/core';
import { BaseComponent } from '../../common/commonComponent';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { ImageCroppedEvent } from 'ngx-image-cropper';
import { NgxUiLoaderService } from "ngx-ui-loader";
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';

declare var $: any;

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent extends BaseComponent implements OnInit {
  imageChangedEvent: any = '';
  croppedImage: any = "";
  public file: any = "";

  public submitted: boolean = false

  public base64Image = 'data:image/png;base64,'

  public isEdit: boolean = false;

  bsConfig: Partial<BsDatepickerConfig>;

  maxDate = new Date();

  colorTheme = 'theme-blue'
  datePickerConfig = {
    dateInputFormat: 'DD-MM-YYYY',
    containerClass: this.colorTheme,
    maxDate: new Date()
  }

  dateValidationConfig = {
    dateInputFormat: 'DD-MM-YYYY',
    containerClass: this.colorTheme,
    maxDate: new Date()

  }


  public genderDetails = [
    {
      'code': 'M',
      'name': "Male"
    },
    {
      'code': 'F',
      'name': "Female"
    }, {
      'code': 'O',
      'name': "Others"
    }
  ]

  public bloodGroupDetails = [

    {
      'code': 'A+ve',
    },
    {
      'code': 'A-ve',
    },
    {
      'code': 'B+ve',
    },
    {
      'code': 'B-ve',
    },
    {
      'code': 'O+ve',
    },
    {
      'code': 'O-ve',
    },
    {
      'code': 'AB+ve',
    },
    {
      'code': 'AB-ve',
    }
  ]

  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService) {
    super(inj)
  }

  ngOnInit(): void {
    this.getProfileData(this.getToken('username'));
    this.getProfileValues(this.getToken('username'));
    this.user.phpto = '';
    this.getCountrList()
    this.maxDate.setDate(this.maxDate.getDate() - 7300);
  }


  /****************************************************************************
      @PURPOSE      : Retriving Profile data
      @PARAMETERS   : username
      @RETURN       : NA
   ****************************************************************************/
  profileData: any;
  modalRef: BsModalRef;
  public user: any = {};
  getProfileData(username) {
    this.ngxLoader.start();
    setTimeout(() => {
      this.commonService.callApi('employee/' + username, '', 'get', false, false, 'APPR').then(success => {
        this.profileData = success;

        // if (this.user.photo) {
        //   this.user.photo = 'data:image/png;base64,' + this.profileData.profilePic;
        // }

        this.ngxLoader.stop();
        //console.log(this.profileData)
      }
      ).catch(e => {
        this.ngxLoader.stop();
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }, 1000)
  }
  /****************************************************************************/

  openfile(event: any) {
    //console.log(event)
    event.preventDefault();
    let element: HTMLElement = document.getElementById('profile') as HTMLElement;
    element.click();
  }

  clearFile() {
    //console.log("Old value : ", $('#profile').prop("value"))
    $('#profile').prop("value", "")
  }


  fileChangeEvent(event, template) {
    this.imageChangedEvent = event;
    //console.log(event.target.files[0]);

    if (!this.validateFile(event.target.files[0].name)) {
      if (localStorage.getItem('Language') === 'en') {
        this.toastr.errorToastr("error", "Selected file format is not supported");
        return 0;
      } else {
        this.toastr.errorToastr("erreur", "Le format de fichier sélectionné n'est pas pris en charge");
        return 0;
      }
    }
    if (event.target.files[0].size > 1000000) {
      if (localStorage.getItem('Language') === 'en') {
        this.toastr.errorToastr("error!", "Selected Image is too large to upload!!!")
      } else {
        this.toastr.errorToastr("erreur!",  "L'image sélectionnée est trop grande pour être téléchargée!!!");
        return 0;
      }
    } else {
      this.modalRef = this.modalService.show(template);
    }
  }


  validateFile(name: String) {

    var ext = name.substring(name.lastIndexOf('.') + 1);
    if ((ext.toLowerCase() == 'jpg') || (ext.toLowerCase() == 'png') || (ext.toLowerCase() == 'jpeg')) {
      return true;
    }
    else {
      return false;
    }

  }

  submitcropped() {
    const sendData = new FormData();
    sendData.append('profilepic', this.file);
    sendData.append('username', this.getToken('username'))
    //console.log(sendData);
    this.ngxLoader.start();
    this.commonService.callApi('empprofileattachment', sendData, 'post', true, false, 'APPR').then(success => {
      let successData: any = success;
      //console.log(successData)

      if (successData.apiStatusCode === 'SUCCESS') {
        this.toastr.successToastr("success", successData.apiStatusDesc);
        this.user.photo = 'data:image/png;base64,' + successData.applicationNumber;
        this.setToken('profilePic', successData.applicationNumber);
        this.modalRef.hide();
        this.ngxLoader.stop();

      } else {
        this.toastr.errorToastr("error", successData.apiStatusDesc)
        this.ngxLoader.stop();
      }
    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
      this.ngxLoader.stop();
    });
  }


  showCropper = false;
  imageLoaded() {
    this.showCropper = true;
    //console.log('Image loaded');
  }

  isExceed: boolean = false;
  imageCropped(event: ImageCroppedEvent) {
    this.croppedImage = event.base64;
    //console.log(this.croppedImage)
    this.file = this.base64ToFile(event.base64);
    if (this.file.size > 1000000) {
      this.isExceed = true;
      //console.log(this.isExceed)
    } else {
      this.isExceed = false;
    }
  }


  base64ToFile(base64Image: string): Blob {
    const split = base64Image.split(',');
    const type = split[0].replace('data:', '').replace(';base64', '');
    const byteString = atob(split[1]);
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i += 1) {
      ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], { type });
  }

  /****************************************************************************
      @PURPOSE      : updating Profile data
      @PARAMETERS   : username
      @RETURN       : NA
   ****************************************************************************/
  public profileDetails: any = {};
  public successData: any = {};
  form: any

  onEdit() {
    this.isEdit = true;
  }

  submitForm(form, profileData) {
    if (form.valid) {
      this.profileData.updatedBy = this.getToken('username');
      profileData.userName = this.getToken('username')
      profileData.personalEmail = profileData.personalEmail.toLowerCase()
      this.commonService.callApi('register/updateemployeedetails', profileData, 'put', false, false, 'APPR').then(success => {
        this.successData = success;
        //console.log(this.successData);
        this.getProfileValues(this.getToken('username'));
        this.isEdit = false

        if (this.successData.apiStatusCode === "SUCCESS") {
          this.router.navigate(["/main/my-profile"])
          this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
        } else {
          this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error')
        }

      }).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    } else {
      this.submitted = true;
    }
  }
  /**********************************************************************************/


  /****************************************************************************
       @PURPOSE      : remove Profile picture
       @PARAMETERS   : NA
       @RETURN       : NA
    ****************************************************************************/
  removePhoto() {
    this.commonService.callApi('removeprofileattachment/' + this.getToken('username'), '', 'get', true, false, 'APPR').then(success => {
      let successData: any = success;
      if (successData.apiStatusCode === 'SUCCESS') {
        this.toastr.successToastr(successData.apiStatusDesc, 'Success')
        this.user.photo = '';
        this.setToken('profilePic', "");
      }
    }).catch(e => {
      this.toastr.errorToastr(e.error.apiStatusDesc, 'Oops!')
    });
  }
  /************************************************************************************/
  profileValues: any = {}
  getProfileValues(username) {

    this.commonService.callApi('employeedesc/' + username, '', 'get', false, false, 'APPR').then(success => {
      this.profileValues = success;
      //console.log(this.profileData)
    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }

}



