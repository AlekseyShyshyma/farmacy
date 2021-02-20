package com.khpi.farmacy.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDto implements Dto {

    private Long medicineCode;
    private Long manufacturerCode;

    private String title;
    private String expirationTerm;
    private String measurementUnit;

    private Double price;
}
