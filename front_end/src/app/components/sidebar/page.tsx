"use client"
import Button from '@mui/material/Button';
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {MdExplore} from "react-icons/md";
import {AiFillHome} from "react-icons/ai";
import {FaUserLarge} from "react-icons/fa6";
import {RiSettings4Fill} from "react-icons/ri";
import {BsFillBookmarkFill} from "react-icons/bs";
import Profile from "@/app/components/sidebar/profile";
import PopupScreen from "@/app/components/popup/page";
import { FaBookOpen } from "react-icons/fa";
import { useState } from 'react';

export default function SideBar(props:any) {
  const [activeTab, setActiveTab] = useState(4);

  const buttonsText = ["feed", "explore", "diary", "saved", "profile", "settings"];
  const buttonsIcons = [
    <AiFillHome />,
    <MdExplore />,
    <FaBookOpen />,
    <BsFillBookmarkFill />,
    <FaUserLarge />,
    <RiSettings4Fill />,
  ];

  const handleButtonClick = (index:number) => {
    setActiveTab(index);
    if (props.onChange) {
      props.onChange(index);
    }
  };

  const buttons = buttonsText.map((text, index) => {
    return (
      <Button
        key={index}
        className={`${styles.button} ${index === activeTab ? styles.active : styles.button}`}
        onClick={() => handleButtonClick(index)}
      >
        {buttonsIcons[index]}
        {text}
      </Button>
    );
  });

    return (
        <Box className={styles.side_bar} width={props.width}>
            <Profile name={"@hesham09"}></Profile>
            <PopupScreen/>
            <Box className={styles.container_buttons}>{buttons}</Box>
        </Box>
    )
}
