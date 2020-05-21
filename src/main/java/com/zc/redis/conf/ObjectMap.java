package com.zc.redis.conf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/5 / 13:45
 */
@Component
@Deprecated
public class ObjectMap extends ObjectMapper {

        private static final long serialVersionUID = 0L;

        public ObjectMap() {
                this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                this.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                this.enableDefaultTyping(DefaultTyping.NON_FINAL);
                // default true
                this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
                this.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,true);
        }
}
