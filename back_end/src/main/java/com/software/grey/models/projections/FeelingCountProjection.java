package com.software.grey.models.projections;

import com.software.grey.models.enums.Feeling;

public interface FeelingCountProjection {
    Feeling getFeeling();
    int getFeelingCount();

    void setFeeling(Feeling feeling);
    void setFeelingCount(int feelingCount);
}
