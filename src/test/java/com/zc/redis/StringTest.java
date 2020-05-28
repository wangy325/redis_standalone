package com.zc.redis;

import com.zc.redis.bean.MyObject;
import com.zc.redis.ops.Ops4String;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/5 / 15:12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class StringTest extends AbstractTest {

    @Test
    public void testIncr() {
        Ops4String ops4String = standaloneTemplate.ops4String();
        ops4String.set("hello", "docker");
        String v = null;
        try {
            v = ops4String.get("hello");
        } catch (Exception e) {
            log.error("error", e);
        }
        assertEquals("docke", v);
    }

    @Test
    public void testDel() {
        System.out.println(ops4String.del("tick"));
    }

    @Test
    public void testSet() {
        ops4String.set("o2", new MyObject("lily", 102), 600);
    }

    @Test
    public void get() {
        System.out.println(ops4String.get("o1", MyObject.class));
    }
}
