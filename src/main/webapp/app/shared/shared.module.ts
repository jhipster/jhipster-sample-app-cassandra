import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {
  JhipsterCassandraSampleApplicationSharedLibsModule,
  JhipsterCassandraSampleApplicationSharedCommonModule,
  JhiLoginModalComponent,
  HasAnyAuthorityDirective
} from './';

@NgModule({
  imports: [JhipsterCassandraSampleApplicationSharedLibsModule, JhipsterCassandraSampleApplicationSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [JhipsterCassandraSampleApplicationSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterCassandraSampleApplicationSharedModule {
  static forRoot() {
    return {
      ngModule: JhipsterCassandraSampleApplicationSharedModule
    };
  }
}
