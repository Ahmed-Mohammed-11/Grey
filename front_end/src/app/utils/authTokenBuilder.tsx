function buildAuthToken(userDTO:UserDTO){
    const username = userDTO.username;
    const password = userDTO.password;
    const base64Credentials = btoa(username + ":" + password);
    const authToken = "Basic " + base64Credentials;
    return authToken;
}

export default buildAuthToken;