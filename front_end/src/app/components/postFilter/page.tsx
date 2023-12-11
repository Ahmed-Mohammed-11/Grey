"use client";
import CustomDatePicker from "./customDatePicker";
import styles from "./page.module.css";
import { Box } from "@mui/system";
import React, { useState, useEffect } from 'react';
import PostFilterDTO from '../../models/dtos/PostFilterDTO';
import FeelingsFilter from "../feelingsFilter/page";

export default function PostFilters(props: any) {
  const { showDatePicker } = props;
  const [filterData, setFilterData] = useState<PostFilterDTO>({} as PostFilterDTO);

  const handleDataChange = (newData:PostFilterDTO) => {
    props.applyFilters(newData)
  };

  return (
    <Box>
      {showDatePicker && <CustomDatePicker  data={filterData} onDateChange={handleDataChange}/>}
      <FeelingsFilter limit={8} data={filterData} onDateChange={handleDataChange}/>
      {/* Other content in the filter component */}
    </Box>
  );
}
