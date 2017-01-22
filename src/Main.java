import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * @author zyuiop
 */

// http://www.azlauncher.nz/files/launcher/version.md5
public class Main {
	private static Random random = new Random();

	private static String encodeKeyInIp(String name, String ip, byte[] key, String md5) {
		CodeGenerator generator = new CodeGenerator(md5, name);
		generator.setKey(key);
		generator.write();
		generator.writeCRC();
		String base64string = generator.getBase64();
		System.err.println("Base64 full data : " + base64string);

		StringBuilder sb = new StringBuilder();

		// Final string structure :
		// 1 random char
		// 60 first base64 characters
		// 1 random cher
		// 1 dot
		// 1 random char
		// all remaining base64 characters (< 60)

		// purpose ? unknown.

		// To experiment :
		// check if funcraft accepts :
		// - base64 strings with wrong jar hash (like a random one) [likely]
		// - base64 with a wrong "random byte token" 				[more likely]
		// - base64 with wrong CRC									[unlikely]

		// Example base64 data for user 'opgzerkfpoze'
		// base64 string :
		// eNmRe6fjcQyKETb--9jQKAF5rbIMb3BnemVya2Zwb3plG4URfkexpP3Q6ed_YsXioN3lsol1Z4y6AAAAAGwd1c8
		// "classic" base64 :
		// eNmRe6fjcQyKETb++9jQKAF5rbIMb3BnemVya2Zwb3plG4URfkexpP3Q6ed/YsXioN3lsol1Z4y6AAAAAGwd1c8
		// generated ip :
		// JeNmRe6fjcQyKETb--9jQKAF5rbIMb3BnemVya2Zwb3plG4URfkexpP3Q6ed_S.UYsXioN3lsol1Z4y6AAAAAGwd1c8I.offline.funcraft.net
		// hexadecimal content :
		// 78 d9 91 7b a7 e3 71 0c 8a 11 36 fe fb d8 d0 28 01 79 ad b2 0c 6f 70 67 7a 65 72 6b 66 70 6f 7a 65 1b 85 11 7e 47 b1 a4 fd d0 e9 e7 7f 62 c5 e2 a0 dd e5 b2 89 75 67 8c ba 00 00 00 00 6c 1d d5 cf

		// 78d9917ba7e3710c8a1136fefbd8d028 : launcher hash
		// 0179adb2   						: timestamp
		// 0c          						: username length (12 here)
		// 6f70677a65726b66706f7a65			: username (12 bytes, hexadecimal)
		// 1b85 117e 47b1 a4fd d0e9 e77f 62c5 e2a0 dde5 b289 7567 8cba : 24 random bytes
		// 0000 0000 6c1d d5cf				: CRC32 control code

		for(int cursor = 0; cursor < base64string.length(); cursor += 60) {
			if(cursor != 0) {
				sb.append(".");
			}

			sb.append(Character.toChars(65 + random.nextInt(26))); //random useless character
			sb.append(base64string.substring(cursor, Math.min(cursor + 60, base64string.length()))); // up to 64 chars
			sb.append(Character.toChars(65 + random.nextInt(26))); //random useless character
		}

		return ip.replaceAll("%KEY%", sb.toString());
	}

	public static void main(String... str) throws IOException {
		String md5;
		if (str.length > 1)
			md5 = str[1];
		else {
			System.err.println("Trying to find md5...");
			md5 = IOUtils.toString(new URL("http://www.azlauncher.nz/files/launcher/version.md5"), Charset.defaultCharset());
			System.err.println(md5);
		}

		String pseudo;
		if (str.length == 0)
			pseudo = "gerggerg";
		else
			pseudo = str[0];

		System.out.println(encodeKeyInIp(pseudo, "%KEY%.offline.funcraft.net", RandomBytes.generateValidCode(), md5));
	}
}
