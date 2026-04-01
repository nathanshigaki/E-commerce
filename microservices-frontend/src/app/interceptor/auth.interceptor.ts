import {HttpInterceptorFn} from "@angular/common/http";
import {inject} from "@angular/core";
import {OidcSecurityService} from "angular-auth-oidc-client";
import { switchMap } from "rxjs";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(OidcSecurityService);

  return authService.getAccessToken().pipe(
    switchMap(token => {
      if (token) {
        const authReq = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
        return next(authReq);
      }
      return next(req);
    })
  );
}
