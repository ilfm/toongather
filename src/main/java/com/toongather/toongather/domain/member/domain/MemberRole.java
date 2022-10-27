package com.toongather.toongather.domain.member.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
@Getter
@Setter
public class MemberRole {

    @Id
    @GeneratedValue
    private Long memberRoleNo;
    private Long memberNo;
    private Long roleId;

    public MemberRole() {
    }

    public Long getMemberRoleNo() {
        return memberRoleNo;
    }

    public void setMemberRoleNo(Long memberRoleNo) {
        this.memberRoleNo = memberRoleNo;
    }

    public Long getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(Long memberNo) {
        this.memberNo = memberNo;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
