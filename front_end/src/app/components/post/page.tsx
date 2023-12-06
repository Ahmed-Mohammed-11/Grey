"use client"
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {BsBookmark, BsFillBookmarkFill} from "react-icons/bs";
import {SlOptions} from "react-icons/sl";
import {Chip, IconButton, ListItem} from "@mui/material";
import { toast } from "react-toastify";
import React from "react";
import savePostService from "@/app/services/savePostService";


export default function Post(props: any) {
    let post = props.post;

    const handleSavePost = (postId: any) => {
        const data = savePostService.sendPostRequest(postId);
        notify(data)
    };

    async function notify (response: any) {
        try {
            const message = await response;
            toast(`${message}`, {
                position: toast.POSITION.TOP_RIGHT,
                autoClose: 1000,
            });
            console.log(message)
        } catch (error) {
            console.log(error)
        }
    }

    return (
        <Box width={props.width}>
            <Box className={styles.post} key={props.key}>
                <Box className={styles.post_header}>
                    <ListItem>
                        {Array.from(post.postFeelings).map((feeling: any, feelingIndex: any) => (
                            <Chip className={feeling} key={feelingIndex} label={feeling}></Chip>
                        ))}
                    </ListItem>
                    <IconButton onClick={() => handleSavePost(post.id)}>
                        {post.saved ?
                          <BsFillBookmarkFill
                            className={styles.icon}></BsFillBookmarkFill>
                        : <BsBookmark
                            className={styles.icon}></BsBookmark>}
                    </IconButton>
                    <IconButton><SlOptions className={styles.icon}></SlOptions></IconButton>
                </Box>
                <p className={styles.post_text}>{post.postText}</p>
            </Box>
        </Box>
    )
}
