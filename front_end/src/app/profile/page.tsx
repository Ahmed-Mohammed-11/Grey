'use client'
import { Box, TextField } from "@mui/material";
import styles from './page.module.css'

function Profile() {

  console.log(localStorage.getItem("Authorization"))


  return (
    <Box className={styles.container}>
      <Box className={styles.signupForm}>
        <TextField className={styles.textArea}
          label='Username'
          placeholder='username'
        >
        </TextField>
        <TextField className={styles.textArea}
          label='email'
          placeholder='user email'
        ></TextField>
        <TextField className={styles.textArea}
          label='password'
          placeholder='password'
        ></TextField>
      </Box>
    </Box>
  )
}

export default Profile;