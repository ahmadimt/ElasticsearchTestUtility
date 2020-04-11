package com.imti.exception;

/**
 * @author imteyaz.ahmad
 */
public class InvalidDataException extends RuntimeException {

  public InvalidDataException(final Exception ioe) {
    super(ioe.getMessage(), ioe);
  }
}
