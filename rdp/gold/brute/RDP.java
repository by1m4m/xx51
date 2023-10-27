/*    */ package rdp.gold.brute;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RDP
/*    */ {
/*    */   public static final byte RDP_NEG_RESP = 2;
/*    */   public static final byte RDP_NEG_FAILURE = 3;
/* 15 */   private static Map<Integer, String> protocols = new HashMap();
/*    */   
/* 17 */   private static Map<Byte, String> protocolFlags = new HashMap();
/*    */   
/*    */   static {
/* 20 */     protocols.put(Integer.valueOf(0), "PROTOCOL_RDP");
/* 21 */     protocols.put(Integer.valueOf(1), "PROTOCOL_SSL");
/* 22 */     protocols.put(Integer.valueOf(2), "PROTOCOL_HYBRID");
/* 23 */     protocols.put(Integer.valueOf(8), "PROTOCOL_HYBRID_EX");
/*    */     
/* 25 */     protocolFlags.put(Byte.valueOf((byte)1), "EXTENDED_CLIENT_DATA_SUPPORTED");
/* 26 */     protocolFlags.put(Byte.valueOf((byte)2), "DYNVC_GFX_PROTOCOL_SUPPORTED");
/* 27 */     protocolFlags.put(Byte.valueOf((byte)4), "NEGRSP_FLAG_RESERVED");
/* 28 */     protocolFlags.put(Byte.valueOf((byte)8), "RESTRICTED_ADMIN_MODE_SUPPORTED");
/* 29 */     protocolFlags.put(Byte.valueOf((byte)16), "REDIRECTED_AUTHENTICATION_MODE_SUPPORTED");
/*    */   }
/*    */   
/*    */   public static String getProtocolName(int protocol) {
/* 33 */     if (protocols.containsKey(Integer.valueOf(protocol))) {
/* 34 */       return (String)protocols.get(Integer.valueOf(protocol));
/*    */     }
/*    */     
/* 37 */     return null;
/*    */   }
/*    */   
/*    */   public static List<String> getProtocolFlags(byte flags) {
/* 41 */     List<String> strFlags = new ArrayList();
/*    */     
/* 43 */     for (Map.Entry<Byte, String> entryFlag : protocolFlags.entrySet()) {
/* 44 */       if ((flags & ((Byte)entryFlag.getKey()).byteValue()) == ((Byte)entryFlag.getKey()).byteValue()) {
/* 45 */         strFlags.add(entryFlag.getValue());
/*    */       }
/*    */     }
/*    */     
/* 49 */     return strFlags;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\RDP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */