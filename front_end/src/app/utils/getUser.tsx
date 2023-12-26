import getUserController from "@/app/services/getUserController";
import toJSON from "@/app/utils/readableStreamResponseBodytoJSON";

async function getUser () {
    const response = await getUserController.sendGetRequest("user");
    const user = await toJSON(response.body!);
    localStorage.setItem('user', user);
    console.log(user);
}

export default getUser;