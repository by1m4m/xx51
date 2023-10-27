/*    */ package rdp.gold.brute.rdp;
/*    */ 
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BaseElement
/*    */ {
/*    */   public static final int UNLIMITED = -1;
/* 11 */   private final Logger logger = Logger.getLogger(BaseElement.class);
/*    */   
/*    */ 
/*    */   protected String id;
/*    */   
/* 16 */   protected boolean verbose = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 22 */   protected int numBuffers = 0;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 27 */   protected int packetNumber = 0;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 32 */   protected int incommingBufLength = -1;
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
/*    */   public boolean cap(ByteBuffer buf, int minLength, int maxLength, boolean fromCursor)
/*    */   {
/* 46 */     if (buf == null) {
/* 47 */       return false;
/*    */     }
/* 49 */     int length = buf.length;
/*    */     int cursor;
/*    */     int cursor;
/* 52 */     if (fromCursor) {
/* 53 */       cursor = buf.cursor;
/*    */     } else {
/* 55 */       cursor = 0;
/*    */     }
/* 57 */     length -= cursor;
/*    */     
/* 59 */     if (((minLength < 0) || (length >= minLength)) && ((maxLength < 0) || (length <= maxLength)))
/*    */     {
/* 61 */       return true;
/*    */     }
/*    */     
/* 64 */     if ((minLength >= 0) && (length < minLength))
/*    */     {
/* 66 */       if (this.verbose) {
/* 67 */         this.logger.info("Buffer is too small. Min length: " + minLength + ", data length (after cursor): " + length + ".");
/*    */       }
/*    */       
/* 70 */       return false; }
/* 71 */     if ((maxLength >= 0) && (length > maxLength))
/*    */     {
/* 73 */       if (this.verbose) {
/* 74 */         this.logger.info("Buffer is too big. Max length: " + maxLength + ", data length (after cursor): " + length + ".");
/*    */       }
/*    */       
/*    */ 
/* 78 */       buf.length = (maxLength + cursor);
/*    */     }
/*    */     
/*    */ 
/* 82 */     return true;
/*    */   }
/*    */   
/*    */   public abstract ByteBuffer proccessPacket(ByteBuffer paramByteBuffer);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\BaseElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */