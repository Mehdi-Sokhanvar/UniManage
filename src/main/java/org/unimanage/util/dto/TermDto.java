package org.unimanage.util.dto;

import lombok.*;
import org.unimanage.util.enumration.TermStatus;

import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TermDto {
    private Long id;
    private int year;
    private String startTime;
    private String endTime;
    private String majorName;
    private Long majorId;

}
