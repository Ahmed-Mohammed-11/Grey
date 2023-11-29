"use client"
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {BsBookmark, BsFillBookmarkFill} from "react-icons/bs";
import {SlOptions} from "react-icons/sl";
import {Chip, IconButton, ListItem} from "@mui/material";
import React from "react";


export default function Post(props: any) {
    const [postSaved, setPostSaved] = React.useState(false);
    const handleSavePost = (postId: number) => {
        setPostSaved(!postSaved);
    };


    const posts = props.posts
    const postsD = posts.map((post: any, index: number) => {
        return (
            <Box className={styles.post} key={index}>
                <Box className={styles.post_header}>
                    <ListItem>
                        {Array.from(post.postFeelings).map((feeling: any, feelingIndex: any) => (
                            <Chip className={feeling} key={feelingIndex} label={feeling}></Chip>
                        ))}
                    </ListItem>
                    <IconButton onClick={() => handleSavePost(post.id)}>
                        {postSaved ?
                          <BsFillBookmarkFill
                            className={styles.icon}></BsFillBookmarkFill>
                        : <BsBookmark
                            className={styles.icon}></BsBookmark>}
                    </IconButton>
                    <IconButton><SlOptions className={styles.icon}></SlOptions></IconButton>
                </Box>
                <p className={styles.post_text}>{post.postText}</p>
            </Box>
        );
    });


    return (
        <Box className={styles.feed} width={props.width}>
            {postsD}
        </Box>
    )
}
