'use client'
import styles from './page.module.css'
import React, { useState, useEffect } from 'react';
import { Box } from '@mui/system';
import Posts from '@/app/components/posts/page';
import SideBar from '@/app/components/sidebar/page';
import { DIARY_ENDPOINT, EXPLORE_ENDPOINT, FEED_ENDPOINT } from '../constants/apiConstants';

const Feed = () => {
  const [selectedFeedIndex, setSelectedFeedIndex] = useState(0);

  const endpointMapping: Record<number, string> = {
    0: FEED_ENDPOINT,
    1: EXPLORE_ENDPOINT,
    2: DIARY_ENDPOINT,
    3: '/posts/test', // this is mocked will be changed
    4: '/posts/profile', // this is mocked will be changed
    5: '/posts/settings', // this is mocked will be changed
  };

  const handleChange = (newSelectedPageIndex: number) => {
    setSelectedFeedIndex(newSelectedPageIndex);
  };
  
  return (
    <div>
      <Box className={styles.container}>
        <SideBar width={'25%'} onChange={handleChange} />
        <Posts width={'75%'} feedType={endpointMapping[selectedFeedIndex]}  />
      </Box>
    </div>
  );
};

export default Feed;
// TODO: what if you reached the end of all the pages and a user posted a new post
