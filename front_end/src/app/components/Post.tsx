'use client'
import savePostService from "@/services/savePostService";
import { promises } from "dns";
import { toast } from "react-toastify";
function Post() {

  async function notify (response: any) {
    try {
      const message = await response;
      toast(`${message}`, {
        position: toast.POSITION.TOP_RIGHT,
        autoClose: 1000,
      });
      console.log(message)
    } catch (error) {
      console.log(error)
    }
  }

  const clickHandler = () => {
    const data = savePostService.sendPostRequest('a0c8a56e-c212-4a65-bfdb-50a68cf916df');
    notify(data)
  }

  return (
    <button onClick={clickHandler}>save post</button>
  );
}
export default Post;