'use client';
import styles from "./page.module.css"
import {Box} from "@mui/system";
import Post from "@/app/components/post/page";
import {toast, ToastContainer, ToastOptions} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import React, {useEffect, useState} from 'react';
import {useInView} from "react-intersection-observer"
import {BASE_BACKEND_URL} from "@/app/constants/apiConstants";
import {Skeleton} from '@mui/material';
import PostFilters from "../postFilter/page";
import PostFilterDTO from '@/app/entities/dtos/PostFilterDTO';
import PopupScreen from "@/app/components/popup/page";
import ReportedPost from "@/app/components/reported post/page";
import notifyToast, {toastStyleTopRight} from "@/app/utils/notifyToast";
import toastResponse from "@/app/utils/notifyToast";

export default function Posts(props: any) {
    const {ref, inView} = useInView();
    const [auth, setAuth] = useState<string | null>(null);
    const [posts, setPosts] = useState<any[]>([]);
    const [filterData, setFilterData] = useState<PostFilterDTO>({} as PostFilterDTO);
    const [pageIndex, setPageIndex] = useState<number>(0);
    const [lastPage, setLastPage] = useState<boolean>(false);
    const [emptyPosts, setEmptyPosts] = useState<boolean>(false);

    const datePickerActiveIndex = [2, 3];
    const feelingsActiveIndex = [0, 3];

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
            if (inView)
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
                if(response.status == 403) {
                    toast.error("You are not authorized to see this", toastStyleTopRight);
                    // setEmptyPosts(true);
                }
                throw new Error('Network response was not ok');
            }

            const newData = await response.json();
            setLastPage(newData.last)
            setPosts((prevPosts) => {
                // filter new coming posts to get only unique post ids only (to avoid duplicates) with previous posts
                const uniqueIds = new Set(prevPosts?.map((post) => post.id));
                newData.content = newData.content.filter((post: any) => !uniqueIds.has(post.id));
                return [...(prevPosts ?? []), ...newData.content];
            });
            // if(newData.content.length == 0) {
            //     console.log("empty posts")
            //     setEmptyPosts(true);
            // }else {
            //     console.log(newData.content.length)
            //     console.log(posts.length)
            //     console.log("not empty posts")
            //     setEmptyPosts(false);
            // }
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
        return props.feedType === 5 ?
            posts.map((post: any) => <ReportedPost post={post} setPosts={setPosts} posts={posts} />) :
            posts.map((post: any) => <Post key={post.id} post={post} feedType={props.feedType} setPosts={setPosts} posts={posts} />);
    };

    const renderEmptyPosts = () => {
        return (
            <Box className={styles.empty_posts}>
                {/*<span className={styles.line} />*/}

                <Box className={styles.empty_text}>
                    No posts to show
                </Box>
            </Box>
        )
    }

    return (
        <Box className={styles.feed} width={props.width}>
            <Box className={styles.posts_bar}>
                <PopupScreen setPosts={setPosts} posts={posts}/>
                <PostFilters showDatePicker = {datePickerActiveIndex.includes(props.feedType)}
                             showFeelingSelection = {feelingsActiveIndex.includes(props.feedType)}
                             applyFilters = {applyFilters}/>
            </Box>
            <Box className={styles.posts}>
                {!emptyPosts ? renderPosts() : renderPosts()}
            </Box>
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