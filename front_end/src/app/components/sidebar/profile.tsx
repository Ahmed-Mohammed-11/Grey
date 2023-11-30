import {Box} from "@mui/system";
import styles from "./page.module.css"
import {FaUserLarge} from "react-icons/fa6";
import {Icon, IconButton} from "@mui/material";

export default function Profile(props: any) {
    return (
        <Box className={styles.container_profile}>
            <Icon className={styles.profile_image}>
                <FaUserLarge/>
            </Icon>
            <h3>{props.name}</h3>
        </Box>
    )
}