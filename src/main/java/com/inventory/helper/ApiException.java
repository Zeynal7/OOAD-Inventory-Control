package com.inventory.helper;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;


public class ApiException extends Exception {
    public enum ExceptionCodes {
        UNKNOWN(0),
        SQL(1), // DB Connection Issues
        JAVA(2), // Java Exception - Division by 0, Null Pointer, etc.
        MISSING_FIELD(3),
        UPLOAD_PROBLEM(4),
        TOKEN_INIT_PROBLEM(5),
        USER_NOT_FOUND(6),
        NOT_AUTHORIZED(7),
        BLOCKED_USER(8),
        ITEM_HAS_ASSIGNED_EMPLOYEE(9),
        CANNOT_SET_AVAILABILITY_TO_NEGATIVE(10);

        private final int exceptionCode;

        ExceptionCodes(int exceptionCode) {
            this.exceptionCode = exceptionCode;
        }

        public int data() {
            return exceptionCode;
        }
    }

    @Schema(example = "1")
    private ExceptionCodes code;
    @Schema(example = "com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure")
    private String description;
    @Schema(example = "Communication Error")
    private String message;


    public ApiException(ExceptionCodes code, String description) {
        this.code = code;
        this.description = description;
        setMessage(getErrorMessage(code));
        // TODO: Log Exceptions before clearing stackTrace.
        // Stack trace is cleared to stop any information from leaking.
        this.setStackTrace(new StackTraceElement[0]);
    }

    public ApiException(ExceptionCodes code) {
        this.code = code;
        String descMessage = getErrorMessage(code);
        setMessage(descMessage);
        setDescription(descMessage);
        // TODO: Log Exceptions before clearing stackTrace.
        // Stack trace is cleared to stop any information from leaking.
        this.setStackTrace(new StackTraceElement[0]);
    }

    private String getErrorMessage(ExceptionCodes code) {
        String descMessage;
        switch (code) {
            case SQL:
                descMessage = "Communication Error";
                break;
            case JAVA:
                descMessage = "Unexpected Value / Missing Field";
                break;
            case MISSING_FIELD:
                descMessage = "Mandatory Field Error";
                break;
            case UPLOAD_PROBLEM:
                descMessage = "Upload Error";
                break;
            case TOKEN_INIT_PROBLEM:
                descMessage = ("Token Initialization Failed. Try Again");
                break;
            case USER_NOT_FOUND:
                descMessage = ("User Could not be found. Try to contact developers.");
                break;
            case NOT_AUTHORIZED:
                descMessage = "You are not authorized to use this function.";
                break;
            case BLOCKED_USER:
                descMessage = "You have been blocked from using our services." +
                        " If you think this is a mistake, please, contact via feedback.";
                break;
            case ITEM_HAS_ASSIGNED_EMPLOYEE:
                descMessage = "Item has assigned employee, when deleting make sure there is no employee with assigned item";
                break;
            case CANNOT_SET_AVAILABILITY_TO_NEGATIVE:
                descMessage = "Cannot set availability to negative number.";
                break;
            default:
                descMessage = "Unknown Error";
        }
        return descMessage;
    }

    public ExceptionCodes getCode() {
        return code;
    }

    public void setCode(ExceptionCodes code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public static Response checkException(Throwable e) {
        if (e instanceof SQLException) {
            return Response.serverError().entity(new ApiException(ExceptionCodes.SQL, e.toString())).build();
        } else if (e instanceof RuntimeException) {
            return Response.serverError().entity(new ApiException(ExceptionCodes.JAVA, e.toString())).build();
        } else if (e instanceof IOException) {
            return Response.serverError().entity(new ApiException(ExceptionCodes.UPLOAD_PROBLEM, e.toString())).build();
        } else if (e instanceof AssertionError) {
            return Response.serverError().entity(new ApiException(ExceptionCodes.MISSING_FIELD, e.toString())).build();
        } else if (e instanceof ApiException) {
            if(((ApiException) e).code == ExceptionCodes.NOT_AUTHORIZED){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            return Response.serverError().entity(e).build();
        } else {
            return Response.serverError().entity(new ApiException(ExceptionCodes.UNKNOWN, e.toString())).build();
        }
    }

    public static void checkNullEmpty(Object object) {
        if (object == null || object.toString().isEmpty() || object.toString().isBlank()) {
            throw new AssertionError("Field value is not correct.");
        }
    }
}


