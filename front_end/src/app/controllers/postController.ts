import {baseUrl} from "@/app/constants/apiConstants";


class PostController {
    sendPostRequest(payload: Object, endpoint: string) {
        const url = baseUrl + endpoint;
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        fetch(url, {
            method: 'POST',
            body: JSON.stringify(payload),
            headers: headers
        })
            .then(response => response)
            .then(data => console.log(data))
            .catch(error => console.log(error.message));
    }
}

export default new PostController();