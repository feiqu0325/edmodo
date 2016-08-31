package com.example.feiqu.edmodo.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by feiqu on 8/29/16.
 */
public class SubmissionModel {
    String content;

    @SerializedName("assignment_id")
    Integer assignmentId;

    @SerializedName("submitted_at")
    String submittedAt;

    CreatorModel creator;

    public String getContent() {
        return content;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public CreatorModel getCreator() {
        return creator;
    }
}
