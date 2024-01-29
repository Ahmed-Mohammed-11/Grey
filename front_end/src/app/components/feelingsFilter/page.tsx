"use client";
import React from 'react';
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {IoMdAdd} from "react-icons/io";
import {Chip, IconButton, Menu, MenuItem, Tooltip} from "@mui/material";
import FaceIcon from '@mui/icons-material/Face';
import Feeling from "@/app/entities/dtos/Feeling";
import {MdOutlineEmojiEmotions} from "react-icons/md";
import {BsEmojiAngry, BsEmojiFrown, BsEmojiHeartEyes, BsEmojiSurprise} from "react-icons/bs";
import {SlOptions} from "react-icons/sl";
import {BiAngry} from "react-icons/bi";

export default function FeelingsFilter(props: any) {
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const [selectedFeelings, setSelectedFeelings] = React.useState(new Set<Feeling>());
    const handleClosePopup = () => setAnchorEl(null);
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => setAnchorEl(event.currentTarget);

    // Feelings chips
    const [isFeelingsValid, setIsFeelingsValid] = React.useState(false);
    const [fullFeelings, setFullFeelings] = React.useState(false);

    const allFeelings = new Set<Feeling>(
        [
            Feeling.HAPPY,
            Feeling.ANXIOUS,
            Feeling.DISGUST,
            Feeling.ANGER,
            Feeling.LOVE,
            Feeling.FEAR,
            Feeling.SAD,
            Feeling.INSPIRE
        ]
    );

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

    const handleFeelingsChange = () => {
        if (selectedFeelings.size === 0) setIsFeelingsValid(false);
        else setIsFeelingsValid(true);
        if (selectedFeelings.size < props.limit) setFullFeelings(false);
        else setFullFeelings(true);
        props.onDataChange(selectedFeelings);
    };

    const handleAdd = (feeling: Feeling) => () => {
        if (selectedFeelings.size < props.limit) {
            selectedFeelings.add(feeling);
            setSelectedFeelings(new Set(selectedFeelings));
            handleFeelingsChange();
        }
    };

    const handleDelete = (chipToDelete: Feeling) => () => {
        selectedFeelings.delete(chipToDelete);
        setSelectedFeelings(new Set(selectedFeelings));
        handleFeelingsChange();
    };

    return (
        <Box className={styles.feelings}>
            <Box>
                {Array.from(selectedFeelings).map((feeling: any, feelingIndex: any) => (
                    <Chip
                        //initiate to empty object to avoid error
                        icon={emojiMap.get(feeling) ?  emojiMap.get(feeling) : <FaceIcon/>}
                        label = {feeling}
                        className={`${feeling} ${styles.chip}`}
                        style={{margin: '0.5%', borderRadius: '5px'}}
                        onDelete={handleDelete(feeling)}
                        key={feelingIndex}
                    />
                ))}
            </Box>

            <Tooltip title="Feelings Filter">
                <IconButton
                    id="basic-button"
                    disabled={fullFeelings}
                    aria-controls={open ? 'basic-menu' : undefined}
                    aria-haspopup="true"
                    aria-expanded={open ? 'true' : undefined}
                    className={styles.add_feeling_button}
                    onClick={handleClick}
                >
                    Select Feeling <IoMdAdd/>
                </IconButton>
            </Tooltip>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClosePopup}
                MenuListProps={{'aria-labelledby': 'basic-button'}}
            >
                {Array.from(allFeelings).filter((feeling) =>
                    !selectedFeelings.has(feeling)).map((feeling: any, feelingIndex: any) => (
                    <MenuItem className={`${feeling} ${styles.menu_item}`} onClick={handleAdd(feeling)}>
                        {emojiMap.get(feeling) ?  emojiMap.get(feeling) : <FaceIcon/>} <span>&nbsp;</span> {feeling}
                    </MenuItem>
                ))}
            </Menu>
        </Box>
    );
}
