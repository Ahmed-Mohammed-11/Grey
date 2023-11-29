"use client"
import Button from '@mui/material/Button';
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {MdExplore} from "react-icons/md";
import {AiFillHome} from "react-icons/ai";
import {FaUserLarge} from "react-icons/fa6";
import {RiSettings4Fill} from "react-icons/ri";
import {BsFillBookmarkFill} from "react-icons/bs";
import Profile from "@/app/components/sidebar/profile";
import PopupScreen from "@/app/components/popup/page";


export default function SideBar(props: any) {
    // TODO: change active tab index when clicked
    var active_tab = 0

    const buttonsText = ["feed", "explore", "profile", "settings", "saved"]
    const buttonsIcons = [
        <AiFillHome/>,
        <MdExplore/>,
        <FaUserLarge/>,
        <RiSettings4Fill/>,
        <BsFillBookmarkFill/>]
    const buttons = buttonsText.map((text, index) => {
        return (
            <Button className={`${styles.button} ${index === active_tab ? styles.active : styles.button}`} key={index}>
                {buttonsIcons[index]}
                {text}
            </Button>
        )
    })

    return (
        <Box className={styles.side_bar} width={props.width}>
            <Profile name={"@hesham09"}></Profile>
            <Box className={styles.container_buttons}>{buttons}</Box>
            <PopupScreen/>
        </Box>
    )
}
