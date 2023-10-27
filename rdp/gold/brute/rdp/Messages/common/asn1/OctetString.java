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
/*    */ public class OctetString
/*    */   extends Tag
/*    */ {
/* 23 */   public ByteBuffer value = null;
/*    */   
/*    */   public OctetString(String name) {
/* 26 */     super(name);
/* 27 */     this.tagType = 4;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readTagValue(ByteBuffer buf, BerType typeAndFlags)
/*    */   {
/* 34 */     long length = buf.readBerLength();
/*    */     
/* 36 */     if (length > buf.length) {
/* 37 */       throw new RuntimeException("BER octet string is too long: " + length + " bytes. Data: " + buf + ".");
/*    */     }
/* 39 */     this.value = buf.readBytes((int)length);
/*    */   }
/*    */   
/*    */   public Tag deepCopy(String suffix)
/*    */   {
/* 44 */     return new OctetString(this.name + suffix).copyFrom(this);
/*    */   }
/*    */   
/*    */   public Tag copyFrom(Tag tag)
/*    */   {
/* 49 */     super.copyFrom(tag);
/* 50 */     this.value = ((OctetString)tag).value;
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 56 */     return super.toString() + "= " + this.value;
/*    */   }
/*    */   
/*    */   public long calculateLengthOfValuePayload()
/*    */   {
/* 61 */     if (this.value != null) {
/* 62 */       return this.value.length;
/*    */     }
/* 64 */     return 0L;
/*    */   }
/*    */   
/*    */   public void writeTagValuePayload(ByteBuffer buf)
/*    */   {
/* 69 */     if (this.value != null) {
/* 70 */       buf.writeBytes(this.value);
/*    */     } else {}
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isValueSet()
/*    */   {
/* 77 */     return this.value != null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\common\asn1\OctetString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */