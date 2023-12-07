import { DemoItem } from '@mui/x-date-pickers/internals/demo';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import Box from '@mui/material/Box';
import React, { useState, useEffect } from 'react';
import styles from './page.module.css'

interface CustomDatePickerProps {
  onDateChange: (newDate: Date) => void;
}

export default function CustomDatePicker(props: CustomDatePickerProps) {
  const [cleared, setCleared] = React.useState<boolean>(false);

  const handleSetDate = (newDate: any) => {
    let filterDTO = { ...props.data };
    filterDTO.day = newDate?.date();
    filterDTO.month = newDate?.month() + 1;
    filterDTO.year = newDate?.year();
    props.onDateChange(filterDTO);
  };

  const handleClearDate = () => {
    let filterDTO = { ...props.data };
    filterDTO.day = null;
    filterDTO.month = null;
    filterDTO.year = null;
    props.onDateChange(filterDTO);
    setCleared(true);
  };

  React.useEffect(() => {
    if (cleared) {
      const timeout = setTimeout(() => {
        setCleared(false);
      }, 1500);

      return () => clearTimeout(timeout);
    }
    return () => {};
  }, [cleared]);

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Box className={styles.customDatePickerContainer}>
        <DemoItem label="DatePicker">
          <DatePicker
            className={styles.customDatePicker}
            onChange={handleSetDate}
            slotProps={{
              field: { clearable: true, onClear: handleClearDate },
            }}
          />
        </DemoItem>
      </Box>
    </LocalizationProvider>
  );
}

