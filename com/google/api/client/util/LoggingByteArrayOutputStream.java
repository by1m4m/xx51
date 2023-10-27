/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public class LoggingByteArrayOutputStream
/*     */   extends ByteArrayOutputStream
/*     */ {
/*     */   private int bytesWritten;
/*     */   private final int maximumBytesToLog;
/*     */   private boolean closed;
/*     */   private final Level loggingLevel;
/*     */   private final Logger logger;
/*     */   
/*     */   public LoggingByteArrayOutputStream(Logger logger, Level loggingLevel, int maximumBytesToLog)
/*     */   {
/*  63 */     this.logger = ((Logger)Preconditions.checkNotNull(logger));
/*  64 */     this.loggingLevel = ((Level)Preconditions.checkNotNull(loggingLevel));
/*  65 */     Preconditions.checkArgument(maximumBytesToLog >= 0);
/*  66 */     this.maximumBytesToLog = maximumBytesToLog;
/*     */   }
/*     */   
/*     */   public synchronized void write(int b)
/*     */   {
/*  71 */     Preconditions.checkArgument(!this.closed);
/*  72 */     this.bytesWritten += 1;
/*  73 */     if (this.count < this.maximumBytesToLog) {
/*  74 */       super.write(b);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void write(byte[] b, int off, int len)
/*     */   {
/*  80 */     Preconditions.checkArgument(!this.closed);
/*  81 */     this.bytesWritten += len;
/*  82 */     if (this.count < this.maximumBytesToLog) {
/*  83 */       int end = this.count + len;
/*  84 */       if (end > this.maximumBytesToLog) {
/*  85 */         len += this.maximumBytesToLog - end;
/*     */       }
/*  87 */       super.write(b, off, len);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void close()
/*     */     throws IOException
/*     */   {
/*  94 */     if (!this.closed)
/*     */     {
/*  96 */       if (this.bytesWritten != 0)
/*     */       {
/*  98 */         StringBuilder buf = new StringBuilder().append("Total: ");
/*  99 */         appendBytes(buf, this.bytesWritten);
/* 100 */         if ((this.count != 0) && (this.count < this.bytesWritten)) {
/* 101 */           buf.append(" (logging first ");
/* 102 */           appendBytes(buf, this.count);
/* 103 */           buf.append(")");
/*     */         }
/* 105 */         this.logger.config(buf.toString());
/*     */         
/* 107 */         if (this.count != 0)
/*     */         {
/* 109 */           this.logger.log(this.loggingLevel, toString("UTF-8").replaceAll("[\\x00-\\x09\\x0B\\x0C\\x0E-\\x1F\\x7F]", " "));
/*     */         }
/*     */       }
/*     */       
/* 113 */       this.closed = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public final int getMaximumBytesToLog()
/*     */   {
/* 119 */     return this.maximumBytesToLog;
/*     */   }
/*     */   
/*     */   public final synchronized int getBytesWritten()
/*     */   {
/* 124 */     return this.bytesWritten;
/*     */   }
/*     */   
/*     */   private static void appendBytes(StringBuilder buf, int x) {
/* 128 */     if (x == 1) {
/* 129 */       buf.append("1 byte");
/*     */     } else {
/* 131 */       buf.append(NumberFormat.getInstance().format(x)).append(" bytes");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\LoggingByteArrayOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */