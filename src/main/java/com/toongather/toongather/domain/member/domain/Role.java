package com.toongather.toongather.domain.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ROLE")
@Entity
@Getter
@SequenceGenerator(
  name = "ROLE_SEQ_GEN",
  sequenceName = "ROLE_SEQ",
  initialValue = 1,
  allocationSize = 1
)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator ="ROLE_SEQ_GEN" )
    @Column(name = "ROLE_ID")
    private Long id;

    @Column(name= "ROLE_NM")
    @Enumerated(EnumType.STRING)
    private RoleType name;

//    @OneToMany(mappedBy = "role")
//    private List<MemberRole> roleMembers = new ArrayList<>();

    @Builder
    public Role(RoleType name) {
        this.name = name;
    }
}
