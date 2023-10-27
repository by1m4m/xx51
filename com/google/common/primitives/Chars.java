/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated=true)
/*     */ public final class Chars
/*     */ {
/*     */   public static final int BYTES = 2;
/*     */   
/*     */   public static int hashCode(char value)
/*     */   {
/*  69 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static char checkedCast(long value)
/*     */   {
/*  81 */     char result = (char)(int)value;
/*  82 */     Preconditions.checkArgument(result == value, "Out of range: %s", value);
/*  83 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static char saturatedCast(long value)
/*     */   {
/*  95 */     if (value > 65535L) {
/*  96 */       return 65535;
/*     */     }
/*  98 */     if (value < 0L) {
/*  99 */       return '\000';
/*     */     }
/* 101 */     return (char)(int)value;
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
/*     */   public static int compare(char a, char b)
/*     */   {
/* 117 */     return a - b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean contains(char[] array, char target)
/*     */   {
/* 128 */     for (char value : array) {
/* 129 */       if (value == target) {
/* 130 */         return true;
/*     */       }
/*     */     }
/* 133 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int indexOf(char[] array, char target)
/*     */   {
/* 145 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */   
/*     */   private static int indexOf(char[] array, char target, int start, int end)
/*     */   {
/* 150 */     for (int i = start; i < end; i++) {
/* 151 */       if (array[i] == target) {
/* 152 */         return i;
/*     */       }
/*     */     }
/* 155 */     return -1;
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
/*     */   public static int indexOf(char[] array, char[] target)
/*     */   {
/* 169 */     Preconditions.checkNotNull(array, "array");
/* 170 */     Preconditions.checkNotNull(target, "target");
/* 171 */     if (target.length == 0) {
/* 172 */       return 0;
/*     */     }
/*     */     
/*     */     label64:
/* 176 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 177 */       for (int j = 0; j < target.length; j++) {
/* 178 */         if (array[(i + j)] != target[j]) {
/*     */           break label64;
/*     */         }
/*     */       }
/* 182 */       return i;
/*     */     }
/* 184 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int lastIndexOf(char[] array, char target)
/*     */   {
/* 196 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */   
/*     */   private static int lastIndexOf(char[] array, char target, int start, int end)
/*     */   {
/* 201 */     for (int i = end - 1; i >= start; i--) {
/* 202 */       if (array[i] == target) {
/* 203 */         return i;
/*     */       }
/*     */     }
/* 206 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static char min(char... array)
/*     */   {
/* 218 */     Preconditions.checkArgument(array.length > 0);
/* 219 */     char min = array[0];
/* 220 */     for (int i = 1; i < array.length; i++) {
/* 221 */       if (array[i] < min) {
/* 222 */         min = array[i];
/*     */       }
/*     */     }
/* 225 */     return min;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static char max(char... array)
/*     */   {
/* 237 */     Preconditions.checkArgument(array.length > 0);
/* 238 */     char max = array[0];
/* 239 */     for (int i = 1; i < array.length; i++) {
/* 240 */       if (array[i] > max) {
/* 241 */         max = array[i];
/*     */       }
/*     */     }
/* 244 */     return max;
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
/*     */   @Beta
/*     */   public static char constrainToRange(char value, char min, char max)
/*     */   {
/* 262 */     Preconditions.checkArgument(min <= max, "min (%s) must be less than or equal to max (%s)", min, max);
/* 263 */     return value < max ? value : value < min ? min : max;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static char[] concat(char[]... arrays)
/*     */   {
/* 274 */     int length = 0;
/* 275 */     for (array : arrays) {
/* 276 */       length += array.length;
/*     */     }
/* 278 */     char[] result = new char[length];
/* 279 */     int pos = 0;
/* 280 */     char[][] arrayOfChar2 = arrays;char[] array = arrayOfChar2.length; for (char[] arrayOfChar3 = 0; arrayOfChar3 < array; arrayOfChar3++) { char[] array = arrayOfChar2[arrayOfChar3];
/* 281 */       System.arraycopy(array, 0, result, pos, array.length);
/* 282 */       pos += array.length;
/*     */     }
/* 284 */     return result;
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
/*     */   @GwtIncompatible
/*     */   public static byte[] toByteArray(char value)
/*     */   {
/* 298 */     return new byte[] { (byte)(value >> '\b'), (byte)value };
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
/*     */   @GwtIncompatible
/*     */   public static char fromByteArray(byte[] bytes)
/*     */   {
/* 313 */     Preconditions.checkArgument(bytes.length >= 2, "array too small: %s < %s", bytes.length, 2);
/* 314 */     return fromBytes(bytes[0], bytes[1]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public static char fromBytes(byte b1, byte b2)
/*     */   {
/* 325 */     return (char)(b1 << 8 | b2 & 0xFF);
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
/*     */   public static char[] ensureCapacity(char[] array, int minLength, int padding)
/*     */   {
/* 342 */     Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
/* 343 */     Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
/* 344 */     return array.length < minLength ? Arrays.copyOf(array, minLength + padding) : array;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String join(String separator, char... array)
/*     */   {
/* 356 */     Preconditions.checkNotNull(separator);
/* 357 */     int len = array.length;
/* 358 */     if (len == 0) {
/* 359 */       return "";
/*     */     }
/*     */     
/* 362 */     StringBuilder builder = new StringBuilder(len + separator.length() * (len - 1));
/* 363 */     builder.append(array[0]);
/* 364 */     for (int i = 1; i < len; i++) {
/* 365 */       builder.append(separator).append(array[i]);
/*     */     }
/* 367 */     return builder.toString();
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
/*     */   public static Comparator<char[]> lexicographicalComparator()
/*     */   {
/* 384 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private static enum LexicographicalComparator implements Comparator<char[]> {
/* 388 */     INSTANCE;
/*     */     
/*     */     private LexicographicalComparator() {}
/*     */     
/* 392 */     public int compare(char[] left, char[] right) { int minLength = Math.min(left.length, right.length);
/* 393 */       for (int i = 0; i < minLength; i++) {
/* 394 */         int result = Chars.compare(left[i], right[i]);
/* 395 */         if (result != 0) {
/* 396 */           return result;
/*     */         }
/*     */       }
/* 399 */       return left.length - right.length;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 404 */       return "Chars.lexicographicalComparator()";
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
/*     */ 
/*     */ 
/*     */   public static char[] toArray(Collection<Character> collection)
/*     */   {
/* 421 */     if ((collection instanceof CharArrayAsList)) {
/* 422 */       return ((CharArrayAsList)collection).toCharArray();
/*     */     }
/*     */     
/* 425 */     Object[] boxedArray = collection.toArray();
/* 426 */     int len = boxedArray.length;
/* 427 */     char[] array = new char[len];
/* 428 */     for (int i = 0; i < len; i++)
/*     */     {
/* 430 */       array[i] = ((Character)Preconditions.checkNotNull(boxedArray[i])).charValue();
/*     */     }
/* 432 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void sortDescending(char[] array)
/*     */   {
/* 441 */     Preconditions.checkNotNull(array);
/* 442 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void sortDescending(char[] array, int fromIndex, int toIndex)
/*     */   {
/* 452 */     Preconditions.checkNotNull(array);
/* 453 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 454 */     Arrays.sort(array, fromIndex, toIndex);
/* 455 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void reverse(char[] array)
/*     */   {
/* 465 */     Preconditions.checkNotNull(array);
/* 466 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(char[] array, int fromIndex, int toIndex)
/*     */   {
/* 480 */     Preconditions.checkNotNull(array);
/* 481 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 482 */     int i = fromIndex; for (int j = toIndex - 1; i < j; j--) {
/* 483 */       char tmp = array[i];
/* 484 */       array[i] = array[j];
/* 485 */       array[j] = tmp;i++;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<Character> asList(char... backingArray)
/*     */   {
/* 502 */     if (backingArray.length == 0) {
/* 503 */       return Collections.emptyList();
/*     */     }
/* 505 */     return new CharArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class CharArrayAsList extends AbstractList<Character> implements RandomAccess, Serializable {
/*     */     final char[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     CharArrayAsList(char[] array) {
/* 516 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     CharArrayAsList(char[] array, int start, int end) {
/* 520 */       this.array = array;
/* 521 */       this.start = start;
/* 522 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 527 */       return this.end - this.start;
/*     */     }
/*     */     
/*     */     public boolean isEmpty()
/*     */     {
/* 532 */       return false;
/*     */     }
/*     */     
/*     */     public Character get(int index)
/*     */     {
/* 537 */       Preconditions.checkElementIndex(index, size());
/* 538 */       return Character.valueOf(this.array[(this.start + index)]);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean contains(Object target)
/*     */     {
/* 544 */       return ((target instanceof Character)) && 
/* 545 */         (Chars.indexOf(this.array, ((Character)target).charValue(), this.start, this.end) != -1);
/*     */     }
/*     */     
/*     */ 
/*     */     public int indexOf(Object target)
/*     */     {
/* 551 */       if ((target instanceof Character)) {
/* 552 */         int i = Chars.indexOf(this.array, ((Character)target).charValue(), this.start, this.end);
/* 553 */         if (i >= 0) {
/* 554 */           return i - this.start;
/*     */         }
/*     */       }
/* 557 */       return -1;
/*     */     }
/*     */     
/*     */ 
/*     */     public int lastIndexOf(Object target)
/*     */     {
/* 563 */       if ((target instanceof Character)) {
/* 564 */         int i = Chars.lastIndexOf(this.array, ((Character)target).charValue(), this.start, this.end);
/* 565 */         if (i >= 0) {
/* 566 */           return i - this.start;
/*     */         }
/*     */       }
/* 569 */       return -1;
/*     */     }
/*     */     
/*     */     public Character set(int index, Character element)
/*     */     {
/* 574 */       Preconditions.checkElementIndex(index, size());
/* 575 */       char oldValue = this.array[(this.start + index)];
/*     */       
/* 577 */       this.array[(this.start + index)] = ((Character)Preconditions.checkNotNull(element)).charValue();
/* 578 */       return Character.valueOf(oldValue);
/*     */     }
/*     */     
/*     */     public List<Character> subList(int fromIndex, int toIndex)
/*     */     {
/* 583 */       int size = size();
/* 584 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 585 */       if (fromIndex == toIndex) {
/* 586 */         return Collections.emptyList();
/*     */       }
/* 588 */       return new CharArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */     
/*     */     public boolean equals(Object object)
/*     */     {
/* 593 */       if (object == this) {
/* 594 */         return true;
/*     */       }
/* 596 */       if ((object instanceof CharArrayAsList)) {
/* 597 */         CharArrayAsList that = (CharArrayAsList)object;
/* 598 */         int size = size();
/* 599 */         if (that.size() != size) {
/* 600 */           return false;
/*     */         }
/* 602 */         for (int i = 0; i < size; i++) {
/* 603 */           if (this.array[(this.start + i)] != that.array[(that.start + i)]) {
/* 604 */             return false;
/*     */           }
/*     */         }
/* 607 */         return true;
/*     */       }
/* 609 */       return super.equals(object);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 614 */       int result = 1;
/* 615 */       for (int i = this.start; i < this.end; i++) {
/* 616 */         result = 31 * result + Chars.hashCode(this.array[i]);
/*     */       }
/* 618 */       return result;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 623 */       StringBuilder builder = new StringBuilder(size() * 3);
/* 624 */       builder.append('[').append(this.array[this.start]);
/* 625 */       for (int i = this.start + 1; i < this.end; i++) {
/* 626 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 628 */       return ']';
/*     */     }
/*     */     
/*     */     char[] toCharArray() {
/* 632 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\primitives\Chars.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */