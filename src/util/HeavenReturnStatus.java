package util;

public class HeavenReturnStatus {
    private boolean successStatus;
    private String errorMsg;
    private HeavenConstants.Event event;

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

    public HeavenReturnStatus(boolean successStatus, HeavenConstants.Event event) {
        this.successStatus = successStatus;
        this.event = event;
    }

    public boolean getSuccessStatus() {
        return successStatus;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public HeavenConstants.Event getEvent() {
        return event;
    }
}