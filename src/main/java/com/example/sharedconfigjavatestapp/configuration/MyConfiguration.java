package com.example.sharedconfigjavatestapp.configuration;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import sharedconfig.core.interfaces.IConfigurationVersionSnapshot;

import java.util.Map;
import java.util.Optional;

public class MyConfiguration {
    @Getter
    private final Long changesetId;
    @Getter
    private final @NotNull String connectionString1;
    @Getter
    private final @NotNull String connectionString2;
    @Getter
    private final @NotNull Long maxConcurrencyLevel;


    public MyConfiguration(IConfigurationVersionSnapshot builder) {
        changesetId = builder.getVersionId();
        connectionString1 = builder.getVariable("TestJavaApp.ConnectionString1").orElse("");
        connectionString2 = builder.getVariable("TestJavaApp.ConnectionString2").orElse("");
        maxConcurrencyLevel = builder.getVariable("TestJavaApp.MaxConcurrencyLevel").map(Long::parseLong).orElse(10L);
    }
}
