package com.sasa.project.grad2.Firebase_DataObjects;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class Organization_DataObjects {



    public String name,url, organization_email,description,type;
// fil el organization data le firebase
    public Organization_DataObjects(String name, String type, String description, String organization_email, String url) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.organization_email = organization_email;
        this.url = url;
    }

    @Exclude
    public Map<String, Object> OrganizationObjects() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("name", name);
        result.put("description", description);
        result.put("url", url);
        result.put("organization_email", organization_email);
        return result;
    }

    public Organization_DataObjects() { }
}
