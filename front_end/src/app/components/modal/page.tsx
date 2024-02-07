import * as React from 'react';
import clsx from 'clsx';
import {styled, css, Box} from '@mui/system';
import { Modal as BaseModal } from '@mui/base/Modal';
import { Button } from '@mui/base/Button';
import styles from "./page.module.css"
import {IconButton} from "@mui/material";

export default function NestedModal(props : any) {
    const [open, setOpen] = React.useState(false);
    const handleOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };

    const [triggerText, setTriggerText] = React.useState("")
    const showTriggerText = () => {
        setTriggerText(props.title)
    }
    const hideTriggerText = () => {
        setTriggerText("")
    }

    return (
        <div>
            <TriggerButton className={props.type} onClick={handleOpen} onMouseEnter={showTriggerText} onMouseLeave={hideTriggerText}>
                <Icon onClick={handleOpen}>{props.icon}{}</Icon>
                {triggerText}
            </TriggerButton>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="parent-modal-title"
                aria-describedby="parent-modal-description"
                slots={{ backdrop: StyledBackdrop }}
            >
                <ModalContent sx={style}>
                    <h2 id="parent-modal-title" className="modal-title">
                        {props.title}
                    </h2>
                    <p id="parent-modal-description" className="modal-description">
                        {props.text}
                    </p>
                    <Box className={styles.post_footer}>
                        <Button className={styles.button} onClick={handleClose}>
                            cancel
                        </Button>
                        <Button className={`${styles.button} ${props.type}`}
                                onClick={() => props.handle(props.postId)}>
                            confirm {props.type}
                        </Button>
                    </Box>
                    {/*<ChildModal />*/}
                </ModalContent>
            </Modal>
        </div>
    );
}


function ChildModal() {
    const [open, setOpen] = React.useState(false);
    const handleOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };

    return (
        <React.Fragment>
            <ModalButton onClick={handleOpen}>Open Child Modal</ModalButton>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="child-modal-title"
                aria-describedby="child-modal-description"
                slots={{ backdrop: StyledBackdrop }}
            >
                <ModalContent sx={[style, { width: '240px' }]}>
                    <h2 id="child-modal-title" className="modal-title">
                        Text in a child modal
                    </h2>
                    <p id="child-modal-description" className="modal-description">
                        Lorem ipsum, dolor sit amet consectetur adipisicing elit.
                    </p>
                    <ModalButton onClick={handleClose}>Close Child Modal</ModalButton>
                </ModalContent>
            </Modal>
        </React.Fragment>
    );
}

const Backdrop = React.forwardRef<
    HTMLDivElement,
    { open?: boolean; className: string }
>((props, ref) => {
    const { open, className, ...other } = props;
    return (
        <div
            className={clsx({ 'base-Backdrop-open': open }, className)}
            ref={ref}
            {...other}
        />
    );
});

const blue = {
    200: '#99CCFF',
    300: '#66B2FF',
    400: '#3399FF',
    500: '#007FFF',
    600: '#0072E5',
    700: '#0066CC',
};

const grey = {
    50: '#F3F6F9',
    100: '#E5EAF2',
    200: '#DAE2ED',
    300: '#C7D0DD',
    400: '#B0B8C4',
    500: '#9DA8B7',
    600: '#6B7A90',
    700: '#434D5B',
    800: '#303740',
    900: '#1C2025',
};

const Modal = styled(BaseModal)`
  position: fixed;
  z-index: 1300;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const StyledBackdrop = styled(Backdrop)`
  z-index: -1;
  position: fixed;
  inset: 0;
  background-color: rgb(0 0 0 / 0.5);
  -webkit-tap-highlight-color: transparent;
