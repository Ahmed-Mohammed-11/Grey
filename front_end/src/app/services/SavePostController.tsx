import { BASE_BACKEND_URL } from "../constants/apiConstants";

class SavePostController implements IPostRequestController {
    sendPostRequest(payload: Object, endpoint: string):Promise<Response> {
        const url = BASE_BACKEND_URL + endpoint + payload.postId;
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', localStorage.getItem("Authorization")!);
        headers.append('mode','cors')
        return fetch(url, {
            method: 'POST',
            headers: headers
        });
    }
}

const savePostController = new SavePostController();
export default savePostController;