/*     */ package com.maxmind.db;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.BigIntegerNode;
/*     */ import com.fasterxml.jackson.databind.node.BinaryNode;
/*     */ import com.fasterxml.jackson.databind.node.BooleanNode;
/*     */ import com.fasterxml.jackson.databind.node.DoubleNode;
/*     */ import com.fasterxml.jackson.databind.node.FloatNode;
/*     */ import com.fasterxml.jackson.databind.node.IntNode;
/*     */ import com.fasterxml.jackson.databind.node.LongNode;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.node.TextNode;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ final class Decoder
/*     */ {
/*  20 */   boolean POINTER_TEST_HACK = false;
/*     */   private final long pointerBase;
/*     */   private final ObjectMapper objectMapper;
/*     */   private final ByteBuffer buffer;
/*     */   
/*     */   static enum Type {
/*     */     private Type() {}
/*     */     
/*  28 */     EXTENDED,  POINTER,  UTF8_STRING,  DOUBLE,  BYTES,  UINT16,  UINT32,  MAP,  INT32,  UINT64,  UINT128,  ARRAY,  CONTAINER,  END_MARKER,  BOOLEAN,  FLOAT;
/*     */     
/*     */ 
/*     */ 
/*  32 */     static final Type[] values = values();
/*     */     
/*     */     public static Type get(int i) {
/*  35 */       return values[i];
/*     */     }
/*     */     
/*     */     private static Type get(byte b)
/*     */     {
/*  40 */       return get(b & 0xFF);
/*     */     }
/*     */     
/*     */     public static Type fromControlByte(int b)
/*     */     {
/*  45 */       return get((byte)((0xFF & b) >>> 5));
/*     */     }
/*     */   }
/*     */   
/*     */   static class Result {
/*     */     private final JsonNode node;
/*     */     private int offset;
/*     */     
/*     */     Result(JsonNode node, int offset) {
/*  54 */       this.node = node;
/*  55 */       this.offset = offset;
/*     */     }
/*     */     
/*     */     JsonNode getNode() {
/*  59 */       return this.node;
/*     */     }
/*     */     
/*     */     int getOffset() {
/*  63 */       return this.offset;
/*     */     }
/*     */     
/*     */     void setOffset(int offset) {
/*  67 */       this.offset = offset;
/*     */     }
/*     */   }
/*     */   
/*     */   Decoder(ByteBuffer buffer, long pointerBase)
/*     */   {
/*  73 */     this.pointerBase = pointerBase;
/*  74 */     this.buffer = buffer;
/*  75 */     this.objectMapper = new ObjectMapper();
/*     */   }
/*     */   
/*     */   Result decode(int offset) throws IOException {
/*  79 */     if (offset >= this.buffer.capacity()) {
/*  80 */       throw new InvalidDatabaseException("The MaxMind DB file's data section contains bad data: pointer larger than the database.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  85 */     this.buffer.position(offset);
/*  86 */     int ctrlByte = 0xFF & this.buffer.get();
/*  87 */     offset++;
/*     */     
/*  89 */     Type type = Type.fromControlByte(ctrlByte);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  94 */     if (type.equals(Type.POINTER)) {
/*  95 */       Result pointer = decodePointer(ctrlByte, offset);
/*     */       
/*     */ 
/*  98 */       if (this.POINTER_TEST_HACK) {
/*  99 */         return pointer;
/*     */       }
/* 101 */       Result result = decode(pointer.getNode().asInt());
/* 102 */       result.setOffset(pointer.getOffset());
/* 103 */       return result;
/*     */     }
/*     */     
/* 106 */     if (type.equals(Type.EXTENDED)) {
/* 107 */       int nextByte = this.buffer.get();
/*     */       
/* 109 */       int typeNum = nextByte + 7;
/*     */       
/* 111 */       if (typeNum < 8) {
/* 112 */         throw new InvalidDatabaseException("Something went horribly wrong in the decoder. An extended type resolved to a type number < 8 (" + typeNum + ")");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 118 */       type = Type.get(typeNum);
/* 119 */       offset++;
/*     */     }
/*     */     
/* 122 */     int[] sizeArray = sizeFromCtrlByte(ctrlByte, offset);
/* 123 */     int size = sizeArray[0];
/* 124 */     offset = sizeArray[1];
/*     */     
/* 126 */     return decodeByType(type, offset, size);
/*     */   }
/*     */   
/*     */ 
/*     */   private Result decodeByType(Type type, int offset, int size)
/*     */     throws IOException
/*     */   {
/* 133 */     int newOffset = offset + size;
/* 134 */     switch (type) {
/*     */     case MAP: 
/* 136 */       return decodeMap(size, offset);
/*     */     case ARRAY: 
/* 138 */       return decodeArray(size, offset);
/*     */     case BOOLEAN: 
/* 140 */       return new Result(decodeBoolean(size), offset);
/*     */     case UTF8_STRING: 
/* 142 */       TextNode s = new TextNode(decodeString(size));
/* 143 */       return new Result(s, newOffset);
/*     */     case DOUBLE: 
/* 145 */       return new Result(decodeDouble(size), newOffset);
/*     */     case FLOAT: 
/* 147 */       return new Result(decodeFloat(size), newOffset);
/*     */     case BYTES: 
/* 149 */       BinaryNode b = new BinaryNode(getByteArray(size));
/* 150 */       return new Result(b, newOffset);
/*     */     case UINT16: 
/* 152 */       IntNode i = decodeUint16(size);
/* 153 */       return new Result(i, newOffset);
/*     */     case UINT32: 
/* 155 */       LongNode l = decodeUint32(size);
/* 156 */       return new Result(l, newOffset);
/*     */     case INT32: 
/* 158 */       IntNode int32 = decodeInt32(size);
/* 159 */       return new Result(int32, newOffset);
/*     */     case UINT64: 
/* 161 */       BigIntegerNode bi = decodeBigInteger(size);
/* 162 */       return new Result(bi, newOffset);
/*     */     case UINT128: 
/* 164 */       BigIntegerNode uint128 = decodeBigInteger(size);
/* 165 */       return new Result(uint128, newOffset);
/*     */     }
/*     */     
/* 168 */     throw new InvalidDatabaseException("Unknown or unexpected type: " + type.name());
/*     */   }
/*     */   
/*     */ 
/* 172 */   private final int[] pointerValueOffset = { 0, 0, 2048, 526336, 0 };
/*     */   
/*     */   private Result decodePointer(int ctrlByte, int offset)
/*     */   {
/* 176 */     int pointerSize = (ctrlByte >>> 3 & 0x3) + 1;
/* 177 */     int base = pointerSize == 4 ? 0 : (byte)(ctrlByte & 0x7);
/* 178 */     int packed = decodeInteger(base, pointerSize);
/* 179 */     long pointer = packed + this.pointerBase + this.pointerValueOffset[pointerSize];
/*     */     
/*     */ 
/* 182 */     return new Result(new LongNode(pointer), offset + pointerSize);
/*     */   }
/*     */   
/*     */   private String decodeString(int size) {
/* 186 */     ByteBuffer buffer = this.buffer.slice();
/* 187 */     buffer.limit(size);
/* 188 */     return Charset.forName("UTF-8").decode(buffer).toString();
/*     */   }
/*     */   
/*     */   private IntNode decodeUint16(int size) {
/* 192 */     return new IntNode(decodeInteger(size));
/*     */   }
/*     */   
/*     */   private IntNode decodeInt32(int size) {
/* 196 */     return new IntNode(decodeInteger(size));
/*     */   }
/*     */   
/*     */   private long decodeLong(int size) {
/* 200 */     long integer = 0L;
/* 201 */     for (int i = 0; i < size; i++) {
/* 202 */       integer = integer << 8 | this.buffer.get() & 0xFF;
/*     */     }
/* 204 */     return integer;
/*     */   }
/*     */   
/*     */   private LongNode decodeUint32(int size) {
/* 208 */     return new LongNode(decodeLong(size));
/*     */   }
/*     */   
/*     */   private int decodeInteger(int size) {
/* 212 */     return decodeInteger(0, size);
/*     */   }
/*     */   
/*     */   private int decodeInteger(int base, int size) {
/* 216 */     return decodeInteger(this.buffer, base, size);
/*     */   }
/*     */   
/*     */   static int decodeInteger(ByteBuffer buffer, int base, int size) {
/* 220 */     int integer = base;
/* 221 */     for (int i = 0; i < size; i++) {
/* 222 */       integer = integer << 8 | buffer.get() & 0xFF;
/*     */     }
/* 224 */     return integer;
/*     */   }
/*     */   
/*     */   private BigIntegerNode decodeBigInteger(int size) {
/* 228 */     byte[] bytes = getByteArray(size);
/* 229 */     return new BigIntegerNode(new java.math.BigInteger(1, bytes));
/*     */   }
/*     */   
/*     */   private DoubleNode decodeDouble(int size) throws InvalidDatabaseException {
/* 233 */     if (size != 8) {
/* 234 */       throw new InvalidDatabaseException("The MaxMind DB file's data section contains bad data: invalid size of double.");
/*     */     }
/*     */     
/*     */ 
/* 238 */     return new DoubleNode(this.buffer.getDouble());
/*     */   }
/*     */   
/*     */   private FloatNode decodeFloat(int size) throws InvalidDatabaseException {
/* 242 */     if (size != 4) {
/* 243 */       throw new InvalidDatabaseException("The MaxMind DB file's data section contains bad data: invalid size of float.");
/*     */     }
/*     */     
/*     */ 
/* 247 */     return new FloatNode(this.buffer.getFloat());
/*     */   }
/*     */   
/*     */   private static BooleanNode decodeBoolean(int size) throws InvalidDatabaseException
/*     */   {
/* 252 */     switch (size) {
/*     */     case 0: 
/* 254 */       return BooleanNode.FALSE;
/*     */     case 1: 
/* 256 */       return BooleanNode.TRUE;
/*     */     }
/* 258 */     throw new InvalidDatabaseException("The MaxMind DB file's data section contains bad data: invalid size of boolean.");
/*     */   }
/*     */   
/*     */ 
/*     */   private Result decodeArray(int size, int offset)
/*     */     throws IOException
/*     */   {
/* 265 */     com.fasterxml.jackson.databind.node.ArrayNode array = this.objectMapper.createArrayNode();
/*     */     
/* 267 */     for (int i = 0; i < size; i++) {
/* 268 */       Result r = decode(offset);
/* 269 */       offset = r.getOffset();
/* 270 */       array.add(r.getNode());
/*     */     }
/*     */     
/* 273 */     return new Result(array, offset);
/*     */   }
/*     */   
/*     */   private Result decodeMap(int size, int offset) throws IOException {
/* 277 */     ObjectNode map = this.objectMapper.createObjectNode();
/*     */     
/* 279 */     for (int i = 0; i < size; i++) {
/* 280 */       Result keyResult = decode(offset);
/* 281 */       String key = keyResult.getNode().asText();
/* 282 */       offset = keyResult.getOffset();
/*     */       
/* 284 */       Result valueResult = decode(offset);
/* 285 */       JsonNode value = valueResult.getNode();
/* 286 */       offset = valueResult.getOffset();
/*     */       
/* 288 */       map.set(key, value);
/*     */     }
/*     */     
/* 291 */     return new Result(map, offset);
/*     */   }
/*     */   
/*     */   private int[] sizeFromCtrlByte(int ctrlByte, int offset) {
/* 295 */     int size = ctrlByte & 0x1F;
/* 296 */     int bytesToRead = size < 29 ? 0 : size - 28;
/*     */     
/* 298 */     if (size == 29) {
/* 299 */       int i = decodeInteger(bytesToRead);
/* 300 */       size = 29 + i;
/* 301 */     } else if (size == 30) {
/* 302 */       int i = decodeInteger(bytesToRead);
/* 303 */       size = 285 + i;
/* 304 */     } else if (size > 30) {
/* 305 */       int i = decodeInteger(bytesToRead) & 268435455 >>> 32 - 8 * bytesToRead;
/*     */       
/* 307 */       size = 65821 + i;
/*     */     }
/* 309 */     return new int[] { size, offset + bytesToRead };
/*     */   }
/*     */   
/*     */   private byte[] getByteArray(int length) {
/* 313 */     return getByteArray(this.buffer, length);
/*     */   }
/*     */   
/*     */   private static byte[] getByteArray(ByteBuffer buffer, int length) {
/* 317 */     byte[] bytes = new byte[length];
/* 318 */     buffer.get(bytes);
/* 319 */     return bytes;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\db\Decoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */