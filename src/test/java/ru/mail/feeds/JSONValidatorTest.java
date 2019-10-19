package ru.mail.feeds;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class JSONValidatorTest {

    private static final JSONValidator jsonValidator = new JSONValidator();
    private final ClassLoader classLoader = JSONValidatorTest.class.getClassLoader();

    @Test
    public void Test_Original_Data() throws URISyntaxException, IOException {
        URL resource = classLoader.getResource("feeds_show.json");
        List<String> jsons = Files.readAllLines(Paths.get(resource.toURI()));
        List<String> validateJsons = jsons.stream().filter(jsonValidator::validateJSON).collect(Collectors.toList());
        Assert.assertEquals(jsons.size(), validateJsons.size());
    }

    @Test
    public void Test_Without_Platform() throws Exception {
        URL resource = classLoader.getResource("without_platform.json");
        String json = Files.readString(Paths.get(resource.toURI()));
        boolean isValid = jsonValidator.validateJSON(json);
        Assert.assertFalse(isValid);
    }

    @Test
    public void Test_Wrong_Platform() throws Exception {
        URL resource = classLoader.getResource("wrong_platform.json");
        String json = Files.readString(Paths.get(resource.toURI()));
        boolean isValid = jsonValidator.validateJSON(json);
        Assert.assertFalse(isValid);
    }

    @Test
    public void Test_String_UserId() throws Exception {
        URL resource = classLoader.getResource("string_user_id.json");
        String json = Files.readString(Paths.get(resource.toURI()));
        boolean isValid = jsonValidator.validateJSON(json);
        Assert.assertFalse(isValid);
    }

    @Test
    public void Test_Negative_Timestamp() throws Exception {
        URL resource = classLoader.getResource("negative_timestamp.json");
        String json = Files.readString(Paths.get(resource.toURI()));
        boolean isValid = jsonValidator.validateJSON(json);
        Assert.assertFalse(isValid);
    }
}