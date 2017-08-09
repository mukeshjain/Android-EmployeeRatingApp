package droid.ninja.darwinboxdemo.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mukesh on 8/8/17.
 */

public class Employee {

    @SerializedName("id")
    public long id;

    @SerializedName("name")
    public String name;

    @SerializedName("age")
    public int age;

    @SerializedName("designation")
    public String desg;

    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("updatedAt")
    public String updatedAt;

    public Employee(long id, String name, int age, String desg, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.desg = desg;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
}
