import {Box} from "@mui/system";
import styles from "./page.module.css"
import {Icon} from "@mui/material";

export default function User(props: any) {
    return (
        <Box className={styles.container_profile}>
            <Icon className={styles.profile_image} sx={{bgcolor: 'secondary.light'}}>
                <img src={"/sloth_avatar.png"} alt={"Grey"} width={`100%`}/>
            </Icon>
            <h3>{props.name}</h3>
        </Box>
    )
}