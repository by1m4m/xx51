/*    */ package rdp.gold.brute.rdp;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.SortedMap;
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
/*    */ public abstract interface RdpConstants
/*    */ {
/* 26 */   public static final Charset CHARSET_8 = (Charset)Charset.availableCharsets().get("US-ASCII");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 31 */   public static final Charset CHARSET_16 = (Charset)Charset.availableCharsets().get("UTF-16LE");
/*    */   public static final int RDP_NEG_REQ_PROTOCOL_SSL = 1;
/*    */   public static final int RDP_NEG_REQ_PROTOCOL_HYBRID = 2;
/*    */   public static final int RDP_NEG_REQ_FLAGS = 0;
/*    */   public static final int RDP_NEG_REQ_TYPE_NEG_REQ = 1;
/*    */   public static final int RDP_NEG_REQ_TYPE_NEG_RSP = 2;
/*    */   public static final int RDP_NEG_REQ_TYPE_NEG_FAILURE = 3;
/*    */   public static final int CHANNEL_IO = 1003;
/*    */   public static final int CHANNEL_RDPRDR = 1004;
/*    */   public static final int CHANNEL_CLIPRDR = 1005;
/*    */   public static final int CHANNEL_RDPSND = 1006;
/*    */   public static final int CHANNEL_USER = 1007;
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\RdpConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */