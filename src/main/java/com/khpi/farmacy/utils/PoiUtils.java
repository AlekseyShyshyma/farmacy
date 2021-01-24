package com.khpi.farmacy.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Current class have utility methods for Apache Poi library
 */
public final class PoiUtils {

    private PoiUtils() {
    }

    public static boolean isCellEmpty(Cell cell) {
        return (cell == null) || cell.toString().equals("");
    }

    public static boolean isCellNotEmpty(Cell cell) {
        return !isCellEmpty(cell);
    }

    public static boolean isCellNumber(Cell cell) {
        return !isCellEmpty(cell) && cell.toString().chars().noneMatch(Character::isLetter);
    }

    public static boolean isCellNotNumber(Cell cell) {
        return !isCellNumber(cell);
    }

    public static boolean isRowEmpty(Row row) {
        Iterator<Cell> iterator = row.iterator();
        return !iterator.hasNext();
    }

    public static boolean isRowNotEmpty(Row row) {
        return !isRowEmpty(row);
    }

    public static boolean isSheetExists(Workbook workbook, String sheetName) {
        return workbook.getSheet(sheetName) != null;
    }

    /**
     * Verifies if all cells in row have string type
     * If row is empty - returns false
     */
    public static boolean areAllCellsString(Row row) {

        if (isRowEmpty(row)) {
            return false;
        }

        Iterator<Cell> iterator = row.iterator();
        while (iterator.hasNext()) {

            Cell currentCell = iterator.next();
            if (isCellNumber(currentCell)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Verifies if all cells in row have number type
     * If row is empty - returns false
     */
    public static boolean areAllCellsNumber(Row row) {

        if (isRowEmpty(row)) {
            return false;
        }

        Iterator<Cell> iterator = row.iterator();
        while (iterator.hasNext()) {

            Cell currentCell = iterator.next();
            if (!isCellNumber(currentCell)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns Map, where key cell data
     * and value it's position according X coordinate
     */
    public static Map<String, Integer> getCellsContent(Row row) {
        Map<String, Integer> indexesMap = new HashMap<>();
        for (int currentIndex = 0; currentIndex < row.getLastCellNum(); currentIndex++) {
            if (!isCellEmpty(row.getCell(currentIndex)))
                indexesMap.put(row.getCell(currentIndex).getStringCellValue().trim(), currentIndex);
        }

        return indexesMap;
    }

    public static boolean isSheetExists(Workbook workbook, int sheetIndex) {
        try {
            workbook.getSheetAt(sheetIndex);
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

}

