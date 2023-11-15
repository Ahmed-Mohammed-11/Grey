'use client';
import styles from './page.module.css'
import {Button, TextField, Link, IconButton, FormControl} from "@mui/material";
import {Box} from "@mui/system";
import ThemeRegistry from "@/app/themes/themeRegistry";
import GoogleAuthn from "@/app/signup/GoogleAuthn";
import {useState} from "react";
import axios from "axios";
import lightTheme from "@/app/themes/lightTheme";
// import { FaArrowRight  } from "react-icons/fa";



function Page() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [responseStatusCode, setResponseStatusCode] = useState(0);
    const [responseMessage, setResponseMessage] = useState('');

    const userDetails = {username, password, email};
    const handleCreateAccount = () => {
        console.log(userDetails);
        fetch('http://localhost:8080/signup', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(userDetails)
        }).then((response) => {
            console.log(response);
        }).catch((error) => {
            console.log(error);
        })
    }


    return (
        <ThemeRegistry options={{key: 'mui'}}>
            <Box className={[styles.container].join()}>
                <Box className={[styles.login_form].join()}>
                    <TextField
                        className={styles.textarea}
                        label='Username'
                        placeholder='Pick a username'
                        required
                        helperText="Min 5 characters and max 15"
                        onChange={(e) => setUsername(e.target.value)}>
                    </TextField>

                    <TextField className={[styles.textarea].join()}
                               label='Email' type="email"
                               placeholder='Email'
                               required
                               onChange={(e) => setEmail(e.target.value)}>
                    </TextField>

                    <TextField
                        className={[styles.textarea].join()}
                        label='Password'
                        type="password"
                        placeholder='pick a password'
                        required
                        helperText="Make it strong"
                        onChange={(e) => setPassword(e.target.value)}>
                    </TextField>

                    <Button
                        className={[styles.button].join()}
                        variant="contained"
                        size="large"
                        onClick={() => {handleCreateAccount()}}>
                        Create Account
                    </Button>

                    <text className={[styles.text].join()}> OR</text>
                    <GoogleAuthn />
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

                {/*<Button className={[styles.iconButton].join()} variant="contained" size="large">*/}
                {/*    <FaArrowRight size={40}/>*/}
                {/*</Button>*/}
            </Box>
        </ThemeRegistry>
    )
}

export default Page;