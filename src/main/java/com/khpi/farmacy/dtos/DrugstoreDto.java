package com.khpi.farmacy.dtos;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrugstoreDto {

    private long managerCode;
    private long drugstoreCode;

    private String address;
    private String networkTitle;
    private String phoneNumber;
    private String region;
}
