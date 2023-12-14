'use client'
import styles from './page.module.css'
import React, { useState, useEffect } from 'react';
import { Box } from '@mui/system';
import Posts from '@/app/components/posts/page';
import SideBar from '@/app/components/sidebar/page';
import { DIARY_ENDPOINT, EXPLORE_ENDPOINT, FEED_ENDPOINT, SAVED_ENDPOINT } from '../constants/apiConstants';
import Profile from '../profile/page';

const Feed = () => {

  const endpointMapping: Record<number, string> = {
    0: FEED_ENDPOINT,
    1: EXPLORE_ENDPOINT,
    2: DIARY_ENDPOINT,
    3: SAVED_ENDPOINT,
  };

  const [selectedFeedIndex, setSelectedFeedIndex] = useState(0);
  const posts = <Posts width={'75%'} feedType={endpointMapping[selectedFeedIndex]}  />
  const profile = <Profile />
  const [display, setDisplay] = useState(profile)

  const handleChange = (newSelectedPageIndex: number) => {
    if (newSelectedPageIndex == 4) {
      setDisplay(profile)
    } else {
      setDisplay(posts)
      setSelectedFeedIndex(newSelectedPageIndex);
    }
  };
  
  return (
    <div>
      <Box className={styles.container}>
        <SideBar width={'25%'} onChange={handleChange} />
        {display}
      </Box>
    </div>
  );
};

export default Feed;
