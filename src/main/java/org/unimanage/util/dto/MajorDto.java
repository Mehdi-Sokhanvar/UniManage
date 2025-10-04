package org.unimanage.util.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class MajorDto {
    private String majorName;
    private byte numberOfUnits;
}
