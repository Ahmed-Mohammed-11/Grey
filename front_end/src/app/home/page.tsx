'use client'
import styles from './page.module.css'
import React, { useState, useEffect } from 'react';
import { Box } from '@mui/system';
import Posts from '@/app/components/posts/page';
import SideBar from '@/app/components/sidebar/page';
import { DIARY_ENDPOINT, EXPLORE_ENDPOINT, FEED_ENDPOINT, SAVED_ENDPOINT } from '../constants/apiConstants';
import Profile from '../profile/page';
import {setDefaultAutoSelectFamily} from "node:net";

const Home = () => {

  const endpointMapping: Record<number, string> = {
    0: FEED_ENDPOINT,
    1: EXPLORE_ENDPOINT,
    2: DIARY_ENDPOINT,
    3: SAVED_ENDPOINT,
  };

  const profile = <Profile />
  const [display, setDisplay] = useState(<Posts width={'80%'}
                                                feedTypeEndPoint={endpointMapping[0]}
                                                feedType={0} />)

  const handleChange = (newSelectedPageIndex: number) => {
    if (newSelectedPageIndex === 4) {
      setDisplay(profile)
    } else if (newSelectedPageIndex <= 3) {
      const posts = <Posts width={'80%'}
                           feedTypeEndPoint={endpointMapping[newSelectedPageIndex]}
                           feedType={newSelectedPageIndex} />
      setDisplay(posts)
    }
  };
  
  return (
    <div>
      <Box className={styles.container}>
        <SideBar width={'20%'} onChange={handleChange} />
        {display}
      </Box>
    </div>
  );
};

export default Home;
