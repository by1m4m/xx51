/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.UUID;
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
/*     */ public class UUIDSerializer
/*     */   extends StdScalarSerializer<UUID>
/*     */ {
/*  21 */   static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
/*     */   
/*  23 */   public UUIDSerializer() { super(UUID.class); }
/*     */   
/*     */   @Deprecated
/*     */   public boolean isEmpty(UUID value)
/*     */   {
/*  28 */     return isEmpty(null, value);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty(SerializerProvider prov, UUID value)
/*     */   {
/*  34 */     if (value == null) {
/*  35 */       return true;
/*     */     }
/*     */     
/*  38 */     if ((value.getLeastSignificantBits() == 0L) && (value.getMostSignificantBits() == 0L))
/*     */     {
/*  40 */       return true;
/*     */     }
/*  42 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serialize(UUID value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*  50 */     if (gen.canWriteBinaryNatively())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  56 */       if (!(gen instanceof TokenBuffer)) {
/*  57 */         gen.writeBinary(_asBytes(value));
/*  58 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  65 */     char[] ch = new char[36];
/*  66 */     long msb = value.getMostSignificantBits();
/*  67 */     _appendInt((int)(msb >> 32), ch, 0);
/*  68 */     ch[8] = '-';
/*  69 */     int i = (int)msb;
/*  70 */     _appendShort(i >>> 16, ch, 9);
/*  71 */     ch[13] = '-';
/*  72 */     _appendShort(i, ch, 14);
/*  73 */     ch[18] = '-';
/*     */     
/*  75 */     long lsb = value.getLeastSignificantBits();
/*  76 */     _appendShort((int)(lsb >>> 48), ch, 19);
/*  77 */     ch[23] = '-';
/*  78 */     _appendShort((int)(lsb >>> 32), ch, 24);
/*  79 */     _appendInt((int)lsb, ch, 28);
/*     */     
/*  81 */     gen.writeString(ch, 0, 36);
/*     */   }
/*     */   
/*     */   private static void _appendInt(int bits, char[] ch, int offset)
/*     */   {
/*  86 */     _appendShort(bits >> 16, ch, offset);
/*  87 */     _appendShort(bits, ch, offset + 4);
/*     */   }
/*     */   
/*     */   private static void _appendShort(int bits, char[] ch, int offset)
/*     */   {
/*  92 */     ch[offset] = HEX_CHARS[(bits >> 12 & 0xF)];
/*  93 */     ch[(++offset)] = HEX_CHARS[(bits >> 8 & 0xF)];
/*  94 */     ch[(++offset)] = HEX_CHARS[(bits >> 4 & 0xF)];
/*  95 */     ch[(++offset)] = HEX_CHARS[(bits & 0xF)];
/*     */   }
/*     */   
/*     */ 
/*     */   private static final byte[] _asBytes(UUID uuid)
/*     */   {
/* 101 */     byte[] buffer = new byte[16];
/* 102 */     long hi = uuid.getMostSignificantBits();
/* 103 */     long lo = uuid.getLeastSignificantBits();
/* 104 */     _appendInt((int)(hi >> 32), buffer, 0);
/* 105 */     _appendInt((int)hi, buffer, 4);
/* 106 */     _appendInt((int)(lo >> 32), buffer, 8);
/* 107 */     _appendInt((int)lo, buffer, 12);
/* 108 */     return buffer;
/*     */   }
/*     */   
/*     */   private static final void _appendInt(int value, byte[] buffer, int offset)
/*     */   {
/* 113 */     buffer[offset] = ((byte)(value >> 24));
/* 114 */     buffer[(++offset)] = ((byte)(value >> 16));
/* 115 */     buffer[(++offset)] = ((byte)(value >> 8));
/* 116 */     buffer[(++offset)] = ((byte)value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\UUIDSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */