package lol.cicco.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TransactionTest {
    private MockMvc mockMvc;

    @Autowired
    public void setWebApplicationContext(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    @After
    public void flush() throws Exception {
        // flush db
        mockMvc.perform(delete("/flushdb")).andExpect(status().isOk());
    }

    @Test
    public void testNonTx() throws Exception {
        // save and rollback test1
        mockMvc.perform(post("/saveForNonTx")).andExpect(status().isOk());

        // can't rollback
        mockMvc.perform(get("/count1")).andExpect(status().isOk()).andExpect(content().string("1"));
        mockMvc.perform(get("/count2")).andExpect(status().isOk()).andExpect(content().string("1"));
    }

    @Test
    public void testDefaultTx() throws Exception {
        // save and rollback test1
        mockMvc.perform(post("/saveTestForSpringTx")).andExpect(status().isOk());

        // can't rollback test2
        mockMvc.perform(get("/count1")).andExpect(status().isOk()).andExpect(content().string("0"));
        mockMvc.perform(get("/count2")).andExpect(status().isOk()).andExpect(content().string("1"));
    }


    @Test
    public void testMoreTx() throws Exception {
        mockMvc.perform(post("/saveTestForMoreTx")).andExpect(status().isOk());

        mockMvc.perform(get("/count1")).andExpect(status().isOk()).andExpect(content().string("0"));
        mockMvc.perform(get("/count2")).andExpect(status().isOk()).andExpect(content().string("0"));
    }


    @Test
    public void testNextTx() throws Exception {
        mockMvc.perform(post("/saveTestForNext")).andExpect(status().isOk());

        mockMvc.perform(get("/count1")).andExpect(status().isOk()).andExpect(content().string("0"));
        mockMvc.perform(get("/count2")).andExpect(status().isOk()).andExpect(content().string("0"));
    }

    @Test
    public void testNextTxWithNewTx() throws Exception {
        mockMvc.perform(post("/saveTestForNextWithNewTx")).andExpect(status().isOk());

        mockMvc.perform(get("/count1")).andExpect(status().isOk()).andExpect(content().string("1"));
        mockMvc.perform(get("/count2")).andExpect(status().isOk()).andExpect(content().string("1"));
    }

    @Test
    public void saveTestForOtherException() throws Exception {
        mockMvc.perform(post("/saveTestForOtherException")).andExpect(status().isOk()); // not rollback

        mockMvc.perform(get("/count1")).andExpect(status().isOk()).andExpect(content().string("1"));
        mockMvc.perform(get("/count2")).andExpect(status().isOk()).andExpect(content().string("1"));
    }

    @Test
    public void saveTestForOtherExceptionAndRollback() throws Exception {
        mockMvc.perform(post("/saveTestForOtherExceptionAndRollback")).andExpect(status().isOk()); // rollback

        mockMvc.perform(get("/count1")).andExpect(status().isOk()).andExpect(content().string("0"));
        mockMvc.perform(get("/count2")).andExpect(status().isOk()).andExpect(content().string("0"));
    }

}
