/*     */ package org.eclipse.jetty.websocket.common.frames;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.websocket.api.ProtocolException;
/*     */ import org.eclipse.jetty.websocket.common.WebSocketFrame;
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
/*     */ public abstract class ControlFrame
/*     */   extends WebSocketFrame
/*     */ {
/*     */   public static final int MAX_CONTROL_PAYLOAD = 125;
/*     */   
/*     */   public ControlFrame(byte opcode)
/*     */   {
/*  35 */     super(opcode);
/*     */   }
/*     */   
/*     */ 
/*     */   public void assertValid()
/*     */   {
/*  41 */     if (isControlFrame())
/*     */     {
/*  43 */       if (getPayloadLength() > 125)
/*     */       {
/*  45 */         throw new ProtocolException("Desired payload length [" + getPayloadLength() + "] exceeds maximum control payload length [" + 125 + "]");
/*     */       }
/*     */       
/*     */ 
/*  49 */       if ((this.finRsvOp & 0x80) == 0)
/*     */       {
/*  51 */         throw new ProtocolException("Cannot have FIN==false on Control frames");
/*     */       }
/*     */       
/*  54 */       if ((this.finRsvOp & 0x40) != 0)
/*     */       {
/*  56 */         throw new ProtocolException("Cannot have RSV1==true on Control frames");
/*     */       }
/*     */       
/*  59 */       if ((this.finRsvOp & 0x20) != 0)
/*     */       {
/*  61 */         throw new ProtocolException("Cannot have RSV2==true on Control frames");
/*     */       }
/*     */       
/*  64 */       if ((this.finRsvOp & 0x10) != 0)
/*     */       {
/*  66 */         throw new ProtocolException("Cannot have RSV3==true on Control frames");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  74 */     if (this == obj)
/*     */     {
/*  76 */       return true;
/*     */     }
/*  78 */     if (obj == null)
/*     */     {
/*  80 */       return false;
/*     */     }
/*  82 */     if (getClass() != obj.getClass())
/*     */     {
/*  84 */       return false;
/*     */     }
/*  86 */     ControlFrame other = (ControlFrame)obj;
/*  87 */     if (this.data == null)
/*     */     {
/*  89 */       if (other.data != null)
/*     */       {
/*  91 */         return false;
/*     */       }
/*     */     }
/*  94 */     else if (!this.data.equals(other.data))
/*     */     {
/*  96 */       return false;
/*     */     }
/*  98 */     if (this.finRsvOp != other.finRsvOp)
/*     */     {
/* 100 */       return false;
/*     */     }
/* 102 */     if (!Arrays.equals(this.mask, other.mask))
/*     */     {
/* 104 */       return false;
/*     */     }
/* 106 */     if (this.masked != other.masked)
/*     */     {
/* 108 */       return false;
/*     */     }
/* 110 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isControlFrame()
/*     */   {
/* 116 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isDataFrame()
/*     */   {
/* 122 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketFrame setPayload(ByteBuffer buf)
/*     */   {
/* 128 */     if ((buf != null) && (buf.remaining() > 125))
/*     */     {
/* 130 */       throw new ProtocolException("Control Payloads can not exceed 125 bytes in length.");
/*     */     }
/* 132 */     return super.setPayload(buf);
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer getPayload()
/*     */   {
/* 138 */     if (super.getPayload() == null)
/*     */     {
/* 140 */       return BufferUtil.EMPTY_BUFFER;
/*     */     }
/* 142 */     return super.getPayload();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\frames\ControlFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */