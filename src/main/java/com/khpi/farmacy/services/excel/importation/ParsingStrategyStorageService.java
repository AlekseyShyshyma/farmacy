package com.khpi.farmacy.services.excel.importation;

import com.khpi.farmacy.dtos.DrugstoreDto;
import com.khpi.farmacy.dtos.MedicineDto;
import com.khpi.farmacy.dtos.SoldInPeriodDto;
import com.khpi.farmacy.exception.StrategyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public final class ParsingStrategyStorageService {

    private static final Map<String, AbstractParserService<?>> STRATEGIES_MAP = new HashMap<>();
    private static final List<String> KEY_LIST = Arrays.asList("drugstoreStrategy",
            "medicineStrategy", "soldInStrategy");

    @Autowired
    public ParsingStrategyStorageService(@Qualifier("drugstoreStrategy") AbstractParserService<DrugstoreDto> drugstoreParserService,
                                         @Qualifier("medicineStrategy") AbstractParserService<MedicineDto> medicineParserService,
                                         @Qualifier("soldInStrategy") AbstractParserService<SoldInPeriodDto> soldInParserService) {

        STRATEGIES_MAP.put(KEY_LIST.get(0), drugstoreParserService);
        STRATEGIES_MAP.put(KEY_LIST.get(1), medicineParserService);
        STRATEGIES_MAP.put(KEY_LIST.get(2), soldInParserService);
    }

    private <M> AbstractParserService<M> getParser(Class<M> model) {
        if (model == DrugstoreDto.class) {
            return (AbstractParserService<M>) STRATEGIES_MAP.get(KEY_LIST.get(0));
        }
        if (model == MedicineDto.class) {
            return (AbstractParserService<M>) STRATEGIES_MAP.get(KEY_LIST.get(1));
        }
        if (model == SoldInPeriodDto.class) {
            return (AbstractParserService<M>) STRATEGIES_MAP.get(KEY_LIST.get(2));
        }
        throw new StrategyNotFoundException("Unknown Strategy");
    }

    public <M> List<M> parse(InputStream inputStream, Class<M> model) throws IOException {
        return getParser(model).parse(inputStream);
    }
}
