'use client'
import styles from './page.module.css'
import React, { useState, useEffect } from 'react';
import { useInView } from "react-intersection-observer"
import { Box } from '@mui/system';
import Posts from '@/app/components/posts/page';
import SideBar from '@/app/components/sidebar/page';

const Feed = () => {
  const [selectedFeedIndex, setSelectedFeedIndex] = useState(0);
  const [pageIndex, setPageIndex] = useState(100);
  const [pageSize, setPageSize] = useState(5);
  const [feedData, setFeedData] = useState(null);
  const [auth, setAuth] = useState<string | null>(null);
  const {ref, inView } = useInView();

  useEffect(() => {
    setAuth(localStorage.getItem('Authorization'));
  }, []); // Empty dependency array to run the effect only once during mount

  const urlBase = 'http://localhost:8080';

  const endpointMapping = {
    0: '/posts/feed',
    1: '/posts/explore',
    2: '/posts/diary',
    3: '/posts/test',
    4: '/posts/profile',
    5: '/posts/settings',
  };

  const handleChange = (newSelectedPageIndex: number) => {
    setSelectedFeedIndex(newSelectedPageIndex);
  };

  useEffect(() => {
    console.log("diary")
    setPageIndex(0)
    setFeedData(null)
    loadMore()
  }, [selectedFeedIndex]);

  useEffect(() =>{
    if(inView){
      console.log("scrolled to the end")
      setPageIndex(prevPageIndex => prevPageIndex + 1);
    }
  }, [inView])

  useEffect(() => {
    console.log("change the page index")
    loadMore();
  }, [pageIndex]);

  const loadMore = async () => {
    try {
      console.log(pageIndex)
      console.log(pageIndex + 1)
      console.log(pageIndex)
      const endpoint = endpointMapping[selectedFeedIndex];
      if (!endpoint) {
        console.error('Invalid selectedFeedIndex:', selectedFeedIndex);
        return;
      }

      const headers = {
        'Content-Type': 'application/json',
        Authorization: auth!,
        mode: 'cors',
      };
      console.log(pageIndex)
      const postFilterDTO = {
        pageNumber: pageIndex,
        pageSize: pageSize,
      };

      const response = await fetch(urlBase + endpoint, {
        method: 'POST',
        body: JSON.stringify(postFilterDTO),
        headers,
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const data = await response.json();
      console.log(data);
      setFeedData((prevData) => [...(prevData ?? []), ...data.content]);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  return (
    <div>
      <Box className={styles.container}>
        <SideBar width={'25%'} onChange={handleChange} />
        <Posts width={'75%'} data={feedData} />
      </Box>
      <Box>
        <div className={styles.load}/>
        <div ref = {ref}/>
      </Box>
    </div>
  );
};

export default Feed;
// TODO: restrict not sending post request if there is no more pages
