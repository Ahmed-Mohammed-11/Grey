import styles from "./page.module.css"
import {Box} from "@mui/system";
import Profile from "@/app/SideBar/profile";

export default function Feed(props: any) {
    const buttonsText = ["feed", "explore", "profile", "settings", "saved"]
    const posts = buttonsText.map((text, index) => {
        return (
            <Box className={styles.post} key={index}>
                {text}
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et
                dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip
                ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu
                fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia
                deserunt mollit anim id est laborum.
            </Box>
        )
    })

    return (
        <Box className={styles.feed} width={props.width}>
            {posts}
        </Box>
    )
}
