"use client";
import CustomDatePicker from "./customDatePicker";
import styles from "./page.module.css";
import { Box } from "@mui/system";
import React, { useState, useEffect } from 'react';
import PostFilterDTO from '../../models/dtos/PostFilterDTO';
import FeelingsFilter from "../feelingsFilter/page";
import Feeling from "@/app/models/dtos/Feeling";

export default function PostFilters(props: any) {
  const { showDatePicker } = props;
  const [filterData, setFilterData] = useState<PostFilterDTO>({} as PostFilterDTO);

  const handleDateFilter = (date:any) => {
    setFilterData({...(filterData), 
      day:date.day,
      month:date.month,
      year:date.year})
    props.applyFilters(filterData)
  };

  const handleFeelingsFilter = (selectedFeelings:Set<Feeling>) => {
    console.log("this is the selected feelings", selectedFeelings)
    setFilterData({...(filterData), feelings:selectedFeelings})
    props.applyFilters(filterData)
  };

  return (
    <Box>
      {showDatePicker && <CustomDatePicker onDateChange={handleDateFilter}/>}
      <FeelingsFilter limit={8} onDataChange={handleFeelingsFilter}/>
      {/* Other content in the filter component */}
    </Box>
  );
}
