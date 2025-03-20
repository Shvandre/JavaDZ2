package com.hse.finance.util.importer;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.hse.finance.facade.BankAccountFacade;
import com.hse.finance.facade.CategoryFacade;
import com.hse.finance.facade.OperationFacade;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of DataImporter for CSV files
 */
@Component
public class CsvDataImporter extends DataImporter {
    private final CsvMapper csvMapper;
    
    public CsvDataImporter(BankAccountFacade bankAccountFacade, 
                          CategoryFacade categoryFacade, 
                          OperationFacade operationFacade) {
        super(bankAccountFacade, categoryFacade, operationFacade);
        this.csvMapper = new CsvMapper();
    }
    
    @Override
    protected Map<String, Object> parseFile(File file) throws IOException {
        Map<String, Object> result = new HashMap<>();
        
        // Read accounts from accounts.csv
        File accountsFile = new File(file.getParentFile(), "accounts.csv");
        if (accountsFile.exists()) {
            result.put("accounts", readCsvFile(accountsFile));
        }
        
        // Read categories from categories.csv
        File categoriesFile = new File(file.getParentFile(), "categories.csv");
        if (categoriesFile.exists()) {
            result.put("categories", readCsvFile(categoriesFile));
        }
        
        // Read operations from operations.csv
        File operationsFile = new File(file.getParentFile(), "operations.csv");
        if (operationsFile.exists()) {
            result.put("operations", readCsvFile(operationsFile));
        }
        
        return result;
    }
    
    private List<Map<String, Object>> readCsvFile(File file) throws IOException {
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        MappingIterator<Map<String, Object>> iterator = csvMapper.readerFor(Map.class)
                .with(schema)
                .readValues(file);
        return iterator.readAll();
    }
} 