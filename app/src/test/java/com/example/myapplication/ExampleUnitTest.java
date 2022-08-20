package com.example.myapplication;

import static com.example.myapplication.util.CryptionUtils.Decrypt;
import static com.example.myapplication.util.CryptionUtils.Encrypt;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        String cKey = "jkl;POIU1234++==";
        // 需要加密的字串
        String cSrc = "www.gowhere.sosdgsh";
        System.out.println(cSrc);


        // 加密
        String enString = Encrypt(cSrc, cKey);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = Decrypt(enString, cKey);
        System.out.println("解密后的字串是：" + DeString);

        assertEquals(4, 2 + 2);
    }
}