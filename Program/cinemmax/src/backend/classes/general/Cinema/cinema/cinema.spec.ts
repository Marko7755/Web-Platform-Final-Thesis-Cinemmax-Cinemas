import { Cinema } from './cinema';

describe('Cinema', () => {
  it('should create an instance', () => {
    expect(new Cinema('Test Location', 'Test Name')).toBeTruthy();
  });
});
