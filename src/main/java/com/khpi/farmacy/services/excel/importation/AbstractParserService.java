package com.khpi.farmacy.services.excel.importation;

import com.khpi.farmacy.utils.PoiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @see ParsingStrategyStorageService
 */
@Slf4j
public abstract class AbstractParserService<M> {

    private static final int HEADER_ROW_INDEX = 0;
    private static final int CONTENT_ROW_START_POSITION = 1;
    private static final int EXCEL_SHEET_INDEX = 0;
    private static final String MISSED_TOKEN = "MISSED";


    /**
     * Verifies if header from excel file belongs to import parsing standard,
     * returns boolean value
     *
     * @param headerMap it's map, which represent header name(Key) from parsed excel file
     *                  and it's position according X coordinate(Value)
     */
    private boolean isHeaderMeetsCriteria(Map<String, Integer> headerMap) {
        return getHeaderNames().stream()
                .allMatch(name -> headerMap.containsKey(name))
                && getHeaderNames().size() == headerMap.size();
    }


    /**
     * Verifies if row cells have all data and not empty
     *
     * @param rowMap map with key(cell name) and value(it's data)
     */
    private boolean isRowContainsFullData(Map<String, String> rowMap) {
        return !rowMap.entrySet().stream()
                .anyMatch(item -> item.getValue().equals(MISSED_TOKEN));

    }


    /**
     * Returns Map, where key is name of particular cell and
     * key is value of parsed cell
     *
     * @param row       it's row, from which need to retrieve data
     * @param headerMap it's map, which represent header name(Key) from parsed excel file
     *                  *                           and it's position according X coordinate(Value)
     */
    private Map<String, String> getFromRow(XSSFRow row, Map<String, Integer> headerMap) {
        return headerMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        (entry) -> row.getCell(entry.getValue()) != null
                                ? row.getCell(entry.getValue()).toString()
                                : MISSED_TOKEN));
    }

    /**
     * Parsing process
     *
     * @param inputStream - excel's data
     */
    protected List<M> parse(InputStream inputStream) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(EXCEL_SHEET_INDEX);
        XSSFRow row = sheet.getRow(HEADER_ROW_INDEX);

        List<M> successfullyParsedModels = new ArrayList<>();
        Map<String, Integer> headerMap = PoiUtils.getCellsContent(row);

        if (!isHeaderMeetsCriteria(headerMap)) {
            throw new RuntimeException("Excel header does not meet criteria");
        }

        int currentRowIndex = CONTENT_ROW_START_POSITION;
        while (Objects.nonNull(sheet.getRow(currentRowIndex))) {

            XSSFRow currentContentRow = sheet.getRow(currentRowIndex);
            Map<String, String> fromRow = getFromRow(currentContentRow, headerMap);

            if (!isRowContainsFullData(fromRow)
                    || !areRowTypesValid(fromRow)) {

                log.info("Current row doesn't match the criteria..");
                log.info(String.format("Ignore parsing row  #%s",
                        currentContentRow.getRowNum()));
                currentRowIndex++;
                continue;
            }

            M parsedModel = map(fromRow);
            successfullyParsedModels.add(parsedModel);
            currentRowIndex++;
        }

        return successfullyParsedModels;
    }

    abstract protected boolean areRowTypesValid(Map<String, String> rowMap);

    abstract protected List<String> getHeaderNames();

    abstract protected M map(Map<String, String> rowMap);
}
