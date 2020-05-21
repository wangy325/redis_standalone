package com.zc.redis;

import com.zc.redis.encrypt.AesUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/21 / 16:26
 */
public class EncryptUtil {

    public static void main(String[] args) throws Exception {
        showEncrypt();
    }

    static void showEncrypt() throws Exception {
        String encrypt = AesUtil.encrypt("123456");
        System.out.println(encrypt);
    }

}
