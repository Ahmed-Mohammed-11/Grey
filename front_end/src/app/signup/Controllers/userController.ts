const url = 'http://localhost:3000/';


// UserController.js

class UserController {
    async sendCredentials(credentials : any) {
        try {
            const endpoint = '/api/signup';

            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(credentials),
            });

            if (response.ok) {
                const data = await response.json();
                console.log('Server response:', data);
                // Perform any additional actions after a successful response
            } else {
                console.error('Server error:', response.statusText);
                // Handle error cases
            }
        } catch (error) {
            console.error('An error occurred:', error);
            // Handle unexpected errors
        }
    }
}

export default new UserController();