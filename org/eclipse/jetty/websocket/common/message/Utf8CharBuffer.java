/*     */ package org.eclipse.jetty.websocket.common.message;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.Utf8Appendable;
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
/*     */ public class Utf8CharBuffer
/*     */   extends Utf8Appendable
/*     */ {
/*     */   private final CharBuffer buffer;
/*     */   
/*     */   public static Utf8CharBuffer wrap(ByteBuffer buffer)
/*     */   {
/*  46 */     return new Utf8CharBuffer(buffer.asCharBuffer());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Utf8CharBuffer(CharBuffer buffer)
/*     */   {
/*  53 */     super(buffer);
/*  54 */     this.buffer = buffer;
/*     */   }
/*     */   
/*     */   public void append(char[] cbuf, int offset, int size)
/*     */   {
/*  59 */     append(BufferUtil.toDirectBuffer(new String(cbuf, offset, size), StandardCharsets.UTF_8));
/*     */   }
/*     */   
/*     */   public void append(int c)
/*     */   {
/*  64 */     this.buffer.append((char)c);
/*     */   }
/*     */   
/*     */ 
/*     */   public void reset()
/*     */   {
/*  70 */     clear();
/*  71 */     super.reset();
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/*  76 */     this.buffer.position(0);
/*  77 */     this.buffer.limit(this.buffer.capacity());
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer getByteBuffer()
/*     */   {
/*  83 */     int limit = this.buffer.limit();
/*  84 */     int position = this.buffer.position();
/*     */     
/*     */ 
/*  87 */     this.buffer.limit(this.buffer.position());
/*  88 */     this.buffer.position(0);
/*     */     
/*     */ 
/*  91 */     ByteBuffer bb = StandardCharsets.UTF_8.encode(this.buffer);
/*     */     
/*     */ 
/*  94 */     this.buffer.limit(limit);
/*  95 */     this.buffer.position(position);
/*     */     
/*  97 */     return bb;
/*     */   }
/*     */   
/*     */ 
/*     */   public int length()
/*     */   {
/* 103 */     return this.buffer.capacity();
/*     */   }
/*     */   
/*     */   public int remaining()
/*     */   {
/* 108 */     return this.buffer.remaining();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPartialString()
/*     */   {
/* 115 */     return this.buffer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 121 */     StringBuilder str = new StringBuilder();
/* 122 */     str.append("Utf8CharBuffer@").append(hashCode());
/* 123 */     str.append("[p=").append(this.buffer.position());
/* 124 */     str.append(",l=").append(this.buffer.limit());
/* 125 */     str.append(",c=").append(this.buffer.capacity());
/* 126 */     str.append(",r=").append(this.buffer.remaining());
/* 127 */     str.append("]");
/* 128 */     return str.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\message\Utf8CharBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */