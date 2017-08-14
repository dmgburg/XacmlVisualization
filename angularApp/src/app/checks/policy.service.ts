import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import 'rxjs/add/operator/toPromise';

import {PolicyNode} from '../policy-node';

@Injectable()
export class PolicyService {
  baseUrl = 'http://localhost:8080';

  constructor(private http: Http) {
  }

  getDecisions(fromDate: Date, toDate: Date): Promise<PolicyNode[]> {
    return this.http.get(`${this.baseUrl}/decisions?fromDate=${fromDate.toISOString()}&toDate=${toDate.toISOString()}`)
      .toPromise()
      .then(responce => responce.json() as PolicyNode[])
      .catch(this.handleError);
  }

  getPolicy(name: String, version: String): Promise<PolicyNode[]> {
    return this.http.get(`${this.baseUrl}/policy?name=${name}&version=${version}`)
      .toPromise()
      .then(response => {
          console.log(response);
           return response.json() as PolicyNode[];
        }
      )
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
