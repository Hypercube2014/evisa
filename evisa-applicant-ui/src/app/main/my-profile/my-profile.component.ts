import { Component, OnInit, Injector } from '@angular/core';
import { BaseComponent } from '../../common/commonComponent';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { ImageCroppedEvent } from 'ngx-image-cropper';
declare var $: any;
@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent extends BaseComponent implements OnInit {

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

  public isEdit: boolean = false;
  imageChangedEvent: any = '';
  croppedImage: any = "";
  constructor(inj: Injector, private ngxLoader: NgxUiLoaderService, private modalService: BsModalService) {
    super(inj)
  }

  ngOnInit(): void {
    this.getProfileData(this.getToken('username'));
    this.user.phpto = '';
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
      this.commonService.callApi('applicant/' + username, '', 'get', false, false, 'REG').then(success => {
        let successData: any = success;
        this.profileData = successData;
        if (this.profileData.profilePic) {
          this.user.photo = 'data:image/png;base64,' + successData.profilePic;
        }

        if (this.profileData.organisation) {
          this.profileData.organisation = 'Yes'
        } else {
          this.profileData.organisation = 'No'
        }
        // //console.log(this.profileData);

        this.ngxLoader.stop();
        // //console.log(this.profileData.organisation)
      }
      ).catch(e => {
        this.ngxLoader.stop();
        this.toastr.error(e.message, 'Oops!')
      });
    })

  }
  /****************************************************************************/



  openfile(event: any) {
    // //console.log(event)
    event.preventDefault();
    let element: HTMLElement = document.getElementById('profile') as HTMLElement;
    element.click();
  }

  clearFile() {
    // //console.log("Old value : ", $('#profile').prop("value"))
    $('#profile').prop("value", "")
  }


  fileChangeEvent(event, template) {
    this.imageChangedEvent = event;
    // //console.log(event.target.files[0]);
    if (!this.validateFile(event.target.files[0].name)) {
      if (localStorage.getItem('Language') === 'en') {
        this.toastr.error("error", "Selected file format is not supported");
        return 0;
      } else {
        this.toastr.error("erreur", "Le format de fichier sélectionné n'est pas pris en charge");
        return 0;
      }
    }

    if (event.target.files[0].size > 1000000) {
      if (localStorage.getItem('Language') === 'en') {
        this.toastr.error("error!", "Selected Image is too large to upload!!!")
      } else {
        this.toastr.error("erreur!",  "L'image sélectionnée est trop grande pour être téléchargée!!!");
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
    this.ngxLoader.start();
    this.commonService.callApi('profileattachment', sendData, 'post', true, false, 'REG').then(success => {
      let successData: any = success;
      // //console.log(successData)
      if (successData.apiStatusCode === 'SUCCESS') {

        this.toastr.success("success", successData.apiStatusDesc);
        this.user.photo = 'data:image/png;base64,' + successData.applicationNumber;
        this.setToken('profilePic', successData.applicationNumber);
        this.modalRef.hide();
        this.ngxLoader.stop();

      } else {

        this.toastr.error("error", successData.apiStatusDesc)
        this.ngxLoader.stop();

      }
    }
    ).catch(e => {
      this.toastr.error(e.message, 'Oops!')
      this.ngxLoader.stop();
    });
    // this.modalRef.hide();
    // //console.log(this.croppedImage);
    // this.admin.photo = this.croppedImage;
    // //console.log(this.admin.photo);
  }
  showCropper = false;
  imageLoaded() {
    this.showCropper = true;
    // //console.log('Image loaded');
  }

  isExceed: boolean = false;
  imageCropped(event: ImageCroppedEvent) {
    this.croppedImage = event.base64;
    // //console.log(this.croppedImage)
    this.file = this.base64ToFile(event.base64);
    // //console.log(this.file.size)
    if (this.file.size > 1000000) {
      this.isExceed = true;
      // //console.log(this.isExceed)
    } else {
      this.isExceed = false;
    }

  }

  public file: any;
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
  form: any;
  public submitted: boolean = false

  onEdit() {
    this.isEdit = true;
  }
  submitForm(form, profileData) {
    if (form.valid) {
      this.profileData.updatedBy = this.getToken('username');
      profileData.userName = this.getToken('username')
      profileData.emailId = profileData.emailId.toLowerCase()
      if (this.profileData.organisation === "Yes") {
        this.profileData.organisation = true
      } else {
        this.profileData.organisation = false
      }

      this.commonService.callApi('applicant/updateuser', profileData, 'put', false, false, 'REG').then(success => {
        this.successData = success;
        // //console.log(this.successData);
        this.isEdit = false

        if (this.profileData.organisation) {
          this.profileData.organisation = 'Yes'
        } else {
          this.profileData.organisation = 'No'
        }

        if (this.successData.apiStatusCode === "SUCCESS") {
          this.router.navigate(["/main/my-profile"])
          // this.toastr.success(decodeURI(this.successData.apiStatusDesc), 'Success')
          this.toastr.success('Mise à jour effectuée avec succès', 'Success')
          ////console.log(this.successData.apiStatusCode)
          
        } else {
          this.toastr.error(this.successData.apiStatusDesc, 'Error')
        }

      }).catch(e => {
        this.toastr.error(e.message, 'Oops!')
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
    // //console.log("remoce photo");

    this.commonService.callApi('removeprofilepic/' + this.getToken('username'), '', 'get', true, false, 'REG').then(success => {
      let successData: any = success;
      if (successData.apiStatusCode === 'SUCCESS') {
        this.toastr.success(successData.apiStatusDesc, 'Success')
        this.user.photo = '';
        this.setToken('profilePic', "");
      }
    }).catch(e => {
      this.toastr.error(e.error.apiStatusDesc, 'Oops!')
    });
  }
  /************************************************************************************/
}


