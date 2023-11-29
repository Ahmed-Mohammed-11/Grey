import {UUID} from "crypto";
import {BASE_BACKEND_URL, SAVE_POST_ENDPOINT} from "@/app/constants/apiConstants";

class savePostService {
    async sendPostRequest(postId: UUID) {
        try {
            const url = new URL(`${BASE_BACKEND_URL}${SAVE_POST_ENDPOINT}/${postId}`)
            const response = await fetch(url, {method: 'POST'});
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            console.log(response.status);
            const data = await response.text();
            console.log('response data: ', data);
            return data;
        } catch (error) {
            console.error('Error', error);
        }
    }
}
export default new savePostService();