/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.Base64Variants;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ public class UUIDDeserializer
/*     */   extends FromStringDeserializer<UUID>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  15 */   static final int[] HEX_DIGITS = new int[127];
/*     */   
/*  17 */   static { Arrays.fill(HEX_DIGITS, -1);
/*  18 */     for (int i = 0; i < 10; i++) HEX_DIGITS[(48 + i)] = i;
/*  19 */     for (int i = 0; i < 6; i++) {
/*  20 */       HEX_DIGITS[(97 + i)] = (10 + i);
/*  21 */       HEX_DIGITS[(65 + i)] = (10 + i);
/*     */     }
/*     */   }
/*     */   
/*  25 */   public UUIDDeserializer() { super(UUID.class); }
/*     */   
/*     */ 
/*     */ 
/*     */   protected UUID _deserialize(String id, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  32 */     if (id.length() != 36)
/*     */     {
/*     */ 
/*     */ 
/*  36 */       if (id.length() == 24) {
/*  37 */         byte[] stuff = Base64Variants.getDefaultVariant().decode(id);
/*  38 */         return _fromBytes(stuff, ctxt);
/*     */       }
/*  40 */       _badFormat(id);
/*     */     }
/*     */     
/*     */ 
/*  44 */     if ((id.charAt(8) != '-') || (id.charAt(13) != '-') || (id.charAt(18) != '-') || (id.charAt(23) != '-'))
/*     */     {
/*  46 */       _badFormat(id);
/*     */     }
/*  48 */     long l1 = intFromChars(id, 0);
/*  49 */     l1 <<= 32;
/*  50 */     long l2 = shortFromChars(id, 9) << 16;
/*  51 */     l2 |= shortFromChars(id, 14);
/*  52 */     long hi = l1 + l2;
/*     */     
/*  54 */     int i1 = shortFromChars(id, 19) << 16 | shortFromChars(id, 24);
/*  55 */     l1 = i1;
/*  56 */     l1 <<= 32;
/*  57 */     l2 = intFromChars(id, 28);
/*  58 */     l2 = l2 << 32 >>> 32;
/*  59 */     long lo = l1 | l2;
/*     */     
/*  61 */     return new UUID(hi, lo);
/*     */   }
/*     */   
/*     */   protected UUID _deserializeEmbedded(Object ob, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  67 */     if ((ob instanceof byte[])) {
/*  68 */       return _fromBytes((byte[])ob, ctxt);
/*     */     }
/*  70 */     super._deserializeEmbedded(ob, ctxt);
/*  71 */     return null;
/*     */   }
/*     */   
/*     */   private void _badFormat(String uuidStr) {
/*  75 */     throw new NumberFormatException("UUID has to be represented by the standard 36-char representation");
/*     */   }
/*     */   
/*     */   static int intFromChars(String str, int index) {
/*  79 */     return (byteFromChars(str, index) << 24) + (byteFromChars(str, index + 2) << 16) + (byteFromChars(str, index + 4) << 8) + byteFromChars(str, index + 6);
/*     */   }
/*     */   
/*     */   static int shortFromChars(String str, int index) {
/*  83 */     return (byteFromChars(str, index) << 8) + byteFromChars(str, index + 2);
/*     */   }
/*     */   
/*     */   static int byteFromChars(String str, int index)
/*     */   {
/*  88 */     char c1 = str.charAt(index);
/*  89 */     char c2 = str.charAt(index + 1);
/*     */     
/*  91 */     if ((c1 <= '') && (c2 <= '')) {
/*  92 */       int hex = HEX_DIGITS[c1] << 4 | HEX_DIGITS[c2];
/*  93 */       if (hex >= 0) {
/*  94 */         return hex;
/*     */       }
/*     */     }
/*  97 */     if ((c1 > '') || (HEX_DIGITS[c1] < 0)) {
/*  98 */       return _badChar(str, index, c1);
/*     */     }
/* 100 */     return _badChar(str, index + 1, c2);
/*     */   }
/*     */   
/*     */   static int _badChar(String uuidStr, int index, char c) {
/* 104 */     throw new NumberFormatException("Non-hex character '" + c + "', not valid character for a UUID String" + "' (value 0x" + Integer.toHexString(c) + ") for UUID String \"" + uuidStr + "\"");
/*     */   }
/*     */   
/*     */   private UUID _fromBytes(byte[] bytes, DeserializationContext ctxt) throws IOException
/*     */   {
/* 109 */     if (bytes.length != 16) {
/* 110 */       ctxt.mappingException("Can only construct UUIDs from byte[16]; got " + bytes.length + " bytes");
/*     */     }
/* 112 */     return new UUID(_long(bytes, 0), _long(bytes, 8));
/*     */   }
/*     */   
/*     */   private static long _long(byte[] b, int offset) {
/* 116 */     long l1 = _int(b, offset) << 32;
/* 117 */     long l2 = _int(b, offset + 4);
/*     */     
/* 119 */     l2 = l2 << 32 >>> 32;
/* 120 */     return l1 | l2;
/*     */   }
/*     */   
/*     */   private static int _int(byte[] b, int offset) {
/* 124 */     return b[offset] << 24 | (b[(offset + 1)] & 0xFF) << 16 | (b[(offset + 2)] & 0xFF) << 8 | b[(offset + 3)] & 0xFF;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\UUIDDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */