package com.sas.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class EmailTemplate {

    private String template;
    private Map<String, String> replacementParams;

    public EmailTemplate(String customTemplate) {
        try {
            this.template = loadTemplate(customTemplate);
        } catch (Exception e) {
            this.template = "Empty";
        }
    }

    private String loadTemplate(String templateName) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(templateName).getFile());
        String content = "Empty";
        try {
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new Exception("Could not read template  = " + templateName);
        }
        return content;
    }

    public String getTemplate(Map<String, String> replacements) {
        String templateContent = this.template;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            templateContent = templateContent.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return templateContent;
    }
}

