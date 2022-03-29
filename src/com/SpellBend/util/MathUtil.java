package com.SpellBend.util;

public class MathUtil {
    public static final double DEGTORAD = Math.PI/180;
    public static final double RADTODEG = 180/Math.PI;

    public static double random(double min, double max) {
        return Math.random() * (max-min) + min;
    }

    public static boolean ALargerB(int[] a, int[] b) {
        try {
            for (int i = 0;i<a.length;i++) {
                if (a[i]>b[i]) return true;
                if (a[i]<b[i]) return false;
            }
            return false;
        } catch (IndexOutOfBoundsException exception) {
            return true;
        }
    }

    public static boolean ALargerB(long[] a, long[] b) {
        try {
            for (int i = 0;i<a.length;i++) {
                if (a[i]>b[i]) return true;
                if (a[i]<b[i]) return false;
            }
            return false;
        } catch (IndexOutOfBoundsException exception) {
            return true;
        }
    }

    public static boolean ASmallerB(int[] a, int[] b) {
        try {
            for (int i = 0;i<a.length;i++) {
                if (a[i]<b[i]) return true;
                if (a[i]>b[i]) return false;
            }
            return false;
        } catch (IndexOutOfBoundsException exception) {
            return false;
        }
    }

    public static boolean ASmallerB(long[] a, long[] b) {
        try {
            for (int i = 0;i<a.length;i++) {
                if (a[i]<b[i]) return true;
                if (a[i]>b[i]) return false;
            }
            return false;
        } catch (IndexOutOfBoundsException exception) {
            return false;
        }
    }
}
