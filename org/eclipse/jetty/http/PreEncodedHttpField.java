/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ServiceLoader;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
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
/*     */ public class PreEncodedHttpField
/*     */   extends HttpField
/*     */ {
/*  44 */   private static final Logger LOG = Log.getLogger(PreEncodedHttpField.class);
/*     */   private static final HttpFieldPreEncoder[] __encoders;
/*     */   
/*     */   static
/*     */   {
/*  49 */     List<HttpFieldPreEncoder> encoders = new ArrayList();
/*  50 */     Iterator<HttpFieldPreEncoder> iter = ServiceLoader.load(HttpFieldPreEncoder.class).iterator();
/*  51 */     while (iter.hasNext())
/*     */     {
/*     */       try
/*     */       {
/*  55 */         HttpFieldPreEncoder encoder = (HttpFieldPreEncoder)iter.next();
/*  56 */         if (index(encoder.getHttpVersion()) >= 0) {
/*  57 */           encoders.add(encoder);
/*     */         }
/*     */       }
/*     */       catch (Error|RuntimeException e) {
/*  61 */         LOG.debug(e);
/*     */       }
/*     */     }
/*  64 */     LOG.debug("HttpField encoders loaded: {}", new Object[] { encoders });
/*  65 */     int size = encoders.size();
/*     */     
/*  67 */     __encoders = new HttpFieldPreEncoder[size == 0 ? 1 : size];
/*  68 */     for (HttpFieldPreEncoder e : encoders)
/*     */     {
/*  70 */       int i = index(e.getHttpVersion());
/*  71 */       if (__encoders[i] == null) {
/*  72 */         __encoders[i] = e;
/*     */       } else {
/*  74 */         LOG.warn("multiple PreEncoders for " + e.getHttpVersion(), new Object[0]);
/*     */       }
/*     */     }
/*     */     
/*  78 */     if (__encoders[0] == null) {
/*  79 */       __encoders[0] = new Http1FieldPreEncoder();
/*     */     }
/*     */   }
/*     */   
/*     */   private static int index(HttpVersion version) {
/*  84 */     switch (version)
/*     */     {
/*     */     case HTTP_1_0: 
/*     */     case HTTP_1_1: 
/*  88 */       return 0;
/*     */     
/*     */     case HTTP_2: 
/*  91 */       return 1;
/*     */     }
/*     */     
/*  94 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*  98 */   private final byte[][] _encodedField = new byte[__encoders.length][];
/*     */   
/*     */   public PreEncodedHttpField(HttpHeader header, String name, String value)
/*     */   {
/* 102 */     super(header, name, value);
/* 103 */     for (int i = 0; i < __encoders.length; i++) {
/* 104 */       this._encodedField[i] = __encoders[i].getEncodedField(header, header.asString(), value);
/*     */     }
/*     */   }
/*     */   
/*     */   public PreEncodedHttpField(HttpHeader header, String value) {
/* 109 */     this(header, header.asString(), value);
/*     */   }
/*     */   
/*     */   public PreEncodedHttpField(String name, String value)
/*     */   {
/* 114 */     this(null, name, value);
/*     */   }
/*     */   
/*     */   public void putTo(ByteBuffer bufferInFillMode, HttpVersion version)
/*     */   {
/* 119 */     bufferInFillMode.put(this._encodedField[index(version)]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\PreEncodedHttpField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */