import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CheckoutPayload } from '../../../backend/classes/Payload/checkout/checkout-payload';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {

  private readonly apiUrl: string = 'http://localhost:8080/api/checkouts';

  constructor(private http: HttpClient) { }

  payAndReserve(checkoutPayload: CheckoutPayload) : Observable<any>{
    return this.http.post(`${this.apiUrl}/payAndReserve`, checkoutPayload);
  }

}
