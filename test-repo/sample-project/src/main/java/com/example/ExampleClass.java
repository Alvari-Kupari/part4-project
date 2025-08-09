package com.example;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.StringWriter;
import java.io.IOException;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ExampleClass {
    
    public void useStringUtils() {
        // Using Apache Commons Lang
        String text = StringUtils.capitalize("hello world");
        boolean isEmpty = StringUtils.isEmpty(text);
        String reversed = StringUtils.reverse(text);
        
        // Using WordUtils from the text package that was moved in version 3.6
        String wrapped = WordUtils.wrap(text, 20);
        String capitalized = WordUtils.capitalizeFully(text);
    }
    
    public void useJackson() {
        try {
            JsonFactory factory = new JsonFactory();
            StringWriter writer = new StringWriter();
            JsonGenerator generator = factory.createGenerator(writer);
            
            ObjectMapper mapper = new ObjectMapper();
            
            generator.writeStartObject();
            generator.writeStringField("name", "test");
            generator.writeEndObject();
            generator.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void useBreakingChanges() {
        try {
            // Direct variable declarations to ensure JavaParser detects these classes as used
            StringArrayDeserializer stringArrayDeserializer;
            StringCollectionDeserializer stringCollectionDeserializer;
            JacksonInject.Value jacksonInjectValue;
            
            // Create instances to trigger symbol detection
            stringArrayDeserializer = new StringArrayDeserializer();
            stringCollectionDeserializer = new StringCollectionDeserializer(List.class, null, null);
            jacksonInjectValue = new JacksonInject.Value();
            
            System.out.println("Created StringArrayDeserializer: " + stringArrayDeserializer.getClass().getName());
            System.out.println("Created StringCollectionDeserializer: " + stringCollectionDeserializer.getClass().getName());
            System.out.println("Created JacksonInject.Value: " + jacksonInjectValue.getClass().getName());
            
            // Reference the classes directly to ensure they're detected
            Class<?> jacksonInjectClass = JacksonInject.class;
            Class<?> jsonAutoDetectClass = JsonAutoDetect.class;
            System.out.println("Loaded JacksonInject class: " + jacksonInjectClass.getName());
            System.out.println("Loaded JsonAutoDetect class: " + jsonAutoDetectClass.getName());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        ExampleClass example = new ExampleClass();
        example.useStringUtils();
        example.useJackson();
        example.useBreakingChanges();
    }
}
