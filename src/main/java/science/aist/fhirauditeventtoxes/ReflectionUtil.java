/*
 * Copyright (c) 2022 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.fhirauditeventtoxes;

import lombok.SneakyThrows;
import science.aist.jack.exception.ExceptionUtils;
import science.aist.jack.general.util.CastUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * <p>Utility class to help with reflection.</p>
 *
 * @author Andreas Pointner
 */
public class ReflectionUtil {
    private ReflectionUtil() {

    }

    @SneakyThrows
    public static <S, T> Function<S, T> createFunctionChain(Class<?> entrypoint, String path) {
        String[] split = path.split("\\.");

        List<Method> methodChain = new ArrayList<>();
        for (String s : split) {
            Method method = entrypoint.getMethod(s);
            methodChain.add(method);
            entrypoint = method.getReturnType();
        }

        return ExceptionUtils.uncheck(auditEvent -> {
            Object prev = auditEvent;
            for (Method method : methodChain) {
                prev = method.invoke(prev);
            }
            return CastUtils.cast(prev);
        });
    }
}
