package com.krego.farmacy.services.importing.parsers;

import com.krego.farmacy.services.importing.dtos.DrugstoreDto;
import com.krego.farmacy.utils.PoiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * Generally, need for parsing from excel format files and for
 **/
@Slf4j
@Service("drugstoreParser")
public class DrugStoreParserService implements Parser<DrugstoreDto> {

    private static final int HEADER_ROW_INDEX = 0;
    private static final int CONTENT_ROW_START_POSITION = 1;
    private static final int EXCEL_SHEET_INDEX = 0;

    private static final List<String> headerRequirements = Arrays.asList(
            "Drugstore code", "Address",
            "Network title", "Phone number",
            "Region", "Manager code");


    @Override
    public Optional<List<DrugstoreDto>> parse(InputStream inputStream) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(EXCEL_SHEET_INDEX);
        XSSFRow row = sheet.getRow(HEADER_ROW_INDEX);

        Map<String, Integer> headersFromFileMap = PoiUtils.getCellsContent(row);
        if (verifyHeader(headersFromFileMap, headerRequirements)) {

            List<DrugstoreDto> drugstores = new ArrayList<>();
            int currentRowIndex = CONTENT_ROW_START_POSITION;
            while (Objects.nonNull(sheet.getRow(currentRowIndex))) {

                XSSFRow currentContentRow = sheet.getRow(currentRowIndex);
                Map<String, String> fromRowMap = verifyParticularRow(currentContentRow,
                        headersFromFileMap,
                        headerRequirements)
                        ? getFromRow(currentContentRow, headersFromFileMap)
                        : Collections.emptyMap();

                //if all data is present
                if (fromRowMap.size() >= headerRequirements.size()) {

                    Double drugstoreCode = Double.parseDouble(fromRowMap.get("Drugstore code"));
                    Double managerCode = Double.parseDouble(fromRowMap.get("Manager code"));
                    String address = fromRowMap.get("Address");
                    String networkTitle = fromRowMap.get("Network title");
                    String phoneNumber = fromRowMap.get("Phone number");
                    String region = fromRowMap.get("Region");

                    DrugstoreDto drugstoreDto = DrugstoreDto.builder()
                            .drugstoreCode(drugstoreCode.longValue())
                            .managerCode(managerCode.longValue())
                            .address(address)
                            .networkTitle(networkTitle)
                            .phoneNumber(phoneNumber)
                            .region(region)
                            .build();

                    drugstores.add(drugstoreDto);
                }
                currentRowIndex++;
            }
            return Optional.ofNullable(drugstores);
        }

        return Optional.empty();
    }

}
