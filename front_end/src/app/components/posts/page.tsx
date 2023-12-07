import styles from "./page.module.css"
import {Box} from "@mui/system";
import Post from "@/app/components/post/page";
import Feeling from '../../models/dtos/Feeling';
import { ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

export default function Posts(props: any) {
    const dd = {
        postText: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et" +
            " dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip" +
            " ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu" +
            " fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia" +
            " deserunt mollit anim id est laborum.",
        postFeelings: new Set<Feeling>([Feeling.DISGUST, Feeling.HAPPY, Feeling.FEAR])
    }
    const posts = [dd, dd, dd, dd, dd, dd]

    return (
        <Box className={styles.feed} width={props.width}>
            <Post posts={posts}/>
            <ToastContainer />
        </Box>
    )
}
