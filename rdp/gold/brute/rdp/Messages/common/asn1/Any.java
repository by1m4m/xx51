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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Any
/*    */   extends Tag
/*    */ {
/*    */   public ByteBuffer value;
/*    */   
/*    */   public Any(String name)
/*    */   {
/* 32 */     super(name);
/*    */   }
/*    */   
/*    */   public boolean isValueSet()
/*    */   {
/* 37 */     return this.value != null;
/*    */   }
/*    */   
/*    */   public long calculateLengthOfValuePayload()
/*    */   {
/* 42 */     return this.value.length;
/*    */   }
/*    */   
/*    */   public void writeTagValuePayload(ByteBuffer buf)
/*    */   {
/* 47 */     buf.writeBytes(this.value);
/*    */   }
/*    */   
/*    */   public void readTagValue(ByteBuffer buf, BerType typeAndFlags)
/*    */   {
/* 52 */     long length = buf.readBerLength();
/*    */     
/* 54 */     this.value = buf.readBytes((int)length);
/*    */   }
/*    */   
/*    */   public Tag deepCopy(String suffix)
/*    */   {
/* 59 */     return new Any(this.name + suffix).copyFrom(this);
/*    */   }
/*    */   
/*    */   public Tag copyFrom(Tag tag)
/*    */   {
/* 64 */     super.copyFrom(tag);
/* 65 */     this.tagType = tag.tagType;
/* 66 */     this.value = new ByteBuffer(((Any)tag).value.toByteArray());
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public boolean isTypeValid(BerType typeAndFlags, boolean explicit)
/*    */   {
/* 72 */     if (explicit) {
/* 73 */       return (typeAndFlags.tagClass == this.tagClass) && (typeAndFlags.constructed) && (typeAndFlags.typeOrTagNumber == this.tagNumber);
/*    */     }
/* 75 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\common\asn1\Any.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */