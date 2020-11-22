package com.krego.farmacy.services.importing.parsers;

import com.krego.farmacy.utils.PoiUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Parser<M> {

    Optional<List<M>> parse(InputStream inputStream) throws IOException;


    /**
     * Verifies if header from excel file belongs to system parsing standard,
     * returns boolean value
     *
     * @param headersFromFileMap it's map, which represent header name(Key) from parsed excel file
     *                           and it's position according X coordinate(Value)
     * @param headerRequirements it's list of cell names, which must be in 'headersFromFileMap' as keys
     */
    default boolean verifyHeader(Map<String, Integer> headersFromFileMap, List<String> headerRequirements) {
        return headerRequirements.stream().allMatch(name -> headersFromFileMap.containsKey(name));
    }


    /**
     * @param row,               which need to verify
     * @param headersFromFileMap map with key(cell name) and value(it's position)
     * @param names              list of cell names, which must be in 'headersFromFileMap' as keys
     */
    default boolean verifyParticularRow(XSSFRow row,
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
    default Map<String, String> getFromRow(XSSFRow row, Map<String, Integer> headersFromFileMap) {
        return headersFromFileMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        (entry) -> row.getCell(entry.getValue()).toString()));
    }
}
