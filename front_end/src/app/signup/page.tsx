'use client';
import styles from './page.module.css'
import {Button, Link, TextField} from "@mui/material";
import {Box} from "@mui/system";
import ThemeRegistry from "@/app/themes/themeRegistry";
import GoogleAuthn from "@/app/googleAuthentication/GoogleAuthn";
import {FaArrowRight} from "react-icons/fa";
import {useRef} from "react";
import postController from "@/app/controllers/postController";
import {signinRoute, signupEndPoint} from "@/app/constants/apiConstants";
import {signupPanelText} from "@/app/constants/displayTextConstants";
import classNames from "classnames";

function Page() {
    const usernameRef = useRef<HTMLInputElement>(null)
    const emailRef = useRef<HTMLInputElement>(null)
    const passwordRef = useRef<HTMLInputElement>(null)

    const handleSubmit = () => {
        const formData = {
            username: usernameRef.current?.value,
            email: emailRef.current?.value,
            password: passwordRef.current?.value
        }
        sendInfoToServer(formData)
    }

    function sendInfoToServer(formData: any) {
        let userDTO: UserDTO = {
            username: formData.username,
            email: formData.email,
            password: formData.password
        }
        console.log(userDTO)
        postController.sendPostRequest(userDTO, signupEndPoint)
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
                        helperText="Username shall be 5 - 20 characters long"
                        InputProps={{style: {background: "#FFF",},}}
                    >
                    </TextField>

                    <TextField
                        className={styles.textArea}
                        label='Email' type="email"
                        placeholder='Email'
                        inputRef={emailRef}
                        required
                        variant="filled"
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
                        helperText="Make it strong"
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
                    <text className={styles.panelText}> {signupPanelText} </text>
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