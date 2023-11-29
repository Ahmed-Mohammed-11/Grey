import {BASE_BACKEND_URL} from "@/app/constants/apiConstants";

class SigninController implements IPostRequestController {
    sendPostRequest(payload: UserDTO, endpoint: string) {
        const url = BASE_BACKEND_URL + endpoint + "?" + "username=" + payload.username + "&" + "password=" + payload.password;
        return fetch(url, {
            method: "POST"
        });
    }
}

const signinController = new SigninController();
export default signinController;