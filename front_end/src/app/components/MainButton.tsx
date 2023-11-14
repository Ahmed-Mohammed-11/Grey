import {Button} from "@mui/material";
import styles from './MainButton.module.css'

function MainButton (props) {
    return (
        <Button className={styles.mainButton} variant="contained"> {props.text} </Button>
    );
}

export default MainButton;