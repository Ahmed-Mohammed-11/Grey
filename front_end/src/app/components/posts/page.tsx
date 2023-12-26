'use client';
import styles from "./page.module.css"
import {Box} from "@mui/system";
import Post from "@/app/components/post/page";
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import React, {useState, useEffect} from 'react';
import {useInView} from "react-intersection-observer"
import {BASE_BACKEND_URL, DIARY_ENDPOINT, EXPLORE_ENDPOINT} from "@/app/constants/apiConstants";
import {Skeleton} from '@mui/material';
import PostFilters from "../postFilter/page";
import PostFilterDTO from '@/app/entities/dtos/PostFilterDTO';
import PopupScreen from "@/app/components/popup/page";

export default function Posts(props: any) {
    const {ref, inView} = useInView();
    const [auth, setAuth] = useState<string | null>(null);
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
        if (pageIndex == 0) {
            loadMore().then(r => console.log("loaded more"));
        } else setPageIndex(0);
    }, [filterData]);

    useEffect(() => {
        if (inView && !lastPage) {
            setPageIndex(i => i + 1);
        }
    }, [inView])

    useEffect(() => {
        loadMore().then(() => {
            if(inView)
                setPageIndex(i => i + 1);
        })
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
            if (props.feedType == 1) {
                response = await fetch(BASE_BACKEND_URL + props.feedTypeEndPoint
                    + "?pageNumber=" + pageIndex + "&pageSize=" + '5', {
                    method: 'GET',
                    headers,
                });

            } else {
                response = await fetch(BASE_BACKEND_URL + props.feedTypeEndPoint, {
                    method: 'POST',
                    body: JSON.stringify({...(filterData), pageNumber: pageIndex, pageSize: 5}),
                    headers,
                });

            }

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const newData = await response.json();
            setLastPage(newData.last)
            setPosts((prevPosts) => {
                console.log(newData);
                return [...(prevPosts ?? []), ...newData.content];
            });
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
            <Box className={styles.posts_bar}>
                <PopupScreen/>
                <PostFilters showDatePicker={props.feedType == 2} showFeelingSelection={props.feedType === 0}
                             applyFilters={applyFilters}/>
            </Box>
            {renderPosts()}
            {!lastPage && (
                <div className={styles.post_skeleton} ref={ref}>
                    <div className={styles.container}>
                        <Skeleton className={styles.chip_shape}/>
                        <Skeleton className={styles.chip_shape}/>
                    </div>
                    <Skeleton className={styles.text_shape}/>
                    <Skeleton className={styles.text_shape}/>
                </div>
            )}
            <ToastContainer/>
        </Box>
    )
}