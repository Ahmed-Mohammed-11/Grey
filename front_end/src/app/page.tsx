import Image from 'next/image'
import styles from './page.module.css'
import Button from '@mui/material/Button';
import Post from './components/Post';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export default function Home() {
  return (
    <div>
        <Post />
        <ToastContainer />
    </div>
  )
}

export default Home

