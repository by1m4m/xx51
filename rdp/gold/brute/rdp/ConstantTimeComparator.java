/*    */ package rdp.gold.brute.rdp;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ 
/*    */ public class ConstantTimeComparator
/*    */ {
/*    */   public static boolean compareBytes(byte[] b1, byte[] b2) {
/*  8 */     if (b1.length != b2.length) {
/*  9 */       return false;
/*    */     }
/*    */     
/* 12 */     int result = 0;
/* 13 */     for (int i = 0; i < b1.length; i++) {
/* 14 */       result |= b1[i] ^ b2[i];
/*    */     }
/* 16 */     return result == 0;
/*    */   }
/*    */   
/*    */   public static boolean compareStrings(String s1, String s2) {
/* 20 */     Charset encoding = Charset.forName("UTF-8");
/* 21 */     return compareBytes(s1.getBytes(encoding), s2.getBytes(encoding));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\ConstantTimeComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */