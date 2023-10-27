/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import io.netty.handler.codec.http.HttpContent;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.LastHttpContent;
/*     */ import io.netty.handler.codec.http.QueryStringDecoder;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpPostStandardRequestDecoder
/*     */   implements InterfaceHttpPostRequestDecoder
/*     */ {
/*     */   private final HttpDataFactory factory;
/*     */   private final HttpRequest request;
/*     */   private final Charset charset;
/*     */   private boolean isLastChunk;
/*  71 */   private final List<InterfaceHttpData> bodyListHttpData = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  76 */   private final Map<String, List<InterfaceHttpData>> bodyMapHttpData = new TreeMap(CaseIgnoringComparator.INSTANCE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ByteBuf undecodedChunk;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int bodyListHttpDataRank;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  92 */   private HttpPostRequestDecoder.MultiPartStatus currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
/*     */   
/*     */ 
/*     */   private Attribute currentAttribute;
/*     */   
/*     */ 
/*     */   private boolean destroyed;
/*     */   
/*     */ 
/* 101 */   private int discardThreshold = 10485760;
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
/*     */   public HttpPostStandardRequestDecoder(HttpRequest request)
/*     */   {
/* 114 */     this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
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
/*     */ 
/*     */ 
/*     */   public HttpPostStandardRequestDecoder(HttpDataFactory factory, HttpRequest request)
/*     */   {
/* 130 */     this(factory, request, HttpConstants.DEFAULT_CHARSET);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpPostStandardRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset)
/*     */   {
/* 148 */     this.request = ((HttpRequest)ObjectUtil.checkNotNull(request, "request"));
/* 149 */     this.charset = ((Charset)ObjectUtil.checkNotNull(charset, "charset"));
/* 150 */     this.factory = ((HttpDataFactory)ObjectUtil.checkNotNull(factory, "factory"));
/* 151 */     if ((request instanceof HttpContent))
/*     */     {
/*     */ 
/* 154 */       offer((HttpContent)request);
/*     */     } else {
/* 156 */       this.undecodedChunk = Unpooled.buffer();
/* 157 */       parseBody();
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkDestroyed() {
/* 162 */     if (this.destroyed) {
/* 163 */       throw new IllegalStateException(HttpPostStandardRequestDecoder.class.getSimpleName() + " was destroyed already");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMultipart()
/*     */   {
/* 175 */     checkDestroyed();
/* 176 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDiscardThreshold(int discardThreshold)
/*     */   {
/* 186 */     this.discardThreshold = ObjectUtil.checkPositiveOrZero(discardThreshold, "discardThreshold");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getDiscardThreshold()
/*     */   {
/* 194 */     return this.discardThreshold;
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
/*     */ 
/*     */   public List<InterfaceHttpData> getBodyHttpDatas()
/*     */   {
/* 209 */     checkDestroyed();
/*     */     
/* 211 */     if (!this.isLastChunk) {
/* 212 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*     */     }
/* 214 */     return this.bodyListHttpData;
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
/*     */ 
/*     */ 
/*     */   public List<InterfaceHttpData> getBodyHttpDatas(String name)
/*     */   {
/* 230 */     checkDestroyed();
/*     */     
/* 232 */     if (!this.isLastChunk) {
/* 233 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*     */     }
/* 235 */     return (List)this.bodyMapHttpData.get(name);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public InterfaceHttpData getBodyHttpData(String name)
/*     */   {
/* 252 */     checkDestroyed();
/*     */     
/* 254 */     if (!this.isLastChunk) {
/* 255 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*     */     }
/* 257 */     List<InterfaceHttpData> list = (List)this.bodyMapHttpData.get(name);
/* 258 */     if (list != null) {
/* 259 */       return (InterfaceHttpData)list.get(0);
/*     */     }
/* 261 */     return null;
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
/*     */   public HttpPostStandardRequestDecoder offer(HttpContent content)
/*     */   {
/* 275 */     checkDestroyed();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 280 */     ByteBuf buf = content.content();
/* 281 */     if (this.undecodedChunk == null) {
/* 282 */       this.undecodedChunk = buf.copy();
/*     */     } else {
/* 284 */       this.undecodedChunk.writeBytes(buf);
/*     */     }
/* 286 */     if ((content instanceof LastHttpContent)) {
/* 287 */       this.isLastChunk = true;
/*     */     }
/* 289 */     parseBody();
/* 290 */     if ((this.undecodedChunk != null) && (this.undecodedChunk.writerIndex() > this.discardThreshold)) {
/* 291 */       this.undecodedChunk.discardReadBytes();
/*     */     }
/* 293 */     return this;
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
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/* 308 */     checkDestroyed();
/*     */     
/* 310 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE)
/*     */     {
/* 312 */       if (this.bodyListHttpDataRank >= this.bodyListHttpData.size()) {
/* 313 */         throw new HttpPostRequestDecoder.EndOfDataDecoderException();
/*     */       }
/*     */     }
/* 316 */     return (!this.bodyListHttpData.isEmpty()) && (this.bodyListHttpDataRank < this.bodyListHttpData.size());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public InterfaceHttpData next()
/*     */   {
/* 333 */     checkDestroyed();
/*     */     
/* 335 */     if (hasNext()) {
/* 336 */       return (InterfaceHttpData)this.bodyListHttpData.get(this.bodyListHttpDataRank++);
/*     */     }
/* 338 */     return null;
/*     */   }
/*     */   
/*     */   public InterfaceHttpData currentPartialHttpData()
/*     */   {
/* 343 */     return this.currentAttribute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void parseBody()
/*     */   {
/* 354 */     if ((this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE) || (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE)) {
/* 355 */       if (this.isLastChunk) {
/* 356 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
/*     */       }
/* 358 */       return;
/*     */     }
/* 360 */     parseBodyAttributes();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void addHttpData(InterfaceHttpData data)
/*     */   {
/* 367 */     if (data == null) {
/* 368 */       return;
/*     */     }
/* 370 */     List<InterfaceHttpData> datas = (List)this.bodyMapHttpData.get(data.getName());
/* 371 */     if (datas == null) {
/* 372 */       datas = new ArrayList(1);
/* 373 */       this.bodyMapHttpData.put(data.getName(), datas);
/*     */     }
/* 375 */     datas.add(data);
/* 376 */     this.bodyListHttpData.add(data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void parseBodyAttributesStandard()
/*     */   {
/* 388 */     int firstpos = this.undecodedChunk.readerIndex();
/* 389 */     int currentpos = firstpos;
/*     */     
/*     */ 
/* 392 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED) {
/* 393 */       this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/*     */     }
/* 395 */     boolean contRead = true;
/*     */     try {
/* 397 */       while ((this.undecodedChunk.isReadable()) && (contRead)) {
/* 398 */         char read = (char)this.undecodedChunk.readUnsignedByte();
/* 399 */         currentpos++;
/* 400 */         switch (this.currentStatus) {
/*     */         case DISPOSITION: 
/* 402 */           if (read == '=') {
/* 403 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
/* 404 */             int equalpos = currentpos - 1;
/* 405 */             String key = decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset);
/*     */             
/* 407 */             this.currentAttribute = this.factory.createAttribute(this.request, key);
/* 408 */             firstpos = currentpos;
/* 409 */           } else if (read == '&') {
/* 410 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/* 411 */             int ampersandpos = currentpos - 1;
/* 412 */             String key = decodeAttribute(this.undecodedChunk
/* 413 */               .toString(firstpos, ampersandpos - firstpos, this.charset), this.charset);
/* 414 */             this.currentAttribute = this.factory.createAttribute(this.request, key);
/* 415 */             this.currentAttribute.setValue("");
/* 416 */             addHttpData(this.currentAttribute);
/* 417 */             this.currentAttribute = null;
/* 418 */             firstpos = currentpos;
/* 419 */             contRead = true; }
/* 420 */           break;
/*     */         
/*     */         case FIELD: 
/* 423 */           if (read == '&') {
/* 424 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/* 425 */             int ampersandpos = currentpos - 1;
/* 426 */             setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 427 */             firstpos = currentpos;
/* 428 */             contRead = true;
/* 429 */           } else if (read == '\r') {
/* 430 */             if (this.undecodedChunk.isReadable()) {
/* 431 */               read = (char)this.undecodedChunk.readUnsignedByte();
/* 432 */               currentpos++;
/* 433 */               if (read == '\n') {
/* 434 */                 this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
/* 435 */                 int ampersandpos = currentpos - 2;
/* 436 */                 setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 437 */                 firstpos = currentpos;
/* 438 */                 contRead = false;
/*     */               }
/*     */               else {
/* 441 */                 throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad end of line");
/*     */               }
/*     */             } else {
/* 444 */               currentpos--;
/*     */             }
/* 446 */           } else if (read == '\n') {
/* 447 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
/* 448 */             int ampersandpos = currentpos - 1;
/* 449 */             setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 450 */             firstpos = currentpos;
/* 451 */             contRead = false;
/*     */           }
/*     */           
/*     */           break;
/*     */         default: 
/* 456 */           contRead = false;
/*     */         }
/*     */       }
/* 459 */       if ((this.isLastChunk) && (this.currentAttribute != null))
/*     */       {
/* 461 */         int ampersandpos = currentpos;
/* 462 */         if (ampersandpos > firstpos) {
/* 463 */           setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 464 */         } else if (!this.currentAttribute.isCompleted()) {
/* 465 */           setFinalBuffer(Unpooled.EMPTY_BUFFER);
/*     */         }
/* 467 */         firstpos = currentpos;
/* 468 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
/* 469 */       } else if ((contRead) && (this.currentAttribute != null) && (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FIELD))
/*     */       {
/* 471 */         this.currentAttribute.addContent(this.undecodedChunk.copy(firstpos, currentpos - firstpos), false);
/*     */         
/* 473 */         firstpos = currentpos;
/*     */       }
/* 475 */       this.undecodedChunk.readerIndex(firstpos);
/*     */     }
/*     */     catch (HttpPostRequestDecoder.ErrorDataDecoderException e) {
/* 478 */       this.undecodedChunk.readerIndex(firstpos);
/* 479 */       throw e;
/*     */     }
/*     */     catch (IOException e) {
/* 482 */       this.undecodedChunk.readerIndex(firstpos);
/* 483 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
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
/*     */   private void parseBodyAttributes()
/*     */   {
/* 496 */     if (!this.undecodedChunk.hasArray()) {
/* 497 */       parseBodyAttributesStandard();
/* 498 */       return;
/*     */     }
/* 500 */     HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
/* 501 */     int firstpos = this.undecodedChunk.readerIndex();
/* 502 */     int currentpos = firstpos;
/*     */     
/*     */ 
/* 505 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED) {
/* 506 */       this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/*     */     }
/* 508 */     boolean contRead = true;
/*     */     try {
/* 510 */       while (sao.pos < sao.limit) {
/* 511 */         char read = (char)(sao.bytes[(sao.pos++)] & 0xFF);
/* 512 */         currentpos++;
/* 513 */         switch (this.currentStatus) {
/*     */         case DISPOSITION: 
/* 515 */           if (read == '=') {
/* 516 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
/* 517 */             int equalpos = currentpos - 1;
/* 518 */             String key = decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset);
/*     */             
/* 520 */             this.currentAttribute = this.factory.createAttribute(this.request, key);
/* 521 */             firstpos = currentpos;
/* 522 */           } else if (read == '&') {
/* 523 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/* 524 */             int ampersandpos = currentpos - 1;
/* 525 */             String key = decodeAttribute(this.undecodedChunk
/* 526 */               .toString(firstpos, ampersandpos - firstpos, this.charset), this.charset);
/* 527 */             this.currentAttribute = this.factory.createAttribute(this.request, key);
/* 528 */             this.currentAttribute.setValue("");
/* 529 */             addHttpData(this.currentAttribute);
/* 530 */             this.currentAttribute = null;
/* 531 */             firstpos = currentpos;
/* 532 */             contRead = true; }
/* 533 */           break;
/*     */         
/*     */         case FIELD: 
/* 536 */           if (read == '&') {
/* 537 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/* 538 */             int ampersandpos = currentpos - 1;
/* 539 */             setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 540 */             firstpos = currentpos;
/* 541 */             contRead = true;
/* 542 */           } else if (read == '\r') {
/* 543 */             if (sao.pos < sao.limit) {
/* 544 */               read = (char)(sao.bytes[(sao.pos++)] & 0xFF);
/* 545 */               currentpos++;
/* 546 */               if (read == '\n') {
/* 547 */                 this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
/* 548 */                 int ampersandpos = currentpos - 2;
/* 549 */                 sao.setReadPosition(0);
/* 550 */                 setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 551 */                 firstpos = currentpos;
/* 552 */                 contRead = false;
/*     */                 
/*     */                 break label520;
/*     */               }
/* 556 */               sao.setReadPosition(0);
/* 557 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad end of line");
/*     */             }
/*     */             
/* 560 */             if (sao.limit > 0) {
/* 561 */               currentpos--;
/*     */             }
/*     */           }
/* 564 */           else if (read == '\n') {
/* 565 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
/* 566 */             int ampersandpos = currentpos - 1;
/* 567 */             sao.setReadPosition(0);
/* 568 */             setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 569 */             firstpos = currentpos;
/* 570 */             contRead = false; }
/* 571 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         default: 
/* 576 */           sao.setReadPosition(0);
/* 577 */           contRead = false;
/*     */           break label520; }
/*     */       }
/*     */       label520:
/* 581 */       if ((this.isLastChunk) && (this.currentAttribute != null))
/*     */       {
/* 583 */         int ampersandpos = currentpos;
/* 584 */         if (ampersandpos > firstpos) {
/* 585 */           setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 586 */         } else if (!this.currentAttribute.isCompleted()) {
/* 587 */           setFinalBuffer(Unpooled.EMPTY_BUFFER);
/*     */         }
/* 589 */         firstpos = currentpos;
/* 590 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
/* 591 */       } else if ((contRead) && (this.currentAttribute != null) && (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FIELD))
/*     */       {
/* 593 */         this.currentAttribute.addContent(this.undecodedChunk.copy(firstpos, currentpos - firstpos), false);
/*     */         
/* 595 */         firstpos = currentpos;
/*     */       }
/* 597 */       this.undecodedChunk.readerIndex(firstpos);
/*     */     }
/*     */     catch (HttpPostRequestDecoder.ErrorDataDecoderException e) {
/* 600 */       this.undecodedChunk.readerIndex(firstpos);
/* 601 */       throw e;
/*     */     }
/*     */     catch (IOException e) {
/* 604 */       this.undecodedChunk.readerIndex(firstpos);
/* 605 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*     */     }
/*     */     catch (IllegalArgumentException e) {
/* 608 */       this.undecodedChunk.readerIndex(firstpos);
/* 609 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setFinalBuffer(ByteBuf buffer) throws IOException {
/* 614 */     this.currentAttribute.addContent(buffer, true);
/* 615 */     String value = decodeAttribute(this.currentAttribute.getByteBuf().toString(this.charset), this.charset);
/* 616 */     this.currentAttribute.setValue(value);
/* 617 */     addHttpData(this.currentAttribute);
/* 618 */     this.currentAttribute = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String decodeAttribute(String s, Charset charset)
/*     */   {
/*     */     try
/*     */     {
/* 628 */       return QueryStringDecoder.decodeComponent(s, charset);
/*     */     } catch (IllegalArgumentException e) {
/* 630 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad string: '" + s + '\'', e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 641 */     cleanFiles();
/*     */     
/* 643 */     this.destroyed = true;
/*     */     
/* 645 */     if ((this.undecodedChunk != null) && (this.undecodedChunk.refCnt() > 0)) {
/* 646 */       this.undecodedChunk.release();
/* 647 */       this.undecodedChunk = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void cleanFiles()
/*     */   {
/* 656 */     checkDestroyed();
/*     */     
/* 658 */     this.factory.cleanRequestHttpData(this.request);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeHttpDataFromClean(InterfaceHttpData data)
/*     */   {
/* 666 */     checkDestroyed();
/*     */     
/* 668 */     this.factory.removeHttpDataFromClean(this.request, data);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\multipart\HttpPostStandardRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */