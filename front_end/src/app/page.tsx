import Login from "@/app/signup/page";
import {SessionProvider} from "next-auth/react";


function Home({}) {
    return (
        // <SessionProvider>
            <Login/>
        // </SessionProvider>
    )
}

export default Home

