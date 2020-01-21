package com.kanefron5.lab4;

import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kanefron5.lab4.database.UserServiceImpl;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

class CodingUtils {


    static String encode(String password) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update((password + Constants.SOULT).getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        StringBuilder hashtext = new StringBuilder(bigInt.toString(16));
        while (hashtext.length() < 32) {
            hashtext.insert(0, "0");
        }
        return hashtext.toString();
    }


    static String tokenGenerator(String mail) {
        Gson gson = new Gson();
        JsonObject object = new JsonObject();
        object.addProperty("mail", mail);
        object.addProperty("date", System.currentTimeMillis() + Constants.SESSION_LIVE_SECONDS * 1000);
        byte[] token = encodeDES3(gson.toJson(object));

        return new String(token);
    }


    static String getTokenOwner(String token, UserServiceImpl userService) {
        try {
            String s = decodeDES3(token.getBytes());
            Gson gson = new Gson();
            JsonObject object = gson.fromJson(s, JsonObject.class);

            long date = object.get("date").getAsLong();

            if ((date - System.currentTimeMillis()) > 0) {
                userService.findByEmailuser(object.get("mail").getAsString());
                return object.get("mail").getAsString();
            } else return "false";

        } catch (Exception e) {
            return "false";
        }
    }



    private static byte[] encodeDES3(String message) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] digestOfPassword = md.digest(Constants.KEY.getBytes(StandardCharsets.UTF_8));
            final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            for (int j = 0, k = 16; j < 8; ) {
                keyBytes[k++] = keyBytes[j++];
            }

            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            final byte[] plainTextBytes = message.getBytes(StandardCharsets.UTF_8);
            final byte[] cipherText = cipher.doFinal(plainTextBytes);

            return Base64.encode(cipherText, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private static String decodeDES3(byte[] message) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] digestOfPassword = md.digest(Constants.KEY.getBytes(StandardCharsets.UTF_8));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8; ) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, key, iv);

        final byte[] plainText = decipher.doFinal(Base64.decode(message, Base64.NO_WRAP));

        return new String(plainText, StandardCharsets.UTF_8);
    }
}
