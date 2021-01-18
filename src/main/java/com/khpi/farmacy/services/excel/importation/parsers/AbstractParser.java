package com.khpi.farmacy.services.excel.importation.parsers;

import com.khpi.farmacy.utils.PoiUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classes, which extend this abstract class - must parse excel files
 */
public abstract class AbstractParser<M> {

    private static final int HEADER_ROW_INDEX = 0;
    private static final int CONTENT_ROW_START_POSITION = 1;
    private static final int EXCEL_SHEET_INDEX = 0;


    abstract protected boolean validateRow(Map<String, String> rowMap);

    abstract protected List<String> getHeaderNames();

    abstract protected M map(Map<String, String> rowMap);

    /**
     * Verifies if header from excel file belongs to system parsing standard,
     * returns boolean value
     *
     * @param headersFromFileMap it's map, which represent header name(Key) from parsed excel file
     *                           and it's position according X coordinate(Value)
     */
    private boolean verifyHeader(Map<String, Integer> headersFromFileMap) {
        return getHeaderNames().stream()
                .allMatch(name -> headersFromFileMap.containsKey(name));
    }


    /**
     * @param row,               which need to verify
     * @param headersFromFileMap map with key(cell name) and value(it's position)
     * @param names              list of cell names, which must be in 'headersFromFileMap' as keys
     */
    private boolean verifyParticularRow(XSSFRow row,
                                        Map<String, Integer> headersFromFileMap,
                                        List<String> names) {

        return names.stream().allMatch(name ->
                !PoiUtils.isCellEmpty(row.getCell(headersFromFileMap.get(name))));

    }


    /**
     * Returns Map, where key is name of particular cell and
     * key is value of parsed cell
     *
     * @param row                it's row, from which need to retrieve data
     * @param headersFromFileMap it's map, which represent header name(Key) from parsed excel file
     *                           *                           and it's position according X coordinate(Value)
     */
    private Map<String, String> getFromRow(XSSFRow row, Map<String, Integer> headersFromFileMap) {
        return headersFromFileMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        (entry) -> row.getCell(entry.getValue()).toString()));
    }

    /**
     * Returns list of models
     *
     * @param inputStream - excel's data
     */
    public Optional<List<M>> parse(InputStream inputStream) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(EXCEL_SHEET_INDEX);
        XSSFRow row = sheet.getRow(HEADER_ROW_INDEX);

        List<M> successfullyParsedModels = new ArrayList<>();
        Map<String, Integer> headersFromFileMap = PoiUtils.getCellsContent(row);
        if (verifyHeader(headersFromFileMap)) {

            int currentRowIndex = CONTENT_ROW_START_POSITION;
            while (Objects.nonNull(sheet.getRow(currentRowIndex))) {

                XSSFRow currentContentRow = sheet.getRow(currentRowIndex);
                Map<String, String> fromRowMap = verifyParticularRow(currentContentRow,
                        headersFromFileMap,
                        getHeaderNames())
                        ? getFromRow(currentContentRow, headersFromFileMap)
                        : Collections.emptyMap();

                if (fromRowMap.size() >= getHeaderNames().size()) {

                    M parsedModel = validateRow(fromRowMap) ? map(fromRowMap) : null;
                    Optional.ofNullable(parsedModel)
                            .ifPresent(successfullyParsedModels::add);
                }
                currentRowIndex++;
            }
        }

        return Optional.ofNullable(successfullyParsedModels);
    }
}
