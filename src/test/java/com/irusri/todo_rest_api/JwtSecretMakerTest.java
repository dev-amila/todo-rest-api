package com.irusri.todo_rest_api;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JwtSecretMakerTest {

    @Test
    public void generateSecretKey(){
        SecretKey key =Jwts.SIG.HS256.key().build();
        String endCodedKey = DatatypeConverter.printHexBinary(key.getEncoded());
        System.out.println(endCodedKey);
    }
}
