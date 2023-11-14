'use client';
import styles from './page.module.css'
import {Button, TextField} from "@mui/material";
import {Box} from "@mui/system";
import ThemeRegistry from "@/app/ThemeRegistry";


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

                </Box>
                <Box className={[styles.panel].join()}>
                    <Box className={[styles.paneltext].join()}> GR </Box>
                    <Box className={[styles.paneltext].join()}> &nbsp;&nbsp; EY </Box>
                </Box>
            </Box>
        </ThemeRegistry>
    )
}

export default Page;