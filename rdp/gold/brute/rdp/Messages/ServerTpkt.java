/*    */ package rdp.gold.brute.rdp.Messages;
/*    */ 
/*    */ import org.apache.log4j.Logger;
/*    */ import rdp.gold.brute.rdp.BaseElement;
/*    */ import rdp.gold.brute.rdp.ByteBuffer;
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
/*    */ public class ServerTpkt
/*    */   extends BaseElement
/*    */ {
/*    */   public static final int PROTOCOL_TPKT = 3;
/* 29 */   private final Logger logger = Logger.getLogger(ServerTpkt.class);
/*    */   
/*    */   public ByteBuffer proccessPacket(ByteBuffer buf)
/*    */   {
/* 33 */     if (buf == null) {
/* 34 */       throw new RuntimeException("Buffer is null");
/*    */     }
/* 36 */     if (this.verbose) {
/* 37 */       this.logger.info("Data received: " + buf + ".");
/*    */     }
/*    */     
/* 40 */     if (!cap(buf, 4, -1, false)) {
/* 41 */       throw new RuntimeException("Error read 4 bytes");
/*    */     }
/* 43 */     int version = buf.readUnsignedByte();
/* 44 */     if (version != 3) {
/* 45 */       throw new RuntimeException("Unexpected data in TPKT header. Expected TPKT version: 0x03,  actual value: " + buf + ".");
/*    */     }
/* 47 */     buf.skipBytes(1);
/*    */     
/*    */ 
/* 50 */     int length = buf.readUnsignedShort();
/* 51 */     if (!cap(buf, length, length, false)) {
/* 52 */       throw new RuntimeException("Error read unsigned short length");
/*    */     }
/* 54 */     int payloadLength = length - buf.cursor;
/*    */     
/*    */ 
/* 57 */     ByteBuffer outBuf = buf.slice(buf.cursor, payloadLength, true);
/* 58 */     buf.unref();
/*    */     
/* 60 */     return outBuf;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\ServerTpkt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */