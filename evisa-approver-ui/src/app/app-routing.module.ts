import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
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

import { CanLoginActivate, CanAuthActivate } from './common/auth.gaurd';
import { CodesViewComponent } from './main/code-maintenance/codes-view/codes-view.component';
import { FaqViewComponent } from './main/faq/faq-view/faq-view.component';
import { ChangePasswordComponent } from './main/change-password/change-password.component';
import { MyProfileComponent } from './main/my-profile/my-profile.component';
import { ManageVisaComponent } from './main/manage-visa/manage-visa.component';
import { VisaListComponent } from './main/manage-visa/visa-list/visa-list.component';
import { VisaDetailComponent } from './main/manage-visa/visa-detail/visa-detail.component';
import { VisaViewComponent } from './main/manage-visa/visa-view/visa-view.component';
import { RoleViewComponent } from './main/role-management/role-view/role-view.component';
import { ProductViewComponent } from './main/product-configuration/product-view/product-view.component';
import { UserViewComponent } from './main/manage-user/user-view/user-view.component';
import { ManageMenuComponent } from './main/manage-menu/manage-menu.component';
import { MenuListComponent } from './main/manage-menu/menu-list/menu-list.component';
import { MenuDetailsComponent } from './main/manage-menu/menu-details/menu-details.component';
import { MenuViewComponent } from './main/manage-menu/menu-view/menu-view.component';
import { ManageModuleComponent } from './main/manage-module/manage-module.component';
import { ModuleListComponent } from './main/manage-module/module-list/module-list.component';
import { ModuleDetailComponent } from './main/manage-module/module-detail/module-detail.component';
import { ModuleViewComponent } from './main/manage-module/module-view/module-view.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';
import { EmployeeManagementComponent } from './main/employee-management/employee-management.component';
import { EmployeeListComponent } from './main/employee-management/employee-list/employee-list.component';
import { EmployeeDetailComponent } from './main/employee-management/employee-detail/employee-detail.component';
import { EmployeeViewComponent } from './main/employee-management/employee-view/employee-view.component';
import { HomeComponent } from './home/home.component';
import { EvisaBorderProcessingComponent } from './main/evisa-border-processing/evisa-border-processing.component';
import { ExitBcoComponent } from './main/exit-bco/exit-bco.component';
import { SearchDepartureeVisaComponent } from './main/search-departuree-visa/search-departuree-visa.component';
import { ErrorPageComponent } from './error-page/error-page.component';
import { DashboardImoComponent } from './main/dashboard-imo/dashboard-imo.component';
import { SearchExtensionVisaComponent } from './main/search-extension-visa/search-extension-visa.component';
import { ExtensionEvisaBorderProcessComponent } from './main/extension-evisa-border-process/extension-evisa-border-process.component';
import { EvisaSearchApplicationComponent } from './main/evisa-search-application/evisa-search-application.component';
import {OverstayListComponent} from "./main/overstay-list/overstay-list.component";
import {OverstayDetailsComponent} from "./main/overstay-list/overstay-details/overstay-details.component";
import { EvisaSearchDmComponent } from './main/evisa-search-dm/evisa-search-dm.component';
import { VisaDownloadComponent } from './main/visa-download/visa-download.component';

