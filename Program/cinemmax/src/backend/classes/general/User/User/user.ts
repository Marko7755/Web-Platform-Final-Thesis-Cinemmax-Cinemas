export class User {
    public role: string = 'user';

    constructor(private name: string, private surname: string, private username: string, private email: string, 
        private password: string, private passwordConfirmation: string) {}
}

