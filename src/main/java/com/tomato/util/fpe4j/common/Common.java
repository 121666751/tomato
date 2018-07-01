/**
 * Format-Preserving Encryption
 *
 * Copyright (c) 2016 Weydstone LLC dba Sutton Abinger
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Sutton Abinger licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tomato.util.fpe4j.common;

import java.math.BigInteger;
import java.util.Arrays;
import com.tomato.util.fpe4j.Constants;

/**
 * The type Common.
 */
public class Common {

    /**
     * Instantiates a new Common.
     */
    public Common() {
        throw new RuntimeException("The Common class cannot be instantiated.");
    }

    /**
     * Num big integer.
     *
     * @param X
     *         the x
     * @param radix
     *         the radix
     *
     * @return the big integer
     */
    public static BigInteger num(int[] X, int radix) {
        // validate X
        if (X == null) {
            throw new NullPointerException("X must not be null.");
        }
        if (X.length < 1 || X.length > Constants.MAXLEN) {
            throw new IllegalArgumentException("The length of X is not within the permitted range of 1" + ".." + Constants.MAXLEN + ": " + X.length);
        }
        // validate radix
        if (radix < Constants.MINRADIX || radix > Constants.MAXRADIX) {
            throw new IllegalArgumentException("Radix not within the permitted range of " + Constants.MINRADIX + ".." + Constants.MAXRADIX + ": " + radix);
        }

        // 1. Let x = 0.
        BigInteger x = BigInteger.ZERO;

        // type conversion for readability
        BigInteger r = BigInteger.valueOf(radix);

        // 2. For i from 1 to LEN(X)
        for (int i = 0; i < X.length; i++) {
            // check the value of X[i]
            if (X[i] < 0 || X[i] >= radix) {
                throw new IllegalArgumentException("X[" + i + "] is not within the range of values defined by the radix (0.." + radix + ")");
            }

            // let x = x * radix + X[i]
            x = x.multiply(r).add(BigInteger.valueOf(X[i]));
        }

        // 3. Return x.
        return x;
    }

    /**
     * Num big integer.
     *
     * @param X
     *         the x
     *
     * @return the big integer
     */
    public static BigInteger num(byte[] X) {
        // validate X
        if (X == null) {
            throw new NullPointerException("X must not be null.");
        }
        if (X.length < 1 || X.length > Constants.MAXLEN) {
            throw new IllegalArgumentException("The length of X is not within the permitted range of 1.." + Constants.MAXLEN + ": " + X.length);
        }

        // 1. Let x = 0.
        BigInteger x = BigInteger.ZERO;

        // set value of radix for readability
        BigInteger r = BigInteger.valueOf(256);

        // 2. For i from 1 to LEN(X)
        for (int i = 0; i < X.length; i++) {
            // let x = 2x + X[i]
            x = x.multiply(r).add(BigInteger.valueOf(X[i] & 0xFF));
            /*
             * Note that the implementation is different than NIST SP 800-38G
             * because we're valuing bytes rather than individual bits
             */
        }

        /*
         * Instead of implementing the algorithm described in NIST SP 800-38G,
         * we could use the native conversion in the BigInteger class.
         *
         * BigInteger x = new BigInteger(1, X)
         *
         * However, we've kept the implementation as described in NIST SP
         * 800-38G for readability.
         */

        // 3. Return x.
        return x;
    }

    /**
     * Str int [ ].
     *
     * @param x
     *         the x
     * @param radix
     *         the radix
     * @param m
     *         the m
     *
     * @return the int [ ]
     */
    public static int[] str(BigInteger x, int radix, int m) {
        // validate m
        if (m < 1 || m > Constants.MAXLEN) {
            throw new IllegalArgumentException("M is not within the permitted range of 1" + ".." + Constants.MAXLEN + ": " + m);
        }

        // validate radix
        if (radix < Constants.MINRADIX || radix > Constants.MAXRADIX) {
            throw new IllegalArgumentException("Radix not within the permitted range of " + Constants.MINRADIX + ".." + Constants.MAXRADIX + ": " + radix);
        }

        // type conversion for readability
        BigInteger r = BigInteger.valueOf(radix);

        // validate x
        if (x == null) {
            throw new NullPointerException("x must not be null");
        }
        if (x.compareTo(BigInteger.ZERO) < 0 || x.compareTo(r.pow(m)) >= 0) {
            throw new IllegalArgumentException("X is not within the permitted range of 0.." + r.pow(m) + ": " + x);
        }

        // allocate result array
        int[] X = new int[m];

        // 1. For i from 1 to m:
        for (int i = 1; i <= m; i++) {

            // i. X[m+1-i] = x mod radix;
            X[m - i] = x.mod(r).intValue();

            // ii. x = floor(x/radix).
            x = x.divide(r);
            /*
             * BigInteger.divide() rounds down, so we don't need to apply the
             * floor function
             */
        }

        // 2. Return X.
        return X;
    }

