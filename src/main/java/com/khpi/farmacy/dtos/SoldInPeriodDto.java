package com.khpi.farmacy.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoldInPeriodDto implements Dto {

    private int amount;

    private double sum;

    private LocalDate periodStart;
    private LocalDate periodEnd;

    private Long soldId;
    private Long drugstoreCode;
    private Long managerCode;
    private Long medicineCode;
}
