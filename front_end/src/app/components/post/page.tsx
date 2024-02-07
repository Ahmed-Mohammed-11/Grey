"use client";
import React from "react";
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {BsBookmark, BsFillBookmarkFill} from "react-icons/bs";
import {SlOptions} from "react-icons/sl";
import {MdDelete, MdGppBad, MdOutlineEmojiEmotions, MdReport} from "react-icons/md";
import {Avatar, Chip, IconButton, ListItem, Menu, MenuItem} from "@mui/material";
import 'react-toastify/dist/ReactToastify.css';
import deletePostController from "@/app/services/deletePostController";
import postRequestWithUrlParams from "@/app/services/postRequestWithUrlParams";
import {
    REPORT_POST_ENDPOINT,
    SAVE_POST_ENDPOINT,
    DELETE_POST_ENDPOINT,
} from "@/app/constants/apiConstants";
import toastResponse from "@/app/utils/notifyToast";
import Feeling from "@/app/entities/dtos/Feeling";
import {BiAngry} from "react-icons/bi";
import FaceIcon from "@mui/icons-material/Face";

import { FaCircleCheck } from "react-icons/fa6";
import { IoShieldCheckmark } from "react-icons/io5";
import { IoCloseCircle } from "react-icons/io5";
import NestedModal from "@/app/components/modal/page";
import {AiFillSafetyCertificate} from "react-icons/ai";

export default function Post(props: any) {
    let post = props.post;

    const [menuAnchorEl, setMenuAnchorEl] = React.useState<null | HTMLElement>(null);
    const openMenu = Boolean(menuAnchorEl);

    const emojiMap = new Map<Feeling, any>(
        [
            [Feeling.HAPPY, <MdOutlineEmojiEmotions style={{color: '#26c08a', fontSize: '21px'}}/>],
            [Feeling.ANXIOUS, <MdOutlineEmojiEmotions style={{color: '#fdc358', fontSize: '21px'}}/>],
            [Feeling.DISGUST, <BiAngry style={{color: '#d29f6fff', fontSize: '21px'}}/>],
            [Feeling.ANGER, <BiAngry style={{color: '#f86c6b', fontSize: '21px'}}/>],
            [Feeling.LOVE, <MdOutlineEmojiEmotions style={{color: '#f0769f', fontSize: '21px'}}/>],
            [Feeling.FEAR, <MdOutlineEmojiEmotions style={{color: '#a60dfd84', fontSize: '21px'}}/>],
            [Feeling.SAD, <MdOutlineEmojiEmotions style={{color: '#689cbd', fontSize: '21px'}}/>],
            [Feeling.INSPIRE, <MdOutlineEmojiEmotions style={{color: '#5252528c', fontSize: '21px'}}/>]
        ]
    );
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
                    <Avatar src={"sloth_avatar.png"} className={styles.post_avatar} sx={{width: 40, height: 40, bgcolor: 'secondary.light'}}/>
                    <ListItem>
                        {Array.from(post.postFeelings).map((feeling: any, feelingIndex: any) => (
                            <Chip
                                className={feeling}
                                key={feelingIndex}
                                icon={emojiMap.get(feeling) ? emojiMap.get(feeling) : <FaceIcon/>}
                                label={feeling}
                                style={{margin: '0.5%', borderRadius: '5px'}}
                            />
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

                    {/*TODO remove it if not appropriate*/}
                    {props.reported && false
                        &&
                        (<NestedModal
                                postId={post.id}
                                type={"safe"}
                                icon={<AiFillSafetyCertificate/>}
                                title={"Mark Safe"}/>
                        )
                        &&
                        (<NestedModal
                                postId={post.id}
                                type={"safe"}
                                icon={<AiFillSafetyCertificate/>}
                                title={"Mark Safe"}/>
                        )
                    }
                </Box>
                <p className={styles.post_text}>{post.postText}</p>
            </Box>
        </Box>
    )
}