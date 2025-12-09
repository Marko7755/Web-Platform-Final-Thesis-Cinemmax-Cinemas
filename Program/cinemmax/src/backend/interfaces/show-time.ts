export interface ShowTimeInterface {
  id: number;
  showTime: string; // "2025-08-22T19:30:00"
  basePrice: number;
  showType: { id: number; type: string; additionalPrice: number };
  hall: {
    id: number; number: number; capacity: number;
    cinema: { id: number; location: string; name: string };
  };
}


