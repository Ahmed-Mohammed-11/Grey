'use client';
import createCache from '@emotion/cache';
import {useServerInsertedHTML} from 'next/navigation';
import {CacheProvider} from '@emotion/react';
import {ThemeProvider} from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import {createTheme} from "@mui/material";
import React from "react";


const theme = createTheme({
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
        type: 'light',
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
            main: '#03C988',
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
            hint: '#DDE6ED',
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
        fontSize: 14,
        fontWeightLight: 300,
        fontWeightRegular: 400,
        fontWeightMedium: 500,
        fontWeightBold: 700,
        useNextVariants: true,
        body1: {
            fontWeight: 400,
            fontSize: '1rem',
            lineHeight: 1.5,
            letterSpacing: '0.00938em',
        },
        body2: {
            fontWeight: 400,
            fontSize: '1rem',
            lineHeight: 1.5,
            letterSpacing: '0.00938em',
        },
        button: {
            fontWeight: 500,
            fontSize: '0.875rem',
            lineHeight: 1.75,
            letterSpacing: '0.02857em',
            textTransform: 'uppercase',
        },
        caption: {
            fontWeight: 400,
            fontSize: '0.75rem',
            lineHeight: 1.42,
            letterSpacing: '0.01071em',
        },
        overline: {
            fontWeight: 400,
            fontSize: '0.75rem',
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
            fontSize: '1rem',
            lineHeight: 1.5,
            letterSpacing: '0.00938em',

        },
        subtitle2: {
            fontWeight: 500,
            fontSize: '0.875rem',
            lineHeight: 1.57,
            letterSpacing: '0.00714em',
        },
    },
    direction: 'ltr',
    props: {},
    shape: {
        borderRadius: 10,
    },
    transitions: {
        easing: {
            easeInOut: 'cubic-bezier(0.4, 0, 0.2, 1)',
            easeIn: 'cubic-bezier(0.4, 0, 1, 1)',
            easeOut: 'cubic-bezier(0, 0, 0.2, 1)',
            easeInOutBack: 'cubic-bezier(0.6, -0.28, 0.735, 0.045)',
            easeInBack: 'cubic-bezier(0.6, 0.04, 0.98, 0.335)',
            easeOutBack: 'cubic-bezier(0.175, 0.885, 0.32, 1.275)',
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

const darkTheme = createTheme({
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
        type: 'dark',
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
            main: '#03C988',
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
            hint: '#DDE6ED',
        },

        background: {
            default: '#27374D',
            paper: '#27374D',
        },
    },
    typography: {
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        fontSize: 14,
        fontWeightLight: 300,
        fontWeightRegular: 400,
        fontWeightMedium: 500,
        fontWeightBold: 700,
        useNextVariants: true,
        body1: {
            fontWeight: 400,
            fontSize: '1rem',
            lineHeight: 1.5,
            letterSpacing: '0.00938em',
        },
        body2: {
            fontWeight: 400,
            fontSize: '1rem',
            lineHeight: 1.5,
            letterSpacing: '0.00938em',
        },
        button: {
            fontWeight: 500,
            fontSize: '0.875rem',
            lineHeight: 1.75,
            letterSpacing: '0.02857em',
            textTransform: 'uppercase',
        },
        caption: {
            fontWeight: 400,
            fontSize: '0.75rem',
            lineHeight: 1.42,
            letterSpacing: '0.01071em',
        },
        overline: {
            fontWeight: 400,
            fontSize: '0.75rem',
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
            fontSize: '1rem',
            lineHeight: 1.5,
            letterSpacing: '0.00938em',

        },
        subtitle2: {
            fontWeight: 500,
            fontSize: '0.875rem',
            lineHeight: 1.57,
            letterSpacing: '0.00714em',
        },
    },
    direction: 'ltr',
    props: {},
    shape: {
        borderRadius: 10,
    },
    transitions: {
        easing: {
            easeInOut: 'cubic-bezier(0.4, 0, 0.2, 1)',
            easeIn: 'cubic-bezier(0.4, 0, 1, 1)',
            easeOut: 'cubic-bezier(0, 0, 0.2, 1)',
            easeInOutBack: 'cubic-bezier(0.6, -0.28, 0.735, 0.045)',
            easeInBack: 'cubic-bezier(0.6, 0.04, 0.98, 0.335)',
            easeOutBack: 'cubic-bezier(0.175, 0.885, 0.32, 1.275)',
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
    zIndex: {
        mobileStepper: 1000,
        speedDial: 1050,
        appBar: 1100,
        drawer: 1200,
        modal: 1300,
        snackbar: 1400,
        tooltip: 1500,
    },
});

// This implementation is from emotion-js
// https://github.com/emotion-js/emotion/issues/2928#issuecomment-1319747902
export default function ThemeRegistry(props) {
    const {options, children} = props;

    const [{cache, flush}] = React.useState(() => {
        const cache = createCache(options);
        cache.compat = true;
        const prevInsert = cache.insert;
        let inserted: string[] = [];
        cache.insert = (...args) => {
            const serialized = args[1];
            if (cache.inserted[serialized.name] === undefined) {
                inserted.push(serialized.name);
            }
            return prevInsert(...args);
        };
        const flush = () => {
            const prevInserted = inserted;
            inserted = [];
            return prevInserted;
        };
        return {cache, flush};
    });

    useServerInsertedHTML(() => {
        const names = flush();
        if (names.length === 0) {
            return null;
        }
        let styles = '';
        for (const name of names) {
            styles += cache.inserted[name];
        }
        return (
            <style
                key={cache.key}
                data-emotion={`${cache.key} ${names.join(' ')}`}
                dangerouslySetInnerHTML={{
                    __html: styles,
                }}
            />
        );
    });

    return (
        <CacheProvider value={cache}>
            <ThemeProvider theme={theme}>
                <CssBaseline/>
                {children}
            </ThemeProvider>
        </CacheProvider>
    );
}