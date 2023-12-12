"use client";
import React from 'react';
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {IoMdAdd} from "react-icons/io";
import {Chip, IconButton, ListItem, Menu, MenuItem, Tooltip} from "@mui/material";
import Feeling from "@/app/models/dtos/Feeling";

export default function FeelingsFilter(props:any) {
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const [selectedFeelings, setSelectedFeelings] = React.useState(new Set<Feeling>());
    const handleClosePopup = () => {setAnchorEl(null);};
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {setAnchorEl(event.currentTarget);};

    // Feelings chips
    const [isFeelingsValid, setIsFeelingsValid] = React.useState(false);
    const [fullFeelings, setFullFeelings] = React.useState(false);
   
    const allFeelings = new Set<Feeling>([
        Feeling.HAPPY, Feeling.SAD,
        Feeling.ANGER, Feeling.DISGUST,
        Feeling.FEAR, Feeling.ANXIOUS,
        Feeling.INSPIRE, Feeling.LOVE]);

        const handleFeelingsChange = () => {
            console.log("handle feeling")
            if(selectedFeelings.size === 0) setIsFeelingsValid(false)
            else setIsFeelingsValid(true)
            console.log("limit is", props.limit)
            if (selectedFeelings.size < props.limit) setFullFeelings(false)
            else setFullFeelings(true)
        }

        const handleAdd = (feeling: Feeling) => () => {
            if (selectedFeelings.size < props.limit)
                setSelectedFeelings((feelings) => {
                    feelings.add(feeling);
                    props.onDataChange(feelings)
                    return new Set<Feeling>(feelings);
                });
            handleFeelingsChange();
        };
        const handleDelete = (chipToDelete: Feeling) => () => {
            setSelectedFeelings((feelings) => {
                feelings.delete(chipToDelete);
                props.onDataChange(feelings)
                return new Set<Feeling>(feelings);
            });
            handleFeelingsChange();
        };


  return (
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
  );
}



