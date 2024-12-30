package com.dms.tareosoft.data.models;

import com.google.gson.annotations.SerializedName;

public class ResponseRegistroAcceso {

    @SerializedName("Success")
    public Boolean Success;

    @SerializedName("Message")
    public String Message;

    public Boolean getSuccess() {
        return Success;
    }

    public void setSuccess(Boolean success) {
        Success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public String toString() {
        return "ResponseRegistroAcceso{" +
                "Succes='" + Success + '\'' +
                ", Message='" + Message + '\'' +
                '}';
    }
}
