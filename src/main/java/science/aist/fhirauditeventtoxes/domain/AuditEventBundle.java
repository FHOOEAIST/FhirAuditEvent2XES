/*
 * Copyright (c) 2022 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.fhirauditeventtoxes.domain;

import lombok.Data;
import org.hl7.fhir.r5.model.AuditEvent;

import java.util.Collection;

/**
 * <p>Domain class to wrap the audit events + the plan definition name</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@Data
public class AuditEventBundle {
    private final String planDefinition;
    private final Collection<AuditEvent> auditEvents;
}
