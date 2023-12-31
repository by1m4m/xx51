/*    */ package org.apache.http.conn.util;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class InetAddressUtils
/*    */ {
/* 45 */   private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
/*    */   
/*    */ 
/*    */ 
/* 49 */   private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
/*    */   
/*    */ 
/*    */ 
/* 53 */   private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
/*    */   
/*    */ 
/*    */   public static boolean isIPv4Address(String input)
/*    */   {
/* 58 */     return IPV4_PATTERN.matcher(input).matches();
/*    */   }
/*    */   
/*    */   public static boolean isIPv6StdAddress(String input) {
/* 62 */     return IPV6_STD_PATTERN.matcher(input).matches();
/*    */   }
/*    */   
/*    */   public static boolean isIPv6HexCompressedAddress(String input) {
/* 66 */     return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
/*    */   }
/*    */   
/*    */   public static boolean isIPv6Address(String input) {
/* 70 */     return (isIPv6StdAddress(input)) || (isIPv6HexCompressedAddress(input));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\conn\util\InetAddressUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */