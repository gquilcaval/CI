package com.dms.tareosoft.data.entities;

import com.google.gson.annotations.SerializedName;

public class MsgResponse {

    @SerializedName("Message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
