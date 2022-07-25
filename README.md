# Fhir Audit Event to XES

This project contains methods to convert a set of audit events into a XES representation.

## Getting Started

To use the project, simply include the maven dependency on the project.

```xml
<dependency>
    <groupId>science.aist</groupId>
    <artifactId>fhir-audit-event-to-xes</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope> <!-- Note: this is default -->
</dependency>
```

This then enables you to access our service class, to convert from AuditEvents to XES Logs.

```java
// Create a new Fhir service
var service = new FhirAuditEventsToXESLogService();
// create some outputstream where to write the results
var outputStream = new ByteArrayOutputStream();
// get the input stream to some auditEvent bundle:
var inputStream = ...
        
// Execute the service method
service.convertFhirAuditEventsToXESLog(inputStream, outputStream);

// (Optional - depending on choosen output stream): Get the data out of the stream
String res = outputStream.toString(StandardCharsets.UTF_8);
```

If you already have the AuditEvents as java classes you can also create the log out of these.

```java
var service = new FhirAuditEventsToXESLogService();
Collection<AuditEvent> auditEvents = ...

var log = service.convertFhirAuditEventsToXESLog(auditEvents);

// (Optional): Write log to some output stream:
var outputStream = ...
new LogRepository().save(new ObjectFactory.createLog(log), outputStream);
```


## FAQ

If you have any questions, please check out our [FAQ](https://fhooeaist.github.io/FhirAuditEvent2XES/faq.html) section.

## Contributing

**First make sure to read our [general contribution guidelines](https://fhooeaist.github.io/CONTRIBUTING.html).**
   
## License

Copyright (c) 2022 the original author or authors.
DO NOT ALTER OR REMOVE COPYRIGHT NOTICES.

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at https://mozilla.org/MPL/2.0/.

## Research

If you are going to use this project as part of a research paper, we would ask you to reference this project by citing
it. 

[![DOI](https://zenodo.org/badge/507932096.svg)](https://zenodo.org/badge/latestdoi/507932096)
