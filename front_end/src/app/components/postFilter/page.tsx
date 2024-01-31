"use client";
import CustomDatePicker from "./customDatePicker";
import {Box} from "@mui/system";
import React, {useEffect, useState} from 'react';
import PostFilterDTO from '@/app/entities/dtos/PostFilterDTO';
import FeelingsFilter from "../feelingsFilter/page";
import Feeling from "@/app/entities/dtos/Feeling";
import styles from "./page.module.css"

export default function PostFilters(props: any) {
    const {showDatePicker} = props;
    const [filterData, setFilterData] = useState<PostFilterDTO>({} as PostFilterDTO);

    useEffect(() => {
        props.applyFilters(filterData);
    }, [filterData]);

    const handleDateFilter = (date: any) => {
        setFilterData({
            ...(filterData),
            day: date.day,
            month: date.month,
            year: date.year
        })
    };

    const handleFeelingsFilter = (selectedFeelings: Set<Feeling>) => {
        setFilterData({...(filterData), feelings: Array.from(selectedFeelings)})
    };

    return (
        <Box className={styles.filter_container}>
            {props.showDatePicker && <CustomDatePicker onDateChange={handleDateFilter}/>}
            {props.showFeelingSelection && <FeelingsFilter limit={8} onDataChange={handleFeelingsFilter}/>}
            {/* Other content in the filter component */}
        </Box>
    );
}
