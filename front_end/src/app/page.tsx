'use client';
import {useRouter} from "next/navigation";
import {HOME_ROUTE, LANDING_ROUTE} from "@/app/constants/apiConstants";

function checkAuth() {
    let Auth = localStorage.getItem("Authorization");
    return !(Auth === null || Auth === undefined || Auth === "");
}

function Welcome() {
    const router = useRouter();
    if (checkAuth()) router.push(HOME_ROUTE);
    else router.push(LANDING_ROUTE);
}

export default Welcome;

