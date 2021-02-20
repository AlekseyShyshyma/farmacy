package com.khpi.farmacy.services.excel.exportation;

import com.khpi.farmacy.dtos.MedicineDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component("medicineExportStrategy")
public class MedicineExportStrategy extends AbstractExportService<MedicineDto>{

    @Override
    protected void fillRowByModelData(Row row, MedicineDto model) {

        long medicineCode = model.getMedicineCode();
        Cell medicineCodeCell = row.createCell(0);
        medicineCodeCell.setCellValue(medicineCode);

        String title = model.getTitle();
        Cell titleCell = row.createCell(1);
        titleCell.setCellValue(title);

        String expirationTerm = model.getExpirationTerm();
        Cell expirationTermCell = row.createCell(2);
        expirationTermCell.setCellValue(expirationTerm);

        Double price = model.getPrice();
        Cell priceCell = row.createCell(3);
        priceCell.setCellValue(price);

        String measurementUnit = model.getMeasurementUnit();
        Cell measurementUnitCell = row.createCell(4);
        measurementUnitCell.setCellValue(measurementUnit);

        Long manufacturerCode = model.getManufacturerCode();
        Cell manufacturerCodeCell = row.createCell(5);
        manufacturerCodeCell.setCellValue(manufacturerCode);
    }

    @Override
    protected String getSheetName() {
        return "Medicines";
    }

    @Override
    protected List<String> getHeaderNames() {
        return Arrays.asList("Medicine Code", "Title", "Expiration Term",
                "Price", "Measurement Unit", "Manufacturer Code");
    }

    @Override
    public String toString() {
        return "medicine";
    }
}
