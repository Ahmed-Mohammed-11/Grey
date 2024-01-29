"use client";
import React from "react";
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {BsBookmark, BsFillBookmarkFill} from "react-icons/bs";
import {SlOptions} from "react-icons/sl";
import {MdDelete, MdReport} from "react-icons/md";
import {Chip, IconButton, ListItem, Menu, MenuItem} from "@mui/material";
import 'react-toastify/dist/ReactToastify.css';
import deletePostController from "@/app/services/deletePostController";
import postRequestWithUrlParams from "@/app/services/postRequestWithUrlParams";
import {
    REPORT_POST_ENDPOINT,
    SAVE_POST_ENDPOINT,
    DELETE_POST_ENDPOINT,
} from "@/app/constants/apiConstants";
import toastResponse from "@/app/utils/notifyToast";


export default function Post(props: any) {
    let post = props.post;

    const [menuAnchorEl, setMenuAnchorEl] = React.useState<null | HTMLElement>(null);
    const openMenu = Boolean(menuAnchorEl);
    const handleMenuClick = (event: React.MouseEvent<HTMLElement>) => {
        setMenuAnchorEl(event.currentTarget);
    };
    const handleMenuClose = () => {
        setMenuAnchorEl(null);
    };


    const handleSavePost = async (postId: string) => {
        const data = postRequestWithUrlParams.sendPostRequest({postId: postId}, SAVE_POST_ENDPOINT);
        await toastResponse(data);
    };

    const handleReportPost = async (postId: string) => {
        const data = postRequestWithUrlParams.sendPostRequest({postId: postId}, REPORT_POST_ENDPOINT);
        await toastResponse(data);
    }

    const handleDeletePost = async (postId: string) => {
        const data = deletePostController.sendDeleteRequest({postId: postId}, DELETE_POST_ENDPOINT);
        if ((await data).status === 200) {
            // delete post from the frontend
            props.setPosts(
                props.posts.filter((post: any) => post.id !== postId)
            );
        }
        await toastResponse(data);
    }

    return (
        <Box width={props.width}>
            <Box className={styles.post} key={post.id}>
                <Box className={styles.post_header}>
                    <ListItem>
                        {Array.from(post.postFeelings).map((feeling: any, feelingIndex: any) => (
                            <Chip className={feeling} key={feelingIndex} label={feeling} style={{margin: '0.5%', borderRadius: '5px'}}></Chip>
                        ))}
                    </ListItem>

                    {!props.reported &&
                        (<IconButton onClick={() => handleSavePost(post.id)}>
                            {post.saved ?
                                <BsFillBookmarkFill className={styles.main_icons}></BsFillBookmarkFill>
                                : <BsBookmark className={styles.main_icons}></BsBookmark>}
                        </IconButton>)}
                    {!props.reported &&
                        (<IconButton
                            aria-controls={openMenu ? 'options-menu' : undefined}
                            aria-expanded={openMenu ? 'true' : undefined}
                            onClick={handleMenuClick}>
                            <SlOptions className={styles.main_icons}></SlOptions>
                        </IconButton>)}
                    <Menu
                        id="options-menu"
                        anchorEl={menuAnchorEl}
                        open={openMenu}
                        onClose={handleMenuClose}>
                        <MenuItem
                            className={styles.menu_item}
                            onClick={() => {
                                handleMenuClose();
                                handleReportPost(post.id)
                            }}>
                            <MdReport className={styles.icon}/>
                            Report Post
                        </MenuItem>
                        {props.feedType === 2 && (
                            <MenuItem
                                className={styles.menu_item}
                                onClick={() => {
                                    handleMenuClose();
                                    handleDeletePost(post.id);
                                }}>
                                <MdDelete className={styles.icon}/>
                                Delete Post
                            </MenuItem>
                        )}
                    </Menu>
                </Box>
                <p className={styles.post_text}>{post.postText}</p>
            </Box>
        </Box>
    )
}