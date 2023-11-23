'use client';
import styles from './page.module.css'
import {Button, Link, TextField} from "@mui/material";
import {Box} from "@mui/system";
import ThemeRegistry from "@/app/themes/themeRegistry";
import GoogleAuthn from "@/app/googleAuthentication/GoogleAuthn";
import {FaArrowRight} from "react-icons/fa";
import {useRef, useState} from "react";
import postController from "@/app/services/postController";
import {SIGNUP_PANEL_TEXT} from "@/app/constants/displayTextMessages";
import {signinRoute, signupEndPoint} from "@/app/constants/apiConstants";
import classNames from "classnames";
import clientValidateForm from "@/app/security/clientFormValidation";

function Page() {
    const usernameRef = useRef<HTMLInputElement>(null);
    const emailRef = useRef<HTMLInputElement>(null);
    const passwordRef = useRef<HTMLInputElement>(null);
    const [responseMessage, setResponseMessage] = useState({});
    const [responseError, setResponseError] = useState({});
    const [isFormValid, setIsFormValid] = useState({
        username: true,
        email: true,
        password: true
    });
    const [errors, setErrors] = useState({
        username: "",
        email: "",
        password: ""
    });

    let test = {}
    const handleSubmit = () => {
        const formData = {
            username: usernameRef.current?.value,
            email: emailRef.current?.value,
            password: passwordRef.current?.value
        }
        sendInfoToServer(formData)
    }

    async function sendInfoToServer(formData: any) {
        let userDTO: UserDTO = {
            username: formData.username,
            email: formData.email,
            password: formData.password
        }
        let {isFormValid, errors} = clientValidateForm(userDTO)
        setIsFormValid(isFormValid)
        setErrors(errors)

        if (isFormValid.username && isFormValid.email && isFormValid.password)
        {
            await postController.sendPostRequest(userDTO, signupEndPoint)
                .then(response => response.json())
                .then(data => {setResponseMessage(data)})
                .catch(error => {setResponseError(error)})
        }
    }

    let topLeftShapeClass = classNames(styles.topLeft, styles.cornerShapes);
    let bottomRightShapeClass = classNames(styles.bottomRight, styles.cornerShapes);

    return (
        <ThemeRegistry options={{key: 'mui'}}>
            <Box className={topLeftShapeClass} sx={{
                background: (theme) => theme.palette.primary.light
            }}
            ></Box>
            <Box className={bottomRightShapeClass}
                 sx={{
                     background: (theme) => theme.palette.primary.light
                 }}
            ></Box>
            <Box className={styles.container}>
                <Box className={styles.signupForm}>
                    <TextField
                        className={styles.textArea}
                        label='Username'
                        placeholder='Pick a username'
                        inputRef={usernameRef}
                        required
                        variant="filled"
                        error = {!isFormValid.username}
                        helperText = {(isFormValid.username)? "": errors.username}
                        InputProps={{style: {background: "#FFF"}}}
                    >
                    </TextField>

                    <TextField
                        className={styles.textArea}
                        label='Email' type="email"
                        placeholder='Email'
                        inputRef={emailRef}
                        required
                        variant="filled"
                        error = {!isFormValid.email}
                        helperText = {(isFormValid.email)? "": errors.email}
                        InputProps={{style: {background: "#FFF"}}}
                    >
                    </TextField>
                    <TextField
                        className={styles.textArea}
                        label='Password'
                        type="password"
                        placeholder='pick a password'
                        inputRef={passwordRef}
                        required
                        variant="filled"
                        error = {!isFormValid.password}
                        helperText = {(isFormValid.password)? "Make it strong": errors.password}
                        InputProps={{style: {background: "#FFF"}}}
                    >
                    </TextField>
                    <Button
                        className={styles.button}
                        variant="contained"
                        size="large"
                        onClick={handleSubmit}>
                        Create Account
                    </Button>
                    <text> OR</text>
                    <GoogleAuthn/>
                </Box>

                <Box className={styles.panel}>
                    <Box className={styles.panelBanner}> GREY </Box>
                    <text className={styles.panelText}> {SIGNUP_PANEL_TEXT} </text>
                </Box>

                <Link href={signinRoute}>
                    <Button className={[styles.iconButton].join()} variant="contained" size="large">
                        <FaArrowRight size={40} style={{strokeWidth: '2', stroke: 'black'}}/>
                    </Button>
                </Link>

            </Box>
        </ThemeRegistry>
    )
}

export default Page;