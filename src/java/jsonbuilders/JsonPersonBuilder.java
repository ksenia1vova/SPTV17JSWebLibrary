package jsonbuilders;

import entity.Person;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class JsonPersonBuilder {
     public JsonObject createJsonPersonObject(Person person) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id",person.getId())
                .add("firstname", person.getFirstname())
                .add("lastname", person.getLastname())
                .add("email", person.getEmail())
                .add("city", person.getCity())
                .add("street",person.getStreet())
                .add("house", person.getHouse())
                .add("room", person.getRoom());
        return job.build();
                
    }
    
}