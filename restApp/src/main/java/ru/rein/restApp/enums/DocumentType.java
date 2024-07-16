package ru.rein.restApp.enums;

public enum DocumentType {
    PASSPORT("PASSPORT"), INTERNATIONAL_PASSPORT("INTERNATIONAL_PASSPORT"), DRIVER("DRIVER");

    private final String documentType;

    DocumentType(String documentType) {
        this.documentType = documentType;
    }
}
