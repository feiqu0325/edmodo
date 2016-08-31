package com.example.feiqu.edmodo.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by feiqu on 8/29/16.
 */
public class AssignmentModel {
    Integer id;
    String title;
    String description;

    @SerializedName("due_at")
    String dueAt;

    CreatorModel creator;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueAt() {
        return dueAt;
    }

    public CreatorModel getCreator() {
        return creator;
    }
}
