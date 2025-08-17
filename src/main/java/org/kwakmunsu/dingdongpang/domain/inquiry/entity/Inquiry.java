package org.kwakmunsu.dingdongpang.domain.inquiry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @JoinColumn(name = "author_id")
    private Member author;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryStatus status;

    public static Inquiry create(Long shopId, Member author, String title, String question) {
        return Inquiry.builder()
                .shopId(shopId)
                .author(author)
                .title(title)
                .question(question)
                .status(InquiryStatus.PADDING)
                .build();
    }

    public void registerAnswer(String answer) {
        this.answer = answer;
    }

    public void updateAnswer(String answer) {
        this.answer = answer;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateQuestion(String question) {
        this.question = question;
    }

    public void updateStatusToCompleted() {
        this.status = InquiryStatus.COMPLETED;
    }

}