/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ public class DefaultHttpDataFactory
/*     */   implements HttpDataFactory
/*     */ {
/*     */   public static final long MINSIZE = 16384L;
/*     */   public static final long MAXSIZE = -1L;
/*     */   private final boolean useDisk;
/*     */   private final boolean checkSize;
/*     */   private long minSize;
/*  58 */   private long maxSize = -1L;
/*     */   
/*  60 */   private Charset charset = HttpConstants.DEFAULT_CHARSET;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */   private final Map<HttpRequest, List<HttpData>> requestFileDeleteMap = Collections.synchronizedMap(new IdentityHashMap());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttpDataFactory()
/*     */   {
/*  77 */     this.useDisk = false;
/*  78 */     this.checkSize = true;
/*  79 */     this.minSize = 16384L;
/*     */   }
/*     */   
/*     */   public DefaultHttpDataFactory(Charset charset) {
/*  83 */     this();
/*  84 */     this.charset = charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultHttpDataFactory(boolean useDisk)
/*     */   {
/*  91 */     this.useDisk = useDisk;
/*  92 */     this.checkSize = false;
/*     */   }
/*     */   
/*     */   public DefaultHttpDataFactory(boolean useDisk, Charset charset) {
/*  96 */     this(useDisk);
/*  97 */     this.charset = charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultHttpDataFactory(long minSize)
/*     */   {
/* 104 */     this.useDisk = false;
/* 105 */     this.checkSize = true;
/* 106 */     this.minSize = minSize;
/*     */   }
/*     */   
/*     */   public DefaultHttpDataFactory(long minSize, Charset charset) {
/* 110 */     this(minSize);
/* 111 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public void setMaxLimit(long maxSize)
/*     */   {
/* 116 */     this.maxSize = maxSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private List<HttpData> getList(HttpRequest request)
/*     */   {
/* 123 */     List<HttpData> list = (List)this.requestFileDeleteMap.get(request);
/* 124 */     if (list == null) {
/* 125 */       list = new ArrayList();
/* 126 */       this.requestFileDeleteMap.put(request, list);
/*     */     }
/* 128 */     return list;
/*     */   }
/*     */   
/*     */   public Attribute createAttribute(HttpRequest request, String name)
/*     */   {
/* 133 */     if (this.useDisk) {
/* 134 */       Attribute attribute = new DiskAttribute(name, this.charset);
/* 135 */       attribute.setMaxSize(this.maxSize);
/* 136 */       List<HttpData> list = getList(request);
/* 137 */       list.add(attribute);
/* 138 */       return attribute;
/*     */     }
/* 140 */     if (this.checkSize) {
/* 141 */       Attribute attribute = new MixedAttribute(name, this.minSize, this.charset);
/* 142 */       attribute.setMaxSize(this.maxSize);
/* 143 */       List<HttpData> list = getList(request);
/* 144 */       list.add(attribute);
/* 145 */       return attribute;
/*     */     }
/* 147 */     MemoryAttribute attribute = new MemoryAttribute(name);
/* 148 */     attribute.setMaxSize(this.maxSize);
/* 149 */     return attribute;
/*     */   }
/*     */   
/*     */   public Attribute createAttribute(HttpRequest request, String name, long definedSize)
/*     */   {
/* 154 */     if (this.useDisk) {
/* 155 */       Attribute attribute = new DiskAttribute(name, definedSize, this.charset);
/* 156 */       attribute.setMaxSize(this.maxSize);
/* 157 */       List<HttpData> list = getList(request);
/* 158 */       list.add(attribute);
/* 159 */       return attribute;
/*     */     }
/* 161 */     if (this.checkSize) {
/* 162 */       Attribute attribute = new MixedAttribute(name, definedSize, this.minSize, this.charset);
/* 163 */       attribute.setMaxSize(this.maxSize);
/* 164 */       List<HttpData> list = getList(request);
/* 165 */       list.add(attribute);
/* 166 */       return attribute;
/*     */     }
/* 168 */     MemoryAttribute attribute = new MemoryAttribute(name, definedSize);
/* 169 */     attribute.setMaxSize(this.maxSize);
/* 170 */     return attribute;
/*     */   }
/*     */   
/*     */ 
/*     */   private static void checkHttpDataSize(HttpData data)
/*     */   {
/*     */     try
/*     */     {
/* 178 */       data.checkSize(data.length());
/*     */     } catch (IOException ignored) {
/* 180 */       throw new IllegalArgumentException("Attribute bigger than maxSize allowed");
/*     */     }
/*     */   }
/*     */   
/*     */   public Attribute createAttribute(HttpRequest request, String name, String value)
/*     */   {
/* 186 */     if (this.useDisk) {
/*     */       Attribute attribute;
/*     */       try {
/* 189 */         Attribute attribute = new DiskAttribute(name, value, this.charset);
/* 190 */         attribute.setMaxSize(this.maxSize);
/*     */       }
/*     */       catch (IOException e) {
/* 193 */         attribute = new MixedAttribute(name, value, this.minSize, this.charset);
/* 194 */         attribute.setMaxSize(this.maxSize);
/*     */       }
/* 196 */       checkHttpDataSize(attribute);
/* 197 */       List<HttpData> list = getList(request);
/* 198 */       list.add(attribute);
/* 199 */       return attribute;
/*     */     }
/* 201 */     if (this.checkSize) {
/* 202 */       Attribute attribute = new MixedAttribute(name, value, this.minSize, this.charset);
/* 203 */       attribute.setMaxSize(this.maxSize);
/* 204 */       checkHttpDataSize(attribute);
/* 205 */       List<HttpData> list = getList(request);
/* 206 */       list.add(attribute);
/* 207 */       return attribute;
/*     */     }
/*     */     try {
/* 210 */       MemoryAttribute attribute = new MemoryAttribute(name, value, this.charset);
/* 211 */       attribute.setMaxSize(this.maxSize);
/* 212 */       checkHttpDataSize(attribute);
/* 213 */       return attribute;
/*     */     } catch (IOException e) {
/* 215 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public FileUpload createFileUpload(HttpRequest request, String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size)
/*     */   {
/* 223 */     if (this.useDisk) {
/* 224 */       FileUpload fileUpload = new DiskFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
/*     */       
/* 226 */       fileUpload.setMaxSize(this.maxSize);
/* 227 */       checkHttpDataSize(fileUpload);
/* 228 */       List<HttpData> list = getList(request);
/* 229 */       list.add(fileUpload);
/* 230 */       return fileUpload;
/*     */     }
/* 232 */     if (this.checkSize) {
/* 233 */       FileUpload fileUpload = new MixedFileUpload(name, filename, contentType, contentTransferEncoding, charset, size, this.minSize);
/*     */       
/* 235 */       fileUpload.setMaxSize(this.maxSize);
/* 236 */       checkHttpDataSize(fileUpload);
/* 237 */       List<HttpData> list = getList(request);
/* 238 */       list.add(fileUpload);
/* 239 */       return fileUpload;
/*     */     }
/* 241 */     MemoryFileUpload fileUpload = new MemoryFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
/*     */     
/* 243 */     fileUpload.setMaxSize(this.maxSize);
/* 244 */     checkHttpDataSize(fileUpload);
/* 245 */     return fileUpload;
/*     */   }
/*     */   
/*     */   public void removeHttpDataFromClean(HttpRequest request, InterfaceHttpData data)
/*     */   {
/* 250 */     if (!(data instanceof HttpData)) {
/* 251 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 256 */     List<HttpData> list = (List)this.requestFileDeleteMap.get(request);
/* 257 */     if (list == null) {
/* 258 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 263 */     Iterator<HttpData> i = list.iterator();
/* 264 */     while (i.hasNext()) {
/* 265 */       HttpData n = (HttpData)i.next();
/* 266 */       if (n == data) {
/* 267 */         i.remove();
/*     */         
/*     */ 
/* 270 */         if (list.isEmpty()) {
/* 271 */           this.requestFileDeleteMap.remove(request);
/*     */         }
/*     */         
/* 274 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void cleanRequestHttpData(HttpRequest request)
/*     */   {
/* 281 */     List<HttpData> list = (List)this.requestFileDeleteMap.remove(request);
/* 282 */     if (list != null) {
/* 283 */       for (HttpData data : list) {
/* 284 */         data.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void cleanAllHttpData()
/*     */   {
/* 291 */     Iterator<Map.Entry<HttpRequest, List<HttpData>>> i = this.requestFileDeleteMap.entrySet().iterator();
/* 292 */     while (i.hasNext()) {
/* 293 */       Map.Entry<HttpRequest, List<HttpData>> e = (Map.Entry)i.next();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 298 */       List<HttpData> list = (List)e.getValue();
/* 299 */       for (HttpData data : list) {
/* 300 */         data.release();
/*     */       }
/*     */       
/* 303 */       i.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public void cleanRequestHttpDatas(HttpRequest request)
/*     */   {
/* 309 */     cleanRequestHttpData(request);
/*     */   }
/*     */   
/*     */   public void cleanAllHttpDatas()
/*     */   {
/* 314 */     cleanAllHttpData();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\multipart\DefaultHttpDataFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */