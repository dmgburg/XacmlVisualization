import {TestBed, inject} from '@angular/core/testing';

import {PolicyService} from './policy.service';
import {HttpModule} from '@angular/http';

describe('PolicyService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpModule, PolicyService],
      imports: [HttpModule]
    });
  });

  it('should be created', inject([PolicyService], (service: PolicyService) => {
    service.getDecisions(new Date(2017, 6, 30, 1, 20, 0, 0), new Date(2017, 6, 30, 4, 20, 0, 0));
  }));
});
