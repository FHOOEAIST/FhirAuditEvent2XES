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
import science.aist.xes.model.AttributeDateType;
import science.aist.xes.model.AttributeStringType;
import science.aist.xes.model.EventType;
import science.aist.xes.model.ObjectFactory;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * <p>Create an {@link EventType} out of an {@link AuditEvent}</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
public class EventRenderer implements TransformationRender<List<EventType>, EventType, List<AuditEvent>, AuditEvent> {

    /**
     * XML object factory to create the element
     */
    private final ObjectFactory factory;

    /**
     * The resolver method for the basedOnReference to convert it into a string representation
     */
    private final Function<AuditEvent, String> conceptNameResolver;

    @Override
    public List<EventType> renderElement(List<AuditEvent> auditEvents, AuditEvent currentElement) {
        EventType start = mapProperties(createElement(), auditEvents, currentElement);
        addTimestamp(start, currentElement, ae -> ae.getOccurredPeriod().getStart());
        addLifecycleTransition(start, currentElement, ae -> "start");

        EventType end = mapProperties(createElement(), auditEvents, currentElement);
        addTimestamp(end, currentElement, ae -> ae.getOccurredPeriod().getEnd());
        addLifecycleTransition(end, currentElement, ae -> "complete");

        return List.of(start, end);
    }

    @Override
    public EventType createElement() {
        return factory.createEventType();
    }

    @Override
    public EventType mapProperties(EventType eventType, List<AuditEvent> auditEvents, AuditEvent currentElement) {
        AttributeStringType conceptName = factory.createAttributeStringType();

        conceptName.setKey("concept:name");
        conceptName.setValue(conceptNameResolver.apply(currentElement));
        eventType.getStringOrDateOrInt().add(conceptName);

        return eventType;
    }

    private void addTimestamp(EventType eventType, AuditEvent currentElement, Function<AuditEvent, Date> timestampSelector) {
        AttributeDateType timestamp = factory.createAttributeDateType();
        timestamp.setKey("time:timestamp");
        timestamp.setValue(DateUtil.dateToGregorianCalendar(timestampSelector.apply(currentElement)));
        eventType.getStringOrDateOrInt().add(timestamp);
    }

    private void addLifecycleTransition(EventType eventType, AuditEvent currentElement, Function<AuditEvent, String> lifecycleTransitionSelector) {
        AttributeStringType lifecycleTransition = factory.createAttributeStringType();
        lifecycleTransition.setKey("lifecycle:transition");
        lifecycleTransition.setValue(lifecycleTransitionSelector.apply(currentElement));
        eventType.getStringOrDateOrInt().add(lifecycleTransition);
    }
}
