package com.resultant.test;

import com.resultant.task.TemplateApplication;
import com.resultant.task.config.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TemplateApplication.class, SecurityConfig.class})
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureRestDocs(outputDir = "target/snippets", uriPort = 5550)
public class TestApp {

    @Autowired
    private MockMvc mockMvc;

    @Value("${template.date-format}")
    private String dateFormat;

    @Test
    public void getCurrency() throws Exception {
        this.mockMvc.perform(get("/api/v1/data/currency").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(document("currency", responseFields(
                        fieldWithPath("[].VEngname").description("Название (English)"),
                        fieldWithPath("[].VnumCode").description("Unknown code").optional(),
                        fieldWithPath("[].VcommonCode").description("Unknown code"),
                        fieldWithPath("[].Vcode").description("Unknown code"),
                        fieldWithPath("[].Vname").description("Название (Russian)"),
                        fieldWithPath("[].Vnom").description("размер корзины"),
                        fieldWithPath("[].VcharCode").description("Символьный код").optional()
                )));
    }


    @Test
    public void getCourses() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("date", "02.06.2018");
        params.add("currencies", "USD");
        params.add("currencies", "AUD");

        this.mockMvc.perform(get("/api/v1/data/courses").contentType(MediaType.APPLICATION_JSON).params(params)  ).andExpect(status().isOk())
                .andDo(document("courses", requestParameters(
                        parameterWithName("date").description("Date - format(dd.MM.yyyy)"),
                        parameterWithName("currencies").description("currencies []")
                ), responseFields(
                        fieldWithPath("[].day").description("Date - format(dd.MM.yyyy)"),
                        fieldWithPath("[].curs").description("курс"),
                        fieldWithPath("[].code").description("код"),
                        fieldWithPath("[].chCode").description("Символьный код"),
                        fieldWithPath("[].name").description("Название"),
                        fieldWithPath("[].nom").description("размер корзины")
                )));
    }

    @Test
    public void getDynamicCourses() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("from", "01.05.2018");
        params.add("to", "01.06.2018");
        params.add("currencies", "R01235");
        params.add("currencies", "R01235");

        this.mockMvc.perform(get("/api/v1/data/courses/dynamic").contentType(MediaType.APPLICATION_JSON).params(params)  ).andExpect(status().isOk())
                .andDo(document("courses-dynamic", requestParameters(
                        parameterWithName("from").description("Date from - format(dd.MM.yyyy)"),
                        parameterWithName("to").description("Date to - format(dd.MM.yyyy)"),
                        parameterWithName("currencies").description("currencies code []")
                ), responseFields(
                        fieldWithPath("[].CursDate").description("Date"),
                        fieldWithPath("[].Vcurs").description("курс"),
                        fieldWithPath("[].Vcode").description("код"),
                        fieldWithPath("[].Vnom").description("размер корзины")
                )));
    }

    @Test
    public void autorize() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "admin@resultant.com");
        params.add("password", "123456");

        this.mockMvc.perform(post("/api/v1/login").contentType(MediaType.APPLICATION_JSON).params(params)  ).andExpect(status().isOk())
                .andDo(document("login", requestParameters(
                        parameterWithName("username").description("username(email)"),
                        parameterWithName("password").description("password")
                )));
    }

    @Test
    @WithMockUser(username = "admin@resultant.com", password = "123456",roles={"ADMIN"})
    public void getListUsersByAdmin() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "admin@resultant.com");
        params.add("password", "123456");

        MvcResult result = this.mockMvc.perform(post("/api/v1/login").contentType(MediaType.APPLICATION_JSON).params(params)  ).andExpect(status().isOk())
                .andReturn();

        this.mockMvc.perform(get("/api/v1/main/list").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(document("list-user", responseFields(
                        fieldWithPath("[].id").description("id"),
                        fieldWithPath("[].username").description("username(email)"),
                        fieldWithPath("[].accountNonExpired").description("account non expired"),
                        fieldWithPath("[].accountNonLocked").description("account non locked"),
                        fieldWithPath("[].credentialsNonExpired").description("credentials non expired"),
                        fieldWithPath("[].enabled").description("enabled"),
                        fieldWithPath("[].roles[]").description("user roles"),
                        fieldWithPath("[].admin").description("mark admin"),
                        fieldWithPath("[].roles[].id").description("role id"),
                        fieldWithPath("[].roles[].name").description("role name"),
                        fieldWithPath("[].replacePass").description("replace password")
                )));

    }

    @Test
    @WithMockUser(username = "user@resultant.com", password = "123456",roles={"USER"})
    public void getListUsersByUser() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "user@resultant.com");
        params.add("password", "123456");

        MvcResult result = this.mockMvc.perform(post("/api/v1/login").contentType(MediaType.APPLICATION_JSON).params(params)  ).andExpect(status().isOk())
                .andReturn();

        this.mockMvc.perform(get("/api/v1/main/list").contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());

    }

}

