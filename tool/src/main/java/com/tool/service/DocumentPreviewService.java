package com.tool.service;

import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DocumentPreviewService {

    private LocalOfficeManager officeManager;

    public synchronized void start() throws Exception {
        if (officeManager == null) {
            officeManager = LocalOfficeManager.builder().install().build();
            officeManager.start();
        }
    }

    public synchronized void stop() throws Exception {
        if (officeManager != null) {
            officeManager.stop();
            officeManager = null;
        }
    }

    public File convertToPdf(File inputFile, File outputFile) throws Exception {
        if (officeManager == null) {
            start();
        }
        LocalConverter.make(officeManager)
                .convert(inputFile)
                .to(outputFile)
                .execute();
        return outputFile;
    }
} 