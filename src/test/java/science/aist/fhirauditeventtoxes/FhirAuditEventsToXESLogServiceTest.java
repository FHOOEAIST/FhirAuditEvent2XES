/*
 * Copyright (c) 2022 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.fhirauditeventtoxes;

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
                "getEncounter.getReference",
                "getCode.getCodingFirstRep.getDisplay"
        );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // when
        service.convertFhirAuditEventsToXESLog(getClass().getResourceAsStream("/auditEvents.json"), "PlanDefinition/1", outputStream);

        // then
        String res = outputStream.toString(StandardCharsets.UTF_8);
        Assert.assertNotNull(res);
        System.out.println(res);
    }

    @Test
    public void testConvertFhirAuditEventsToXESLog2() {
        // given
        FhirAuditEventsToXESLogService service = new FhirAuditEventsToXESLogService(
                "getEncounter.getReference",
                "getCode.getCodingFirstRep.getDisplay"
        );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // when
        service.convertFhirAuditEventsToXESLog(getClass().getResourceAsStream("/auditEvents2.json"), "PlanDefinition/1", outputStream);

        // then
        String res = outputStream.toString(StandardCharsets.UTF_8);
        Assert.assertNotNull(res);
        System.out.println(res);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testConvertFhirAuditEventsToXESLog3() {
        // given
        FhirAuditEventsToXESLogService service = new FhirAuditEventsToXESLogService(
                "getEncounter.getReference",
                "getCode.getCodingFirstRep.getDisplay"
        );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // when
        service.convertFhirAuditEventsToXESLog(getClass().getResourceAsStream("/auditEvents3.json"), "PlanDefinition/1", outputStream);

        // then
        // expected exception no end at period set
    }
}