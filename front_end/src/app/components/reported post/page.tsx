"use client";
import React from "react";
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {MdGppBad} from "react-icons/md";
import 'react-toastify/dist/ReactToastify.css';
import deletePostController from "@/app/services/deletePostController";
import {NOT_SAFE_POST_ENDPOINT, SAFE_POST_ENDPOINT,} from "@/app/constants/apiConstants";
import Post from "@/app/components/post/page";
import Button from "@mui/material/Button";
import {AiFillSafetyCertificate} from "react-icons/ai";
import toastResponse from "@/app/utils/notifyToast";


export default function ReportedPost(props: any) {
    let post = props.post;
    const handleSafePost = async (postId: string) => {
        const data = deletePostController.sendDeleteRequest({postId: postId}, SAFE_POST_ENDPOINT);
        if ((await data).status === 200) {
            // delete post from the frontend
            props.setPosts(
                props.posts.filter((post: any) => post.id !== postId)
            );
        }
        await toastResponse(data);
    }

    const handleNotSafePost = async (postId: string) => {
        const data = deletePostController.sendDeleteRequest({postId: postId}, NOT_SAFE_POST_ENDPOINT);
        if ((await data).status === 200) {
            // delete post from the frontend
            props.setPosts(
                props.posts.filter((post: any) => post.id !== postId)
            );
        }
        await toastResponse(data);

    }

    return (
        <Box className={styles.reported_post}>
            <Post key={post.id} post={post} feedType={props.feedType} reported={true} setPosts={props.setPosts} posts={props.posts}/>

            <hr className={styles.hr}></hr>

            <Box className={styles.post_footer} sx={{background: (theme) => theme.palette.primary.dark}}>
                <Button className={styles.button} onClick={() => handleSafePost(post.id)}>
                    <AiFillSafetyCertificate/>safe
                </Button>
                <Button className={styles.button} onClick={() => handleNotSafePost(post.id)}>
                    <MdGppBad/>not safe
                </Button>
            </Box>
        </Box>
    )
}
