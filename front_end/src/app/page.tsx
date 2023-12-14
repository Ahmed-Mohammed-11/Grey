'use client';
import { Box } from "@mui/material";
import React, {useEffect, useState} from "react";
import SideBar from "./components/sidebar/page";
import styles from "./page.module.css"
import Profile from "./profile/page";
import Feed from "./components/posts/page";


function Home() {

    const [Auth, setAuth] = useState<string | null>("")
    useEffect(() => {
        console.log("hello");
        setAuth(localStorage.getItem("Authorization"));
    })

    const [index, setIndex] = useState(0)
    const profile = <Profile />
    const feed = <Feed index={index}/>
    const [display, setDisplay] = useState(feed)

    const handleChange = (idx: number) => {
        setIndex(idx)
        if (idx === 4) {
            setDisplay(profile)
        } else {
            // posts
            setDisplay(<Feed index={index} />)
        }
    }

    return (
        <>
            <Box className={styles.container}>
                <SideBar width={'25%'} onChange={handleChange} />
                {display}
            </Box>
        </>
    )
}

export default Home

