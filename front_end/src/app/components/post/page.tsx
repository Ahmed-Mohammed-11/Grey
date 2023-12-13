"use client"
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {BsBookmark, BsFillBookmarkFill} from "react-icons/bs";
import {SlOptions} from "react-icons/sl";
import {Chip, IconButton, ListItem} from "@mui/material";
import { toast } from "react-toastify";
import React from "react";
import SavePostController from "@/app/services/SavePostController";
import { SAVE_POST_ENDPOINT } from "@/app/constants/apiConstants";
import 'react-toastify/dist/ReactToastify.css';


export default function Post(props: any) {

    let post = props.post;

    const handleSavePost = (postId: string) => {
        const data = SavePostController.sendPostRequest({postId: postId}, SAVE_POST_ENDPOINT);
        notify(data)
    };

    async function notify (response: Promise<Response>) {
        try {
            const res = await response;
            const message = await res.text();
            console.log('message:' + message)
            toast.success(`${message}`, {
                position: toast.POSITION.TOP_RIGHT,
                autoClose: 1000,
            });
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
