// SideBar.js
import Button from '@mui/material/Button';
import styles from "./page.module.css"
import { Box } from "@mui/system";
import { MdExplore } from "react-icons/md";
import { AiFillHome } from "react-icons/ai";
import { FaUserLarge } from "react-icons/fa6";
import { RiSettings4Fill } from "react-icons/ri";
import { BsFillBookmarkFill } from "react-icons/bs";
import { FaBookOpen } from "react-icons/fa";
import Profile from "@/app/SideBar/profile";
import { useState } from 'react';

export default function SideBar(props:any) {
  const [activeTab, setActiveTab] = useState(0);

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

  //TODO: change this name to be the user name 
  return (
    <Box className={styles.side_bar} width={props.width}>
      <Profile name={"@hesham09"}></Profile>
      <Box className={styles.container_buttons}>{buttons}</Box>
    </Box>
  );
}
