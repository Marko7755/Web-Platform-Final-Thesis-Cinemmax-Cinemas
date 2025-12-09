import { HttpInterceptor, HttpInterceptorFn } from '@angular/common/http';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, catchError, filter, Observable, of, switchMap, throwError, take, EMPTY } from "rxjs";
import { AuthService } from "../../services/auth.service";
import { Router } from '@angular/router';
import { NotificationService } from '../../../frontend/services/notification/notification.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    /*
        [1] Login
        ↓
        Backend → (accessToken in body, refreshToken in HttpOnly cookie)
        ↓
        Frontend stores accessToken in localStorage

        [2] Request to protected API
        ↓
        accessToken in Authorization header

        [3] accessToken expires → 401
        ↓
        Interceptor → refreshAccessToken() (refreshToken in cookie)
        ↓
        Backend validates cookie, returns new accessToken
        ↓
        Interceptor retries original request
    */

    /*
    If a request fails with 401, we check if a refresh is in progress.

    If not, we call refreshAccessToken() and update the token in a BehaviorSubject.

    If yes, other requests wait for that token using filter(token => token != null).

    All pending requests retry with the new access token.
     */

    private isRefreshing = false;
    private refreshTokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
    constructor(private authService: AuthService, private notificationService: NotificationService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const accessToken = this.authService.getAccessToken();
        const isExcludedUrl = req.url.includes('/login') || req.url.includes('/refreshAccessToken') || req.url.includes('/register');

        //req is immutable so we had to clone it to be able to change its properties
        //when a user gets an 401 error(exluding login and refreshAccessToken URL-s), we automatically try to refresh the access token
        const cloned = (!isExcludedUrl && accessToken)
            ? this.addToken(req, accessToken)
            : req;


        return next.handle(cloned).pipe(
            catchError((error: HttpErrorResponse) => {
                const isExcludedUrl = req.url.includes('/login') || req.url.includes('/refreshAccessToken') || req.url.includes('/register');
                const isServerDown = error.status === 0 || [502, 503, 504].includes(error.status);

                if (isServerDown) {
                    this.notificationService.warningNotification(
                        'Server error',
                        'Server is not available at the moment, please try again later.'
                    );

                    // Swallow for ALL methods (GET/POST/PUT/DELETE)
                    return EMPTY; // completes; no error propagates to subscribers
                }


                if (error.status === 401 && !isExcludedUrl) {
                    return this.handle401Error(req, next);
                }

                if (error.status === 403) {
                    const msg =
                        (typeof error.error === 'object' && error.error?.message) ? error.error.message :
                            (typeof error.error === 'string' ? error.error : null) ||
                            'You do not have permission to perform this action.';
                    this.notificationService.errorNotification('Forbidden', msg);
                    // You can choose to swallow 403 too, but most apps rethrow:
                    return throwError(() => error);
                }

                return throwError(() => error);
            })
        );

    }

    private addToken(request: HttpRequest<any>, token: string): HttpRequest<any> {
        return request.clone({
            headers: request.headers.set('Authorization', `Bearer ${token}`)
        });
    }

    private handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this.isRefreshing) {
            this.isRefreshing = true;
            this.refreshTokenSubject.next(null); // reset subject

            return this.authService.refreshAccessToken().pipe(
                switchMap(() => {
                    const newToken = this.authService.getAccessToken();
                    if (!newToken) {
                        throw new Error('No new access token returned');
                    }
                    console.log("New access token from interceptor");
                    this.refreshTokenSubject.next(newToken); // notify waiting requests
                    this.isRefreshing = false;

                    return next.handle(this.addToken(request, newToken));
                }),
                catchError(err => {
                    this.isRefreshing = false;
                    // fallback if logout endpoint fails
                    this.authService.localLogout(); // helper method that removes only access token
                    return throwError(() => err);
                })
            );
        } else {
            // Wait until the token is refreshed and retry the request
            return this.refreshTokenSubject.pipe(
                filter(token => token != null),
                take(1),
                switchMap((token) => next.handle(this.addToken(request, token!)))
            );
        }
    }


    /*
           //when a user gets an 401 error(exluding login and refreshAccessToken URL-s), we automatically try to refresh the access token
           return next.handle(cloned).pipe(
               catchError((error: HttpErrorResponse) => {
                   if(error.status === 401 && !isExcludedUrl) {
                       this.isRefreshing = true;
   
                       return this.authService.refreshAccessToken().pipe(
                           switchMap(() => {
                               this.isRefreshing = false;
                               const newAccessToken = this.authService.getAccessToken();
                               const retryRequest = req.clone({
                                   headers: req.headers.set('Authorization', `Bearer ${newAccessToken}`)
                               });
                               return next.handle(retryRequest);
                           }),
                           catchError(() => {
                               this.isRefreshing = false;
                               return this.authService.logout().pipe(
                                   catchError(() => {
                                   // fallback if logout endpoint fails
                                   this.authService.localLogout(); // helper method that removes only access token
                                   return of(null);
                                   }),
                                   switchMap(() => {
                                   return throwError(() => error);
                                   })
                               );
                           })
                       )
                   }
                   return throwError(() => error);
               })
           )
           */




}



