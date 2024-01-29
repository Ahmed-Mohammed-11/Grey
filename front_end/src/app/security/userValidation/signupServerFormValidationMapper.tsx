import {USER_EMAIL_TAKEN_MSG} from "@/app/constants/displayErrorMessages";
import buildAuthToken from "@/app/utils/authTokenBuilder";


let isUserValid = {
    username: true,
    email: true,
    password: true
};

let errors = {
    username: "",
    email: "",
    password: ""
};

function handleTakenCredentials() {
    isUserValid.username = false;
    isUserValid.email = false;
    errors.username = USER_EMAIL_TAKEN_MSG;
    errors.email = USER_EMAIL_TAKEN_MSG;
    return;
}

function handleInvalidCredentials(responseBody: UserValidationResponse) {
    if (responseBody.username !== undefined) {
        isUserValid.username = false;
        errors.username = responseBody.username;
    }
    if (responseBody.email !== undefined) {
        isUserValid.email = false;
        errors.email = responseBody.email;
    }
    if (responseBody.password !== undefined) {
        isUserValid.password = false;
        errors.password = responseBody.password;
    }
    return;
}


function handleAuth(userDTO: UserDTO) {
    const authToken = buildAuthToken(userDTO);
    localStorage.setItem("Authorization", authToken);
}


function signupServerFormValidationMapper(responseStatus: number, responseBody: UserValidationResponse, userDTO: UserDTO) {

    isUserValid = {
        username: true,
        email: true,
        password: true
    };

    errors = {
        username: "",
        email: "",
        password: ""
    };

    switch (responseStatus) {
        case 200:
            handleAuth(userDTO);
            break;
        case 400:
            handleInvalidCredentials(responseBody);
            break;
        case 401:
            handleTakenCredentials();
            break;
        case 404:
            break;
        default:
            break;
    }

    return {isUserValid, errors};
}


export default signupServerFormValidationMapper;