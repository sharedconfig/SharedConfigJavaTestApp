package com.example.sharedconfigjavatestapp.configuration;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sharedconfig.core.ApplicationSettings;
import sharedconfig.core.ConfigurationEngine;
import sharedconfig.core.SharedConfigConfigurer;
import sharedconfig.core.interfaces.IScopedConfigurationService;
import sharedconfig.core.interfaces.ISharedConfigMonitor;
import sharedconfig.core.loger.configuration.SharedConfigLoggerConfigurer;

import javax.inject.Singleton;

@Configuration
@Log4j2
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

        // проверяем что все файлы для конфигурации извлечены из архива
        var configurationFolder = SharedConfigConfigurer.ensureConfigurationFilesExtracted(UpConfiguration.class, "up-configuration");

        // включаем сохранения логов конфигурирования в файл
        SharedConfigLoggerConfigurer.traceLogsToFile(traceLogPath);

        var appSettings = ApplicationSettings.create(
                configurationFolder,
                "app-declaration.xml",
                "../.config",
                appName,
                appVersion);

        var engine = ConfigurationEngine.create(appSettings, configurationFolder);
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
