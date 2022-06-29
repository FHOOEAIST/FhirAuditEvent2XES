/*
 * Copyright (c) 2022 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.fhirauditeventtoxes;

import lombok.AllArgsConstructor;
import org.hl7.fhir.r5.model.AuditEvent;
import science.aist.gtf.transformation.renderer.TransformationRender;
import science.aist.xes.model.AttributeStringType;
import science.aist.xes.model.EventType;
import science.aist.xes.model.ObjectFactory;
import science.aist.xes.model.TraceType;

import java.util.List;
import java.util.function.Function;

/**
 * <p>Creates a {@link TraceType} out of a list of {@link AuditEvent}s.</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
public class TraceRenderer implements TransformationRender<TraceType, TraceType, List<AuditEvent>, List<AuditEvent>> {

    private final ObjectFactory factory;
    private final TransformationRender<EventType, EventType, List<AuditEvent>, AuditEvent> eventRenderer;
    private final Function<AuditEvent, String> conceptNameResolver;

    @Override
    public TraceType renderElement(List<AuditEvent> auditEvents, List<AuditEvent> currentElement) {
        return mapProperties(createElement(), auditEvents, currentElement);
    }

    @Override
    public TraceType createElement() {
        return factory.createTraceType();
    }

    @Override
    public TraceType mapProperties(TraceType traceType, List<AuditEvent> auditEvents, List<AuditEvent> currentElement) {
        if (currentElement.isEmpty()) return traceType;

        // Uses the first audit event to extract the process id.
        // Note: It is expected, that the audit events are correctly grouped before and are all assign to the same process.
        AttributeStringType attributeStringType = factory.createAttributeStringType();
        attributeStringType.setKey("concept:name");
        attributeStringType.setValue(conceptNameResolver.apply(currentElement.get(0)));
        traceType.getStringOrDateOrInt().add(attributeStringType);

        // Create the events
        currentElement.stream().map(auditEvent -> eventRenderer.renderElement(currentElement, auditEvent)).forEach(traceType.getEvent()::add);

        return traceType;
    }
}
