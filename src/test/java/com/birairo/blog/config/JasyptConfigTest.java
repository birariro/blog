package com.birairo.blog.config;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JasyptConfigTest {
    @Test
    void encoding() {

        StringEncryptor encryptor = new JasyptConfig("secretKey", "PBEWithMD5AndDES").stringEncryptor();
        String value = "test-secret-value";

        String encrypt = encryptor.encrypt(value);
        String decrypt = encryptor.decrypt(encrypt);

        System.out.println("encrypt = " + encrypt);
        System.out.println("decrypt = " + decrypt);
        assertThat(decrypt).isEqualTo(value);
    }

}