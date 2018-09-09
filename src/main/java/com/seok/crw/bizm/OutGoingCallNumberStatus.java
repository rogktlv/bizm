package com.seok.crw.bizm;

import java.util.Arrays;

public enum OutGoingCallNumberStatus {

    WAIT("심사 대기중",0),
    REJECT("반려",1),
    APPROVE("승인", 2),
    UNKNOWN("알 수 없음", -1);


    private String description;
    private int code;

    OutGoingCallNumberStatus(String description, int code){
        this.description = description;
        this.code = code;
    }


    public static OutGoingCallNumberStatus findByDescription(String description){
        return Arrays.stream(OutGoingCallNumberStatus.values())
                .filter(status -> status.getDescription().equals(description))
                .findAny()
                .orElse(UNKNOWN);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
