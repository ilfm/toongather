package com.toongather.toongather.domain.webtoon.domain;

import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.global.common.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Table(name="WEBTOON_GENRE_KEYWORD")
@Entity
public class WebtoonGenreKeyword extends BaseEntity {

    @Id
    @GenericGenerator(name="seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
            parameters ={@org.hibernate.annotations.Parameter(name= SeqGenerator.SEQ_NAME,value="WEBTOON_GENRE_KEYWORD_SEQ"),
                    @org.hibernate.annotations.Parameter(name= SeqGenerator.PREFIX,value="WGK")} )
    @GeneratedValue(generator = "seqGenerator")
    private String webtoonGenreKeywordId;

    @ManyToOne
    @JoinColumn(name="toonId",foreignKey = @ForeignKey(name = "fk_toongenrekeword_to_toon"))
    private Webtoon webtoon;

    @ManyToOne
    @JoinColumn(name="genreKeywordId",foreignKey = @ForeignKey(name = "fk_toongenrekeword_to_genrekeyword"))
    private GenreKeyword genreKeyword;

}
