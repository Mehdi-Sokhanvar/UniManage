package org.unimanage.util.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class MajorDto {
    private Long id;
    private String name;
    private byte numberOfUnits;
}
