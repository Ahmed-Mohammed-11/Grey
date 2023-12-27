import type {Metadata} from 'next'
import {Inter} from 'next/font/google'
import './globals.css'
import Providers from "@/app/googleAuthentication/Providers";
import ThemeRegistry from "@/app/themes/themeRegistry";
import React from "react";

const inter = Inter({subsets: ['latin']})

export const metadata: Metadata = {
    title: 'Grey',
    description: 'Grey Social Network',
}


export default function RootLayout({children,}:
                                       {
                                           children: React.ReactNode
                                       }) {
    return (
        <html lang="en">
        <body className={inter.className}>
        <ThemeRegistry options={{key: 'mui'}}>
            <Providers>
                {children}
            </Providers>
        </ThemeRegistry>
        </body>
        </html>
    )
}
