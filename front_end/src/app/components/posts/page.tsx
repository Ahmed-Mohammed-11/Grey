'use client';
import styles from "./page.module.css"
import {Box} from "@mui/system";
import Post from "@/app/components/post/page";
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import React, { useState, useEffect } from 'react';
import { useInView } from "react-intersection-observer"
import {BASE_BACKEND_URL, DIARY_ENDPOINT, EXPLORE_ENDPOINT} from "@/app/constants/apiConstants";
import { Skeleton } from '@mui/material';
import PostFilters from "../postFilter/page";
import PostFilterDTO from '../../models/dtos/PostFilterDTO';

export default function Feed(props: any) {
    const {ref, inView} = useInView();
    const [auth, setAuth] = useState<string | null>(null);
    const [totalNumberOfPages, setTotalNumberOfPages] = useState(1);
    const [posts, setPosts] = useState<any[]>([]);
    const [filterData, setFilterData] = useState<PostFilterDTO>({} as PostFilterDTO);
    const [pageIndex, setPageIndex] = useState<number>(0);
    const [lastPage, setLastPage] = useState<boolean>(false);

    useEffect(() => {
      setAuth(localStorage.getItem('Authorization'));
    }, []);

    useEffect(() => {
      setFilterData({} as PostFilterDTO);
    }, [props.feedType]);

    useEffect(() => {
      setPosts([])
      if(pageIndex == 0) {
        console.log("in index == 0")
        loadMore();
      }
      else setPageIndex(0);
    }, [filterData]);

    useEffect(() =>{
      console.log("from view")
      if(inView && posts.length != 0){
        setPageIndex(Math.max(Math.min(pageIndex + 1, totalNumberOfPages - 1), 0))
      }
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

            let response: Response

            // TODO change all the requests to get requests
            // TODO handle the case where backend sends a list not a page
            if (props.feedType == 1) {
                response = await fetch(BASE_BACKEND_URL + props.feedTypeEndPoint
                    + "?pageNumber=" + pageIndex + "&pageSize=" + '5', {
                    method: 'GET',
                    headers,
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const explorePosts = await response.json();

                setPosts((prevPosts) => {
                  return [...(prevPosts ?? []), ...explorePosts];
                });
           } else {
              response = await fetch(BASE_BACKEND_URL + props.feedTypeEndPoint, {
                method: 'POST',
                body: JSON.stringify({...(filterData),pageNumber: pageIndex, pageSize:5}),
                headers,
              });

              if (!response.ok) {
                throw new Error('Network response was not ok');
              }

              const newData = await response.json();
              setTotalNumberOfPages(newData.totalPages);
              setLastPage(newData.last)
              setPosts((prevPosts) => {
                return [...(prevPosts ?? []), ...newData.content];
              });
           }
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    const applyFilters = (newData: PostFilterDTO) => {
        setFilterData((prevFilterData) => ({
            ...prevFilterData,
            ...Object.fromEntries(
                Object.entries(newData).filter(([key, value]) => value !== undefined)
            ),
        }));
    };

    const renderPosts = () => {
        return posts.map((post: any) => <Post key={post.id} post={post} feedType={props.feedType}/>);
    };

    return (
        <Box className={styles.feed} width={props.width}>
          <PostFilters showDatePicker={props.feedType == 2} showFeelingSelection={props.feedType === 0} applyFilters={applyFilters}/>
          {renderPosts()}
          {!lastPage && (
            <div className={styles.postSkeleton} ref={ref}>
              <div className={styles.postContent}>
                <Skeleton variant="circular" width={70} height={70} />
                <div className={styles.additionalContent}>
                  <Skeleton height={20} width="60%" />
                  <Skeleton height={20} width="80%" />
                  </div>
              </div>
            </div>
            )}
            <ToastContainer/>
        </Box>
    )
}