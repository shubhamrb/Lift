package com.liftPlzz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResponseChatSuggestion {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("suggestions")
    @Expose
    private List<String> suggestions = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

}
