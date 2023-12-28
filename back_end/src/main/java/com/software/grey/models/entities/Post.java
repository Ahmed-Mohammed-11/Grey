package com.software.grey.models.entities;

import com.software.grey.models.enums.Feeling;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Post implements Comparable<Post> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "text")
    private String postText;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "post_feelings", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "feeling")
    private Set<Feeling> postFeelings;

    @Column(name = "post_time")
    private Timestamp postTime;

    @Override
    public int compareTo(Post o) {
        if (this.postTime.equals(o.postTime)) {
            return this.id.compareTo(o.id);
        }
        return o.postTime.compareTo(this.postTime);
    }
}
