package com.khpi.farmacy.services.excel.exportation;

import com.khpi.farmacy.dtos.DrugstoreDto;
import com.khpi.farmacy.dtos.Dto;
import com.khpi.farmacy.dtos.MedicineDto;
import com.khpi.farmacy.dtos.SoldInPeriodDto;
import com.khpi.farmacy.exception.StrategyNotFoundException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

@Service
public final class ExportStrategyStorageService {

    private static final Map<String, AbstractExportService<?>> STRATEGIES_MAP = new HashMap<>();
    private static final List<String> KEY_LIST = Arrays.asList("drugstoreExportStrategy",
            "medicineExportStrategy", "soldInExportStrategy");

    private static final String FOLDER_PATH = "server/generated/excel/";
    private static final String EXCEL_SUFFIX = "export.xlsx";
    private static final UnaryOperator<String> GENERATE_FILE_NAME = (filePrefix) ->
            FOLDER_PATH + filePrefix + EXCEL_SUFFIX;

    @Autowired
    public ExportStrategyStorageService(@Qualifier("drugstoreExportStrategy")
                                                AbstractExportService<DrugstoreDto> drugstoreExportService,
                                        @Qualifier("medicineExportStrategy")
                                                AbstractExportService<MedicineDto> medicineExportService,
                                        @Qualifier("soldInExportStrategy")
                                                AbstractExportService<SoldInPeriodDto> soldInExportService) {

        STRATEGIES_MAP.put(KEY_LIST.get(0), drugstoreExportService);
        STRATEGIES_MAP.put(KEY_LIST.get(1), medicineExportService);
        STRATEGIES_MAP.put(KEY_LIST.get(2), soldInExportService);
    }

    private <M extends Dto> AbstractExportService<M> getExporter(Class<M> model) {
        if (model == DrugstoreDto.class) {
            return (AbstractExportService<M>) STRATEGIES_MAP.get(KEY_LIST.get(0));
        }
        if (model == MedicineDto.class) {
            return (AbstractExportService<M>) STRATEGIES_MAP.get(KEY_LIST.get(1));
        }
        if (model == SoldInPeriodDto.class) {
            return (AbstractExportService<M>) STRATEGIES_MAP.get(KEY_LIST.get(2));
        }
        throw new StrategyNotFoundException("Unknown Strategy");
    }

    public <M extends Dto> byte[] parse(Class<M> modelType, List<M> models) throws IOException {

        AbstractExportService<M> exporter = getExporter(modelType);
        Workbook exportedWorkbook = exporter.export(models);

        String generatedFileName = ExportStrategyStorageService.GENERATE_FILE_NAME.apply(exporter.toString());
        OutputStream outputStream = new FileOutputStream(generatedFileName);
        exportedWorkbook.write(outputStream);

        exportedWorkbook.close();
        outputStream.close();
        return Files.readAllBytes(Paths.get(generatedFileName));
    }
}
