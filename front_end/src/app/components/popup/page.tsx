"use client";
import React from 'react';
import Popup from 'reactjs-popup';
import styles from "./page.module.css"
import Profile from "@/app/components/sidebar/profile";
import {FaPen} from "react-icons/fa";
import {IoSend} from "react-icons/io5";
import {Alert, Snackbar, TextareaAutosize, Tooltip} from "@mui/material";
import Feeling from "@/app/models/dtos/Feeling";
import PostDTO from "@/app/models/dtos/PostDTO";
import {CREATE_POST_ENDPOINT} from "@/app/constants/apiConstants";
import createPostController from "@/app/services/createPostController";
import toJSON from "@/app/utils/readableStreamResponseBodytoJSON";
import FeelingsFilter from '../feelingsFilter/page';

const allFeelings = new Set<Feeling>([Feeling.HAPPY, Feeling.SAD,
    Feeling.ANGER, Feeling.DISGUST,
    Feeling.FEAR, Feeling.ANXIOUS,
    Feeling.INSPIRE, Feeling.LOVE]);

export default function PopupScreen() {
    // Feelings chips
    const [selectedFeelings, setSelectedFeelings] = React.useState(new Set<Feeling>());

    // Handling post text and send to server
    const [isPostTextValid, setIsPostTextValid] = React.useState("");
    const handlePostText = (event: React.ChangeEvent<HTMLTextAreaElement>) => {setIsPostTextValid(event.target.value);}
    const handleCreatePost = () => {
        let post: PostDTO = {
            postText: isPostTextValid,
            postFeelings: Array.from(selectedFeelings)
        };
        (selectedFeelings.size > 0) && isPostTextValid && sendInfoToServer(post)
    }

    async function sendInfoToServer(post: PostDTO) {
        // prepare user data to send to server
        let postDTO: PostDTO = {
            postText: post.postText,
            postFeelings: Array.from(post.postFeelings)
        }
        fetchResponse(postDTO);
    }

    const fetchResponse = async (postDTO : PostDTO) => {
        let response = await createPostController.sendPostRequest(postDTO, CREATE_POST_ENDPOINT);
        let jsonResponse = await toJSON(response.body!);
        let responseStat = response.status;
        if (responseStat === 201) {
            handleOpenSnackbar();
            console.log(jsonResponse);
        }
    }

    // Handling server response
    const [openSnackbar, setOpenSnackbar] = React.useState(false);
    const handleOpenSnackbar = () => {setOpenSnackbar(true);};
    const handleCloseSnackbar = (event: React.SyntheticEvent | Event, reason?: string) => {
        if (reason === 'clickaway') return;
        setOpenSnackbar(false);
    };

    return (
        <Popup trigger={<button className={styles.create_button}><FaPen/>create post </button>} modal nested>
            {close => {
                return (
                    <section className={styles.container}>
                        <div className={styles.modal}>
                            <div className={styles.profile}>
                                <Profile name={"@hesham09"}/>
                            </div>
                            <FeelingsFilter limit={3} onDataChange={setSelectedFeelings}/>
                            <TextareaAutosize
                                className={styles.input}
                                maxLength={5000}
                                placeholder={"Pour it out to the world"}
                                value={isPostTextValid}
                                onChange={handlePostText}></TextareaAutosize>

                            <div className={styles.actions}>
                                <button className={styles.button} onClick={close}>
                                    cancel
                                </button>
                                <button
                                        className={`${styles.button} ${styles.filled}
                                        ${!(selectedFeelings.size > 0) || !isPostTextValid ? styles.disabled : ""}`}
                                        disabled={!(selectedFeelings.size > 0) || !isPostTextValid}
                                        onClick={handleCreatePost}>
                                    create post <IoSend/>
                                </button>
                            </div>

                            <button className={styles.close} onClick={close}>
                                &times;
                            </button>

                            <Snackbar open={openSnackbar} autoHideDuration={6000} onClose={handleCloseSnackbar}>
                                <Alert onClose={handleCloseSnackbar} severity="success" sx={{ width: '100%' }}>
                                    Post created successfully
                                </Alert>
                            </Snackbar>
                        </div>
                    </section>
                );
            }}
        </Popup>
    )
}