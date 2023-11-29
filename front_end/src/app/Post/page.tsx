import styles from "./page.module.css"
import {Box, Stack} from "@mui/system";
import {Chip, ListItem} from "@mui/material";
import React from "react";


export default function Post(props: any) {

    const posts = props.posts
    const postsD = posts.map((post: any, index: number) => {
        return (
            <Box className={styles.post} key={index}>
                <ListItem>
                    {Array.from(post.postFeelings).map((feeling: any, feelingIndex: any) => (
                        <Chip className={feeling} key={feelingIndex} label={feeling}></Chip>
                    ))}
                </ListItem>
                <p className={styles.postText}>{post.postText}</p>
            </Box>
        );
    });


    return (
        <Box className={styles.feed} width={props.width}>
            {postsD}
        </Box>
    )
}
