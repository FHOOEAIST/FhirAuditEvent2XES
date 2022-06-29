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

import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Date;
import java.time.Month;

import static org.testng.Assert.*;

/**
 * <p>Test class for {@link DateUtil}</p>
 *
 * @author Andreas Pointner
 */

public class DateUtilTest {

    @Test
    public void testDateToGregorianCalendar() {
        // given
        Date d = Date.valueOf("2022-06-28");

        // when
        XMLGregorianCalendar xmlGregorianCalendar = DateUtil.dateToGregorianCalendar(d);

        // then
        Assert.assertEquals(xmlGregorianCalendar.getYear(), 2022);
        Assert.assertEquals(xmlGregorianCalendar.getMonth(), Month.JUNE.getValue());
        Assert.assertEquals(xmlGregorianCalendar.getDay(), 28);
    }
}