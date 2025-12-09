import { JwtResponseDTO } from '../JwtResponse/jwt-response-dto';

describe('JwtResponseDTO', () => {
  it('should create an instance', () => {
    expect(new JwtResponseDTO('test-token')).toBeTruthy();
  });
});
