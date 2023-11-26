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
    errors.username = USER_EMAIL_TAKEN_MSG
    errors.email = USER_EMAIL_TAKEN_MSG
    isUserValid.username = false ;
    isUserValid.email = false;
}

function serverValidateMapper(responseStatus: number, responseBody: UserValidationResponse ) {

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
        case 401:
            handleTakenCredentials();
            break;
        case 404:
            break;
        default:
            return {isUserValid, errors};
    }

    return {isUserValid, errors};
}


export default serverValidateMapper;