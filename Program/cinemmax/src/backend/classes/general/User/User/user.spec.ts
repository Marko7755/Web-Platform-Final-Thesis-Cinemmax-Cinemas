import { User } from './user';

describe('User', () => {
  it('should create an instance', () => {
    expect(new User('TestName', 'TestSurname', 'TestUsername', 'test@email.com', 'password123', 'password123')).toBeTruthy();
  });
});
