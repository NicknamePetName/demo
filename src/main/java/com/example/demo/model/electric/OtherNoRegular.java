package com.example.demo.model.electric;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 其它不合格项表
 */
public class OtherNoRegular implements Serializable {
    private Integer id;
    private String oneText;
    private String twoText;
    private String threeText;
    private String fourText;
    private String fiveText;
    private String sixText;
    private String oneCorrector;
    private String twoCorrector;
    private String threeCorrector;
    private String fourCorrector;
    private String fiveCorrector;
    private String sixCorrector;
    private String oneRecheck;
    private String twoRecheck;
    private String threeRecheck;
    private String fourRecheck;
    private String fiveRecheck;
    private String sixRecheck;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
