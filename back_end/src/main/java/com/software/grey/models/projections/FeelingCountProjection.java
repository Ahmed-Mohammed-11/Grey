package com.software.grey.models.projections;

import com.software.grey.models.enums.Feeling;

public interface FeelingCountProjection {

    Feeling getFeeling();

    void setFeeling(Feeling feeling);

    int getFeelingCount();

    void setFeelingCount(int feelingCount);
}
