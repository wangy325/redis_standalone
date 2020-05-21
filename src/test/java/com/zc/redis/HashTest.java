package com.zc.redis;

import com.zc.redis.bean.MyObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/6 / 14:18
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class HashTest extends AbstractTest {

    @Test
    public void testSet() {
        ops4Hash.hset("mh", "k1", new MyObject("lilei", 103));
        ops4Hash.hset("mh", "k2", new MyObject("zhangli", 104), 6000);
    }

    @Test
    public void testSetAll() {
        Map map = new HashMap<>();
        MyObject o1 = new MyObject("lucy", 109);
        MyObject o2 = new MyObject("anna", 110);
        String o3 = "{\"name\":\"cris\",\"code\":111}";
        map.put("hk1", o1);
        map.put("hk2", o2);
        map.put("hk3", o3);
        ops4Hash.hsetAll("mh", map);
    }

    @Test
    public void testGet() {
        System.out.println(ops4Hash.hget("mh", "hk1", MyObject.class));
        System.out.println(ops4Hash.hget("mh", "hk3", String.class));
    }


    @Test
    public void testHasKey() {
        System.out.println(ops4Hash.hasKey("mh", "k9"));
    }

    @Test
    public void getHash() {
        Map<Object, Object> mh = ops4Hash.getHash("mh");
    }

    @Test
    public void testHdel() {
        System.out.println(ops4Hash.hdel("mh", "k1"));
    }

    @Test
    public void testMdel() {
        List list = new ArrayList();
        list.add("k2");
        list.add("hk1");
        System.out.println(ops4Hash.mdel("mh", list));
    }
}

