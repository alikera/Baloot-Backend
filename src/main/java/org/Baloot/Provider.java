package org.Baloot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Provider {
    private int id;
    private String name;
    private String registryDate;

    public Provider(@JsonProperty("id") int _id,
                    @JsonProperty("name") String _name,
                    @JsonProperty("registryDate") String _registryDate) {
        this.id = _id;
        this.name = _name;
        this.registryDate = _registryDate;
    }

    public void print() {
        System.out.println(this.id + " " + this.name + " " + this.registryDate);
    }
}
