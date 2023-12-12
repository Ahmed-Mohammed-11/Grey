'use client'
import { Box, Button, TextField } from "@mui/material";
import styles from './page.module.css';
import {useEffect, useRef, useState} from 'react';
import clientValidateForm from "../security/userValidation/clientFormValidation";
import updateUserController from "../services/updateUserController";
import { UPDATE_USER_ENDPOINT } from "../constants/apiConstants";
import toJSON from "../utils/readableStreamResponseBodytoJSON";
function Profile() {

  const usernameRef = useRef<HTMLInputElement>(null);
  const emailRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLInputElement>(null);

  const [isUserValid, setIsUserValid] = useState({
    username: true,
    email: true,
    password: true
  });

  const [errors, setErrors] = useState({
    username: "",
    email: "",
    password: ""
  });

  const fetchServerResponse = async (userDto: UserDTO) => {
    
  }

  const sendRequest = (user: User) => {
    let userDto: UserDTO = {
      username: user.username,
      email: user.email,
      password: user.password
    }

    fetchServerResponse(userDto)
  }

  const handleUpdate = () => {
    let user: User = {
      username: usernameRef.current!.value,
      email: emailRef.current!.value,
      password: passwordRef.current!.value
    }

    let {isUserValid, errors} = clientValidateForm(user)
    setIsUserValid(isUserValid)
    setErrors(errors)

    isUserValid.username && isUserValid.email && isUserValid.password && sendRequest(user)
  }

  return (
    <Box className={styles.container}>
      <Box className={styles.signupForm}>
        <TextField className={styles.textArea}
          label='Username'
          placeholder='new username'
          inputRef={usernameRef}
          variant="filled"
          error={!isUserValid.username}
          helperText={(isUserValid.username)? "" : errors.username}
        >
        </TextField>
        <TextField className={styles.textArea}
          label='Email'
          placeholder='new email'
          inputRef={emailRef}
          variant="filled"
          error={!isUserValid.email}
          helperText={(isUserValid.email)? "" : errors.email}
        ></TextField>
        <TextField className={styles.textArea}
          label='Password'
          placeholder='new password'
          inputRef={passwordRef}
          variant="filled"
          error={!isUserValid.password}
          helperText={(isUserValid.password)? "" : errors.password}
        ></TextField>
        <Button className={styles.button}
          variant="contained"
          size="large"
          onClick={handleUpdate}
        >save</Button>
      </Box>
    </Box>
  )
}

export default Profile;