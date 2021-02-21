package com.khpi.farmacy.services.excel.exportation;

import com.khpi.farmacy.dtos.DrugstoreDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Need for exporting excel format files,
 * which contains drugstore data
 *
 * @see ExportStrategyStorageService
 **/
@Component("drugstoreExportStrategy")
public class DrugstoreExportStrategy extends AbstractExportService<DrugstoreDto> {

    @Override
    protected void fillRowByModelData(Row row, DrugstoreDto model) {

        long drugstoreCode = model.getDrugstoreCode();
        Cell drugstoreCodeCell = row.createCell(0);
        drugstoreCodeCell.setCellValue(drugstoreCode);

        String address = model.getAddress();
        Cell addressCell = row.createCell(1);
        addressCell.setCellValue(address);

        String networkTitle = model.getNetworkTitle();
        Cell networkTitleCell = row.createCell(2);
        networkTitleCell.setCellValue(networkTitle);

        String phoneNumber = model.getPhoneNumber();
        Cell phoneNumberCell = row.createCell(3);
        phoneNumberCell.setCellValue(phoneNumber);

        String region = model.getRegion();
        Cell regionCell = row.createCell(4);
        regionCell.setCellValue(region);

        long managerCode = model.getManagerCode();
        Cell managerCodeCell = row.createCell(5);
        managerCodeCell.setCellValue(managerCode);
    }

    @Override
    protected String getSheetName() {
        return "Drugstores";
    }

    @Override
    protected List<String> getHeaderNames() {
        return Arrays.asList("Drugstore code", "Address",
                "Network title", "Phone number", "Region", "Manager code");
    }

    @Override
    public String toString() {
        return "drugstore";
    }
}
