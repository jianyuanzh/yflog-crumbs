package com.yflog.test;

import java.util.HashMap;

/**
 * Created by Vincent on 1/22/16.
 */
public class TestReference {

    public void testChangeReference(HashMap<String,String> test){
        if (test == null)
            test = new HashMap<String, String>();

        test.put("test", "teststed");
    }


    public static void main(String[] args) {
        HashMap<String, String> test = null;
        TestReference ref = new TestReference();
        ref.testChangeReference(test);

        System.out.println(test);

        test = new HashMap<String, String>();
        ref.testChangeReference(test);
        System.out.println(test);
    }
}
