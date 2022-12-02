package com.toongather.toongather.domain.member.domain;

import com.toongather.toongather.SeqGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER")
@Entity
@Getter
public class Member implements UserDetails {

    @Id
    @GenericGenerator(name="seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
            parameters ={@org.hibernate.annotations.Parameter(name= SeqGenerator.SEQ_NAME,value="REVIEW_SEQ"),
                    @org.hibernate.annotations.Parameter(name= SeqGenerator.PREFIX,value="MB")} )
    @GeneratedValue(generator = "seqGenerator")
    @Column(name = "MEMBER_NO")
    private Long id;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "NICKNAME")
    private String nickName;

    @Column
    private String imgPath;
    @Column(name = "CRT")
    private String crt;

    @Column(name = "CRT_EXPIRED")
    private LocalDateTime crtExpired;

    @Column(name = "LAST_LOGIN")
    private LocalDateTime lastLogin;

    @Column(name = "JOIN_TYPE")
    private String joinType;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<MebmerRole> memberRoles  = new ArrayList<>();

    @Builder
    public Member(String name, String password) {
        this.name = name;
        this.email = "aaa@test.co.kr";
        this.phone = "010-000-0000";
        this.nickName = "test";
        this.password = password;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> collect = memberRoles.stream().map(entity -> new SimpleGrantedAuthority(entity.getRole().getName()))
                .collect(Collectors.toList());


        return collect;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
