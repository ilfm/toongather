package com.toongather.toongather.domain.member.domain;

import com.toongather.toongather.SeqGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ROLE")
@Entity
@Getter
public class Role {

    @Id
    @GenericGenerator(name="seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
            parameters ={@org.hibernate.annotations.Parameter(name= SeqGenerator.SEQ_NAME,value="ROLE_SEQ"),
                    @org.hibernate.annotations.Parameter(name= SeqGenerator.PREFIX,value="RL")} )
    @GeneratedValue(generator = "seqGenerator")
    @Column(name = "ROLE_ID")
    private int id;

    @Column(name= "ROLE_NM")
    private String name;

    @OneToMany(mappedBy = "role")
    private List<MebmerRole> roleMembers = new ArrayList<>();


    @Builder
    public Role (String name) {
        this.name = name;
    }




}
