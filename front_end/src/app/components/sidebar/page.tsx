"use client"
import Button from '@mui/material/Button';
import styles from "./page.module.css"
import {Box} from "@mui/system";
import {MdExplore, MdReport} from "react-icons/md";
import {AiFillHome} from "react-icons/ai";
import {FaUserLarge} from "react-icons/fa6";
import {BsFillBookmarkFill} from "react-icons/bs";
import User from "@/app/components/sidebar/user";
import {FaBookOpen} from "react-icons/fa";
import {useState} from 'react';
import {Icon} from "@mui/material";

export default function SideBar(props: any) {
  const [activeTab, setActiveTab] = useState(0);
  const username = localStorage.getItem('username')!;

  const buttonsText = ["feed", "explore", "diary", "saved", "profile", "reported"];
  const buttonsIcons = [
    <AiFillHome />,
    <MdExplore />,
    <FaBookOpen />,
    <BsFillBookmarkFill />,
    <FaUserLarge />,
    <MdReport />
  ];

    const handleButtonClick = (index: number) => {
        if (props.onChange) {
            props.onChange(index);
        }
        setActiveTab(index);
    };

  const buttons = buttonsText.map((text, index) => {
    return (
      <Button
        key={index}
        className={`${styles.button} ${index === activeTab ? styles.active : styles.button}`}
        onClick={() => handleButtonClick(index)}>
        <Icon className={styles.icon}>{buttonsIcons[index]}</Icon>
        {text}
        <span className={styles.line}></span>
      </Button>
    );
  });

    return (
        <Box className={styles.side_bar} width={props.width}>
            <User name={'@' + username} backgroundColor={"#c3dff8"}></User>
            <Box className={styles.container_buttons}>{buttons}</Box>
        </Box>
    )
}
