import { SeatPayload } from "./seatPayload";

describe('Seat', () => {
  it('should create an instance', () => {
    expect(new SeatPayload(1, 'A', 'B', 10)).toBeTruthy();
  });
});
