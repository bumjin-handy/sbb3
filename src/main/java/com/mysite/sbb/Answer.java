package com.mysite.sbb;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;
//https://www.baeldung.com/jpa-cascade-types
    @ManyToOne
    private Question question;

    public Answer() {

    }

    public Answer(String content, LocalDateTime createDate) {
            this.content  = content;
            this.createDate = createDate;
    }

    public void updateQuestion(Question question) {
        this.question = question;
    }
}
