import {baseUrl} from "@/app/constants/apiConstants";


class PostController {
    sendPostRequest(payload: Object, endpoint: string) {
        const url = baseUrl + endpoint;
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('mode','cors')
        return fetch(url, {
            method: 'POST',
            body: JSON.stringify(payload),
            headers: headers
        })

    }
}

export default new PostController();