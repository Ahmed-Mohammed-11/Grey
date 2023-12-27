'use client';
import {useRouter} from "next/navigation";
import {HOME_ROUTE, LANDING_ROUTE} from "@/app/constants/apiConstants";

function checkAuth() {
    try{
        let Auth = localStorage.getItem("Authorization");
        return !(Auth === null || Auth === undefined || Auth === "");
    } catch (e) {
        return false;
    }
}

function Page() {
    const router = useRouter();
    if (checkAuth()) router.push(HOME_ROUTE);
    else router.push(LANDING_ROUTE);
}

export default Page;

