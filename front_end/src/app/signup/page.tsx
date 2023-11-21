'use client';
import styles from './page.module.css'
import {Button, TextField, Link, IconButton, FormControl} from "@mui/material";
import {Box} from "@mui/system";
import ThemeRegistry from "@/app/themes/themeRegistry";
import GoogleAuthn from "@/app/signup/GoogleAuthn";
import lightTheme from "@/app/themes/lightTheme";
import {FaArrowRight} from "react-icons/fa";
import {useRef} from "react";
import userController from "@/app/signup/Controllers/userController";


function Page() {
    const usernameRef = useRef()
    const emailRef = useRef()
    const passwordRef = useRef()

    const handleSubmit = () => {
        const formData = {
            username: usernameRef.current?.value,
            email: emailRef.current?.value,
            password: passwordRef.current?.value
        }
        sendInfoToServer(formData)
    }

    return (
        <ThemeRegistry options={{key: 'mui'}}>
            <Box className={[styles.container].join()}>
                <Box className={[styles.login_form].join()}>
                    <TextField
                        className={styles.textarea}
                        label='Username'
                        placeholder='Pick a username'
                        inputRef={usernameRef}
                        required
                        variant="filled"
                        helperText="Username shall be 5 - 20 characters long"
                        InputProps={{style: {background: "#FFF",},}}
                        FormHelperTextProps={{className: styles.helperText}}>
                    </TextField>

                    <TextField
                        className={[styles.textarea].join()}
                        label='Email' type="email"
                        placeholder='Email'
                        inputRef={emailRef}
                        required
                        variant="filled"
                        InputProps={{style: {background: "#FFF",},}}>
                    </TextField>

                    <TextField
                        className={[styles.textarea].join()}
                        label='Password'
                        type="password"
                        placeholder='pick a password'
                        inputRef={passwordRef}
                        required
                        variant="filled"
                        helperText="Make it strong"
                        InputProps={{style: {background: "#FFF",},}}
                        FormHelperTextProps={{className: styles.helperText}}>
                    </TextField>

                    <Button
                        className={[styles.button].join()}
                        variant="contained"
                        size="large"
                        onClick={handleSubmit}>
                        Create Account
                    </Button>

                    <text className={[styles.text].join()}> OR</text>
                    <GoogleAuthn/>
                    {/*<br></br>*/}
                    {/*<Link*/}
                    {/*    className={[styles.link].join()}*/}
                    {/*    href="/login"*/}
                    {/*    color="secondary">*/}
                    {/*    Already have an account? Sign In*/}
                    {/*</Link>*/}
                </Box>

                <Box className={[styles.panel].join()}>
                    <Box className={[styles.panelbanner].join()}> GREY </Box>
                    <text className={[styles.paneltext].join()}>social media platform that provides anonymous
                        experience for users to freely express their feelings and opinions, participate in events and
                        much more.
                    </text>
                </Box>
                <Link href="http://localhost:8080/oauth2/authorization/google">
                    <Button className={[styles.iconButton].join()} variant="contained" size="large">
                        <FaArrowRight size={40} style={{strokeWidth: '2', stroke: 'black'}}/>

                    </Button>
                </Link>
            </Box>
        </ThemeRegistry>
    )
}

function sendInfoToServer(formData: any) {
    let userDTO : UserDTO = {
        username: formData.username,
        email: formData.email,
        password: formData.password
    }
    console.log(userDTO)
    userController.sendCredentials(userDTO)
}

export default Page;