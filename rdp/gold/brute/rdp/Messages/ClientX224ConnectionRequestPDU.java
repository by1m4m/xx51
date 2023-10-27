/*    */ package rdp.gold.brute.rdp.Messages;
/*    */ 
/*    */ import rdp.gold.brute.rdp.BaseElement;
/*    */ import rdp.gold.brute.rdp.ByteBuffer;
/*    */ import rdp.gold.brute.rdp.RdpConstants;
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
/*    */ public class ClientX224ConnectionRequestPDU
/*    */   extends BaseElement
/*    */ {
/*    */   public static final int X224_TPDU_CONNECTION_REQUEST = 224;
/*    */   public static final int X224_TPDU_CONNECTION_CONFIRM = 208;
/*    */   public static final int X224_TPDU_DISCONNECTION_REQUEST = 128;
/*    */   public static final int X224_TPDU_DISCONNECTION_CONFIRM = 192;
/*    */   public static final int X224_TPDU_EXPEDITED_DATA = 16;
/*    */   public static final int X224_TPDU_DATA_ACKNOWLEDGE = 97;
/*    */   public static final int X224_TPDU_EXPEDITET_ACKNOWLEDGE = 64;
/*    */   public static final int X224_TPDU_REJECT = 81;
/*    */   public static final int X224_TPDU_ERROR = 112;
/*    */   public static final int X224_TPDU_PROTOCOL_IDENTIFIER = 1;
/*    */   protected String userName;
/*    */   protected int protocol;
/*    */   
/*    */   public ClientX224ConnectionRequestPDU(String userName, int protocol)
/*    */   {
/* 51 */     this.userName = userName;
/* 52 */     this.protocol = protocol;
/*    */   }
/*    */   
/*    */ 
/*    */   public ByteBuffer proccessPacket(ByteBuffer byteBuffer)
/*    */   {
/* 58 */     int length = 33 + this.userName.length();
/* 59 */     ByteBuffer buf = new ByteBuffer(length, true);
/*    */     
/*    */ 
/* 62 */     buf.writeByte(224);
/*    */     
/* 64 */     buf.writeShort(0);
/* 65 */     buf.writeShort(0);
/* 66 */     buf.writeByte(0);
/* 67 */     buf.writeString("Cookie: mstshash=" + this.userName + "\r\n", RdpConstants.CHARSET_8);
/*    */     
/*    */ 
/* 70 */     buf.writeByte(1);
/*    */     
/* 72 */     buf.writeByte(0);
/*    */     
/* 74 */     buf.writeByte(8);
/* 75 */     buf.writeByte(0);
/*    */     
/*    */ 
/* 78 */     buf.writeIntLE(this.protocol);
/*    */     
/*    */ 
/* 81 */     ByteBuffer data = new ByteBuffer(5);
/*    */     
/*    */ 
/* 84 */     data.writeVariableIntLE(buf.length);
/*    */     
/*    */ 
/* 87 */     data.length = data.cursor;
/*    */     
/* 89 */     buf.prepend(data);
/* 90 */     data.unref();
/*    */     
/* 92 */     return buf;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\ClientX224ConnectionRequestPDU.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */