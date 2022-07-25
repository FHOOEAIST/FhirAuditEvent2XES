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
import science.aist.fhirauditeventtoxes.domain.AuditEventBundle;
import science.aist.gtf.transformation.Transformer;
import science.aist.gtf.transformation.renderer.TransformationRender;
import science.aist.xes.model.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Transformation between {@link AuditEvent}s and {@link LogType}</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
public class FhirAuditEventsToXESLogTransformer implements Transformer<AuditEventBundle, LogType> {

    private final TransformationRender<LogType, LogType, AuditEventBundle, Collection<AuditEvent>> logTypeTransformationRenderer;

    @Override
    public LogType applyTransformation(AuditEventBundle auditEventBundle) {
        return logTypeTransformationRenderer.renderElement(auditEventBundle, auditEventBundle.getAuditEvents());
    }
}
