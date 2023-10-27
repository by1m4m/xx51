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
/*     */ public abstract class Tag
/*     */   implements Asn1Constants
/*     */ {
/*  26 */   public String name = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  31 */   public boolean optional = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  37 */   public boolean constructed = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  42 */   public int tagClass = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  47 */   public int tagNumber = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   public int tagType = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */   public boolean explicit = false;
/*     */   
/*     */   public Tag(String name) {
/*  61 */     this.name = name;
/*     */   }
/*     */   
/*     */   public static final String tagTypeOrNumberToString(int tagClass, int tagTypeOrNumber) {
/*  65 */     switch (tagClass) {
/*     */     case 0: 
/*  67 */       switch (tagTypeOrNumber) {
/*     */       case 0: 
/*  69 */         return "EOF";
/*     */       case 1: 
/*  71 */         return "BOOLEAN";
/*     */       case 2: 
/*  73 */         return "INTEGER";
/*     */       case 3: 
/*  75 */         return "BIT_STRING";
/*     */       case 4: 
/*  77 */         return "OCTET_STRING";
/*     */       case 5: 
/*  79 */         return "NULL";
/*     */       case 6: 
/*  81 */         return "OBJECT_ID";
/*     */       case 9: 
/*  83 */         return "REAL";
/*     */       case 10: 
/*  85 */         return "ENUMERATED";
/*     */       case 16: 
/*  87 */         return "SEQUENCE";
/*     */       case 17: 
/*  89 */         return "SET";
/*     */       case 18: 
/*  91 */         return "NUMERIC_STRING";
/*     */       case 19: 
/*  93 */         return "PRINTABLE_STRING";
/*     */       case 20: 
/*  95 */         return "TELETEX_STRING";
/*     */       case 21: 
/*  97 */         return "VIDEOTEXT_STRING";
/*     */       case 22: 
/*  99 */         return "IA5_STRING";
/*     */       case 23: 
/* 101 */         return "UTCTIME";
/*     */       case 24: 
/* 103 */         return "GENERAL_TIME";
/*     */       case 25: 
/* 105 */         return "GRAPHIC_STRING";
/*     */       case 26: 
/* 107 */         return "VISIBLE_STRING";
/*     */       case 27: 
/* 109 */         return "GENERAL_STRING";
/*     */       case 31: 
/* 111 */         return "EXTENDED_TYPE (multibyte)";
/*     */       }
/* 113 */       return "UNKNOWN(" + tagTypeOrNumber + ")";
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 118 */     return "[" + tagTypeOrNumber + "]";
/*     */   }
/*     */   
/*     */   public static final String tagClassToString(int tagClass)
/*     */   {
/* 123 */     switch (tagClass) {
/*     */     case 0: 
/* 125 */       return "UNIVERSAL";
/*     */     case 128: 
/* 127 */       return "CONTEXT";
/*     */     case 64: 
/* 129 */       return "APPLICATION";
/*     */     case 192: 
/* 131 */       return "PRIVATE";
/*     */     }
/* 133 */     return "UNKNOWN";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeTag(ByteBuffer buf)
/*     */   {
/* 142 */     if (!isMustBeWritten()) {
/* 143 */       return;
/*     */     }
/*     */     
/* 146 */     if (this.explicit)
/*     */     {
/*     */ 
/* 149 */       BerType berTagPrefix = new BerType(this.tagClass, true, this.tagNumber);
/* 150 */       writeBerType(buf, berTagPrefix);
/*     */       
/*     */ 
/* 153 */       buf.writeBerLength(calculateLength());
/*     */       
/*     */ 
/* 156 */       writeTagValue(buf);
/*     */     }
/*     */     else {
/* 159 */       writeTagValue(buf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMustBeWritten()
/*     */   {
/* 168 */     return (!this.optional) || (isValueSet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isValueSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long calculateFullLength()
/*     */   {
/* 181 */     if (!isMustBeWritten()) {
/* 182 */       return 0L;
/*     */     }
/*     */     
/* 185 */     long length = calculateLength();
/*     */     
/* 187 */     if (!this.explicit)
/*     */     {
/* 189 */       length += calculateLengthOfTagTypeOrTagNumber(this.tagType) + calculateLengthOfLength(length);
/*     */     }
/*     */     else {
/* 192 */       length += calculateLengthOfTagTypeOrTagNumber(this.tagNumber) + calculateLengthOfLength(length);
/*     */     }
/*     */     
/* 195 */     return length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long calculateLength()
/*     */   {
/* 203 */     if (!isMustBeWritten()) {
/* 204 */       return 0L;
/*     */     }
/*     */     
/* 207 */     long length = calculateLengthOfValuePayload();
/*     */     
/* 209 */     if (this.explicit)
/*     */     {
/* 211 */       length += calculateLengthOfTagTypeOrTagNumber(this.tagType) + calculateLengthOfLength(length);
/*     */     }
/*     */     
/* 214 */     return length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int calculateLengthOfLength(long length)
/*     */   {
/* 221 */     if (length < 0L) {
/* 222 */       throw new RuntimeException("[" + this + "] ERROR: Length of tag cannot be less than zero: " + length + ".");
/*     */     }
/* 224 */     if (length <= 127L)
/* 225 */       return 1;
/* 226 */     if (length <= 255L)
/* 227 */       return 2;
/* 228 */     if (length <= 65535L)
/* 229 */       return 3;
/* 230 */     if (length <= 16777215L)
/* 231 */       return 4;
/* 232 */     if (length <= 4294967295L)
/* 233 */       return 5;
/* 234 */     if (length <= 1099511627775L)
/* 235 */       return 6;
/* 236 */     if (length <= 281474976710655L)
/* 237 */       return 7;
/* 238 */     if (length <= 72057594037927935L) {
/* 239 */       return 8;
/*     */     }
/* 241 */     return 9;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int calculateLengthOfTagTypeOrTagNumber(int tagType)
/*     */   {
/* 252 */     if (tagType >= 31) {
/* 253 */       throw new RuntimeException("Multibyte tag types are not supported yet.");
/*     */     }
/* 255 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract long calculateLengthOfValuePayload();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeTagValue(ByteBuffer buf)
/*     */   {
/* 272 */     BerType valueType = new BerType(0, this.constructed, this.tagType);
/* 273 */     writeBerType(buf, valueType);
/*     */     
/*     */ 
/* 276 */     long lengthOfPayload = calculateLengthOfValuePayload();
/* 277 */     buf.writeBerLength(lengthOfPayload);
/*     */     
/*     */ 
/*     */ 
/* 281 */     int storedCursor = buf.cursor;
/*     */     
/*     */ 
/* 284 */     writeTagValuePayload(buf);
/*     */     
/*     */ 
/* 287 */     int actualLength = buf.cursor - storedCursor;
/* 288 */     if (actualLength != lengthOfPayload) {
/* 289 */       throw new RuntimeException("[" + this + "] ERROR: Unexpected length of data in buffer. Expected " + lengthOfPayload + " of bytes of payload, but " + actualLength + " bytes are written instead. Data: " + buf + ".");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void writeTagValuePayload(ByteBuffer paramByteBuffer);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readTag(ByteBuffer buf)
/*     */   {
/* 305 */     BerType typeAndFlags = readBerType(buf);
/*     */     
/*     */ 
/*     */ 
/* 309 */     if (!isTypeValid(typeAndFlags)) {
/* 310 */       throw new RuntimeException("[" + this + "] Unexpected type: " + typeAndFlags + ".");
/*     */     }
/* 312 */     readTag(buf, typeAndFlags);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readTag(ByteBuffer buf, BerType typeAndFlags)
/*     */   {
/* 320 */     if (this.explicit) {
/* 321 */       long length = buf.readBerLength();
/*     */       
/* 323 */       if (length > buf.length) {
/* 324 */         throw new RuntimeException("BER value is too long: " + length + " bytes. Data: " + buf + ".");
/*     */       }
/* 326 */       ByteBuffer value = buf.readBytes((int)length);
/*     */       
/* 328 */       readTagValue(value);
/*     */       
/* 330 */       value.unref();
/*     */     }
/*     */     else {
/* 333 */       readTagValue(buf, typeAndFlags);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readTagValue(ByteBuffer value)
/*     */   {
/* 341 */     BerType typeAndFlags = readBerType(value);
/*     */     
/*     */ 
/*     */ 
/* 345 */     if (!isTypeValid(typeAndFlags, false)) {
/* 346 */       throw new RuntimeException("[" + this + "] Unexpected type: " + typeAndFlags + ".");
/*     */     }
/* 348 */     readTagValue(value, typeAndFlags);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean isTypeValid(BerType typeAndFlags)
/*     */   {
/* 355 */     return isTypeValid(typeAndFlags, this.explicit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTypeValid(BerType typeAndFlags, boolean explicit)
/*     */   {
/* 364 */     if (explicit) {
/* 365 */       return (typeAndFlags.tagClass == this.tagClass) && (typeAndFlags.constructed) && (typeAndFlags.typeOrTagNumber == this.tagNumber);
/*     */     }
/* 367 */     return (typeAndFlags.tagClass == 0) && (!typeAndFlags.constructed) && (typeAndFlags.typeOrTagNumber == this.tagType);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 372 */     return 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 380 */       "  \nTag [name=" + this.name + (this.constructed ? ", constructed=" + this.constructed : "") + ", tagType=" + tagTypeOrNumberToString(0, this.tagType) + (this.explicit ? ", explicit=" + this.explicit + ", optional=" + this.optional + ", tagClass=" + tagClassToString(this.tagClass) + ", tagNumber=" + tagTypeOrNumberToString(this.tagClass, this.tagNumber) : "") + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BerType readBerType(ByteBuffer buf)
/*     */   {
/* 387 */     int typeAndFlags = buf.readUnsignedByte();
/*     */     
/* 389 */     int tagClass = typeAndFlags & 0xC0;
/*     */     
/* 391 */     boolean constructed = (typeAndFlags & 0x20) != 0;
/*     */     
/* 393 */     int type = typeAndFlags & 0x1F;
/* 394 */     if (type == 31) {
/* 395 */       throw new RuntimeException("Extended tag types/numbers (31+) are not supported yet.");
/*     */     }
/* 397 */     return new BerType(tagClass, constructed, type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeBerType(ByteBuffer buf, BerType berType)
/*     */   {
/* 405 */     if ((berType.typeOrTagNumber >= 31) || (berType.typeOrTagNumber < 0)) {
/* 406 */       throw new RuntimeException("Extended tag types/numbers (31+) are not supported yet: " + berType + ".");
/*     */     }
/* 408 */     if ((berType.tagClass & 0xC0) != berType.tagClass) {
/* 409 */       throw new RuntimeException("Value of BER tag class is out of range: " + berType.tagClass + ". Expected values: " + 0 + ", " + 128 + ", " + 64 + ", " + 192 + ".");
/*     */     }
/*     */     
/* 412 */     int typeAndFlags = berType.tagClass | (berType.constructed ? 32 : 0) | berType.typeOrTagNumber;
/*     */     
/* 414 */     buf.writeByte(typeAndFlags);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void readTagValue(ByteBuffer paramByteBuffer, BerType paramBerType);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Tag deepCopy(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tag deepCopy(int index)
/*     */   {
/* 440 */     return deepCopy("[" + index + "]");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tag copyFrom(Tag tag)
/*     */   {
/* 449 */     this.constructed = tag.constructed;
/* 450 */     this.explicit = tag.explicit;
/* 451 */     this.optional = tag.optional;
/* 452 */     this.tagClass = tag.tagClass;
/* 453 */     this.tagNumber = tag.tagNumber;
/* 454 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\common\asn1\Tag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */