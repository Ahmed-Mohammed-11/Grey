import { BASE_BACKEND_URL } from "@/app/constants/apiConstants";

class SignupController implements IPostRequestController {
    sendPostRequest(payload: Object, endpoint: string):Promise<Response> {
        const url = BASE_BACKEND_URL + endpoint;
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('mode','cors')
        return fetch(url, {
            method: 'POST',
            body: JSON.stringify(payload),
            headers: headers
        });
    }
}

const signupController = new SignupController();
export default signupController;