import {Box} from "@mui/system";
import styles from "./page.module.css"
import {Button, Link} from "@mui/material";
import React from "react";
import {SIGN_UP_ROUTE} from "@/app/constants/apiConstants";
import {LANDING_PANEL_TEXT} from "@/app/constants/displayTextMessages";

function Welcome() {

    return (
        <body
            style={
                {
                    backgroundImage: "url(../landing.svg)",
                    backgroundRepeat: "no-repeat",
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    height: "100vh",
                    width: "100vw",
                    overflow: "hidden",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center"
                }
            }
        >
        <Box className={styles.container}>
            <Box className={styles.welcome}>
                <h2 className={styles.title}>Welcome to Grey</h2>
                <text className={styles.description}> {LANDING_PANEL_TEXT} </text>
                <Link className={styles.link} href={SIGN_UP_ROUTE}> <Button> Create Account </Button></Link>
            </Box>
        </Box>
        </body>
    );
}

export default Welcome;