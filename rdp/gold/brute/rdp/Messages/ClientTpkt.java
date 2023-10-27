/*    */ package rdp.gold.brute.rdp.Messages;
/*    */ 
/*    */ import org.apache.log4j.Logger;
/*    */ import rdp.gold.brute.rdp.ByteBuffer;
/*    */ 
/*    */ public class ClientTpkt extends rdp.gold.brute.rdp.BaseElement
/*    */ {
/*  8 */   private final Logger logger = Logger.getLogger(ClientTpkt.class);
/*    */   
/*    */   public ByteBuffer proccessPacket(ByteBuffer buf) {
/* 11 */     if (buf == null) {
/* 12 */       throw new RuntimeException("Buffer is null");
/*    */     }
/* 14 */     if (this.verbose) {
/* 15 */       this.logger.info("Data received: " + buf + ".");
/*    */     }
/* 17 */     if (buf.length + 4 > 65535) {
/* 18 */       throw new RuntimeException("Packet is too long for TPKT (max length 65535-4): " + buf + ".");
/*    */     }
/* 20 */     ByteBuffer data = new ByteBuffer(4);
/*    */     
/* 22 */     data.writeByte(3);
/*    */     
/* 24 */     data.writeByte(0);
/*    */     
/* 26 */     data.writeShort(buf.length + 4);
/*    */     
/* 28 */     buf.prepend(data);
/* 29 */     data.unref();
/*    */     
/* 31 */     return buf;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\ClientTpkt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */