/*      */ package io.netty.handler.codec.http.multipart;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.channel.ChannelHandlerContext;
/*      */ import io.netty.handler.codec.DecoderResult;
/*      */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*      */ import io.netty.handler.codec.http.DefaultHttpContent;
/*      */ import io.netty.handler.codec.http.EmptyHttpHeaders;
/*      */ import io.netty.handler.codec.http.FullHttpRequest;
/*      */ import io.netty.handler.codec.http.HttpConstants;
/*      */ import io.netty.handler.codec.http.HttpContent;
/*      */ import io.netty.handler.codec.http.HttpHeaderNames;
/*      */ import io.netty.handler.codec.http.HttpHeaderValues;
/*      */ import io.netty.handler.codec.http.HttpHeaders;
/*      */ import io.netty.handler.codec.http.HttpMethod;
/*      */ import io.netty.handler.codec.http.HttpRequest;
/*      */ import io.netty.handler.codec.http.HttpUtil;
/*      */ import io.netty.handler.codec.http.HttpVersion;
/*      */ import io.netty.handler.codec.http.LastHttpContent;
/*      */ import io.netty.handler.stream.ChunkedInput;
/*      */ import io.netty.util.AsciiString;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.URLEncoder;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.AbstractMap.SimpleImmutableEntry;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Random;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class HttpPostRequestEncoder
/*      */   implements ChunkedInput<HttpContent>
/*      */ {
/*      */   public static enum EncoderMode
/*      */   {
/*   77 */     RFC1738, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*   82 */     RFC3986, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   93 */     HTML5;
/*      */     
/*      */ 
/*      */     private EncoderMode() {}
/*      */   }
/*      */   
/*      */ 
/*  100 */   private static final Map.Entry[] percentEncodings = { new AbstractMap.SimpleImmutableEntry(
/*  101 */     Pattern.compile("\\*"), "%2A"), new AbstractMap.SimpleImmutableEntry(
/*  102 */     Pattern.compile("\\+"), "%20"), new AbstractMap.SimpleImmutableEntry(
/*  103 */     Pattern.compile("~"), "%7E") };
/*      */   
/*      */ 
/*      */   private final HttpDataFactory factory;
/*      */   
/*      */ 
/*      */   private final HttpRequest request;
/*      */   
/*      */ 
/*      */   private final Charset charset;
/*      */   
/*      */ 
/*      */   private boolean isChunked;
/*      */   
/*      */ 
/*      */   private final List<InterfaceHttpData> bodyListDatas;
/*      */   
/*      */ 
/*      */   final List<InterfaceHttpData> multipartHttpDatas;
/*      */   
/*      */ 
/*      */   private final boolean isMultipart;
/*      */   
/*      */ 
/*      */   String multipartDataBoundary;
/*      */   
/*      */ 
/*      */   String multipartMixedBoundary;
/*      */   
/*      */ 
/*      */   private boolean headerFinalized;
/*      */   
/*      */ 
/*      */   private final EncoderMode encoderMode;
/*      */   
/*      */ 
/*      */   private boolean isLastChunk;
/*      */   
/*      */ 
/*      */   private boolean isLastChunkSent;
/*      */   
/*      */ 
/*      */   private FileUpload currentFileUpload;
/*      */   
/*      */ 
/*      */   private boolean duringMixedMode;
/*      */   
/*      */ 
/*      */   private long globalBodySize;
/*      */   
/*      */ 
/*      */   private long globalProgress;
/*      */   
/*      */ 
/*      */   private ListIterator<InterfaceHttpData> iterator;
/*      */   
/*      */ 
/*      */   private ByteBuf currentBuffer;
/*      */   
/*      */ 
/*      */   private InterfaceHttpData currentData;
/*      */   
/*      */ 
/*      */   public HttpPostRequestEncoder(HttpRequest request, boolean multipart)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  169 */     this(new DefaultHttpDataFactory(16384L), request, multipart, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpPostRequestEncoder(HttpDataFactory factory, HttpRequest request, boolean multipart)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  188 */     this(factory, request, multipart, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpPostRequestEncoder(HttpDataFactory factory, HttpRequest request, boolean multipart, Charset charset, EncoderMode encoderMode)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  212 */     this.request = ((HttpRequest)ObjectUtil.checkNotNull(request, "request"));
/*  213 */     this.charset = ((Charset)ObjectUtil.checkNotNull(charset, "charset"));
/*  214 */     this.factory = ((HttpDataFactory)ObjectUtil.checkNotNull(factory, "factory"));
/*  215 */     if (HttpMethod.TRACE.equals(request.method())) {
/*  216 */       throw new ErrorDataEncoderException("Cannot create a Encoder if request is a TRACE");
/*      */     }
/*      */     
/*  219 */     this.bodyListDatas = new ArrayList();
/*      */     
/*  221 */     this.isLastChunk = false;
/*  222 */     this.isLastChunkSent = false;
/*  223 */     this.isMultipart = multipart;
/*  224 */     this.multipartHttpDatas = new ArrayList();
/*  225 */     this.encoderMode = encoderMode;
/*  226 */     if (this.isMultipart) {
/*  227 */       initDataMultipart();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void cleanFiles()
/*      */   {
/*  235 */     this.factory.cleanRequestHttpData(this.request);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isMultipart()
/*      */   {
/*  269 */     return this.isMultipart;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void initDataMultipart()
/*      */   {
/*  276 */     this.multipartDataBoundary = getNewMultipartDelimiter();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void initMixedMultipart()
/*      */   {
/*  283 */     this.multipartMixedBoundary = getNewMultipartDelimiter();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String getNewMultipartDelimiter()
/*      */   {
/*  292 */     return Long.toHexString(PlatformDependent.threadLocalRandom().nextLong());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<InterfaceHttpData> getBodyListAttributes()
/*      */   {
/*  301 */     return this.bodyListDatas;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBodyHttpDatas(List<InterfaceHttpData> datas)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  313 */     if (datas == null) {
/*  314 */       throw new NullPointerException("datas");
/*      */     }
/*  316 */     this.globalBodySize = 0L;
/*  317 */     this.bodyListDatas.clear();
/*  318 */     this.currentFileUpload = null;
/*  319 */     this.duringMixedMode = false;
/*  320 */     this.multipartHttpDatas.clear();
/*  321 */     for (InterfaceHttpData data : datas) {
/*  322 */       addBodyHttpData(data);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addBodyAttribute(String name, String value)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  339 */     String svalue = value != null ? value : "";
/*  340 */     Attribute data = this.factory.createAttribute(this.request, (String)ObjectUtil.checkNotNull(name, "name"), svalue);
/*  341 */     addBodyHttpData(data);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addBodyFileUpload(String name, File file, String contentType, boolean isText)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  362 */     addBodyFileUpload(name, file.getName(), file, contentType, isText);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addBodyFileUpload(String name, String filename, File file, String contentType, boolean isText)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  386 */     ObjectUtil.checkNotNull(name, "name");
/*  387 */     ObjectUtil.checkNotNull(file, "file");
/*  388 */     if (filename == null) {
/*  389 */       filename = "";
/*      */     }
/*  391 */     String scontentType = contentType;
/*  392 */     String contentTransferEncoding = null;
/*  393 */     if (contentType == null) {
/*  394 */       if (isText) {
/*  395 */         scontentType = "text/plain";
/*      */       } else {
/*  397 */         scontentType = "application/octet-stream";
/*      */       }
/*      */     }
/*  400 */     if (!isText) {
/*  401 */       contentTransferEncoding = HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value();
/*      */     }
/*  403 */     FileUpload fileUpload = this.factory.createFileUpload(this.request, name, filename, scontentType, contentTransferEncoding, null, file
/*  404 */       .length());
/*      */     try {
/*  406 */       fileUpload.setContent(file);
/*      */     } catch (IOException e) {
/*  408 */       throw new ErrorDataEncoderException(e);
/*      */     }
/*  410 */     addBodyHttpData(fileUpload);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addBodyFileUploads(String name, File[] file, String[] contentType, boolean[] isText)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  431 */     if ((file.length != contentType.length) && (file.length != isText.length)) {
/*  432 */       throw new IllegalArgumentException("Different array length");
/*      */     }
/*  434 */     for (int i = 0; i < file.length; i++) {
/*  435 */       addBodyFileUpload(name, file[i], contentType[i], isText[i]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addBodyHttpData(InterfaceHttpData data)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  448 */     if (this.headerFinalized) {
/*  449 */       throw new ErrorDataEncoderException("Cannot add value once finalized");
/*      */     }
/*  451 */     this.bodyListDatas.add(ObjectUtil.checkNotNull(data, "data"));
/*  452 */     if (!this.isMultipart) {
/*  453 */       if ((data instanceof Attribute)) {
/*  454 */         Attribute attribute = (Attribute)data;
/*      */         try
/*      */         {
/*  457 */           String key = encodeAttribute(attribute.getName(), this.charset);
/*  458 */           String value = encodeAttribute(attribute.getValue(), this.charset);
/*  459 */           Attribute newattribute = this.factory.createAttribute(this.request, key, value);
/*  460 */           this.multipartHttpDatas.add(newattribute);
/*  461 */           this.globalBodySize += newattribute.getName().length() + 1 + newattribute.length() + 1L;
/*      */         } catch (IOException e) {
/*  463 */           throw new ErrorDataEncoderException(e);
/*      */         }
/*  465 */       } else if ((data instanceof FileUpload))
/*      */       {
/*  467 */         FileUpload fileUpload = (FileUpload)data;
/*      */         
/*  469 */         String key = encodeAttribute(fileUpload.getName(), this.charset);
/*  470 */         String value = encodeAttribute(fileUpload.getFilename(), this.charset);
/*  471 */         Attribute newattribute = this.factory.createAttribute(this.request, key, value);
/*  472 */         this.multipartHttpDatas.add(newattribute);
/*  473 */         this.globalBodySize += newattribute.getName().length() + 1 + newattribute.length() + 1L;
/*      */       }
/*  475 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  509 */     if ((data instanceof Attribute)) {
/*  510 */       if (this.duringMixedMode) {
/*  511 */         InternalAttribute internal = new InternalAttribute(this.charset);
/*  512 */         internal.addValue("\r\n--" + this.multipartMixedBoundary + "--");
/*  513 */         this.multipartHttpDatas.add(internal);
/*  514 */         this.multipartMixedBoundary = null;
/*  515 */         this.currentFileUpload = null;
/*  516 */         this.duringMixedMode = false;
/*      */       }
/*  518 */       InternalAttribute internal = new InternalAttribute(this.charset);
/*  519 */       if (!this.multipartHttpDatas.isEmpty())
/*      */       {
/*  521 */         internal.addValue("\r\n");
/*      */       }
/*  523 */       internal.addValue("--" + this.multipartDataBoundary + "\r\n");
/*      */       
/*  525 */       Attribute attribute = (Attribute)data;
/*  526 */       internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + attribute
/*  527 */         .getName() + "\"\r\n");
/*      */       
/*  529 */       internal.addValue(HttpHeaderNames.CONTENT_LENGTH + ": " + attribute
/*  530 */         .length() + "\r\n");
/*  531 */       Charset localcharset = attribute.getCharset();
/*  532 */       if (localcharset != null)
/*      */       {
/*  534 */         internal.addValue(HttpHeaderNames.CONTENT_TYPE + ": " + "text/plain" + "; " + HttpHeaderValues.CHARSET + '=' + localcharset
/*      */         
/*      */ 
/*  537 */           .name() + "\r\n");
/*      */       }
/*      */       
/*  540 */       internal.addValue("\r\n");
/*  541 */       this.multipartHttpDatas.add(internal);
/*  542 */       this.multipartHttpDatas.add(data);
/*  543 */       this.globalBodySize += attribute.length() + internal.size();
/*  544 */     } else if ((data instanceof FileUpload)) {
/*  545 */       FileUpload fileUpload = (FileUpload)data;
/*  546 */       InternalAttribute internal = new InternalAttribute(this.charset);
/*  547 */       if (!this.multipartHttpDatas.isEmpty())
/*      */       {
/*  549 */         internal.addValue("\r\n");
/*      */       }
/*      */       boolean localMixed;
/*  552 */       if (this.duringMixedMode) { boolean localMixed;
/*  553 */         if ((this.currentFileUpload != null) && (this.currentFileUpload.getName().equals(fileUpload.getName())))
/*      */         {
/*      */ 
/*  556 */           localMixed = true;
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  563 */           internal.addValue("--" + this.multipartMixedBoundary + "--");
/*  564 */           this.multipartHttpDatas.add(internal);
/*  565 */           this.multipartMixedBoundary = null;
/*      */           
/*      */ 
/*  568 */           internal = new InternalAttribute(this.charset);
/*  569 */           internal.addValue("\r\n");
/*  570 */           boolean localMixed = false;
/*      */           
/*  572 */           this.currentFileUpload = fileUpload;
/*  573 */           this.duringMixedMode = false;
/*      */         }
/*      */       }
/*  576 */       else if ((this.encoderMode != EncoderMode.HTML5) && (this.currentFileUpload != null) && 
/*  577 */         (this.currentFileUpload.getName().equals(fileUpload.getName())))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  598 */         initMixedMultipart();
/*  599 */         InternalAttribute pastAttribute = (InternalAttribute)this.multipartHttpDatas.get(this.multipartHttpDatas
/*  600 */           .size() - 2);
/*      */         
/*  602 */         this.globalBodySize -= pastAttribute.size();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  635 */         StringBuilder replacement = new StringBuilder(139 + this.multipartDataBoundary.length() + this.multipartMixedBoundary.length() * 2 + fileUpload.getFilename().length() + fileUpload.getName().length()).append("--").append(this.multipartDataBoundary).append("\r\n").append(HttpHeaderNames.CONTENT_DISPOSITION).append(": ").append(HttpHeaderValues.FORM_DATA).append("; ").append(HttpHeaderValues.NAME).append("=\"").append(fileUpload.getName()).append("\"\r\n").append(HttpHeaderNames.CONTENT_TYPE).append(": ").append(HttpHeaderValues.MULTIPART_MIXED).append("; ").append(HttpHeaderValues.BOUNDARY).append('=').append(this.multipartMixedBoundary).append("\r\n\r\n").append("--").append(this.multipartMixedBoundary).append("\r\n").append(HttpHeaderNames.CONTENT_DISPOSITION).append(": ").append(HttpHeaderValues.ATTACHMENT);
/*      */         
/*  637 */         if (!fileUpload.getFilename().isEmpty())
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  642 */           replacement.append("; ").append(HttpHeaderValues.FILENAME).append("=\"").append(fileUpload.getFilename()).append('"');
/*      */         }
/*      */         
/*  645 */         replacement.append("\r\n");
/*      */         
/*  647 */         pastAttribute.setValue(replacement.toString(), 1);
/*  648 */         pastAttribute.setValue("", 2);
/*      */         
/*      */ 
/*  651 */         this.globalBodySize += pastAttribute.size();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  657 */         boolean localMixed = true;
/*  658 */         this.duringMixedMode = true;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  663 */         localMixed = false;
/*  664 */         this.currentFileUpload = fileUpload;
/*  665 */         this.duringMixedMode = false;
/*      */       }
/*      */       
/*      */ 
/*  669 */       if (localMixed)
/*      */       {
/*      */ 
/*  672 */         internal.addValue("--" + this.multipartMixedBoundary + "\r\n");
/*      */         
/*  674 */         if (fileUpload.getFilename().isEmpty())
/*      */         {
/*  676 */           internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.ATTACHMENT + "\r\n");
/*      */         }
/*      */         else
/*      */         {
/*  680 */           internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.ATTACHMENT + "; " + HttpHeaderValues.FILENAME + "=\"" + fileUpload
/*      */           
/*  682 */             .getFilename() + "\"\r\n");
/*      */         }
/*      */       } else {
/*  685 */         internal.addValue("--" + this.multipartDataBoundary + "\r\n");
/*      */         
/*  687 */         if (fileUpload.getFilename().isEmpty())
/*      */         {
/*  689 */           internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + fileUpload
/*  690 */             .getName() + "\"\r\n");
/*      */         }
/*      */         else
/*      */         {
/*  694 */           internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + fileUpload
/*  695 */             .getName() + "\"; " + HttpHeaderValues.FILENAME + "=\"" + fileUpload
/*  696 */             .getFilename() + "\"\r\n");
/*      */         }
/*      */       }
/*      */       
/*  700 */       internal.addValue(HttpHeaderNames.CONTENT_LENGTH + ": " + fileUpload
/*  701 */         .length() + "\r\n");
/*      */       
/*      */ 
/*      */ 
/*  705 */       internal.addValue(HttpHeaderNames.CONTENT_TYPE + ": " + fileUpload.getContentType());
/*  706 */       String contentTransferEncoding = fileUpload.getContentTransferEncoding();
/*  707 */       if ((contentTransferEncoding != null) && 
/*  708 */         (contentTransferEncoding.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value()))) {
/*  709 */         internal.addValue("\r\n" + HttpHeaderNames.CONTENT_TRANSFER_ENCODING + ": " + HttpPostBodyUtil.TransferEncodingMechanism.BINARY
/*  710 */           .value() + "\r\n\r\n");
/*  711 */       } else if (fileUpload.getCharset() != null) {
/*  712 */         internal.addValue("; " + HttpHeaderValues.CHARSET + '=' + fileUpload.getCharset().name() + "\r\n\r\n");
/*      */       } else {
/*  714 */         internal.addValue("\r\n\r\n");
/*      */       }
/*  716 */       this.multipartHttpDatas.add(internal);
/*  717 */       this.multipartHttpDatas.add(data);
/*  718 */       this.globalBodySize += fileUpload.length() + internal.size();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpRequest finalizeRequest()
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  739 */     if (!this.headerFinalized) {
/*  740 */       if (this.isMultipart) {
/*  741 */         InternalAttribute internal = new InternalAttribute(this.charset);
/*  742 */         if (this.duringMixedMode) {
/*  743 */           internal.addValue("\r\n--" + this.multipartMixedBoundary + "--");
/*      */         }
/*  745 */         internal.addValue("\r\n--" + this.multipartDataBoundary + "--\r\n");
/*  746 */         this.multipartHttpDatas.add(internal);
/*  747 */         this.multipartMixedBoundary = null;
/*  748 */         this.currentFileUpload = null;
/*  749 */         this.duringMixedMode = false;
/*  750 */         this.globalBodySize += internal.size();
/*      */       }
/*  752 */       this.headerFinalized = true;
/*      */     } else {
/*  754 */       throw new ErrorDataEncoderException("Header already encoded");
/*      */     }
/*      */     
/*  757 */     HttpHeaders headers = this.request.headers();
/*  758 */     List<String> contentTypes = headers.getAll(HttpHeaderNames.CONTENT_TYPE);
/*  759 */     List<String> transferEncoding = headers.getAll(HttpHeaderNames.TRANSFER_ENCODING);
/*  760 */     if (contentTypes != null) {
/*  761 */       headers.remove(HttpHeaderNames.CONTENT_TYPE);
/*  762 */       for (String contentType : contentTypes)
/*      */       {
/*  764 */         lowercased = contentType.toLowerCase();
/*  765 */         if ((!lowercased.startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString())) && 
/*  766 */           (!lowercased.startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())))
/*      */         {
/*      */ 
/*  769 */           headers.add(HttpHeaderNames.CONTENT_TYPE, contentType); }
/*      */       }
/*      */     }
/*      */     String lowercased;
/*  773 */     if (this.isMultipart) {
/*  774 */       String value = HttpHeaderValues.MULTIPART_FORM_DATA + "; " + HttpHeaderValues.BOUNDARY + '=' + this.multipartDataBoundary;
/*      */       
/*  776 */       headers.add(HttpHeaderNames.CONTENT_TYPE, value);
/*      */     }
/*      */     else {
/*  779 */       headers.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
/*      */     }
/*      */     
/*  782 */     long realSize = this.globalBodySize;
/*  783 */     if (!this.isMultipart) {
/*  784 */       realSize -= 1L;
/*      */     }
/*  786 */     this.iterator = this.multipartHttpDatas.listIterator();
/*      */     
/*  788 */     headers.set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(realSize));
/*  789 */     if ((realSize > 8096L) || (this.isMultipart)) {
/*  790 */       this.isChunked = true;
/*  791 */       if (transferEncoding != null) {
/*  792 */         headers.remove(HttpHeaderNames.TRANSFER_ENCODING);
/*  793 */         for (CharSequence v : transferEncoding) {
/*  794 */           if (!HttpHeaderValues.CHUNKED.contentEqualsIgnoreCase(v))
/*      */           {
/*      */ 
/*  797 */             headers.add(HttpHeaderNames.TRANSFER_ENCODING, v);
/*      */           }
/*      */         }
/*      */       }
/*  801 */       HttpUtil.setTransferEncodingChunked(this.request, true);
/*      */       
/*      */ 
/*  804 */       return new WrappedHttpRequest(this.request);
/*      */     }
/*      */     
/*  807 */     HttpContent chunk = nextChunk();
/*  808 */     if ((this.request instanceof FullHttpRequest)) {
/*  809 */       FullHttpRequest fullRequest = (FullHttpRequest)this.request;
/*  810 */       ByteBuf chunkContent = chunk.content();
/*  811 */       if (fullRequest.content() != chunkContent) {
/*  812 */         fullRequest.content().clear().writeBytes(chunkContent);
/*  813 */         chunkContent.release();
/*      */       }
/*  815 */       return fullRequest;
/*      */     }
/*  817 */     return new WrappedFullHttpRequest(this.request, chunk, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isChunked()
/*      */   {
/*  826 */     return this.isChunked;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String encodeAttribute(String s, Charset charset)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  838 */     if (s == null) {
/*  839 */       return "";
/*      */     }
/*      */     try {
/*  842 */       String encoded = URLEncoder.encode(s, charset.name());
/*  843 */       if (this.encoderMode == EncoderMode.RFC3986) {
/*  844 */         for (Map.Entry<Pattern, String> entry : percentEncodings) {
/*  845 */           String replacement = (String)entry.getValue();
/*  846 */           encoded = ((Pattern)entry.getKey()).matcher(encoded).replaceAll(replacement);
/*      */         }
/*      */       }
/*  849 */       return encoded;
/*      */     } catch (UnsupportedEncodingException e) {
/*  851 */       throw new ErrorDataEncoderException(charset.name(), e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  866 */   private boolean isKey = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private ByteBuf fillByteBuf()
/*      */   {
/*  873 */     int length = this.currentBuffer.readableBytes();
/*  874 */     if (length > 8096) {
/*  875 */       return this.currentBuffer.readRetainedSlice(8096);
/*      */     }
/*      */     
/*  878 */     ByteBuf slice = this.currentBuffer;
/*  879 */     this.currentBuffer = null;
/*  880 */     return slice;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private HttpContent encodeNextChunkMultipart(int sizeleft)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  895 */     if (this.currentData == null) {
/*  896 */       return null;
/*      */     }
/*      */     
/*  899 */     if ((this.currentData instanceof InternalAttribute)) {
/*  900 */       ByteBuf buffer = ((InternalAttribute)this.currentData).toByteBuf();
/*  901 */       this.currentData = null;
/*      */     } else {
/*      */       try {
/*  904 */         buffer = ((HttpData)this.currentData).getChunk(sizeleft);
/*      */       } catch (IOException e) { ByteBuf buffer;
/*  906 */         throw new ErrorDataEncoderException(e);
/*      */       }
/*  908 */       if (buffer.capacity() == 0)
/*      */       {
/*  910 */         this.currentData = null;
/*  911 */         return null;
/*      */       }
/*      */     }
/*  914 */     if (this.currentBuffer == null) {
/*  915 */       this.currentBuffer = buffer;
/*      */     } else {
/*  917 */       this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer });
/*      */     }
/*  919 */     if (this.currentBuffer.readableBytes() < 8096) {
/*  920 */       this.currentData = null;
/*  921 */       return null;
/*      */     }
/*  923 */     ByteBuf buffer = fillByteBuf();
/*  924 */     return new DefaultHttpContent(buffer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private HttpContent encodeNextChunkUrlEncoded(int sizeleft)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  938 */     if (this.currentData == null) {
/*  939 */       return null;
/*      */     }
/*  941 */     int size = sizeleft;
/*      */     
/*      */ 
/*      */ 
/*  945 */     if (this.isKey) {
/*  946 */       String key = this.currentData.getName();
/*  947 */       ByteBuf buffer = Unpooled.wrappedBuffer(key.getBytes());
/*  948 */       this.isKey = false;
/*  949 */       if (this.currentBuffer == null) {
/*  950 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { buffer, Unpooled.wrappedBuffer("=".getBytes()) });
/*      */       } else {
/*  952 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer, Unpooled.wrappedBuffer("=".getBytes()) });
/*      */       }
/*      */       
/*  955 */       size -= buffer.readableBytes() + 1;
/*  956 */       if (this.currentBuffer.readableBytes() >= 8096) {
/*  957 */         buffer = fillByteBuf();
/*  958 */         return new DefaultHttpContent(buffer);
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  964 */       buffer = ((HttpData)this.currentData).getChunk(size);
/*      */     } catch (IOException e) { ByteBuf buffer;
/*  966 */       throw new ErrorDataEncoderException(e);
/*      */     }
/*      */     
/*      */ 
/*  970 */     ByteBuf delimiter = null;
/*  971 */     if (buffer.readableBytes() < size) {
/*  972 */       this.isKey = true;
/*  973 */       delimiter = this.iterator.hasNext() ? Unpooled.wrappedBuffer("&".getBytes()) : null;
/*      */     }
/*      */     
/*      */ 
/*  977 */     if (buffer.capacity() == 0) {
/*  978 */       this.currentData = null;
/*  979 */       if (this.currentBuffer == null) {
/*  980 */         this.currentBuffer = delimiter;
/*      */       }
/*  982 */       else if (delimiter != null) {
/*  983 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, delimiter });
/*      */       }
/*      */       
/*  986 */       if (this.currentBuffer.readableBytes() >= 8096) {
/*  987 */         buffer = fillByteBuf();
/*  988 */         return new DefaultHttpContent(buffer);
/*      */       }
/*  990 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  994 */     if (this.currentBuffer == null) {
/*  995 */       if (delimiter != null) {
/*  996 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { buffer, delimiter });
/*      */       } else {
/*  998 */         this.currentBuffer = buffer;
/*      */       }
/*      */     }
/* 1001 */     else if (delimiter != null) {
/* 1002 */       this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer, delimiter });
/*      */     } else {
/* 1004 */       this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1009 */     if (this.currentBuffer.readableBytes() < 8096) {
/* 1010 */       this.currentData = null;
/* 1011 */       this.isKey = true;
/* 1012 */       return null;
/*      */     }
/*      */     
/* 1015 */     ByteBuf buffer = fillByteBuf();
/* 1016 */     return new DefaultHttpContent(buffer);
/*      */   }
/*      */   
/*      */ 
/*      */   public void close()
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */   @Deprecated
/*      */   public HttpContent readChunk(ChannelHandlerContext ctx)
/*      */     throws Exception
/*      */   {
/* 1028 */     return readChunk(ctx.alloc());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpContent readChunk(ByteBufAllocator allocator)
/*      */     throws Exception
/*      */   {
/* 1041 */     if (this.isLastChunkSent) {
/* 1042 */       return null;
/*      */     }
/* 1044 */     HttpContent nextChunk = nextChunk();
/* 1045 */     this.globalProgress += nextChunk.content().readableBytes();
/* 1046 */     return nextChunk;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private HttpContent nextChunk()
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/* 1059 */     if (this.isLastChunk) {
/* 1060 */       this.isLastChunkSent = true;
/* 1061 */       return LastHttpContent.EMPTY_LAST_CONTENT;
/*      */     }
/*      */     
/* 1064 */     int size = calculateRemainingSize();
/* 1065 */     if (size <= 0)
/*      */     {
/* 1067 */       ByteBuf buffer = fillByteBuf();
/* 1068 */       return new DefaultHttpContent(buffer);
/*      */     }
/*      */     
/* 1071 */     if (this.currentData != null) {
/*      */       HttpContent chunk;
/*      */       HttpContent chunk;
/* 1074 */       if (this.isMultipart) {
/* 1075 */         chunk = encodeNextChunkMultipart(size);
/*      */       } else {
/* 1077 */         chunk = encodeNextChunkUrlEncoded(size);
/*      */       }
/* 1079 */       if (chunk != null)
/*      */       {
/* 1081 */         return chunk;
/*      */       }
/* 1083 */       size = calculateRemainingSize();
/*      */     }
/* 1085 */     if (!this.iterator.hasNext()) {
/* 1086 */       return lastChunk();
/*      */     }
/* 1088 */     while ((size > 0) && (this.iterator.hasNext())) {
/* 1089 */       this.currentData = ((InterfaceHttpData)this.iterator.next());
/*      */       HttpContent chunk;
/* 1091 */       HttpContent chunk; if (this.isMultipart) {
/* 1092 */         chunk = encodeNextChunkMultipart(size);
/*      */       } else {
/* 1094 */         chunk = encodeNextChunkUrlEncoded(size);
/*      */       }
/* 1096 */       if (chunk == null)
/*      */       {
/* 1098 */         size = calculateRemainingSize();
/*      */       }
/*      */       else
/*      */       {
/* 1102 */         return chunk;
/*      */       }
/*      */     }
/* 1105 */     return lastChunk();
/*      */   }
/*      */   
/*      */   private int calculateRemainingSize() {
/* 1109 */     int size = 8096;
/* 1110 */     if (this.currentBuffer != null) {
/* 1111 */       size -= this.currentBuffer.readableBytes();
/*      */     }
/* 1113 */     return size;
/*      */   }
/*      */   
/*      */   private HttpContent lastChunk() {
/* 1117 */     this.isLastChunk = true;
/* 1118 */     if (this.currentBuffer == null) {
/* 1119 */       this.isLastChunkSent = true;
/*      */       
/* 1121 */       return LastHttpContent.EMPTY_LAST_CONTENT;
/*      */     }
/*      */     
/* 1124 */     ByteBuf buffer = this.currentBuffer;
/* 1125 */     this.currentBuffer = null;
/* 1126 */     return new DefaultHttpContent(buffer);
/*      */   }
/*      */   
/*      */   public boolean isEndOfInput() throws Exception
/*      */   {
/* 1131 */     return this.isLastChunkSent;
/*      */   }
/*      */   
/*      */   public long length()
/*      */   {
/* 1136 */     return this.isMultipart ? this.globalBodySize : this.globalBodySize - 1L;
/*      */   }
/*      */   
/*      */   public long progress()
/*      */   {
/* 1141 */     return this.globalProgress;
/*      */   }
/*      */   
/*      */ 
/*      */   public static class ErrorDataEncoderException
/*      */     extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 5020247425493164465L;
/*      */     
/*      */     public ErrorDataEncoderException() {}
/*      */     
/*      */     public ErrorDataEncoderException(String msg)
/*      */     {
/* 1154 */       super();
/*      */     }
/*      */     
/*      */     public ErrorDataEncoderException(Throwable cause) {
/* 1158 */       super();
/*      */     }
/*      */     
/*      */     public ErrorDataEncoderException(String msg, Throwable cause) {
/* 1162 */       super(cause);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class WrappedHttpRequest implements HttpRequest {
/*      */     private final HttpRequest request;
/*      */     
/* 1169 */     WrappedHttpRequest(HttpRequest request) { this.request = request; }
/*      */     
/*      */ 
/*      */     public HttpRequest setProtocolVersion(HttpVersion version)
/*      */     {
/* 1174 */       this.request.setProtocolVersion(version);
/* 1175 */       return this;
/*      */     }
/*      */     
/*      */     public HttpRequest setMethod(HttpMethod method)
/*      */     {
/* 1180 */       this.request.setMethod(method);
/* 1181 */       return this;
/*      */     }
/*      */     
/*      */     public HttpRequest setUri(String uri)
/*      */     {
/* 1186 */       this.request.setUri(uri);
/* 1187 */       return this;
/*      */     }
/*      */     
/*      */     public HttpMethod getMethod()
/*      */     {
/* 1192 */       return this.request.method();
/*      */     }
/*      */     
/*      */     public HttpMethod method()
/*      */     {
/* 1197 */       return this.request.method();
/*      */     }
/*      */     
/*      */     public String getUri()
/*      */     {
/* 1202 */       return this.request.uri();
/*      */     }
/*      */     
/*      */     public String uri()
/*      */     {
/* 1207 */       return this.request.uri();
/*      */     }
/*      */     
/*      */     public HttpVersion getProtocolVersion()
/*      */     {
/* 1212 */       return this.request.protocolVersion();
/*      */     }
/*      */     
/*      */     public HttpVersion protocolVersion()
/*      */     {
/* 1217 */       return this.request.protocolVersion();
/*      */     }
/*      */     
/*      */     public HttpHeaders headers()
/*      */     {
/* 1222 */       return this.request.headers();
/*      */     }
/*      */     
/*      */     public DecoderResult decoderResult()
/*      */     {
/* 1227 */       return this.request.decoderResult();
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public DecoderResult getDecoderResult()
/*      */     {
/* 1233 */       return this.request.getDecoderResult();
/*      */     }
/*      */     
/*      */     public void setDecoderResult(DecoderResult result)
/*      */     {
/* 1238 */       this.request.setDecoderResult(result);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class WrappedFullHttpRequest extends HttpPostRequestEncoder.WrappedHttpRequest implements FullHttpRequest {
/*      */     private final HttpContent content;
/*      */     
/*      */     private WrappedFullHttpRequest(HttpRequest request, HttpContent content) {
/* 1246 */       super();
/* 1247 */       this.content = content;
/*      */     }
/*      */     
/*      */     public FullHttpRequest setProtocolVersion(HttpVersion version)
/*      */     {
/* 1252 */       super.setProtocolVersion(version);
/* 1253 */       return this;
/*      */     }
/*      */     
/*      */     public FullHttpRequest setMethod(HttpMethod method)
/*      */     {
/* 1258 */       super.setMethod(method);
/* 1259 */       return this;
/*      */     }
/*      */     
/*      */     public FullHttpRequest setUri(String uri)
/*      */     {
/* 1264 */       super.setUri(uri);
/* 1265 */       return this;
/*      */     }
/*      */     
/*      */     public FullHttpRequest copy()
/*      */     {
/* 1270 */       return replace(content().copy());
/*      */     }
/*      */     
/*      */     public FullHttpRequest duplicate()
/*      */     {
/* 1275 */       return replace(content().duplicate());
/*      */     }
/*      */     
/*      */     public FullHttpRequest retainedDuplicate()
/*      */     {
/* 1280 */       return replace(content().retainedDuplicate());
/*      */     }
/*      */     
/*      */     public FullHttpRequest replace(ByteBuf content)
/*      */     {
/* 1285 */       DefaultFullHttpRequest duplicate = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), content);
/* 1286 */       duplicate.headers().set(headers());
/* 1287 */       duplicate.trailingHeaders().set(trailingHeaders());
/* 1288 */       return duplicate;
/*      */     }
/*      */     
/*      */     public FullHttpRequest retain(int increment)
/*      */     {
/* 1293 */       this.content.retain(increment);
/* 1294 */       return this;
/*      */     }
/*      */     
/*      */     public FullHttpRequest retain()
/*      */     {
/* 1299 */       this.content.retain();
/* 1300 */       return this;
/*      */     }
/*      */     
/*      */     public FullHttpRequest touch()
/*      */     {
/* 1305 */       this.content.touch();
/* 1306 */       return this;
/*      */     }
/*      */     
/*      */     public FullHttpRequest touch(Object hint)
/*      */     {
/* 1311 */       this.content.touch(hint);
/* 1312 */       return this;
/*      */     }
/*      */     
/*      */     public ByteBuf content()
/*      */     {
/* 1317 */       return this.content.content();
/*      */     }
/*      */     
/*      */     public HttpHeaders trailingHeaders()
/*      */     {
/* 1322 */       if ((this.content instanceof LastHttpContent)) {
/* 1323 */         return ((LastHttpContent)this.content).trailingHeaders();
/*      */       }
/* 1325 */       return EmptyHttpHeaders.INSTANCE;
/*      */     }
/*      */     
/*      */ 
/*      */     public int refCnt()
/*      */     {
/* 1331 */       return this.content.refCnt();
/*      */     }
/*      */     
/*      */     public boolean release()
/*      */     {
/* 1336 */       return this.content.release();
/*      */     }
/*      */     
/*      */     public boolean release(int decrement)
/*      */     {
/* 1341 */       return this.content.release(decrement);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\multipart\HttpPostRequestEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */