import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ImageCropperModule } from 'ngx-image-cropper';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BaseComponent } from './common/commonComponent';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { HeaderComponent } from './main/header/header.component';
import { SidebarComponent } from './main/sidebar/sidebar.component';
import { FooterComponent } from './main/footer/footer.component';
import { DashboardComponent } from './main/dashboard/dashboard.component';
import { EvisaSearchComponent } from './main/evisa-search/evisa-search.component';
import { DetachApplicationsComponent } from './main/detach-applications/detach-applications.component';
import { SuspendAgentComponent } from './main/suspend-agent/suspend-agent.component';
import { ManageAgentsComponent } from './main/manage-agents/manage-agents.component';
import { AgentsListComponent } from './main/manage-agents/agents-list/agents-list.component';
import { AgentsDetailsComponent } from './main/manage-agents/agents-details/agents-details.component';
import { PerfomanceReportsComponent } from './main/perfomance-reports/perfomance-reports.component';
import { ProcessReportsComponent } from './main/process-reports/process-reports.component';
import { AgentTrackerComponent } from './main/agent-tracker/agent-tracker.component';
import { CodeMaintenanceComponent } from './main/code-maintenance/code-maintenance.component';
import { CodesListComponent } from './main/code-maintenance/codes-list/codes-list.component';
import { CodesDetailComponent } from './main/code-maintenance/codes-detail/codes-detail.component';
import { ProductConfigurationComponent } from './main/product-configuration/product-configuration.component';
import { ProductListComponent } from './main/product-configuration/product-list/product-list.component';
import { ProductDetailComponent } from './main/product-configuration/product-detail/product-detail.component';
import { ManageUserComponent } from './main/manage-user/manage-user.component';
import { UserListComponent } from './main/manage-user/user-list/user-list.component';
import { UserDetailComponent } from './main/manage-user/user-detail/user-detail.component';
import { RoleManagementComponent } from './main/role-management/role-management.component';
import { RoleListComponent } from './main/role-management/role-list/role-list.component';
import { RoleDetailComponent } from './main/role-management/role-detail/role-detail.component';
import { TransferAgentComponent } from './main/transfer-agent/transfer-agent.component';
import { AssignAgentComponent } from './main/assign-agent/assign-agent.component';
import { FAQComponent } from './main/faq/faq.component';
import { FaqListComponent } from './main/faq/faq-list/faq-list.component';
import { FaqDetailComponent } from './main/faq/faq-detail/faq-detail.component';


import { EnvServiceProvider } from './common/env.service.provider';
import { Broadcaster } from "./common/BroadCaster";
import { NgPopupsModule } from 'ng-popups';




import { ModalModule } from 'ngx-bootstrap/modal';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ToastrModule } from 'ng6-toastr-notifications';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { NgxSpinnerModule } from "ngx-spinner";
import { TabAllModule } from '@syncfusion/ej2-angular-navigations';
import { NgxDualListboxModule } from 'ngx-dual-listbox';


import { CanLoginActivate, CanAuthActivate } from './common/auth.gaurd';
import { CodesViewComponent } from './main/code-maintenance/codes-view/codes-view.component';

import { FaqViewComponent } from './main/faq/faq-view/faq-view.component';
import { ManageVisaComponent } from './main/manage-visa/manage-visa.component';
import { VisaDetailComponent } from './main/manage-visa/visa-detail/visa-detail.component';
import { VisaListComponent } from './main/manage-visa/visa-list/visa-list.component';
import { VisaViewComponent } from './main/manage-visa/visa-view/visa-view.component';

import { ChangePasswordComponent } from './main/change-password/change-password.component';
import { MyProfileComponent } from './main/my-profile/my-profile.component';
import { ManageMenuComponent } from './main/manage-menu/manage-menu.component';
import { MenuDetailsComponent } from './main/manage-menu/menu-details/menu-details.component';
import { MenuListComponent } from './main/manage-menu/menu-list/menu-list.component';
import { MenuViewComponent } from './main/manage-menu/menu-view/menu-view.component';
import { ManageModuleComponent } from './main/manage-module/manage-module.component'
import { ModuleListComponent } from './main/manage-module/module-list/module-list.component';
import { ModuleDetailComponent } from './main/manage-module/module-detail/module-detail.component';
import { ModuleViewComponent } from './main/manage-module/module-view/module-view.component';

import { RoleViewComponent } from './main/role-management/role-view/role-view.component';

import { ProductViewComponent } from './main/product-configuration/product-view/product-view.component';

import { UserViewComponent } from './main/manage-user/user-view/user-view.component';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

// AoT requires an exported function for factories
export function HttpLoaderFactory(httpClient: HttpClient) {
  // return new TranslateHttpLoader(httpClient);
  return new TranslateHttpLoader(httpClient, "./assets/i18n/", ".json");
}

import { NgxUiLoaderModule } from "ngx-ui-loader";
import { UpdatePasswordComponent } from './update-password/update-password.component';

import { EmployeeManagementComponent } from './main/employee-management/employee-management.component';
import { EmployeeViewComponent } from './main/employee-management/employee-view/employee-view.component';
import { EmployeeDetailComponent } from './main/employee-management/employee-detail/employee-detail.component';
import { EmployeeListComponent } from './main/employee-management/employee-list/employee-list.component';

import { BlockCopyPasteDirective } from './common/block-copy-paste.directive';
import { HomeComponent } from './home/home.component';
import { EvisaBorderProcessingComponent } from './main/evisa-border-processing/evisa-border-processing.component';

import { HighchartsChartModule } from 'highcharts-angular';

