import {BASE_BACKEND_URL} from "@/app/constants/apiConstants";

class GetRequestNoParams implements IGetRequestController{
    sendGetRequest(endpoint: string) {
        const url = BASE_BACKEND_URL+ endpoint;
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Authorization', localStorage.getItem("Authorization")!);
        headers.append('mode','cors')
        return fetch(url, {
            method: "GET",
            headers: headers
        });
    }
}

const getUserController = new GetRequestNoParams();
export default getUserController;