'use client';
import React, {useEffect, useState} from "react";
import {Box, Card, CardContent, CircularProgress, Divider, Stack, Tooltip, Typography} from "@mui/material";
import styles from "./page.module.css"
import getRequestNoParams from "@/app/services/getRequestNoParams";
import toJSON from "@/app/utils/readableStreamResponseBodytoJSON";
import {PieChart} from '@mui/x-charts/PieChart';

function Dashboard() {
    const [totalNumberOfPosts, setTotalNumberOfPosts] = useState<number>(0);
    const [totalNumberOfUserPosts, setTotalNumberOfUserPosts] = useState<number>(0);
    const [dominantPersonalFeeling, setDominantPersonalFeeling] = useState<string>("");
    const [dominantPersonalFeelingCount, setDominantPersonalFeelingCount] = useState<number>(0);
    const [jsonResponseFormat, setJsonResponseFormat] = useState<object>({
        totalNumberOfPosts: 0,
        totalNumberOfUserPosts: 0,
        userFeelings: [],
        globalFeelings: [],
    });
    const [dominantGlobalFeeling, setDominantGlobalFeeling] = useState<string>("");
    const [dominantGlobalFeelingCount, setDominantGlobalFeelingCount] = useState<number>(0);
    const [userFeelingsChart, setUserFeelingsChart] = useState<Array<object>>([]);
    const [globalFeelingsChart, setGlobalFeelingsChart] = useState<Array<object>>([]);
    const feelingsPaletteMap = new Map([
        ["HAPPY", ["#26c08a", "#e1f1e9"]],
        ["SAD", ["#689cbd", "#dbe6f1"]],
        ["ANGER", ["#f86c6b", "#fde9e9"]],
        ["FEAR", ["#a60dfd84", "#ffecff"]],
        ["LOVE", ["#f0769f", "#ffe0eb"]],
        ["DISGUST", ["#d29f6fff", "#fdede9"]],
        ["ANXIOUS", ["#fdc358", "#fff7e9"]],
        ["INSPIRE", ["#5252528c", "#ffffff"]],
    ]);

    useEffect(() => {
        async function fetchRequest() {
            let response = await getRequestNoParams.sendGetRequest("/feeling-dashboard");
            // toJSON util to convert ReadableStream to JSON
            let jsonResponse = await toJSON(response.body!);
            setTotalNumberOfPosts(jsonResponse.totalNumberOfPosts);
            setTotalNumberOfUserPosts(jsonResponse.totalNumberOfUserPosts);
            setDominantPersonalFeelingCount(jsonResponse.userFeelings[0]?.feelingCount);
            setDominantPersonalFeeling(jsonResponse.userFeelings[0]?.feeling);
            setDominantGlobalFeelingCount(jsonResponse.globalFeelings[0]?.feelingCount);
            setDominantGlobalFeeling(jsonResponse.globalFeelings[0]?.feeling);

            let tmpUserData = [];
            let tmpGlobalData = [];

            for (let i = 0; i < jsonResponse.userFeelings.length; i++) {
                let feeling = jsonResponse.userFeelings[i]?.feeling;
                // @ts-ignore
                tmpUserData.push({color: feelingsPaletteMap.get(feeling)[0], id: i, label: jsonResponse.userFeelings[i]?.feeling, value: jsonResponse.userFeelings[i]?.feelingCount})
            }

            for (let i = 0; i < jsonResponse.globalFeelings.length; i++) {
                let feeling = jsonResponse.globalFeelings[i]?.feeling;
                // @ts-ignore
                tmpGlobalData.push({color: feelingsPaletteMap.get(feeling)[0], id: i, label: jsonResponse.globalFeelings[i]?.feeling, value: jsonResponse.globalFeelings[i]?.feelingCount})
            }

            console.log(tmpGlobalData)
            setUserFeelingsChart(tmpUserData);
            setGlobalFeelingsChart(tmpGlobalData);
        }

        fetchRequest().then(r => console.log("fetched data"));

    }, []);

    let dominantUserFeelingColorArray = feelingsPaletteMap.get(dominantPersonalFeeling);
    let dominantUserFeelingBackgroundColor = "";
    let dominantUserFeelingColor = "";
    if (dominantUserFeelingColorArray) {
        dominantUserFeelingColor = dominantUserFeelingColorArray[0];
        dominantUserFeelingBackgroundColor = dominantUserFeelingColorArray[1];
    }
    let dominantGlobalFeelingColorArray = feelingsPaletteMap.get(dominantGlobalFeeling);
    let dominantGlobalFeelingColor = "";
    let dominantGlobalFeelingBackgroundColor = "";
    if (dominantGlobalFeelingColorArray) {
        dominantGlobalFeelingColor = dominantGlobalFeelingColorArray[0];
        dominantGlobalFeelingBackgroundColor = dominantGlobalFeelingColorArray[1];
    }

    return (
        <Box className={styles.container}>
            <Box className={`${styles.personal_posts} ${styles.box}`}>
                <Card
                    className={styles.card}
                    style={{
                        backgroundColor: dominantUserFeelingBackgroundColor,
                        color: dominantUserFeelingColor,
                        borderLeftColor: dominantUserFeelingColor,
                        borderLeftWidth: 10,
                        borderLeftStyle: "solid",
                    }}
                >
                    <CardContent className={styles.card_content}>
                        <Typography variant={"h5"} component={"h5"} style={{fontWeight: 'bold'}}>
                            Your Dominant Feeling Today
                        </Typography>
                        <Divider/>
                        {
                            totalNumberOfUserPosts === 0 ?
                                <Typography
                                    variant={"h5"}
                                    component={"h5"}
                                    className={styles.no_posts}
                                >
                                    <br/> You haven't written any posts yet today!
                                </Typography>
                                :
                                <Stack direction={"row"} display={"flex"} justifyContent={"space-between"} alignItems={"center"} sx={{p: 2}} spacing={5}>
                                    <Tooltip
                                        title={`You wrote ${totalNumberOfUserPosts} post/s today and mentioned ${dominantPersonalFeeling} ${dominantPersonalFeelingCount} time/s`}>
                                        <Box sx={{position: 'relative', display: 'flex'}}>
                                            <CircularProgress
                                                variant={"determinate"}
                                                thickness={5}
                                                size={75}
                                                style={{color: dominantUserFeelingColor}}
                                                value={(dominantPersonalFeelingCount / totalNumberOfUserPosts) * 100}
                                            />
                                            <Box
                                                sx={{
                                                    top: 0,
                                                    left: 0,
                                                    bottom: 0,
                                                    right: 0,
                                                    position: 'absolute',
                                                    display: 'flex',
                                                    alignItems: 'center',
                                                    justifyContent: 'center',
                                                }}
                                            >
                                                <Typography
                                                    color={dominantUserFeelingColor}
                                                    style={{fontWeight: 'bold'}}
                                                > {Math.round(dominantPersonalFeelingCount / totalNumberOfUserPosts * 100)}% </Typography>
                                            </Box>
                                        </Box>
                                    </Tooltip>

                                    <Typography variant={"h5"} component={"h5"}>
                                        We noticed that you feel <span
                                        style={{fontWeight: 'bold'}}>{dominantPersonalFeeling}</span> today as you
                                        wrote <span
                                        style={{fontWeight: 'bold'}}>{dominantPersonalFeelingCount}</span> post/s about
                                        it.
                                    </Typography>
                                </Stack>
                        }
                    </CardContent>
                </Card>
            </Box>
            <Box className={`${styles.global_posts} ${styles.box}`}>
                <Card
                    className={styles.card}
                    style={{
                        backgroundColor: dominantGlobalFeelingBackgroundColor,
                        color: dominantGlobalFeelingColor,
                        borderLeftColor: dominantGlobalFeelingColor,
                        borderLeftWidth: 10,
                        borderLeftStyle: "solid",
                    }}
                >
                    <CardContent className={styles.card_content}>
                        <Typography variant={"h5"} component={"h5"} style={{fontWeight: 'bold'}}>
                            Dominant Global Feeling Today
                        </Typography>
                        <Divider/>
                        {
                            totalNumberOfPosts === 0 ?
                                <Typography
                                    variant={"h5"}
                                    component={"h5"}
                                    className={styles.no_posts}
                                >
                                    <br/> Sorry, No one have written posts yet today!
                                </Typography>
                                :
                                <Stack direction={"row"} display={"flex"} justifyContent={"space-between"} alignItems={"center"} sx={{p: 2}} spacing={5}>
                                    <Tooltip
                                        title={`${totalNumberOfPosts} post/s have been written today and ${dominantGlobalFeeling} is mentioned in ${dominantGlobalFeelingCount} of them`}>
                                        <Box sx={{position: 'relative', display: 'flex'}}>
                                            <CircularProgress
                                                variant="determinate"
                                                thickness={5}
                                                size={75}
                                                style={{color: dominantGlobalFeelingColor}}
                                                value={(dominantGlobalFeelingCount / totalNumberOfPosts) * 100}
                                            />
                                            <Box
                                                sx={{
                                                    top: 0,
                                                    left: 0,
                                                    bottom: 0,
                                                    right: 0,
                                                    position: 'absolute',
                                                    display: 'flex',
                                                    alignItems: 'center',
                                                    justifyContent: 'center',
                                                }}
                                            >
                                                <Typography
                                                    color={dominantGlobalFeelingColor}
                                                    style={{fontWeight: 'bold'}}
                                                > {Math.round(dominantGlobalFeelingCount / totalNumberOfPosts * 100)}% </Typography>
                                            </Box>
                                        </Box>
                                    </Tooltip>

                                    <Typography variant={"h5"} component={"h5"}>
                                        <span style={{fontWeight: 'bold'}}>{dominantGlobalFeeling}</span> is the
                                        dominant feeling today as
                                        <span
                                            style={{fontWeight: 'bold'}}> {Math.round(dominantGlobalFeelingCount / totalNumberOfPosts * 100)}% </span>of
                                        posts match this feeling.
                                    </Typography>
                                </Stack>
                        }
                    </CardContent>
                </Card>
            </Box>
            <Stack width={"100%"} direction={"row"} justifyContent={"space-between"} alignItems={"center"} sx={{p: 1}}
                   className={`${styles.charts_container} ${styles.box}`}>
                {
                    (totalNumberOfPosts !== 0)
                    &&
                    <Stack width={"50%"} direction={"column"} justifyContent={"space-between"} alignItems={"center"}>
                        <PieChart
                            series={[
                                {
                                    // @ts-ignore
                                    data: userFeelingsChart,
                                    highlightScope: {faded: 'global', highlighted: 'item'},
                                    faded: {innerRadius: 50, additionalRadius: -30, color: 'gray'},
                                    innerRadius: 50,
                                    outerRadius: 150,
                                    cornerRadius: 10,
                                    paddingAngle: 2,
                                },
                            ]}
                            slotProps={{ legend: { labelStyle: { fontSize: 15 }}}}
                            width={500}
                            height={400}
                        />
                        <Tooltip title={"Occurrences of each feeling in your posts"}>
                            <Typography>
                                Your Feelings Chart
                            </Typography>
                        </Tooltip>

                    </Stack>}

                {
                    (totalNumberOfPosts !== 0)
                    &&
                    <Stack width={"50%"} direction={"column"} justifyContent={"space-between"} alignItems={"center"}>
                        <PieChart
                            series={[
                                {
                                    // @ts-ignore
                                    data: globalFeelingsChart,
                                    highlightScope: {faded: 'global', highlighted: 'item'},
                                    faded: {innerRadius: 50, additionalRadius: -30, color: 'gray'},
                                    innerRadius: 50,
                                    outerRadius: 150,
                                    cornerRadius: 10,
                                    paddingAngle: 2,
                                },
                            ]}
                            slotProps={{ legend: { labelStyle: { fontSize: 15 }}}}
                            width={500}
                            height={400}
                        />
                        <Tooltip title={"Occurrences of each feeling in all posts"}>
                            <Typography>
                                Global Feelings Chart
                            </Typography>
                        </Tooltip>
                    </Stack>
                }
            </Stack>
        </Box>
    )
}

export default Dashboard;
