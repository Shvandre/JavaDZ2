package com.hse.finance.util.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hse.finance.facade.BankAccountFacade;
import com.hse.finance.facade.CategoryFacade;
import com.hse.finance.facade.OperationFacade;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Implementation of DataImporter for YAML files
 */
@Component
public class YamlDataImporter extends DataImporter {
    private final ObjectMapper yamlMapper;

    public YamlDataImporter(BankAccountFacade bankAccountFacade, 
                           CategoryFacade categoryFacade, 
                           OperationFacade operationFacade) {
        super(bankAccountFacade, categoryFacade, operationFacade);
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
    }

    @Override
    protected Map<String, Object> parseFile(File file) throws IOException {
        return yamlMapper.readValue(file, Map.class);
    }
} 