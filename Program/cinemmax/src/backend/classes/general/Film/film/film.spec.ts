import { Film } from "./film";

describe('Film', () => {
  it('should create an instance', () => {
    expect(new Film('Test Name', 'Test Genre', 'Test About')).toBeTruthy();
  });
});
