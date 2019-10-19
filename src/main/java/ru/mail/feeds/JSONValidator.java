package ru.mail.feeds;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONValidator {

    private final JSONObject jsonSchema = new JSONObject(
            new JSONTokener(JSONValidator.class.getClassLoader().getResourceAsStream("feed_schema.json")));

    public boolean validateJSON(String json) {
        JSONObject jsonSubject = new JSONObject(
                new JSONTokener(json));
        Schema schema = SchemaLoader.load(jsonSchema);
        try {
            schema.validate(jsonSubject);
            return true;
        } catch (ValidationException e) {
            e.printStackTrace();
            return false;
        }
    }
}
