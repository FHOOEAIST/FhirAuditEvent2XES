/*
 * Copyright (c) 2022 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.fhirauditeventtoxes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>Util class to convert between date formats</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {
    /**
     * Create a {@link XMLGregorianCalendar} element with the same timestamp as {@link Date}.
     *
     * @param date the data that should be converted
     * @return the resulting {@link XMLGregorianCalendar} element
     */
    @SneakyThrows
    public static XMLGregorianCalendar dateToGregorianCalendar(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    }
}
