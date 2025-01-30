package com.pogodin.flightbooking;

/**
 * represents a result of an operation
 */
public class OperationResult {
    private static final OperationResult SUCCESS_OPERATION = new OperationResult(true, "");
    private final boolean operationIsSuccess;
    private final String failureReason;

    private OperationResult(boolean operationIsSuccess, String failureReason) {
        this.operationIsSuccess = operationIsSuccess;
        this.failureReason = failureReason;
    }

    public static OperationResult success(){
        return SUCCESS_OPERATION;
    }

    public static OperationResult failure(String reason) {
        return new OperationResult(false, reason);
    }

    public boolean isSuccess() {
        return operationIsSuccess;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
