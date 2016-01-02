package com.github.microserviceworkshop.books;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by marco.poli on 31.08.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BooksApplication.class)
@WebAppConfiguration
public class HelloWorldServiceTest {
    private MediaType contentType = new MediaType(MediaType.TEXT_PLAIN.getType(),
            MediaType.TEXT_PLAIN.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void readSingleBookmark() throws Exception {
        mockMvc.perform(get("/echo?name=mytest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().string("Hello mytest"));
    }
}
