'use client';
import {useEffect, useState} from "react";


function Home() {

    const [Auth, setAuth] = useState<string | null>("")
    useEffect(() => {
        console.log("hello");
        setAuth(localStorage.getItem("Authorization"));
    })
    return (
        <>
            <h2> &nbsp;&nbsp;   hello </h2>
            <br/>
            <h2>  &nbsp;&nbsp; {Auth} </h2>
        </>
    )
}

export default Home

