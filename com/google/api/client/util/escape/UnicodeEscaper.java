/*     */ package com.google.api.client.util.escape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class UnicodeEscaper
/*     */   extends Escaper
/*     */ {
/*     */   private static final int DEST_PAD = 32;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract char[] escape(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract int nextEscapeIndex(CharSequence paramCharSequence, int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String escape(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String escapeSlow(String s, int index)
/*     */   {
/* 132 */     int end = s.length();
/*     */     
/*     */ 
/* 135 */     char[] dest = Platform.charBufferFromThreadLocal();
/* 136 */     int destIndex = 0;
/* 137 */     int unescapedChunkStart = 0;
/*     */     
/* 139 */     while (index < end) {
/* 140 */       int cp = codePointAt(s, index, end);
/* 141 */       if (cp < 0) {
/* 142 */         throw new IllegalArgumentException("Trailing high surrogate at end of input");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 147 */       char[] escaped = escape(cp);
/* 148 */       int nextIndex = index + (Character.isSupplementaryCodePoint(cp) ? 2 : 1);
/* 149 */       if (escaped != null) {
/* 150 */         int charsSkipped = index - unescapedChunkStart;
/*     */         
/*     */ 
/*     */ 
/* 154 */         int sizeNeeded = destIndex + charsSkipped + escaped.length;
/* 155 */         if (dest.length < sizeNeeded) {
/* 156 */           int destLength = sizeNeeded + end - index + 32;
/* 157 */           dest = growBuffer(dest, destIndex, destLength);
/*     */         }
/*     */         
/* 160 */         if (charsSkipped > 0) {
/* 161 */           s.getChars(unescapedChunkStart, index, dest, destIndex);
/* 162 */           destIndex += charsSkipped;
/*     */         }
/* 164 */         if (escaped.length > 0) {
/* 165 */           System.arraycopy(escaped, 0, dest, destIndex, escaped.length);
/* 166 */           destIndex += escaped.length;
/*     */         }
/*     */         
/* 169 */         unescapedChunkStart = nextIndex;
/*     */       }
/* 171 */       index = nextEscapeIndex(s, nextIndex, end);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 176 */     int charsSkipped = end - unescapedChunkStart;
/* 177 */     if (charsSkipped > 0) {
/* 178 */       int endIndex = destIndex + charsSkipped;
/* 179 */       if (dest.length < endIndex) {
/* 180 */         dest = growBuffer(dest, destIndex, endIndex);
/*     */       }
/* 182 */       s.getChars(unescapedChunkStart, end, dest, destIndex);
/* 183 */       destIndex = endIndex;
/*     */     }
/* 185 */     return new String(dest, 0, destIndex);
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
/*     */   protected static int codePointAt(CharSequence seq, int index, int end)
/*     */   {
/* 221 */     if (index < end) {
/* 222 */       char c1 = seq.charAt(index++);
/* 223 */       if ((c1 < 55296) || (c1 > 57343))
/*     */       {
/* 225 */         return c1; }
/* 226 */       if (c1 <= 56319)
/*     */       {
/* 228 */         if (index == end) {
/* 229 */           return -c1;
/*     */         }
/*     */         
/* 232 */         c2 = seq.charAt(index);
/* 233 */         if (Character.isLowSurrogate(c2)) {
/* 234 */           return Character.toCodePoint(c1, c2);
/*     */         }
/* 236 */         c1 = c2;i = c2;int j = index;throw new IllegalArgumentException(83 + "Expected low surrogate but got char '" + c1 + "' with value " + i + " at index " + j);
/*     */       }
/*     */       
/*     */ 
/* 240 */       char c2 = c1;char c1 = c1;int i = index - 1;throw new IllegalArgumentException(82 + "Unexpected low surrogate character '" + c2 + "' with value " + c1 + " at index " + i);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 245 */     throw new IndexOutOfBoundsException("Index exceeds specified range");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static char[] growBuffer(char[] dest, int index, int size)
/*     */   {
/* 253 */     char[] copy = new char[size];
/* 254 */     if (index > 0) {
/* 255 */       System.arraycopy(dest, 0, copy, 0, index);
/*     */     }
/* 257 */     return copy;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\escape\UnicodeEscaper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */