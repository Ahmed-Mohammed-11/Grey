'use client'
import styles from './page.module.css'
import React, { useState, useEffect } from 'react';
import { Box } from '@mui/system';
import Posts from '@/app/components/posts/page';
import SideBar from '@/app/components/sidebar/page';

export default function Feed() {
  const [selectedPageIndex, setSelectedPageIndex] = useState(0);
  const [feedData, setFeedData] = useState(null);
  const [Auth, setAuth] = useState<string | null>("")
  useEffect(() => {
    const auth = localStorage.getItem('Authorization');
    setAuth(auth);
  }, []); // Empty dependency array to run the effect only once during mount

  const urlBase = 'http://localhost:8080';

  const endpointMapping: { [key: number]: string } = {
    0: '/posts/feed',
    1: '/posts/explore',
    2: '/posts/diary',
    3: '/posts/test',
    4: '/posts/profile',
    5: '/posts/settings',
  };

  const fetchData = async () => {
    try {
      const endpoint = endpointMapping[selectedPageIndex];
      if (!endpoint) {
        console.error('Invalid selectedPageIndex:', selectedPageIndex);
        return;
      }

      const headers = {
        'Content-Type': 'application/json',
        Authorization: localStorage.getItem('Authorization')!,
        mode: 'cors',
      };
      
      // this is mocked will be replaced and filled from the user
      const postFilterDTO = {
        "pageNumber": 0,
        "pageSize": 3
      }

      const response = await fetch(urlBase + endpoint, {
        method: 'POST',
        body: JSON.stringify(postFilterDTO),
        headers,
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const data = await response.json();
      console.log(data.content);
      setFeedData(data.content);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  useEffect(() => {
    fetchData();
  }, [selectedPageIndex]);

  const handleChange = (newSelectedPageIndex: number) => {
    setSelectedPageIndex(newSelectedPageIndex);
  };

  return (
    <Box className={styles.container}>
      <SideBar width={'25%'} onChange={handleChange} />
      <Posts width={'75%'} data={feedData} />
    </Box>
  );
}
