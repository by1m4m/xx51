/*     */ package com.fasterxml.jackson.core.io;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonEncoding;
/*     */ import com.fasterxml.jackson.core.util.BufferRecycler;
/*     */ import com.fasterxml.jackson.core.util.TextBuffer;
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
/*     */ public class IOContext
/*     */ {
/*     */   protected final Object _sourceRef;
/*     */   protected JsonEncoding _encoding;
/*     */   protected final boolean _managedResource;
/*     */   protected final BufferRecycler _bufferRecycler;
/*  59 */   protected byte[] _readIOBuffer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */   protected byte[] _writeEncodingBuffer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   protected byte[] _base64Buffer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   protected char[] _tokenCBuffer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   protected char[] _concatCBuffer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   protected char[] _nameCopyBuffer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IOContext(BufferRecycler br, Object sourceRef, boolean managedResource)
/*     */   {
/* 103 */     this._bufferRecycler = br;
/* 104 */     this._sourceRef = sourceRef;
/* 105 */     this._managedResource = managedResource;
/*     */   }
/*     */   
/*     */   public void setEncoding(JsonEncoding enc) {
/* 109 */     this._encoding = enc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */   public Object getSourceReference() { return this._sourceRef; }
/* 119 */   public JsonEncoding getEncoding() { return this._encoding; }
/* 120 */   public boolean isResourceManaged() { return this._managedResource; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TextBuffer constructTextBuffer()
/*     */   {
/* 129 */     return new TextBuffer(this._bufferRecycler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] allocReadIOBuffer()
/*     */   {
/* 138 */     _verifyAlloc(this._readIOBuffer);
/* 139 */     return this._readIOBuffer = this._bufferRecycler.allocByteBuffer(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public byte[] allocReadIOBuffer(int minSize)
/*     */   {
/* 146 */     _verifyAlloc(this._readIOBuffer);
/* 147 */     return this._readIOBuffer = this._bufferRecycler.allocByteBuffer(0, minSize);
/*     */   }
/*     */   
/*     */   public byte[] allocWriteEncodingBuffer() {
/* 151 */     _verifyAlloc(this._writeEncodingBuffer);
/* 152 */     return this._writeEncodingBuffer = this._bufferRecycler.allocByteBuffer(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public byte[] allocWriteEncodingBuffer(int minSize)
/*     */   {
/* 159 */     _verifyAlloc(this._writeEncodingBuffer);
/* 160 */     return this._writeEncodingBuffer = this._bufferRecycler.allocByteBuffer(1, minSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public byte[] allocBase64Buffer()
/*     */   {
/* 167 */     _verifyAlloc(this._base64Buffer);
/* 168 */     return this._base64Buffer = this._bufferRecycler.allocByteBuffer(3);
/*     */   }
/*     */   
/*     */   public char[] allocTokenBuffer() {
/* 172 */     _verifyAlloc(this._tokenCBuffer);
/* 173 */     return this._tokenCBuffer = this._bufferRecycler.allocCharBuffer(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public char[] allocTokenBuffer(int minSize)
/*     */   {
/* 180 */     _verifyAlloc(this._tokenCBuffer);
/* 181 */     return this._tokenCBuffer = this._bufferRecycler.allocCharBuffer(0, minSize);
/*     */   }
/*     */   
/*     */   public char[] allocConcatBuffer() {
/* 185 */     _verifyAlloc(this._concatCBuffer);
/* 186 */     return this._concatCBuffer = this._bufferRecycler.allocCharBuffer(1);
/*     */   }
/*     */   
/*     */   public char[] allocNameCopyBuffer(int minSize) {
/* 190 */     _verifyAlloc(this._nameCopyBuffer);
/* 191 */     return this._nameCopyBuffer = this._bufferRecycler.allocCharBuffer(3, minSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void releaseReadIOBuffer(byte[] buf)
/*     */   {
/* 199 */     if (buf != null)
/*     */     {
/*     */ 
/*     */ 
/* 203 */       _verifyRelease(buf, this._readIOBuffer);
/* 204 */       this._readIOBuffer = null;
/* 205 */       this._bufferRecycler.releaseByteBuffer(0, buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseWriteEncodingBuffer(byte[] buf) {
/* 210 */     if (buf != null)
/*     */     {
/*     */ 
/*     */ 
/* 214 */       _verifyRelease(buf, this._writeEncodingBuffer);
/* 215 */       this._writeEncodingBuffer = null;
/* 216 */       this._bufferRecycler.releaseByteBuffer(1, buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseBase64Buffer(byte[] buf) {
/* 221 */     if (buf != null) {
/* 222 */       _verifyRelease(buf, this._base64Buffer);
/* 223 */       this._base64Buffer = null;
/* 224 */       this._bufferRecycler.releaseByteBuffer(3, buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseTokenBuffer(char[] buf) {
/* 229 */     if (buf != null) {
/* 230 */       _verifyRelease(buf, this._tokenCBuffer);
/* 231 */       this._tokenCBuffer = null;
/* 232 */       this._bufferRecycler.releaseCharBuffer(0, buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseConcatBuffer(char[] buf) {
/* 237 */     if (buf != null)
/*     */     {
/* 239 */       _verifyRelease(buf, this._concatCBuffer);
/* 240 */       this._concatCBuffer = null;
/* 241 */       this._bufferRecycler.releaseCharBuffer(1, buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseNameCopyBuffer(char[] buf) {
/* 246 */     if (buf != null)
/*     */     {
/* 248 */       _verifyRelease(buf, this._nameCopyBuffer);
/* 249 */       this._nameCopyBuffer = null;
/* 250 */       this._bufferRecycler.releaseCharBuffer(3, buf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void _verifyAlloc(Object buffer)
/*     */   {
/* 261 */     if (buffer != null) throw new IllegalStateException("Trying to call same allocXxx() method second time");
/*     */   }
/*     */   
/*     */   protected final void _verifyRelease(byte[] toRelease, byte[] src) {
/* 265 */     if ((toRelease != src) && (toRelease.length <= src.length)) throw wrongBuf();
/*     */   }
/*     */   
/*     */   protected final void _verifyRelease(char[] toRelease, char[] src) {
/* 269 */     if ((toRelease != src) && (toRelease.length <= src.length)) throw wrongBuf();
/*     */   }
/*     */   
/* 272 */   private IllegalArgumentException wrongBuf() { return new IllegalArgumentException("Trying to release buffer not owned by the context"); }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\io\IOContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */