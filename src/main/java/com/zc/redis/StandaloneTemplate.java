package com.zc.redis;

import com.zc.redis.ops.Ops4Hash;
import com.zc.redis.ops.Ops4String;
import org.springframework.stereotype.Service;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/6 / 17:29
 */
@Service
public class StandaloneTemplate {

    private final Ops4Hash ops4Hash;

    private final Ops4String ops4String;

    public StandaloneTemplate(Ops4Hash ops4Hash, Ops4String ops4String) {
        this.ops4Hash = ops4Hash;
        this.ops4String = ops4String;
    }

    public Ops4Hash ops4Hash() {
        return ops4Hash;
    }

    public Ops4String ops4String() {
        return ops4String;
    }
}
