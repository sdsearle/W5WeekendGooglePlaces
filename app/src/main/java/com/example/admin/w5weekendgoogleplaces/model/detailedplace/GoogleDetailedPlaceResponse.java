
package com.example.admin.w5weekendgoogleplaces.model.detailedplace;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoogleDetailedPlaceResponse {

    @SerializedName("debug_log")
    @Expose
    private DebugLog debugLog;
    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("status")
    @Expose
    private String status;

    public DebugLog getDebugLog() {
        return debugLog;
    }

    public void setDebugLog(DebugLog debugLog) {
        this.debugLog = debugLog;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
