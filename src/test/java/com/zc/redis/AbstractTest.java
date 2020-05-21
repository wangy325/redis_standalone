package com.zc.redis;

import com.zc.redis.conf.StandAloneConfig;
import com.zc.redis.ops.Ops4Hash;
import com.zc.redis.ops.Ops4String;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/6 / 14:19
 */
@ContextConfiguration(classes = {StandAloneConfig.class})
public abstract class AbstractTest {
    @Autowired
    protected Ops4String ops4String;
    @Autowired
    protected Ops4Hash ops4Hash;
    @Autowired
    protected StandaloneTemplate standaloneTemplate;
}
