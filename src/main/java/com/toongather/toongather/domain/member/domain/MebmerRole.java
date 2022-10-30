package com.toongather.toongather.domain.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER_ROLE")
@Entity
@Getter
public class MebmerRole {

    @Id @GeneratedValue
    @Column(name = "NO")
    private int id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_NO")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @Builder MebmerRole(Member member, Role role) {
        this.member = member;
        this.role = role;
    }

}
