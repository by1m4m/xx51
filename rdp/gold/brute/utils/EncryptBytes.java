/*    */ package rdp.gold.brute.utils;
/*    */ 
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.spec.IvParameterSpec;
/*    */ import javax.crypto.spec.SecretKeySpec;
/*    */ import org.apache.log4j.Logger;
/*    */ import rdp.gold.brute.Config;
/*    */ 
/*    */ public class EncryptBytes
/*    */ {
/* 11 */   private static final Logger logger = Logger.getLogger(EncryptBytes.class);
/*    */   private static Cipher cipherEncrypt;
/*    */   private static Cipher cipherDecrypt;
/*    */   
/*    */   static {
/*    */     try {
/* 17 */       SecretKeySpec key = new SecretKeySpec(Config.KEY_ENCRYPT, "AES");
/* 18 */       IvParameterSpec iv = new IvParameterSpec(Config.IV_ENCRYPT);
/*    */       
/* 20 */       cipherEncrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
/* 21 */       cipherEncrypt.init(1, key, iv);
/*    */       
/* 23 */       cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
/* 24 */       cipherDecrypt.init(2, key, iv);
/*    */     } catch (Exception e) {
/* 26 */       logger.error(e);
/* 27 */       System.exit(0);
/*    */     }
/*    */   }
/*    */   
/*    */   public static byte[] encryptBytes(byte[] bytesArray) throws Exception {
/* 32 */     return cipherEncrypt.doFinal(bytesArray);
/*    */   }
/*    */   
/*    */   public static byte[] decryptBytes(byte[] bytesArray) throws Exception {
/* 36 */     return cipherDecrypt.doFinal(bytesArray);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\utils\EncryptBytes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */