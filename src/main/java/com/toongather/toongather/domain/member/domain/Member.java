package com.toongather.toongather.domain.member.domain;

import com.toongather.toongather.global.common.BaseTimeEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER")
@Entity
@Getter
@SequenceGenerator(
    name = "MEMBER_SEQ_GEN",
    sequenceName = "MEMBER_SEQ",
    initialValue = 1,
    allocationSize = 1
)
public class Member extends BaseTimeEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "MEMBER_SEQ_GEN")
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

  @Column(name = "IMG_PATH")
  private String imgPath;

  @Column(name = "TEMP_CODE")
  private String tempCode;

  @Column(name = "TEMP_CODE_EXPIRED")
  private LocalDateTime tempCodeExpired;

  @Column(name = "USE_AT")
  private Boolean useAt;

  @Column(name ="MEMBER_TYPE")
  @Enumerated(EnumType.STRING)
  private MemberType memberType;

  @Column(name = "LAST_LOGIN")
  private LocalDateTime lastLogin;

  @Column(name = "JOIN_TYPE")
  @Enumerated(EnumType.STRING)
  private JoinType joinType;

  @Column(name = "REFRESH_TOKEN")
  private String refreshToken;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemberRole> memberRoles = new ArrayList<>();


  //생성자
  @Builder
  public Member(Long memberId, String name, String password, String email, String phone, String nickName) {
    this.id = memberId;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.nickName = nickName;
    this.password = password;
    this.joinType = JoinType.NORMAL;
    this.memberType = MemberType.TEMP;
  }

  @Builder(builderMethodName = "OAuthBuilder", builderClassName = "OAuthBuilder")
  public Member(String name, String email, String nickName, JoinType joinType) {
    this.name = name;
    this.email = email;
    this.nickName = nickName;
    this.joinType = joinType;
    this.memberType = MemberType.ACTIVE;
  }

  //비즈니스로직

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public void addMemberRoles(Role role) {
    MemberRole memberRole = MemberRole.builder().role(role).member(this).build();
    memberRoles.add(memberRole);
  }

  public void regLastLoginHistory() {
    this.lastLogin = LocalDateTime.now();
  }

  public void updateTempCode(String tempCode, LocalDateTime tempCodeExpired) {
    this.tempCode = tempCode;
    this.tempCodeExpired = tempCodeExpired;
  }

  public void updateActiveMember() {
    this.memberType = MemberType.ACTIVE;
    this.updateTempCode(null, null);
  }

  public void resetPassword(String password) {
    this.password = password;
  }


  //회원 extend

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    return memberRoles.stream()
        .map(entity -> new SimpleGrantedAuthority(entity.getRole().getName().name()))
        .collect(Collectors.toList());
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
