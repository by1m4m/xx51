/*      */ package com.sun.jna;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.nio.Buffer;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
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
/*      */ public abstract class Structure
/*      */ {
/*      */   public static final int ALIGN_DEFAULT = 0;
/*      */   public static final int ALIGN_NONE = 1;
/*      */   public static final int ALIGN_GNUC = 2;
/*      */   public static final int ALIGN_MSVC = 3;
/*      */   protected static final int CALCULATE_SIZE = -1;
/*  126 */   static final Map layoutInfo = new WeakHashMap();
/*  127 */   static final Map fieldOrder = new WeakHashMap();
/*      */   
/*      */   private Pointer memory;
/*      */   
/*  131 */   private int size = -1;
/*      */   
/*      */   private int alignType;
/*      */   
/*      */   private String encoding;
/*      */   private int actualAlignType;
/*      */   private int structAlignment;
/*      */   private Map structFields;
/*  139 */   private final Map nativeStrings = new HashMap();
/*      */   
/*      */   private TypeMapper typeMapper;
/*      */   
/*      */   private long typeInfo;
/*  144 */   private boolean autoRead = true;
/*  145 */   private boolean autoWrite = true;
/*      */   private Structure[] array;
/*      */   private boolean readCalled;
/*      */   
/*      */   protected Structure()
/*      */   {
/*  151 */     this(0);
/*      */   }
/*      */   
/*      */   protected Structure(TypeMapper mapper) {
/*  155 */     this(null, 0, mapper);
/*      */   }
/*      */   
/*      */   protected Structure(int alignType) {
/*  159 */     this(null, alignType);
/*      */   }
/*      */   
/*      */   protected Structure(int alignType, TypeMapper mapper) {
/*  163 */     this(null, alignType, mapper);
/*      */   }
/*      */   
/*      */   protected Structure(Pointer p)
/*      */   {
/*  168 */     this(p, 0);
/*      */   }
/*      */   
/*      */   protected Structure(Pointer p, int alignType) {
/*  172 */     this(p, alignType, null);
/*      */   }
/*      */   
/*      */   protected Structure(Pointer p, int alignType, TypeMapper mapper) {
/*  176 */     setAlignType(alignType);
/*  177 */     setStringEncoding(Native.getStringEncoding(getClass()));
/*  178 */     initializeTypeMapper(mapper);
/*  179 */     validateFields();
/*  180 */     if (p != null) {
/*  181 */       useMemory(p, 0, true);
/*      */     }
/*      */     else {
/*  184 */       allocateMemory(-1);
/*      */     }
/*  186 */     initializeFields();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Map fields()
/*      */   {
/*  197 */     return this.structFields;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   TypeMapper getTypeMapper()
/*      */   {
/*  204 */     return this.typeMapper;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initializeTypeMapper(TypeMapper mapper)
/*      */   {
/*  214 */     if (mapper == null) {
/*  215 */       mapper = Native.getTypeMapper(getClass());
/*      */     }
/*  217 */     this.typeMapper = mapper;
/*  218 */     layoutChanged();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void layoutChanged()
/*      */   {
/*  225 */     if (this.size != -1) {
/*  226 */       this.size = -1;
/*  227 */       if ((this.memory instanceof AutoAllocated)) {
/*  228 */         this.memory = null;
/*      */       }
/*      */       
/*  231 */       ensureAllocated();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setStringEncoding(String encoding)
/*      */   {
/*  240 */     this.encoding = encoding;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getStringEncoding()
/*      */   {
/*  248 */     return this.encoding;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setAlignType(int alignType)
/*      */   {
/*  257 */     this.alignType = alignType;
/*  258 */     if (alignType == 0) {
/*  259 */       alignType = Native.getStructureAlignment(getClass());
/*  260 */       if (alignType == 0) {
/*  261 */         if (Platform.isWindows()) {
/*  262 */           alignType = 3;
/*      */         } else
/*  264 */           alignType = 2;
/*      */       }
/*      */     }
/*  267 */     this.actualAlignType = alignType;
/*  268 */     layoutChanged();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Memory autoAllocate(int size)
/*      */   {
/*  277 */     return new AutoAllocated(size);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void useMemory(Pointer m)
/*      */   {
/*  287 */     useMemory(m, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void useMemory(Pointer m, int offset)
/*      */   {
/*  299 */     useMemory(m, offset, false);
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
/*      */   void useMemory(Pointer m, int offset, boolean force)
/*      */   {
/*      */     try
/*      */     {
/*  315 */       this.nativeStrings.clear();
/*      */       
/*  317 */       if (((this instanceof ByValue)) && (!force))
/*      */       {
/*      */ 
/*  320 */         byte[] buf = new byte[size()];
/*  321 */         m.read(0L, buf, 0, buf.length);
/*  322 */         this.memory.write(0L, buf, 0, buf.length);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  327 */         this.memory = m.share(offset);
/*  328 */         if (this.size == -1) {
/*  329 */           this.size = calculateSize(false);
/*      */         }
/*  331 */         if (this.size != -1) {
/*  332 */           this.memory = m.share(offset, this.size);
/*      */         }
/*      */       }
/*  335 */       this.array = null;
/*  336 */       this.readCalled = false;
/*      */     }
/*      */     catch (IndexOutOfBoundsException e) {
/*  339 */       throw new IllegalArgumentException("Structure exceeds provided memory bounds", e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void ensureAllocated()
/*      */   {
/*  346 */     ensureAllocated(false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void ensureAllocated(boolean avoidFFIType)
/*      */   {
/*  355 */     if (this.memory == null) {
/*  356 */       allocateMemory(avoidFFIType);
/*      */     }
/*  358 */     else if (this.size == -1) {
/*  359 */       this.size = calculateSize(true, avoidFFIType);
/*  360 */       if (!(this.memory instanceof AutoAllocated)) {
/*      */         try
/*      */         {
/*  363 */           this.memory = this.memory.share(0L, this.size);
/*      */         }
/*      */         catch (IndexOutOfBoundsException e) {
/*  366 */           throw new IllegalArgumentException("Structure exceeds provided memory bounds", e);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void allocateMemory()
/*      */   {
/*  376 */     allocateMemory(false);
/*      */   }
/*      */   
/*      */   private void allocateMemory(boolean avoidFFIType) {
/*  380 */     allocateMemory(calculateSize(true, avoidFFIType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void allocateMemory(int size)
/*      */   {
/*  391 */     if (size == -1)
/*      */     {
/*  393 */       size = calculateSize(false);
/*      */     }
/*  395 */     else if (size <= 0) {
/*  396 */       throw new IllegalArgumentException("Structure size must be greater than zero: " + size);
/*      */     }
/*      */     
/*      */ 
/*  400 */     if (size != -1) {
/*  401 */       if ((this.memory == null) || ((this.memory instanceof AutoAllocated)))
/*      */       {
/*  403 */         this.memory = autoAllocate(size);
/*      */       }
/*  405 */       this.size = size;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int size()
/*      */   {
/*  413 */     ensureAllocated();
/*  414 */     return this.size;
/*      */   }
/*      */   
/*      */   public void clear()
/*      */   {
/*  419 */     ensureAllocated();
/*  420 */     this.memory.clear(size());
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
/*      */   public Pointer getPointer()
/*      */   {
/*  434 */     ensureAllocated();
/*  435 */     return this.memory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  444 */   private static final ThreadLocal reads = new ThreadLocal() {
/*      */     protected synchronized Object initialValue() {
/*  446 */       return new HashMap();
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*  452 */   private static final ThreadLocal busy = new ThreadLocal()
/*      */   {
/*  454 */     protected synchronized Object initialValue() { return new Structure.StructureSet(); }
/*      */   };
/*      */   
/*      */   public static abstract interface ByValue {}
/*      */   
/*      */   public static abstract interface ByReference {}
/*      */   
/*      */   static class StructureSet extends AbstractCollection implements Set {
/*      */     Structure[] elements;
/*      */     private int count;
/*      */     
/*  465 */     private void ensureCapacity(int size) { if (this.elements == null) {
/*  466 */         this.elements = new Structure[size * 3 / 2];
/*      */       }
/*  468 */       else if (this.elements.length < size) {
/*  469 */         Structure[] e = new Structure[size * 3 / 2];
/*  470 */         System.arraycopy(this.elements, 0, e, 0, this.elements.length);
/*  471 */         this.elements = e;
/*      */       }
/*      */     }
/*      */     
/*  475 */     public Structure[] getElements() { return this.elements; }
/*      */     
/*  477 */     public int size() { return this.count; }
/*      */     
/*  479 */     public boolean contains(Object o) { return indexOf(o) != -1; }
/*      */     
/*      */     public boolean add(Object o) {
/*  482 */       if (!contains(o)) {
/*  483 */         ensureCapacity(this.count + 1);
/*  484 */         this.elements[(this.count++)] = ((Structure)o);
/*      */       }
/*  486 */       return true;
/*      */     }
/*      */     
/*  489 */     private int indexOf(Object o) { Structure s1 = (Structure)o;
/*  490 */       for (int i = 0; i < this.count; i++) {
/*  491 */         Structure s2 = this.elements[i];
/*  492 */         if ((s1 == s2) || (
/*  493 */           (s1.getClass() == s2.getClass()) && 
/*  494 */           (s1.size() == s2.size()) && 
/*  495 */           (s1.getPointer().equals(s2.getPointer())))) {
/*  496 */           return i;
/*      */         }
/*      */       }
/*  499 */       return -1;
/*      */     }
/*      */     
/*  502 */     public boolean remove(Object o) { int idx = indexOf(o);
/*  503 */       if (idx != -1) {
/*  504 */         if (--this.count >= 0) {
/*  505 */           this.elements[idx] = this.elements[this.count];
/*  506 */           this.elements[this.count] = null;
/*      */         }
/*  508 */         return true;
/*      */       }
/*  510 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     public Iterator iterator()
/*      */     {
/*  516 */       Structure[] e = new Structure[this.count];
/*  517 */       if (this.count > 0) {
/*  518 */         System.arraycopy(this.elements, 0, e, 0, this.count);
/*      */       }
/*  520 */       return Arrays.asList(e).iterator();
/*      */     }
/*      */   }
/*      */   
/*      */   static Set busy() {
/*  525 */     return (Set)busy.get();
/*      */   }
/*      */   
/*  528 */   static Map reading() { return (Map)reads.get(); }
/*      */   
/*      */ 
/*      */   void conditionalAutoRead()
/*      */   {
/*  533 */     if (!this.readCalled) {
/*  534 */       autoRead();
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void read()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 27	com/sun/jna/Structure:memory	Lcom/sun/jna/Pointer;
/*      */     //   4: getstatic 1	com/sun/jna/Structure:PLACEHOLDER_MEMORY	Lcom/sun/jna/Pointer;
/*      */     //   7: if_acmpne +4 -> 11
/*      */     //   10: return
/*      */     //   11: aload_0
/*      */     //   12: iconst_1
/*      */     //   13: putfield 46	com/sun/jna/Structure:readCalled	Z
/*      */     //   16: aload_0
/*      */     //   17: invokevirtual 29	com/sun/jna/Structure:ensureAllocated	()V
/*      */     //   20: invokestatic 68	com/sun/jna/Structure:busy	()Ljava/util/Set;
/*      */     //   23: aload_0
/*      */     //   24: invokeinterface 69 2 0
/*      */     //   29: ifeq +4 -> 33
/*      */     //   32: return
/*      */     //   33: invokestatic 68	com/sun/jna/Structure:busy	()Ljava/util/Set;
/*      */     //   36: aload_0
/*      */     //   37: invokeinterface 70 2 0
/*      */     //   42: pop
/*      */     //   43: aload_0
/*      */     //   44: instanceof 71
/*      */     //   47: ifeq +17 -> 64
/*      */     //   50: invokestatic 72	com/sun/jna/Structure:reading	()Ljava/util/Map;
/*      */     //   53: aload_0
/*      */     //   54: invokevirtual 73	com/sun/jna/Structure:getPointer	()Lcom/sun/jna/Pointer;
/*      */     //   57: aload_0
/*      */     //   58: invokeinterface 74 3 0
/*      */     //   63: pop
/*      */     //   64: aload_0
/*      */     //   65: invokevirtual 75	com/sun/jna/Structure:fields	()Ljava/util/Map;
/*      */     //   68: invokeinterface 76 1 0
/*      */     //   73: invokeinterface 77 1 0
/*      */     //   78: astore_1
/*      */     //   79: aload_1
/*      */     //   80: invokeinterface 78 1 0
/*      */     //   85: ifeq +22 -> 107
/*      */     //   88: aload_1
/*      */     //   89: invokeinterface 79 1 0
/*      */     //   94: checkcast 80	com/sun/jna/Structure$StructField
/*      */     //   97: astore_2
/*      */     //   98: aload_0
/*      */     //   99: aload_2
/*      */     //   100: invokevirtual 81	com/sun/jna/Structure:readField	(Lcom/sun/jna/Structure$StructField;)Ljava/lang/Object;
/*      */     //   103: pop
/*      */     //   104: goto -25 -> 79
/*      */     //   107: invokestatic 68	com/sun/jna/Structure:busy	()Ljava/util/Set;
/*      */     //   110: aload_0
/*      */     //   111: invokeinterface 82 2 0
/*      */     //   116: pop
/*      */     //   117: invokestatic 72	com/sun/jna/Structure:reading	()Ljava/util/Map;
/*      */     //   120: aload_0
/*      */     //   121: invokevirtual 73	com/sun/jna/Structure:getPointer	()Lcom/sun/jna/Pointer;
/*      */     //   124: invokeinterface 83 2 0
/*      */     //   129: aload_0
/*      */     //   130: if_acmpne +61 -> 191
/*      */     //   133: invokestatic 72	com/sun/jna/Structure:reading	()Ljava/util/Map;
/*      */     //   136: aload_0
/*      */     //   137: invokevirtual 73	com/sun/jna/Structure:getPointer	()Lcom/sun/jna/Pointer;
/*      */     //   140: invokeinterface 84 2 0
/*      */     //   145: pop
/*      */     //   146: goto +45 -> 191
/*      */     //   149: astore_3
/*      */     //   150: invokestatic 68	com/sun/jna/Structure:busy	()Ljava/util/Set;
/*      */     //   153: aload_0
/*      */     //   154: invokeinterface 82 2 0
/*      */     //   159: pop
/*      */     //   160: invokestatic 72	com/sun/jna/Structure:reading	()Ljava/util/Map;
/*      */     //   163: aload_0
/*      */     //   164: invokevirtual 73	com/sun/jna/Structure:getPointer	()Lcom/sun/jna/Pointer;
/*      */     //   167: invokeinterface 83 2 0
/*      */     //   172: aload_0
/*      */     //   173: if_acmpne +16 -> 189
/*      */     //   176: invokestatic 72	com/sun/jna/Structure:reading	()Ljava/util/Map;
/*      */     //   179: aload_0
/*      */     //   180: invokevirtual 73	com/sun/jna/Structure:getPointer	()Lcom/sun/jna/Pointer;
/*      */     //   183: invokeinterface 84 2 0
/*      */     //   188: pop
/*      */     //   189: aload_3
/*      */     //   190: athrow
/*      */     //   191: return
/*      */     // Line number table:
/*      */     //   Java source line #543	-> byte code offset #0
/*      */     //   Java source line #544	-> byte code offset #10
/*      */     //   Java source line #546	-> byte code offset #11
/*      */     //   Java source line #552	-> byte code offset #16
/*      */     //   Java source line #555	-> byte code offset #20
/*      */     //   Java source line #556	-> byte code offset #32
/*      */     //   Java source line #558	-> byte code offset #33
/*      */     //   Java source line #559	-> byte code offset #43
/*      */     //   Java source line #560	-> byte code offset #50
/*      */     //   Java source line #563	-> byte code offset #64
/*      */     //   Java source line #564	-> byte code offset #88
/*      */     //   Java source line #565	-> byte code offset #98
/*      */     //   Java source line #566	-> byte code offset #104
/*      */     //   Java source line #569	-> byte code offset #107
/*      */     //   Java source line #570	-> byte code offset #117
/*      */     //   Java source line #571	-> byte code offset #133
/*      */     //   Java source line #569	-> byte code offset #149
/*      */     //   Java source line #570	-> byte code offset #160
/*      */     //   Java source line #571	-> byte code offset #176
/*      */     //   Java source line #574	-> byte code offset #191
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	192	0	this	Structure
/*      */     //   78	11	1	i	Iterator
/*      */     //   97	3	2	structField	StructField
/*      */     //   149	41	3	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   64	107	149	finally
/*      */   }
/*      */   
/*      */   protected int fieldOffset(String name)
/*      */   {
/*  581 */     ensureAllocated();
/*  582 */     StructField f = (StructField)fields().get(name);
/*  583 */     if (f == null)
/*  584 */       throw new IllegalArgumentException("No such field: " + name);
/*  585 */     return f.offset;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object readField(String name)
/*      */   {
/*  595 */     ensureAllocated();
/*  596 */     StructField f = (StructField)fields().get(name);
/*  597 */     if (f == null)
/*  598 */       throw new IllegalArgumentException("No such field: " + name);
/*  599 */     return readField(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   Object getFieldValue(Field field)
/*      */   {
/*      */     try
/*      */     {
/*  609 */       return field.get(this);
/*      */     }
/*      */     catch (Exception e) {
/*  612 */       throw new Error("Exception reading field '" + field.getName() + "' in " + getClass(), e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   void setFieldValue(Field field, Object value)
/*      */   {
/*  621 */     setFieldValue(field, value, false);
/*      */   }
/*      */   
/*      */   private void setFieldValue(Field field, Object value, boolean overrideFinal)
/*      */   {
/*      */     try {
/*  627 */       field.set(this, value);
/*      */     }
/*      */     catch (IllegalAccessException e) {
/*  630 */       int modifiers = field.getModifiers();
/*  631 */       if (Modifier.isFinal(modifiers)) {
/*  632 */         if (overrideFinal)
/*      */         {
/*      */ 
/*  635 */           throw new UnsupportedOperationException("This VM does not support Structures with final fields (field '" + field.getName() + "' within " + getClass() + ")", e);
/*      */         }
/*  637 */         throw new UnsupportedOperationException("Attempt to write to read-only field '" + field.getName() + "' within " + getClass(), e);
/*      */       }
/*  639 */       throw new Error("Unexpectedly unable to write to field '" + field.getName() + "' within " + getClass(), e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static Structure updateStructureByReference(Class type, Structure s, Pointer address)
/*      */   {
/*  651 */     if (address == null) {
/*  652 */       s = null;
/*      */ 
/*      */     }
/*  655 */     else if ((s == null) || (!address.equals(s.getPointer()))) {
/*  656 */       Structure s1 = (Structure)reading().get(address);
/*  657 */       if ((s1 != null) && (type.equals(s1.getClass()))) {
/*  658 */         s = s1;
/*  659 */         s.autoRead();
/*      */       }
/*      */       else {
/*  662 */         s = newInstance(type, address);
/*  663 */         s.conditionalAutoRead();
/*      */       }
/*      */     }
/*      */     else {
/*  667 */       s.autoRead();
/*      */     }
/*      */     
/*  670 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object readField(StructField structField)
/*      */   {
/*  682 */     int offset = structField.offset;
/*      */     
/*      */ 
/*  685 */     Class fieldType = structField.type;
/*  686 */     FromNativeConverter readConverter = structField.readConverter;
/*  687 */     if (readConverter != null) {
/*  688 */       fieldType = readConverter.nativeType();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  697 */     Object currentValue = (Structure.class.isAssignableFrom(fieldType)) || (Callback.class.isAssignableFrom(fieldType)) || ((Platform.HAS_BUFFERS) && (Buffer.class.isAssignableFrom(fieldType))) || (Pointer.class.isAssignableFrom(fieldType)) || (NativeMapped.class.isAssignableFrom(fieldType)) || (fieldType.isArray()) ? getFieldValue(structField.field) : null;
/*      */     Object result;
/*      */     Object result;
/*  700 */     if (fieldType == String.class) {
/*  701 */       Pointer p = this.memory.getPointer(offset);
/*  702 */       result = p == null ? null : p.getString(0L, this.encoding);
/*      */     }
/*      */     else {
/*  705 */       result = this.memory.getValue(offset, fieldType, currentValue);
/*      */     }
/*  707 */     if (readConverter != null) {
/*  708 */       result = readConverter.fromNative(result, structField.context);
/*  709 */       if ((currentValue != null) && (currentValue.equals(result))) {
/*  710 */         result = currentValue;
/*      */       }
/*      */     }
/*      */     
/*  714 */     if ((fieldType.equals(String.class)) || 
/*  715 */       (fieldType.equals(WString.class))) {
/*  716 */       this.nativeStrings.put(structField.name + ".ptr", this.memory.getPointer(offset));
/*  717 */       this.nativeStrings.put(structField.name + ".val", result);
/*      */     }
/*      */     
/*      */ 
/*  721 */     setFieldValue(structField.field, result, true);
/*  722 */     return result;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void write()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 27	com/sun/jna/Structure:memory	Lcom/sun/jna/Pointer;
/*      */     //   4: getstatic 1	com/sun/jna/Structure:PLACEHOLDER_MEMORY	Lcom/sun/jna/Pointer;
/*      */     //   7: if_acmpne +4 -> 11
/*      */     //   10: return
/*      */     //   11: aload_0
/*      */     //   12: invokevirtual 29	com/sun/jna/Structure:ensureAllocated	()V
/*      */     //   15: aload_0
/*      */     //   16: instanceof 38
/*      */     //   19: ifeq +8 -> 27
/*      */     //   22: aload_0
/*      */     //   23: invokevirtual 133	com/sun/jna/Structure:getTypeInfo	()Lcom/sun/jna/Pointer;
/*      */     //   26: pop
/*      */     //   27: invokestatic 68	com/sun/jna/Structure:busy	()Ljava/util/Set;
/*      */     //   30: aload_0
/*      */     //   31: invokeinterface 69 2 0
/*      */     //   36: ifeq +4 -> 40
/*      */     //   39: return
/*      */     //   40: invokestatic 68	com/sun/jna/Structure:busy	()Ljava/util/Set;
/*      */     //   43: aload_0
/*      */     //   44: invokeinterface 70 2 0
/*      */     //   49: pop
/*      */     //   50: aload_0
/*      */     //   51: invokevirtual 75	com/sun/jna/Structure:fields	()Ljava/util/Map;
/*      */     //   54: invokeinterface 76 1 0
/*      */     //   59: invokeinterface 77 1 0
/*      */     //   64: astore_1
/*      */     //   65: aload_1
/*      */     //   66: invokeinterface 78 1 0
/*      */     //   71: ifeq +28 -> 99
/*      */     //   74: aload_1
/*      */     //   75: invokeinterface 79 1 0
/*      */     //   80: checkcast 80	com/sun/jna/Structure$StructField
/*      */     //   83: astore_2
/*      */     //   84: aload_2
/*      */     //   85: getfield 134	com/sun/jna/Structure$StructField:isVolatile	Z
/*      */     //   88: ifne +8 -> 96
/*      */     //   91: aload_0
/*      */     //   92: aload_2
/*      */     //   93: invokevirtual 135	com/sun/jna/Structure:writeField	(Lcom/sun/jna/Structure$StructField;)V
/*      */     //   96: goto -31 -> 65
/*      */     //   99: invokestatic 68	com/sun/jna/Structure:busy	()Ljava/util/Set;
/*      */     //   102: aload_0
/*      */     //   103: invokeinterface 82 2 0
/*      */     //   108: pop
/*      */     //   109: goto +16 -> 125
/*      */     //   112: astore_3
/*      */     //   113: invokestatic 68	com/sun/jna/Structure:busy	()Ljava/util/Set;
/*      */     //   116: aload_0
/*      */     //   117: invokeinterface 82 2 0
/*      */     //   122: pop
/*      */     //   123: aload_3
/*      */     //   124: athrow
/*      */     //   125: return
/*      */     // Line number table:
/*      */     //   Java source line #730	-> byte code offset #0
/*      */     //   Java source line #731	-> byte code offset #10
/*      */     //   Java source line #737	-> byte code offset #11
/*      */     //   Java source line #740	-> byte code offset #15
/*      */     //   Java source line #741	-> byte code offset #22
/*      */     //   Java source line #745	-> byte code offset #27
/*      */     //   Java source line #746	-> byte code offset #39
/*      */     //   Java source line #748	-> byte code offset #40
/*      */     //   Java source line #751	-> byte code offset #50
/*      */     //   Java source line #752	-> byte code offset #74
/*      */     //   Java source line #753	-> byte code offset #84
/*      */     //   Java source line #754	-> byte code offset #91
/*      */     //   Java source line #756	-> byte code offset #96
/*      */     //   Java source line #759	-> byte code offset #99
/*      */     //   Java source line #760	-> byte code offset #109
/*      */     //   Java source line #759	-> byte code offset #112
/*      */     //   Java source line #761	-> byte code offset #125
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	126	0	this	Structure
/*      */     //   64	11	1	i	Iterator
/*      */     //   83	10	2	sf	StructField
/*      */     //   112	12	3	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   50	99	112	finally
/*      */   }
/*      */   
/*      */   public void writeField(String name)
/*      */   {
/*  769 */     ensureAllocated();
/*  770 */     StructField f = (StructField)fields().get(name);
/*  771 */     if (f == null)
/*  772 */       throw new IllegalArgumentException("No such field: " + name);
/*  773 */     writeField(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeField(String name, Object value)
/*      */   {
/*  784 */     ensureAllocated();
/*  785 */     StructField structField = (StructField)fields().get(name);
/*  786 */     if (structField == null)
/*  787 */       throw new IllegalArgumentException("No such field: " + name);
/*  788 */     setFieldValue(structField.field, value);
/*  789 */     writeField(structField);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void writeField(StructField structField)
/*      */   {
/*  797 */     if (structField.isReadOnly) {
/*  798 */       return;
/*      */     }
/*      */     
/*  801 */     int offset = structField.offset;
/*      */     
/*      */ 
/*  804 */     Object value = getFieldValue(structField.field);
/*      */     
/*      */ 
/*  807 */     Class fieldType = structField.type;
/*  808 */     ToNativeConverter converter = structField.writeConverter;
/*  809 */     if (converter != null) {
/*  810 */       value = converter.toNative(value, new StructureWriteContext(this, structField.field));
/*  811 */       fieldType = converter.nativeType();
/*      */     }
/*      */     
/*      */ 
/*  815 */     if ((String.class == fieldType) || (WString.class == fieldType))
/*      */     {
/*      */ 
/*  818 */       boolean wide = fieldType == WString.class;
/*  819 */       if (value != null)
/*      */       {
/*      */ 
/*  822 */         if ((this.nativeStrings.containsKey(structField.name + ".ptr")) && 
/*  823 */           (value.equals(this.nativeStrings.get(structField.name + ".val")))) {
/*  824 */           return;
/*      */         }
/*      */         
/*      */ 
/*  828 */         NativeString nativeString = wide ? new NativeString(value.toString(), true) : new NativeString(value.toString(), this.encoding);
/*      */         
/*      */ 
/*  831 */         this.nativeStrings.put(structField.name, nativeString);
/*  832 */         value = nativeString.getPointer();
/*      */       }
/*      */       else {
/*  835 */         this.nativeStrings.remove(structField.name);
/*      */       }
/*  837 */       this.nativeStrings.remove(structField.name + ".ptr");
/*  838 */       this.nativeStrings.remove(structField.name + ".val");
/*      */     }
/*      */     try
/*      */     {
/*  842 */       this.memory.setValue(offset, value, fieldType);
/*      */     }
/*      */     catch (IllegalArgumentException e) {
/*  845 */       String msg = "Structure field \"" + structField.name + "\" was declared as " + structField.type + (structField.type == fieldType ? "" : new StringBuilder().append(" (native type ").append(fieldType).append(")").toString()) + ", which is not supported within a Structure";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  850 */       throw new IllegalArgumentException(msg, e);
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
/*      */   protected abstract List getFieldOrder();
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected final void setFieldOrder(String[] fields)
/*      */   {
/*  886 */     throw new Error("This method is obsolete, use getFieldOrder() instead");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void sortFields(List fields, List names)
/*      */   {
/*  894 */     for (int i = 0; i < names.size(); i++) {
/*  895 */       String name = (String)names.get(i);
/*  896 */       for (int f = 0; f < fields.size(); f++) {
/*  897 */         Field field = (Field)fields.get(f);
/*  898 */         if (name.equals(field.getName())) {
/*  899 */           Collections.swap(fields, i, f);
/*  900 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List getFieldList()
/*      */   {
/*  911 */     List flist = new ArrayList();
/*  912 */     for (Class cls = getClass(); 
/*  913 */         !cls.equals(Structure.class); 
/*  914 */         cls = cls.getSuperclass()) {
/*  915 */       List classFields = new ArrayList();
/*  916 */       Field[] fields = cls.getDeclaredFields();
/*  917 */       for (int i = 0; i < fields.length; i++) {
/*  918 */         int modifiers = fields[i].getModifiers();
/*  919 */         if ((!Modifier.isStatic(modifiers)) && 
/*  920 */           (Modifier.isPublic(modifiers)))
/*      */         {
/*  922 */           classFields.add(fields[i]); }
/*      */       }
/*  924 */       flist.addAll(0, classFields);
/*      */     }
/*  926 */     return flist;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private List fieldOrder()
/*      */   {
/*  933 */     synchronized (fieldOrder) {
/*  934 */       List list = (List)fieldOrder.get(getClass());
/*  935 */       if (list == null) {
/*  936 */         list = getFieldOrder();
/*  937 */         fieldOrder.put(getClass(), list);
/*      */       }
/*  939 */       return list;
/*      */     }
/*      */   }
/*      */   
/*      */   private List sort(Collection c) {
/*  944 */     List list = new ArrayList(c);
/*  945 */     Collections.sort(list);
/*  946 */     return list;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List getFields(boolean force)
/*      */   {
/*  957 */     List flist = getFieldList();
/*  958 */     Set names = new HashSet();
/*  959 */     for (Iterator i = flist.iterator(); i.hasNext();) {
/*  960 */       names.add(((Field)i.next()).getName());
/*      */     }
/*  962 */     List fieldOrder = fieldOrder();
/*  963 */     if ((fieldOrder.size() != flist.size()) && (flist.size() > 1)) {
/*  964 */       if (force)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  971 */         throw new Error("Structure.getFieldOrder() on " + getClass() + " does not provide enough names [" + fieldOrder.size() + "] (" + sort(fieldOrder) + ") to match declared fields [" + flist.size() + "] (" + sort(names) + ")");
/*      */       }
/*      */       
/*  974 */       return null;
/*      */     }
/*      */     
/*  977 */     Set orderedNames = new HashSet(fieldOrder);
/*  978 */     if (!orderedNames.equals(names))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  983 */       throw new Error("Structure.getFieldOrder() on " + getClass() + " returns names (" + sort(fieldOrder) + ") which do not match declared field names (" + sort(names) + ")");
/*      */     }
/*      */     
/*  986 */     sortFields(flist, fieldOrder);
/*      */     
/*  988 */     return flist;
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
/*      */   protected int calculateSize(boolean force)
/*      */   {
/* 1006 */     return calculateSize(force, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static int size(Class type)
/*      */   {
/* 1014 */     return size(type, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static int size(Class type, Structure value)
/*      */   {
/*      */     LayoutInfo info;
/*      */     
/*      */ 
/* 1024 */     synchronized (layoutInfo) {
/* 1025 */       info = (LayoutInfo)layoutInfo.get(type);
/*      */     }
/* 1027 */     int sz = (info != null) && (!info.variable) ? info.size : -1;
/* 1028 */     if (sz == -1) {
/* 1029 */       if (value == null) {
/* 1030 */         value = newInstance(type, PLACEHOLDER_MEMORY);
/*      */       }
/* 1032 */       sz = value.size();
/*      */     }
/* 1034 */     return sz;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int calculateSize(boolean force, boolean avoidFFIType)
/*      */   {
/* 1045 */     int size = -1;
/*      */     LayoutInfo info;
/* 1047 */     synchronized (layoutInfo) {
/* 1048 */       info = (LayoutInfo)layoutInfo.get(getClass());
/*      */     }
/* 1050 */     if ((info == null) || 
/* 1051 */       (this.alignType != info.alignType) || 
/* 1052 */       (this.typeMapper != info.typeMapper)) {
/* 1053 */       info = deriveLayout(force, avoidFFIType);
/*      */     }
/* 1055 */     if (info != null) {
/* 1056 */       this.structAlignment = info.alignment;
/* 1057 */       this.structFields = info.fields;
/*      */       
/* 1059 */       if (!info.variable) {
/* 1060 */         synchronized (layoutInfo)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1066 */           if ((!layoutInfo.containsKey(getClass())) || (this.alignType != 0) || (this.typeMapper != null))
/*      */           {
/*      */ 
/* 1069 */             layoutInfo.put(getClass(), info);
/*      */           }
/*      */         }
/*      */       }
/* 1073 */       size = info.size;
/*      */     }
/* 1075 */     return size;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class LayoutInfo
/*      */   {
/* 1082 */     private int size = -1;
/* 1083 */     private int alignment = 1;
/* 1084 */     private final Map fields = Collections.synchronizedMap(new LinkedHashMap());
/* 1085 */     private int alignType = 0;
/*      */     private TypeMapper typeMapper;
/*      */     private boolean variable;
/*      */     private Structure.StructField typeInfoField;
/*      */   }
/*      */   
/*      */   private void validateField(String name, Class type)
/*      */   {
/* 1093 */     if (this.typeMapper != null) {
/* 1094 */       ToNativeConverter toNative = this.typeMapper.getToNativeConverter(type);
/* 1095 */       if (toNative != null) {
/* 1096 */         validateField(name, toNative.nativeType());
/* 1097 */         return;
/*      */       }
/*      */     }
/* 1100 */     if (type.isArray()) {
/* 1101 */       validateField(name, type.getComponentType());
/*      */     } else {
/*      */       try
/*      */       {
/* 1105 */         getNativeSize(type);
/*      */       }
/*      */       catch (IllegalArgumentException e) {
/* 1108 */         String msg = "Invalid Structure field in " + getClass() + ", field name '" + name + "' (" + type + "): " + e.getMessage();
/* 1109 */         throw new IllegalArgumentException(msg, e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void validateFields()
/*      */   {
/* 1116 */     List fields = getFieldList();
/* 1117 */     for (Iterator i = fields.iterator(); i.hasNext();) {
/* 1118 */       Field f = (Field)i.next();
/* 1119 */       validateField(f.getName(), f.getType());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private LayoutInfo deriveLayout(boolean force, boolean avoidFFIType)
/*      */   {
/* 1128 */     int calculatedSize = 0;
/* 1129 */     List fields = getFields(force);
/* 1130 */     if (fields == null) {
/* 1131 */       return null;
/*      */     }
/*      */     
/* 1134 */     LayoutInfo info = new LayoutInfo(null);
/* 1135 */     info.alignType = this.alignType;
/* 1136 */     info.typeMapper = this.typeMapper;
/*      */     
/* 1138 */     boolean firstField = true;
/* 1139 */     for (Iterator i = fields.iterator(); i.hasNext(); firstField = false) {
/* 1140 */       Field field = (Field)i.next();
/* 1141 */       int modifiers = field.getModifiers();
/*      */       
/* 1143 */       Class type = field.getType();
/* 1144 */       if (type.isArray()) {
/* 1145 */         info.variable = true;
/*      */       }
/* 1147 */       StructField structField = new StructField();
/* 1148 */       structField.isVolatile = Modifier.isVolatile(modifiers);
/* 1149 */       structField.isReadOnly = Modifier.isFinal(modifiers);
/* 1150 */       if (structField.isReadOnly) {
/* 1151 */         if (!Platform.RO_FIELDS)
/*      */         {
/* 1153 */           throw new IllegalArgumentException("This VM does not support read-only fields (field '" + field.getName() + "' within " + getClass() + ")");
/*      */         }
/*      */         
/*      */ 
/* 1157 */         field.setAccessible(true);
/*      */       }
/* 1159 */       structField.field = field;
/* 1160 */       structField.name = field.getName();
/* 1161 */       structField.type = type;
/*      */       
/*      */ 
/* 1164 */       if ((Callback.class.isAssignableFrom(type)) && (!type.isInterface()))
/*      */       {
/* 1166 */         throw new IllegalArgumentException("Structure Callback field '" + field.getName() + "' must be an interface");
/*      */       }
/*      */       
/* 1169 */       if ((type.isArray()) && 
/* 1170 */         (Structure.class.equals(type.getComponentType()))) {
/* 1171 */         String msg = "Nested Structure arrays must use a derived Structure type so that the size of the elements can be determined";
/*      */         
/*      */ 
/* 1174 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */       
/* 1177 */       int fieldAlignment = 1;
/* 1178 */       if (Modifier.isPublic(field.getModifiers()))
/*      */       {
/*      */ 
/*      */ 
/* 1182 */         Object value = getFieldValue(structField.field);
/* 1183 */         if ((value == null) && (type.isArray())) {
/* 1184 */           if (force) {
/* 1185 */             throw new IllegalStateException("Array fields must be initialized");
/*      */           }
/*      */           
/* 1188 */           return null;
/*      */         }
/* 1190 */         Class nativeType = type;
/* 1191 */         if (NativeMapped.class.isAssignableFrom(type)) {
/* 1192 */           NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/* 1193 */           nativeType = tc.nativeType();
/* 1194 */           structField.writeConverter = tc;
/* 1195 */           structField.readConverter = tc;
/* 1196 */           structField.context = new StructureReadContext(this, field);
/*      */         }
/* 1198 */         else if (this.typeMapper != null) {
/* 1199 */           ToNativeConverter writeConverter = this.typeMapper.getToNativeConverter(type);
/* 1200 */           FromNativeConverter readConverter = this.typeMapper.getFromNativeConverter(type);
/* 1201 */           if ((writeConverter != null) && (readConverter != null)) {
/* 1202 */             value = writeConverter.toNative(value, new StructureWriteContext(this, structField.field));
/*      */             
/* 1204 */             nativeType = value != null ? value.getClass() : Pointer.class;
/* 1205 */             structField.writeConverter = writeConverter;
/* 1206 */             structField.readConverter = readConverter;
/* 1207 */             structField.context = new StructureReadContext(this, field);
/*      */           }
/* 1209 */           else if ((writeConverter != null) || (readConverter != null)) {
/* 1210 */             String msg = "Structures require bidirectional type conversion for " + type;
/* 1211 */             throw new IllegalArgumentException(msg);
/*      */           }
/*      */         }
/*      */         
/* 1215 */         if (value == null) {
/* 1216 */           value = initializeField(structField.field, type);
/*      */         }
/*      */         try
/*      */         {
/* 1220 */           structField.size = getNativeSize(nativeType, value);
/* 1221 */           fieldAlignment = getNativeAlignment(nativeType, value, firstField);
/*      */         }
/*      */         catch (IllegalArgumentException e)
/*      */         {
/* 1225 */           if ((!force) && (this.typeMapper == null)) {
/* 1226 */             return null;
/*      */           }
/* 1228 */           String msg = "Invalid Structure field in " + getClass() + ", field name '" + structField.name + "' (" + structField.type + "): " + e.getMessage();
/* 1229 */           throw new IllegalArgumentException(msg, e);
/*      */         }
/*      */         
/*      */ 
/* 1233 */         if (fieldAlignment == 0) {
/* 1234 */           throw new Error("Field alignment is zero for field '" + structField.name + "' within " + getClass());
/*      */         }
/* 1236 */         info.alignment = Math.max(info.alignment, fieldAlignment);
/* 1237 */         if (calculatedSize % fieldAlignment != 0) {
/* 1238 */           calculatedSize += fieldAlignment - calculatedSize % fieldAlignment;
/*      */         }
/* 1240 */         if ((this instanceof Union)) {
/* 1241 */           structField.offset = 0;
/* 1242 */           calculatedSize = Math.max(calculatedSize, structField.size);
/*      */         }
/*      */         else {
/* 1245 */           structField.offset = calculatedSize;
/* 1246 */           calculatedSize += structField.size;
/*      */         }
/*      */         
/*      */ 
/* 1250 */         info.fields.put(structField.name, structField);
/*      */         
/* 1252 */         if ((info.typeInfoField == null) || 
/* 1253 */           (info.typeInfoField.size < structField.size) || (
/* 1254 */           (info.typeInfoField.size == structField.size) && 
/* 1255 */           (Structure.class.isAssignableFrom(structField.type)))) {
/* 1256 */           info.typeInfoField = structField;
/*      */         }
/*      */       }
/*      */     }
/* 1260 */     if (calculatedSize > 0) {
/* 1261 */       int size = addPadding(calculatedSize, info.alignment);
/*      */       
/* 1263 */       if (((this instanceof ByValue)) && (!avoidFFIType)) {
/* 1264 */         getTypeInfo();
/*      */       }
/* 1266 */       info.size = size;
/* 1267 */       return info;
/*      */     }
/*      */     
/* 1270 */     throw new IllegalArgumentException("Structure " + getClass() + " has unknown or zero size (ensure " + "all fields are public)");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initializeFields()
/*      */   {
/* 1281 */     List flist = getFieldList();
/* 1282 */     for (Iterator i = flist.iterator(); i.hasNext();) {
/* 1283 */       Field f = (Field)i.next();
/*      */       try {
/* 1285 */         Object o = f.get(this);
/* 1286 */         if (o == null) {
/* 1287 */           initializeField(f, f.getType());
/*      */         }
/*      */       }
/*      */       catch (Exception e) {
/* 1291 */         throw new Error("Exception reading field '" + f.getName() + "' in " + getClass(), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private Object initializeField(Field field, Class type) {
/* 1297 */     Object value = null;
/* 1298 */     if ((Structure.class.isAssignableFrom(type)) && 
/* 1299 */       (!ByReference.class.isAssignableFrom(type))) {
/*      */       try {
/* 1301 */         value = newInstance(type, PLACEHOLDER_MEMORY);
/* 1302 */         setFieldValue(field, value);
/*      */       }
/*      */       catch (IllegalArgumentException e) {
/* 1305 */         String msg = "Can't determine size of nested structure";
/* 1306 */         throw new IllegalArgumentException(msg, e);
/*      */       }
/*      */     }
/* 1309 */     else if (NativeMapped.class.isAssignableFrom(type)) {
/* 1310 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/* 1311 */       value = tc.defaultValue();
/* 1312 */       setFieldValue(field, value);
/*      */     }
/* 1314 */     return value;
/*      */   }
/*      */   
/*      */   private int addPadding(int calculatedSize) {
/* 1318 */     return addPadding(calculatedSize, this.structAlignment);
/*      */   }
/*      */   
/*      */ 
/*      */   private int addPadding(int calculatedSize, int alignment)
/*      */   {
/* 1324 */     if ((this.actualAlignType != 1) && 
/* 1325 */       (calculatedSize % alignment != 0)) {
/* 1326 */       calculatedSize += alignment - calculatedSize % alignment;
/*      */     }
/*      */     
/* 1329 */     return calculatedSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected int getStructAlignment()
/*      */   {
/* 1336 */     if (this.size == -1)
/*      */     {
/* 1338 */       calculateSize(true);
/*      */     }
/* 1340 */     return this.structAlignment;
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
/*      */   protected int getNativeAlignment(Class type, Object value, boolean isFirstElement)
/*      */   {
/* 1354 */     int alignment = 1;
/* 1355 */     if (NativeMapped.class.isAssignableFrom(type)) {
/* 1356 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/* 1357 */       type = tc.nativeType();
/* 1358 */       value = tc.toNative(value, new ToNativeContext());
/*      */     }
/* 1360 */     int size = Native.getNativeSize(type, value);
/* 1361 */     if ((type.isPrimitive()) || (Long.class == type) || (Integer.class == type) || (Short.class == type) || (Character.class == type) || (Byte.class == type) || (Boolean.class == type) || (Float.class == type) || (Double.class == type))
/*      */     {
/*      */ 
/*      */ 
/* 1365 */       alignment = size;
/*      */     }
/* 1367 */     else if (((Pointer.class.isAssignableFrom(type)) && (!Function.class.isAssignableFrom(type))) || ((Platform.HAS_BUFFERS) && 
/* 1368 */       (Buffer.class.isAssignableFrom(type))) || 
/* 1369 */       (Callback.class.isAssignableFrom(type)) || (WString.class == type) || (String.class == type))
/*      */     {
/*      */ 
/* 1372 */       alignment = Pointer.SIZE;
/*      */     }
/* 1374 */     else if (Structure.class.isAssignableFrom(type)) {
/* 1375 */       if (ByReference.class.isAssignableFrom(type)) {
/* 1376 */         alignment = Pointer.SIZE;
/*      */       }
/*      */       else {
/* 1379 */         if (value == null)
/* 1380 */           value = newInstance(type, PLACEHOLDER_MEMORY);
/* 1381 */         alignment = ((Structure)value).getStructAlignment();
/*      */       }
/*      */     }
/* 1384 */     else if (type.isArray()) {
/* 1385 */       alignment = getNativeAlignment(type.getComponentType(), null, isFirstElement);
/*      */     }
/*      */     else {
/* 1388 */       throw new IllegalArgumentException("Type " + type + " has unknown " + "native alignment");
/*      */     }
/*      */     
/* 1391 */     if (this.actualAlignType == 1) {
/* 1392 */       alignment = 1;
/*      */     }
/* 1394 */     else if (this.actualAlignType == 3) {
/* 1395 */       alignment = Math.min(8, alignment);
/*      */     }
/* 1397 */     else if (this.actualAlignType == 2)
/*      */     {
/*      */ 
/* 1400 */       if ((!isFirstElement) || (!Platform.isMac()) || (!Platform.isPPC())) {
/* 1401 */         alignment = Math.min(Native.MAX_ALIGNMENT, alignment);
/*      */       }
/* 1403 */       if ((!isFirstElement) && (Platform.isAIX()) && ((type == Double.TYPE) || (type == Double.class))) {
/* 1404 */         alignment = 4;
/*      */       }
/*      */     }
/* 1407 */     return alignment;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1416 */     return toString(Boolean.getBoolean("jna.dump_memory"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString(boolean debug)
/*      */   {
/* 1425 */     return toString(0, true, debug);
/*      */   }
/*      */   
/*      */   private String format(Class type) {
/* 1429 */     String s = type.getName();
/* 1430 */     int dot = s.lastIndexOf(".");
/* 1431 */     return s.substring(dot + 1);
/*      */   }
/*      */   
/*      */   private String toString(int indent, boolean showContents, boolean dumpMemory) {
/* 1435 */     ensureAllocated();
/* 1436 */     String LS = System.getProperty("line.separator");
/* 1437 */     String name = format(getClass()) + "(" + getPointer() + ")";
/* 1438 */     if (!(getPointer() instanceof Memory)) {
/* 1439 */       name = name + " (" + size() + " bytes)";
/*      */     }
/* 1441 */     String prefix = "";
/* 1442 */     for (int idx = 0; idx < indent; idx++) {
/* 1443 */       prefix = prefix + "  ";
/*      */     }
/* 1445 */     String contents = LS;
/* 1446 */     Iterator i; if (!showContents) {
/* 1447 */       contents = "...}";
/*      */     } else
/* 1449 */       for (i = fields().values().iterator(); i.hasNext();) {
/* 1450 */         StructField sf = (StructField)i.next();
/* 1451 */         Object value = getFieldValue(sf.field);
/* 1452 */         String type = format(sf.type);
/* 1453 */         String index = "";
/* 1454 */         contents = contents + prefix;
/* 1455 */         if ((sf.type.isArray()) && (value != null)) {
/* 1456 */           type = format(sf.type.getComponentType());
/* 1457 */           index = "[" + Array.getLength(value) + "]";
/*      */         }
/*      */         
/* 1460 */         contents = contents + "  " + type + " " + sf.name + index + "@" + Integer.toHexString(sf.offset);
/* 1461 */         if ((value instanceof Structure)) {
/* 1462 */           value = ((Structure)value).toString(indent + 1, !(value instanceof ByReference), dumpMemory);
/*      */         }
/* 1464 */         contents = contents + "=";
/* 1465 */         if ((value instanceof Long)) {
/* 1466 */           contents = contents + Long.toHexString(((Long)value).longValue());
/*      */         }
/* 1468 */         else if ((value instanceof Integer)) {
/* 1469 */           contents = contents + Integer.toHexString(((Integer)value).intValue());
/*      */         }
/* 1471 */         else if ((value instanceof Short)) {
/* 1472 */           contents = contents + Integer.toHexString(((Short)value).shortValue());
/*      */         }
/* 1474 */         else if ((value instanceof Byte)) {
/* 1475 */           contents = contents + Integer.toHexString(((Byte)value).byteValue());
/*      */         }
/*      */         else {
/* 1478 */           contents = contents + String.valueOf(value).trim();
/*      */         }
/* 1480 */         contents = contents + LS;
/* 1481 */         if (!i.hasNext())
/* 1482 */           contents = contents + prefix + "}";
/*      */       }
/* 1484 */     if ((indent == 0) && (dumpMemory)) {
/* 1485 */       int BYTES_PER_ROW = 4;
/* 1486 */       contents = contents + LS + "memory dump" + LS;
/* 1487 */       byte[] buf = getPointer().getByteArray(0L, size());
/* 1488 */       for (int i = 0; i < buf.length; i++) {
/* 1489 */         if (i % 4 == 0) contents = contents + "[";
/* 1490 */         if ((buf[i] >= 0) && (buf[i] < 16))
/* 1491 */           contents = contents + "0";
/* 1492 */         contents = contents + Integer.toHexString(buf[i] & 0xFF);
/* 1493 */         if ((i % 4 == 3) && (i < buf.length - 1))
/* 1494 */           contents = contents + "]" + LS;
/*      */       }
/* 1496 */       contents = contents + "]";
/*      */     }
/* 1498 */     return name + " {" + contents;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Structure[] toArray(Structure[] array)
/*      */   {
/* 1510 */     ensureAllocated();
/* 1511 */     if ((this.memory instanceof AutoAllocated))
/*      */     {
/* 1513 */       Memory m = (Memory)this.memory;
/* 1514 */       int requiredSize = array.length * size();
/* 1515 */       if (m.size() < requiredSize) {
/* 1516 */         useMemory(autoAllocate(requiredSize));
/*      */       }
/*      */     }
/*      */     
/* 1520 */     array[0] = this;
/* 1521 */     int size = size();
/* 1522 */     for (int i = 1; i < array.length; i++) {
/* 1523 */       array[i] = newInstance(getClass(), this.memory.share(i * size, size));
/* 1524 */       array[i].conditionalAutoRead();
/*      */     }
/*      */     
/* 1527 */     if (!(this instanceof ByValue))
/*      */     {
/* 1529 */       this.array = array;
/*      */     }
/*      */     
/* 1532 */     return array;
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
/*      */   public Structure[] toArray(int size)
/*      */   {
/* 1545 */     return toArray((Structure[])Array.newInstance(getClass(), size));
/*      */   }
/*      */   
/*      */   private Class baseClass() {
/* 1549 */     if (((this instanceof ByReference)) || ((this instanceof ByValue)))
/*      */     {
/* 1551 */       if (Structure.class.isAssignableFrom(getClass().getSuperclass()))
/* 1552 */         return getClass().getSuperclass();
/*      */     }
/* 1554 */     return getClass();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean dataEquals(Structure s)
/*      */   {
/* 1563 */     return dataEquals(s, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean dataEquals(Structure s, boolean clear)
/*      */   {
/* 1573 */     if (clear) {
/* 1574 */       s.getPointer().clear(s.size());
/* 1575 */       s.write();
/* 1576 */       getPointer().clear(size());
/* 1577 */       write();
/*      */     }
/* 1579 */     byte[] data = s.getPointer().getByteArray(0L, s.size());
/* 1580 */     byte[] ref = getPointer().getByteArray(0L, size());
/* 1581 */     if (data.length == ref.length) {
/* 1582 */       for (int i = 0; i < data.length; i++) {
/* 1583 */         if (data[i] != ref[i]) {
/* 1584 */           return false;
/*      */         }
/*      */       }
/* 1587 */       return true;
/*      */     }
/* 1589 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object o)
/*      */   {
/* 1598 */     return ((o instanceof Structure)) && (o.getClass() == getClass()) && (((Structure)o).getPointer().equals(getPointer()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1605 */     Pointer p = getPointer();
/* 1606 */     if (p != null) {
/* 1607 */       return getPointer().hashCode();
/*      */     }
/* 1609 */     return getClass().hashCode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void cacheTypeInfo(Pointer p)
/*      */   {
/* 1616 */     this.typeInfo = p.peer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   Pointer getFieldTypeInfo(StructField f)
/*      */   {
/* 1624 */     Class type = f.type;
/* 1625 */     Object value = getFieldValue(f.field);
/* 1626 */     if (this.typeMapper != null) {
/* 1627 */       ToNativeConverter nc = this.typeMapper.getToNativeConverter(type);
/* 1628 */       if (nc != null) {
/* 1629 */         type = nc.nativeType();
/* 1630 */         value = nc.toNative(value, new ToNativeContext());
/*      */       }
/*      */     }
/* 1633 */     return FFIType.get(value, type);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   Pointer getTypeInfo()
/*      */   {
/* 1640 */     Pointer p = getTypeInfo(this);
/* 1641 */     cacheTypeInfo(p);
/* 1642 */     return p;
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
/*      */   public void setAutoSynch(boolean auto)
/*      */   {
/* 1666 */     setAutoRead(auto);
/* 1667 */     setAutoWrite(auto);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoRead(boolean auto)
/*      */   {
/* 1675 */     this.autoRead = auto;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAutoRead()
/*      */   {
/* 1683 */     return this.autoRead;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoWrite(boolean auto)
/*      */   {
/* 1691 */     this.autoWrite = auto;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAutoWrite()
/*      */   {
/* 1699 */     return this.autoWrite;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static Pointer getTypeInfo(Object obj)
/*      */   {
/* 1707 */     return FFIType.get(obj);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static Structure newInstance(Class type, long init)
/*      */   {
/*      */     try
/*      */     {
/* 1716 */       Structure s = newInstance(type, init == 0L ? PLACEHOLDER_MEMORY : new Pointer(init));
/* 1717 */       if (init != 0L) {
/* 1718 */         s.conditionalAutoRead();
/*      */       }
/* 1720 */       return s;
/*      */     }
/*      */     catch (Throwable e) {
/* 1723 */       System.err.println("JNA: Error creating structure: " + e); }
/* 1724 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Structure newInstance(Class type, Pointer init)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     try
/*      */     {
/* 1737 */       Constructor ctor = type.getConstructor(new Class[] { Pointer.class });
/* 1738 */       return (Structure)ctor.newInstance(new Object[] { init });
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (NoSuchMethodException e) {}catch (SecurityException e) {}catch (InstantiationException e)
/*      */     {
/*      */ 
/*      */ 
/* 1747 */       String msg = "Can't instantiate " + type;
/* 1748 */       throw new IllegalArgumentException(msg, e);
/*      */     }
/*      */     catch (IllegalAccessException e) {
/* 1751 */       String msg = "Instantiation of " + type + " (Pointer) not allowed, is it public?";
/* 1752 */       throw new IllegalArgumentException(msg, e);
/*      */     }
/*      */     catch (InvocationTargetException e) {
/* 1755 */       String msg = "Exception thrown while instantiating an instance of " + type;
/* 1756 */       e.printStackTrace();
/* 1757 */       throw new IllegalArgumentException(msg, e);
/*      */     }
/* 1759 */     Structure s = newInstance(type);
/* 1760 */     if (init != PLACEHOLDER_MEMORY) {
/* 1761 */       s.useMemory(init);
/*      */     }
/* 1763 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static Structure newInstance(Class type)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     try
/*      */     {
/* 1773 */       Structure s = (Structure)type.newInstance();
/* 1774 */       if ((s instanceof ByValue)) {
/* 1775 */         s.allocateMemory();
/*      */       }
/* 1777 */       return s;
/*      */     }
/*      */     catch (InstantiationException e) {
/* 1780 */       String msg = "Can't instantiate " + type;
/* 1781 */       throw new IllegalArgumentException(msg, e);
/*      */     }
/*      */     catch (IllegalAccessException e) {
/* 1784 */       String msg = "Instantiation of " + type + " not allowed, is it public?";
/*      */       
/* 1786 */       throw new IllegalArgumentException(msg, e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   StructField typeInfoField()
/*      */   {
/*      */     LayoutInfo info;
/*      */     
/* 1796 */     synchronized (layoutInfo) {
/* 1797 */       info = (LayoutInfo)layoutInfo.get(getClass());
/*      */     }
/* 1799 */     if (info != null) {
/* 1800 */       return info.typeInfoField;
/*      */     }
/* 1802 */     return null;
/*      */   }
/*      */   
/*      */   protected static class StructField {
/*      */     public String name;
/*      */     public Class type;
/*      */     public Field field;
/* 1809 */     public int size = -1;
/* 1810 */     public int offset = -1;
/*      */     public boolean isVolatile;
/*      */     public boolean isReadOnly;
/*      */     public FromNativeConverter readConverter;
/*      */     public ToNativeConverter writeConverter;
/*      */     public FromNativeContext context;
/*      */     
/* 1817 */     public String toString() { return this.name + "@" + this.offset + "[" + this.size + "] (" + this.type + ")"; } }
/*      */   
/*      */   static class FFIType extends Structure { private static class FFITypes { private static Pointer ffi_type_void;
/*      */       private static Pointer ffi_type_float;
/*      */       private static Pointer ffi_type_double;
/*      */       private static Pointer ffi_type_longdouble;
/*      */       private static Pointer ffi_type_uint8;
/*      */       private static Pointer ffi_type_sint8;
/*      */       private static Pointer ffi_type_uint16; private static Pointer ffi_type_sint16; private static Pointer ffi_type_uint32; private static Pointer ffi_type_sint32; private static Pointer ffi_type_uint64; private static Pointer ffi_type_sint64; private static Pointer ffi_type_pointer; }
/* 1826 */     public static class size_t extends IntegerType { public size_t() { this(0L); }
/* 1827 */       public size_t(long value) { super(value); } }
/*      */     
/* 1829 */     private static Map typeInfoMap = new WeakHashMap();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private static final int FFI_TYPE_STRUCT = 13;
/*      */     
/*      */ 
/*      */ 
/*      */     public size_t size;
/*      */     
/*      */ 
/*      */ 
/*      */     public short alignment;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     static
/*      */     {
/* 1849 */       if (Native.POINTER_SIZE == 0)
/* 1850 */         throw new Error("Native library not initialized");
/* 1851 */       if (FFITypes.ffi_type_void == null)
/* 1852 */         throw new Error("FFI types not initialized");
/* 1853 */       typeInfoMap.put(Void.TYPE, FFITypes.ffi_type_void);
/* 1854 */       typeInfoMap.put(Void.class, FFITypes.ffi_type_void);
/* 1855 */       typeInfoMap.put(Float.TYPE, FFITypes.ffi_type_float);
/* 1856 */       typeInfoMap.put(Float.class, FFITypes.ffi_type_float);
/* 1857 */       typeInfoMap.put(Double.TYPE, FFITypes.ffi_type_double);
/* 1858 */       typeInfoMap.put(Double.class, FFITypes.ffi_type_double);
/* 1859 */       typeInfoMap.put(Long.TYPE, FFITypes.ffi_type_sint64);
/* 1860 */       typeInfoMap.put(Long.class, FFITypes.ffi_type_sint64);
/* 1861 */       typeInfoMap.put(Integer.TYPE, FFITypes.ffi_type_sint32);
/* 1862 */       typeInfoMap.put(Integer.class, FFITypes.ffi_type_sint32);
/* 1863 */       typeInfoMap.put(Short.TYPE, FFITypes.ffi_type_sint16);
/* 1864 */       typeInfoMap.put(Short.class, FFITypes.ffi_type_sint16);
/*      */       
/* 1866 */       Pointer ctype = Native.WCHAR_SIZE == 2 ? FFITypes.ffi_type_uint16 : FFITypes.ffi_type_uint32;
/* 1867 */       typeInfoMap.put(Character.TYPE, ctype);
/* 1868 */       typeInfoMap.put(Character.class, ctype);
/* 1869 */       typeInfoMap.put(Byte.TYPE, FFITypes.ffi_type_sint8);
/* 1870 */       typeInfoMap.put(Byte.class, FFITypes.ffi_type_sint8);
/* 1871 */       typeInfoMap.put(Pointer.class, FFITypes.ffi_type_pointer);
/* 1872 */       typeInfoMap.put(String.class, FFITypes.ffi_type_pointer);
/* 1873 */       typeInfoMap.put(WString.class, FFITypes.ffi_type_pointer);
/* 1874 */       typeInfoMap.put(Boolean.TYPE, FFITypes.ffi_type_uint32);
/* 1875 */       typeInfoMap.put(Boolean.class, FFITypes.ffi_type_uint32);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1882 */     public short type = 13;
/*      */     public Pointer elements;
/*      */     
/*      */     private FFIType(Structure ref)
/*      */     {
/* 1887 */       ref.ensureAllocated(true);
/*      */       Pointer[] els;
/* 1889 */       Pointer[] els; int idx; Iterator i; if ((ref instanceof Union)) {
/* 1890 */         Structure.StructField sf = ((Union)ref).typeInfoField();
/*      */         
/* 1892 */         els = new Pointer[] {get(ref.getFieldValue(sf.field), sf.type), null };
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1897 */         els = new Pointer[ref.fields().size() + 1];
/* 1898 */         idx = 0;
/* 1899 */         for (i = ref.fields().values().iterator(); i.hasNext();) {
/* 1900 */           Structure.StructField sf = (Structure.StructField)i.next();
/* 1901 */           els[(idx++)] = ref.getFieldTypeInfo(sf);
/*      */         }
/*      */       }
/* 1904 */       init(els);
/*      */     }
/*      */     
/*      */     private FFIType(Object array, Class type) {
/* 1908 */       int length = Array.getLength(array);
/* 1909 */       Pointer[] els = new Pointer[length + 1];
/* 1910 */       Pointer p = get(null, type.getComponentType());
/* 1911 */       for (int i = 0; i < length; i++) {
/* 1912 */         els[i] = p;
/*      */       }
/* 1914 */       init(els);
/*      */     }
/*      */     
/* 1917 */     protected List getFieldOrder() { return Arrays.asList(new String[] { "size", "alignment", "type", "elements" }); }
/*      */     
/*      */     private void init(Pointer[] els) {
/* 1920 */       this.elements = new Memory(Pointer.SIZE * els.length);
/* 1921 */       this.elements.write(0L, els, 0, els.length);
/* 1922 */       write();
/*      */     }
/*      */     
/*      */     static Pointer get(Object obj)
/*      */     {
/* 1927 */       if (obj == null)
/* 1928 */         return FFITypes.ffi_type_pointer;
/* 1929 */       if ((obj instanceof Class))
/* 1930 */         return get(null, (Class)obj);
/* 1931 */       return get(obj, obj.getClass());
/*      */     }
/*      */     
/*      */     private static Pointer get(Object obj, Class cls) {
/* 1935 */       TypeMapper mapper = Native.getTypeMapper(cls);
/* 1936 */       if (mapper != null) {
/* 1937 */         ToNativeConverter nc = mapper.getToNativeConverter(cls);
/* 1938 */         if (nc != null) {
/* 1939 */           cls = nc.nativeType();
/*      */         }
/*      */       }
/* 1942 */       synchronized (typeInfoMap) {
/* 1943 */         Object o = typeInfoMap.get(cls);
/* 1944 */         if ((o instanceof Pointer)) {
/* 1945 */           return (Pointer)o;
/*      */         }
/* 1947 */         if ((o instanceof FFIType)) {
/* 1948 */           return ((FFIType)o).getPointer();
/*      */         }
/* 1950 */         if (((Platform.HAS_BUFFERS) && (Buffer.class.isAssignableFrom(cls))) || 
/* 1951 */           (Callback.class.isAssignableFrom(cls))) {
/* 1952 */           typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
/* 1953 */           return FFITypes.ffi_type_pointer;
/*      */         }
/* 1955 */         if (Structure.class.isAssignableFrom(cls)) {
/* 1956 */           if (obj == null) obj = newInstance(cls, Structure.PLACEHOLDER_MEMORY);
/* 1957 */           if (Structure.ByReference.class.isAssignableFrom(cls)) {
/* 1958 */             typeInfoMap.put(cls, FFITypes.ffi_type_pointer);
/* 1959 */             return FFITypes.ffi_type_pointer;
/*      */           }
/* 1961 */           FFIType type = new FFIType((Structure)obj);
/* 1962 */           typeInfoMap.put(cls, type);
/* 1963 */           return type.getPointer();
/*      */         }
/* 1965 */         if (NativeMapped.class.isAssignableFrom(cls)) {
/* 1966 */           NativeMappedConverter c = NativeMappedConverter.getInstance(cls);
/* 1967 */           return get(c.toNative(obj, new ToNativeContext()), c.nativeType());
/*      */         }
/* 1969 */         if (cls.isArray()) {
/* 1970 */           FFIType type = new FFIType(obj, cls);
/*      */           
/* 1972 */           typeInfoMap.put(obj, type);
/* 1973 */           return type.getPointer();
/*      */         }
/* 1975 */         throw new IllegalArgumentException("Unsupported type " + cls);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static class AutoAllocated extends Memory {
/*      */     public AutoAllocated(int size) {
/* 1982 */       super();
/*      */       
/* 1984 */       super.clear();
/*      */     }
/*      */     
/* 1987 */     public String toString() { return "auto-" + super.toString(); }
/*      */   }
/*      */   
/*      */   private static void structureArrayCheck(Structure[] ss)
/*      */   {
/* 1992 */     if (ByReference[].class.isAssignableFrom(ss.getClass())) {
/* 1993 */       return;
/*      */     }
/* 1995 */     Pointer base = ss[0].getPointer();
/* 1996 */     int size = ss[0].size();
/* 1997 */     for (int si = 1; si < ss.length; si++) {
/* 1998 */       if (ss[si].getPointer().peer != base.peer + size * si) {
/* 1999 */         String msg = "Structure array elements must use contiguous memory (bad backing address at Structure array index " + si + ")";
/*      */         
/* 2001 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void autoRead(Structure[] ss) {
/* 2007 */     structureArrayCheck(ss);
/* 2008 */     if (ss[0].array == ss) {
/* 2009 */       ss[0].autoRead();
/*      */     }
/*      */     else {
/* 2012 */       for (int si = 0; si < ss.length; si++) {
/* 2013 */         if (ss[si] != null) {
/* 2014 */           ss[si].autoRead();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void autoRead() {
/* 2021 */     if (getAutoRead()) {
/* 2022 */       read();
/* 2023 */       if (this.array != null) {
/* 2024 */         for (int i = 1; i < this.array.length; i++) {
/* 2025 */           this.array[i].autoRead();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void autoWrite(Structure[] ss) {
/* 2032 */     structureArrayCheck(ss);
/* 2033 */     if (ss[0].array == ss) {
/* 2034 */       ss[0].autoWrite();
/*      */     }
/*      */     else {
/* 2037 */       for (int si = 0; si < ss.length; si++) {
/* 2038 */         if (ss[si] != null) {
/* 2039 */           ss[si].autoWrite();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void autoWrite() {
/* 2046 */     if (getAutoWrite()) {
/* 2047 */       write();
/* 2048 */       if (this.array != null) {
/* 2049 */         for (int i = 1; i < this.array.length; i++) {
/* 2050 */           this.array[i].autoWrite();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getNativeSize(Class nativeType)
/*      */   {
/* 2062 */     return getNativeSize(nativeType, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getNativeSize(Class nativeType, Object value)
/*      */   {
/* 2072 */     return Native.getNativeSize(nativeType, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 2078 */   private static final Pointer PLACEHOLDER_MEMORY = new Pointer(0L) {
/* 2079 */     public Pointer share(long offset, long sz) { return this; }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */   static void validate(Class cls)
/*      */   {
/* 2086 */     newInstance(cls, PLACEHOLDER_MEMORY);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\Structure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */