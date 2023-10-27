/*    */ package rdp.gold.brute.rdp.Messages.common.asn1;
/*    */ 
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
/*    */ public class ObjectID
/*    */   extends Tag
/*    */ {
/*    */   public ByteBuffer value;
/*    */   
/*    */   public ObjectID(String name)
/*    */   {
/* 29 */     super(name);
/* 30 */     this.tagType = 6;
/*    */   }
/*    */   
/*    */   public boolean isValueSet()
/*    */   {
/* 35 */     return this.value != null;
/*    */   }
/*    */   
/*    */   public long calculateLengthOfValuePayload()
/*    */   {
/* 40 */     return this.value.length;
/*    */   }
/*    */   
/*    */   public void writeTagValuePayload(ByteBuffer buf)
/*    */   {
/* 45 */     buf.writeBytes(this.value);
/*    */   }
/*    */   
/*    */   public void readTagValue(ByteBuffer buf, BerType typeAndFlags)
/*    */   {
/* 50 */     long length = buf.readBerLength();
/*    */     
/* 52 */     this.value = buf.readBytes((int)length);
/*    */   }
/*    */   
/*    */   public Tag deepCopy(String suffix)
/*    */   {
/* 57 */     return new ObjectID(this.name + suffix).copyFrom(this);
/*    */   }
/*    */   
/*    */   public Tag copyFrom(Tag tag)
/*    */   {
/* 62 */     super.copyFrom(tag);
/* 63 */     this.value = new ByteBuffer(((ObjectID)tag).value.toByteArray());
/* 64 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\common\asn1\ObjectID.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */