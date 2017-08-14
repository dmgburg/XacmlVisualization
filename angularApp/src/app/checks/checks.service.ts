import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/toPromise';

import {PolicyNode} from '../policy-node';

@Injectable()
export class ChecksService {
  baseUrl = 'http://localhost:8080';

  constructor(private http: Http) {
  }

  getDecisions(fromDate: Date, toDate: Date): Promise<PolicyNode[]> {
    return this.http.get(`${this.baseUrl}/decisions?fromDate=${fromDate.toISOString()}&toDate=${toDate.toISOString()}`)
      .toPromise()
      .then(responce => responce.json().data as PolicyNode[])
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
