/**
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomato.util;

import java.math.BigInteger;

/**
 * <p>
 * Base58 is a way to encode Bitcoin addresses as numbers and letters. Note that this is not the
 * same base58 as used by Flickr, which you may see reference to around the internet.
 * </p>
 *
 * <p>
 * You may instead wish to work with {@link VersionedChecksummedBytes}, which adds support for
 * testing the prefix and suffix bytes commonly found in addresses.
 * </p>
 *
 * <p>
 * Satoshi says: why base-58 instead of standard base-64 encoding?
 * <p>
 *
 * <ul>
 * <li>Don't want 0OIl characters that look the same in some fonts and could be used to create
 * visually identical looking account numbers.</li>
 * <li>A string with non-alphanumeric characters is not as easily accepted as an account number.
 * </li>
 * <li>E-mail usually won't line-break if there's no punctuation to break at.</li>
 * <li>Doubleclicking selects the whole number as one word if it's all alphanumeric.</li>
 * </ul>
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Base58">https://en.wikipedia.org/wiki/Base58</a>
 */
public final class Base58 {
	private static final BaseX base58 = new BaseX("123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz");

	/**
	 * Encodes the given bytes in baseX. No checksum is appended.
	 * 
	 * @param input
	 * @return
	 */
	public static String encode(byte[] input) {
		return base58.encode(input);
	}

	/**
	 * @param input
	 * @return
	 */
	public static byte[] decode(String input) {
		return base58.decode(input);
	}

	/**
	 * @param input
	 * @return
	 */
	public static BigInteger decodeToBigInteger(String input) {
		return base58.decodeToBigInteger(input);
	}

	/**
	 * Uses the checksum in the last 4 bytes of the decoded data to verify the rest are correct. The
	 * checksum is removed from the returned data.
	 *
	 * @param input
	 * @return
	 */
	public static byte[] decodeChecked(String input) {
		return base58.decodeChecked(input);
	}

}
