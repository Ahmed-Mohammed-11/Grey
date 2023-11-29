'use client'

import styles from './page.module.css'
import SideBar from "@/app/SideBar/page";
import { Box } from "@mui/system";
import Feed from "@/app/Feed/page";
import { useState, useEffect } from 'react';

export default function Home() {
  const [selectedPageIndex, setSelectedPageIndex] = useState(0);
  const [feedData, setFeedData] = useState(null);

  const urlBase = 'http://localhost:8080'

  const endpointMapping = {
    0: '/posts/feed', // this is still mocked
    1: '/posts/explore',// this is still mocked
    2: '/posts/diary',
    3: '/posts/test',// this is still mocked
    4: '/posts/profile',// this is still mocked
    5: '/posts/settings',// this is still mocked
  };

  const fetchData = async () => {
    try {
      const endpoint = endpointMapping[selectedPageIndex];
      if (!endpoint) {
        console.error('Invalid selectedPageIndex:', selectedPageIndex);
        return;
      }

      const response = await fetch(urlBase + endpoint, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({pageNumber:0, pageSize:10}),
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const data = await response.json();
      console.log(data);
      setFeedData(data.content);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  useEffect(() => {
    fetchData();
  }, [selectedPageIndex]);

  const handleChange = (newSelectedPageIndex:number) => {
    setSelectedPageIndex(newSelectedPageIndex);
  };

  return (
    <Box className={styles.container}>
      <SideBar width={"25%"} onChange={handleChange} />
      <Feed width={"75%"} data={feedData} />
    </Box>
  );
}
