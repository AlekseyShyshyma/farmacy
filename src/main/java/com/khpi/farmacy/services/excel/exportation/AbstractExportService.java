package com.khpi.farmacy.services.excel.exportation;

import com.khpi.farmacy.dtos.Dto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.List;

/**
 * @see ExportStrategyStorageService
 */
@Slf4j
public abstract class AbstractExportService<M extends Dto> {

    protected static final int HEADER_POSITION = 0;

    /**
     * Exporting process: returns excel file
     *
     * @param models
     * @return data as bytes
     */
    protected Workbook export(List<M> models) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(getSheetName());
        List<String> headerNames = getHeaderNames();

        Row headerRow = sheet.createRow(HEADER_POSITION);
        for (int i = 0; i < headerNames.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerNames.get(i));
        }

        for (int i = 0; i < models.size(); i++) {
            M currentModel = models.get(i);
            fillRowByModelData(sheet.createRow(i + 1), currentModel);
        }

        return workbook;
    }

    abstract protected void fillRowByModelData(Row row, M model);

    abstract protected String getSheetName();

    abstract protected List<String> getHeaderNames();
}
