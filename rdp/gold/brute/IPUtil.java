/*    */ package rdp.gold.brute;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.regex.Matcher;
/*    */ import org.apache.commons.net.util.SubnetUtils;
/*    */ 
/*    */ public class IPUtil
/*    */ {
/*    */   public static List<String> getIpList(String minIp, String maxIp)
/*    */   {
/* 12 */     List<String> iplist = new ArrayList();
/*    */     
/* 14 */     long minIpLong = ipToLong(minIp);
/* 15 */     long maxIpLong = ipToLong(maxIp);
/* 16 */     for (long ipItem = minIpLong; ipItem <= maxIpLong; ipItem += 1L) {
/* 17 */       iplist.add(longToIp(ipItem));
/*    */     }
/*    */     
/* 20 */     return iplist;
/*    */   }
/*    */   
/*    */   public static List<String> getIpList(String cidr) {
/* 24 */     SubnetUtils subnetUtils = new SubnetUtils(cidr);
/*    */     
/* 26 */     List<String> ipList = new ArrayList(java.util.Arrays.asList(subnetUtils.getInfo().getAllAddresses()));
/*    */     
/* 28 */     return ipList;
/*    */   }
/*    */   
/*    */   public static long cntIpByIpRangeOrCidr(String ipRangeOrCidr) {
/* 32 */     Matcher matcherRange = Config.PATTERN_IS_RANGE.matcher(ipRangeOrCidr);
/* 33 */     Matcher matcherCidr = Config.PATTERN_IS_CIDR.matcher(ipRangeOrCidr);
/*    */     
/* 35 */     long cnt = 0L;
/*    */     
/* 37 */     if (matcherRange.find()) {
/* 38 */       cnt += getIpList(matcherRange.group(1), matcherRange.group(2)).size();
/* 39 */     } else if (matcherCidr.find()) {
/* 40 */       SubnetUtils subnetUtils = new SubnetUtils(ipRangeOrCidr);
/* 41 */       cnt += subnetUtils.getInfo().getAddressCountLong();
/*    */     }
/*    */     
/* 44 */     return cnt;
/*    */   }
/*    */   
/*    */   public static long ipToLong(String ipAddress) {
/* 48 */     long result = 0L;
/*    */     
/* 50 */     String[] ipAddressInArray = ipAddress.split("\\.");
/*    */     
/* 52 */     for (int i = 3; i >= 0; i--) {
/* 53 */       long ip = Long.parseLong(ipAddressInArray[(3 - i)]);
/*    */       
/* 55 */       result |= ip << i * 8;
/*    */     }
/*    */     
/* 58 */     return result;
/*    */   }
/*    */   
/*    */   public static String longToIp(long ip) {
/* 62 */     StringBuilder sb = new StringBuilder(15);
/*    */     
/* 64 */     for (int i = 0; i < 4; i++) {
/* 65 */       sb.insert(0, Long.toString(ip & 0xFF));
/*    */       
/* 67 */       if (i < 3) {
/* 68 */         sb.insert(0, '.');
/*    */       }
/*    */       
/* 71 */       ip >>= 8;
/*    */     }
/*    */     
/*    */ 
/* 75 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\IPUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */