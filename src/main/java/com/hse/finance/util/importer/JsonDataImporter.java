package com.hse.finance.util.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hse.finance.facade.BankAccountFacade;
import com.hse.finance.facade.CategoryFacade;
import com.hse.finance.facade.OperationFacade;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Importer for JSON format files
 */
@Component
public class JsonDataImporter extends DataImporter {
    private final ObjectMapper objectMapper;
    
    public JsonDataImporter(BankAccountFacade bankAccountFacade, 
                           CategoryFacade categoryFacade, 
                           OperationFacade operationFacade) {
        super(bankAccountFacade, categoryFacade, operationFacade);
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    protected Map<String, Object> parseFile(File file) throws IOException {
        return objectMapper.readValue(file, Map.class);
    }
} 