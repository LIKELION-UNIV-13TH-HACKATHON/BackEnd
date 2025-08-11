package org.kwakmunsu.dingdongpang.domain.inquiry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.BaseEntity;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Inquiry extends BaseEntity {

    @Column(nullable = false)
    private Long shopId;

    @Column(nullable = false)
    private Long authorId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    public static Inquiry create(Long shopId, Long authorId, String question) {
        return Inquiry.builder()
                .shopId(shopId)
                .authorId(authorId)
                .question(question)
                .build();
    }

    public void registerAnswer(String answer) {
        this.answer = answer;
    }

    public void updateAnswer(String answer) {
        this.answer = answer;
    }

}