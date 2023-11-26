import
{
    USER_EMAIL_TAKEN_MSG
} from "@/app/constants/displayErrorMessages";

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

function handleTakenCredentials()
{
    isUserValid.username = false ;
    isUserValid.email = false;
    errors.username = USER_EMAIL_TAKEN_MSG;
    errors.email = USER_EMAIL_TAKEN_MSG;
    return;
}

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

function serverValidateMapper(responseStatus: number, responseBody: UserValidationResponse) {

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
            handleTakenCredentials();
            break;
        case 404:
            break;
        default:
            break;
    }

    return {isUserValid, errors};
}


export default serverValidateMapper;