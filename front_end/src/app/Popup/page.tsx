"use client";
import React from 'react';
import Popup from 'reactjs-popup';
import styles from "./page.module.css"
import Profile from "@/app/SideBar/profile";
import {Box} from "@mui/system";
import {FaPen} from "react-icons/fa";
import {IoSend} from "react-icons/io5";
import {IoMdAdd} from "react-icons/io";
import {Chip, IconButton, ListItem, Menu, MenuItem, TextareaAutosize, Tooltip} from "@mui/material";
import Feeling from "@/app/DTO/Feeling";
import PostDTO from "@/app/DTO/PostDTO";
import postController from "@/app/services/postController";
import {createPostEndPoint} from "@/app/constants/apiConstants";

const allFeelings = new Set<Feeling>([Feeling.HAPPY, Feeling.SAD,
    Feeling.ANGER, Feeling.DISGUST,
    Feeling.FEAR, Feeling.ANXIOUS,
    Feeling.INSPIRE, Feeling.LOVE]);

export default function PopupScreen() {

    // Feelings drop down menu
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const handleClose = () => {setAnchorEl(null);};
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {setAnchorEl(event.currentTarget);};


    // Feelings chips
    const [selectedFeelings, setSelectedFeelings] = React.useState(new Set<Feeling>());
    const [isFeelingsValid, setIsFeelingsValid] = React.useState(false);
    const [fullFeelings, setFullFeelings] = React.useState(false);

    const handleFeelingsChange = () => {
        if(selectedFeelings.size === 0) setIsFeelingsValid(false)
        else setIsFeelingsValid(true)

        if (selectedFeelings.size < 3) setFullFeelings(false)
        else setFullFeelings(true)
    }
    const handleAdd = (feeling: Feeling) => () => {
        if (selectedFeelings.size < 3)
            setSelectedFeelings((feelings) => {
                feelings.add(feeling);
                return new Set<Feeling>(feelings);
            });
        handleFeelingsChange();
    };
    const handleDelete = (chipToDelete: Feeling) => () => {
        setSelectedFeelings((feelings) => {
            feelings.delete(chipToDelete);
            return new Set<Feeling>(feelings);
        });
        handleFeelingsChange();
    };


    // Handling post text
    const [isPostTextValid, setIsPostTextValid] = React.useState("");
    const handlePostText = (event: React.ChangeEvent<HTMLTextAreaElement>) => {setIsPostTextValid(event.target.value);}
    const handleCreatePost = () => {
        let post: PostDTO = {
            postText: isPostTextValid,
            postFeelings: selectedFeelings
        }
        isFeelingsValid && isPostTextValid && sendInfoToServer(post)
    }


    // send post to server
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

                            <Box className={styles.feelings}>
                                <ListItem>
                                    {Array.from(selectedFeelings).map((feeling: any, feelingIndex: any) => (
                                        <Chip
                                            label={feeling}
                                            className={feeling}
                                            onDelete={handleDelete(feeling)}
                                            key={feelingIndex}/>
                                    ))}
                                </ListItem>
                                <Tooltip title="Add Feeling">
                                    <IconButton
                                        id="basic-button"
                                        disabled={fullFeelings}
                                        className={styles.feeling_button}
                                        aria-controls={open ? 'basic-menu' : undefined}
                                        aria-haspopup="true"
                                        aria-expanded={open ? 'true' : undefined}
                                        onClick={handleClick}>
                                        <IoMdAdd/>
                                    </IconButton>
                                </Tooltip>
                                <Menu
                                    id="basic-menu"
                                    anchorEl={anchorEl}
                                    open={open}
                                    onClose={handleClose}
                                    MenuListProps={{'aria-labelledby': 'basic-button',}}>
                                    {Array.from(allFeelings).filter((feeling) =>
                                        !selectedFeelings.has(feeling)).map((feeling: any, feelingIndex: any) => (
                                        <MenuItem className={feeling} onClick={handleAdd(feeling)}>
                                            {feeling}
                                        </MenuItem>
                                    ))}
                                </Menu>

                            </Box>

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
                                        ${!isFeelingsValid || !isPostTextValid ? styles.disabled : ""}`}
                                        disabled={!isFeelingsValid || !isPostTextValid}
                                        onClick={handleCreatePost}>
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
        </Popup>)
}