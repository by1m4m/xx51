/*     */ package com.google.api.client.repackaged.com.google.common.base;
/*     */ 
/*     */ import com.google.api.client.repackaged.com.google.common.annotations.Beta;
/*     */ import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible
/*     */ public final class Objects
/*     */ {
/*     */   public static boolean equal(@Nullable Object a, @Nullable Object b)
/*     */   {
/*  58 */     return (a == b) || ((a != null) && (a.equals(b)));
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
/*     */ 
/*     */   public static int hashCode(@Nullable Object... objects)
/*     */   {
/*  77 */     return Arrays.hashCode(objects);
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
/*     */   public static ToStringHelper toStringHelper(Object self)
/*     */   {
/* 121 */     return new ToStringHelper(simpleName(self.getClass()), null);
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
/*     */   public static ToStringHelper toStringHelper(Class<?> clazz)
/*     */   {
/* 135 */     return new ToStringHelper(simpleName(clazz), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ToStringHelper toStringHelper(String className)
/*     */   {
/* 147 */     return new ToStringHelper(className, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String simpleName(Class<?> clazz)
/*     */   {
/* 155 */     String name = clazz.getName();
/*     */     
/*     */ 
/*     */ 
/* 159 */     name = name.replaceAll("\\$[0-9]+", "\\$");
/*     */     
/*     */ 
/* 162 */     int start = name.lastIndexOf('$');
/*     */     
/*     */ 
/*     */ 
/* 166 */     if (start == -1) {
/* 167 */       start = name.lastIndexOf('.');
/*     */     }
/* 169 */     return name.substring(start + 1);
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
/*     */ 
/*     */ 
/*     */   public static <T> T firstNonNull(@Nullable T first, @Nullable T second)
/*     */   {
/* 189 */     return first != null ? first : Preconditions.checkNotNull(second);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class ToStringHelper
/*     */   {
/*     */     private final String className;
/*     */     
/*     */ 
/* 200 */     private final List<ValueHolder> valueHolders = new LinkedList();
/*     */     
/* 202 */     private boolean omitNullValues = false;
/*     */     
/*     */ 
/*     */ 
/*     */     private ToStringHelper(String className)
/*     */     {
/* 208 */       this.className = ((String)Preconditions.checkNotNull(className));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Beta
/*     */     public ToStringHelper omitNullValues()
/*     */     {
/* 219 */       this.omitNullValues = true;
/* 220 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper add(String name, @Nullable Object value)
/*     */     {
/* 230 */       Preconditions.checkNotNull(name);
/* 231 */       addHolder(value).builder.append(name).append('=').append(value);
/* 232 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper add(String name, boolean value)
/*     */     {
/* 242 */       checkNameAndAppend(name).append(value);
/* 243 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper add(String name, char value)
/*     */     {
/* 253 */       checkNameAndAppend(name).append(value);
/* 254 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper add(String name, double value)
/*     */     {
/* 264 */       checkNameAndAppend(name).append(value);
/* 265 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper add(String name, float value)
/*     */     {
/* 275 */       checkNameAndAppend(name).append(value);
/* 276 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper add(String name, int value)
/*     */     {
/* 286 */       checkNameAndAppend(name).append(value);
/* 287 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper add(String name, long value)
/*     */     {
/* 297 */       checkNameAndAppend(name).append(value);
/* 298 */       return this;
/*     */     }
/*     */     
/*     */     private StringBuilder checkNameAndAppend(String name) {
/* 302 */       Preconditions.checkNotNull(name);
/* 303 */       return addHolder().builder.append(name).append('=');
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper addValue(@Nullable Object value)
/*     */     {
/* 313 */       addHolder(value).builder.append(value);
/* 314 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper addValue(boolean value)
/*     */     {
/* 326 */       addHolder().builder.append(value);
/* 327 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper addValue(char value)
/*     */     {
/* 339 */       addHolder().builder.append(value);
/* 340 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper addValue(double value)
/*     */     {
/* 352 */       addHolder().builder.append(value);
/* 353 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper addValue(float value)
/*     */     {
/* 365 */       addHolder().builder.append(value);
/* 366 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper addValue(int value)
/*     */     {
/* 378 */       addHolder().builder.append(value);
/* 379 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ToStringHelper addValue(long value)
/*     */     {
/* 391 */       addHolder().builder.append(value);
/* 392 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 403 */       boolean omitNullValuesSnapshot = this.omitNullValues;
/* 404 */       boolean needsSeparator = false;
/* 405 */       StringBuilder builder = new StringBuilder(32).append(this.className).append('{');
/*     */       
/* 407 */       for (ValueHolder valueHolder : this.valueHolders) {
/* 408 */         if ((!omitNullValuesSnapshot) || (!valueHolder.isNull)) {
/* 409 */           if (needsSeparator) {
/* 410 */             builder.append(", ");
/*     */           } else {
/* 412 */             needsSeparator = true;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 418 */           CharSequence sequence = valueHolder.builder;
/* 419 */           builder.append(sequence);
/*     */         }
/*     */       }
/* 422 */       return '}';
/*     */     }
/*     */     
/*     */     private ValueHolder addHolder() {
/* 426 */       ValueHolder valueHolder = new ValueHolder(null);
/* 427 */       this.valueHolders.add(valueHolder);
/* 428 */       return valueHolder;
/*     */     }
/*     */     
/*     */     private ValueHolder addHolder(@Nullable Object value) {
/* 432 */       ValueHolder valueHolder = addHolder();
/* 433 */       valueHolder.isNull = (value == null);
/* 434 */       return valueHolder;
/*     */     }
/*     */     
/*     */     private static final class ValueHolder {
/* 438 */       final StringBuilder builder = new StringBuilder();
/*     */       boolean isNull;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\repackaged\com\google\common\base\Objects.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */