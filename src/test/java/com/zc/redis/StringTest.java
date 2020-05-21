package com.zc.redis;

import com.zc.redis.bean.MyObject;
import com.zc.redis.ops.Ops4String;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/5 / 15:12
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StringTest extends AbstractTest {

    @Test
    public void testIncr() {
//        System.out.println(standaloneTemplate.ops4String().get("hello"));
        Ops4String ops4String = standaloneTemplate.ops4String();
//        ops4String.set("hello", "docker");
        assertEquals("docker", ops4String.get("hello"));
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
