/*     */ package org.eclipse.jetty.websocket.common.extensions;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.Calendar;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.util.IO;
/*     */ import org.eclipse.jetty.util.StringUtil;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.common.Generator;
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
/*     */ public class FrameCaptureExtension
/*     */   extends AbstractExtension
/*     */ {
/*  46 */   private static final Logger LOG = Log.getLogger(FrameCaptureExtension.class);
/*     */   
/*     */   private static final int BUFSIZE = 32768;
/*     */   private Generator generator;
/*     */   private Path outputDir;
/*  51 */   private String prefix = "frame";
/*     */   
/*     */   private Path incomingFramesPath;
/*     */   private Path outgoingFramesPath;
/*  55 */   private AtomicInteger incomingCount = new AtomicInteger(0);
/*  56 */   private AtomicInteger outgoingCount = new AtomicInteger(0);
/*     */   
/*     */   private SeekableByteChannel incomingChannel;
/*     */   
/*     */   private SeekableByteChannel outgoingChannel;
/*     */   
/*     */   public String getName()
/*     */   {
/*  64 */     return "@frame-capture";
/*     */   }
/*     */   
/*     */ 
/*     */   public void incomingFrame(Frame frame)
/*     */   {
/*  70 */     saveFrame(frame, false);
/*     */     try
/*     */     {
/*  73 */       nextIncomingFrame(frame);
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/*  77 */       IO.close(this.incomingChannel);
/*  78 */       this.incomingChannel = null;
/*  79 */       throw t;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
/*     */   {
/*  86 */     saveFrame(frame, true);
/*     */     try
/*     */     {
/*  89 */       nextOutgoingFrame(frame, callback, batchMode);
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/*  93 */       IO.close(this.outgoingChannel);
/*  94 */       this.outgoingChannel = null;
/*  95 */       throw t;
/*     */     }
/*     */   }
/*     */   
/*     */   private void saveFrame(Frame frame, boolean outgoing)
/*     */   {
/* 101 */     if ((this.outputDir == null) || (this.generator == null))
/*     */     {
/* 103 */       return;
/*     */     }
/*     */     
/*     */ 
/* 107 */     SeekableByteChannel channel = outgoing ? this.outgoingChannel : this.incomingChannel;
/*     */     
/* 109 */     if (channel == null)
/*     */     {
/* 111 */       return;
/*     */     }
/*     */     
/* 114 */     ByteBuffer buf = getBufferPool().acquire(32768, false);
/*     */     
/*     */     try
/*     */     {
/* 118 */       WebSocketFrame f = WebSocketFrame.copy(frame);
/* 119 */       f.setMasked(false);
/* 120 */       this.generator.generateHeaderBytes(f, buf);
/* 121 */       channel.write(buf);
/* 122 */       if (frame.hasPayload())
/*     */       {
/* 124 */         channel.write(frame.getPayload().slice());
/*     */       }
/* 126 */       if (LOG.isDebugEnabled()) {
/* 127 */         LOG.debug("Saved {} frame #{}", new Object[] { outgoing ? "outgoing" : "incoming", 
/* 128 */           Integer.valueOf(outgoing ? this.outgoingCount.incrementAndGet() : this.incomingCount.incrementAndGet()) });
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 132 */       LOG.warn("Unable to save frame: " + frame, e);
/*     */     }
/*     */     finally
/*     */     {
/* 136 */       getBufferPool().release(buf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setConfig(ExtensionConfig config)
/*     */   {
/* 143 */     super.setConfig(config);
/*     */     
/* 145 */     String cfgOutputDir = config.getParameter("output-dir", null);
/* 146 */     if (StringUtil.isNotBlank(cfgOutputDir))
/*     */     {
/* 148 */       Path path = new File(cfgOutputDir).toPath();
/* 149 */       if ((Files.isDirectory(path, new LinkOption[0])) && (Files.exists(path, new LinkOption[0])) && (Files.isWritable(path)))
/*     */       {
/* 151 */         this.outputDir = path;
/*     */       }
/*     */       else
/*     */       {
/* 155 */         LOG.warn("Unable to configure {}: not a valid output directory", new Object[] { path.toAbsolutePath().toString() });
/*     */       }
/*     */     }
/*     */     
/* 159 */     String cfgPrefix = config.getParameter("prefix", "frame");
/* 160 */     if (StringUtil.isNotBlank(cfgPrefix))
/*     */     {
/* 162 */       this.prefix = cfgPrefix;
/*     */     }
/*     */     
/* 165 */     if (this.outputDir != null)
/*     */     {
/*     */       try
/*     */       {
/* 169 */         Path dir = this.outputDir.toRealPath(new LinkOption[0]);
/*     */         
/*     */ 
/* 172 */         String tstamp = String.format("%1$tY%1$tm%1$td-%1$tH%1$tM%1$tS", new Object[] { Calendar.getInstance() });
/* 173 */         this.incomingFramesPath = dir.resolve(String.format("%s-%s-incoming.dat", new Object[] { this.prefix, tstamp }));
/* 174 */         this.outgoingFramesPath = dir.resolve(String.format("%s-%s-outgoing.dat", new Object[] { this.prefix, tstamp }));
/*     */         
/* 176 */         this.incomingChannel = Files.newByteChannel(this.incomingFramesPath, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE });
/* 177 */         this.outgoingChannel = Files.newByteChannel(this.outgoingFramesPath, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE });
/*     */         
/* 179 */         this.generator = new Generator(WebSocketPolicy.newServerPolicy(), getBufferPool(), false, true);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 183 */         LOG.warn("Unable to create capture file(s)", e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\extensions\FrameCaptureExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */