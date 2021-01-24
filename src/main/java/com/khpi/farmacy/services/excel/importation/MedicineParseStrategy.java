package com.khpi.farmacy.services.excel.importation;

import com.khpi.farmacy.dtos.MedicineDto;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Need for parsing from excel format files,
 * which contains medicine data
 *
 * @see ParsingStrategyStorageService
 **/
@Service("medicineStrategy")
public class MedicineParseStrategy extends AbstractParserService<MedicineDto> {

    @Override
    protected List<String> getHeaderNames() {
        return Arrays.asList("Medicine Code", "Title", "Expiration Term",
                "Price", "Measurement Unit", "Manufacturer Code");
    }

    @Override
    protected boolean areRowTypesValid(Map<String, String> rowMap) {
        return rowMap.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("Title")
                        && !entry.getKey().equals("Expiration Term")
                        && !entry.getKey().equals("Measurement Unit")
                )
                .allMatch(entry -> NumberUtils.isCreatable(entry.getValue()));
    }


    @Override
    protected MedicineDto map(Map<String, String> rowMap) {

        double price = Double.parseDouble(rowMap.get("Price"));

        String title = rowMap.get("Title");
        String expirationTerm = rowMap.get("Expiration Term");
        String measurementUnit = rowMap.get("Measurement Unit");

        long medicineCode = (long) Double.parseDouble(rowMap.get("Medicine Code"));
        long manufacturerCode = (long) Double.parseDouble(rowMap.get("Manufacturer Code"));

        return MedicineDto.builder()
                .price(price)
                .title(title)
                .expirationTerm(expirationTerm)
                .measurementUnit(measurementUnit)
                .medicineCode(medicineCode)
                .manufacturerCode(manufacturerCode)
                .build();
    }
}
