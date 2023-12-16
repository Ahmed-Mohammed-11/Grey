"use client";
import React from 'react';
import Popup from 'reactjs-popup';
import styles from "./page.module.css"
import User from "@/app/components/sidebar/user";
import {FaPen} from "react-icons/fa";
import {IoSend} from "react-icons/io5";
import {TextareaAutosize} from "@mui/material";
import Feeling from "@/app/models/dtos/Feeling";
import PostDTO from "@/app/models/dtos/PostDTO";
import {CREATE_POST_ENDPOINT} from "@/app/constants/apiConstants";
import createPostController from "@/app/services/createPostController";
import FeelingsFilter from '../feelingsFilter/page';
import {toast} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

export default function PopupScreen() {
    // Feelings chips
    const [selectedFeelings, setSelectedFeelings] = React.useState(new Set<Feeling>());


    // Handling post text and send to server
    const [isPostTextValid, setIsPostTextValid] = React.useState("");
    const handlePostText = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
        setIsPostTextValid(event.target.value);
    }
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


    // Handling server response
    const fetchResponse = async (postDTO: PostDTO) => {
        const response = createPostController.sendPostRequest(postDTO, CREATE_POST_ENDPOINT);
        notify(response);
        clearPostInfo();
    }

    async function notify(response: Promise<Response>) {
        try {
            toast.promise(response.then(res => {
                }),
                {
                    pending: 'Creating post...',
                    success: 'Shared to the world successfully',
                    error: 'Something went wrong',
                },
                {
                    position: "bottom-left",
                    autoClose: 2000,
                    theme: "colored",
                    hideProgressBar: true
                }
            );

        } catch (error) {
            console.log(error)
        }
    }

    const clearPostInfo = () => {
        setIsPostTextValid("")
        setSelectedFeelings(new Set<Feeling>());
    }


    return (
        <Popup trigger={<button className={styles.create_button}><FaPen/>shout</button>} modal nested>
            {close => {
                return (
                    <section className={styles.container}>
                        <div className={styles.modal}>
                            <div className={styles.profile}>
                                <User name={"@hesham09"} backgroundColor={"#9DB2BF"}/>
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
                                    disabled={!(selectedFeelings.size > 0) || (isPostTextValid == "")}
                                    onClick={() => {
                                        handleCreatePost();
                                        close()
                                    }}>
                                    create post <IoSend/>
                                </button>
                            </div>

                            <button className={styles.close} onClick={close}>
                                &times;
                            </button>
                        </div>
                    </section>
                );
            }}
        </Popup>
    )
}