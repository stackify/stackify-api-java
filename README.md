[![Maven Central](https://img.shields.io/maven-central/v/com.stackify/stackify-api-java.svg)](http://mvnrepository.com/artifact/com.stackify/stackify-api-java)
[![Coverage Status](https://coveralls.io/repos/stackify/stackify-api-java/badge.png?branch=master)](https://coveralls.io/r/stackify/stackify-api-java?branch=master)

## Stackify API for Java

* **Errors and Logs Overview:** http://support.stackify.com/errors-and-logs-overview/
* **Sign Up for a Trial:** http://www.stackify.com/sign-up/
* **Appenders**
    * **Log4j 1.2:** https://github.com/stackify/stackify-log-log4j12
    * **Log4j 2.x:** https://github.com/stackify/stackify-log-log4j2
    * **Logback:** https://github.com/stackify/stackify-log-logback

## Installation

Add it as a maven dependency:
```xml
<dependency>
    <groupId>com.stackify</groupId>
    <artifactId>stackify-api-java</artifactId>
    <version>4.0.0</version>
</dependency>
```

## Configuration and Usage for Direct Logger

You need a stackify-api.properties file on your classpath that defines the configuration required for the Log API:
```
stackify.apiKey=YOUR_API_KEY
stackify.application=YOUR_APPLICATION_NAME
stackify.environment=YOUR_ENVIRONMENT
```

### Masking

The Stackify logger has built-in data masking for credit cards and social security number values.

**Enable Masking:**

Add `stackify.log.mask.enabled=true` to `stackify-api.properties`.

**Customize Masking:**

The example below has the following customizations: 

1. Credit Card value masking is disabled (`stackify.log.mask.CREDITCARD=false`)
2. IP Address masking is enabled (`stackify.log.mask.IP=true`). Built in masks are `CREDITCARD`, `SSN` and `IP`.
3. Custom masking to remove vowels using a regex (`stackify.log.mask.custom.VOWELS=[aeiou]`)
 
```
stackify.log.mask.enabled=true
stackify.log.mask.CREDITCARD=false
stackify.log.mask.SSN=true
stackify.log.mask.IP=true
stackify.log.mask.custom.VOWELS=[aeiou]
``` 

Note: *If you are logging from a device that has the stackify-agent installed, the environment setting is optional. We will use the environment associated to your device in Stackify.*

Log a message to Stackify 
```java
Logger.queueMessage("info", "Test message");
```

Log an exception to Stackify 
```java
Logger.queueException("error", e);
```

Log a message and exception to Stackify 
```java
Logger.queueException("error", "Test message", e);
```

Be sure to shutdown the Direct Logger to flush this appender of any messages and shutdown the background thread:
```java
LogManager.shutdown();
```

### RUM Setup

Real User Monitoring (RUM) setup allows you to specify where the RUM JavaScript block is injected. 

It is recommended you provide a `stackify-api.properties` file in your application classpath with the following content: 

````
stackify.application=My Application Name
stackify.environment=My Environment Name
````

Your application code will need to call out to the Stackify `com.stackify.apm.Stackify.getRUMJavaScriptBlock()` method in the `<head>` section as detailed below: 

**JSP Example**
  
````
<html>
  <head> 
        <%= com.stackify.apm.Stackify.getRUMJavaScriptBlock() %>
        <script and link tags>
    </head>
    <body>
    ... 
    </body>
</html>
````

## Legacy Support 

For legacy support of **Java 1.6 and 1.7** use the following maven dependency: 
```
<dependency>
    <groupId>com.stackify</groupId>
    <artifactId>stackify-api-java</artifactId>
    <version>3.1.2</version>
</dependency>
```

## License

Copyright 2019 Stackify, LLC.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
