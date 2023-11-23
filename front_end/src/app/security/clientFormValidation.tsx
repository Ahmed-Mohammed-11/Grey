import {
    EMAIL_EMPTY_MSG,
    PASSWORD_EMPTY_MSG,
    PASSWORD_FORMAT_MSG,
    USERNAME_EMPTY_MSG,
    USERNAME_LEN_MSG
} from "@/app/constants/displayErrorMessages";

let isFormValid = {
    username: true,
    email: true,
    password: true

};
let errors = {
    username: "",
    email: "",
    password: ""
};

function validateUsername(userDTO: UserDTO) {
    if (!userDTO.username) {
        isFormValid.username = false;
        errors.username = USERNAME_EMPTY_MSG;
        return;
    }

    if (userDTO.username.length < 5 || userDTO.username.length > 20) {
        isFormValid.username = false;
        errors.username = USERNAME_LEN_MSG;
    }
}


function validateEmail(userDTO: UserDTO) {
    if (!userDTO.email) {
        isFormValid.email = false;
        errors.email = EMAIL_EMPTY_MSG;
    }
}

function validatePassword(userDTO: UserDTO) {
    if (!userDTO.password) {
        isFormValid.password = false;
        errors.password = PASSWORD_EMPTY_MSG;
        return;
    }

    if (typeof userDTO.password !== "undefined") {
        if (!userDTO.password.match(/^\w{3,10}\s\w{3,10}\s\w{3,10}(\s\w{3,10})?/)) {
            isFormValid.password = false;
            errors.password = PASSWORD_FORMAT_MSG;
        }
    }
}

function clientValidateForm(userDTO: UserDTO) {
    isFormValid = {
        username: true,
        email: true,
        password: true
    };
    errors = {
        username: "",
        email: "",
        password: ""
    };
    validateUsername(userDTO);
    validateEmail(userDTO);
    validatePassword(userDTO);
    return {isFormValid, errors};
}

export default clientValidateForm;