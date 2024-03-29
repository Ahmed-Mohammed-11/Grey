'use client'
import styles from './page.module.css'
import React, {useState} from 'react';
import {Box} from '@mui/system';
import Posts from '@/app/components/posts/page';
import SideBar from '@/app/components/sidebar/page';
import {
    DIARY_ENDPOINT,
    EXPLORE_ENDPOINT,
    FEED_ENDPOINT,
    REPORTED_ENDPOINT,
    SAVED_ENDPOINT,
    SIGN_IN_ROUTE
} from '../constants/apiConstants';
import Profile from '../profile/page';
import {useRouter} from "next/navigation";

const Home = () => {

  const router = useRouter();
  const endpointMapping: Record<number, string> = {
    0: FEED_ENDPOINT,
    1: EXPLORE_ENDPOINT,
    2: DIARY_ENDPOINT,
    3: SAVED_ENDPOINT,
    5: REPORTED_ENDPOINT,
  };

    const profile = <Profile/>
    const [display, setDisplay] = useState(<Posts width={'80%'}
                                                  feedTypeEndPoint={endpointMapping[0]}
                                                  feedType={0}/>)

  const handleChange = (newSelectedPageIndex: number) => {
    if (newSelectedPageIndex === 4) {
      setDisplay(profile)
    } else {
      const posts = <Posts width={'80%'}
                           feedTypeEndPoint={endpointMapping[newSelectedPageIndex]}
                           feedType={newSelectedPageIndex} />
      setDisplay(posts)
    }
  };

  const user = localStorage.getItem('Authorization') || '{}';
  (user == '{}') && (router.push(SIGN_IN_ROUTE));

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
