/*     */ package rdp.gold.brute.rdp.Messages.common.asn1;
/*     */ 
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ public class Sequence
/*     */   extends Tag
/*     */ {
/*     */   public Tag[] tags;
/*     */   
/*     */   public Sequence(String name)
/*     */   {
/*  33 */     super(name);
/*  34 */     this.tagType = 16;
/*     */     
/*  36 */     this.constructed = true;
/*     */   }
/*     */   
/*     */   public long calculateLengthOfValuePayload()
/*     */   {
/*  41 */     long sum = 0L;
/*     */     
/*  43 */     for (Tag tag : this.tags) {
/*  44 */       long tagLength = tag.calculateFullLength();
/*  45 */       sum += tagLength;
/*     */     }
/*     */     
/*  48 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeTagValuePayload(ByteBuffer buf)
/*     */   {
/*  54 */     for (Tag tag : this.tags) {
/*  55 */       tag.writeTag(buf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readTagValue(ByteBuffer buf, BerType typeAndFlags)
/*     */   {
/*  63 */     long length = buf.readBerLength();
/*  64 */     if (length > buf.remainderLength()) {
/*  65 */       throw new RuntimeException("BER sequence is too long: " + length + " bytes, while buffer remainder length is " + buf.remainderLength() + ". Data: " + buf + ".");
/*     */     }
/*     */     
/*  68 */     ByteBuffer value = buf.readBytes((int)length);
/*  69 */     parseContent(value);
/*     */     
/*  71 */     value.unref();
/*     */   }
/*     */   
/*     */   protected void parseContent(ByteBuffer buf) {
/*  75 */     for (int i = 0; (buf.remainderLength() > 0) && (i < this.tags.length); i++) {
/*  76 */       BerType typeAndFlags = readBerType(buf);
/*     */       
/*     */ 
/*  79 */       if (!this.tags[i].isTypeValid(typeAndFlags))
/*     */       {
/*     */ 
/*  82 */         if (!this.tags[i].optional) {
/*  83 */           throw new RuntimeException("[" + this + "] ERROR: Required tag is missed: " + this.tags[i] + ". Unexected tag type: " + typeAndFlags + ". Data: " + buf + ".");
/*     */         }
/*  87 */         for (; 
/*     */             
/*  87 */             i < this.tags.length; i++) {
/*  88 */           if (this.tags[i].isTypeValid(typeAndFlags)) {
/*     */             break;
/*     */           }
/*     */         }
/*     */         
/*  93 */         if ((i >= this.tags.length) || (!this.tags[i].isTypeValid(typeAndFlags))) {
/*  94 */           throw new RuntimeException("[" + this + "] ERROR: No more tags to read or skip, but some data still left in buffer. Unexected tag type: " + typeAndFlags + ". Data: " + buf + ".");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 100 */       this.tags[i].readTag(buf, typeAndFlags);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isTypeValid(BerType typeAndFlags, boolean explicit)
/*     */   {
/* 107 */     if (explicit) {
/* 108 */       return (typeAndFlags.tagClass == this.tagClass) && (typeAndFlags.constructed) && (typeAndFlags.typeOrTagNumber == this.tagNumber);
/*     */     }
/*     */     
/* 111 */     return (typeAndFlags.tagClass == 0) && (typeAndFlags.constructed) && (typeAndFlags.typeOrTagNumber == 16);
/*     */   }
/*     */   
/*     */   public Tag deepCopy(String suffix)
/*     */   {
/* 116 */     return new Sequence(this.name + suffix).copyFrom(this);
/*     */   }
/*     */   
/*     */   public Tag copyFrom(Tag tag)
/*     */   {
/* 121 */     super.copyFrom(tag);
/*     */     
/* 123 */     if (this.tags.length != ((Sequence)tag).tags.length) {
/* 124 */       throw new RuntimeException("Incompatible sequences. This: " + this + ", another: " + tag + ".");
/*     */     }
/* 126 */     for (int i = 0; i < this.tags.length; i++) {
/* 127 */       this.tags[i].copyFrom(((Sequence)tag).tags[i]);
/*     */     }
/*     */     
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 135 */     return super.toString() + "{" + Arrays.toString(this.tags) + " }";
/*     */   }
/*     */   
/*     */   public boolean isValueSet()
/*     */   {
/* 140 */     return this.tags != null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\common\asn1\Sequence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */