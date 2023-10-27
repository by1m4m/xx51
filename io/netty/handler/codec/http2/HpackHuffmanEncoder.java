/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ final class HpackHuffmanEncoder
/*     */ {
/*     */   private final int[] codes;
/*     */   private final byte[] lengths;
/*  44 */   private final EncodedLengthProcessor encodedLengthProcessor = new EncodedLengthProcessor(null);
/*  45 */   private final EncodeProcessor encodeProcessor = new EncodeProcessor(null);
/*     */   
/*     */   HpackHuffmanEncoder() {
/*  48 */     this(HpackUtil.HUFFMAN_CODES, HpackUtil.HUFFMAN_CODE_LENGTHS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HpackHuffmanEncoder(int[] codes, byte[] lengths)
/*     */   {
/*  58 */     this.codes = codes;
/*  59 */     this.lengths = lengths;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void encode(ByteBuf out, CharSequence data)
/*     */   {
/*  69 */     ObjectUtil.checkNotNull(out, "out");
/*  70 */     if ((data instanceof AsciiString)) {
/*  71 */       AsciiString string = (AsciiString)data;
/*     */       try {
/*  73 */         this.encodeProcessor.out = out;
/*  74 */         string.forEachByte(this.encodeProcessor);
/*     */       } catch (Exception e) {
/*  76 */         PlatformDependent.throwException(e);
/*     */       } finally {
/*  78 */         this.encodeProcessor.end();
/*     */       }
/*     */     } else {
/*  81 */       encodeSlowPath(out, data);
/*     */     }
/*     */   }
/*     */   
/*     */   private void encodeSlowPath(ByteBuf out, CharSequence data) {
/*  86 */     long current = 0L;
/*  87 */     int n = 0;
/*     */     
/*  89 */     for (int i = 0; i < data.length(); i++) {
/*  90 */       int b = data.charAt(i) & 0xFF;
/*  91 */       int code = this.codes[b];
/*  92 */       int nbits = this.lengths[b];
/*     */       
/*  94 */       current <<= nbits;
/*  95 */       current |= code;
/*  96 */       n += nbits;
/*     */       
/*  98 */       while (n >= 8) {
/*  99 */         n -= 8;
/* 100 */         out.writeByte((int)(current >> n));
/*     */       }
/*     */     }
/*     */     
/* 104 */     if (n > 0) {
/* 105 */       current <<= 8 - n;
/* 106 */       current |= 255 >>> n;
/* 107 */       out.writeByte((int)current);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int getEncodedLength(CharSequence data)
/*     */   {
/* 118 */     if ((data instanceof AsciiString)) {
/* 119 */       AsciiString string = (AsciiString)data;
/*     */       try {
/* 121 */         this.encodedLengthProcessor.reset();
/* 122 */         string.forEachByte(this.encodedLengthProcessor);
/* 123 */         return this.encodedLengthProcessor.length();
/*     */       } catch (Exception e) {
/* 125 */         PlatformDependent.throwException(e);
/* 126 */         return -1;
/*     */       }
/*     */     }
/* 129 */     return getEncodedLengthSlowPath(data);
/*     */   }
/*     */   
/*     */   private int getEncodedLengthSlowPath(CharSequence data)
/*     */   {
/* 134 */     long len = 0L;
/* 135 */     for (int i = 0; i < data.length(); i++) {
/* 136 */       len += this.lengths[(data.charAt(i) & 0xFF)];
/*     */     }
/* 138 */     return (int)(len + 7L >> 3);
/*     */   }
/*     */   
/*     */   private final class EncodeProcessor implements ByteProcessor {
/*     */     ByteBuf out;
/*     */     private long current;
/*     */     private int n;
/*     */     
/*     */     private EncodeProcessor() {}
/*     */     
/* 148 */     public boolean process(byte value) { int b = value & 0xFF;
/* 149 */       int nbits = HpackHuffmanEncoder.this.lengths[b];
/*     */       
/* 151 */       this.current <<= nbits;
/* 152 */       this.current |= HpackHuffmanEncoder.this.codes[b];
/* 153 */       this.n += nbits;
/*     */       
/* 155 */       while (this.n >= 8) {
/* 156 */         this.n -= 8;
/* 157 */         this.out.writeByte((int)(this.current >> this.n));
/*     */       }
/* 159 */       return true;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     void end()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 7	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:n	I
/*     */       //   4: ifle +50 -> 54
/*     */       //   7: aload_0
/*     */       //   8: dup
/*     */       //   9: getfield 5	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:current	J
/*     */       //   12: bipush 8
/*     */       //   14: aload_0
/*     */       //   15: getfield 7	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:n	I
/*     */       //   18: isub
/*     */       //   19: lshl
/*     */       //   20: putfield 5	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:current	J
/*     */       //   23: aload_0
/*     */       //   24: dup
/*     */       //   25: getfield 5	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:current	J
/*     */       //   28: sipush 255
/*     */       //   31: aload_0
/*     */       //   32: getfield 7	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:n	I
/*     */       //   35: iushr
/*     */       //   36: i2l
/*     */       //   37: lor
/*     */       //   38: putfield 5	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:current	J
/*     */       //   41: aload_0
/*     */       //   42: getfield 8	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:out	Lio/netty/buffer/ByteBuf;
/*     */       //   45: aload_0
/*     */       //   46: getfield 5	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:current	J
/*     */       //   49: l2i
/*     */       //   50: invokevirtual 9	io/netty/buffer/ByteBuf:writeByte	(I)Lio/netty/buffer/ByteBuf;
/*     */       //   53: pop
/*     */       //   54: aload_0
/*     */       //   55: aconst_null
/*     */       //   56: putfield 8	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:out	Lio/netty/buffer/ByteBuf;
/*     */       //   59: aload_0
/*     */       //   60: lconst_0
/*     */       //   61: putfield 5	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:current	J
/*     */       //   64: aload_0
/*     */       //   65: iconst_0
/*     */       //   66: putfield 7	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:n	I
/*     */       //   69: goto +21 -> 90
/*     */       //   72: astore_1
/*     */       //   73: aload_0
/*     */       //   74: aconst_null
/*     */       //   75: putfield 8	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:out	Lio/netty/buffer/ByteBuf;
/*     */       //   78: aload_0
/*     */       //   79: lconst_0
/*     */       //   80: putfield 5	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:current	J
/*     */       //   83: aload_0
/*     */       //   84: iconst_0
/*     */       //   85: putfield 7	io/netty/handler/codec/http2/HpackHuffmanEncoder$EncodeProcessor:n	I
/*     */       //   88: aload_1
/*     */       //   89: athrow
/*     */       //   90: return
/*     */       // Line number table:
/*     */       //   Java source line #164	-> byte code offset #0
/*     */       //   Java source line #165	-> byte code offset #7
/*     */       //   Java source line #166	-> byte code offset #23
/*     */       //   Java source line #167	-> byte code offset #41
/*     */       //   Java source line #170	-> byte code offset #54
/*     */       //   Java source line #171	-> byte code offset #59
/*     */       //   Java source line #172	-> byte code offset #64
/*     */       //   Java source line #173	-> byte code offset #69
/*     */       //   Java source line #170	-> byte code offset #72
/*     */       //   Java source line #171	-> byte code offset #78
/*     */       //   Java source line #172	-> byte code offset #83
/*     */       //   Java source line #173	-> byte code offset #88
/*     */       //   Java source line #174	-> byte code offset #90
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	91	0	this	EncodeProcessor
/*     */       //   72	17	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	54	72	finally
/*     */     }
/*     */   }
/*     */   
/*     */   private final class EncodedLengthProcessor
/*     */     implements ByteProcessor
/*     */   {
/*     */     private long len;
/*     */     
/*     */     private EncodedLengthProcessor() {}
/*     */     
/*     */     public boolean process(byte value)
/*     */     {
/* 182 */       this.len += HpackHuffmanEncoder.this.lengths[(value & 0xFF)];
/* 183 */       return true;
/*     */     }
/*     */     
/*     */     void reset() {
/* 187 */       this.len = 0L;
/*     */     }
/*     */     
/*     */     int length() {
/* 191 */       return (int)(this.len + 7L >> 3);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\HpackHuffmanEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */