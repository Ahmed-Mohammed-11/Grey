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
import NestedModal from "@/app/components/modal/page";


export default function ReportedPost(props: any) {
    let post = props.post;

    const pre_text = "You think this is a "
    const post_text = " post! Make sure no falsest occur, " +
        "then confirm what you think"

    const handleSafePost = async (postId: string) => {
        console.log(postId)
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
            <Post key={post.id} post={post} feedType={props.feedType} reported={true}
                  setPosts={props.setPosts} posts={props.posts}/>

            <Box className={styles.post_footer}>
                <NestedModal
                    postId={post.id}
                    type={"safe"}
                    icon={<AiFillSafetyCertificate/>}
                    title={"Mark Safe"}
                    text={pre_text + "safe" + post_text}
                    handle={handleSafePost}/>
                <NestedModal
                    postId={post.id}
                    type={"violating"}
                    icon={<MdGppBad/>}
                    title={"Mark Violating"}
                    text={pre_text + "violating" + post_text}
                    handle={handleNotSafePost}/>
            </Box>
        </Box>
    )
}
