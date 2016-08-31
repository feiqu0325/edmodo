package com.example.feiqu.edmodo.network;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by feiqu on 8/29/16.
 */
public class CreatorModel {
    String id;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    Map<String, String> avatars;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSmallAvatar() {
        return avatars.get("small");
    }

    public String getLargeAvatar() {
        return avatars.get("large");
    }
}
