package com.tomato.util;

import java.math.BigInteger;

public final class Base62 {
    private static final BaseX base62 = new BaseX("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

    /**
     * Encodes the given bytes in baseX. No checksum is appended.
     *
     * @param input
     *
     * @return
     */
    public static String encode(byte[] input) {
        return base62.encode(input);
    }

    /**
     * @param input
     *
     * @return
     */
    public static byte[] decode(String input) {
        return base62.decode(input);
    }

    /**
     * @param input
     *
     * @return
     */
    public static BigInteger decodeToBigInteger(String input) {
        return base62.decodeToBigInteger(input);
    }

    /**
     * Uses the checksum in the last 4 bytes of the decoded data to verify the rest are correct. The
     * checksum is removed from the returned data.
     *
     * @param input
     *
     * @return
     */
    public static byte[] decodeChecked(String input) {
        return base62.decodeChecked(input);
    }

}
