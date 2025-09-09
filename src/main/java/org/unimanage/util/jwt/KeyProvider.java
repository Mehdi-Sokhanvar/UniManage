package org.unimanage.util.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.util.Collections.replaceAll;

@Component
public class KeyProvider {

    private final PrivateKey privateKey;

    private final PublicKey publicKey;

    /* @Value("${jwt.private-key-path}") String privateKey,
     @Value("${jwt.public-key-path}") String publicKey*/
    public KeyProvider() {
        this.privateKey = loadPrivateKey("/keys/private.pem");
        this.publicKey = loadPublicKey("/keys/public.pem");
    }

    private PublicKey loadPublicKey( String fileName)  {
       try  {
           String key=readFileData(fileName);
           byte[] keyBytes = Base64.getDecoder().decode(key);
           X509EncodedKeySpec spec = new X509EncodedKeySpec (keyBytes);
           KeyFactory kf = KeyFactory.getInstance("RSA");
           return kf.generatePublic(spec);
       }catch (Exception e){
           throw new RuntimeException(e);
       }
    }

    private PrivateKey loadPrivateKey( String fileName)  {
        try{
            String key=readFileData(fileName);
            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private String readFileData(final String fileName) throws IOException {
       try(InputStream inputStream = getClass().getResourceAsStream(fileName)) {
           if (inputStream == null) {
               throw new FileNotFoundException(fileName);
           }
           return new  String(inputStream.readAllBytes())
                   .replaceAll("-----BEGIN (.*)-----","")
                   .replaceAll("-----END (.*)-----","")
                   .replaceAll("\r\n","");



       }catch (Exception e){
           throw new RuntimeException(e);
       }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
