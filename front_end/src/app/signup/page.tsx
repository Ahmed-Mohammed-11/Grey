'use client';
import styles from './page.module.css'
import {Button, TextField, Link, IconButton, FormControl} from "@mui/material";
import {Box} from "@mui/system";
import ThemeRegistry from "@/app/themes/themeRegistry";
import GoogleAuthn from "@/app/signup/GoogleAuthn";
import lightTheme from "@/app/themes/lightTheme";
import {FaArrowRight} from "react-icons/fa";
import {useRef} from "react";


function Page() {
    const usernameRef = useRef()
    const emailRef = useRef()
    const passwordRef = useRef()

    const handleSubmit = () => {
        const formData = {
            username: usernameRef.current.value,
            email: emailRef.current.value,
            password: passwordRef.current.value
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
                        helperText="Min 5 characters and max 15"
                        InputProps={{style: {background: "#FFF",},}}
                        FormHelperTextProps={{className: styles.helperText}}>
                    </TextField>

                    <TextField
                        className={[styles.textarea].join()}
                        label='Email' type="email"
                        placeholder='Email'
                        inputRef={emailRef}
                        required
                        InputProps={{style: {background: "#FFF",},}}>
                    </TextField>

                    <TextField
                        className={[styles.textarea].join()}
                        label='Password'
                        type="password"
                        placeholder='pick a password'
                        inputRef={passwordRef}
                        required
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
                    <br></br>
                    <Link
                        className={[styles.link].join()}
                        href="/login"
                        color="secondary">
                        Already have an account? Sign In
                    </Link>
                </Box>

                <Box className={[styles.panel].join()}>
                    <Box className={[styles.panelbanner].join()}> GREY </Box>
                    <text className={[styles.paneltext].join()}>social media platform that provides anonymous
                        experience for users to freely express there feelings and opinions, participate in events and
                        much more.
                    </text>
                </Box>

                <Button className={[styles.iconButton].join()} variant="contained" size="large">
                    <FaArrowRight size={40} style={{strokeWidth: '2', stroke: 'black'}}/>
                </Button>
            </Box>
        </ThemeRegistry>
    )
}

function sendInfoToServer(formData: any) {
    let userDTO : UserDTO = {
        name: formData.username,
        email: formData.email,
        password: formData.password
    }
    console.log(userDTO)
}

export default Page;