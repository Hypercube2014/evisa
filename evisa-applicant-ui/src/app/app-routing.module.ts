import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { DashboardComponent } from './main/dashboard/dashboard.component';
import { NewDashboardComponent } from './main/new-dashboard/new-dashboard.component';
import { HomeComponent } from './home/home.component';
import { ManageApplicationsComponent } from './main/manage-applications/manage-applications.component';
import { ApplicationsListComponent } from './main/manage-applications/applications-list/applications-list.component';
import { ApplicationsDetailsComponent } from './main/manage-applications/applications-details/applications-details.component';
import { VisaDetailsComponent } from './main/manage-applications/visa-details/visa-details.component';
import { PaymentComponent } from './main/payment/payment.component';
import { MyProfileComponent } from './main/my-profile/my-profile.component';
import { ApplicationPreviewComponent } from './main/application-preview/application-preview.component';
import { CanLoginActivate, CanAuthActivate } from './common/auth.gaurd';
import { FaqComponent } from './faq/faq.component';
import { AddPaymentComponent } from './main/add-payment/add-payment.component';
import { SuccessPageComponent } from './main/success-page/success-page.component';
import { PendingPaymentComponent } from './main/pending-payment/pending-payment.component';
import { PendingPaymentListComponent } from './main/pending-payment/pending-payment-list/pending-payment-list.component';
import { PendingPaymentEditComponent } from './main/pending-payment/pending-payment-edit/pending-payment-edit.component';
import { PaymentCompletedComponent } from './main/payment-completed/payment-completed.component';
import { PaymentCompletedListComponent } from './main/payment-completed/payment-completed-list/payment-completed-list.component';
import { PaymentCompletedEditComponent } from './main/payment-completed/payment-completed-edit/payment-completed-edit.component';
import { ApplyVisaExtensionComponent } from './main/apply-visa-extension/apply-visa-extension.component';
import { ErrorpageComponent } from './main/errorpage/errorpage.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';
import { ApplyVisaListComponent } from './main/apply-visa-extension/apply-visa-list/apply-visa-list.component';
import { ApplyVisaEditComponent } from './main/apply-visa-extension/apply-visa-edit/apply-visa-edit.component';
import { ApplyVisaViewComponent } from './main/apply-visa-extension/apply-visa-view/apply-visa-view.component';
import { DashboardViewComponent } from './main/dashboard/dashboard-view/dashboard-view.component';
import {VisaOverstayComponent} from "./main/visa-overstay/visa-overstay.component";
import {VisaoverstayViewComponent} from "./visaoverstay-view/visaoverstay-view.component";
const routes: Routes = [
  { path: "", component: HomeComponent, pathMatch: "full" },
  {
    path: "home",
    component: HomeComponent,
    canActivate: [CanLoginActivate],
    pathMatch: "full"
  },
  {
    path: "error-page",
    component: ErrorpageComponent,
    pathMatch: "full"
  },
  {
    path: "faq",
    component: FaqComponent,
    canActivate: [CanLoginActivate],
    pathMatch: "full"
  },
  {
    path: "login",
    component: LoginComponent,
    canActivate: [CanLoginActivate],
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
    canActivate: [CanAuthActivate],
    children: [
      { path: "", redirectTo: "dashboard", pathMatch: "full" },
      {
        path: "dashboard",
        component: DashboardComponent,
        pathMatch: "full"
      },
      {
        path: "evisa-ordinary",
        component: DashboardComponent,
        pathMatch: "full"
      },
      {
        path: "viewdetails/:id",
        component: DashboardViewComponent,
        pathMatch: "full"
      },

      
      {
        path: "application-preview/:id",
        component: ApplicationPreviewComponent,
        pathMatch: "full"
      },
      {
        path: "my-profile",
        component: MyProfileComponent,
        pathMatch: "full"
      },
      {
        path: "payment",
        component: PaymentComponent,
        pathMatch: "full"
      },
      {
        path: "add-payment",
        component: AddPaymentComponent,
        pathMatch: "full"
      },
      {
        path: "success-payment",
        component: SuccessPageComponent,
        pathMatch: "full"
      },

        {
            path: "visa-overstay",
            component: VisaOverstayComponent,
            pathMatch: "full"
        },

        {
            path: "visaoverstay-view/:applicationNumber/:amount",
            component: VisaoverstayViewComponent,
            pathMatch: "full"
        },
      {
        path: "manage-applications",
        component: ManageApplicationsComponent,
        children: [
          { path: "", redirectTo: "apps-list", pathMatch: "full" },
          {
            path: "apps-list",
            component: ApplicationsListComponent,
            pathMatch: "full"
          },
          {
            path: "apps-details/:id",
            component: ApplicationsDetailsComponent,
            pathMatch: "full"
          },
          {
            path: "visa-details/:id",
            component: VisaDetailsComponent,
            pathMatch: "full"
          }

        ]
      },
      {
        path: 'pending-payment',
        component: PendingPaymentComponent,
        children: [
          { path: '', redirectTo: 'pending-list', pathMatch: 'full' },
          {
            path: 'pending-list',
            component: PendingPaymentListComponent,
            pathMatch: 'full'
          },
          {
            path: 'pending-edit/:id',
            component: PendingPaymentEditComponent,
            pathMatch: 'full'
          }
        ]
      },

      {
        path: "evisa-extension",
        component: ApplyVisaExtensionComponent,
        children: [
          { path: '', redirectTo: 'apply-visa-list', pathMatch: 'full' },
          {
            path: 'apply-visa-list',
            component: ApplyVisaListComponent,
            pathMatch: 'full'
          },
          {
            path: 'apply-visa-edit/:id',
            component: ApplyVisaEditComponent,
            pathMatch: 'full'
          },
          {
            path: 'apply-visa-view/:id',
            component: ApplyVisaViewComponent,
            pathMatch: 'full'
          }
        ]
      },

      {
        path: 'payment-completed',
        component: PaymentCompletedComponent,
        children: [
          { path: '', redirectTo: 'payment-list', pathMatch: 'full' },
          {
            path: 'payment-list',
            component: PaymentCompletedListComponent,
            pathMatch: 'full'
          },
          {
            path: 'payment-edit/:id',
            component: PaymentCompletedEditComponent,
            pathMatch: 'full'
          }
        ]
      }
    ]
  },

  { path: "**", redirectTo: "/home", pathMatch: "full" },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true, scrollPositionRestoration: 'enabled', relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
