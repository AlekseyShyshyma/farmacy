package com.khpi.farmacy.services.excel.importation;

import com.khpi.farmacy.dtos.DrugstoreDto;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Need for parsing from excel format files,
 * which contains drugstore data
 *
 * @see ParsingStrategyStorageService
 **/
@Service("drugstoreStrategy")
public class DrugstoreParseStrategy extends AbstractParserService<DrugstoreDto> {


    @Override
    protected List<String> getHeaderNames() {
        return Arrays.asList("Drugstore code", "Address",
                "Network title", "Phone number", "Region", "Manager code");
    }

    @Override
    protected boolean areRowTypesValid(Map<String, String> rowMap) {
        return NumberUtils.isCreatable(rowMap.get("Drugstore code"))
                && NumberUtils.isCreatable(rowMap.get("Manager code"));
    }

    @Override
    protected DrugstoreDto map(Map<String, String> rowMap) {

        String address = rowMap.get("Address");
        String networkTitle = rowMap.get("Network title");
        String phoneNumber = rowMap.get("Phone number");
        String region = rowMap.get("Region");

        long drugstoreCode = (long) Double.parseDouble(rowMap.get("Drugstore code"));
        long managerCode = (long) Double.parseDouble(rowMap.get("Manager code"));

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
