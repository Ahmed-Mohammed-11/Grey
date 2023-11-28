"use client";
import React, {useRef, useState} from 'react';
import Popup from 'reactjs-popup';
import styles from "./page.module.css"
import Profile from "@/app/SideBar/profile";
import {Box} from "@mui/system";
import {FaPen} from "react-icons/fa";
import {IoSend} from "react-icons/io5";
import {Chip, ListItem, TextField} from "@mui/material";
import Feeling from "@/app/DTO/Feeling";
import PostDTO from "@/app/DTO/PostDTO";
import postController from "@/app/services/postController";
import {createPostEndPoint} from "@/app/constants/apiConstants";

export default function PopupScreen() {
    const [feelings, setFeelings] =
        React.useState(new Set<Feeling>([Feeling.HAPPY, Feeling.SAD, Feeling.ANGER, Feeling.DISGUST,
            Feeling.FEAR, Feeling.ANXIOUS, Feeling.INSPIRE, Feeling.LOVE]));

    const handleDelete = (chipToDelete: Feeling) => () => {
        setFeelings((feelings) => {
            feelings.delete(chipToDelete);
            return new Set<Feeling>(feelings);
        });
    };

    const postText = useRef<HTMLInputElement>(null);
    const handleCreatePost = () => {
        let post: PostDTO = {
            postText: postText.current!.value,
            postFeelings: feelings
        }

        console.log(post);
        // post.postText && sendInfoToServer(post)
    }

    async function sendInfoToServer(post: PostDTO) {
        let postDTO: PostDTO = {
            postText: post.postText,
            postFeelings: post.postFeelings
        }

        fetchResponse(postDTO);
    }

    const fetchResponse = async (postDTO: PostDTO) => {
        let response = await postController.sendPostRequest(postDTO, createPostEndPoint);

        let jsonResponse = await toJSON(response.body!);
        let responseStat = response.status;
        if (responseStat === 200) {
            console.log("success");
        } else {
            console.log("failed");
        }
    }


    return (
        <Popup trigger={<button className={styles.create_button}><FaPen/>create post </button>} modal nested>
            {close => {
                return (
                    <section className={styles.container}>
                        <div className={styles.modal}>
                            <div className={styles.profile}>
                                <Profile name={"@hesham09"}/>
                            </div>
                            <button className={styles.close} onClick={close}>
                                &times;
                            </button>
                            <TextField
                                className={styles.input}
                                aria-valuemax={5000}
                                placeholder={"Pour it out to the world"}
                                inputRef={postText}></TextField>

                            <Box className={styles.post}>
                                <ListItem>
                                    {Array.from(feelings).map((feeling: any, feelingIndex: any) => (
                                        <Chip
                                            // icon={icons[feelingIndex]}
                                            label={feeling}
                                            className={feeling}
                                            onDelete={handleDelete(feeling)}
                                            key={feelingIndex}/>
                                    ))}
                                </ListItem>
                            </Box>

                            <div className={styles.actions}>
                                <button className={styles.button} onClick={() => close()}>
                                    cancel
                                </button>
                                <button className={`${styles.button} ${styles.filled}`} onClick={() => handleCreatePost()}>
                                    create post <IoSend/>
                                </button>
                            </div>
                        </div>
                    </section>
                );
            }}
        </Popup>)
}