package com.silencedut.diffadapterdemo;


import android.util.Log;

/**
 *
 */
public class MethodStackTrace {

    private MethodStackTrace() {

    }

    public static void printMethodStack(String TAG, String msg) {

        Log.d("MethodStackTrace", msg+"stack"+ getStackMsg(Thread.currentThread().getStackTrace()));
    }

    private static String getStackMsg(StackTraceElement[] stackArray) {
        StringBuilder sb = new StringBuilder();
        for (int i = 3; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }

}