    /**
     * Rev int [ ].
     *
     * @param X
     *         the x
     *
     * @return the int [ ]
     */
    public static int[] rev(int[] X) {
        // validate x
        if (X == null) {
            throw new NullPointerException("X must not be null");
        }

        int[] Y = new int[X.length];

        // 1. For i from 1 to LEN(X)
        for (int i = 0; i < X.length; i++) {

            // let Y[i] = X[LEN(X)+1-i]
            Y[i] = X[X.length - i - 1];
            /*
             * Note that NIST SP 800-38G assumes array indexes starting at 1
             * instead of array indexes starting at 0.
             */
        }

        // 2. Return Y[1..LEN(X)].
        return Y;
    }

    /**
     * Revb byte [ ].
     *
     * @param X
     *         the x
     *
     * @return the byte [ ]
     */
    public static byte[] revb(byte[] X) {
        // validate x
        if (X == null) {
            throw new NullPointerException("X must not be null");
        }

        byte[] Y = new byte[X.length];

        // 1. For i from 0 to BYTELEN(X)-1 and j from 1 to 8,
        for (int i = 0; i < X.length; i++) {
            // let Y[8i+j] = * X[8 * (BYTELEN(X)-1-i)+j].
            Y[i] = X[X.length - i - 1];
            /*
             * Note that the implementation is different than NIST SP 800-38G
             * because we're copying bytes rather than individual bits
             */
        }

        // 2. Return Y[1..8 * BYTELEN(X)].
        return Y;
    }

    /**
     * Xor byte [ ].
     *
     * @param X
     *         the x
     * @param Y
     *         the y
     *
     * @return the byte [ ]
     */
    public static byte[] xor(byte[] X, byte[] Y) {
        // validate X
        if (X == null) {
            throw new NullPointerException("X must not be null");
        }
        if (X.length < 1 || X.length > Constants.MAXLEN) {
            throw new IllegalArgumentException("The length of X is not within the permitted range of 1.." + Constants.MAXLEN + ": " + X.length);
        }

        // validate Y
        if (Y == null) {
            throw new NullPointerException("Y must not be null");
        }
        if (Y.length < 1 || Y.length > Constants.MAXLEN) {
            throw new IllegalArgumentException("The length of Y is not within the permitted range of 1.." + Constants.MAXLEN + ": " + Y.length);
        }
        if (Y.length != X.length) {
            throw new IllegalArgumentException("X and Y must be the same length. X: " + X.length + " Y: " + Y.length);
        }

        // allocate result array
        byte[] Z = new byte[X.length];

        // xor bytes
        for (int i = 0; i < X.length; i++) {
            Z[i] = (byte) (X[i] ^ Y[i]);
        }

        return Z;
    }

    /**
     * Log 2 double.
     *
     * @param x
     *         the x
     *
     * @return the double
     */
    public static double log2(int x) {
        // validate x
        if (x <= 0) {
            throw new IllegalArgumentException("x must be a positive integer");
        }

        return Math.log(x) / Math.log(2);
    }

    /**
     * Floor int.
     *
     * @param x
     *         the x
     *
     * @return the int
     */
    public static int floor(double x) {
        return (int) Math.floor(x);
    }

    /**
     * Floor int.
     *
     * @param x
     *         the x
     *
     * @return the int
     */
    public static int floor(int x) {
        throw new IllegalArgumentException("x must be a double");
    }

    /**
     * Ceiling int.
     *
     * @param x
     *         the x
     *
     * @return the int
     */
    public static int ceiling(double x) {
        return (int) Math.ceil(x);
    }

