'use client';
import { Box } from "@mui/material";
import React, {useEffect, useState} from "react";
import SideBar from "./components/sidebar/page";
import styles from "./page.module.css"
import Profile from "./profile/page";
import Home from "@/app/home/page";


function Welcome() {

    const [Auth, setAuth] = useState<string | null>("")
    useEffect(() => {
        console.log("hello");
        setAuth(localStorage.getItem("Authorization"));
    })

    // TODO create a Welcome page that redirects to Home if the user is authenticated
    // Welcome page should explain what Grey hopes to do, and includes a button to the sign up page.
    return (
            <Home />
    )
}

export default Home

