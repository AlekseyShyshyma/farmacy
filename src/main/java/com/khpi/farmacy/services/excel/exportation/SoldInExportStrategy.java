package com.khpi.farmacy.services.excel.exportation;

import com.khpi.farmacy.dtos.SoldInPeriodDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Need for exporting excel format files,
 * which contains sold in period data
 *
 * @see ExportStrategyStorageService
 **/
@Component("soldInExportStrategy")
public class SoldInExportStrategy extends AbstractExportService<SoldInPeriodDto> {

    @Override
    protected void fillRowByModelData(Row row, SoldInPeriodDto model) {

        Long soldId = model.getSoldId();
        Cell soldIdCell = row.createCell(0);
        soldIdCell.setCellValue(soldId);

        double sum = model.getSum();
        Cell sumCell = row.createCell(1);
        sumCell.setCellValue(sum);

        int amount = model.getAmount();
        Cell amountCell = row.createCell(2);
        amountCell.setCellValue(amount);

        LocalDate periodStart = model.getPeriodStart();
        Cell periodStartCell = row.createCell(3);
        periodStartCell.setCellValue(periodStart.toString());

        LocalDate periodEnd = model.getPeriodStart();
        Cell periodEndCell = row.createCell(4);
        periodEndCell.setCellValue(periodEnd.toString());

        Long managerCode = model.getManagerCode();
        Cell managerCodeCell = row.createCell(5);
        managerCodeCell.setCellValue(managerCode);

        Long drugstoreCode = model.getDrugstoreCode();
        Cell drugstoreCodeCell = row.createCell(6);
        drugstoreCodeCell.setCellValue(drugstoreCode);

        Long medicineCode = model.getMedicineCode();
        Cell medicineCodeCell = row.createCell(7);
        medicineCodeCell.setCellValue(medicineCode);
    }

    @Override
    protected String getSheetName() {
        return "Sold In";
    }

    @Override
    protected List<String> getHeaderNames() {
        return Arrays.asList("Sold Id", "Sum", "Amount", "Period Start",
                "Period End", "Manager Code", "Drugstore Code",
                "Medicine Code");
    }

    @Override
    public String toString() {
        return "sold in";
    }
}
