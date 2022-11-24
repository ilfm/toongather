package com.toongather.toongather.domain.member.domain;

import com.toongather.toongather.SeqGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER_ROLE")
@Entity
@Getter
public class MebmerRole {

    @Id
    @GenericGenerator(name="seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
            parameters ={@org.hibernate.annotations.Parameter(name= SeqGenerator.SEQ_NAME,value="MEMBER_ROLE_SEQ"),
                    @org.hibernate.annotations.Parameter(name= SeqGenerator.PREFIX,value="MR")} )
    @GeneratedValue(generator = "seqGenerator")
    @Column(name = "NO")
    private int id;


    @ManyToOne
    @JoinColumn(name = "MEMBER_NO",foreignKey = @ForeignKey(name = "fk_memberrole_to_member"))
    private Member member;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID",foreignKey = @ForeignKey(name = "fk_memberrole_to_role"))
    private Role role;

    @Builder MebmerRole(Member member, Role role) {
        this.member = member;
        this.role = role;
    }

}
