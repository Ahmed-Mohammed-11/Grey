const url = 'http://localhost:8080';

class UserController {
    sendCredentials(credentials : UserDTO) {

        const endpoint = url + '/signup';
        let headers = new Headers();

        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json');

        headers.append('Access-Control-Allow-Origin', 'http://127.0.0.1:3000');
        headers.append('Access-Control-Allow-Methods', 'POST');
        headers.append('Access-Control-Allow-Headers', 'Content-Type, Authorization');
        headers.append('Access-Control-Allow-Credentials', 'true');

        fetch(endpoint, {
            credentials: 'include',
            method: 'POST',
            body: JSON.stringify(credentials),
            headers: headers
        })
            .then(response => response.json())
            .then(json => console.log(json))
            .catch(error => console.log('Authorization failed : ' + error.message));
    }
}

export default new UserController();
