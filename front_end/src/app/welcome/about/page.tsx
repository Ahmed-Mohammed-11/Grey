'use client';
import React from "react";
import styles from "@/app/welcome/page.module.css";
import {Card, CardMedia, Grid} from "@mui/material";
import {Box} from "@mui/system";


function About() {

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
        <Grid container spacing={20} direction={"column"} alignItems={"center"} justifyContent={"center"}>
            <Grid item md={3}>
                <Card>
                    <CardMedia
                        component="img"
                        image="../ahmed.svg"
                        alt="green iguana"
                    />
                </Card>
            </Grid>
            <Grid item md={3}>
                <Card>
                    <CardMedia
                        component="img"
                        image="../josef.svg"
                        alt="green iguana"
                    />
                </Card>
            </Grid>
            <Grid item md={3}>
                <Card>
                    <CardMedia
                        component="img"
                        image="../omar.svg"
                        alt="green iguana"
                    />
                </Card>
            </Grid>
            <Grid item md={3}>
                <Card>
                    <CardMedia
                        component="img"
                        image="../abdelrahman.svg"
                        alt="green iguana"
                    />
                </Card>
            </Grid>
            <Grid item md={3}>
                <Card>
                    <CardMedia
                        component="img"
                        image="../osama.svg"
                        alt="green iguana"
                    />
                </Card>
            </Grid>
        </Grid>
        </body>
    )
}

export default About;
