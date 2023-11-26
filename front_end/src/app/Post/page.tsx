import styles from "./page.module.css"
import {Box, Stack} from "@mui/system";
import {Chip} from "@mui/material";


export default function Post(props: any) {

    const posts = props.posts
    const postsD = posts.map((post: PostDTO, index: number) => {
        return (
            <Box className={styles.post} key={index}>
                <Stack direction="row" spacing={10}>
                    {Array.from(post.postFeelings).map((feeling: any, feelingIndex: any) => (
                        <Chip className={styles.feeling} key={feelingIndex} label={feeling}></Chip>
                    ))}
                </Stack>
                {post.postText}
            </Box>
        );
    });


    return (
        <Box className={styles.feed} width={props.width}>
            {postsD}
        </Box>
    )
}
