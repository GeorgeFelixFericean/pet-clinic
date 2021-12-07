package com.example.petclinic.rest.util;

public enum ErrorReturnCode {
    BAD_REQUEST("40000", "Bad request"),
    NAME_MISSING("40001", "Name missing"),
    ADDRESS_MISSING("40002", "Address missing"),
    PHONE_MISSING("40003", "Phone missing"),
    INVALID_PHONE_NUMBER("40004", "Invalid phone number"),
    PET_TYPE_MISSING("40005", "Pet type missing"),
    DESCRIPTION_MISSING("40006", "Description missing"),
    COST_MISSING("40007", "Cost missing"),
    DATE_MISSING("40008", "Date missing"),
    OWNER_MISSING("40009", "Owner missing"),

    NOT_FOUND("40400", "Not found"),
    OWNER_NOT_FOUND("40401", "Owner not found"),
    PET_NOT_FOUND("40402", "Pet not found"),
    TREATMENT_NOT_FOUND("40403", "Treatment not found");



    private final String code;
    private final String message;

    ErrorReturnCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
