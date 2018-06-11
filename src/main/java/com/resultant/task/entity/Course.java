package com.resultant.task.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @JsonProperty
    private LocalDate day;

    @Column
    @JsonProperty
    private BigDecimal curs;

    @Column
    @JsonProperty
    private Long code;

    @Column(name = "ch_code")
    @JsonProperty
    private String chCode;

    @Column
    @JsonProperty
    private String name;

    @Column
    @JsonProperty
    private Long nom;

    @Transient
    public static Course create(Map<String, String> courseMap){

        Course course = new Course();
        course.setCurs(new BigDecimal(courseMap.get("Vcurs")));
        course.setCode(Long.valueOf(courseMap.get("Vcode")));
        course.setChCode(courseMap.get("VchCode"));
        course.setName(courseMap.get("Vname"));
        course.setNom(Long.valueOf(courseMap.get("Vnom")));
        return course;

    }

}
