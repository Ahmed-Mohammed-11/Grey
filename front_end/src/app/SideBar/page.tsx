import Button from '@mui/material/Button';
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {MdExplore} from "react-icons/md";
import {AiFillHome} from "react-icons/ai";
import {FaUserLarge} from "react-icons/fa6";
import {RiSettings4Fill} from "react-icons/ri";
import {BsBookmark, BsFillBookmarkFill} from "react-icons/bs";
import Profile from "@/app/SideBar/profile";


export default function SideBar(props: any) {
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
            <Button className={(index === active_tab ? styles.button : styles.button)} key={index}>
                {buttonsIcons[index]}
                {text}
            </Button>
        )
    })

    return (
        <Box className={styles.side_bar} width={props.width}>
            <Profile name={"@teez_tammam"}></Profile>
            <Box className={styles.container_buttons}>{buttons}</Box>
        </Box>
    )
}
