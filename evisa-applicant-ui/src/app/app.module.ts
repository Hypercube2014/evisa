import { HttpClient, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgSelectModule } from '@ng-select/ng-select';
//import { RECAPTCHA_V3_SITE_KEY, RecaptchaV3Module } from 'ng-recaptcha';
import { DatePipe } from '@angular/common';
import { RecaptchaModule,RecaptchaFormsModule  } from 'ng-recaptcha';
import { NgImageSliderModule } from 'ng-image-slider';

import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NgPopupsModule } from 'ng-popups';
import { ImageCropperModule } from 'ngx-image-cropper';
import { ToastrModule } from 'ngx-toastr';
import { CanAuthActivate, CanLoginActivate } from './common/auth.gaurd';
import { Broadcaster } from "./common/BroadCaster";
import { EnvServiceProvider } from './common/env.service.provider';


import { ModalModule } from 'ngx-bootstrap/modal';
import { TabsModule } from 'ngx-bootstrap/tabs';
// import { ToastrModule } from 'ng6-toastr-notifications';
import { TabAllModule } from '@syncfusion/ej2-angular-navigations';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { NgxSpinnerModule } from "ngx-spinner";
import { NgxUiLoaderModule } from "ngx-ui-loader";

import { defineLocale } from 'ngx-bootstrap/chronos';
import { frLocale } from 'ngx-bootstrap/locale';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BlockCopyPasteDirective } from './common/block-copy-paste.directive';
import { BaseComponent } from './common/commonComponent';
import { FaqComponent } from './faq/faq.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { AddPaymentComponent } from './main/add-payment/add-payment.component';
import { ApplicationPreviewComponent } from './main/application-preview/application-preview.component';
import { ApplyVisaExtensionComponent } from './main/apply-visa-extension/apply-visa-extension.component';
import { DashboardComponent } from './main/dashboard/dashboard.component';
import { ErrorpageComponent } from './main/errorpage/errorpage.component';
import { FooterComponent } from './main/footer/footer.component';
import { HeaderComponent } from './main/header/header.component';
import { MainComponent } from './main/main.component';
import { ApplicationsDetailsComponent } from './main/manage-applications/applications-details/applications-details.component';
import { ApplicationsListComponent } from './main/manage-applications/applications-list/applications-list.component';
import { ManageApplicationsComponent } from './main/manage-applications/manage-applications.component';
import { VisaDetailsComponent } from './main/manage-applications/visa-details/visa-details.component';
import { MyProfileComponent } from './main/my-profile/my-profile.component';
import { NewDashboardComponent } from './main/new-dashboard/new-dashboard.component';
import { PaymentCompletedEditComponent } from './main/payment-completed/payment-completed-edit/payment-completed-edit.component';
import { PaymentCompletedListComponent } from './main/payment-completed/payment-completed-list/payment-completed-list.component';
import { PaymentCompletedComponent } from './main/payment-completed/payment-completed.component';
import { PaymentComponent } from './main/payment/payment.component';
import { PendingPaymentEditComponent } from './main/pending-payment/pending-payment-edit/pending-payment-edit.component';
import { PendingPaymentListComponent } from './main/pending-payment/pending-payment-list/pending-payment-list.component';
import { PendingPaymentComponent } from './main/pending-payment/pending-payment.component';
import { SidebarComponent } from './main/sidebar/sidebar.component';
import { SuccessPageComponent } from './main/success-page/success-page.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';


import { CaptchaService } from './common/captcha/captcha.service';
import { ApplyVisaEditComponent } from './main/apply-visa-extension/apply-visa-edit/apply-visa-edit.component';
import { ApplyVisaListComponent } from './main/apply-visa-extension/apply-visa-list/apply-visa-list.component';
import { ApplyVisaViewComponent } from './main/apply-visa-extension/apply-visa-view/apply-visa-view.component';
import { DashboardViewComponent } from './main/dashboard/dashboard-view/dashboard-view.component';
import { VisaOverstayComponent } from './main/visa-overstay/visa-overstay.component';
import { VisaoverstayViewComponent } from './visaoverstay-view/visaoverstay-view.component';
import { environment } from '../environments/environment';

defineLocale('fr', frLocale);
// AoT requires an exported function for factories
export function HttpLoaderFactory(httpClient: HttpClient) {
  // return new TranslateHttpLoader(httpClient);
  return new TranslateHttpLoader(httpClient, "./assets/i18n/", ".json");
}

@NgModule({
  declarations: [

    AppComponent,
    BaseComponent,
    LoginComponent,
    MainComponent,
    HeaderComponent,
    SidebarComponent,
    FooterComponent,
    DashboardComponent,
    NewDashboardComponent,
    HomeComponent,
    ManageApplicationsComponent,
    ApplicationsListComponent,
    ApplicationsDetailsComponent,
    VisaDetailsComponent,
    PaymentComponent,
    MyProfileComponent,
    BlockCopyPasteDirective,
    ApplicationPreviewComponent,
    FaqComponent,
    AddPaymentComponent,
    SuccessPageComponent,
    PaymentCompletedComponent,
    PendingPaymentComponent,
    PendingPaymentListComponent,
    PendingPaymentEditComponent,
    PaymentCompletedListComponent,
    PaymentCompletedEditComponent,
    ApplyVisaExtensionComponent,
    ErrorpageComponent,
    UpdatePasswordComponent,
    ApplyVisaListComponent,
    ApplyVisaEditComponent,
    ApplyVisaViewComponent,
    DashboardViewComponent,
    VisaOverstayComponent,
    VisaoverstayViewComponent,

  ],
  imports: [
    BrowserModule,
    NgSelectModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    NgxSpinnerModule,
    BrowserAnimationsModule,
    TabAllModule,
    // RecaptchaModule,
    // RecaptchaFormsModule,
    //RecaptchaV3Module,
    ModalModule.forRoot(),
    ToastrModule.forRoot(),
    TabsModule.forRoot(),
    PaginationModule.forRoot(),
    BsDatepickerModule.forRoot(),
    NgPopupsModule.forRoot(),
    TooltipModule.forRoot(),
    AccordionModule.forRoot(),

    // NgxCaptchaModule,
    NgxUiLoaderModule,
    NgImageSliderModule,
    ImageCropperModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [CanLoginActivate, CanAuthActivate, EnvServiceProvider, Broadcaster, DatePipe,
    /* {
      provide: RECAPTCHA_V3_SITE_KEY,
      useValue: environment.recaptcha.siteKey,
    }, */
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
