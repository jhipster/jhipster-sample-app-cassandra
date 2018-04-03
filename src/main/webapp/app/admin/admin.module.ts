import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { JhipsterCassandraSampleApplicationSharedModule } from 'app/shared';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

import {
  adminState,
  UserDeleteDialogComponent,
  UserMgmtComponent,
  UserMgmtDetailComponent,
  UserMgmtUpdateComponent,
  UserMgmtDeleteDialogComponent,
  LogsComponent,
  JhiMetricsMonitoringModalComponent,
  JhiMetricsMonitoringComponent,
  JhiHealthModalComponent,
  JhiHealthCheckComponent,
  JhiConfigurationComponent,
  JhiDocsComponent,
  JhiConfigurationService,
  JhiHealthService,
  JhiMetricsService,
  LogsService,
  UserResolvePagingParams,
  UserMgmtResolve,
  UserResolve
} from './';

@NgModule({
  imports: [
    JhipsterCassandraSampleApplicationSharedModule,
    RouterModule.forChild(adminState)
    /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
  ],
  declarations: [
    UserDeleteDialogComponent,
    UserMgmtComponent,
    UserMgmtDetailComponent,
    UserMgmtUpdateComponent,
    UserMgmtDeleteDialogComponent,
    LogsComponent,
    JhiConfigurationComponent,
    JhiHealthCheckComponent,
    JhiHealthModalComponent,
    JhiDocsComponent,
    JhiMetricsMonitoringComponent,
    JhiMetricsMonitoringModalComponent
  ],
  entryComponents: [UserMgmtUpdateComponent, UserMgmtDeleteDialogComponent, JhiHealthModalComponent, JhiMetricsMonitoringModalComponent],
  providers: [
    JhiConfigurationService,
    JhiHealthService,
    JhiMetricsService,
    LogsService,
    UserResolvePagingParams,
    UserResolve,
    UserMgmtResolve
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterCassandraSampleApplicationAdminModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => this.languageService.changeLanguage(languageKey));
  }
}
