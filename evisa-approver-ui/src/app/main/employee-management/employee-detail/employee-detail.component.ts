import { Component, Injector, OnInit } from '@angular/core';
import * as moment from 'moment';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { BaseComponent } from 'src/app/common/commonComponent';

@Component({
  selector: 'app-employee-detail',
  templateUrl: './employee-detail.component.html',
  styleUrls: ['./employee-detail.component.css']
})
export class EmployeeDetailComponent extends BaseComponent implements OnInit {
  compareDate: any = []
  maxDate: any = []
  date = new Date()
  employeeDetails: any = {}
  submitted: boolean = false
  isNew: boolean = false
  id: string
  successData: any;
  bsConfig: Partial<BsDatepickerConfig>;
  isUserExists: boolean = true;
  datePickerConfig: Partial<BsDatepickerConfig>;
  dateValidationConfig: Partial<BsDatepickerConfig>;
  colorTheme = 'theme-blue'


  constructor(inj: Injector) {
    super(inj);
    this.activatedRoute.params.subscribe((params) => {
      if (params['id'] === 'new') {
        this.isNew = true;
      } else {
        this.id = params['id'];
        this.getEmployeeDetails(this.id)
        this.isNew = false;
      }
    })
    this.datePickerConfig = Object.assign({}, { containerClass: this.colorTheme, dateInputFormat: 'DD-MM-YYYY', showWeekNumbers: false });
    this.dateValidationConfig = Object.assign({}, { containerClass: this.colorTheme, showWeekNumbers: false, dateInputFormat: 'DD-MM-YYYY' });
  }
    public rolessbco = [
        {
            'code': 'BCO',
            'name': "Border Control Officer"
        },
        {
            'code': 'SECRE',
            'name': "Secretary"
        }
    ]
  public roles = [
    {
      'code': 'SBCO',
      'name': "Senior Border Control Officer"
    },
    {
      'code': 'DM',
      'name': "Decision Maker"
    },
      {
          'code': 'SECRE',
          'name': "Secretary"
      }
  ]

  public dateError: boolean = false
  ngOnInit(): void {
    this.getCountrList()
    this.getNationalityList()
    this.getRoles()
    let date = new Date()
    this.maxDate = moment(new Date(), "DD-MM-YYYY").subtract(15, 'years').toDate();
    this.dateValidationConfig.maxDate = this.maxDate
    // if (this.getToken('Role') === 'ADM') {
    //   this.isNew = true;
    // }

  }

  public roleDetails = [
    {
      'code': 'Admin',
      'name': "Admin"
    }
  ]
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

  pmail: boolean = true
  public cnfmMail: boolean = false
  onValidation() {
    if (this.employeeDetails.confirmEmail != undefined) {
      if (this.employeeDetails.email.toLowerCase() == this.employeeDetails.confirmEmail.toLowerCase()) {
        this.cnfmMail = true
      } else {
        this.cnfmMail = false
      }
    }
    if (this.employeeDetails.personalEmail != undefined) {
      if (this.employeeDetails.email.toLowerCase() === this.employeeDetails.personalEmail.toLowerCase()) {
        this.pmail = false
      } else {
        this.pmail = true
      }
    }
  }


