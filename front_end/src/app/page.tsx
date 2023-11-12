import Image from 'next/image'
import styles from './page.module.css'
import Button from '@mui/material/Button';

export default function Home() {
  return (
    <div>
        <p className={styles.paragraph}> Hello greys </p>
        {/*testing mui
        installation -> npm install @mui/material @emotion/react @emotion/styled
        documentation -> https://mui.com/material-ui/getting-started/installation/*/  }
        <Button className={styles.main_button} size="large" variant="contained">press me</Button>
    </div>
  )
}
