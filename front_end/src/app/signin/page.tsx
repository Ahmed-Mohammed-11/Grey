'use client';
import styles from './page.module.css'
import {Button, TextField, Link} from "@mui/material";
import {Box} from "@mui/system";
import GoogleAuthn from "@/app/signup/GoogleAuthn";
import {useRef} from "react";
import {FaArrowLeft} from "react-icons/fa";
import ThemeRegistry from "@/app/themes/themeRegistry";


function Page() {
    const usernameEmailRef = useRef()
    const passwordRef = useRef()

    const handleSubmit = () => {
    }

    return (
        <ThemeRegistry options={{key: 'mui'}}>
            <Box className={styles.topLeft} sx={{
                background: (theme) => theme.palette.primary.light
            }}
            ></Box>
            <Box className={styles.bottomRight}
                 sx={{
                background: (theme) => theme.palette.primary.light
                }}
            ></Box>
            <Box className={styles.container}>
                <Box className={styles.signinForm}>
                    <TextField
                        className={styles.textArea}
                        label='Username/Email'
                        placeholder='Enter your username/email'
                        inputRef={usernameEmailRef}
                        required
                        variant="filled"
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
                    <Box typography="body1" color="text.primary" fontSize="2rem" className={styles.panelText}> Welcome Back! </Box>
                </Box>
                <Link href="/signup">
                    <Button className={styles.iconButton} variant="contained" size="large">
                        <FaArrowLeft size={40} style={{strokeWidth: '2', stroke: 'black'}}/>
                    </Button>
                </Link>
            </Box>
        </ThemeRegistry>
    )
}

export default Page;