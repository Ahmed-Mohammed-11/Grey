package com.software.grey.models.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportedPostId implements Serializable {
    @ManyToOne(cascade = CascadeType.MERGE)
    private Post post;
    @ManyToOne(cascade = CascadeType.MERGE)
    private User reporter;
}