  /****************************************************************************
   @PURPOSE      : To Submit and Edit Employee details.
   @PARAMETERS   : form,employeeDetails
   @RETURN       : API Status
  ****************************************************************************/
  submitForm(form, employeeDetails) {
    if (this.isNew) {
      if (form.valid && /* this.dateError && */ this.dobError && this.cnfmMail && this.pmail) {
        if (this.employeeDetails.role === "ADM" || this.employeeDetails.role === "DMM") {
          this.employeeDetails.reportingTo = null;
        }
        this.employeeDetails.createdBy = this.getToken('username')
        this.employeeDetails.loggedUser = this.getToken('username')
        this.employeeDetails.employementStatus = 'Y'
        if (this.getToken('Role') === 'SBCO') {
          this.employeeDetails.reportingTo = this.getToken('username')
        }
        if (this.getToken('Role') === 'DMM') {
          this.employeeDetails.reportingTo = this.getToken('username')
        }
        employeeDetails.email = employeeDetails.email.toLowerCase()
        if (employeeDetails.personalEmail != undefined) {
          employeeDetails.personalEmail = employeeDetails.personalEmail.toLowerCase()
        }

        this.commonService.callApi('register/employee', employeeDetails, 'post', false, false, 'APPR').then(success => {
          this.successData = success;
          if (this.successData.apiStatusCode === "SUCCESS") {
            this.router.navigate(["main/employee-mgnt/"]);
            this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
          } else {
            this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error');
          }
        }
        ).catch(e => {
          this.toastr.errorToastr(e.message, 'Oops!')
        });

      } else {
        this.submitted = true;
      }
    } else {
      if (this.dateError && this.dobError && this.pmail) {
        if (this.employeeDetails.role === "ADM" || this.employeeDetails.role === "DMM") {
          this.employeeDetails.reportingTo = null;
        }
        this.employeeDetails.updatedBy = this.getToken('username')
        this.employeeDetails.userName = this.id
        this.employeeDetails.employementStatus = 'Y'

        employeeDetails.email = employeeDetails.email.toLowerCase()
        if (employeeDetails.personalEmail != undefined) {
          employeeDetails.personalEmail = employeeDetails.personalEmail.toLowerCase()
        }

        this.commonService.callApi('register/updateemployeedetails', employeeDetails, 'put', false, false, 'APPR').then(success => {
          this.successData = success;
          if (this.successData.apiStatusCode === "SUCCESS") {
            this.router.navigate(["main/employee-mgnt/"]);
            this.toastr.successToastr(this.successData.apiStatusDesc, 'Success')
          } else {
            this.toastr.errorToastr(this.successData.apiStatusDesc, 'Error');
          }
        }
        ).catch(e => {
          this.toastr.errorToastr(e.message, 'Oops!')
        });
      }
    }

  }
  /****************************************************************************/

