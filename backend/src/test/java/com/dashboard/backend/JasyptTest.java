package com.dashboard.backend;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
public class JasyptTest {
    @Test 
    void encryptTest(){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("get-dashboard-pw-in-2026");
        encryptor.setAlgorithm("PBEWithMD5AndDES");


        
    }
}
