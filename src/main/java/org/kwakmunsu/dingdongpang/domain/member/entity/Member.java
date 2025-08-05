package org.kwakmunsu.dingdongpang.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.BaseEntity;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String socialId;

    private String refreshToken;

    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createGuest(String email, String nickname, String socialId) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .socialId(socialId)
                .role(Role.ROLE_GUEST)
                .build();
    }

    public static Member createMember(String email, String nickname, String socialId) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .socialId(socialId)
                .role(Role.ROLE_MEMBER)
                .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void upgradeRoleToMember() {
        this.role = Role.ROLE_MEMBER;
    }

    public boolean isEqualNickname(String nickname) {
        return this.nickname.equals(nickname);
    }

}