package com.example.sharedconfigjavatestapp.configuration;

import com.example.sharedconfigjavatestapp.helpers.ClassLocationHelper;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sharedconfig.core.ApplicationSettings;
import sharedconfig.core.ConfigurationEngine;
import sharedconfig.core.SharedConfigLoggerConfigurer;
import sharedconfig.core.interfaces.IScopedConfigurationService;
import sharedconfig.core.interfaces.ISharedConfigMonitor;
import sharedconfig.helpers.SharedConfigHelperTwo;

import javax.inject.Singleton;

@Configuration @Log4j2
public class UpConfiguration {
    @Value("${sharedconfig.app-name}")
    private @NotNull String appName;
    @Value("${sharedconfig.app-version}")
    private @NotNull String appVersion;
    @Value("${sharedconfig.trace-log-path}")
    private @NotNull String traceLogPath;

    /**
     * Регистрация движка конфигурирования
     */
    @Bean
    @Singleton
    ConfigurationEngine getConfigurationEngine() throws Exception {
        log.info("Start configuring ConfigurationEngine");

        var currentExecutable = ClassLocationHelper.urlToFile(ClassLocationHelper.getLocation(UpConfiguration.class));
        var currentExecutableFolder = currentExecutable.isDirectory() ? currentExecutable.toString() : currentExecutable.getParentFile().toString();

        //тестирование нового метода
        if (!currentExecutable.isDirectory()) {
            // get path of the current running JAR
//            такой путь нужен
//            /C:/Users/Vasypu/Desktop/softconsalt/SharedConfigJava/SharedConfigJavaTestApp/target/SharedConfigJavaTestApp-0.0.1-SNAPSHOT.jar
            SharedConfigHelperTwo.ensureConfigurationFilesExtracted("/" + currentExecutable.toString().replace('\\','/'));
        }

        // проверяем что все файлы для конфигурации извлечены из архива
        //var currentExecutableFolder = SharedConfigHelper.ensureConfigurationFilesExtracted("app-declaration.xml");

        // включаем сохранения логов конфигурирования в файл
        SharedConfigLoggerConfigurer.traceLogsToFile(appName, appVersion, traceLogPath);

        var appSettings = ApplicationSettings.create(
                currentExecutableFolder,
                "app-declaration.xml",
                ".config",
                appName,
                appVersion);

        var engine = ConfigurationEngine.create(appSettings, currentExecutableFolder);
        //engine.waitAgent();
        //engine.waitStore();
        return engine;
    }


    /**
     * Регистрация сервиса конфигурированя с доступом к переменным уровня приложения
     */
    @Bean
    @Singleton
    IScopedConfigurationService<MyConfiguration> getApplicationConfigurationService(@Autowired ConfigurationEngine engine) {
        return engine.getApplicationConfigurationService(MyConfiguration::new);
    }

    /**
     * Регистрация провайдера актуальной конфигурации
     */
    @Bean
    @Singleton
    ISharedConfigMonitor<MyConfiguration> getMonitoredConfiguration(@Autowired IScopedConfigurationService<MyConfiguration> configurationService) {
        return configurationService.getObservedVersion();
    }
}
