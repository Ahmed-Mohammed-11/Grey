'use client';
import {Box} from "@mui/system";
import styles from "./page.module.css"
import {AppBar, Card, CardMedia, Link, Stack, Toolbar, Tooltip, Typography} from "@mui/material";
import React from "react";
import {ABOUT_ROUTE, SIGN_IN_ROUTE, SIGN_UP_ROUTE} from "@/app/constants/apiConstants";


function Welcome() {

    return (
        <body
            style={
                {
                    backgroundImage: "url(../landing.svg)",
                    backgroundRepeat: "no-repeat",
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    width: "100vw",
                    height: "100vh",
                }
            }
        >
        <Box>
            <Box sx={{flexGrow: 1}}>
                <AppBar
                    position="static"
                    sx={
                        {
                            width: '43%',
                            margin: 'auto',
                            backgroundColor: 'transparent',
                            borderColor: 'rgba(86, 86, 86, 0.15)',
                            borderStyle: 'solid',
                            borderWidth: '0 0 1px 0',
                        }
                    }
                    elevation={0}
                >
                    <Toolbar>
                        <img src={"./1F428.svg"} alt={"koala"} width={"45px"} height={"45px"}></img>
                        <Typography
                            variant="h4"
                            noWrap
                            component="div"
                            sx={
                                {
                                    flexGrow: 1,
                                    display: {
                                        xs: 'none',
                                        sm: 'block'
                                    },
                                    fontWeight: 'bold',
                                    cursor: 'pointer',
                                }
                            }
                            color={"primary"}
                        >
                            <Link href={"/welcome"} underline={"none"}>
                                GREY
                            </Link>
                        </Typography>
                        <Stack direction={"row"} spacing={3} sx={{flexGrow: 0, p: 0}}>
                            <Tooltip title={"About"} arrow={true} placement={"bottom"}>
                                <Link className={styles.nav_item} href={ABOUT_ROUTE} underline={"none"}>
                                    About
                                </Link>
                            </Tooltip>
                            <Tooltip title={"Sign up"} arrow={true} placement={"bottom"}>
                                <Link className={styles.nav_item} href={SIGN_UP_ROUTE} underline={"none"}>
                                    Sign up
                                </Link>
                            </Tooltip>
                            <Tooltip title={"Login"} arrow={true} placement={"bottom"}>
                                <Link className={styles.nav_item} href={SIGN_IN_ROUTE} underline={"none"}>
                                    Login
                                </Link>
                            </Tooltip>
                        </Stack>
                    </Toolbar>
                </AppBar>
            </Box>

            <Typography className={styles.banner_head} variant="h2" component={"div"} sx={{flexGrow: 1}}>
                Share. Express. Connect
            </Typography>

            <Typography className={styles.banner_text} variant="h5" component={"div"} sx={{flexGrow: 1}}>
                Your anonymity is our responsibility. Grey is your habitat to express what you feel anonymously.
            </Typography>

            <Box className={styles.card_container}>

                <Tooltip title={"Feelings"} arrow={true} placement={"bottom"}>
                    <Card className={styles.card}>
                        <CardMedia
                            component="img"
                            image="/3.png"
                            alt="Feelings"
                            sx={{
                                width: '100%',
                                height: '100%',
                                objectFit: 'contain',

                            }}
                        />
                    </Card>
                </Tooltip>

                <Tooltip title={"Profile"} arrow={true} placement={"bottom"}>
                    <Card className={styles.card}>
                        <CardMedia
                            component="img"
                            image="/1.png"
                            alt="Profile"
                        />
                    </Card>
                </Tooltip>

                <Tooltip title={"Feed"} arrow={true} placement={"bottom"}>
                    <Card className={styles.card}>
                        <CardMedia
                            component="img"
                            image="/2.png"
                            alt="Feed"
                        />
                    </Card>
                </Tooltip>

            </Box>
        </Box>
        </body>
    )
}

export default Welcome;
