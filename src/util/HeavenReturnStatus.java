package util;

public class HeavenReturnStatus {
    private boolean successStatus;
    private String errorMsg;

    public HeavenReturnStatus(boolean successStatus) {
        String errorMsg;
        if (successStatus) {
            errorMsg = "Success";
        } else {
            errorMsg = "Generic Failure";
        }
        new HeavenReturnStatus(true, errorMsg);
    }

    public HeavenReturnStatus(boolean successStatus, String errorMsg) {
        this.successStatus = successStatus;
        this.errorMsg = errorMsg;
    }

    public boolean getSuccessStatus() {
        return successStatus;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}