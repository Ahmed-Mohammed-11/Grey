package com.software.grey.models.projections;

import com.software.grey.models.enums.Feeling;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

public interface PostFilteringProjection {
    public String getId();
    public String getText();
    public String getUserId();
    public Timestamp getPostTime();
    public Feeling getFeeling();
}
