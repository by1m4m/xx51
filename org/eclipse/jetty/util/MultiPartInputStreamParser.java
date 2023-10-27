/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.MultipartConfigElement;
/*     */ import javax.servlet.ServletInputStream;
/*     */ import javax.servlet.http.Part;
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
/*     */ 
/*     */ public class MultiPartInputStreamParser
/*     */ {
/*  59 */   private static final Logger LOG = Log.getLogger(MultiPartInputStreamParser.class);
/*  60 */   public static final MultipartConfigElement __DEFAULT_MULTIPART_CONFIG = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
/*  61 */   public static final MultiMap<Part> EMPTY_MAP = new MultiMap(Collections.emptyMap());
/*     */   
/*     */   protected InputStream _in;
/*     */   protected MultipartConfigElement _config;
/*     */   protected String _contentType;
/*     */   protected MultiMap<Part> _parts;
/*     */   protected Exception _err;
/*     */   protected File _tmpDir;
/*     */   protected File _contextTmpDir;
/*     */   protected boolean _deleteOnExit;
/*     */   protected boolean _writeFilesWithFilenames;
/*     */   
/*     */   public class MultiPart
/*     */     implements Part
/*     */   {
/*     */     protected String _name;
/*     */     protected String _filename;
/*     */     protected File _file;
/*     */     protected OutputStream _out;
/*     */     protected ByteArrayOutputStream2 _bout;
/*     */     protected String _contentType;
/*     */     protected MultiMap<String> _headers;
/*  83 */     protected long _size = 0L;
/*  84 */     protected boolean _temporary = true;
/*     */     
/*     */     public MultiPart(String name, String filename)
/*     */       throws IOException
/*     */     {
/*  89 */       this._name = name;
/*  90 */       this._filename = filename;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/*  96 */       return String.format("Part{n=%s,fn=%s,ct=%s,s=%d,t=%b,f=%s}", new Object[] { this._name, this._filename, this._contentType, Long.valueOf(this._size), Boolean.valueOf(this._temporary), this._file });
/*     */     }
/*     */     
/*     */     protected void setContentType(String contentType) {
/* 100 */       this._contentType = contentType;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void open()
/*     */       throws IOException
/*     */     {
/* 110 */       if ((MultiPartInputStreamParser.this.isWriteFilesWithFilenames()) && (this._filename != null) && (this._filename.trim().length() > 0))
/*     */       {
/* 112 */         createFile();
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 118 */         this._out = (this._bout = new ByteArrayOutputStream2());
/*     */       }
/*     */     }
/*     */     
/*     */     protected void close()
/*     */       throws IOException
/*     */     {
/* 125 */       this._out.close();
/*     */     }
/*     */     
/*     */ 
/*     */     protected void write(int b)
/*     */       throws IOException
/*     */     {
/* 132 */       if ((MultiPartInputStreamParser.this._config.getMaxFileSize() > 0L) && (this._size + 1L > MultiPartInputStreamParser.this._config.getMaxFileSize())) {
/* 133 */         throw new IllegalStateException("Multipart Mime part " + this._name + " exceeds max filesize");
/*     */       }
/* 135 */       if ((MultiPartInputStreamParser.this._config.getFileSizeThreshold() > 0) && (this._size + 1L > MultiPartInputStreamParser.this._config.getFileSizeThreshold()) && (this._file == null)) {
/* 136 */         createFile();
/*     */       }
/* 138 */       this._out.write(b);
/* 139 */       this._size += 1L;
/*     */     }
/*     */     
/*     */     protected void write(byte[] bytes, int offset, int length)
/*     */       throws IOException
/*     */     {
/* 145 */       if ((MultiPartInputStreamParser.this._config.getMaxFileSize() > 0L) && (this._size + length > MultiPartInputStreamParser.this._config.getMaxFileSize())) {
/* 146 */         throw new IllegalStateException("Multipart Mime part " + this._name + " exceeds max filesize");
/*     */       }
/* 148 */       if ((MultiPartInputStreamParser.this._config.getFileSizeThreshold() > 0) && (this._size + length > MultiPartInputStreamParser.this._config.getFileSizeThreshold()) && (this._file == null)) {
/* 149 */         createFile();
/*     */       }
/* 151 */       this._out.write(bytes, offset, length);
/* 152 */       this._size += length;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected void createFile()
/*     */       throws IOException
/*     */     {
/* 160 */       boolean USER = true;
/* 161 */       boolean WORLD = false;
/*     */       
/* 163 */       this._file = File.createTempFile("MultiPart", "", MultiPartInputStreamParser.this._tmpDir);
/* 164 */       this._file.setReadable(false, false);
/* 165 */       this._file.setReadable(true, true);
/*     */       
/* 167 */       if (MultiPartInputStreamParser.this._deleteOnExit)
/* 168 */         this._file.deleteOnExit();
/* 169 */       FileOutputStream fos = new FileOutputStream(this._file);
/* 170 */       BufferedOutputStream bos = new BufferedOutputStream(fos);
/*     */       
/* 172 */       if ((this._size > 0L) && (this._out != null))
/*     */       {
/*     */ 
/* 175 */         this._out.flush();
/* 176 */         this._bout.writeTo(bos);
/* 177 */         this._out.close();
/* 178 */         this._bout = null;
/*     */       }
/* 180 */       this._out = bos;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected void setHeaders(MultiMap<String> headers)
/*     */     {
/* 187 */       this._headers = headers;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getContentType()
/*     */     {
/* 196 */       return this._contentType;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getHeader(String name)
/*     */     {
/* 205 */       if (name == null)
/* 206 */         return null;
/* 207 */       return (String)this._headers.getValue(name.toLowerCase(Locale.ENGLISH), 0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Collection<String> getHeaderNames()
/*     */     {
/* 216 */       return this._headers.keySet();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Collection<String> getHeaders(String name)
/*     */     {
/* 225 */       return this._headers.getValues(name);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public InputStream getInputStream()
/*     */       throws IOException
/*     */     {
/* 234 */       if (this._file != null)
/*     */       {
/*     */ 
/* 237 */         return new BufferedInputStream(new FileInputStream(this._file));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 242 */       return new ByteArrayInputStream(this._bout.getBuf(), 0, this._bout.size());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getSubmittedFileName()
/*     */     {
/* 253 */       return getContentDispositionFilename();
/*     */     }
/*     */     
/*     */     public byte[] getBytes()
/*     */     {
/* 258 */       if (this._bout != null)
/* 259 */         return this._bout.toByteArray();
/* 260 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getName()
/*     */     {
/* 269 */       return this._name;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public long getSize()
/*     */     {
/* 278 */       return this._size;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void write(String fileName)
/*     */       throws IOException
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 12	org/eclipse/jetty/util/MultiPartInputStreamParser$MultiPart:_file	Ljava/io/File;
/*     */       //   4: ifnonnull +95 -> 99
/*     */       //   7: aload_0
/*     */       //   8: iconst_0
/*     */       //   9: putfield 4	org/eclipse/jetty/util/MultiPartInputStreamParser$MultiPart:_temporary	Z
/*     */       //   12: aload_0
/*     */       //   13: new 66	java/io/File
/*     */       //   16: dup
/*     */       //   17: aload_0
/*     */       //   18: getfield 1	org/eclipse/jetty/util/MultiPartInputStreamParser$MultiPart:this$0	Lorg/eclipse/jetty/util/MultiPartInputStreamParser;
/*     */       //   21: getfield 38	org/eclipse/jetty/util/MultiPartInputStreamParser:_tmpDir	Ljava/io/File;
/*     */       //   24: aload_1
/*     */       //   25: invokespecial 67	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
/*     */       //   28: putfield 12	org/eclipse/jetty/util/MultiPartInputStreamParser$MultiPart:_file	Ljava/io/File;
/*     */       //   31: aconst_null
/*     */       //   32: astore_2
/*     */       //   33: new 45	java/io/BufferedOutputStream
/*     */       //   36: dup
/*     */       //   37: new 43	java/io/FileOutputStream
/*     */       //   40: dup
/*     */       //   41: aload_0
/*     */       //   42: getfield 12	org/eclipse/jetty/util/MultiPartInputStreamParser$MultiPart:_file	Ljava/io/File;
/*     */       //   45: invokespecial 44	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
/*     */       //   48: invokespecial 46	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
/*     */       //   51: astore_2
/*     */       //   52: aload_0
/*     */       //   53: getfield 20	org/eclipse/jetty/util/MultiPartInputStreamParser$MultiPart:_bout	Lorg/eclipse/jetty/util/ByteArrayOutputStream2;
/*     */       //   56: aload_2
/*     */       //   57: invokevirtual 48	org/eclipse/jetty/util/ByteArrayOutputStream2:writeTo	(Ljava/io/OutputStream;)V
/*     */       //   60: aload_2
/*     */       //   61: invokevirtual 68	java/io/BufferedOutputStream:flush	()V
/*     */       //   64: aload_2
/*     */       //   65: ifnull +7 -> 72
/*     */       //   68: aload_2
/*     */       //   69: invokevirtual 69	java/io/BufferedOutputStream:close	()V
/*     */       //   72: aload_0
/*     */       //   73: aconst_null
/*     */       //   74: putfield 20	org/eclipse/jetty/util/MultiPartInputStreamParser$MultiPart:_bout	Lorg/eclipse/jetty/util/ByteArrayOutputStream2;
/*     */       //   77: goto +19 -> 96
/*     */       //   80: astore_3
/*     */       //   81: aload_2
/*     */       //   82: ifnull +7 -> 89
/*     */       //   85: aload_2
/*     */       //   86: invokevirtual 69	java/io/BufferedOutputStream:close	()V
/*     */       //   89: aload_0
/*     */       //   90: aconst_null
/*     */       //   91: putfield 20	org/eclipse/jetty/util/MultiPartInputStreamParser$MultiPart:_bout	Lorg/eclipse/jetty/util/ByteArrayOutputStream2;
/*     */       //   94: aload_3
/*     */       //   95: athrow
/*     */       //   96: goto +50 -> 146
/*     */       //   99: aload_0
/*     */       //   100: iconst_0
/*     */       //   101: putfield 4	org/eclipse/jetty/util/MultiPartInputStreamParser$MultiPart:_temporary	Z
/*     */       //   104: aload_0
/*     */       //   105: getfield 12	org/eclipse/jetty/util/MultiPartInputStreamParser$MultiPart:_file	Ljava/io/File;
/*     */       //   108: invokevirtual 70	java/io/File:toPath	()Ljava/nio/file/Path;
/*     */       //   111: astore_2
/*     */       //   112: aload_2
/*     */       //   113: aload_1
/*     */       //   114: invokeinterface 71 2 0
/*     */       //   119: astore_3
/*     */       //   120: aload_2
/*     */       //   121: aload_3
/*     */       //   122: iconst_1
/*     */       //   123: anewarray 72	java/nio/file/CopyOption
/*     */       //   126: dup
/*     */       //   127: iconst_0
/*     */       //   128: getstatic 73	java/nio/file/StandardCopyOption:REPLACE_EXISTING	Ljava/nio/file/StandardCopyOption;
/*     */       //   131: aastore
/*     */       //   132: invokestatic 74	java/nio/file/Files:move	(Ljava/nio/file/Path;Ljava/nio/file/Path;[Ljava/nio/file/CopyOption;)Ljava/nio/file/Path;
/*     */       //   135: pop
/*     */       //   136: aload_0
/*     */       //   137: aload_3
/*     */       //   138: invokeinterface 75 1 0
/*     */       //   143: putfield 12	org/eclipse/jetty/util/MultiPartInputStreamParser$MultiPart:_file	Ljava/io/File;
/*     */       //   146: return
/*     */       // Line number table:
/*     */       //   Java source line #287	-> byte code offset #0
/*     */       //   Java source line #289	-> byte code offset #7
/*     */       //   Java source line #292	-> byte code offset #12
/*     */       //   Java source line #294	-> byte code offset #31
/*     */       //   Java source line #297	-> byte code offset #33
/*     */       //   Java source line #298	-> byte code offset #52
/*     */       //   Java source line #299	-> byte code offset #60
/*     */       //   Java source line #303	-> byte code offset #64
/*     */       //   Java source line #304	-> byte code offset #68
/*     */       //   Java source line #305	-> byte code offset #72
/*     */       //   Java source line #306	-> byte code offset #77
/*     */       //   Java source line #303	-> byte code offset #80
/*     */       //   Java source line #304	-> byte code offset #85
/*     */       //   Java source line #305	-> byte code offset #89
/*     */       //   Java source line #307	-> byte code offset #96
/*     */       //   Java source line #311	-> byte code offset #99
/*     */       //   Java source line #313	-> byte code offset #104
/*     */       //   Java source line #314	-> byte code offset #112
/*     */       //   Java source line #315	-> byte code offset #120
/*     */       //   Java source line #316	-> byte code offset #136
/*     */       //   Java source line #318	-> byte code offset #146
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	147	0	this	MultiPart
/*     */       //   0	147	1	fileName	String
/*     */       //   32	54	2	bos	BufferedOutputStream
/*     */       //   111	10	2	src	java.nio.file.Path
/*     */       //   80	15	3	localObject	Object
/*     */       //   119	19	3	target	java.nio.file.Path
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   33	64	80	finally
/*     */     }
/*     */     
/*     */     public void delete()
/*     */       throws IOException
/*     */     {
/* 328 */       if ((this._file != null) && (this._file.exists())) {
/* 329 */         this._file.delete();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void cleanUp()
/*     */       throws IOException
/*     */     {
/* 339 */       if ((this._temporary) && (this._file != null) && (this._file.exists())) {
/* 340 */         this._file.delete();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public File getFile()
/*     */     {
/* 350 */       return this._file;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getContentDispositionFilename()
/*     */     {
/* 360 */       return this._filename;
/*     */     }
/*     */   }
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
/*     */   public MultiPartInputStreamParser(InputStream in, String contentType, MultipartConfigElement config, File contextTmpDir)
/*     */   {
/* 375 */     this._contentType = contentType;
/* 376 */     this._config = config;
/* 377 */     this._contextTmpDir = contextTmpDir;
/* 378 */     if (this._contextTmpDir == null) {
/* 379 */       this._contextTmpDir = new File(System.getProperty("java.io.tmpdir"));
/*     */     }
/* 381 */     if (this._config == null) {
/* 382 */       this._config = new MultipartConfigElement(this._contextTmpDir.getAbsolutePath());
/*     */     }
/* 384 */     if ((in instanceof ServletInputStream))
/*     */     {
/* 386 */       if (((ServletInputStream)in).isFinished())
/*     */       {
/* 388 */         this._parts = EMPTY_MAP;
/* 389 */         return;
/*     */       }
/*     */     }
/* 392 */     this._in = new ReadLineInputStream(in);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<Part> getParsedParts()
/*     */   {
/* 401 */     if (this._parts == null) {
/* 402 */       return Collections.emptyList();
/*     */     }
/* 404 */     Collection<List<Part>> values = this._parts.values();
/* 405 */     List<Part> parts = new ArrayList();
/* 406 */     for (List<Part> o : values)
/*     */     {
/* 408 */       List<Part> asList = LazyList.getList(o, false);
/* 409 */       parts.addAll(asList);
/*     */     }
/* 411 */     return parts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void deleteParts()
/*     */     throws MultiException
/*     */   {
/* 422 */     Collection<Part> parts = getParsedParts();
/* 423 */     MultiException err = new MultiException();
/* 424 */     for (Part p : parts)
/*     */     {
/*     */       try
/*     */       {
/* 428 */         ((MultiPart)p).cleanUp();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 432 */         err.add(e);
/*     */       }
/*     */     }
/* 435 */     this._parts.clear();
/*     */     
/* 437 */     err.ifExceptionThrowMulti();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<Part> getParts()
/*     */     throws IOException
/*     */   {
/* 450 */     parse();
/* 451 */     throwIfError();
/*     */     
/*     */ 
/* 454 */     Collection<List<Part>> values = this._parts.values();
/* 455 */     List<Part> parts = new ArrayList();
/* 456 */     for (List<Part> o : values)
/*     */     {
/* 458 */       List<Part> asList = LazyList.getList(o, false);
/* 459 */       parts.addAll(asList);
/*     */     }
/* 461 */     return parts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Part getPart(String name)
/*     */     throws IOException
/*     */   {
/* 475 */     parse();
/* 476 */     throwIfError();
/* 477 */     return (Part)this._parts.getValue(name, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void throwIfError()
/*     */     throws IOException
/*     */   {
/* 488 */     if (this._err != null)
/*     */     {
/* 490 */       if ((this._err instanceof IOException))
/* 491 */         throw ((IOException)this._err);
/* 492 */       if ((this._err instanceof IllegalStateException))
/* 493 */         throw ((IllegalStateException)this._err);
/* 494 */       throw new IllegalStateException(this._err);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void parse()
/*     */   {
/* 505 */     if ((this._parts != null) || (this._err != null)) {
/* 506 */       return;
/*     */     }
/*     */     
/*     */ 
/* 510 */     long total = 0L;
/* 511 */     this._parts = new MultiMap();
/*     */     
/*     */ 
/* 514 */     if ((this._contentType == null) || (!this._contentType.startsWith("multipart/form-data"))) {
/* 515 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 521 */       if (this._config.getLocation() == null) {
/* 522 */         this._tmpDir = this._contextTmpDir;
/* 523 */       } else if ("".equals(this._config.getLocation())) {
/* 524 */         this._tmpDir = this._contextTmpDir;
/*     */       }
/*     */       else {
/* 527 */         File f = new File(this._config.getLocation());
/* 528 */         if (f.isAbsolute()) {
/* 529 */           this._tmpDir = f;
/*     */         } else {
/* 531 */           this._tmpDir = new File(this._contextTmpDir, this._config.getLocation());
/*     */         }
/*     */       }
/* 534 */       if (!this._tmpDir.exists()) {
/* 535 */         this._tmpDir.mkdirs();
/*     */       }
/* 537 */       String contentTypeBoundary = "";
/* 538 */       int bstart = this._contentType.indexOf("boundary=");
/* 539 */       if (bstart >= 0)
/*     */       {
/* 541 */         int bend = this._contentType.indexOf(";", bstart);
/* 542 */         bend = bend < 0 ? this._contentType.length() : bend;
/* 543 */         contentTypeBoundary = QuotedStringTokenizer.unquote(value(this._contentType.substring(bstart, bend)).trim());
/*     */       }
/*     */       
/* 546 */       String boundary = "--" + contentTypeBoundary;
/* 547 */       String lastBoundary = boundary + "--";
/* 548 */       byte[] byteBoundary = lastBoundary.getBytes(StandardCharsets.ISO_8859_1);
/*     */       
/*     */ 
/* 551 */       String line = null;
/*     */       try
/*     */       {
/* 554 */         line = ((ReadLineInputStream)this._in).readLine();
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 558 */         LOG.warn("Badly formatted multipart request", new Object[0]);
/* 559 */         throw e;
/*     */       }
/*     */       
/* 562 */       if (line == null) {
/* 563 */         throw new IOException("Missing content for multipart request");
/*     */       }
/* 565 */       boolean badFormatLogged = false;
/* 566 */       line = line.trim();
/* 567 */       while ((line != null) && (!line.equals(boundary)) && (!line.equals(lastBoundary)))
/*     */       {
/* 569 */         if (!badFormatLogged)
/*     */         {
/* 571 */           LOG.warn("Badly formatted multipart request", new Object[0]);
/* 572 */           badFormatLogged = true;
/*     */         }
/* 574 */         line = ((ReadLineInputStream)this._in).readLine();
/* 575 */         line = line == null ? line : line.trim();
/*     */       }
/*     */       
/* 578 */       if ((line == null) || (line.length() == 0)) {
/* 579 */         throw new IOException("Missing initial multi part boundary");
/*     */       }
/*     */       
/* 582 */       if (line.equals(lastBoundary)) {
/* 583 */         return;
/*     */       }
/*     */       
/* 586 */       boolean lastPart = false;
/*     */       
/* 588 */       while (!lastPart)
/*     */       {
/* 590 */         String contentDisposition = null;
/* 591 */         String contentType = null;
/* 592 */         String contentTransferEncoding = null;
/*     */         
/* 594 */         MultiMap<String> headers = new MultiMap();
/*     */         for (;;)
/*     */         {
/* 597 */           line = ((ReadLineInputStream)this._in).readLine();
/*     */           
/*     */ 
/* 600 */           if (line == null) {
/*     */             break label1402;
/*     */           }
/*     */           
/* 604 */           if ("".equals(line)) {
/*     */             break;
/*     */           }
/* 607 */           total += line.length();
/* 608 */           if ((this._config.getMaxRequestSize() > 0L) && (total > this._config.getMaxRequestSize())) {
/* 609 */             throw new IllegalStateException("Request exceeds maxRequestSize (" + this._config.getMaxRequestSize() + ")");
/*     */           }
/*     */           
/* 612 */           int c = line.indexOf(':', 0);
/* 613 */           if (c > 0)
/*     */           {
/* 615 */             String key = line.substring(0, c).trim().toLowerCase(Locale.ENGLISH);
/* 616 */             String value = line.substring(c + 1, line.length()).trim();
/* 617 */             headers.put(key, value);
/* 618 */             if (key.equalsIgnoreCase("content-disposition"))
/* 619 */               contentDisposition = value;
/* 620 */             if (key.equalsIgnoreCase("content-type"))
/* 621 */               contentType = value;
/* 622 */             if (key.equals("content-transfer-encoding")) {
/* 623 */               contentTransferEncoding = value;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 628 */         boolean form_data = false;
/* 629 */         if (contentDisposition == null)
/*     */         {
/* 631 */           throw new IOException("Missing content-disposition");
/*     */         }
/*     */         
/* 634 */         QuotedStringTokenizer tok = new QuotedStringTokenizer(contentDisposition, ";", false, true);
/* 635 */         String name = null;
/* 636 */         String filename = null;
/* 637 */         while (tok.hasMoreTokens())
/*     */         {
/* 639 */           String t = tok.nextToken().trim();
/* 640 */           String tl = t.toLowerCase(Locale.ENGLISH);
/* 641 */           if (t.startsWith("form-data")) {
/* 642 */             form_data = true;
/* 643 */           } else if (tl.startsWith("name=")) {
/* 644 */             name = value(t);
/* 645 */           } else if (tl.startsWith("filename=")) {
/* 646 */             filename = filenameValue(t);
/*     */           }
/*     */         }
/*     */         
/* 650 */         if ((form_data) && 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 659 */           (name != null))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 665 */           MultiPart part = new MultiPart(name, filename);
/* 666 */           part.setHeaders(headers);
/* 667 */           part.setContentType(contentType);
/* 668 */           this._parts.add(name, part);
/* 669 */           part.open();
/*     */           
/* 671 */           InputStream partInput = null;
/* 672 */           if ("base64".equalsIgnoreCase(contentTransferEncoding))
/*     */           {
/* 674 */             partInput = new Base64InputStream((ReadLineInputStream)this._in);
/*     */           }
/* 676 */           else if ("quoted-printable".equalsIgnoreCase(contentTransferEncoding))
/*     */           {
/* 678 */             partInput = new FilterInputStream(this._in)
/*     */             {
/*     */               public int read()
/*     */                 throws IOException
/*     */               {
/* 683 */                 int c = this.in.read();
/* 684 */                 if ((c >= 0) && (c == 61))
/*     */                 {
/* 686 */                   int hi = this.in.read();
/* 687 */                   int lo = this.in.read();
/* 688 */                   if ((hi < 0) || (lo < 0))
/*     */                   {
/* 690 */                     throw new IOException("Unexpected end to quoted-printable byte");
/*     */                   }
/* 692 */                   char[] chars = { (char)hi, (char)lo };
/* 693 */                   c = Integer.parseInt(new String(chars), 16);
/*     */                 }
/* 695 */                 return c;
/*     */               }
/*     */               
/*     */             };
/*     */           } else {
/* 700 */             partInput = this._in;
/*     */           }
/*     */           
/*     */           try
/*     */           {
/* 705 */             int state = -2;
/*     */             
/* 707 */             boolean cr = false;
/* 708 */             boolean lf = false;
/*     */             
/*     */ 
/*     */             for (;;)
/*     */             {
/* 713 */               int b = 0;
/* 714 */               int c; while ((c = state != -2 ? state : partInput.read()) != -1)
/*     */               {
/* 716 */                 total += 1L;
/* 717 */                 if ((this._config.getMaxRequestSize() > 0L) && (total > this._config.getMaxRequestSize())) {
/* 718 */                   throw new IllegalStateException("Request exceeds maxRequestSize (" + this._config.getMaxRequestSize() + ")");
/*     */                 }
/* 720 */                 state = -2;
/*     */                 
/*     */ 
/* 723 */                 if ((c == 13) || (c == 10))
/*     */                 {
/* 725 */                   if (c != 13)
/*     */                     break;
/* 727 */                   partInput.mark(1);
/* 728 */                   int tmp = partInput.read();
/* 729 */                   if (tmp != 10) {
/* 730 */                     partInput.reset();
/*     */                   } else
/* 732 */                     state = tmp;
/* 733 */                   break;
/*     */                 }
/*     */                 
/*     */ 
/*     */ 
/* 738 */                 if ((b >= 0) && (b < byteBoundary.length) && (c == byteBoundary[b]))
/*     */                 {
/* 740 */                   b++;
/*     */ 
/*     */                 }
/*     */                 else
/*     */                 {
/*     */ 
/* 746 */                   if (cr) {
/* 747 */                     part.write(13);
/*     */                   }
/* 749 */                   if (lf) {
/* 750 */                     part.write(10);
/*     */                   }
/* 752 */                   cr = lf = 0;
/* 753 */                   if (b > 0) {
/* 754 */                     part.write(byteBoundary, 0, b);
/*     */                   }
/* 756 */                   b = -1;
/* 757 */                   part.write(c);
/*     */                 }
/*     */               }
/*     */               
/*     */ 
/* 762 */               if (((b > 0) && (b < byteBoundary.length - 2)) || (b == byteBoundary.length - 1))
/*     */               {
/* 764 */                 if (cr) {
/* 765 */                   part.write(13);
/*     */                 }
/* 767 */                 if (lf) {
/* 768 */                   part.write(10);
/*     */                 }
/* 770 */                 cr = lf = 0;
/* 771 */                 part.write(byteBoundary, 0, b);
/* 772 */                 b = -1;
/*     */               }
/*     */               
/*     */ 
/* 776 */               if ((b > 0) || (c == -1))
/*     */               {
/*     */ 
/* 779 */                 if (b == byteBoundary.length)
/* 780 */                   lastPart = true;
/* 781 */                 if (state != 10) break;
/* 782 */                 state = -2; break;
/*     */               }
/*     */               
/*     */ 
/*     */ 
/* 787 */               if (cr) {
/* 788 */                 part.write(13);
/*     */               }
/* 790 */               if (lf) {
/* 791 */                 part.write(10);
/*     */               }
/* 793 */               cr = c == 13;
/* 794 */               lf = (c == 10) || (state == 10);
/* 795 */               if (state == 10) {
/* 796 */                 state = -2;
/*     */               }
/*     */             }
/*     */           }
/*     */           finally {
/* 801 */             part.close();
/*     */           } } }
/*     */       label1402:
/* 804 */       if (lastPart)
/*     */       {
/* 806 */         while (line != null) {
/* 807 */           line = ((ReadLineInputStream)this._in).readLine();
/*     */         }
/*     */       }
/* 810 */       throw new IOException("Incomplete parts");
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 814 */       this._err = e;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDeleteOnExit(boolean deleteOnExit)
/*     */   {
/* 820 */     this._deleteOnExit = deleteOnExit;
/*     */   }
/*     */   
/*     */   public void setWriteFilesWithFilenames(boolean writeFilesWithFilenames)
/*     */   {
/* 825 */     this._writeFilesWithFilenames = writeFilesWithFilenames;
/*     */   }
/*     */   
/*     */   public boolean isWriteFilesWithFilenames()
/*     */   {
/* 830 */     return this._writeFilesWithFilenames;
/*     */   }
/*     */   
/*     */   public boolean isDeleteOnExit()
/*     */   {
/* 835 */     return this._deleteOnExit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String value(String nameEqualsValue)
/*     */   {
/* 842 */     int idx = nameEqualsValue.indexOf('=');
/* 843 */     String value = nameEqualsValue.substring(idx + 1).trim();
/* 844 */     return QuotedStringTokenizer.unquoteOnly(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String filenameValue(String nameEqualsValue)
/*     */   {
/* 851 */     int idx = nameEqualsValue.indexOf('=');
/* 852 */     String value = nameEqualsValue.substring(idx + 1).trim();
/*     */     
/* 854 */     if (value.matches(".??[a-z,A-Z]\\:\\\\[^\\\\].*"))
/*     */     {
/*     */ 
/*     */ 
/* 858 */       char first = value.charAt(0);
/* 859 */       if ((first == '"') || (first == '\''))
/* 860 */         value = value.substring(1);
/* 861 */       char last = value.charAt(value.length() - 1);
/* 862 */       if ((last == '"') || (last == '\'')) {
/* 863 */         value = value.substring(0, value.length() - 1);
/*     */       }
/* 865 */       return value;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 872 */     return QuotedStringTokenizer.unquoteOnly(value, true);
/*     */   }
/*     */   
/*     */ 
/*     */   private static class Base64InputStream
/*     */     extends InputStream
/*     */   {
/*     */     ReadLineInputStream _in;
/*     */     
/*     */     String _line;
/*     */     byte[] _buffer;
/*     */     int _pos;
/*     */     
/*     */     public Base64InputStream(ReadLineInputStream rlis)
/*     */     {
/* 887 */       this._in = rlis;
/*     */     }
/*     */     
/*     */     public int read()
/*     */       throws IOException
/*     */     {
/* 893 */       if ((this._buffer == null) || (this._pos >= this._buffer.length))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 899 */         this._line = this._in.readLine();
/* 900 */         if (this._line == null)
/* 901 */           return -1;
/* 902 */         if (this._line.startsWith("--")) {
/* 903 */           this._buffer = (this._line + "\r\n").getBytes();
/* 904 */         } else if (this._line.length() == 0) {
/* 905 */           this._buffer = "\r\n".getBytes();
/*     */         }
/*     */         else {
/* 908 */           ByteArrayOutputStream baos = new ByteArrayOutputStream(4 * this._line.length() / 3 + 2);
/* 909 */           B64Code.decode(this._line, baos);
/* 910 */           baos.write(13);
/* 911 */           baos.write(10);
/* 912 */           this._buffer = baos.toByteArray();
/*     */         }
/*     */         
/* 915 */         this._pos = 0;
/*     */       }
/*     */       
/* 918 */       return this._buffer[(this._pos++)];
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\MultiPartInputStreamParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */