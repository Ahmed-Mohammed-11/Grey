'use client';
import styles from './page.module.css'
import {Button, Link, TextField} from "@mui/material";
import {Box} from "@mui/system";
import GoogleAuthn from "@/app/googleAuthentication/GoogleAuthn"
import {useRef, useState} from "react";
import {FaArrowLeft} from "react-icons/fa";
import classNames from "classnames";
import clientValidateForm from "@/app/security/userValidation/clientFormValidation";
import loginController from "@/app/services/loginController";
import {HOME_ROUTE, LOG_IN_BACKEND_ENDPOINT, SIGN_UP_ROUTE} from "@/app/constants/apiConstants";
import toJSON from "@/app/utils/readableStreamResponseBodytoJSON";
import getUser from "@/app/utils/getUser";
import {LOGIN_PANEL_TEXT} from "@/app/constants/displayTextMessages";
import loginServerFormValidationMapper from "@/app/security/userValidation/loginServerFormValidationMapper";
import {useRouter} from "next/navigation";

function Page() {
    const usernameRef = useRef<HTMLInputElement>(null);
    const passwordRef = useRef<HTMLInputElement>(null);
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
        let user: User = {
            username: usernameRef.current!.value,
            password: passwordRef.current!.value
        }

        // validate user credentials on client side
        let {isUserValid, errors} = clientValidateForm(user)
        setIsUserValid(isUserValid)
        setErrors(errors);

        // if user credentials are valid, try sending to server
        isUserValid.username && isUserValid.password && sendInfoToServer(user)
    }

    async function sendInfoToServer(user: UserDTO) {
        // prepare user data to send to server
        let userDTO: UserDTO = {
            username: user.username,
            password: user.password
        }
        await fetchResponse(userDTO);
    }

    const fetchResponse = async (userDTO: UserDTO) => {
        let response = await loginController.sendPostRequest(userDTO, LOG_IN_BACKEND_ENDPOINT);
        // toJSON util to convert ReadableStream to JSON
        let jsonResponse = await toJSON(response.body!);
        let responseStat = response.status;
        //if response status is 200, redirect to home page
        //if response status is not 200, map response from server to display appropriate error messages
        //and if 200 get auth token and store it in local storage
        let {isUserValid, errors} = loginServerFormValidationMapper(responseStat, jsonResponse, userDTO);

        setIsUserValid(isUserValid);
        setErrors(errors);

        (responseStat == 200) && await getUser().then(() => router.push(HOME_ROUTE));
    }

    let topLeftShapeClass = classNames(styles.topLeft, styles.cornerShapes);
    let bottomRightShapeClass = classNames(styles.bottomRight, styles.cornerShapes);
    return (
        <Box>
            <Box className={topLeftShapeClass} sx={{
                background: (theme) => theme.palette.primary.light
            }}
            >
            </Box>
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
                        placeholder='Enter your username/email'
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
                        label='Password'
                        type="password"
                        placeholder='Enter your password'
                        inputRef={passwordRef}
                        required
                        variant="filled"
                        error={!isUserValid.password}
                        helperText={(isUserValid.password) ? "" : errors.password}
                        InputProps={{style: {background: "#FFF"}}}
                    >
                    </TextField>

                    <Button
                        className={styles.button}
                        variant="contained"
                        size="large"
                        onClick={handleSubmit}>
                        Sign In
                    </Button>

                    <text>OR</text>

                    <GoogleAuthn/>
                </Box>

                <Box className={styles.panel}>
                    <Box className={styles.panelBanner}> GREY </Box>
                    <Box typography="body1" color="text.primary" fontSize="2rem"
                         className={styles.panelText}> {LOGIN_PANEL_TEXT} </Box>
                </Box>
                <Link href={SIGN_UP_ROUTE}>
                    <Button className={styles.iconButton} variant="contained" size="large">
                        <FaArrowLeft size={40} style={{strokeWidth: '2', stroke: 'black'}}/>
                    </Button>
                </Link>
            </Box>
        </Box>
    )
}

export default Page;