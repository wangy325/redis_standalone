package com.zc.redis;

import com.zc.redis.ops.Ops4Hash;
import com.zc.redis.ops.Ops4Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/6 / 17:29
 */
@Service
public class StandaloneTemplate {

    private final Ops4Hash ops4Hash;

    private final Ops4Value ops4Value;

    public StandaloneTemplate(Ops4Hash ops4Hash, Ops4Value ops4Value) {
        this.ops4Hash = ops4Hash;
        this.ops4Value = ops4Value;
    }

    public Ops4Hash ops4Hash() {
        return ops4Hash;
    }

    public Ops4Value ops4Value() {
        return ops4Value;
    }
}
