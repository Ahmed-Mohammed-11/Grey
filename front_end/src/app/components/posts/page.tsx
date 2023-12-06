import styles from "./page.module.css"
import {Box} from "@mui/system";
import Post from "@/app/components/post/page";
import Feeling from '../../models/dtos/Feeling';
import React, { useState, useEffect } from 'react';
import { useInView } from "react-intersection-observer"
import { BASE_BACKEND_URL, DIARY_ENDPOINT } from "@/app/constants/apiConstants";
import { Skeleton } from '@mui/material';

export default function Feed(props:any) {
    const {ref, inView } = useInView();
    const [auth, setAuth] = useState<string | null>(null);
    const [pageIndex, setPageIndex] = useState(-1);
    const [pageSize, setPageSize] = useState(5);
    const [totalNumberOfPages, setTotalNumberOfPages] = useState(10);
    const [posts, setPosts] = useState<any[]>([]);
    
    useEffect(() => {
        setAuth(localStorage.getItem('Authorization'));
    }, []);
    
    useEffect(() => {
        setPageIndex(0)
        setPosts([])
    }, [props.feedType]);

    useEffect(() =>{
        if(inView)
          setPageIndex(prevPageIndex => Math.min(prevPageIndex + 1, totalNumberOfPages - 1));
    }, [inView])

    useEffect(() => {
        loadMore();
    }, [pageIndex]);

    const loadMore = async () => {
        try {
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
      
          const response = await fetch(BASE_BACKEND_URL + props.feedType, {
            method: 'POST',
            body: JSON.stringify(postFilterDTO),
            headers,
          });
      
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
      
          const newData = await response.json();
          setTotalNumberOfPages(newData.totalPages);
          setPosts((prevPosts) => [...(prevPosts ?? []), ...newData.content]);
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      };

    const renderPosts = () => {
        return posts.map((post: any) => <Post key={post.id} post={post} />);
    };

    return (
        <Box className={styles.feed} width={props.width}>
            {renderPosts()}
            <div className={styles.postSkeleton} ref = {ref} >
                <div className={styles.postContent}>
                    <Skeleton variant="circular" width={70} height={70} />
                    <div className={styles.additionalContent}>
                        <Skeleton height={20} width="60%" />
                        <Skeleton height={20} width="80%" />
                    </div>
                </div>
            </div>
        </Box>
    )
}
