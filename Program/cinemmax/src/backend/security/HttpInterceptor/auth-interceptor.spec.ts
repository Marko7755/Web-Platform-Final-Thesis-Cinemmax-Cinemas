import { AuthInterceptor } from './auth-interceptor';

describe('HttpInterceptor', () => {
  it('should create an instance', () => {
    // Create a mock AuthService with just the methods you need
    const mockAuthService = {
      getAccessToken: () => 'mock-token',
      refreshAccessToken: () => { /* mock implementation or empty */ }
    } as any; // 'as any' to satisfy TS for minimal mock

    // Create a mock Router with minimal implementation
    const mockRouter = {} as any;

    const interceptor = new AuthInterceptor(mockAuthService, mockRouter);

    expect(interceptor).toBeTruthy();
  });
});
