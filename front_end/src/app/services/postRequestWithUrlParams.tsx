import {BASE_BACKEND_URL} from "../constants/apiConstants";

class PostRequestWithUrlParams implements IPostRequestController {
    sendPostRequest(payload: Object, endpoint: string): Promise<Response> {
        const postId = JSON.parse(JSON.stringify(payload)).postId;
        const url = BASE_BACKEND_URL + endpoint + "/" + postId;
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', localStorage.getItem("Authorization")!);
        headers.append('mode', 'cors')
        return fetch(url, {
            method: 'POST',
            headers: headers
        });
    }
}

const postRequestWithUrlParams = new PostRequestWithUrlParams();
export default postRequestWithUrlParams;