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

  useEffect(() => {
    console.log("######", filterData)
    props.applyFilters(filterData);
  }, [filterData]);

  const handleDateFilter = (date:any) => {
    console.log("date filter change")
    setFilterData({...(filterData), 
      day:date.day,
      month:date.month,
      year:date.year})
  };

  const handleFeelingsFilter = (selectedFeelings:Set<Feeling>) => {
    console.log("feelign filter change")
    setFilterData({...(filterData), feelings:Array.from(selectedFeelings)})
  };

  return (
    <Box>
      {showDatePicker && <CustomDatePicker onDateChange={handleDateFilter}/>}
      <FeelingsFilter limit={8} onDataChange={handleFeelingsFilter}/>
      {/* Other content in the filter component */}
    </Box>
  );
}
