package app.seven.flexisafses.resources;

import app.seven.flexisafses.models.exception.BadRequestException;
import app.seven.flexisafses.models.exception.NotFoundException;
import app.seven.flexisafses.models.pojo.Department;
import app.seven.flexisafses.services.department.DepartmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.main.lazy-initialization=true")
//@WebMvcTest(DepartmentResource.class)
@AutoConfigureMockMvc()
class DepartmentResourceTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Mock
    DepartmentService departmentService;

    @InjectMocks
    DepartmentResource resource;

    @Autowired
    private WebApplicationContext webApplicationContext;


    Department testDepartment;
    @BeforeEach
    public void setUp() {
        testDepartment = Department.builder()
                .id(UUID.randomUUID().toString())
                .name("Computer Science")
                .build();
//        mvc = MockMvcBuilders.standaloneSetup(new HandlerController()).build();
        mockMvc = MockMvcBuilders
                .standaloneSetup(resource).build();
//                .webAppContextSetup(webApplicationContext).build();
    }


    @WithMockUser(value = "spring")
    @Test
    void fetchDepartment() throws Exception {

        given(departmentService.fetchDepartments()).willReturn(Arrays.asList(testDepartment));

        RequestBuilder request = MockMvcRequestBuilders.get("/departments");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void shouldCreateDepartment() throws Exception {
        given(departmentService.getDepartmentByName(any())).willReturn(Optional.empty());
        given(departmentService.createDepartment(any())).willReturn(testDepartment);

        RequestBuilder request = MockMvcRequestBuilders.post("/departments")
                .accept(APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDepartment));
        mockMvc.perform(request).andExpect(status().isCreated()).andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Test
    void createDepartment() throws Exception {
        given(departmentService.getDepartmentByName(any())).willReturn(Optional.of(testDepartment));
        given(departmentService.createDepartment(any())).willReturn(testDepartment);

        RequestBuilder request = MockMvcRequestBuilders.post("/departments")
                .accept(APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDepartment));
        mockMvc.perform(request).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException));
    }

    @Test
    void updateDepartment() throws Exception {
        given(departmentService.getDepartmentById(testDepartment.getId())).willReturn(testDepartment);
        given(departmentService.updateDepartment(any())).willReturn(testDepartment);

        RequestBuilder request = MockMvcRequestBuilders.patch("/departments/" + testDepartment.getId())
                .accept(APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDepartment));
        mockMvc.perform(request).andExpect(status().isOk());
    }
}
