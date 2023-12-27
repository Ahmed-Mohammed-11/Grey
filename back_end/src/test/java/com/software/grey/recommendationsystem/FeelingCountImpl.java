package com.software.grey.recommendationsystem;

import com.software.grey.models.enums.Feeling;
import com.software.grey.models.projections.FeelingCountProjection;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
class FeelingCountImpl implements FeelingCountProjection {
    private Feeling feeling;
    private int feelingCount;

    @Override
    public Feeling getFeeling() {
        return this.feeling;
    }

    @Override
    public void setFeeling(Feeling feeling) {
        this.feeling = feeling;
    }

    @Override
    public int getFeelingCount() {
        return this.feelingCount;
    }

    @Override
    public void setFeelingCount(int feelingCount) {
        this.feelingCount = feelingCount;
    }
}