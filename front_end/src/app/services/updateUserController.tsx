import {BASE_BACKEND_URL} from "../constants/apiConstants";

class UpdateUserController {
    sendPutRequest(payload: Object, endpoint: string): Promise<Response> {
        const url = BASE_BACKEND_URL + endpoint
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', localStorage.getItem("Authorization")!);
        headers.append('mode', 'cors')
        return fetch(url, {
            method: 'PUT',
            body: JSON.stringify(payload),
            headers: headers
        });
    }
}

const updateUserController = new UpdateUserController();
export default updateUserController;