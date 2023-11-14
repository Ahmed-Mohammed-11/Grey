'use client';
import styles from './page.module.css'
import {Button, TextField, Link, IconButton} from "@mui/material";
import {Box} from "@mui/system";
import ThemeRegistry from "@/app/themes/themeRegistry";
import GoogleButton from "@/app/signup/GoogleButton";


function Page() {
    return (
        <ThemeRegistry options={{key: 'mui'}}>
            <Box className={[styles.container].join()}>
                <Box className={[styles.login_form].join()}>
                    <TextField className={[styles.textarea].join()} label='Username' placeholder='pick a username' required
                               helperText="min 5 characters and max 15" size="small"></TextField>
                    <TextField className={[styles.textarea].join()} label='Email' type="email" placeholder='Email' required
                               size="small"></TextField>
                    <TextField className={[styles.textarea].join()} label='Password' type="password"
                               placeholder='pick a password' required
                               size="small" helperText="don't share your password with anyone"></TextField>
                    <Button className={[styles.button].join()} variant="contained" size="large"> Create Account </Button>
                    <text className={[styles.text].join()}> OR </text>
                    <GoogleButton />
                    <br></br>
                    <Link className={[styles.link].join()} href="/login" color="secondary">Already have an account? Sign In</Link>
                </Box>

                <Box className={[styles.panel].join()}>
                    <Box className={[styles.panelbanner].join()}> GREY </Box>
                    <text className={[styles.paneltextheader].join()}>Who Are We??</text>
                    <text className={[styles.paneltext].join()}>We are a social media platform that provides anonymous experience for users to freely express there feelings and opinions, participate in events and much more.</text>
                </Box>

                <Button className={[styles.iconButton].join()} variant="contained" size="large">
                    Z
                </Button>
            </Box>
        </ThemeRegistry>
    )
}

export default Page;