const routes: Routes = [
  { path: "", redirectTo: "home", pathMatch: "full" },
  {
    path: "home",
    component: HomeComponent,
    canActivate: [CanLoginActivate],
    pathMatch: "full"
  },
  {
    path: "error-page",
    component: ErrorPageComponent,
    pathMatch: "full"
  },
  {
    path: "login",
    component: LoginComponent,
    pathMatch: "full"
  },
  {
    path: "update-password",
    component: UpdatePasswordComponent,
    pathMatch: "full"
  },
  {
    path: "main",
    component: MainComponent,
    children: [
      { path: "", redirectTo: "dashboard", pathMatch: "full" },
      {
        path: "dashboard",
        component: DashboardComponent,
        pathMatch: "full"
      },
      {
        path: "dashboardimo",
        component: DashboardImoComponent,
        pathMatch: "full"
      },
      {
        path: "evisasearch",
        component: EvisaSearchComponent,
        pathMatch: "full",
      },
      {
        path: "evisasearchdm",
        component: EvisaSearchDmComponent,
        pathMatch: "full",
      },
      {
        path: "visa-download",
        component: VisaDownloadComponent,
        pathMatch: "full",
      },
      {
        path: "extensionevisasearch",
        component: SearchExtensionVisaComponent,
        pathMatch: "full",
      },
      {
        path: "overstayList",
        component: OverstayListComponent,
        pathMatch: "full",
      },
      {
        path: "overstayDetails/:applicationNumber/:amount",
        component: OverstayDetailsComponent,
        pathMatch: "full",
      },
      {
        path: "searchApplication",
        component: EvisaSearchApplicationComponent,
        pathMatch: "full",
      },
      {
        path: "search-departure",
        component: SearchDepartureeVisaComponent,
        pathMatch: "full",
      },
      {
        path: "evisaborderprocess/:id",
        component: EvisaBorderProcessingComponent,
        pathMatch: "full"
      },
      {
        path: "extensionevisaprocess/:id/:details",
        component: ExtensionEvisaBorderProcessComponent,
        pathMatch: "full"
      },
      {
        path: "detach",
        component: DetachApplicationsComponent,
        pathMatch: "full"
      },
      {
        path: "suspend",
        component: SuspendAgentComponent,
        pathMatch: "full"
      },
      {
        path: "perfomance-reports",
        component: PerfomanceReportsComponent,
        pathMatch: "full"
      },
      {
        path: "process-reports",
        component: ProcessReportsComponent,
        pathMatch: "full"
      },
      {
        path: "agent-tracker",
        component: AgentTrackerComponent,
        pathMatch: "full"
      },
      {
        path: "transfer-agent",
        component: TransferAgentComponent,
        pathMatch: "full"
      },
      {
        path: "assign-agent",
        component: AssignAgentComponent,
        pathMatch: "full"
      },
      {
        path: "manage",
        component: ManageAgentsComponent,
        children: [
          { path: "", redirectTo: "agent-list", pathMatch: "full" },
          {
            path: "agent-list",
            component: AgentsListComponent,
            pathMatch: "full"
          },
          {
            path: "agent-detail/:id",
            component: AgentsDetailsComponent,
            pathMatch: "full"
          },
        ]
      },
      {
        path: "product-config",
        component: ProductConfigurationComponent,
        children: [
          { path: "", redirectTo: "product-list", pathMatch: "full" },
          {
            path: "product-list",
            component: ProductListComponent,
            pathMatch: "full"
          },
          {
            path: "product-detail/:id",
            component: ProductDetailComponent,
            pathMatch: "full"
          }, {
            path: "product-view/:id",
            component: ProductViewComponent,
            pathMatch: "full"
          }
        ]
      },
      {
        path: "code-maintenance",
        component: CodeMaintenanceComponent,
        children: [
          { path: "", redirectTo: "codes-list", pathMatch: "full" },
          {
            path: "codes-list",
            component: CodesListComponent,
            pathMatch: "full"
          },
          {
            path: "codes-detail/:id",
            component: CodesDetailComponent,
            pathMatch: "full"
          },
          {
            path: "codes-view/:id",
            component: CodesViewComponent,
            pathMatch: "full"
          }
        ]
      },
      {
        path: "manage-user",
        component: ManageUserComponent,
        children: [
          { path: "", redirectTo: "user-list", pathMatch: "full" },
          {
            path: "user-list",
            component: UserListComponent,
            pathMatch: "full"
          },
          {
            path: "user-detail/:id",
            component: UserDetailComponent,
            pathMatch: "full"
          }, {
            path: "user-view/:id",
            component: UserViewComponent,
            pathMatch: "full"
          },
        ]
      },
      {
        path: "role-mgnt",
        component: RoleManagementComponent,
        children: [
          { path: "", redirectTo: "role-list", pathMatch: "full" },
          {
            path: "role-list",
            component: RoleListComponent,
            pathMatch: "full"
          },
          {
            path: "role-detail/:id",
            component: RoleDetailComponent,
            pathMatch: "full"
          }, {
            path: "role-view/:id",
            component: RoleViewComponent,
            pathMatch: "full"
          },
        ]
      },
      {
        path: "faq",
        component: FAQComponent,
        children: [
          { path: "", redirectTo: "faq-list", pathMatch: "full" },
          {
            path: "faq-list",
            component: FaqListComponent,
            pathMatch: "full"
          },
          {
            path: "faq-detail/:id",
            component: FaqDetailComponent,
            pathMatch: "full"
          },
          {
            path: "faq-view/:id",
            component: FaqViewComponent,
            pathMatch: "full"
          },
        ]
      },

      {
        path: "menu",
        component: ManageMenuComponent,
        children: [
          { path: "", redirectTo: "menu-list", pathMatch: "full" },
          {
            path: "menu-list",
            component: MenuListComponent,
            pathMatch: "full"
          },
          {
            path: "menu-detail/:id",
            component: MenuDetailsComponent,
            pathMatch: "full"
          },
          {
            path: "menu-view/:id",
            component: MenuViewComponent,
            pathMatch: "full"
          },
        ]
      },

      {
        path: "module",
        component: ManageModuleComponent,
        children: [
          { path: "", redirectTo: "module-list", pathMatch: "full" },
          {
            path: "module-list",
            component: ModuleListComponent,
            pathMatch: "full"
          },
          {
            path: "module-detail/:id",
            component: ModuleDetailComponent,
            pathMatch: "full"
          },
          {
            path: "module-view/:id",
            component: ModuleViewComponent,
            pathMatch: "full"
          },
        ]
      },

      {
        path: "visa",
        component: ManageVisaComponent,
        children: [
          { path: "", redirectTo: "visa-list", pathMatch: "full" },
          {
            path: "visa-list",
            component: VisaListComponent,
            pathMatch: "full"
          },
          {
            path: "visa-detail/:id",
            component: VisaDetailComponent,
            pathMatch: "full"
          },
          {
            path: "visa-view/:id",
            component: VisaViewComponent,
            pathMatch: "full"
          },
        ]
      },
      {
        path: "employee-mgnt",
        component: EmployeeManagementComponent,
        children: [
          { path: "", redirectTo: "employee-list", pathMatch: "full" },
          {
            path: "employee-list",
            component: EmployeeListComponent,
            pathMatch: "full"
          },
          {
            path: "employee-detail/:id",
            component: EmployeeDetailComponent,
            pathMatch: "full"
          },
          {
            path: "employee-view/:id",
            component: EmployeeViewComponent,
            pathMatch: "full"
          },
        ]
      },

      {
        path: "change-password",
        component: ChangePasswordComponent,
        pathMatch: "full"

      },

      {
        path: "my-profile",
        component: MyProfileComponent,
        pathMatch: "full"

      },
      {
        path: 'exit-bco/:id',
        component: ExitBcoComponent,
        pathMatch: 'full'
      }

    ]
  },
  { path: "**", redirectTo: "/", pathMatch: "full" }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true, scrollPositionRestoration: 'enabled' })],
  exports: [RouterModule]

})
export class AppRoutingModule { }
