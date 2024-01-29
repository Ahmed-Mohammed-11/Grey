import getUserController from "@/app/services/getUserController";
import toJSON from "@/app/utils/readableStreamResponseBodytoJSON";

async function getUser () {
    const response = await getUserController.sendGetRequest("user");
    const user = await toJSON(response.body!);
    console.log("my user" + user.username)

    localStorage.setItem('username', user.username)
    localStorage.setItem('role', user.role)
    localStorage.setItem('email', user.email)
}

export default getUser;