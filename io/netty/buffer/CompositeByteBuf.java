/*      */ package io.netty.buffer;
/*      */ 
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
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
/*      */ public class CompositeByteBuf
/*      */   extends AbstractReferenceCountedByteBuf
/*      */   implements Iterable<ByteBuf>
/*      */ {
/*   46 */   private static final ByteBuffer EMPTY_NIO_BUFFER = Unpooled.EMPTY_BUFFER.nioBuffer();
/*   47 */   private static final Iterator<ByteBuf> EMPTY_ITERATOR = Collections.emptyList().iterator();
/*      */   
/*      */   private final ByteBufAllocator alloc;
/*      */   private final boolean direct;
/*      */   private final ComponentList components;
/*      */   private final int maxNumComponents;
/*      */   private boolean freed;
/*      */   
/*      */   public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents)
/*      */   {
/*   57 */     super(Integer.MAX_VALUE);
/*   58 */     if (alloc == null) {
/*   59 */       throw new NullPointerException("alloc");
/*      */     }
/*   61 */     this.alloc = alloc;
/*   62 */     this.direct = direct;
/*   63 */     this.maxNumComponents = maxNumComponents;
/*   64 */     this.components = newList(0, maxNumComponents);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, ByteBuf... buffers) {
/*   68 */     this(alloc, direct, maxNumComponents, buffers, 0, buffers.length);
/*      */   }
/*      */   
/*      */   CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, ByteBuf[] buffers, int offset, int len)
/*      */   {
/*   73 */     super(Integer.MAX_VALUE);
/*   74 */     if (alloc == null) {
/*   75 */       throw new NullPointerException("alloc");
/*      */     }
/*   77 */     if (maxNumComponents < 2) {
/*   78 */       throw new IllegalArgumentException("maxNumComponents: " + maxNumComponents + " (expected: >= 2)");
/*      */     }
/*      */     
/*      */ 
/*   82 */     this.alloc = alloc;
/*   83 */     this.direct = direct;
/*   84 */     this.maxNumComponents = maxNumComponents;
/*   85 */     this.components = newList(len, maxNumComponents);
/*      */     
/*   87 */     addComponents0(false, 0, buffers, offset, len);
/*   88 */     consolidateIfNeeded();
/*   89 */     setIndex(0, capacity());
/*      */   }
/*      */   
/*      */   public CompositeByteBuf(ByteBufAllocator alloc, boolean direct, int maxNumComponents, Iterable<ByteBuf> buffers)
/*      */   {
/*   94 */     super(Integer.MAX_VALUE);
/*   95 */     if (alloc == null) {
/*   96 */       throw new NullPointerException("alloc");
/*      */     }
/*   98 */     if (maxNumComponents < 2) {
/*   99 */       throw new IllegalArgumentException("maxNumComponents: " + maxNumComponents + " (expected: >= 2)");
/*      */     }
/*      */     
/*      */ 
/*  103 */     int len = (buffers instanceof Collection) ? ((Collection)buffers).size() : 0;
/*      */     
/*  105 */     this.alloc = alloc;
/*  106 */     this.direct = direct;
/*  107 */     this.maxNumComponents = maxNumComponents;
/*  108 */     this.components = newList(len, maxNumComponents);
/*      */     
/*  110 */     addComponents0(false, 0, buffers);
/*  111 */     consolidateIfNeeded();
/*  112 */     setIndex(0, capacity());
/*      */   }
/*      */   
/*      */   private static ComponentList newList(int initComponents, int maxNumComponents) {
/*  116 */     int capacityGuess = Math.min(16, maxNumComponents);
/*  117 */     return new ComponentList(Math.max(initComponents, capacityGuess));
/*      */   }
/*      */   
/*      */   CompositeByteBuf(ByteBufAllocator alloc)
/*      */   {
/*  122 */     super(Integer.MAX_VALUE);
/*  123 */     this.alloc = alloc;
/*  124 */     this.direct = false;
/*  125 */     this.maxNumComponents = 0;
/*  126 */     this.components = null;
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
/*      */   public CompositeByteBuf addComponent(ByteBuf buffer)
/*      */   {
/*  140 */     return addComponent(false, buffer);
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
/*      */   public CompositeByteBuf addComponents(ByteBuf... buffers)
/*      */   {
/*  155 */     return addComponents(false, buffers);
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
/*      */   public CompositeByteBuf addComponents(Iterable<ByteBuf> buffers)
/*      */   {
/*  170 */     return addComponents(false, buffers);
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
/*      */   public CompositeByteBuf addComponent(int cIndex, ByteBuf buffer)
/*      */   {
/*  185 */     return addComponent(false, cIndex, buffer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf addComponent(boolean increaseWriterIndex, ByteBuf buffer)
/*      */   {
/*  197 */     ObjectUtil.checkNotNull(buffer, "buffer");
/*  198 */     addComponent0(increaseWriterIndex, this.components.size(), buffer);
/*  199 */     consolidateIfNeeded();
/*  200 */     return this;
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
/*      */   public CompositeByteBuf addComponents(boolean increaseWriterIndex, ByteBuf... buffers)
/*      */   {
/*  213 */     addComponents0(increaseWriterIndex, this.components.size(), buffers, 0, buffers.length);
/*  214 */     consolidateIfNeeded();
/*  215 */     return this;
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
/*      */   public CompositeByteBuf addComponents(boolean increaseWriterIndex, Iterable<ByteBuf> buffers)
/*      */   {
/*  228 */     addComponents0(increaseWriterIndex, this.components.size(), buffers);
/*  229 */     consolidateIfNeeded();
/*  230 */     return this;
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
/*      */   public CompositeByteBuf addComponent(boolean increaseWriterIndex, int cIndex, ByteBuf buffer)
/*      */   {
/*  243 */     ObjectUtil.checkNotNull(buffer, "buffer");
/*  244 */     addComponent0(increaseWriterIndex, cIndex, buffer);
/*  245 */     consolidateIfNeeded();
/*  246 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int addComponent0(boolean increaseWriterIndex, int cIndex, ByteBuf buffer)
/*      */   {
/*  253 */     assert (buffer != null);
/*  254 */     boolean wasAdded = false;
/*      */     try {
/*  256 */       checkComponentIndex(cIndex);
/*      */       
/*  258 */       int readableBytes = buffer.readableBytes();
/*      */       
/*      */ 
/*      */ 
/*  262 */       Component c = new Component(buffer.order(ByteOrder.BIG_ENDIAN).slice());
/*  263 */       Component prev; if (cIndex == this.components.size()) {
/*  264 */         wasAdded = this.components.add(c);
/*  265 */         if (cIndex == 0) {
/*  266 */           c.endOffset = readableBytes;
/*      */         } else {
/*  268 */           prev = (Component)this.components.get(cIndex - 1);
/*  269 */           c.offset = prev.endOffset;
/*  270 */           c.endOffset = (c.offset + readableBytes);
/*      */         }
/*      */       } else {
/*  273 */         this.components.add(cIndex, c);
/*  274 */         wasAdded = true;
/*  275 */         if (readableBytes != 0) {
/*  276 */           updateComponentOffsets(cIndex);
/*      */         }
/*      */       }
/*  279 */       if (increaseWriterIndex) {
/*  280 */         writerIndex(writerIndex() + buffer.readableBytes());
/*      */       }
/*  282 */       return cIndex;
/*      */     } finally {
/*  284 */       if (!wasAdded) {
/*  285 */         buffer.release();
/*      */       }
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
/*      */   public CompositeByteBuf addComponents(int cIndex, ByteBuf... buffers)
/*      */   {
/*  305 */     addComponents0(false, cIndex, buffers, 0, buffers.length);
/*  306 */     consolidateIfNeeded();
/*  307 */     return this;
/*      */   }
/*      */   
/*      */   private int addComponents0(boolean increaseWriterIndex, int cIndex, ByteBuf[] buffers, int offset, int len) {
/*  311 */     ObjectUtil.checkNotNull(buffers, "buffers");
/*  312 */     int i = offset;
/*      */     try {
/*  314 */       checkComponentIndex(cIndex);
/*      */       
/*      */       ByteBuf b;
/*  317 */       while (i < len)
/*      */       {
/*      */ 
/*  320 */         b = buffers[(i++)];
/*  321 */         if (b == null) {
/*      */           break;
/*      */         }
/*  324 */         cIndex = addComponent0(increaseWriterIndex, cIndex, b) + 1;
/*  325 */         int size = this.components.size();
/*  326 */         if (cIndex > size)
/*  327 */           cIndex = size;
/*      */       }
/*      */       ByteBuf b;
/*  330 */       return cIndex;
/*      */     } finally {
/*  332 */       for (; i < len; i++) {
/*  333 */         ByteBuf b = buffers[i];
/*  334 */         if (b != null) {
/*      */           try {
/*  336 */             b.release();
/*      */           }
/*      */           catch (Throwable localThrowable1) {}
/*      */         }
/*      */       }
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
/*      */   public CompositeByteBuf addComponents(int cIndex, Iterable<ByteBuf> buffers)
/*      */   {
/*  359 */     addComponents0(false, cIndex, buffers);
/*  360 */     consolidateIfNeeded();
/*  361 */     return this;
/*      */   }
/*      */   
/*      */   private int addComponents0(boolean increaseIndex, int cIndex, Iterable<ByteBuf> buffers) {
/*  365 */     if ((buffers instanceof ByteBuf))
/*      */     {
/*  367 */       return addComponent0(increaseIndex, cIndex, (ByteBuf)buffers);
/*      */     }
/*  369 */     ObjectUtil.checkNotNull(buffers, "buffers");
/*      */     
/*  371 */     if (!(buffers instanceof Collection)) {
/*  372 */       List<ByteBuf> list = new ArrayList();
/*      */       try {
/*  374 */         for (ByteBuf b : buffers) {
/*  375 */           list.add(b);
/*      */         }
/*  377 */         buffers = list;
/*      */       } finally { ByteBuf b;
/*  379 */         if (buffers != list) {
/*  380 */           for (ByteBuf b : buffers) {
/*  381 */             if (b != null) {
/*      */               try {
/*  383 */                 b.release();
/*      */               }
/*      */               catch (Throwable localThrowable1) {}
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  393 */     Collection<ByteBuf> col = (Collection)buffers;
/*  394 */     return addComponents0(increaseIndex, cIndex, (ByteBuf[])col.toArray(new ByteBuf[0]), 0, col.size());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void consolidateIfNeeded()
/*      */   {
/*  404 */     int numComponents = this.components.size();
/*  405 */     if (numComponents > this.maxNumComponents) {
/*  406 */       int capacity = ((Component)this.components.get(numComponents - 1)).endOffset;
/*      */       
/*  408 */       ByteBuf consolidated = allocBuffer(capacity);
/*      */       
/*      */ 
/*  411 */       for (int i = 0; i < numComponents; i++) {
/*  412 */         Component c = (Component)this.components.get(i);
/*  413 */         ByteBuf b = c.buf;
/*  414 */         consolidated.writeBytes(b);
/*  415 */         c.freeIfNecessary();
/*      */       }
/*  417 */       Component c = new Component(consolidated);
/*  418 */       c.endOffset = c.length;
/*  419 */       this.components.clear();
/*  420 */       this.components.add(c);
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkComponentIndex(int cIndex) {
/*  425 */     ensureAccessible();
/*  426 */     if ((cIndex < 0) || (cIndex > this.components.size())) {
/*  427 */       throw new IndexOutOfBoundsException(String.format("cIndex: %d (expected: >= 0 && <= numComponents(%d))", new Object[] {
/*      */       
/*  429 */         Integer.valueOf(cIndex), Integer.valueOf(this.components.size()) }));
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkComponentIndex(int cIndex, int numComponents) {
/*  434 */     ensureAccessible();
/*  435 */     if ((cIndex < 0) || (cIndex + numComponents > this.components.size())) {
/*  436 */       throw new IndexOutOfBoundsException(String.format("cIndex: %d, numComponents: %d (expected: cIndex >= 0 && cIndex + numComponents <= totalNumComponents(%d))", new Object[] {
/*      */       
/*      */ 
/*  439 */         Integer.valueOf(cIndex), Integer.valueOf(numComponents), Integer.valueOf(this.components.size()) }));
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateComponentOffsets(int cIndex) {
/*  444 */     int size = this.components.size();
/*  445 */     if (size <= cIndex) {
/*  446 */       return;
/*      */     }
/*      */     
/*  449 */     Component c = (Component)this.components.get(cIndex);
/*  450 */     if (cIndex == 0) {
/*  451 */       c.offset = 0;
/*  452 */       c.endOffset = c.length;
/*  453 */       cIndex++;
/*      */     }
/*      */     
/*  456 */     for (int i = cIndex; i < size; i++) {
/*  457 */       Component prev = (Component)this.components.get(i - 1);
/*  458 */       Component cur = (Component)this.components.get(i);
/*  459 */       cur.offset = prev.endOffset;
/*  460 */       cur.endOffset = (cur.offset + cur.length);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf removeComponent(int cIndex)
/*      */   {
/*  470 */     checkComponentIndex(cIndex);
/*  471 */     Component comp = (Component)this.components.remove(cIndex);
/*  472 */     comp.freeIfNecessary();
/*  473 */     if (comp.length > 0)
/*      */     {
/*  475 */       updateComponentOffsets(cIndex);
/*      */     }
/*  477 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf removeComponents(int cIndex, int numComponents)
/*      */   {
/*  487 */     checkComponentIndex(cIndex, numComponents);
/*      */     
/*  489 */     if (numComponents == 0) {
/*  490 */       return this;
/*      */     }
/*  492 */     int endIndex = cIndex + numComponents;
/*  493 */     boolean needsUpdate = false;
/*  494 */     for (int i = cIndex; i < endIndex; i++) {
/*  495 */       Component c = (Component)this.components.get(i);
/*  496 */       if (c.length > 0) {
/*  497 */         needsUpdate = true;
/*      */       }
/*  499 */       c.freeIfNecessary();
/*      */     }
/*  501 */     this.components.removeRange(cIndex, endIndex);
/*      */     
/*  503 */     if (needsUpdate)
/*      */     {
/*  505 */       updateComponentOffsets(cIndex);
/*      */     }
/*  507 */     return this;
/*      */   }
/*      */   
/*      */   public Iterator<ByteBuf> iterator()
/*      */   {
/*  512 */     ensureAccessible();
/*  513 */     if (this.components.isEmpty()) {
/*  514 */       return EMPTY_ITERATOR;
/*      */     }
/*  516 */     return new CompositeByteBufIterator(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<ByteBuf> decompose(int offset, int length)
/*      */   {
/*  523 */     checkIndex(offset, length);
/*  524 */     if (length == 0) {
/*  525 */       return Collections.emptyList();
/*      */     }
/*      */     
/*  528 */     int componentId = toComponentIndex(offset);
/*  529 */     List<ByteBuf> slice = new ArrayList(this.components.size());
/*      */     
/*      */ 
/*  532 */     Component firstC = (Component)this.components.get(componentId);
/*  533 */     ByteBuf first = firstC.buf.duplicate();
/*  534 */     first.readerIndex(offset - firstC.offset);
/*      */     
/*  536 */     ByteBuf buf = first;
/*  537 */     int bytesToSlice = length;
/*      */     do {
/*  539 */       int readableBytes = buf.readableBytes();
/*  540 */       if (bytesToSlice <= readableBytes)
/*      */       {
/*  542 */         buf.writerIndex(buf.readerIndex() + bytesToSlice);
/*  543 */         slice.add(buf);
/*  544 */         break;
/*      */       }
/*      */       
/*  547 */       slice.add(buf);
/*  548 */       bytesToSlice -= readableBytes;
/*  549 */       componentId++;
/*      */       
/*      */ 
/*  552 */       buf = ((Component)this.components.get(componentId)).buf.duplicate();
/*      */     }
/*  554 */     while (bytesToSlice > 0);
/*      */     
/*      */ 
/*  557 */     for (int i = 0; i < slice.size(); i++) {
/*  558 */       slice.set(i, ((ByteBuf)slice.get(i)).slice());
/*      */     }
/*      */     
/*  561 */     return slice;
/*      */   }
/*      */   
/*      */   public boolean isDirect()
/*      */   {
/*  566 */     int size = this.components.size();
/*  567 */     if (size == 0) {
/*  568 */       return false;
/*      */     }
/*  570 */     for (int i = 0; i < size; i++) {
/*  571 */       if (!((Component)this.components.get(i)).buf.isDirect()) {
/*  572 */         return false;
/*      */       }
/*      */     }
/*  575 */     return true;
/*      */   }
/*      */   
/*      */   public boolean hasArray()
/*      */   {
/*  580 */     switch (this.components.size()) {
/*      */     case 0: 
/*  582 */       return true;
/*      */     case 1: 
/*  584 */       return ((Component)this.components.get(0)).buf.hasArray();
/*      */     }
/*  586 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public byte[] array()
/*      */   {
/*  592 */     switch (this.components.size()) {
/*      */     case 0: 
/*  594 */       return EmptyArrays.EMPTY_BYTES;
/*      */     case 1: 
/*  596 */       return ((Component)this.components.get(0)).buf.array();
/*      */     }
/*  598 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */   public int arrayOffset()
/*      */   {
/*  604 */     switch (this.components.size()) {
/*      */     case 0: 
/*  606 */       return 0;
/*      */     case 1: 
/*  608 */       return ((Component)this.components.get(0)).buf.arrayOffset();
/*      */     }
/*  610 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean hasMemoryAddress()
/*      */   {
/*  616 */     switch (this.components.size()) {
/*      */     case 0: 
/*  618 */       return Unpooled.EMPTY_BUFFER.hasMemoryAddress();
/*      */     case 1: 
/*  620 */       return ((Component)this.components.get(0)).buf.hasMemoryAddress();
/*      */     }
/*  622 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public long memoryAddress()
/*      */   {
/*  628 */     switch (this.components.size()) {
/*      */     case 0: 
/*  630 */       return Unpooled.EMPTY_BUFFER.memoryAddress();
/*      */     case 1: 
/*  632 */       return ((Component)this.components.get(0)).buf.memoryAddress();
/*      */     }
/*  634 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */   public int capacity()
/*      */   {
/*  640 */     int numComponents = this.components.size();
/*  641 */     if (numComponents == 0) {
/*  642 */       return 0;
/*      */     }
/*  644 */     return ((Component)this.components.get(numComponents - 1)).endOffset;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf capacity(int newCapacity)
/*      */   {
/*  649 */     checkNewCapacity(newCapacity);
/*      */     
/*  651 */     int oldCapacity = capacity();
/*  652 */     if (newCapacity > oldCapacity) {
/*  653 */       int paddingLength = newCapacity - oldCapacity;
/*      */       
/*  655 */       int nComponents = this.components.size();
/*  656 */       if (nComponents < this.maxNumComponents) {
/*  657 */         ByteBuf padding = allocBuffer(paddingLength);
/*  658 */         padding.setIndex(0, paddingLength);
/*  659 */         addComponent0(false, this.components.size(), padding);
/*      */       } else {
/*  661 */         ByteBuf padding = allocBuffer(paddingLength);
/*  662 */         padding.setIndex(0, paddingLength);
/*      */         
/*      */ 
/*  665 */         addComponent0(false, this.components.size(), padding);
/*  666 */         consolidateIfNeeded();
/*      */       }
/*  668 */     } else if (newCapacity < oldCapacity) {
/*  669 */       int bytesToTrim = oldCapacity - newCapacity;
/*  670 */       for (ListIterator<Component> i = this.components.listIterator(this.components.size()); i.hasPrevious();) {
/*  671 */         Component c = (Component)i.previous();
/*  672 */         if (bytesToTrim >= c.length) {
/*  673 */           bytesToTrim -= c.length;
/*  674 */           i.remove();
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  679 */           Component newC = new Component(c.buf.slice(0, c.length - bytesToTrim));
/*  680 */           newC.offset = c.offset;
/*  681 */           newC.endOffset = (newC.offset + newC.length);
/*  682 */           i.set(newC);
/*      */         }
/*      */       }
/*      */       
/*  686 */       if (readerIndex() > newCapacity) {
/*  687 */         setIndex(newCapacity, newCapacity);
/*  688 */       } else if (writerIndex() > newCapacity) {
/*  689 */         writerIndex(newCapacity);
/*      */       }
/*      */     }
/*  692 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBufAllocator alloc()
/*      */   {
/*  697 */     return this.alloc;
/*      */   }
/*      */   
/*      */   public ByteOrder order()
/*      */   {
/*  702 */     return ByteOrder.BIG_ENDIAN;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int numComponents()
/*      */   {
/*  709 */     return this.components.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int maxNumComponents()
/*      */   {
/*  716 */     return this.maxNumComponents;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int toComponentIndex(int offset)
/*      */   {
/*  723 */     checkIndex(offset);
/*      */     
/*  725 */     int low = 0; for (int high = this.components.size(); low <= high;) {
/*  726 */       int mid = low + high >>> 1;
/*  727 */       Component c = (Component)this.components.get(mid);
/*  728 */       if (offset >= c.endOffset) {
/*  729 */         low = mid + 1;
/*  730 */       } else if (offset < c.offset) {
/*  731 */         high = mid - 1;
/*      */       } else {
/*  733 */         return mid;
/*      */       }
/*      */     }
/*      */     
/*  737 */     throw new Error("should not reach here");
/*      */   }
/*      */   
/*      */   public int toByteIndex(int cIndex) {
/*  741 */     checkComponentIndex(cIndex);
/*  742 */     return ((Component)this.components.get(cIndex)).offset;
/*      */   }
/*      */   
/*      */   public byte getByte(int index)
/*      */   {
/*  747 */     return _getByte(index);
/*      */   }
/*      */   
/*      */   protected byte _getByte(int index)
/*      */   {
/*  752 */     Component c = findComponent(index);
/*  753 */     return c.buf.getByte(index - c.offset);
/*      */   }
/*      */   
/*      */   protected short _getShort(int index)
/*      */   {
/*  758 */     Component c = findComponent(index);
/*  759 */     if (index + 2 <= c.endOffset)
/*  760 */       return c.buf.getShort(index - c.offset);
/*  761 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  762 */       return (short)((_getByte(index) & 0xFF) << 8 | _getByte(index + 1) & 0xFF);
/*      */     }
/*  764 */     return (short)(_getByte(index) & 0xFF | (_getByte(index + 1) & 0xFF) << 8);
/*      */   }
/*      */   
/*      */ 
/*      */   protected short _getShortLE(int index)
/*      */   {
/*  770 */     Component c = findComponent(index);
/*  771 */     if (index + 2 <= c.endOffset)
/*  772 */       return c.buf.getShortLE(index - c.offset);
/*  773 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  774 */       return (short)(_getByte(index) & 0xFF | (_getByte(index + 1) & 0xFF) << 8);
/*      */     }
/*  776 */     return (short)((_getByte(index) & 0xFF) << 8 | _getByte(index + 1) & 0xFF);
/*      */   }
/*      */   
/*      */ 
/*      */   protected int _getUnsignedMedium(int index)
/*      */   {
/*  782 */     Component c = findComponent(index);
/*  783 */     if (index + 3 <= c.endOffset)
/*  784 */       return c.buf.getUnsignedMedium(index - c.offset);
/*  785 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  786 */       return (_getShort(index) & 0xFFFF) << 8 | _getByte(index + 2) & 0xFF;
/*      */     }
/*  788 */     return _getShort(index) & 0xFFFF | (_getByte(index + 2) & 0xFF) << 16;
/*      */   }
/*      */   
/*      */ 
/*      */   protected int _getUnsignedMediumLE(int index)
/*      */   {
/*  794 */     Component c = findComponent(index);
/*  795 */     if (index + 3 <= c.endOffset)
/*  796 */       return c.buf.getUnsignedMediumLE(index - c.offset);
/*  797 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  798 */       return _getShortLE(index) & 0xFFFF | (_getByte(index + 2) & 0xFF) << 16;
/*      */     }
/*  800 */     return (_getShortLE(index) & 0xFFFF) << 8 | _getByte(index + 2) & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */   protected int _getInt(int index)
/*      */   {
/*  806 */     Component c = findComponent(index);
/*  807 */     if (index + 4 <= c.endOffset)
/*  808 */       return c.buf.getInt(index - c.offset);
/*  809 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  810 */       return (_getShort(index) & 0xFFFF) << 16 | _getShort(index + 2) & 0xFFFF;
/*      */     }
/*  812 */     return _getShort(index) & 0xFFFF | (_getShort(index + 2) & 0xFFFF) << 16;
/*      */   }
/*      */   
/*      */ 
/*      */   protected int _getIntLE(int index)
/*      */   {
/*  818 */     Component c = findComponent(index);
/*  819 */     if (index + 4 <= c.endOffset)
/*  820 */       return c.buf.getIntLE(index - c.offset);
/*  821 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  822 */       return _getShortLE(index) & 0xFFFF | (_getShortLE(index + 2) & 0xFFFF) << 16;
/*      */     }
/*  824 */     return (_getShortLE(index) & 0xFFFF) << 16 | _getShortLE(index + 2) & 0xFFFF;
/*      */   }
/*      */   
/*      */ 
/*      */   protected long _getLong(int index)
/*      */   {
/*  830 */     Component c = findComponent(index);
/*  831 */     if (index + 8 <= c.endOffset)
/*  832 */       return c.buf.getLong(index - c.offset);
/*  833 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  834 */       return (_getInt(index) & 0xFFFFFFFF) << 32 | _getInt(index + 4) & 0xFFFFFFFF;
/*      */     }
/*  836 */     return _getInt(index) & 0xFFFFFFFF | (_getInt(index + 4) & 0xFFFFFFFF) << 32;
/*      */   }
/*      */   
/*      */ 
/*      */   protected long _getLongLE(int index)
/*      */   {
/*  842 */     Component c = findComponent(index);
/*  843 */     if (index + 8 <= c.endOffset)
/*  844 */       return c.buf.getLongLE(index - c.offset);
/*  845 */     if (order() == ByteOrder.BIG_ENDIAN) {
/*  846 */       return _getIntLE(index) & 0xFFFFFFFF | (_getIntLE(index + 4) & 0xFFFFFFFF) << 32;
/*      */     }
/*  848 */     return (_getIntLE(index) & 0xFFFFFFFF) << 32 | _getIntLE(index + 4) & 0xFFFFFFFF;
/*      */   }
/*      */   
/*      */ 
/*      */   public CompositeByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
/*      */   {
/*  854 */     checkDstIndex(index, length, dstIndex, dst.length);
/*  855 */     if (length == 0) {
/*  856 */       return this;
/*      */     }
/*      */     
/*  859 */     int i = toComponentIndex(index);
/*  860 */     while (length > 0) {
/*  861 */       Component c = (Component)this.components.get(i);
/*  862 */       ByteBuf s = c.buf;
/*  863 */       int adjustment = c.offset;
/*  864 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  865 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/*  866 */       index += localLength;
/*  867 */       dstIndex += localLength;
/*  868 */       length -= localLength;
/*  869 */       i++;
/*      */     }
/*  871 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuffer dst)
/*      */   {
/*  876 */     int limit = dst.limit();
/*  877 */     int length = dst.remaining();
/*      */     
/*  879 */     checkIndex(index, length);
/*  880 */     if (length == 0) {
/*  881 */       return this;
/*      */     }
/*      */     
/*  884 */     int i = toComponentIndex(index);
/*      */     try {
/*  886 */       while (length > 0) {
/*  887 */         Component c = (Component)this.components.get(i);
/*  888 */         ByteBuf s = c.buf;
/*  889 */         int adjustment = c.offset;
/*  890 */         int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  891 */         dst.limit(dst.position() + localLength);
/*  892 */         s.getBytes(index - adjustment, dst);
/*  893 */         index += localLength;
/*  894 */         length -= localLength;
/*  895 */         i++;
/*      */       }
/*      */     } finally {
/*  898 */       dst.limit(limit);
/*      */     }
/*  900 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
/*      */   {
/*  905 */     checkDstIndex(index, length, dstIndex, dst.capacity());
/*  906 */     if (length == 0) {
/*  907 */       return this;
/*      */     }
/*      */     
/*  910 */     int i = toComponentIndex(index);
/*  911 */     while (length > 0) {
/*  912 */       Component c = (Component)this.components.get(i);
/*  913 */       ByteBuf s = c.buf;
/*  914 */       int adjustment = c.offset;
/*  915 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  916 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/*  917 */       index += localLength;
/*  918 */       dstIndex += localLength;
/*  919 */       length -= localLength;
/*  920 */       i++;
/*      */     }
/*  922 */     return this;
/*      */   }
/*      */   
/*      */   public int getBytes(int index, GatheringByteChannel out, int length)
/*      */     throws IOException
/*      */   {
/*  928 */     int count = nioBufferCount();
/*  929 */     if (count == 1) {
/*  930 */       return out.write(internalNioBuffer(index, length));
/*      */     }
/*  932 */     long writtenBytes = out.write(nioBuffers(index, length));
/*  933 */     if (writtenBytes > 2147483647L) {
/*  934 */       return Integer.MAX_VALUE;
/*      */     }
/*  936 */     return (int)writtenBytes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getBytes(int index, FileChannel out, long position, int length)
/*      */     throws IOException
/*      */   {
/*  944 */     int count = nioBufferCount();
/*  945 */     if (count == 1) {
/*  946 */       return out.write(internalNioBuffer(index, length), position);
/*      */     }
/*  948 */     long writtenBytes = 0L;
/*  949 */     for (ByteBuffer buf : nioBuffers(index, length)) {
/*  950 */       writtenBytes += out.write(buf, position + writtenBytes);
/*      */     }
/*  952 */     if (writtenBytes > 2147483647L) {
/*  953 */       return Integer.MAX_VALUE;
/*      */     }
/*  955 */     return (int)writtenBytes;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, OutputStream out, int length)
/*      */     throws IOException
/*      */   {
/*  961 */     checkIndex(index, length);
/*  962 */     if (length == 0) {
/*  963 */       return this;
/*      */     }
/*      */     
/*  966 */     int i = toComponentIndex(index);
/*  967 */     while (length > 0) {
/*  968 */       Component c = (Component)this.components.get(i);
/*  969 */       ByteBuf s = c.buf;
/*  970 */       int adjustment = c.offset;
/*  971 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/*  972 */       s.getBytes(index - adjustment, out, localLength);
/*  973 */       index += localLength;
/*  974 */       length -= localLength;
/*  975 */       i++;
/*      */     }
/*  977 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setByte(int index, int value)
/*      */   {
/*  982 */     Component c = findComponent(index);
/*  983 */     c.buf.setByte(index - c.offset, value);
/*  984 */     return this;
/*      */   }
/*      */   
/*      */   protected void _setByte(int index, int value)
/*      */   {
/*  989 */     setByte(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setShort(int index, int value)
/*      */   {
/*  994 */     return (CompositeByteBuf)super.setShort(index, value);
/*      */   }
/*      */   
/*      */   protected void _setShort(int index, int value)
/*      */   {
/*  999 */     Component c = findComponent(index);
/* 1000 */     if (index + 2 <= c.endOffset) {
/* 1001 */       c.buf.setShort(index - c.offset, value);
/* 1002 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1003 */       _setByte(index, (byte)(value >>> 8));
/* 1004 */       _setByte(index + 1, (byte)value);
/*      */     } else {
/* 1006 */       _setByte(index, (byte)value);
/* 1007 */       _setByte(index + 1, (byte)(value >>> 8));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _setShortLE(int index, int value)
/*      */   {
/* 1013 */     Component c = findComponent(index);
/* 1014 */     if (index + 2 <= c.endOffset) {
/* 1015 */       c.buf.setShortLE(index - c.offset, value);
/* 1016 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1017 */       _setByte(index, (byte)value);
/* 1018 */       _setByte(index + 1, (byte)(value >>> 8));
/*      */     } else {
/* 1020 */       _setByte(index, (byte)(value >>> 8));
/* 1021 */       _setByte(index + 1, (byte)value);
/*      */     }
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setMedium(int index, int value)
/*      */   {
/* 1027 */     return (CompositeByteBuf)super.setMedium(index, value);
/*      */   }
/*      */   
/*      */   protected void _setMedium(int index, int value)
/*      */   {
/* 1032 */     Component c = findComponent(index);
/* 1033 */     if (index + 3 <= c.endOffset) {
/* 1034 */       c.buf.setMedium(index - c.offset, value);
/* 1035 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1036 */       _setShort(index, (short)(value >> 8));
/* 1037 */       _setByte(index + 2, (byte)value);
/*      */     } else {
/* 1039 */       _setShort(index, (short)value);
/* 1040 */       _setByte(index + 2, (byte)(value >>> 16));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _setMediumLE(int index, int value)
/*      */   {
/* 1046 */     Component c = findComponent(index);
/* 1047 */     if (index + 3 <= c.endOffset) {
/* 1048 */       c.buf.setMediumLE(index - c.offset, value);
/* 1049 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1050 */       _setShortLE(index, (short)value);
/* 1051 */       _setByte(index + 2, (byte)(value >>> 16));
/*      */     } else {
/* 1053 */       _setShortLE(index, (short)(value >> 8));
/* 1054 */       _setByte(index + 2, (byte)value);
/*      */     }
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setInt(int index, int value)
/*      */   {
/* 1060 */     return (CompositeByteBuf)super.setInt(index, value);
/*      */   }
/*      */   
/*      */   protected void _setInt(int index, int value)
/*      */   {
/* 1065 */     Component c = findComponent(index);
/* 1066 */     if (index + 4 <= c.endOffset) {
/* 1067 */       c.buf.setInt(index - c.offset, value);
/* 1068 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1069 */       _setShort(index, (short)(value >>> 16));
/* 1070 */       _setShort(index + 2, (short)value);
/*      */     } else {
/* 1072 */       _setShort(index, (short)value);
/* 1073 */       _setShort(index + 2, (short)(value >>> 16));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _setIntLE(int index, int value)
/*      */   {
/* 1079 */     Component c = findComponent(index);
/* 1080 */     if (index + 4 <= c.endOffset) {
/* 1081 */       c.buf.setIntLE(index - c.offset, value);
/* 1082 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1083 */       _setShortLE(index, (short)value);
/* 1084 */       _setShortLE(index + 2, (short)(value >>> 16));
/*      */     } else {
/* 1086 */       _setShortLE(index, (short)(value >>> 16));
/* 1087 */       _setShortLE(index + 2, (short)value);
/*      */     }
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setLong(int index, long value)
/*      */   {
/* 1093 */     return (CompositeByteBuf)super.setLong(index, value);
/*      */   }
/*      */   
/*      */   protected void _setLong(int index, long value)
/*      */   {
/* 1098 */     Component c = findComponent(index);
/* 1099 */     if (index + 8 <= c.endOffset) {
/* 1100 */       c.buf.setLong(index - c.offset, value);
/* 1101 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1102 */       _setInt(index, (int)(value >>> 32));
/* 1103 */       _setInt(index + 4, (int)value);
/*      */     } else {
/* 1105 */       _setInt(index, (int)value);
/* 1106 */       _setInt(index + 4, (int)(value >>> 32));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _setLongLE(int index, long value)
/*      */   {
/* 1112 */     Component c = findComponent(index);
/* 1113 */     if (index + 8 <= c.endOffset) {
/* 1114 */       c.buf.setLongLE(index - c.offset, value);
/* 1115 */     } else if (order() == ByteOrder.BIG_ENDIAN) {
/* 1116 */       _setIntLE(index, (int)value);
/* 1117 */       _setIntLE(index + 4, (int)(value >>> 32));
/*      */     } else {
/* 1119 */       _setIntLE(index, (int)(value >>> 32));
/* 1120 */       _setIntLE(index + 4, (int)value);
/*      */     }
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
/*      */   {
/* 1126 */     checkSrcIndex(index, length, srcIndex, src.length);
/* 1127 */     if (length == 0) {
/* 1128 */       return this;
/*      */     }
/*      */     
/* 1131 */     int i = toComponentIndex(index);
/* 1132 */     while (length > 0) {
/* 1133 */       Component c = (Component)this.components.get(i);
/* 1134 */       ByteBuf s = c.buf;
/* 1135 */       int adjustment = c.offset;
/* 1136 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 1137 */       s.setBytes(index - adjustment, src, srcIndex, localLength);
/* 1138 */       index += localLength;
/* 1139 */       srcIndex += localLength;
/* 1140 */       length -= localLength;
/* 1141 */       i++;
/*      */     }
/* 1143 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuffer src)
/*      */   {
/* 1148 */     int limit = src.limit();
/* 1149 */     int length = src.remaining();
/*      */     
/* 1151 */     checkIndex(index, length);
/* 1152 */     if (length == 0) {
/* 1153 */       return this;
/*      */     }
/*      */     
/* 1156 */     int i = toComponentIndex(index);
/*      */     try {
/* 1158 */       while (length > 0) {
/* 1159 */         Component c = (Component)this.components.get(i);
/* 1160 */         ByteBuf s = c.buf;
/* 1161 */         int adjustment = c.offset;
/* 1162 */         int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 1163 */         src.limit(src.position() + localLength);
/* 1164 */         s.setBytes(index - adjustment, src);
/* 1165 */         index += localLength;
/* 1166 */         length -= localLength;
/* 1167 */         i++;
/*      */       }
/*      */     } finally {
/* 1170 */       src.limit(limit);
/*      */     }
/* 1172 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
/*      */   {
/* 1177 */     checkSrcIndex(index, length, srcIndex, src.capacity());
/* 1178 */     if (length == 0) {
/* 1179 */       return this;
/*      */     }
/*      */     
/* 1182 */     int i = toComponentIndex(index);
/* 1183 */     while (length > 0) {
/* 1184 */       Component c = (Component)this.components.get(i);
/* 1185 */       ByteBuf s = c.buf;
/* 1186 */       int adjustment = c.offset;
/* 1187 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 1188 */       s.setBytes(index - adjustment, src, srcIndex, localLength);
/* 1189 */       index += localLength;
/* 1190 */       srcIndex += localLength;
/* 1191 */       length -= localLength;
/* 1192 */       i++;
/*      */     }
/* 1194 */     return this;
/*      */   }
/*      */   
/*      */   public int setBytes(int index, InputStream in, int length) throws IOException
/*      */   {
/* 1199 */     checkIndex(index, length);
/* 1200 */     if (length == 0) {
/* 1201 */       return in.read(EmptyArrays.EMPTY_BYTES);
/*      */     }
/*      */     
/* 1204 */     int i = toComponentIndex(index);
/* 1205 */     int readBytes = 0;
/*      */     do
/*      */     {
/* 1208 */       Component c = (Component)this.components.get(i);
/* 1209 */       ByteBuf s = c.buf;
/* 1210 */       int adjustment = c.offset;
/* 1211 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 1212 */       if (localLength == 0)
/*      */       {
/* 1214 */         i++;
/*      */       }
/*      */       else {
/* 1217 */         int localReadBytes = s.setBytes(index - adjustment, in, localLength);
/* 1218 */         if (localReadBytes < 0) {
/* 1219 */           if (readBytes != 0) break;
/* 1220 */           return -1;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1226 */         if (localReadBytes == localLength) {
/* 1227 */           index += localLength;
/* 1228 */           length -= localLength;
/* 1229 */           readBytes += localLength;
/* 1230 */           i++;
/*      */         } else {
/* 1232 */           index += localReadBytes;
/* 1233 */           length -= localReadBytes;
/* 1234 */           readBytes += localReadBytes;
/*      */         }
/* 1236 */       } } while (length > 0);
/*      */     
/* 1238 */     return readBytes;
/*      */   }
/*      */   
/*      */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException
/*      */   {
/* 1243 */     checkIndex(index, length);
/* 1244 */     if (length == 0) {
/* 1245 */       return in.read(EMPTY_NIO_BUFFER);
/*      */     }
/*      */     
/* 1248 */     int i = toComponentIndex(index);
/* 1249 */     int readBytes = 0;
/*      */     do {
/* 1251 */       Component c = (Component)this.components.get(i);
/* 1252 */       ByteBuf s = c.buf;
/* 1253 */       int adjustment = c.offset;
/* 1254 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 1255 */       if (localLength == 0)
/*      */       {
/* 1257 */         i++;
/*      */       }
/*      */       else {
/* 1260 */         int localReadBytes = s.setBytes(index - adjustment, in, localLength);
/*      */         
/* 1262 */         if (localReadBytes == 0) {
/*      */           break;
/*      */         }
/*      */         
/* 1266 */         if (localReadBytes < 0) {
/* 1267 */           if (readBytes != 0) break;
/* 1268 */           return -1;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1274 */         if (localReadBytes == localLength) {
/* 1275 */           index += localLength;
/* 1276 */           length -= localLength;
/* 1277 */           readBytes += localLength;
/* 1278 */           i++;
/*      */         } else {
/* 1280 */           index += localReadBytes;
/* 1281 */           length -= localReadBytes;
/* 1282 */           readBytes += localReadBytes;
/*      */         }
/* 1284 */       } } while (length > 0);
/*      */     
/* 1286 */     return readBytes;
/*      */   }
/*      */   
/*      */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException
/*      */   {
/* 1291 */     checkIndex(index, length);
/* 1292 */     if (length == 0) {
/* 1293 */       return in.read(EMPTY_NIO_BUFFER, position);
/*      */     }
/*      */     
/* 1296 */     int i = toComponentIndex(index);
/* 1297 */     int readBytes = 0;
/*      */     do {
/* 1299 */       Component c = (Component)this.components.get(i);
/* 1300 */       ByteBuf s = c.buf;
/* 1301 */       int adjustment = c.offset;
/* 1302 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 1303 */       if (localLength == 0)
/*      */       {
/* 1305 */         i++;
/*      */       }
/*      */       else {
/* 1308 */         int localReadBytes = s.setBytes(index - adjustment, in, position + readBytes, localLength);
/*      */         
/* 1310 */         if (localReadBytes == 0) {
/*      */           break;
/*      */         }
/*      */         
/* 1314 */         if (localReadBytes < 0) {
/* 1315 */           if (readBytes != 0) break;
/* 1316 */           return -1;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1322 */         if (localReadBytes == localLength) {
/* 1323 */           index += localLength;
/* 1324 */           length -= localLength;
/* 1325 */           readBytes += localLength;
/* 1326 */           i++;
/*      */         } else {
/* 1328 */           index += localReadBytes;
/* 1329 */           length -= localReadBytes;
/* 1330 */           readBytes += localReadBytes;
/*      */         }
/* 1332 */       } } while (length > 0);
/*      */     
/* 1334 */     return readBytes;
/*      */   }
/*      */   
/*      */   public ByteBuf copy(int index, int length)
/*      */   {
/* 1339 */     checkIndex(index, length);
/* 1340 */     ByteBuf dst = allocBuffer(length);
/* 1341 */     if (length != 0) {
/* 1342 */       copyTo(index, length, toComponentIndex(index), dst);
/*      */     }
/* 1344 */     return dst;
/*      */   }
/*      */   
/*      */   private void copyTo(int index, int length, int componentId, ByteBuf dst) {
/* 1348 */     int dstIndex = 0;
/* 1349 */     int i = componentId;
/*      */     
/* 1351 */     while (length > 0) {
/* 1352 */       Component c = (Component)this.components.get(i);
/* 1353 */       ByteBuf s = c.buf;
/* 1354 */       int adjustment = c.offset;
/* 1355 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 1356 */       s.getBytes(index - adjustment, dst, dstIndex, localLength);
/* 1357 */       index += localLength;
/* 1358 */       dstIndex += localLength;
/* 1359 */       length -= localLength;
/* 1360 */       i++;
/*      */     }
/*      */     
/* 1363 */     dst.writerIndex(dst.capacity());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuf component(int cIndex)
/*      */   {
/* 1373 */     return internalComponent(cIndex).duplicate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuf componentAtOffset(int offset)
/*      */   {
/* 1383 */     return internalComponentAtOffset(offset).duplicate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuf internalComponent(int cIndex)
/*      */   {
/* 1393 */     checkComponentIndex(cIndex);
/* 1394 */     return ((Component)this.components.get(cIndex)).buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuf internalComponentAtOffset(int offset)
/*      */   {
/* 1404 */     return findComponent(offset).buf;
/*      */   }
/*      */   
/*      */   private Component findComponent(int offset) {
/* 1408 */     checkIndex(offset);
/*      */     
/* 1410 */     int low = 0; for (int high = this.components.size(); low <= high;) {
/* 1411 */       int mid = low + high >>> 1;
/* 1412 */       Component c = (Component)this.components.get(mid);
/* 1413 */       if (offset >= c.endOffset) {
/* 1414 */         low = mid + 1;
/* 1415 */       } else if (offset < c.offset) {
/* 1416 */         high = mid - 1;
/*      */       } else {
/* 1418 */         assert (c.length != 0);
/* 1419 */         return c;
/*      */       }
/*      */     }
/*      */     
/* 1423 */     throw new Error("should not reach here");
/*      */   }
/*      */   
/*      */   public int nioBufferCount()
/*      */   {
/* 1428 */     switch (this.components.size()) {
/*      */     case 0: 
/* 1430 */       return 1;
/*      */     case 1: 
/* 1432 */       return ((Component)this.components.get(0)).buf.nioBufferCount();
/*      */     }
/* 1434 */     int count = 0;
/* 1435 */     int componentsCount = this.components.size();
/* 1436 */     for (int i = 0; i < componentsCount; i++) {
/* 1437 */       Component c = (Component)this.components.get(i);
/* 1438 */       count += c.buf.nioBufferCount();
/*      */     }
/* 1440 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */   public ByteBuffer internalNioBuffer(int index, int length)
/*      */   {
/* 1446 */     switch (this.components.size()) {
/*      */     case 0: 
/* 1448 */       return EMPTY_NIO_BUFFER;
/*      */     case 1: 
/* 1450 */       return ((Component)this.components.get(0)).buf.internalNioBuffer(index, length);
/*      */     }
/* 1452 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */   public ByteBuffer nioBuffer(int index, int length)
/*      */   {
/* 1458 */     checkIndex(index, length);
/*      */     
/* 1460 */     switch (this.components.size()) {
/*      */     case 0: 
/* 1462 */       return EMPTY_NIO_BUFFER;
/*      */     case 1: 
/* 1464 */       ByteBuf buf = ((Component)this.components.get(0)).buf;
/* 1465 */       if (buf.nioBufferCount() == 1) {
/* 1466 */         return ((Component)this.components.get(0)).buf.nioBuffer(index, length);
/*      */       }
/*      */       break;
/*      */     }
/* 1470 */     ByteBuffer[] buffers = nioBuffers(index, length);
/*      */     
/* 1472 */     if (buffers.length == 1) {
/* 1473 */       return buffers[0].duplicate();
/*      */     }
/*      */     
/* 1476 */     ByteBuffer merged = ByteBuffer.allocate(length).order(order());
/* 1477 */     for (ByteBuffer buf : buffers) {
/* 1478 */       merged.put(buf);
/*      */     }
/*      */     
/* 1481 */     merged.flip();
/* 1482 */     return merged;
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int index, int length)
/*      */   {
/* 1487 */     checkIndex(index, length);
/* 1488 */     if (length == 0) {
/* 1489 */       return new ByteBuffer[] { EMPTY_NIO_BUFFER };
/*      */     }
/*      */     
/* 1492 */     List<ByteBuffer> buffers = new ArrayList(this.components.size());
/* 1493 */     int i = toComponentIndex(index);
/* 1494 */     while (length > 0) {
/* 1495 */       Component c = (Component)this.components.get(i);
/* 1496 */       ByteBuf s = c.buf;
/* 1497 */       int adjustment = c.offset;
/* 1498 */       int localLength = Math.min(length, s.capacity() - (index - adjustment));
/* 1499 */       switch (s.nioBufferCount()) {
/*      */       case 0: 
/* 1501 */         throw new UnsupportedOperationException();
/*      */       case 1: 
/* 1503 */         buffers.add(s.nioBuffer(index - adjustment, localLength));
/* 1504 */         break;
/*      */       default: 
/* 1506 */         Collections.addAll(buffers, s.nioBuffers(index - adjustment, localLength));
/*      */       }
/*      */       
/* 1509 */       index += localLength;
/* 1510 */       length -= localLength;
/* 1511 */       i++;
/*      */     }
/*      */     
/* 1514 */     return (ByteBuffer[])buffers.toArray(new ByteBuffer[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf consolidate()
/*      */   {
/* 1521 */     ensureAccessible();
/* 1522 */     int numComponents = numComponents();
/* 1523 */     if (numComponents <= 1) {
/* 1524 */       return this;
/*      */     }
/*      */     
/* 1527 */     Component last = (Component)this.components.get(numComponents - 1);
/* 1528 */     int capacity = last.endOffset;
/* 1529 */     ByteBuf consolidated = allocBuffer(capacity);
/*      */     
/* 1531 */     for (int i = 0; i < numComponents; i++) {
/* 1532 */       Component c = (Component)this.components.get(i);
/* 1533 */       ByteBuf b = c.buf;
/* 1534 */       consolidated.writeBytes(b);
/* 1535 */       c.freeIfNecessary();
/*      */     }
/*      */     
/* 1538 */     this.components.clear();
/* 1539 */     this.components.add(new Component(consolidated));
/* 1540 */     updateComponentOffsets(0);
/* 1541 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf consolidate(int cIndex, int numComponents)
/*      */   {
/* 1551 */     checkComponentIndex(cIndex, numComponents);
/* 1552 */     if (numComponents <= 1) {
/* 1553 */       return this;
/*      */     }
/*      */     
/* 1556 */     int endCIndex = cIndex + numComponents;
/* 1557 */     Component last = (Component)this.components.get(endCIndex - 1);
/* 1558 */     int capacity = last.endOffset - ((Component)this.components.get(cIndex)).offset;
/* 1559 */     ByteBuf consolidated = allocBuffer(capacity);
/*      */     
/* 1561 */     for (int i = cIndex; i < endCIndex; i++) {
/* 1562 */       Component c = (Component)this.components.get(i);
/* 1563 */       ByteBuf b = c.buf;
/* 1564 */       consolidated.writeBytes(b);
/* 1565 */       c.freeIfNecessary();
/*      */     }
/*      */     
/* 1568 */     this.components.removeRange(cIndex + 1, endCIndex);
/* 1569 */     this.components.set(cIndex, new Component(consolidated));
/* 1570 */     updateComponentOffsets(cIndex);
/* 1571 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public CompositeByteBuf discardReadComponents()
/*      */   {
/* 1578 */     ensureAccessible();
/* 1579 */     int readerIndex = readerIndex();
/* 1580 */     if (readerIndex == 0) {
/* 1581 */       return this;
/*      */     }
/*      */     
/*      */ 
/* 1585 */     int writerIndex = writerIndex();
/* 1586 */     if ((readerIndex == writerIndex) && (writerIndex == capacity())) {
/* 1587 */       int size = this.components.size();
/* 1588 */       for (int i = 0; i < size; i++) {
/* 1589 */         ((Component)this.components.get(i)).freeIfNecessary();
/*      */       }
/* 1591 */       this.components.clear();
/* 1592 */       setIndex(0, 0);
/* 1593 */       adjustMarkers(readerIndex);
/* 1594 */       return this;
/*      */     }
/*      */     
/*      */ 
/* 1598 */     int firstComponentId = toComponentIndex(readerIndex);
/* 1599 */     for (int i = 0; i < firstComponentId; i++) {
/* 1600 */       ((Component)this.components.get(i)).freeIfNecessary();
/*      */     }
/* 1602 */     this.components.removeRange(0, firstComponentId);
/*      */     
/*      */ 
/* 1605 */     Component first = (Component)this.components.get(0);
/* 1606 */     int offset = first.offset;
/* 1607 */     updateComponentOffsets(0);
/* 1608 */     setIndex(readerIndex - offset, writerIndex - offset);
/* 1609 */     adjustMarkers(offset);
/* 1610 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf discardReadBytes()
/*      */   {
/* 1615 */     ensureAccessible();
/* 1616 */     int readerIndex = readerIndex();
/* 1617 */     if (readerIndex == 0) {
/* 1618 */       return this;
/*      */     }
/*      */     
/*      */ 
/* 1622 */     int writerIndex = writerIndex();
/* 1623 */     if ((readerIndex == writerIndex) && (writerIndex == capacity())) {
/* 1624 */       int size = this.components.size();
/* 1625 */       for (int i = 0; i < size; i++) {
/* 1626 */         ((Component)this.components.get(i)).freeIfNecessary();
/*      */       }
/* 1628 */       this.components.clear();
/* 1629 */       setIndex(0, 0);
/* 1630 */       adjustMarkers(readerIndex);
/* 1631 */       return this;
/*      */     }
/*      */     
/*      */ 
/* 1635 */     int firstComponentId = toComponentIndex(readerIndex);
/* 1636 */     for (int i = 0; i < firstComponentId; i++) {
/* 1637 */       ((Component)this.components.get(i)).freeIfNecessary();
/*      */     }
/*      */     
/*      */ 
/* 1641 */     Component c = (Component)this.components.get(firstComponentId);
/* 1642 */     int adjustment = readerIndex - c.offset;
/* 1643 */     if (adjustment == c.length)
/*      */     {
/* 1645 */       firstComponentId++;
/*      */     } else {
/* 1647 */       Component newC = new Component(c.buf.slice(adjustment, c.length - adjustment));
/* 1648 */       this.components.set(firstComponentId, newC);
/*      */     }
/*      */     
/* 1651 */     this.components.removeRange(0, firstComponentId);
/*      */     
/*      */ 
/* 1654 */     updateComponentOffsets(0);
/* 1655 */     setIndex(0, writerIndex - readerIndex);
/* 1656 */     adjustMarkers(readerIndex);
/* 1657 */     return this;
/*      */   }
/*      */   
/*      */   private ByteBuf allocBuffer(int capacity) {
/* 1661 */     return this.direct ? alloc().directBuffer(capacity) : alloc().heapBuffer(capacity);
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1666 */     String result = super.toString();
/* 1667 */     result = result.substring(0, result.length() - 1);
/* 1668 */     return result + ", components=" + this.components.size() + ')';
/*      */   }
/*      */   
/*      */   private static final class Component {
/*      */     final ByteBuf buf;
/*      */     final int length;
/*      */     int offset;
/*      */     int endOffset;
/*      */     
/*      */     Component(ByteBuf buf) {
/* 1678 */       this.buf = buf;
/* 1679 */       this.length = buf.readableBytes();
/*      */     }
/*      */     
/*      */     void freeIfNecessary() {
/* 1683 */       this.buf.release();
/*      */     }
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readerIndex(int readerIndex)
/*      */   {
/* 1689 */     return (CompositeByteBuf)super.readerIndex(readerIndex);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writerIndex(int writerIndex)
/*      */   {
/* 1694 */     return (CompositeByteBuf)super.writerIndex(writerIndex);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setIndex(int readerIndex, int writerIndex)
/*      */   {
/* 1699 */     return (CompositeByteBuf)super.setIndex(readerIndex, writerIndex);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf clear()
/*      */   {
/* 1704 */     return (CompositeByteBuf)super.clear();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf markReaderIndex()
/*      */   {
/* 1709 */     return (CompositeByteBuf)super.markReaderIndex();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf resetReaderIndex()
/*      */   {
/* 1714 */     return (CompositeByteBuf)super.resetReaderIndex();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf markWriterIndex()
/*      */   {
/* 1719 */     return (CompositeByteBuf)super.markWriterIndex();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf resetWriterIndex()
/*      */   {
/* 1724 */     return (CompositeByteBuf)super.resetWriterIndex();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf ensureWritable(int minWritableBytes)
/*      */   {
/* 1729 */     return (CompositeByteBuf)super.ensureWritable(minWritableBytes);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst)
/*      */   {
/* 1734 */     return (CompositeByteBuf)super.getBytes(index, dst);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, ByteBuf dst, int length)
/*      */   {
/* 1739 */     return (CompositeByteBuf)super.getBytes(index, dst, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf getBytes(int index, byte[] dst)
/*      */   {
/* 1744 */     return (CompositeByteBuf)super.getBytes(index, dst);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBoolean(int index, boolean value)
/*      */   {
/* 1749 */     return (CompositeByteBuf)super.setBoolean(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setChar(int index, int value)
/*      */   {
/* 1754 */     return (CompositeByteBuf)super.setChar(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setFloat(int index, float value)
/*      */   {
/* 1759 */     return (CompositeByteBuf)super.setFloat(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setDouble(int index, double value)
/*      */   {
/* 1764 */     return (CompositeByteBuf)super.setDouble(index, value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src)
/*      */   {
/* 1769 */     return (CompositeByteBuf)super.setBytes(index, src);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, ByteBuf src, int length)
/*      */   {
/* 1774 */     return (CompositeByteBuf)super.setBytes(index, src, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setBytes(int index, byte[] src)
/*      */   {
/* 1779 */     return (CompositeByteBuf)super.setBytes(index, src);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf setZero(int index, int length)
/*      */   {
/* 1784 */     return (CompositeByteBuf)super.setZero(index, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst)
/*      */   {
/* 1789 */     return (CompositeByteBuf)super.readBytes(dst);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst, int length)
/*      */   {
/* 1794 */     return (CompositeByteBuf)super.readBytes(dst, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuf dst, int dstIndex, int length)
/*      */   {
/* 1799 */     return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(byte[] dst)
/*      */   {
/* 1804 */     return (CompositeByteBuf)super.readBytes(dst);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(byte[] dst, int dstIndex, int length)
/*      */   {
/* 1809 */     return (CompositeByteBuf)super.readBytes(dst, dstIndex, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(ByteBuffer dst)
/*      */   {
/* 1814 */     return (CompositeByteBuf)super.readBytes(dst);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf readBytes(OutputStream out, int length) throws IOException
/*      */   {
/* 1819 */     return (CompositeByteBuf)super.readBytes(out, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf skipBytes(int length)
/*      */   {
/* 1824 */     return (CompositeByteBuf)super.skipBytes(length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBoolean(boolean value)
/*      */   {
/* 1829 */     return (CompositeByteBuf)super.writeBoolean(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeByte(int value)
/*      */   {
/* 1834 */     return (CompositeByteBuf)super.writeByte(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeShort(int value)
/*      */   {
/* 1839 */     return (CompositeByteBuf)super.writeShort(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeMedium(int value)
/*      */   {
/* 1844 */     return (CompositeByteBuf)super.writeMedium(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeInt(int value)
/*      */   {
/* 1849 */     return (CompositeByteBuf)super.writeInt(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeLong(long value)
/*      */   {
/* 1854 */     return (CompositeByteBuf)super.writeLong(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeChar(int value)
/*      */   {
/* 1859 */     return (CompositeByteBuf)super.writeChar(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeFloat(float value)
/*      */   {
/* 1864 */     return (CompositeByteBuf)super.writeFloat(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeDouble(double value)
/*      */   {
/* 1869 */     return (CompositeByteBuf)super.writeDouble(value);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src)
/*      */   {
/* 1874 */     return (CompositeByteBuf)super.writeBytes(src);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src, int length)
/*      */   {
/* 1879 */     return (CompositeByteBuf)super.writeBytes(src, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuf src, int srcIndex, int length)
/*      */   {
/* 1884 */     return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(byte[] src)
/*      */   {
/* 1889 */     return (CompositeByteBuf)super.writeBytes(src);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(byte[] src, int srcIndex, int length)
/*      */   {
/* 1894 */     return (CompositeByteBuf)super.writeBytes(src, srcIndex, length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeBytes(ByteBuffer src)
/*      */   {
/* 1899 */     return (CompositeByteBuf)super.writeBytes(src);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf writeZero(int length)
/*      */   {
/* 1904 */     return (CompositeByteBuf)super.writeZero(length);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf retain(int increment)
/*      */   {
/* 1909 */     return (CompositeByteBuf)super.retain(increment);
/*      */   }
/*      */   
/*      */   public CompositeByteBuf retain()
/*      */   {
/* 1914 */     return (CompositeByteBuf)super.retain();
/*      */   }
/*      */   
/*      */   public CompositeByteBuf touch()
/*      */   {
/* 1919 */     return this;
/*      */   }
/*      */   
/*      */   public CompositeByteBuf touch(Object hint)
/*      */   {
/* 1924 */     return this;
/*      */   }
/*      */   
/*      */   public ByteBuffer[] nioBuffers()
/*      */   {
/* 1929 */     return nioBuffers(readerIndex(), readableBytes());
/*      */   }
/*      */   
/*      */   public CompositeByteBuf discardSomeReadBytes()
/*      */   {
/* 1934 */     return discardReadComponents();
/*      */   }
/*      */   
/*      */   protected void deallocate()
/*      */   {
/* 1939 */     if (this.freed) {
/* 1940 */       return;
/*      */     }
/*      */     
/* 1943 */     this.freed = true;
/* 1944 */     int size = this.components.size();
/*      */     
/*      */ 
/* 1947 */     for (int i = 0; i < size; i++) {
/* 1948 */       ((Component)this.components.get(i)).freeIfNecessary();
/*      */     }
/*      */   }
/*      */   
/*      */   public ByteBuf unwrap()
/*      */   {
/* 1954 */     return null;
/*      */   }
/*      */   
/*      */   private final class CompositeByteBufIterator implements Iterator<ByteBuf> {
/* 1958 */     private final int size = CompositeByteBuf.this.components.size();
/*      */     private int index;
/*      */     
/*      */     private CompositeByteBufIterator() {}
/*      */     
/* 1963 */     public boolean hasNext() { return this.size > this.index; }
/*      */     
/*      */ 
/*      */     public ByteBuf next()
/*      */     {
/* 1968 */       if (this.size != CompositeByteBuf.this.components.size()) {
/* 1969 */         throw new ConcurrentModificationException();
/*      */       }
/* 1971 */       if (!hasNext()) {
/* 1972 */         throw new NoSuchElementException();
/*      */       }
/*      */       try {
/* 1975 */         return ((CompositeByteBuf.Component)CompositeByteBuf.this.components.get(this.index++)).buf;
/*      */       } catch (IndexOutOfBoundsException e) {
/* 1977 */         throw new ConcurrentModificationException();
/*      */       }
/*      */     }
/*      */     
/*      */     public void remove()
/*      */     {
/* 1983 */       throw new UnsupportedOperationException("Read-Only");
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class ComponentList extends ArrayList<CompositeByteBuf.Component>
/*      */   {
/*      */     ComponentList(int initialCapacity) {
/* 1990 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */     public void removeRange(int fromIndex, int toIndex)
/*      */     {
/* 1996 */       super.removeRange(fromIndex, toIndex);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\CompositeByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */