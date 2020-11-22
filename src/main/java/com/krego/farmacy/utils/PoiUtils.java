package com.krego.farmacy.utils;

import lombok.NoArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class PoiUtils {

    public static boolean isCellEmpty(XSSFCell cell) {
        return (cell == null) || cell.toString().equals("");
    }

    public static boolean isCellNumber(XSSFCell cell) {
        return !isCellEmpty(cell) && cell.toString().chars().noneMatch(Character::isLetter);
    }

    public static Map<String, Integer> getCellsContent(XSSFRow row) {
        Map<String, Integer> indexesMap = new HashMap<>();

        for (int i = 0; i < row.getLastCellNum(); i++) {

            if (!isCellEmpty(row.getCell(i)))
                indexesMap.put(row.getCell(i).getStringCellValue(), i);
        }

        return indexesMap;
    }

}

