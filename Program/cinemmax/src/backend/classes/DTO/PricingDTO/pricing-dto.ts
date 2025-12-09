export class PricingDTO {
    constructor(
        public base: number,
        public additional: number,
        public finalPrice: number
    ) {}
}
