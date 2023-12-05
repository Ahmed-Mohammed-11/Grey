'use client'
import styles from './page.module.css'
import React, { useState, useEffect } from 'react';
import { useInView } from "react-intersection-observer"
import { Box } from '@mui/system';
import Posts from '@/app/components/posts/page';
import SideBar from '@/app/components/sidebar/page';
import PostDTO from '../models/dtos/PostDTO';

const Feed = () => {
  const [selectedFeedIndex, setSelectedFeedIndex] = useState(0);
  const [pageIndex, setPageIndex] = useState(0);
  const [pageSize, setPageSize] = useState(5);
  const [feedData, setFeedData] = useState<null | PostDTO[]>(null);
  const [totalNumberOfPages, setTotalNumberOfPages] = useState(10);
  const [auth, setAuth] = useState<string | null>(null);
  const {ref, inView } = useInView();
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);


  const handleDateChange = (newDate:Date) => {
    setSelectedDate(newDate);
  };

  useEffect(() => {
    setAuth(localStorage.getItem('Authorization'));
  }, []); // Empty dependency array to run the effect only once during mount

  const urlBase = 'http://localhost:8080';

  const endpointMapping: Record<number, string> = {
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
    setPageIndex(0)
    setFeedData(null)
    loadMore()
  }, [selectedFeedIndex, selectedDate]);

  useEffect(() =>{
    if(inView){
      console.log("scrolled to the end")
      setPageIndex(prevPageIndex => Math.min(prevPageIndex + 1, totalNumberOfPages - 1));
    }
  }, [inView])

  useEffect(() => {
    loadMore();
  }, [pageIndex]);

  const loadMore = async () => {
    try {
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
      // TODO set this variables by a date picker 
      // if attributes is null it means no date filter is applied
      const day = null;
      const month = null; 
      const year = null;

      const postFilterDTO = {
        pageNumber: pageIndex,
        pageSize: pageSize,
        day: day,
        month: month,
        year: year
      };
  
      const response = await fetch(urlBase + endpoint, {
        method: 'POST',
        body: JSON.stringify(postFilterDTO),
        headers,
      });
  
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
  
      const newData = await response.json();
      console.log(newData);
      setTotalNumberOfPages(newData.totalPages);
      setFeedData((prevData) => [...(prevData ?? []), ...newData.content]);
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
// TODO: what if you reached the end of all the pages and a user posted a new post