  /****************************************************************************
  @PURPOSE      : To retrive Employee Details by ID.
  @PARAMETERS   : id
  @RETURN       : Details of Employee
  ****************************************************************************/
  getEmployeeDetails(id) {
    this.commonService.callApi('employee/' + id, '', 'get', false, false, 'APPR').then(success => {
      this.employeeDetails = success;
      this.employeeDetails.dateOfJoining = new Date(this.employeeDetails.dateOfJoining)
      this.employeeDetails.dateOfBirth = new Date(this.employeeDetails.dateOfBirth)
      //console.log(this.employeeDetails);
      this.onSelectdob(this.employeeDetails.dateOfBirth)
      this.onSelectDate(this.employeeDetails.dateOfJoining)
      this.getRoleCode();
    }
    ).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!')
    });
  }
  /****************************************************************************/

  /****************************************************************************
  @PURPOSE      : To send the username to User Exists Method.
  @PARAMETERS   : $Event
  @RETURN       : Boolean
  ****************************************************************************/
  timeout: any = null;
  onKeySearchUser(event: any) {
    clearTimeout(this.timeout);
    let $this = this;
    this.timeout = setTimeout(function () {
      if (event.keyCode != 13) {
        if (event.target.value) {
          event.target.value;
          $this.getUserExists(event.target.value);
        } else {
          this.isUserExists = true;
        }
      }
    }, 1000);
  }
  /****************************************************************************/
  username: any = null;
  firstname: any = "";
  lastname: any = "";
  employeeDateOfJoining = this.employeeDetails.dateOfJoining = new Date();
  onKeyCreateUsernameFromFirstName(event: any) {
    this.firstname = event.target.value.toLowerCase()
    this.username = this.firstname + '.' + this.lastname;
    this.employeeDetails.userName = this.username;
  }
  onKeyCreateUsernameFromLastName(event: any) {
    this.lastname = event.target.value.toLowerCase();
    this.lastname = this.lastname.replace(" ",".");
    this.username = this.firstname + '.' + this.lastname;
    this.employeeDetails.userName = this.username;
  }
  /****************************************************************************/

  /****************************************************************************
  @PURPOSE      : To verify the username to User Exists or not in DB.
  @PARAMETERS   : username
  @RETURN       : Boolean
  ****************************************************************************/
  getUserExists(username) {
    this.commonService.callApi('employeeexists/' + username, '', 'get', false, false, 'APPR').then(success => {
      if (success === true) {
        this.isUserExists = false;
      } else {
        this.isUserExists = true;
      }
    }).catch(e => {
      this.toastr.errorToastr(e.message, 'Oops!');
    });
  }
  /****************************************************************************/

  /****************************************************************************
  @PURPOSE      : To Allow Numbers & Characters.
  @PARAMETERS   : Event
  @RETURN       : Boolean
  ****************************************************************************/
  keyPressAlphaNumeric(event) {
    var inp = String.fromCharCode(event.keyCode);
    if (/[a-zA-Z0-9.]/.test(inp)) {
      this.result = false
      return true;
    } else {
      this.result = true
      return false;
    }
  }
  /****************************************************************************/


  /****************************************************************************
  @PURPOSE      : Get Fullname based on roles.
  @PARAMETERS   : Role
  @RETURN       : fullName
****************************************************************************/
  public roleCodeDetails: any = {};
  public value: any;

  getRoleCode() {
    if (this.employeeDetails.role === "DM" || this.employeeDetails.role === "BCO" || this.employeeDetails.role === "SBCO" ||this.employeeDetails.role ==="SECRE") {

      if (this.employeeDetails.role === "DM" || this.employeeDetails.role === "SBCO" || this.employeeDetails.role ==='SECRE') {
        this.roleCodeDetails = "DMM"
      }
      if (this.employeeDetails.role === "BCO") {
        this.roleCodeDetails = "SBCO"
      }
      this.commonService.callApi('employeesbyrolecode/' + this.roleCodeDetails, '', 'get', false, false, 'APPR').then(success => {
        this.roleCodeDetails = success;

        this.value = this.roleCodeDetails.employeeDetailsDTOs
        //console.log(this.value)
      }).catch(e => {
        this.toastr.errorToastr(e.message, 'Oops!')
      });
    }
  }

  /************************************************************************************************* */

  onSelect() {
    this.getRoleCode()
    this.employeeDetails.reportingTo = null;
  }

  dobError: boolean
  onSelectdob(dob) {
    if (this.maxDate > this.employeeDetails.dateOfBirth) {
      this.dobError = true
    } else {
      this.dobError = false
    }
    this.compareDate = moment(this.employeeDetails.dateOfBirth, "DD-MM-YYYY").add(15, 'years').toDate();
    this.datePickerConfig.minDate = this.compareDate
  }

  onSelectDate(doj) {
    if (this.employeeDetails.dateOfJoining > this.compareDate) {
      this.dateError = true
    } else {
      this.dateError = false
    }

  }


  validateInput(event: Event, type: 'string' | 'number' | 'address' | 'passport'): void {
    // Vérifier si l'élément cible est un input ou une textarea
    const inputElement = event.target as HTMLInputElement | HTMLTextAreaElement;
    const input = inputElement.value; // Obtenir la valeur de l'élément
  
    // Définir une expression régulière selon le type
    let regex: RegExp;
  
    switch (type) {
      case 'string': // Autoriser uniquement les lettres sans espaces
        regex = /^[a-zA-Z-]*$/;
        break;
      case 'number': // Autoriser uniquement les chiffres
        regex = /^[0-9]*$/;
        break;
      case 'address': // Autoriser lettres, chiffres et quelques caractères spéciaux
        regex = /^[a-zA-Z0-9\s,.'-/]*$/;
        break;
      case 'passport': // Numéro de passeport
        regex = /^[A-Z0-9-]{6,12}$/;
        break;
      default:
        regex = /.*/; // Autoriser tout (fallback)
    }
  
    // Remplacer les caractères invalides
    if (!regex.test(input)) {
      inputElement.value = input.replace(
        type === 'string'
          ? /[^a-zA-Z-]/g // Pour les lettres, autoriser uniquement les lettres et les tirets
          : type === 'number'
            ? /[^0-9]/g // Pour les chiffres, supprimer les caractères non numériques
            : /[^a-zA-Z0-9\s,.'-/]/g, // Pour les adresses, supprimer les caractères non autorisés
        ''
      );
    }
  }
  

}
