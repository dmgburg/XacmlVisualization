import {TestBed, inject} from '@angular/core/testing';

import {ChecksService} from './checks.service';
import {HttpModule} from '@angular/http';

describe('ChecksService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpModule, ChecksService],
      imports: [HttpModule]
    });
  });

  it('should be created', inject([ChecksService], (service: ChecksService) => {
    service.getDecisions(new Date(2017, 6, 30, 1, 20, 0, 0), new Date(2017, 6, 30, 4, 20, 0, 0));
  }));
});
