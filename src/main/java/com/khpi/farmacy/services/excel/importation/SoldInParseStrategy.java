package com.khpi.farmacy.services.excel.importation;

import com.khpi.farmacy.dtos.SoldInPeriodDto;
import com.khpi.farmacy.utils.LocalDateUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Need for parsing from excel format files,
 * which contains sold in period data
 *
 * @see ParsingStrategyStorageService
 **/
@Service("soldInStrategy")
public class SoldInParseStrategy extends AbstractParserService<SoldInPeriodDto> {

    @Override
    protected List<String> getHeaderNames() {
        return Arrays.asList("Sold Id", "Sum", "Amount", "Period Start",
                "Period End", "Manager Code", "Drugstore Code",
                "Medicine Code");
    }

    @Override
    protected boolean areRowTypesValid(Map<String, String> rowMap) {

        return rowMap.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("Period Start")
                        && !entry.getKey().equals("Period End"))
                .allMatch(entry -> NumberUtils.isCreatable(entry.getValue()))

                && LocalDateUtils.isDateParsable(rowMap.get("Period Start"))
                && LocalDateUtils.isDateParsable(rowMap.get("Period End"));
    }

    @Override
    protected SoldInPeriodDto map(Map<String, String> rowMap) {

        long soldId = (long) Double.parseDouble(rowMap.get("Sold Id"));
        long drugstoreCode = (long) Double.parseDouble(rowMap.get("Drugstore Code"));
        long managerCode = (long) Double.parseDouble(rowMap.get("Manager Code"));
        long medicineCode = (long) Double.parseDouble(rowMap.get("Medicine Code"));

        int amount = (int) Double.parseDouble(rowMap.get("Amount"));

        double sum = Double.parseDouble(rowMap.get("Sum"));

        LocalDate periodStart = LocalDateUtils.blackBoxParse(rowMap.get("Period Start"));
        LocalDate periodEnd = LocalDateUtils.blackBoxParse(rowMap.get("Period End"));

        return SoldInPeriodDto.builder()
                .soldId(soldId)
                .amount(amount)
                .sum(sum)
                .drugstoreCode(drugstoreCode)
                .managerCode(managerCode)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .medicineCode(medicineCode)
                .build();
    }
}
