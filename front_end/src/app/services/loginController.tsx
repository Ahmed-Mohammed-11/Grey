import {BASE_BACKEND_URL} from "@/app/constants/apiConstants";

class LoginController implements IPostRequestController {
    sendPostRequest(payload: UserDTO, endpoint: string) {
        const url = BASE_BACKEND_URL + endpoint + "?" + "username=" + payload.username + "&" + "password=" + payload.password;
        return fetch(url, {
            method: "POST"
        });
    }
}

const loginController = new LoginController();
export default loginController;