"use client";
import React from 'react';
// import Popup from 'reactjs-popup';
import styles from "./page.module.css"
import Profile from "@/app/components/sidebar/profile";
import {Box} from "@mui/system";
import {FaPen} from "react-icons/fa";
import {IoSend} from "react-icons/io5";
import {IoMdAdd} from "react-icons/io";
import {Alert, Chip, IconButton, ListItem, Menu, MenuItem, Snackbar, TextareaAutosize, Tooltip} from "@mui/material";
import Feeling from "@/app/models/dtos/Feeling";
import PostDTO from "@/app/models/dtos/PostDTO";
import {CREATE_POST_ENDPOINT} from "@/app/constants/apiConstants";
import createPostController from "@/app/services/createPostController";
import toJSON from "@/app/utils/readableStreamResponseBodytoJSON";

const allFeelings = new Set<Feeling>([Feeling.HAPPY, Feeling.SAD,
    Feeling.ANGER, Feeling.DISGUST,
    Feeling.FEAR, Feeling.ANXIOUS,
    Feeling.INSPIRE, Feeling.LOVE]);

export default function PopupScreen() {
    // Feelings drop down menu
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const handleClosePopup = () => {setAnchorEl(null);};
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


    // Handling post text and send to server
    const [isPostTextValid, setIsPostTextValid] = React.useState("");
    const handlePostText = (event: React.ChangeEvent<HTMLTextAreaElement>) => {setIsPostTextValid(event.target.value);}
    const handleCreatePost = () => {
        let post: PostDTO = {
            postText: isPostTextValid,
            postFeelings: Array.from(selectedFeelings)
        }
        isFeelingsValid && isPostTextValid && sendInfoToServer(post)
    }

    async function sendInfoToServer(post: PostDTO) {
        // prepare user data to send to server
        let postDTO: PostDTO = {
            postText: post.postText,
            postFeelings: Array.from(post.postFeelings)
        }
        console.log(postDTO);
        fetchResponse(postDTO);
    }

    const fetchResponse = async (postDTO : PostDTO) =>`` {
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
                                    onClose={handleClosePopup}
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

                            <Snackbar open={openSnackbar} autoHideDuration={6000} onClose={handleCloseSnackbar}>
                                <Alert onClose={handleCloseSnackbar} severity="success" sx={{ width: '100%' }}>
                                    Post created successfully
                                </Alert>
                            </Snackbar>
                        </div>
                    </section>
                );
}