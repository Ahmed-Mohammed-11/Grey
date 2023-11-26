import Image from 'next/image'
import styles from './page.module.css'
import SideBar from "@/app/SideBar/page";
import {Box} from "@mui/system";
import Feed from "@/app/Feed/page";

export default function Home() {

  return (
      <Box className={styles.container}>
        <SideBar width={"25%"}/>
        <Feed width={"75%"}/>
      </Box>
  )
}
