package com.zc.redis;

import com.zc.redis.bean.MyObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/5 / 15:12
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ValueTest extends AbstractTest {

    @Test
    public void testIncr() {
        System.out.println(standaloneTemplate.ops4Value().increment("tick"));
    }

    @Test
    public void testDel() {
        System.out.println(ops4Value.del("tick"));
    }

    @Test
    public void testSet() {
        ops4Value.set("o2", new MyObject("lily", 102), 600);
    }

    @Test
    public void get() {
        System.out.println(ops4Value.get("o1", MyObject.class));
    }
}
