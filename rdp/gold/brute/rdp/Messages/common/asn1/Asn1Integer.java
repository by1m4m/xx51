/*     */ package rdp.gold.brute.rdp.Messages.common.asn1;
/*     */ 
/*     */ import rdp.gold.brute.rdp.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Asn1Integer
/*     */   extends Tag
/*     */ {
/*  26 */   public Long value = null;
/*     */   
/*     */   public Asn1Integer(String name) {
/*  29 */     super(name);
/*  30 */     this.tagType = 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readTagValue(ByteBuffer buf, BerType typeAndFlags)
/*     */   {
/*  37 */     long length = buf.readBerLength();
/*  38 */     if (length > 8L) {
/*  39 */       throw new RuntimeException("[" + this + "] ERROR: Integer value is too long: " + length + " bytes. Cannot handle integers more than 8 bytes long. Data: " + buf + ".");
/*     */     }
/*     */     
/*  42 */     this.value = Long.valueOf(buf.readSignedVarInt((int)length));
/*     */   }
/*     */   
/*     */   public Tag deepCopy(String suffix)
/*     */   {
/*  47 */     return new Asn1Integer(this.name + suffix).copyFrom(this);
/*     */   }
/*     */   
/*     */   public Tag copyFrom(Tag tag)
/*     */   {
/*  52 */     super.copyFrom(tag);
/*  53 */     this.value = ((Asn1Integer)tag).value;
/*  54 */     return this;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  59 */     return super.toString() + "= " + this.value;
/*     */   }
/*     */   
/*     */   public long calculateLengthOfValuePayload()
/*     */   {
/*  64 */     if (this.value.longValue() <= 255L)
/*  65 */       return 1L;
/*  66 */     if (this.value.longValue() <= 65535L)
/*  67 */       return 2L;
/*  68 */     if (this.value.longValue() <= 16777215L)
/*  69 */       return 3L;
/*  70 */     if (this.value.longValue() <= 4294967295L)
/*  71 */       return 4L;
/*  72 */     if (this.value.longValue() <= 1099511627775L)
/*  73 */       return 5L;
/*  74 */     if (this.value.longValue() <= 281474976710655L)
/*  75 */       return 6L;
/*  76 */     if (this.value.longValue() <= 72057594037927935L) {
/*  77 */       return 7L;
/*     */     }
/*  79 */     return 8L;
/*     */   }
/*     */   
/*     */   public void writeTagValuePayload(ByteBuffer buf)
/*     */   {
/*  84 */     long value = this.value.longValue();
/*     */     
/*  86 */     if (value < 255L) {
/*  87 */       buf.writeByte((int)value);
/*  88 */     } else if (value <= 65535L) {
/*  89 */       buf.writeShort((int)value);
/*  90 */     } else if (value <= 16777215L) {
/*  91 */       buf.writeByte((int)(value >> 16));
/*  92 */       buf.writeShort((int)value);
/*  93 */     } else if (value <= 4294967295L) {
/*  94 */       buf.writeInt((int)value);
/*  95 */     } else if (value <= 1099511627775L) {
/*  96 */       buf.writeByte((int)(value >> 32));
/*  97 */       buf.writeInt((int)value);
/*  98 */     } else if (value <= 281474976710655L) {
/*  99 */       buf.writeShort((int)(value >> 32));
/* 100 */       buf.writeInt((int)value);
/* 101 */     } else if (value <= 72057594037927935L) {
/* 102 */       buf.writeByte((int)(value >> 48));
/* 103 */       buf.writeShort((int)(value >> 32));
/* 104 */       buf.writeInt((int)value);
/*     */     } else {
/* 106 */       buf.writeInt((int)(value >> 32));
/* 107 */       buf.writeInt((int)value);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isValueSet()
/*     */   {
/* 113 */     return this.value != null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\common\asn1\Asn1Integer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */