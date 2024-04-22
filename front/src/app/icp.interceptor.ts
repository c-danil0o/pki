import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, throwError} from "rxjs";
import {MessageService} from "primeng/api";
import {inject} from "@angular/core";
export const icpInterceptor: HttpInterceptorFn = (req, next) => {
  let messageService = inject(MessageService)
  let showToast = function (message: string){
    messageService.add({
      severity: 'error',
      summary: 'Error message',
      key: 'bc',
      detail: message,
      life: 2000,
    })
  }
  return next(req).pipe(
    catchError((err: any) => {
      console.log(err)
      if (err instanceof HttpErrorResponse) {
        if (err.status === 401) {
          // Specific handling for unauthorized errors
          console.error('Unauthorized request:', err);
          // You might trigger a re-authentication flow or redirect the user here
        } else if (err.status === 418)  {
          console.log("teapot")
          if (err.error && err.error[0] === '{'){
            showToast(JSON.parse(err.error).message)
          }else{
            showToast(err.error.message)
          }
          return throwError(err)
        }else{

          console.error('HTTP error:', err);
        }
      } else {
        // Handle non-HTTP errors
        console.error('An error occurred:', err);
      }

      // Re-throw the error to propagate it further
      return throwError(() => err);
    })
  );
};
