package org.gear.framework.core.design_patterns.exception;

public class SingletonDeclarationClassViolationException extends RuntimeException {

    public SingletonDeclarationClassViolationException(String message) {
        super(message);
    }
}
