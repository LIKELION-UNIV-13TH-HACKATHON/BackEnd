package org.kwakmunsu.dingdongpang.domain.inquiry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.BaseEntity;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Inquiry extends BaseEntity {

    @Column(nullable = false)
    private Long shopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private Member author;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    public static Inquiry create(Long shopId, Member author, String question) {
        return Inquiry.builder()
                .shopId(shopId)
                .author(author)
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