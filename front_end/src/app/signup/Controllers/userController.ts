import axios from "axios";

const url = 'http://localhost:8080';

class UserController {
    async sendCredentials(credentials: UserDTO) {
        try {
            const endpoint = '/signup';

            const response = await axios.post(endpoint, credentials);

            if (response.status === 200) {
                console.log('Server response:', response.data);
            } else {
                console.error('Server error:', response.statusText);
            }
        } catch (error) {
            console.error('An error occurred:', error.message);
        }
    }

}

export default new UserController();
