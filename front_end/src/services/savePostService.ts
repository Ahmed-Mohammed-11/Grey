import { UUID } from "crypto";
import { deflate } from "zlib";

class savePostService {
  async sendPostRequest(postId: UUID) {
    try {
      const url = new URL(`http://localhost:8080/post/toggle/save/${postId}`)
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