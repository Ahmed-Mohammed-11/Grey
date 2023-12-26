package com.software.grey.models.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "saved_post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(SavedPostId.class)
public class SavedPost {
    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Column(name = "saved_time")
    private Timestamp SavedTime;
}
