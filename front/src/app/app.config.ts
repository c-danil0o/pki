import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import { provideAnimations } from '@angular/platform-browser/animations';
import {MessageService} from "primeng/api";
import {icpInterceptor} from "./icp.interceptor";

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes),
  provideHttpClient(withInterceptors([icpInterceptor])), provideAnimations(), MessageService]
};
