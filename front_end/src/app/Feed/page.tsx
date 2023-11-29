import styles from "./page.module.css"
import {Box} from "@mui/system";
import Post from "@/app/Post/page";

export default function Feed(props:any) {
    const posts = props.data || []

    return (
        <Box className={styles.feed} width={props.width}>
            <Post posts={posts}/>
        </Box>
    )
}
