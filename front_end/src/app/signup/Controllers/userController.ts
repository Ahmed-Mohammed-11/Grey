const url = 'http://localhost:8080';

class UserController {
    sendCredentials(credentials : UserDTO) {

        const endpoint = url + '/signup';
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');

        fetch(endpoint, {
            method: 'POST',
            body: JSON.stringify(credentials),
            headers: headers
        })
            .then(response => response)
            .then(data => console.log(data))
            .catch(error => console.log('Authorization failed : ' + error.message));
    }
}

export default new UserController();