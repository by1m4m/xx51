/*     */ package org.eclipse.jetty.websocket.common.extensions.compress;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Deflater;
/*     */ import java.util.zip.Inflater;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.BadPayloadException;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
/*     */ import org.eclipse.jetty.websocket.common.OpCode;
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
/*     */ public class PerMessageDeflateExtension
/*     */   extends CompressExtension
/*     */ {
/*  40 */   private static final Logger LOG = Log.getLogger(PerMessageDeflateExtension.class);
/*     */   
/*     */   private ExtensionConfig configRequested;
/*     */   private ExtensionConfig configNegotiated;
/*  44 */   private boolean incomingContextTakeover = true;
/*  45 */   private boolean outgoingContextTakeover = true;
/*     */   
/*     */   private boolean incomingCompressed;
/*     */   
/*     */   public String getName()
/*     */   {
/*  51 */     return "permessage-deflate";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void incomingFrame(Frame frame)
/*     */   {
/*  63 */     if (frame.getType().isData())
/*     */     {
/*  65 */       this.incomingCompressed = frame.isRsv1();
/*     */     }
/*     */     
/*  68 */     if ((OpCode.isControlFrame(frame.getOpCode())) || (!this.incomingCompressed))
/*     */     {
/*  70 */       nextIncomingFrame(frame);
/*  71 */       return;
/*     */     }
/*     */     
/*  74 */     ByteAccumulator accumulator = newByteAccumulator();
/*     */     
/*     */     try
/*     */     {
/*  78 */       ByteBuffer payload = frame.getPayload();
/*  79 */       decompress(accumulator, payload);
/*  80 */       if (frame.isFin())
/*     */       {
/*  82 */         decompress(accumulator, TAIL_BYTES_BUF.slice());
/*     */       }
/*     */       
/*  85 */       forwardIncoming(frame, accumulator);
/*     */     }
/*     */     catch (DataFormatException e)
/*     */     {
/*  89 */       throw new BadPayloadException(e);
/*     */     }
/*     */     
/*  92 */     if (frame.isFin()) {
/*  93 */       this.incomingCompressed = false;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void nextIncomingFrame(Frame frame)
/*     */   {
/*  99 */     if ((frame.isFin()) && (!this.incomingContextTakeover))
/*     */     {
/* 101 */       LOG.debug("Incoming Context Reset", new Object[0]);
/* 102 */       this.decompressCount.set(0);
/* 103 */       getInflater().reset();
/*     */     }
/* 105 */     super.nextIncomingFrame(frame);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void nextOutgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
/*     */   {
/* 111 */     if ((frame.isFin()) && (!this.outgoingContextTakeover))
/*     */     {
/* 113 */       LOG.debug("Outgoing Context Reset", new Object[0]);
/* 114 */       getDeflater().reset();
/*     */     }
/* 116 */     super.nextOutgoingFrame(frame, callback, batchMode);
/*     */   }
/*     */   
/*     */ 
/*     */   int getRsvUseMode()
/*     */   {
/* 122 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */   int getTailDropMode()
/*     */   {
/* 128 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setConfig(ExtensionConfig config)
/*     */   {
/* 134 */     this.configRequested = new ExtensionConfig(config);
/* 135 */     this.configNegotiated = new ExtensionConfig(config.getName());
/*     */     
/* 137 */     for (String key : config.getParameterKeys())
/*     */     {
/* 139 */       key = key.trim();
/* 140 */       switch (key)
/*     */       {
/*     */       case "client_max_window_bits": 
/*     */       case "server_max_window_bits": 
/*     */         break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case "client_no_context_takeover": 
/* 151 */         this.configNegotiated.setParameter("client_no_context_takeover");
/* 152 */         switch (getPolicy().getBehavior())
/*     */         {
/*     */         case CLIENT: 
/* 155 */           this.incomingContextTakeover = false;
/* 156 */           break;
/*     */         case SERVER: 
/* 158 */           this.outgoingContextTakeover = false;
/*     */         }
/*     */         
/* 161 */         break;
/*     */       
/*     */ 
/*     */       case "server_no_context_takeover": 
/* 165 */         this.configNegotiated.setParameter("server_no_context_takeover");
/* 166 */         switch (getPolicy().getBehavior())
/*     */         {
/*     */         case CLIENT: 
/* 169 */           this.outgoingContextTakeover = false;
/* 170 */           break;
/*     */         case SERVER: 
/* 172 */           this.incomingContextTakeover = false;
/*     */         }
/*     */         
/* 175 */         break;
/*     */       
/*     */ 
/*     */       default: 
/* 179 */         throw new IllegalArgumentException();
/*     */       }
/*     */       
/*     */     }
/*     */     
/* 184 */     LOG.debug("config: outgoingContextTakover={}, incomingContextTakeover={} : {}", new Object[] { Boolean.valueOf(this.outgoingContextTakeover), Boolean.valueOf(this.incomingContextTakeover), this });
/*     */     
/* 186 */     super.setConfig(this.configNegotiated);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 192 */     return String.format("%s[requested=\"%s\", negotiated=\"%s\"]", new Object[] {
/* 193 */       getClass().getSimpleName(), this.configRequested
/* 194 */       .getParameterizedName(), this.configNegotiated
/* 195 */       .getParameterizedName() });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\extensions\compress\PerMessageDeflateExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */