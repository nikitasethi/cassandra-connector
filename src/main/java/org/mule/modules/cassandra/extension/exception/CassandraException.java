/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the
 * Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */


package org.mule.modules.cassandra.extension.exception;


public class CassandraException extends Exception {

    /**
     * Constructs a new exception with null as its detail message.
     */
    public CassandraException() {
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CassandraException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param msg   the detail message.
     * @param cause the cause.
     */
    public CassandraException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message.
     *
     * @param cause the cause
     */
    public CassandraException(Throwable cause) {
        super(cause);
    }
}
