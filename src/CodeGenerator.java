import com.google.common.base.Charsets;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.apache.commons.codec.binary.Base64;

import java.util.zip.CRC32;

public class CodeGenerator {
   private ByteArrayDataOutput output;
   private String launcherMD5;
   private String str;
   private byte[] key;

   public CodeGenerator(String launcherMD5, String var2) {
      this.output = ByteStreams.newDataOutput(); // crée le buffer
      this.launcherMD5 = launcherMD5;
      this.str = var2;
   }

   private byte[] convertDigest(String md5) {
      byte[] jarFileDigest = new byte[16];
      for (int i = 0; i < 32; i += 2) {
         jarFileDigest[i / 2] = (Integer.valueOf(md5.substring(i, i + 2), 16)).byteValue();
      }
      return jarFileDigest;
   }

   public void setKey(byte[] var1) {
      this.key = var1;
   }

   public void write() {
      this.output.write(this.convertDigest(launcherMD5), 0, 16); // que les 16 premiers.
      this.output.writeInt((int)(System.currentTimeMillis() / 1000L / 60L));

      byte[] bytes = this.str.getBytes(Charsets.US_ASCII);
      this.output.writeByte(bytes.length);

      for (byte var5 : bytes) {
         this.output.writeByte(var5);
      }

      this.output.write(this.key, 0, 24); //24 bits
   }

   public void writeCRC() {
      byte[] currentOutput = this.output.toByteArray();
      CRC32 crc = new CRC32();
      crc.update(currentOutput, 0, currentOutput.length);
      long crcVa = crc.getValue();
      crc.reset();
      this.output.writeLong(crcVa);
   }

   public String getBase64() {
      return Base64.encodeBase64String(this.output.toByteArray()).replaceAll("\\+", "-").replaceAll("/", "_").replaceAll("=", "");
   }
}
