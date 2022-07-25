/*
 * Copyright (c) 2022 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.fhirauditeventtoxes;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import lombok.SneakyThrows;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.Bundle;
import science.aist.fhirauditeventtoxes.domain.AuditEventBundle;
import science.aist.fhirauditeventtoxes.renderer.EventRenderer;
import science.aist.fhirauditeventtoxes.renderer.LogRenderer;
import science.aist.fhirauditeventtoxes.renderer.TraceRenderer;
import science.aist.gtf.transformation.Transformer;
import science.aist.xes.model.LogType;
import science.aist.xes.model.ObjectFactory;
import science.aist.xes.model.XMLRepository;
import science.aist.xes.model.impl.LogRepository;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>Service which composes the transformers and renderer and allows to create the resulting xes log</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
public class FhirAuditEventsToXESLogService {

    private final ObjectFactory factory;
    private final Transformer<AuditEventBundle, LogType> transformer;


    /**
     * Initializes the transformers and renderers
     */
    public FhirAuditEventsToXESLogService() {
        this(
                ae -> ae.getEncounter().getReference(),
                ae -> ae.getCode().getCodingFirstRep().getDisplay()
        );
    }

    @SneakyThrows
    public FhirAuditEventsToXESLogService(String traceConceptNameResolverPath, String eventConceptNameResolverPath) {
        this(
                ReflectionUtil.createFunctionChain(AuditEvent.class, traceConceptNameResolverPath),
                ReflectionUtil.createFunctionChain(AuditEvent.class, eventConceptNameResolverPath)
        );
    }

    public FhirAuditEventsToXESLogService(Function<AuditEvent, String> traceConceptNameResolver, Function<AuditEvent, String> eventConceptNameResolver) {
        factory = new ObjectFactory();
        var eventRenderer = new EventRenderer(factory, eventConceptNameResolver);
        var traceRenderer = new TraceRenderer(factory, eventRenderer, traceConceptNameResolver);
        var logRenderer = new LogRenderer(factory, traceRenderer, traceConceptNameResolver);
        transformer = new FhirAuditEventsToXESLogTransformer(logRenderer);
    }

    /**
     * Use the transformer to transform from a {@link Collection} of {@link AuditEvent}s into a {@link LogType}
     *
     * @param bundle the collection of audit events
     * @return the resulting xes log
     */
    public LogType convertFhirAuditEventsToXESLog(AuditEventBundle bundle) {
        return transformer.applyTransformation(bundle);
    }

    /**
     * Loads an audit bundle from an input stream and output the resulting xes log into a given output stream
     *
     * @param auditBundleInputStream an input stream to a bundle of audit logs
     * @param logOutputStream        and output stream where the resulting log should be written to
     */
    public void convertFhirAuditEventsToXESLog(InputStream auditBundleInputStream, String planDefinition, OutputStream logOutputStream) {
        // Create fhir context and parse
        FhirContext ctx = FhirContext.forR5();
        IParser parser = ctx.newJsonParser();

        // read the bundle from the input stream
        Bundle auditEventBundle = parser.parseResource(Bundle.class, auditBundleInputStream);
        // convert the bundle resources into audit events
        Collection<AuditEvent> auditEvents = auditEventBundle.getEntry().stream().map(Bundle.BundleEntryComponent::getResource).map(AuditEvent.class::cast).collect(Collectors.toList());

        // convert the audit events into a log
        LogType logType = convertFhirAuditEventsToXESLog(new AuditEventBundle(planDefinition, auditEvents));

        // write the xes log on the output stream
        XMLRepository<LogType> repository = new LogRepository();
        repository.save(factory.createLog(logType), logOutputStream);
    }
}
