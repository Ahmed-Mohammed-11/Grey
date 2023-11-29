'use client';
import React, {useEffect, useState} from "react";
import Posts from "@/app/components/posts/page";
import SideBar from "@/app/components/sidebar/page";
import {Box} from "@mui/system";


function Feed() {

    const [Auth, setAuth] = useState<string | null>("")
    useEffect(() => {
        console.log("hello");
        setAuth(localStorage.getItem("Authorization"));
    })
    return (
        <Box>
            <SideBar width={"25%"}/>
            <Posts width={"75%"}/>
        </Box>
    )
}

export default Feed

