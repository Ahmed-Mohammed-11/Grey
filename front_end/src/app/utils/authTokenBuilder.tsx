function buildAuthToken(userDTO: UserDTO) {
    const username = userDTO.username;
    const password = userDTO.password;
    const base64Credentials = btoa(username + ":" + password);
    return "Basic " + base64Credentials;
}

export default buildAuthToken;