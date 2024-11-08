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
import science.aist.fhirauditeventtoxes.DateUtil;
import science.aist.gtf.transformation.renderer.TransformationRender;
import science.aist.xes.model.AttributeDateType;
import science.aist.xes.model.AttributeStringType;
import science.aist.xes.model.EventType;
import science.aist.xes.model.ObjectFactory;

import java.util.ArrayList;
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
public class EventRenderer implements TransformationRender<List<EventType>, List<EventType>, List<AuditEvent>, AuditEvent> {

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
        return mapProperties(createElement(), auditEvents, currentElement);
    }

    @Override
    public List<EventType> createElement() {
        return new ArrayList<>();
    }

    @Override
    public List<EventType> mapProperties(List<EventType> eventType, List<AuditEvent> auditEvents, AuditEvent currentElement) {
        if (currentElement.hasOccurredDateTimeType()) {
            EventType complete = createEvent(currentElement, currentElement.getOccurredDateTimeType().getValue(), "complete");
            eventType.add(complete);
        } else if(currentElement.hasOccurredPeriod()) {
            if (!(currentElement.getOccurredPeriod().hasStart() && currentElement.getOccurredPeriod().hasEnd())) {
                throw new IllegalStateException("Must have both OccurredPeriod start and end value set.");
            }
            EventType start = createEvent(currentElement, currentElement.getOccurredPeriod().getStart(), "start");
            eventType.add(start);
            EventType complete = createEvent(currentElement, currentElement.getOccurredPeriod().getEnd(), "complete");
            eventType.add(complete);
        } else {
            throw new IllegalStateException("AuditEvent must either have occurredPeriod or occurredDateTime value.");
        }

        return eventType;
    }

    public EventType createEvent(AuditEvent currentElement, Date date, String lifeCycleTransition) {
        EventType eventType = factory.createEventType();
        AttributeStringType conceptName = factory.createAttributeStringType();

        conceptName.setKey("concept:name");
        conceptName.setValue(conceptNameResolver.apply(currentElement));
        eventType.getStringOrDateOrInt().add(conceptName);

        AttributeDateType timestamp = factory.createAttributeDateType();
        timestamp.setKey("time:timestamp");
        timestamp.setValue(DateUtil.dateToGregorianCalendar(date));
        eventType.getStringOrDateOrInt().add(timestamp);

        AttributeStringType lifecycleTransition = factory.createAttributeStringType();
        lifecycleTransition.setKey("lifecycle:transition");
        lifecycleTransition.setValue(lifeCycleTransition);
        eventType.getStringOrDateOrInt().add(lifecycleTransition);

        return eventType;
    }

}
