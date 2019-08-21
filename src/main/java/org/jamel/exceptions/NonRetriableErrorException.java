package org.jamel.exceptions;

import java.util.Arrays;

import com.yandex.ydb.core.Status;

/**
 * @author Sergey Polovko
 */
public class NonRetriableErrorException extends RuntimeException {
    public NonRetriableErrorException(Status status) {
        super("code: " + status.getCode() + ", issues: " + Arrays.toString(status.getIssues()));
    }
}
