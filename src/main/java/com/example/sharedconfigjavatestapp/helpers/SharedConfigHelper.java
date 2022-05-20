package com.example.sharedconfigjavatestapp.helpers;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import sharedconfig.helpers.FileHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SharedConfigHelper {
    /**
     * Если приложение запущено в контексте jar - извлекает необходимые конфигурационные xml в папку с jar
     * Если нет - ничего не делает
     * @return current executable folder
     */
    public static String ensureConfigurationFilesExtracted(String... appConfigNames) throws IOException {
        log.info("Trying to ensure configuration files extracted");

        var currentExecutable = ClassLocationHelper.urlToFile(ClassLocationHelper.getLocation(SharedConfigHelper.class));
        var currentExecutableFolder = currentExecutable.isDirectory() ? currentExecutable.toString() : currentExecutable.getParentFile().toString();
        log.info("Current executable folder : {}", currentExecutableFolder);

        var filesToCheck = new ArrayList<String>();
        filesToCheck.add("c-build.vars.xml");
        filesToCheck.add("c-dev.vars.xml");
        filesToCheck.addAll(List.of(appConfigNames));

        for (var fileToCheck : filesToCheck) {
            var filePath = FileHelper.combinePaths(currentExecutableFolder, fileToCheck);
            if (filePath.toFile().exists()) {
                continue;
            }

            var resource = new ClassPathResource(fileToCheck);
            if (!resource.exists()) {
                continue;
            }

            var appDeclXmlContent = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
            var appDeclXmlCopyPath = FileHelper.combinePaths(currentExecutableFolder, fileToCheck);
            Files.write(appDeclXmlCopyPath, appDeclXmlContent, StandardCharsets.UTF_8);
            log.info("File {} moved to {}", fileToCheck, appDeclXmlCopyPath);
        }
        return currentExecutableFolder;
    }
}
