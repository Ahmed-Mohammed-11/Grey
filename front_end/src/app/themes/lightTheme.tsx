import {createTheme} from "@mui/material";

const lightTheme = createTheme({
    breakpoints: {
        values: {
            xs: 0,
            sm: 600,
            md: 960,
            lg: 1280,
            xl: 1920,
        },
    },
    palette: {
        primary: {
            light: '#9DB2BF',
            main: '#526D82',
            dark: '#526D82',
            contrastText: '#fff',
        },
        secondary: {
            light: '#DDE6ED',
            main: '#9DB2BF',
            dark: '#526D82',
            contrastText: '#fff',
        },

        success: {
            light: '#E8F5E9',
            main: '#C8E6C9',
            dark: '#388E3C',
            contrastText: '#fff',
        },


        error: {
            light: '#FFCDD2',
            main: '#FC2947',
            dark: '#D30027',
            contrastText: '#fff',
        },

        warning: {
            light: '#ffcc80',
            main: '#ff9800',
            dark: '#c67605',
            contrastText: '#000',
        },

        text: {
            primary: '#526D82',
            secondary: '#526D82',
            disabled: '#9DB2BF',
        },


        background: {
            default: '#DDE6ED',
            paper: '#DDE6ED',
        },


        info: {
            light: '#E3F2FD',
            main: '#526D82',
            dark: '#526D82',
            contrastText: '#fff',
        },

        action: {
            active: '#526D82',
            hover: '#526D82',
            hoverOpacity: 0.8,
            selected: '#526D82',
            selectedOpacity: 0.8,
            disabled: '#9DB2BF',
            disabledBackground: '#DDE6ED',
            disabledOpacity: 0.8,
            focus: '#526D82',
            focusOpacity: 0.12,
            activatedOpacity: 0.12,

        }
    },
    typography: {
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: 18,
        fontWeightLight: 300,
        fontWeightRegular: 400,
        fontWeightMedium: 500,
        fontWeightBold: 700,
        body1: {
            fontWeight: 400,
            fontSize: '1.2rem',
            lineHeight: 1.5,
            letterSpacing: '0.00938em',
        },
        body2: {
            fontWeight: 400,
            fontSize: '1.2rem',
            lineHeight: 1.5,
            letterSpacing: '0.00938em',
        },
        button: {
            fontWeight: 500,
            fontSize: '1.2rem',
            lineHeight: 1.75,
            letterSpacing: '0.02857em',
            textTransform: 'uppercase',
        },
        caption: {
            fontWeight: 400,
            fontSize: '1rem',
            lineHeight: 1.42,
            letterSpacing: '0.01071em',
        },
        overline: {
            fontWeight: 400,
            fontSize: '1rem',
            lineHeight: 1.75,
            letterSpacing: '0.02857em',
            textTransform: 'uppercase',
        },
        h1: {
            fontWeight: 300,
            fontSize: '6rem',
            lineHeight: 1.167,
            letterSpacing: '-0.01562em',
        },
        h2: {
            fontWeight: 300,
            fontSize: '3.75rem',
            lineHeight: 1.2,
            letterSpacing: '-0.00833em',
        },
        h3: {
            fontWeight: 400,
            fontSize: '3rem',
            lineHeight: 1.167,
            letterSpacing: '0em',
        },
        h4: {
            fontWeight: 400,
            fontSize: '2.125rem',
            lineHeight: 1.235,
            letterSpacing: '0.00735em',
        },
        h5: {
            fontWeight: 400,
            fontSize: '1.5rem',
            lineHeight: 1.334,
            letterSpacing: '0em',
        },
        h6: {
            fontWeight: 500,
            fontSize: '1.25rem',
            lineHeight: 1.6,
            letterSpacing: '0.0075em',
        },
        subtitle1: {
            fontWeight: 400,
            fontSize: '1.2rem',
            lineHeight: 1.5,
            letterSpacing: '0.00938em',

        },
        subtitle2: {
            fontWeight: 500,
            fontSize: '1rem',
            lineHeight: 1.57,
            letterSpacing: '0.00714em',
        },
    },
    direction: 'ltr',
    shape: {
        borderRadius: 10,
    },
    transitions: {
        easing: {
            easeIn: 'cubic-bezier(0.4, 0, 1, 1)',
            easeInOut: 'cubic-bezier(0.4, 0, 0.2, 1)',
            easeOut: 'cubic-bezier(0, 0, 0.2, 1)',

        },
        duration: {
            shortest: 150,
            shorter: 200,
            short: 250,
            standard: 300,
            complex: 375,
            enteringScreen: 225,
            leavingScreen: 195,
        },
    },
    // zIndex: {
    //     mobileStepper: 1000,
    //     speedDial: 1050,
    //     appBar: 1100,
    //     drawer: 1200,
    //     modal: 1300,
    //     snackbar: 1400,
    //     tooltip: 1500,
    // },
});

export default lightTheme;