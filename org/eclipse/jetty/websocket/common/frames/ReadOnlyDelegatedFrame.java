/*     */ package org.eclipse.jetty.websocket.common.frames;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
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
/*     */ public class ReadOnlyDelegatedFrame
/*     */   implements Frame
/*     */ {
/*     */   private final Frame delegate;
/*     */   
/*     */   public ReadOnlyDelegatedFrame(Frame frame)
/*     */   {
/*  34 */     this.delegate = frame;
/*     */   }
/*     */   
/*     */ 
/*     */   public byte[] getMask()
/*     */   {
/*  40 */     return this.delegate.getMask();
/*     */   }
/*     */   
/*     */ 
/*     */   public byte getOpCode()
/*     */   {
/*  46 */     return this.delegate.getOpCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer getPayload()
/*     */   {
/*  52 */     if (!this.delegate.hasPayload()) {
/*  53 */       return null;
/*     */     }
/*  55 */     return this.delegate.getPayload().asReadOnlyBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getPayloadLength()
/*     */   {
/*  61 */     return this.delegate.getPayloadLength();
/*     */   }
/*     */   
/*     */ 
/*     */   public Frame.Type getType()
/*     */   {
/*  67 */     return this.delegate.getType();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasPayload()
/*     */   {
/*  73 */     return this.delegate.hasPayload();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isFin()
/*     */   {
/*  79 */     return this.delegate.isFin();
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public boolean isLast()
/*     */   {
/*  86 */     return this.delegate.isLast();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isMasked()
/*     */   {
/*  92 */     return this.delegate.isMasked();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isRsv1()
/*     */   {
/*  98 */     return this.delegate.isRsv1();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isRsv2()
/*     */   {
/* 104 */     return this.delegate.isRsv2();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isRsv3()
/*     */   {
/* 110 */     return this.delegate.isRsv3();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\frames\ReadOnlyDelegatedFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */