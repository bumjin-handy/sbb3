package com.mysite.sbb;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

@ToString(callSuper = true)
@Getter @Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList = new ArrayList<>();

    /* User.java */
    public void addAnswer(Answer answer) {
        answerList.add(answer);
        answer.updateQuestion(this);
    }

    public void removeAnswer(Answer answer) {
        answerList.remove(answer);
        answer.setQuestion(null);
    }
}
