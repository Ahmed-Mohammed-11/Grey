import {USER_WRONG_CREDENTIALS_MSG} from "@/app/constants/displayErrorMessages";

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

function handleInvalidCredentials(responseBody: UserValidationResponse)
{
    if (responseBody.username)
    {
        errors.username = responseBody.username;
        isUserValid.username = false;
    }
    if (responseBody.email)
    {
        errors.email = responseBody.email;
        isUserValid.email = false;
    }
    if (responseBody.password)
    {
        errors.password = responseBody.password;
        isUserValid.password = false;
    }
}

function handleWrongCredentials(responseBody: UserValidationResponse){
    isUserValid.password = false;
    isUserValid.username = false;
    errors.username = USER_WRONG_CREDENTIALS_MSG;
    errors.password = USER_WRONG_CREDENTIALS_MSG;
}


function signinServerFormValidationMapper(responseStatus: number, responseBody: UserValidationResponse) {

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
            break;
        case 400:
            handleInvalidCredentials(responseBody);
            break;
        case 401:
            handleWrongCredentials(responseBody);
        case 404:
            break;
        default:
            break;
    }

    return {isUserValid, errors};
}

export default signinServerFormValidationMapper;