import { TestBed } from '@angular/core/testing';

import { ShowTypeService } from './show-type.service';

describe('ShowTypeService', () => {
  let service: ShowTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShowTypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
