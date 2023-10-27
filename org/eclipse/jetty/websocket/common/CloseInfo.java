/*     */ package org.eclipse.jetty.websocket.common;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.Utf8Appendable.NotUtf8Exception;
/*     */ import org.eclipse.jetty.util.Utf8StringBuilder;
/*     */ import org.eclipse.jetty.websocket.api.BadPayloadException;
/*     */ import org.eclipse.jetty.websocket.api.ProtocolException;
/*     */ import org.eclipse.jetty.websocket.api.StatusCode;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.common.frames.CloseFrame;
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
/*     */ public class CloseInfo
/*     */ {
/*  36 */   private int statusCode = 0;
/*     */   private byte[] reasonBytes;
/*     */   
/*     */   public CloseInfo()
/*     */   {
/*  41 */     this(1005, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CloseInfo(ByteBuffer payload, boolean validate)
/*     */   {
/*  52 */     this.statusCode = 1005;
/*     */     
/*  54 */     if ((payload == null) || (payload.remaining() == 0))
/*     */     {
/*  56 */       return;
/*     */     }
/*     */     
/*  59 */     ByteBuffer data = payload.slice();
/*  60 */     if ((data.remaining() == 1) && (validate))
/*     */     {
/*  62 */       throw new ProtocolException("Invalid 1 byte payload");
/*     */     }
/*     */     
/*  65 */     if (data.remaining() >= 2)
/*     */     {
/*     */ 
/*  68 */       this.statusCode = 0;
/*  69 */       this.statusCode |= (data.get() & 0xFF) << 8;
/*  70 */       this.statusCode |= data.get() & 0xFF;
/*     */       
/*  72 */       if (validate)
/*     */       {
/*  74 */         assertValidStatusCode(this.statusCode);
/*     */       }
/*     */       
/*  77 */       if (data.remaining() > 0)
/*     */       {
/*     */ 
/*  80 */         int len = Math.min(data.remaining(), 123);
/*  81 */         this.reasonBytes = new byte[len];
/*  82 */         data.get(this.reasonBytes, 0, len);
/*     */         
/*     */ 
/*  85 */         if (validate)
/*     */         {
/*     */           try
/*     */           {
/*  89 */             Utf8StringBuilder utf = new Utf8StringBuilder();
/*     */             
/*  91 */             utf.append(this.reasonBytes, 0, this.reasonBytes.length);
/*     */           }
/*     */           catch (Utf8Appendable.NotUtf8Exception e)
/*     */           {
/*  95 */             throw new BadPayloadException("Invalid Close Reason", e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public CloseInfo(Frame frame)
/*     */   {
/* 104 */     this(frame.getPayload(), false);
/*     */   }
/*     */   
/*     */   public CloseInfo(Frame frame, boolean validate)
/*     */   {
/* 109 */     this(frame.getPayload(), validate);
/*     */   }
/*     */   
/*     */   public CloseInfo(int statusCode)
/*     */   {
/* 114 */     this(statusCode, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CloseInfo(int statusCode, String reason)
/*     */   {
/* 125 */     this.statusCode = statusCode;
/* 126 */     if (reason != null)
/*     */     {
/* 128 */       byte[] utf8Bytes = reason.getBytes(StandardCharsets.UTF_8);
/* 129 */       if (utf8Bytes.length > 123)
/*     */       {
/* 131 */         this.reasonBytes = new byte[123];
/* 132 */         System.arraycopy(utf8Bytes, 0, this.reasonBytes, 0, 123);
/*     */       }
/*     */       else
/*     */       {
/* 136 */         this.reasonBytes = utf8Bytes;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void assertValidStatusCode(int statusCode)
/*     */   {
/* 144 */     if ((statusCode <= 999) || (statusCode >= 5000))
/*     */     {
/* 146 */       throw new ProtocolException("Out of range close status code: " + statusCode);
/*     */     }
/*     */     
/*     */ 
/* 150 */     if ((statusCode == 1006) || (statusCode == 1005) || (statusCode == 1015))
/*     */     {
/* 152 */       throw new ProtocolException("Frame forbidden close status code: " + statusCode);
/*     */     }
/*     */     
/*     */ 
/* 156 */     if ((statusCode >= 1000) && (statusCode <= 2999) && (!StatusCode.isTransmittable(statusCode)))
/*     */     {
/* 158 */       throw new ProtocolException("RFC6455 and IANA Undefined close status code: " + statusCode);
/*     */     }
/*     */   }
/*     */   
/*     */   private ByteBuffer asByteBuffer()
/*     */   {
/* 164 */     if ((this.statusCode == 1006) || (this.statusCode == 1005) || (this.statusCode == -1))
/*     */     {
/*     */ 
/* 167 */       return null;
/*     */     }
/*     */     
/* 170 */     int len = 2;
/* 171 */     boolean hasReason = (this.reasonBytes != null) && (this.reasonBytes.length > 0);
/* 172 */     if (hasReason)
/*     */     {
/* 174 */       len += this.reasonBytes.length;
/*     */     }
/*     */     
/* 177 */     ByteBuffer buf = BufferUtil.allocate(len);
/* 178 */     BufferUtil.flipToFill(buf);
/* 179 */     buf.put((byte)(this.statusCode >>> 8 & 0xFF));
/* 180 */     buf.put((byte)(this.statusCode >>> 0 & 0xFF));
/*     */     
/* 182 */     if (hasReason)
/*     */     {
/* 184 */       buf.put(this.reasonBytes, 0, this.reasonBytes.length);
/*     */     }
/* 186 */     BufferUtil.flipToFlush(buf, 0);
/*     */     
/* 188 */     return buf;
/*     */   }
/*     */   
/*     */   public CloseFrame asFrame()
/*     */   {
/* 193 */     CloseFrame frame = new CloseFrame();
/* 194 */     frame.setFin(true);
/*     */     
/* 196 */     if ((this.statusCode != 1006) && (this.statusCode != 1005) && (this.statusCode != 1015))
/*     */     {
/* 198 */       assertValidStatusCode(this.statusCode);
/* 199 */       frame.setPayload(asByteBuffer());
/*     */     }
/* 201 */     return frame;
/*     */   }
/*     */   
/*     */   public String getReason()
/*     */   {
/* 206 */     if (this.reasonBytes == null)
/*     */     {
/* 208 */       return null;
/*     */     }
/* 210 */     return new String(this.reasonBytes, StandardCharsets.UTF_8);
/*     */   }
/*     */   
/*     */   public int getStatusCode()
/*     */   {
/* 215 */     return this.statusCode;
/*     */   }
/*     */   
/*     */   public boolean isHarsh()
/*     */   {
/* 220 */     return (this.statusCode != 1000) && (this.statusCode != 1005);
/*     */   }
/*     */   
/*     */   public boolean isAbnormal()
/*     */   {
/* 225 */     return this.statusCode != 1000;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 231 */     return String.format("CloseInfo[code=%d,reason=%s]", new Object[] { Integer.valueOf(this.statusCode), getReason() });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\CloseInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */