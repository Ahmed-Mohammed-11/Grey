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
import PostFilterDTO from '../../models/dtos/PostFilterDTO';

export default function Feed(props: any) {
    const {ref, inView} = useInView();
    const [auth, setAuth] = useState<string | null>(null);
    const [totalNumberOfPages, setTotalNumberOfPages] = useState(10);
    const [posts, setPosts] = useState<any[]>([]);
    const [filterData, setFilterData] = useState<PostFilterDTO>({} as PostFilterDTO);

    useEffect(() => {
        setAuth(localStorage.getItem('Authorization'));
    }, []);

    useEffect(() => {
        setFilterData((prevFilterData) => ({
            ...prevFilterData,
            pageNumber: 0,
            pageSize: 5,
        }));
        setPosts([])
    }, [props.feedType]);

    useEffect(() => {
        if (inView) {
            setFilterData((prevFilterData) => ({
                ...prevFilterData,
                pageNumber: Math.max(Math.min(prevFilterData.pageNumber + 1, totalNumberOfPages - 1), 0),
                pageSize: 5,
            }));
        }
    }, [inView])

    useEffect(() => {
        console.log("from use effect")
        loadMore();
    }, [filterData]);

    const loadMore = async () => {
        try {
            const headers = {
                'Content-Type': 'application/json',
                Authorization: auth!,
                mode: 'cors',
            };
            console.log(filterData)

            let response: Response

            // TODO change all the requests to get requests
            // TODO handle the case where backend sends a list not a page
            if (props.feedType == EXPLORE_ENDPOINT) {
                response = await fetch(BASE_BACKEND_URL + props.feedType
                    + "?pageNumber=" + filterData.pageNumber + "&pageSize=" + filterData.pageSize, {
                    method: 'GET',
                    headers,
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
        
                const explorePosts = await response.json();
        
                setPosts((prevPosts) => {
                    if (explorePosts && explorePosts.length > 0) {
                        return [...(prevPosts ?? []), ...explorePosts];
                    } else {
                        return prevPosts ?? [];
                    }
                });


            } else {
                response = await fetch(BASE_BACKEND_URL + props.feedType, {
                    method: 'POST',
                    body: JSON.stringify(filterData),
                    headers,
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
    
                const newData = await response.json();

                setTotalNumberOfPages(newData.totalPages);
                setPosts((prevPosts) => {
                    if (newData.content && newData.content.length > 0) {
                        return [...(prevPosts ?? []), ...newData.content];
                    } else {
                        setTotalNumberOfPages(0)
                        return [];
                    }
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
        return posts.map((post: any) => <Post key={post.id} post={post}/>);
    };

    return (
        <Box className={styles.feed} width={props.width}>
            <PostFilters showDatePicker={true} filterDTO={filterData} applyFilters={applyFilters}/>
            {renderPosts()}
            {totalNumberOfPages - 1 !== filterData.pageNumber && (
                <div className={styles.postSkeleton} ref={ref}>
                    <div className={styles.postContent}>
                        <Skeleton variant="circular" width={70} height={70}/>
                        <div className={styles.additionalContent}>
                            <Skeleton height={20} width="60%"/>
                            <Skeleton height={20} width="80%"/>
                        </div>
                    </div>
                </div>
            )}
            <ToastContainer/>
        </Box>
    )
}
