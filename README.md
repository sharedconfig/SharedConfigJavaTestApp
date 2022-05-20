# Требования к окружению
- [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11) (я использовал jdk-11.0.14)
- [Maven](https://maven.apache.org/install.html) (желательно самый последний, я использовал 3.8.4)
 
После установки ```mvn -v``` должно отрабатывать без ошибок

# Сборка

- перейти в директорию ```../SharedConfigJava```

- выполнить 
```mvn clean install -DskipTests```

- вернуться в текущую директорию ```../SharedConfigJavaTestApp```

- выполнить 
```mvn clean package spring-boot:repackage -DskipTests```

- в результате после успешной сборки должен появиться файл
```./target/SharedConfigJavaTestApp-0.0.1-SNAPSHOT.jar```

# Конфигурация

Конфигурацию приложения лежит в ```./config/production.properties```

- ```sharedconfig.app-name``` - имя приложения
- ```sharedconfig.app-version``` - версия приложения
- ```sharedconfig.app-version``` - путь сохранения логов конфигурирования

# Запуск

Запустить можно командой 

```java -jar ./target/SharedConfigJavaTestApp-0.0.1-SNAPSHOT.jar --spring.config.location=./config/production.properties```