`;

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
};

const ModalContent = styled('div')(
    ({ theme }) => css`
      font-family: 'IBM Plex Sans', sans-serif;
      font-weight: 500;
      text-align: start;
      position: relative;
      display: flex;
      flex-direction: column;
      gap: 8px;
      overflow: hidden;
      background-color: ${theme.palette.mode === 'dark' ? grey[900] : '#fff'};
      border-radius: 8px;
      border: 1px solid ${theme.palette.mode === 'dark' ? grey[700] : grey[200]};
      box-shadow: 0 4px 12px ${theme.palette.mode === 'dark' ? 'rgb(0 0 0 / 0.5)' : 'rgb(0 0 0 / 0.2)'};
      padding: 24px;
      color: ${theme.palette.mode === 'dark' ? grey[50] : grey[900]};

      & .modal-title {
        margin: 0;
        line-height: 1.5rem;
        margin-bottom: 8px;
      }

      & .modal-description {
        line-height: 1.5rem;
        font-weight: 100;
        padding: 0 0 0 10px;
        color: ${theme.palette.mode === 'dark' ? grey[400] : grey[800]};
        margin-bottom: 4px;
      }

      & .safe {
        background: #51ef5f;
        color: #FFF;
      }

      & .violating {
        background: #e02b2b;
        color: #cfe9ff;
      }
    `,
);

const TriggerButton = styled(Button)(
    ({ theme }) => css`
  font-family: 'IBM Plex Sans', sans-serif;
  font-weight: 600;
  font-size: 0.875rem;
  padding: 8px;
  border-radius: 30px;
  color: white;
  transition: all 150ms ease;
  cursor: pointer;
  background: ${theme.palette.mode === 'dark' ? grey[900] : '#fff'};
  border: 1px solid ${theme.palette.mode === 'dark' ? grey[700] : grey[200]};
  color: ${theme.palette.mode === 'dark' ? grey[200] : grey[900]};
  box-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05);
  
  &.safe {
    background: #51ef5f;
    color: #FFF;
  }
  &.violating {
    background: #e02b2b;
    color: #cfe9ff;
  }
  // &:hover {
  //   background: ${theme.palette.mode === 'dark' ? grey[800] : grey[50]};
  //   border-color: ${theme.palette.mode === 'dark' ? grey[600] : grey[300]};
  // }
  //
  // &:active {
  //   background: ${theme.palette.mode === 'dark' ? grey[700] : grey[100]};
  // }
  //
  // &:focus-visible {
  //   box-shadow: 0 0 0 4px ${theme.palette.mode === 'dark' ? blue[300] : blue[200]};
  //   outline: none;
  // }
`,
);

const ModalButton = styled(Button)(
    ({ theme }) => css`
  font-family: 'IBM Plex Sans', sans-serif;
  font-weight: 600;
  font-size: 0.875rem;
  line-height: 1.5;
  background-color: ${blue[500]};
  padding: 8px 16px;
  border-radius: 8px;
  color: white;
  transition: all 150ms ease;
  cursor: pointer;
  border: 1px solid ${blue[500]};
  box-shadow: 0 2px 1px ${
        theme.palette.mode === 'dark' ? 'rgba(0, 0, 0, 0.5)' : 'rgba(45, 45, 60, 0.2)'
    }, inset 0 1.5px 1px ${blue[400]}, inset 0 -2px 1px ${blue[600]};
    
  &:hover {
    background-color: ${blue[600]};
  }

  &:active {
    background-color: ${blue[700]};
    box-shadow: none;
  }

  &:focus-visible {
    box-shadow: 0 0 0 4px ${theme.palette.mode === 'dark' ? blue[300] : blue[200]};
    outline: none;
  }

  &:disabled {
    opacity: 0.4;
    cursor: not-allowed;
    box-shadow: none;
    &:hover {
      background-color: ${blue[500]};
    }
  }
`,
);

const Icon = styled(IconButton)(
    ({ theme }) => css`
  font-family: 'IBM Plex Sans', sans-serif;
  font-weight: 600;
  font-size: 1rem;
  border-radius: 30px;
  color: white;
  transition: all 150ms ease;
  cursor: pointer;
    
  &:hover {
    background-color: transparent;
  }

  &:active {
    
  }

  &:focus-visible {
    
  }
`,
);