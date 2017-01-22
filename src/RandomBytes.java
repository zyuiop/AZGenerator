import java.security.SecureRandom;

/**
 * @author zyuiop
 */
public class RandomBytes {

	private static boolean isValid(byte[] var1) {
		if(var1.length != 24) {
			return false;
		} else {
			boolean var2 = true;
			for(int var3 = 0; var3 < var1.length - 7; ++var3) {
				byte var4 = var1[var3];
				int var5 = 0;

				for(int var6 = 0; var6 < var1.length; ++var6) {
					if(var4 == var1[var6]) {
						++var5;
						if(var5 >= 8) {
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