import { ImageViewerModule } from '@hallysonh/ngx-imageviewer';
import { PinchZoomModule } from 'ngx-pinch-zoom';

import { IMAGEVIEWER_CONFIG, ImageViewerConfig } from '@hallysonh/ngx-imageviewer';
import { ExitBcoComponent } from './main/exit-bco/exit-bco.component';
import { SearchDepartureeVisaComponent } from './main/search-departuree-visa/search-departuree-visa.component';
import { ErrorPageComponent } from './error-page/error-page.component';
import { DashboardImoComponent } from './main/dashboard-imo/dashboard-imo.component';
import { SortDirective } from './common/directive/sort.directive';
import { SearchExtensionVisaComponent } from './main/search-extension-visa/search-extension-visa.component';
import { ExtensionEvisaBorderProcessComponent } from './main/extension-evisa-border-process/extension-evisa-border-process.component';
import { EvisaSearchApplicationComponent } from './main/evisa-search-application/evisa-search-application.component';
import { OverstayListComponent } from './main/overstay-list/overstay-list.component';
import { OverstayDetailsComponent } from './main/overstay-list/overstay-details/overstay-details.component';
import { EvisaSearchDmComponent } from './main/evisa-search-dm/evisa-search-dm.component';
import { VisaDownloadComponent } from './main/visa-download/visa-download.component';


const MY_IMAGEVIEWER_CONFIG: ImageViewerConfig = {
  width: 800, // component default width
  height: 600, // component default height
  bgStyle: '#ECEFF1', // component background style
  scaleStep: 0, // zoom scale step (using the zoom in/out buttons)
  rotateStepper: false, // touch rotate should rotate only 90 to 90 degrees
  loadingMessage: 'Loading...',
  buttonStyle: {
    iconFontFamily: 'Material Icons', // font used to render the button icons
    alpha: 0, // buttons' transparence value
    hoverAlpha: 0, // buttons' transparence value when mouse is over
    bgStyle: '#000000', //  buttons' background style
    iconStyle: '#ffffff', // buttons' icon colors
    borderStyle: '#000000', // buttons' border style
    borderWidth: 0, // buttons' border width (0 == disabled)
  },
  tooltips: {
    bgAlpha: 0, // tooltip background transparence
    textAlpha: 0,
    enabled: false, // enable or disable tooltips for buttons
  },
  zoomOutButton: {
    show: false, // used to show/hide the button
  },

  // shorter button configuration style
  nextPageButton: {
    show: false, // used to show/hide the button
  },
  beforePageButton: {
    show: false, // used to show/hide the button
  },
  zoomInButton: {
    show: false, // used to show/hide the button
  },
  rotateLeftButton: {
    show: false, // used to show/hide the button
  },
  rotateRightButton: {
    show: false, // used to show/hide the button
  },
  resetButton: {
    show: false, // used to show/hide the button
  }
};



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
    EvisaSearchComponent,

    DetachApplicationsComponent,
    SuspendAgentComponent,
    ManageAgentsComponent,
    AgentsListComponent,
    AgentsDetailsComponent,
    PerfomanceReportsComponent,
    ProcessReportsComponent,
    AgentTrackerComponent,
    CodeMaintenanceComponent,
    CodesListComponent,
    CodesDetailComponent,
    ProductConfigurationComponent,
    ProductListComponent,
    ProductDetailComponent,
    ManageUserComponent,
    UserListComponent,
    UserDetailComponent,
    RoleManagementComponent,
    RoleListComponent,
    RoleDetailComponent,
    TransferAgentComponent,
    AssignAgentComponent,
    FAQComponent,
    FaqListComponent,
    FaqDetailComponent,
    FaqViewComponent,
    ManageVisaComponent,
    VisaDetailComponent,
    VisaListComponent,
    VisaViewComponent,
    CodesViewComponent,
    ChangePasswordComponent,
    MyProfileComponent,
    ManageMenuComponent,
    RoleViewComponent,
    MenuDetailsComponent,
    MenuListComponent,
    MenuViewComponent,
    ManageModuleComponent,
    ModuleListComponent,
    ModuleDetailComponent,
    ModuleViewComponent,
    ProductViewComponent,
    UserViewComponent,
    UpdatePasswordComponent,
    EmployeeManagementComponent,
    EmployeeDetailComponent,
    EmployeeViewComponent,
    EmployeeListComponent,
    BlockCopyPasteDirective,
    HomeComponent,
    EvisaBorderProcessingComponent,
    ExitBcoComponent,
    SearchDepartureeVisaComponent,
    ErrorPageComponent,
    DashboardImoComponent,
    SortDirective,
    SearchExtensionVisaComponent,
    ExtensionEvisaBorderProcessComponent,
    EvisaSearchApplicationComponent,
    OverstayListComponent,
    OverstayDetailsComponent,
    EvisaSearchDmComponent,
    VisaDownloadComponent
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
    ModalModule.forRoot(),
    ToastrModule.forRoot(),
    TabsModule.forRoot(),
    PaginationModule.forRoot(),
    BsDatepickerModule.forRoot(),
    NgPopupsModule.forRoot(),
    NgxDualListboxModule.forRoot(),
    ImageCropperModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    NgxUiLoaderModule,
    HighchartsChartModule,
    ImageViewerModule,
    PinchZoomModule,
  ],


  providers: [CanLoginActivate, CanAuthActivate, EnvServiceProvider, Broadcaster,
    {
      provide: IMAGEVIEWER_CONFIG,
      useValue: MY_IMAGEVIEWER_CONFIG
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
