/*     */ package rdp.gold.brute.rdp.Messages;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.Key;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Arrays;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import rdp.gold.brute.rdp.RdpConstants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CryptoAlgos
/*     */   implements NtlmConstants
/*     */ {
/*     */   public static final String NIL = "";
/*     */   
/*     */   public static String concatenationOf(String... args)
/*     */   {
/*  46 */     StringBuffer sb = new StringBuffer();
/*  47 */     for (String arg : args) {
/*  48 */       sb.append(arg);
/*     */     }
/*  50 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static byte[] concatenationOf(byte[]... arrays)
/*     */   {
/*  57 */     int length = 0;
/*  58 */     for (array : arrays) {
/*  59 */       length += array.length;
/*     */     }
/*     */     
/*  62 */     byte[] result = new byte[length];
/*  63 */     int destPos = 0;
/*  64 */     byte[][] arrayOfByte2 = arrays;byte[] array = arrayOfByte2.length; for (byte[] arrayOfByte3 = 0; arrayOfByte3 < array; arrayOfByte3++) { byte[] array = arrayOfByte2[arrayOfByte3];
/*  65 */       System.arraycopy(array, 0, result, destPos, array.length);
/*  66 */       destPos += array.length;
/*     */     }
/*     */     
/*  69 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static byte[] CRC32(byte[] m)
/*     */   {
/*  76 */     throw new RuntimeException("FATAL: Not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] DES(byte[] k, byte[] d)
/*     */   {
/*  85 */     throw new RuntimeException("FATAL: Not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] DESL(byte[] k, byte[] d)
/*     */   {
/* 102 */     throw new RuntimeException("FATAL: Not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] getVersion()
/*     */   {
/* 111 */     return new byte[] { 6, 1, -79, 29, 0, 0, 0, 15 };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] LMGETKEY(byte[] u, byte[] d)
/*     */   {
/* 119 */     throw new RuntimeException("FATAL: Not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static byte[] NTGETKEY(byte[] u, byte[] d)
/*     */   {
/* 126 */     throw new RuntimeException("FATAL: Not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] HMAC(byte[] k, byte[] m)
/*     */   {
/* 134 */     throw new RuntimeException("FATAL: Not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static byte[] HMAC_MD5(byte[] k, byte[] m)
/*     */   {
/*     */     try
/*     */     {
/* 143 */       String algorithm = "HMacMD5";
/* 144 */       Mac hashMac = Mac.getInstance(algorithm);
/*     */       
/* 146 */       Key secretKey = new SecretKeySpec(k, 0, k.length, algorithm);
/* 147 */       hashMac.init(secretKey);
/* 148 */       return hashMac.doFinal(m);
/*     */     } catch (Exception e) {
/* 150 */       throw new RuntimeException("Cannot calculate HMAC-MD5.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] KXKEY(byte[] sessionBaseKey)
/*     */   {
/* 161 */     return Arrays.copyOf(sessionBaseKey, sessionBaseKey.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] LMOWF()
/*     */   {
/* 170 */     throw new RuntimeException("FATAL: Not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static byte[] MD4(byte[] m)
/*     */   {
/*     */     try
/*     */     {
/* 179 */       MD4 md4 = new MD4();
/* 180 */       md4.update(m);
/* 181 */       return md4.digest();
/*     */     } catch (Exception e) {
/* 183 */       throw new RuntimeException("Cannot calculate MD5.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static byte[] MD5(byte[] m)
/*     */   {
/*     */     try
/*     */     {
/* 193 */       return MessageDigest.getInstance("MD5").digest(m);
/*     */     } catch (Exception e) {
/* 195 */       throw new RuntimeException("Cannot calculate MD5.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static byte[] MD5_HASH(byte[] m)
/*     */   {
/*     */     try
/*     */     {
/* 205 */       return MessageDigest.getInstance("MD5").digest(m);
/*     */     } catch (Exception e) {
/* 207 */       throw new RuntimeException("Cannot calculate MD5.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] NONCE(int n)
/*     */   {
/* 222 */     byte[] nonce = new byte[n];
/* 223 */     SecureRandom random = new SecureRandom();
/* 224 */     random.nextBytes(nonce);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 229 */     return nonce;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] NTOWF()
/*     */   {
/* 238 */     throw new RuntimeException("FATAL: Not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] RC4(Cipher h, byte[] d)
/*     */   {
/* 250 */     return h.update(d);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] RC4K(byte[] k, byte[] d)
/*     */   {
/*     */     try
/*     */     {
/* 262 */       Cipher cipher = Cipher.getInstance("RC4");
/* 263 */       Key key = new SecretKeySpec(k, "RC4");
/* 264 */       cipher.init(1, key);
/* 265 */       return cipher.doFinal(d);
/*     */     } catch (Exception e) {
/* 267 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Cipher RC4Init(byte[] k)
/*     */   {
/* 276 */     throw new RuntimeException("FATAL: Not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] SEALKEY(byte[] f, byte[] k, byte[] string1)
/*     */   {
/* 284 */     throw new RuntimeException("FATAL: Not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] SIGNKEY(int flag, byte[] k, byte[] string1)
/*     */   {
/* 292 */     throw new RuntimeException("FATAL: Not implemented.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] Currenttime()
/*     */   {
/* 303 */     long time = (System.currentTimeMillis() + 11644473600000L) * 10000L;
/*     */     
/*     */ 
/* 306 */     byte[] result = new byte[8];
/* 307 */     for (int i = 0; i < 8; time >>>= 8) {
/* 308 */       result[i] = ((byte)(int)time);i++;
/*     */     }
/*     */     
/* 311 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] UNICODE(String string)
/*     */   {
/* 320 */     return string.getBytes(RdpConstants.CHARSET_16);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String UpperCase(String string)
/*     */   {
/* 327 */     return string.toUpperCase();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] Z(int n)
/*     */   {
/* 335 */     return new byte[n];
/*     */   }
/*     */   
/*     */   public static Cipher initRC4(byte[] key) {
/*     */     try {
/* 340 */       Cipher cipher = Cipher.getInstance("RC4");
/* 341 */       cipher.init(1, new SecretKeySpec(key, "RC4"));
/* 342 */       return cipher;
/*     */     } catch (Exception e) {
/* 344 */       throw new RuntimeException("Cannot initialize RC4 sealing handle with client sealing key.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void callAll(Object obj)
/*     */   {
/* 352 */     Method[] methods = obj.getClass().getDeclaredMethods();
/* 353 */     for (Method m : methods) {
/* 354 */       if (m.getName().startsWith("test")) {
/*     */         try {
/* 356 */           m.invoke(obj, (Object[])null);
/*     */         } catch (Exception e) {
/* 358 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 365 */     callAll(new CryptoAlgos());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\CryptoAlgos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */