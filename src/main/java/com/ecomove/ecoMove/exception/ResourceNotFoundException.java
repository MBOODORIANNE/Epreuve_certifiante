package com.ecomove.ecoMove.exception;

/**
 * Exception levée lorsqu'une ressource demandée est introuvable.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("La ressource '%s' avec %s = '%s' est introuvable", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() { return resourceName; }
    public String getFieldName() { return fieldName; }
    public Object getFieldValue() { return fieldValue; }
}
