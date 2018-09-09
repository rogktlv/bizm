package com.seok.crw.bizm;

public class OutGoingCallNumber {

    private String callNumber;
    private OutGoingCallNumberStatus status;

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public OutGoingCallNumberStatus getStatus() {
        return status;
    }

    public void setStatus(OutGoingCallNumberStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OutGoingCallNumber{" +
                "callNumber='" + callNumber + '\'' +
                ", status=" + status +
                '}';
    }
}