    /**
     * Ceiling int.
     *
     * @param x
     *         the x
     *
     * @return the int
     */
    public static int ceiling(int x) {
        throw new IllegalArgumentException("x must be a double");
    }

    /**
     * Mod int.
     *
     * @param x
     *         the x
     * @param m
     *         the m
     *
     * @return the int
     */
    public static int mod(int x, int m) {
        // validate m
        if (m < 1) {
            throw new ArithmeticException("m must be a positive integer");
        }

        // x - m * floor(x / m);
        return x - m * floor(x / (double) m);
    }

    /**
     * Mod big integer.
     *
     * @param x
     *         the x
     * @param m
     *         the m
     *
     * @return the big integer
     */
    public static BigInteger mod(BigInteger x, BigInteger m) {
        // validate m
        if (m.signum() != 1) {
            throw new ArithmeticException("m must be a positive integer");
        }

        // return x - m * floor(x / m);
        /*
         * return x.subtract(m.multiply(new BigDecimal(x).divide(new
         * BigDecimal(m), RoundingMode.FLOOR).toBigInteger()));
         *
         * This literal implementation of the pseudocode from NIST SP 800-38G is
         * provided only for comparison to the BigInteger.mod() method.
         */

        return x.mod(m);
    }

    /**
     * Bytestring byte [ ].
     *
     * @param x
     *         the x
     * @param s
     *         the s
     *
     * @return the byte [ ]
     */
    public static byte[] bytestring(int x, int s) {
        // validate s
        if (s < 0 || s > Constants.MAXLEN) {
            throw new IllegalArgumentException("s is not within the permitted range of 0.." + Constants.MAXLEN + ": " + s);
        }

        // validate x
        if (x < 0) {
            throw new IllegalArgumentException("x must be nonnegative");
        }
        if (x >= Math.pow(256, s)) {
            throw new IllegalArgumentException("x must be less than 256^s (" + x + " >= " + Math.pow(256, s) + ")");
        }

        byte[] string = new byte[s];

        // traverse s in reverse order, but stop if x is zero
        for (int i = s - 1; i >= 0 && x > 0; i--) {

            // copy the least significant byte of x
            string[i] = (byte) (x & 0xFF);

            // shift x to get the next byte
            x >>>= 8;
        }

        return string;
    }

    /**
     * Bytestring byte [ ].
     *
     * @param x
     *         the x
     * @param s
     *         the s
     *
     * @return the byte [ ]
     */
    public static byte[] bytestring(BigInteger x, int s) {
        // validate s
        if (s < 1) {
            throw new IllegalArgumentException("s must be a positive integer");
        }

        // validate x
        if (x.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("x must be nonnegative");
        }
        if (x.compareTo(BigInteger.valueOf(256).pow(s)) >= 0) {
            throw new IllegalArgumentException("x must be less than 256^s (" + x + " >= " + Math.pow(256, s) + ")");
        }

        byte[] string = new byte[s];

        // convert x to an array of bytes
        byte[] xBytes = x.toByteArray();

        // copy the bytes to the rightmost portion of the result
        System.arraycopy(xBytes, Math.max(xBytes.length - s, 0), string, Math.max(s - xBytes.length, 0), Math.min(xBytes.length, s));

        return string;
    }

    /**
     * Bitstring byte [ ].
     *
     * @param bit
     *         the bit
     * @param s
     *         the s
     *
     * @return the byte [ ]
     *
     * @throws IllegalArgumentException
     *         the illegal argument exception
     */
    public static byte[] bitstring(boolean bit, int s) throws IllegalArgumentException {
        // validate s
        if (s < 1) {
            throw new IllegalArgumentException("s must be a positive integer");
        }
        if (s % 8 != 0) {
            throw new IllegalArgumentException("s must be a multiple of 8: " + s);
        }

        byte[] string = new byte[s / 8];

        Arrays.fill(string, bit ? (byte) 0xFF : (byte) 0x00);

        return string;
    }

    /**
     * Concatenate int [ ].
     *
     * @param X
     *         the x
     * @param Y
     *         the y
     *
     * @return the int [ ]
     */
    public static int[] concatenate(int[] X, int[] Y) {
        // validate X
        if (X == null) {
            throw new NullPointerException("X must not be null");
        }

        // validate Y
        if (Y == null) {
            throw new NullPointerException("Y must not be null");
        }

        int[] Z = new int[X.length + Y.length];

        System.arraycopy(X, 0, Z, 0, X.length);
        System.arraycopy(Y, 0, Z, X.length, Y.length);

        return Z;
    }

