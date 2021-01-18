package com.khpi.farmacy.services.excel.importation.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugstoreDto {

    private long drugstoreCode;
    private String address;
    private String networkTitle;
    private String phoneNumber;
    private String region;
    private long managerCode;
}
