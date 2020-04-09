/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonbuilders;

import entity.Book;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class JsonBookBuilder {
    public JsonObject createJsonObject(Book book){
       JsonObjectBuilder job = Json.createObjectBuilder();
          job.add("id",book.getId())
            .add("caption",book.getCaption())
            .add("author",book.getAuthor())
            .add("date",book.getDate().toString())
            .add("text",book.getText())
            .add("publishedYear", book.getPublishedYear())
            .add("cover", book.getCover());
            return job.build();
                    
    }
}
