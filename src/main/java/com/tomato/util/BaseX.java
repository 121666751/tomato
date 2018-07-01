/**
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomato.util;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * BaseX, extracted from Base58
 *
 * @see <a href=
 * "https://github.com/mikehearn/bitcoinj/blob/master/core/src/main/java/com/google/bitcoin/core/Base58.java">
 * com/google/bitcoin/core/Base58.java</a>
 * @see <a href="https://github.com/cryptocoinjs/base-x">cryptocoinjs/base-x</a>
 */
public final class BaseX {
    public static final Charset CHARSET_DEFAULT = Charset.forName("UTF-8");
    private final int base;
    private final char[] alphabet;
    private final int[] indexes;

    /**
     * @param alphabet
     */
    public BaseX(String alphabet) {
        this.base = alphabet.length();
        this.alphabet = alphabet.toCharArray();
        this.indexes = new int[128];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = -1;
        }
        for (int i = 0; i < this.alphabet.length; i++) {
            indexes[this.alphabet[i]] = i;
        }
    }

    /**
     * Encodes the given bytes in baseX. No checksum is appended.
     *
     * @param input
     *
     * @return
     */
    public String encode(byte[] input) {
        if (null == input) {
            return null;
        } else if (input.length == 0) {
            return "";
        }

        input = copyOfRange(input, 0, input.length);
        // Count leading zeroes.
        int zeroCount = 0;
        while (zeroCount < input.length && input[zeroCount] == 0) {
            ++zeroCount;
        }
        // The actual encoding.
        byte[] temp = new byte[input.length * 2];
        int j = temp.length;

        int startAt = zeroCount;
        while (startAt < input.length) {
            byte mod = divModX(input, startAt);
            if (input[startAt] == 0) {
                ++startAt;
            }
            temp[--j] = (byte) alphabet[mod];
        }

        // Strip extra '1' if there are some after decoding.
        while (j < temp.length && temp[j] == alphabet[0]) {
            ++j;
        }
        // Add as many leading '1' as there were leading zeros.
        while (--zeroCount >= 0) {
            temp[--j] = (byte) alphabet[0];
        }

        byte[] output = copyOfRange(temp, j, temp.length);
        return new String(output, CHARSET_DEFAULT);
    }

    /**
     * @param input
     *
     * @return
     */
    public byte[] decode(String input) {
        if (null == input) {
            return null;
        } else if (input.length() == 0) {
            return new byte[0];
        }

        byte[] inputX = new byte[input.length()];
        // Transform the String to a baseX byte sequence
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);

            int digitX = -1;
            if (c >= 0 && c < 128) {
                digitX = indexes[c];
            }
            if (digitX < 0) {
                throw new IllegalArgumentException("Illegal character " + c + " at " + i);
            }

            inputX[i] = (byte) digitX;
        }
        // Count leading zeroes
        int zeroCount = 0;
        while (zeroCount < inputX.length && inputX[zeroCount] == 0) {
            ++zeroCount;
        }
        // The encoding
        byte[] temp = new byte[input.length()];
        int j = temp.length;

        int startAt = zeroCount;
        while (startAt < inputX.length) {
            byte mod = divMod256(inputX, startAt);
            if (inputX[startAt] == 0) {
                ++startAt;
            }

            temp[--j] = mod;
        }
        // Do no add extra leading zeroes, move j to first non null byte.
        while (j < temp.length && temp[j] == 0) {
            ++j;
        }

        return copyOfRange(temp, j - zeroCount, temp.length);
    }

    /**
     * @param input
     *
     * @return
     */
    public BigInteger decodeToBigInteger(String input) {
        return new BigInteger(1, decode(input));
    }

    /**
     * Uses the checksum in the last 4 bytes of the decoded data to verify the rest are correct. The
     * checksum is removed from the returned data.
     *
     * @param input
     *
     * @return
     */
    public byte[] decodeChecked(String input) {
        byte tmp[] = decode(input);
        if (tmp.length < 4) {
            throw new IllegalArgumentException("Input too short");
        }
        byte[] bytes = copyOfRange(tmp, 0, tmp.length - 4);
        byte[] checksum = copyOfRange(tmp, tmp.length - 4, tmp.length);

        tmp = SHA256Util.doubleDigest(bytes);
        byte[] hash = copyOfRange(tmp, 0, 4);
        if (!Arrays.equals(checksum, hash)) {
            throw new IllegalArgumentException("Checksum does not validate");
        }
        return bytes;
    }

    /**
     * number -> number / X, returns number % X
     *
     * @param number
     * @param startAt
     *
     * @return
     */
    private byte divModX(byte[] number, int startAt) {
        int remainder = 0;
        for (int i = startAt; i < number.length; i++) {
            int digit256 = number[i] & 0xFF;
            int temp = remainder * 256 + digit256;

            number[i] = (byte) (temp / base);

            remainder = temp % base;
        }

        return (byte) remainder;
    }

    /**
     * number -> number / 256, returns number % 256
     *
     * @param numberX
     * @param startAt
     *
     * @return
     */
    private byte divMod256(byte[] numberX, int startAt) {
        int remainder = 0;
        for (int i = startAt; i < numberX.length; i++) {
            int digitX = numberX[i] & 0xFF;
            int temp = remainder * base + digitX;

            numberX[i] = (byte) (temp / 256);

            remainder = temp % 256;
        }

        return (byte) remainder;
    }

    /**
     * @param source
     * @param from
     * @param to
     *
     * @return
     */
    private byte[] copyOfRange(byte[] source, int from, int to) {
        byte[] range = new byte[to - from];
        System.arraycopy(source, from, range, 0, range.length);
        return range;
    }

}