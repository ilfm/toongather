package com.toongather.toongather.domain.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER_ROLE")
@Entity
@Getter
@SequenceGenerator(
  name = "MEMBER_ROLE_SEQ_GEN",
  sequenceName = "MEMBER_ROLE_SEQ",
  initialValue = 1,
  allocationSize = 1
)
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator ="MEMBER_ROLE_SEQ_GEN" )
    @Column(name = "NO")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_NO",foreignKey = @ForeignKey(name = "fk_memberrole_to_member"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID",foreignKey = @ForeignKey(name = "fk_memberrole_to_role"))
    private Role role;

    @Builder
    public MemberRole(Role role, Member member) {
        this.member = member;
        this.role = role;
    }


}
