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
		StringBuilder sb = new StringBuilder();

		for(int cursor = 0; cursor < base64string.length(); cursor += 60) {
			if(cursor != 0) {
				sb.append(".");
			}

			sb.append(Character.toChars(65 + random.nextInt(26))); //random useless character
			sb.append(base64string.substring(cursor, Math.min(cursor + 60, base64string.length()))); // one char every 60
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
