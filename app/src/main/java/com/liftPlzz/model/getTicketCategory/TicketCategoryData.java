package com.liftPlzz.model.getTicketCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketCategoryData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category")
    @Expose
    private String category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
