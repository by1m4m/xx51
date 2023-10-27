/*     */ package com.sun.jna;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class IntegerType
/*     */   extends Number
/*     */   implements NativeMapped
/*     */ {
/*     */   private int size;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Number number;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean unsigned;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long value;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IntegerType(int size)
/*     */   {
/*  38 */     this(size, 0L, false);
/*     */   }
/*     */   
/*     */   public IntegerType(int size, boolean unsigned)
/*     */   {
/*  43 */     this(size, 0L, unsigned);
/*     */   }
/*     */   
/*     */   public IntegerType(int size, long value)
/*     */   {
/*  48 */     this(size, value, false);
/*     */   }
/*     */   
/*     */   public IntegerType(int size, long value, boolean unsigned)
/*     */   {
/*  53 */     this.size = size;
/*  54 */     this.unsigned = unsigned;
/*  55 */     setValue(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setValue(long value)
/*     */   {
/*  62 */     long truncated = value;
/*  63 */     this.value = value;
/*  64 */     switch (this.size) {
/*     */     case 1: 
/*  66 */       if (this.unsigned) this.value = (value & 0xFF);
/*  67 */       truncated = (byte)(int)value;
/*  68 */       this.number = new Byte((byte)(int)value);
/*  69 */       break;
/*     */     case 2: 
/*  71 */       if (this.unsigned) this.value = (value & 0xFFFF);
/*  72 */       truncated = (short)(int)value;
/*  73 */       this.number = new Short((short)(int)value);
/*  74 */       break;
/*     */     case 4: 
/*  76 */       if (this.unsigned) this.value = (value & 0xFFFFFFFF);
/*  77 */       truncated = (int)value;
/*  78 */       this.number = new Integer((int)value);
/*  79 */       break;
/*     */     case 8: 
/*  81 */       this.number = new Long(value);
/*  82 */       break;
/*     */     case 3: case 5: case 6: case 7: default: 
/*  84 */       throw new IllegalArgumentException("Unsupported size: " + this.size);
/*     */     }
/*  86 */     if (this.size < 8) {
/*  87 */       long mask = (1L << this.size * 8) - 1L ^ 0xFFFFFFFFFFFFFFFF;
/*  88 */       if (((value < 0L) && (truncated != value)) || ((value >= 0L) && ((mask & value) != 0L)))
/*     */       {
/*     */ 
/*     */ 
/*  92 */         throw new IllegalArgumentException("Argument value 0x" + Long.toHexString(value) + " exceeds native capacity (" + this.size + " bytes) mask=0x" + Long.toHexString(mask));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object toNative()
/*     */   {
/*  99 */     return this.number;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object fromNative(Object nativeValue, FromNativeContext context)
/*     */   {
/* 106 */     long value = nativeValue == null ? 0L : ((Number)nativeValue).longValue();
/*     */     try {
/* 108 */       IntegerType number = (IntegerType)getClass().newInstance();
/* 109 */       number.setValue(value);
/* 110 */       return number;
/*     */     }
/*     */     catch (InstantiationException e)
/*     */     {
/* 114 */       throw new IllegalArgumentException("Can't instantiate " + getClass());
/*     */     }
/*     */     catch (IllegalAccessException e)
/*     */     {
/* 118 */       throw new IllegalArgumentException("Not allowed to instantiate " + getClass());
/*     */     }
/*     */   }
/*     */   
/*     */   public Class nativeType()
/*     */   {
/* 124 */     return this.number.getClass();
/*     */   }
/*     */   
/*     */   public int intValue()
/*     */   {
/* 129 */     return (int)this.value;
/*     */   }
/*     */   
/*     */   public long longValue()
/*     */   {
/* 134 */     return this.value;
/*     */   }
/*     */   
/*     */   public float floatValue()
/*     */   {
/* 139 */     return this.number.floatValue();
/*     */   }
/*     */   
/*     */   public double doubleValue()
/*     */   {
/* 144 */     return this.number.doubleValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object rhs)
/*     */   {
/* 150 */     return ((rhs instanceof IntegerType)) && (this.number.equals(((IntegerType)rhs).number));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 155 */     return this.number.toString();
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 160 */     return this.number.hashCode();
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
/*     */   public static <T extends IntegerType> int compare(T v1, T v2)
/*     */   {
/* 178 */     if (v1 == v2)
/* 179 */       return 0;
/* 180 */     if (v1 == null)
/* 181 */       return 1;
/* 182 */     if (v2 == null) {
/* 183 */       return -1;
/*     */     }
/* 185 */     return compare(v1.longValue(), v2.longValue());
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
/*     */   public static int compare(IntegerType v1, long v2)
/*     */   {
/* 201 */     if (v1 == null) {
/* 202 */       return 1;
/*     */     }
/* 204 */     return compare(v1.longValue(), v2);
/*     */   }
/*     */   
/*     */ 
/*     */   public static final int compare(long v1, long v2)
/*     */   {
/* 210 */     if (v1 == v2)
/* 211 */       return 0;
/* 212 */     if (v1 < v2) {
/* 213 */       return -1;
/*     */     }
/* 215 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\IntegerType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */