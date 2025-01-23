package com.toongather.toongather.domain.keyword.domain;

import com.toongather.toongather.SeqGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Table(name = "KEYWORD")
@Entity
public class Keyword {

  @Id
  @GenericGenerator(name="seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
      parameters ={@org.hibernate.annotations.Parameter(name= SeqGenerator.SEQ_NAME,value="KEYWORD_SEQ"),
          @org.hibernate.annotations.Parameter(name= SeqGenerator.PREFIX,value="KE")} )
  @GeneratedValue(generator = "seqGenerator")
  private String keywordId;

  @Column
  private String keywordNm;

}
