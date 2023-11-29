'use client';
import React, {useEffect, useState} from "react";
import Verify from "@/app/verify/page";
import SideBar from "@/app/sidebar/page";
import Feed from "@/app/Feed/page";
import {Box} from "@mui/system";


function Home() {

    const [Auth, setAuth] = useState<string | null>("")
    useEffect(() => {
        console.log("hello");
        setAuth(localStorage.getItem("Authorization"));
    })
    return (
        // <>
        //     <h2> &nbsp;&nbsp;   hello </h2>
        //     <br/>
        //     <h2>  &nbsp;&nbsp; {Auth} </h2>
        // </>
        <Box>
           <SideBar width={"25%"}/>
           <Feed width={"75%"}/>
        </Box>
    )
}

export default Home

