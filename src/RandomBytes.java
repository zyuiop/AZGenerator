import java.security.SecureRandom;

/**
 * @author zyuiop
 */
public class RandomBytes {

	/*
	Valid hash : max 8 identical bytes
	 */
	private static boolean isValid(byte[] array) {
		if(array.length != 24) {
			return false;
		} else {
			for(int i = 0; i < array.length - 7; ++i) {
				byte b = array[i];
				int identicalBytes = 0;

				for(int j = 0; j < array.length; ++j) {
					if(b == array[j]) {
						++identicalBytes;
						if(identicalBytes >= 8) {
							return false;
						}
					}
				}
			}

			return true;
		}
	}

	public static byte[] generateValidCode() {
		byte[] var14 = new byte[24];

		do {
			(new SecureRandom()).nextBytes(var14);
		} while(!isValid(var14));

		return var14;
	}
}
