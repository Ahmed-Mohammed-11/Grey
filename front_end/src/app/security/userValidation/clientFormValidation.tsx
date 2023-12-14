import {
    EMAIL_EMPTY_MSG,
    PASSWORD_EMPTY_MSG,
    USERNAME_EMPTY_MSG,
    USERNAME_INVALID_MSG,
    EMAIL_INVALID_MSG,
    PASSWORD_INVALID_MSG
} from "@/app/constants/displayErrorMessages";

import {
    EMAIL_REGEX,
    PASSWORD_REGEX,
    USERNAME_REGEX
} from "@/app/constants/regularExpressions";

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

function validateUsername(formData: any) {
    if (!formData.username) {
        isUserValid.username = false;
        errors.username = USERNAME_EMPTY_MSG;
        return;
    }

    if (!formData.username.match(USERNAME_REGEX)) {
        isUserValid.username = false;
        errors.username = USERNAME_INVALID_MSG;
    }
}

function validateEmail(formData: any){
    if (!formData.email) {
        isUserValid.email = false;
        errors.email = EMAIL_EMPTY_MSG;
        return;
    }

    if(!formData.email.match(EMAIL_REGEX)) {
        isUserValid.email = false;
        errors.email = EMAIL_INVALID_MSG;
    }
}

function validatePassword(formData: any) {
    if (!formData.password) {
        isUserValid.password = false;
        errors.password = PASSWORD_EMPTY_MSG;
        return;
    }

    if (typeof formData.password !== "undefined") {
        if (!formData.password.match(PASSWORD_REGEX)) {
            isUserValid.password = false;
            errors.password = PASSWORD_INVALID_MSG;
        }
    }
}

function clientValidateForm(formData: any) {
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

    validateUsername(formData);
    validateEmail(formData);
    validatePassword(formData);
    return {isUserValid, errors};
}

export default clientValidateForm;