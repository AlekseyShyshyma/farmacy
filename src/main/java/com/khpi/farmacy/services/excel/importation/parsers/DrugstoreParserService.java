package com.khpi.farmacy.services.excel.importation.parsers;

import com.khpi.farmacy.services.excel.importation.dtos.DrugstoreDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import java.util.*;


/**
 * Generally, need for parsing from excel format files,
 * which contains drugstore data
 **/
@Slf4j
@Service("drugstoreParser")
public class DrugstoreParserService extends AbstractParser<DrugstoreDto> {


    @Override
    public List<String> getHeaderNames() {
        return Arrays.asList("Drugstore code", "Address",
                "Network title", "Phone number", "Region", "Manager code");
    }

    @Override
    public boolean validateRow(Map<String, String> rowMap) {
        return NumberUtils.isCreatable(rowMap.get("Drugstore code"))
                && NumberUtils.isCreatable(rowMap.get("Manager code"));
    }

    @Override
    public DrugstoreDto map(Map<String, String> rowMap) {

        String address = rowMap.get("Address");
        String networkTitle = rowMap.get("Network title");
        String phoneNumber = rowMap.get("Phone number");
        String region = rowMap.get("Region");

        long drugstoreCode = Long.parseLong(rowMap.get("Drugstore code"));
        long managerCode = Long.parseLong(rowMap.get("Manager code"));

        return DrugstoreDto.builder()
                .drugstoreCode(drugstoreCode)
                .managerCode(managerCode)
                .address(address)
                .networkTitle(networkTitle)
                .phoneNumber(phoneNumber)
                .region(region)
                .build();
    }
}
