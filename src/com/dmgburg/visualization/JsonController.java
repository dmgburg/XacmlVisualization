package com.dmgburg.visualization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

@Controller
@EnableAutoConfiguration
public class JsonController {

    @RequestMapping("/")
    @ResponseBody
    String home() throws JsonProcessingException {
        Node root = new Node("Root", Desicion.ALLOW);

        root.addChild(new Node("Teddy bears").addChild(new Node("Mary and the Bear")));
        root.addChild(new Node("Star Wars"));
        root.addChild(new Node("Disney worls", Desicion.ALLOW));
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(root);
    }

    @RequestMapping("/html")
    @ResponseBody
    String html() throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get("E:\\dev\\alfa-visualizations\\resources\\index.html"));
        return new String(encoded, UTF_8);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(JsonController.class, args);
    }
}