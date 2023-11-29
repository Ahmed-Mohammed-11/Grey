'use client';
import styles from './page.module.css'
import {Button, TextField, Link} from "@mui/material";
import {Box} from "@mui/system";
import GoogleAuthn from "@/app/googleAuthentication/GoogleAuthn"
import {useRef, useState} from "react";
import {FaArrowLeft} from "react-icons/fa";
import ThemeRegistry from "@/app/themes/themeRegistry";
import classNames from "classnames";
import clientValidateForm from "@/app/security/userValidation/clientFormValidation";
import signinController from "@/app/services/signinController";
import {
    SIGN_IN_BACKEND_ENDPOINT,
    SIGN_UP_ROUTE,
    HOME_ROUTE,
    SIGN_UP_VERIFICATION_ENDPOINT, SIGN_IN_ROUTE
} from "@/app/constants/apiConstants";
import toJSON from "@/app/utils/readableStreamResponseBodytoJSON";
import {LOGIN_PANEL_TEXT, VERIFY_PANEL_TEXT} from "@/app/constants/displayTextMessages";
import signinServerFormValidationMapper from "@/app/security/userValidation/signinServerFormValidationMapper";
import {useRouter} from "next/navigation";
import signupController from "@/app/services/signupController";

function Verify() {
    const usernameRef = useRef<HTMLInputElement>(null);
    const confirmationCodeRef = useRef<HTMLInputElement>(null);
    const [isUserValid, setIsUserValid] = useState({
        username: true,
        password: true
    });
    const [errors, setErrors] = useState({
        username: "",
        password: ""
    });
    const router = useRouter();

    const handleSubmit = () => {
        let user: UserVerification = {
            username: usernameRef.current!.value,
            confirmationCode: confirmationCodeRef.current!.value
        }

        sendInfoToServer(user)
    }

    async function sendInfoToServer(user: UserVerification) {
        fetchResponse(user);
    }

    const fetchResponse = async (userVerification: UserVerification) => {
        let response = await signupController.sendPostRequest(userVerification, SIGN_UP_VERIFICATION_ENDPOINT);
        // toJSON util to convert ReadableStream to JSON
        let jsonResponse = await toJSON(response.body!);
        let responseStat = response.status;
        //if response status is 200, redirect to sing in page
        (responseStat == 200) && router.push(SIGN_IN_ROUTE);
        //if response status is not 200, map response from server to display appropriate error messages
        //and if 200 get auth token and store it in local storage
        // let {isUserValid, errors} = signinServerFormValidationMapper(responseStat, jsonResponse, userDTO)
        // setIsUserValid(isUserValid);
        // setErrors(errors);
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
                <Box className={styles.signinForm}>
                    <TextField
                        className={styles.textArea}
                        label='Username'
                        placeholder='Enter your username'
                        inputRef={usernameRef}
                        required
                        variant="filled"
                        error = {!isUserValid.username}
                        helperText = {(isUserValid.username)? "": errors.username}
                        InputProps={{style: {background: "#FFF"}}}
                    >
                    </TextField>

                    <TextField
                        className={styles.textArea}
                        label='Confirmation code'
                        placeholder='Enter your confirmation code'
                        inputRef={confirmationCodeRef}
                        required
                        variant="filled"
                        // error = {!isUserValid.password}
                        // helperText = {(isUserValid.password)? "": errors.password}
                        InputProps={{style: {background: "#FFF"}}}
                    >
                    </TextField>

                    <Button
                        className={styles.button}
                        variant="contained"
                        size="large"
                        onClick={handleSubmit}>
                        Verify
                    </Button>
                </Box>

                <Box className={styles.panel}>
                    <Box className={styles.panelBanner}> GREY </Box>
                    <Box typography="body1" color="text.primary" fontSize="2rem" className={styles.panelText}> {VERIFY_PANEL_TEXT} </Box>
                </Box>
                {/*<Link href={SIGN_UP_ROUTE}>*/}
                {/*    <Button className={styles.iconButton} variant="contained" size="large">*/}
                {/*        <FaArrowLeft size={40} style={{strokeWidth: '2', stroke: 'black'}}/>*/}
                {/*    </Button>*/}
                {/*</Link>*/}
            </Box>
        </ThemeRegistry>
    )
}

export default Verify;