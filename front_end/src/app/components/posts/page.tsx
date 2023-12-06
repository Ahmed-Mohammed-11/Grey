import styles from "./page.module.css"
import {Box} from "@mui/system";
import Post from "@/app/components/post/page";
import Feeling from '../../models/dtos/Feeling';

export default function Feed(props:any) {
    const posts = props.data || []

    return (
        <Box className={styles.feed} width={props.width}>
            {posts.map((post: any) => (
                <Post key={post.id} post={post} />
            ))}
            
        </Box>
    )
}
