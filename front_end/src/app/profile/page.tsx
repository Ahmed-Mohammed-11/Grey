'use client'
import { Box, Button, TextField } from "@mui/material";
import styles from './page.module.css';
import {useRef, useState} from 'react';
import clientValidateForm from "../security/userValidation/clientFormValidation";
import updateUserController from "../services/updateUserController";
import { UPDATE_USER_ENDPOINT } from "../constants/apiConstants";
function Profile() {

  const usernameRef = useRef<HTMLInputElement>(null);
  const emailRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLInputElement>(null);

  const initialUsername = 'hesham09'
  const initialEmail = 'hamadayl3b@grey.com'
  const initialPassword = 'Everyone loves Ahmed Elnaggar'

  const [username, setUsername] = useState(initialUsername);
  const [email, setEmail] = useState(initialEmail);
  const [password, setPassword] = useState(initialPassword);

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

  const noChange = (user: User) : boolean => {
    return user.username === initialUsername && user.email === initialEmail && user.password === initialPassword
  }

  const fetchServerResponse = async (userDto: UserDTO) => {
    const response = await updateUserController.sendPutRequest(userDto, UPDATE_USER_ENDPOINT)
    const message = await response.text()
    console.log(message)
  }

  const sendRequest = (user: User) => {
    let userDto: UserDTO = {
      username: user.username,
      email: user.email,
      password: user.password
    }

    console.log(userDto)

    fetchServerResponse(userDto)
  }

  const handleUpdate = () => {
    if (!username) {
      setUsername(initialUsername)
    }
    if (!email) {
      setEmail(initialEmail)
    }
    if (!password) {
      setPassword(initialPassword)
    }

    let user: User = {
      username: username,
      email: email,
      password: password
    }

    if (noChange(user)) {
      console.log('no change')
      return
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
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          error={!isUserValid.username}
          helperText={(isUserValid.username)? "" : errors.username}
        >
        </TextField>
        <TextField className={styles.textArea}
          label='Email'
          placeholder='new email'
          inputRef={emailRef}
          variant="filled"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          error={!isUserValid.email}
          helperText={(isUserValid.email)? "" : errors.email}
        ></TextField>
        <TextField className={styles.textArea}
          label='Password'
          placeholder='new password'
          inputRef={passwordRef}
          variant="filled"
          error={!isUserValid.password}
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          helperText={(isUserValid.password)? "" : errors.password}
        ></TextField>
        <Button className={styles.button}
          variant="contained"
          size="large"
          onClick={handleUpdate}
        >save changes</Button>
      </Box>
    </Box>
  )
}

export default Profile;