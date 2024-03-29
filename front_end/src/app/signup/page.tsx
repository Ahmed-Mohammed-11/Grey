'use client';
import styles from './page.module.css'
import {Button, Link, TextField} from "@mui/material";
import {Box} from "@mui/system";
import ThemeRegistry from "@/app/themes/themeRegistry";
import GoogleAuthn from "@/app/googleAuthentication/GoogleAuthn";
import {FaArrowRight} from "react-icons/fa";
import {useRef, useState} from "react";
import signupController from "@/app/services/signupController";
import {SIGNUP_PANEL_TEXT} from "@/app/constants/displayTextMessages";
import {
    HOME_ROUTE,
    SIGN_IN_ROUTE,
    SIGN_UP_BACKEND_ENDPOINT,
    SIGN_UP_VERIFICATION_ENDPOINT
} from "@/app/constants/apiConstants";
import classNames from "classnames";
import clientValidateForm from "@/app/security/userValidation/clientFormValidation";
import signupServerFormValidationMapper from "@/app/security/userValidation/signupServerFormValidationMapper";
import toJSON from "@/app/utils/readableStreamResponseBodytoJSON";
import {useRouter} from "next/navigation";
import getUser from "@/app/utils/getUser";
function Page() {
    const usernameRef = useRef<HTMLInputElement>(null);
    const emailRef = useRef<HTMLInputElement>(null);
    const passwordRef = useRef<HTMLInputElement>(null);
    const [isUserValid, setIsUserValid] = useState({
        username: true,
        email: true,
        password: true
    });
    const [errors, setErrors] = useState({
        username: "",
        email: "",
        password: ""
    });

    const router = useRouter();

    const handleSubmit = () => {
        // user credentials
        let user: User = {
            username: usernameRef.current!.value,
            email: emailRef.current!.value,
            password: passwordRef.current!.value
        }

        // validate user credentials on client side
        let {isUserValid, errors} = clientValidateForm(user)
        setIsUserValid(isUserValid)
        setErrors(errors);

        // if user credentials are valid, try send to server
        isUserValid.username && isUserValid.email && isUserValid.password && sendInfoToServer(user)
    }

    async function sendInfoToServer(user: UserDTO) {
        // prepare user data to send to server
        let userDTO: UserDTO = {
            username: user.username,
            email: user.email,
            password: user.password
        }

        fetchResponse(userDTO);
    }

    const fetchResponse = async (userDTO: UserDTO) => {
        let response = await signupController.sendPostRequest(userDTO, SIGN_UP_BACKEND_ENDPOINT);
        // toJSON util to convert ReadableStream to JSON
        let jsonResponse = await toJSON(response.body!);
        let responseStat = response.status;
        //if response status is not 200, map response from server to display appropriate error messages
        //and if 200 get auth token and store it in local storage
        let {isUserValid, errors} = signupServerFormValidationMapper(responseStat, jsonResponse, userDTO)
        setIsUserValid(isUserValid);
        setErrors(errors);

        //if response status is 200, redirect to home page
        (responseStat == 200) && await getUser().then(() => router.push(HOME_ROUTE));
    }


    // classes of the corner shapes
    let topLeftShapeClass = classNames(styles.topLeft, styles.cornerShapes);
    let bottomRightShapeClass = classNames(styles.bottomRight, styles.cornerShapes);

    return (

        <ThemeRegistry options={{key: 'mui'}}>
            <Box className={topLeftShapeClass}
                 sx={{background: (theme) => theme.palette.primary.light}}>
            </Box>
            <Box className={bottomRightShapeClass}
                 sx={{background: (theme) => theme.palette.primary.light}}>
            </Box>


            <Box className={styles.container}>
                <Box className={styles.signupForm}>
                    <TextField
                        className={styles.textArea}
                        label='Username'
                        placeholder='Pick a username'
                        inputRef={usernameRef}
                        required
                        variant="filled"
                        error={!isUserValid.username}
                        helperText={(isUserValid.username) ? "" : errors.username}
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
                        error = {!isUserValid.email}
                        helperText = {(isUserValid.email)? "": errors.email}
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
                        error = {!isUserValid.password}
                        helperText = {(isUserValid.password)? "Make it strong": errors.password}
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

                <Link href={SIGN_IN_ROUTE}>
                    <Button className={[styles.iconButton].join()} variant="contained" size="large">
                        <FaArrowRight size={40} style={{strokeWidth: '2', stroke: 'black'}}/>
                    </Button>
                </Link>

            </Box>
        </ThemeRegistry>

    )
}

export default Page;