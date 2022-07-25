/*
 * Copyright (c) 2022 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.fhirauditeventtoxes.renderer;

import lombok.AllArgsConstructor;
import org.hl7.fhir.r5.model.AuditEvent;
import science.aist.fhirauditeventtoxes.domain.AuditEventBundle;
import science.aist.gtf.transformation.renderer.TransformationRender;
import science.aist.xes.model.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Creates and {@link LogType} out of an {@link AuditEventBundle}</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
public class LogRenderer implements TransformationRender<LogType, LogType, AuditEventBundle, Collection<AuditEvent>> {

    private final ObjectFactory factory;
    private final TransformationRender<TraceType, TraceType, List<AuditEvent>, List<AuditEvent>> traceRenderer;
    private final Function<AuditEvent, ?> groupingSelector;


    @Override
    public LogType renderElement(AuditEventBundle auditEventBundle, Collection<AuditEvent> currentElement) {
        return mapProperties(createElement(), auditEventBundle, currentElement);
    }

    @Override
    public LogType createElement() {
        return factory.createLogType();
    }

    @Override
    public LogType mapProperties(LogType logType, AuditEventBundle auditEventBundle, Collection<AuditEvent> currentElement) {
        logType.setXesVersion(BigDecimal.valueOf(1.0));

        // create extensions types
        Stream.of(timeExtension(), lifecycleExtension(), conceptExtension()).forEach(logType.getExtension()::add);
        AttributeStringType conceptName = factory.createAttributeStringType();
        conceptName.setKey("concept:name");
        conceptName.setValue(auditEventBundle.getPlanDefinition());
        logType.getStringOrDateOrInt().add(conceptName);

        // create traces
        currentElement.stream().collect(Collectors.groupingBy(groupingSelector)).forEach(
                (ignore, events) -> logType.getTrace().add(traceRenderer.renderElement(events, events))
        );
        return logType;
    }

    private ExtensionType timeExtension() {
        return createExtension(
                "Time",
                "time",
                "http://www.xes-standard.org/time.xesext"
        );
    }

    private ExtensionType lifecycleExtension() {
        return createExtension(
                "Lifecycle",
                "lifecycle",
                "http://www.xes-standard.org/lifecycle.xesext"
        );
    }

    private ExtensionType conceptExtension() {
        return createExtension(
                "Concept",
                "concept",
                "http://www.xes-standard.org/concept.xesext"
        );
    }

    private ExtensionType createExtension(String name, String prefix, String uri) {
        ExtensionType conceptExtension = factory.createExtensionType();
        conceptExtension.setName(name);
        conceptExtension.setPrefix(prefix);
        conceptExtension.setUri(uri);
        return conceptExtension;
    }

}
