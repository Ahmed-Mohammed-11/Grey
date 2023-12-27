"use client";
import CustomDatePicker from "./customDatePicker";
import {Box} from "@mui/system";
import React, {useEffect, useState} from 'react';
import PostFilterDTO from '@/app/entities/dtos/PostFilterDTO';
import FeelingsFilter from "../feelingsFilter/page";
import Feeling from "@/app/entities/dtos/Feeling";

export default function PostFilters(props: any) {
    const {showDatePicker} = props;
    const [filterData, setFilterData] = useState<PostFilterDTO>({} as PostFilterDTO);

    useEffect(() => {
        console.log("######", filterData)
        props.applyFilters(filterData);
    }, [filterData]);

    const handleDateFilter = (date: any) => {
        console.log("date filter change")
        setFilterData({
            ...(filterData),
            day: date.day,
            month: date.month,
            year: date.year
        })
    };

    const handleFeelingsFilter = (selectedFeelings: Set<Feeling>) => {
        console.log("feeling filter change")
        setFilterData({...(filterData), feelings: Array.from(selectedFeelings)})
    };

    return (
        <Box>
            {props.showDatePicker && <CustomDatePicker onDateChange={handleDateFilter}/>}
            {props.showFeelingSelection && <FeelingsFilter limit={8} onDataChange={handleFeelingsFilter}/>}
            {/* Other content in the filter component */}
        </Box>
    );
}
