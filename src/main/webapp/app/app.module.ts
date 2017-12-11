import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ngx-webstorage';

import { JhipsterCassandraSampleApplicationSharedModule, UserRouteAccessService } from './shared';
import { JhipsterCassandraSampleApplicationAppRoutingModule} from './app-routing.module';
import { JhipsterCassandraSampleApplicationHomeModule } from './home/home.module';
import { JhipsterCassandraSampleApplicationAdminModule } from './admin/admin.module';
import { JhipsterCassandraSampleApplicationAccountModule } from './account/account.module';
import { JhipsterCassandraSampleApplicationEntityModule } from './entities/entity.module';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        JhipsterCassandraSampleApplicationAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        JhipsterCassandraSampleApplicationSharedModule,
        JhipsterCassandraSampleApplicationHomeModule,
        JhipsterCassandraSampleApplicationAdminModule,
        JhipsterCassandraSampleApplicationAccountModule,
        JhipsterCassandraSampleApplicationEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class JhipsterCassandraSampleApplicationAppModule {}
