import {BASE_BACKEND_URL} from "../constants/apiConstants";

class DeletePostController implements IDeleteRequestController {
    sendDeleteRequest(payload: Object, endpoint: string): Promise<Response> {
        //extract post id from payload
        const postId = JSON.parse(JSON.stringify(payload)).postId;
        const url = BASE_BACKEND_URL + endpoint + "/" + postId;
        let headers = new Headers();
        headers.append('Authorization', localStorage.getItem("Authorization")!);
        headers.append('mode', 'cors')
        return fetch(url, {
            method: 'DELETE',
            headers: headers
        });
    }
}

const deletePostController = new DeletePostController();
export default deletePostController;