    /**
     * Concatenate byte [ ].
     *
     * @param X
     *         the x
     * @param Y
     *         the y
     *
     * @return the byte [ ]
     */
    public static byte[] concatenate(byte[] X, byte[] Y) {
        // validate X
        if (X == null) {
            throw new NullPointerException("X must not be null");
        }

        // validate Y
        if (Y == null) {
            throw new NullPointerException("Y must not be null");
        }

        byte[] Z = new byte[X.length + Y.length];

        System.arraycopy(X, 0, Z, 0, X.length);
        System.arraycopy(Y, 0, Z, X.length, Y.length);

        return Z;
    }

    /**
     * Int array to string string.
     *
     * @param X
     *         the x
     * @param len
     *         the len
     *
     * @return the string
     */
    public static String intArrayToString(int[] X, int len) {
        // validate X
        if (X == null) {
            throw new NullPointerException("X must not be null");
        }
        if (len > X.length) {
            throw new ArrayIndexOutOfBoundsException(len);
        } else if (len <= 0) {
            len = X.length;
        }

        StringBuilder builder = new StringBuilder(len * 4);
        for (int i = 0; i < len; ++i) {
            builder.append(X[i]);
            builder.append(" ");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    /**
     * Int array to string string.
     *
     * @param X
     *         the x
     *
     * @return the string
     */
    public static String intArrayToString(int[] X) {
        return intArrayToString(X, 0);
    }

    /**
     * Unsigned byte array to string string.
     *
     * @param X
     *         the x
     *
     * @return the string
     */
    public static String unsignedByteArrayToString(byte[] X) {
        // validate X
        if (X == null) {
            throw new NullPointerException("X must not be null");
        }

        StringBuilder builder = new StringBuilder(X.length * 3);
        builder.append("[ ");
        for (byte b : X) {
            builder.append(b & 0xFF);
            builder.append(", ");
        }
        if (X.length > 0) {
            builder.replace(builder.length() - 2, builder.length(), " ]");
        } else {
            builder.append("]");
        }
        return builder.toString();
    }

    private static final String HEX_STRINGS[] = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10", "11",
            "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "2A", "2B", "2C", "2D", "2E", "2F", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F", "40", "41",
            "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
            "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71",
            "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
            "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F", "A0", "A1",
            "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9",
            "BA", "BB", "BC", "BD", "BE", "BF", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1",
            "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9",
            "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

    /**
     * Byte array to hex string string.
     *
     * @param X
     *         the x
     *
     * @return the string
     */
    public static String byteArrayToHexString(byte[] X) {
        // validate X
        if (X == null) {
            throw new NullPointerException("X must not be null");
        }

        StringBuilder builder = new StringBuilder(X.length * 2);
        for (byte b : X) {
            builder.append(HEX_STRINGS[b & 0xFF]);
        }
        return builder.toString();
    }

    /**
     * Floor div int.
     *
     * @param x
     *         the x
     * @param y
     *         the y
     *
     * @return the int
     */
    public static int floorDiv(int x, int y) {
        int r = x / y;
        // if the signs are different and modulo not zero, round down
        if ((x ^ y) < 0 && (r * y != x)) {
            r--;
        }
        return r;
    }

    /**
     * Floor div long.
     *
     * @param x
     *         the x
     * @param y
     *         the y
     *
     * @return the long
     */
    public static long floorDiv(long x, long y) {
        long r = x / y;
        // if the signs are different and modulo not zero, round down
        if ((x ^ y) < 0 && (r * y != x)) {
            r--;
        }
        return r;
    }

    /**
     * Floor mod int.
     *
     * @param x
     *         the x
     * @param y
     *         the y
     *
     * @return the int
     */
    public static int floorMod(int x, int y) {
        int r = x - floorDiv(x, y) * y;
        return r;
    }

    /**
     * Floor mod long.
     *
     * @param x
     *         the x
     * @param y
     *         the y
     *
     * @return the long
     */
    public static long floorMod(long x, long y) {
        return x - floorDiv(x, y) * y;
    }

}
