/*
 * Copyright (c) 2022 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.fhirauditeventtoxes;

import org.hl7.fhir.r5.model.AuditEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>Test class for {@link FhirAuditEventsToXESLogService}</p>
 *
 * @author Andreas Pointner
 */

public class FhirAuditEventsToXESLogServiceTest {

    @Test
    public void testConvertFhirAuditEventsToXESLog() {
        // given
        FhirAuditEventsToXESLogService service = new FhirAuditEventsToXESLogService(
                "getBasedOnFirstRep.getReference",
                "getCode.getCodingFirstRep.getDisplay",
                "getEncounter.getReference"
                );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // when
        service.convertFhirAuditEventsToXESLog(getClass().getResourceAsStream("/auditEventBundle.json"), outputStream);

        // then
        String res = outputStream.toString(StandardCharsets.UTF_8);
        Assert.assertNotNull(res);
        System.out.println(res);
    }
}