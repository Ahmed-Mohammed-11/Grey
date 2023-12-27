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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Post post = (Post) o;
//        return Objects.equals(id, post.id) &&
//                Objects.equals(postText, post.postText) &&
//                Objects.equals(user, post.user) &&
//                Objects.equals(postFeelings, post.postFeelings) &&
//                Objects.equals(postTime, post.postTime);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, postText, user, postFeelings, postTime);
//    }
}
