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
/*     */ public class PercentEscaper
/*     */   extends UnicodeEscaper
/*     */ {
/*     */   public static final String SAFECHARS_URLENCODER = "-_.*";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String SAFEPATHCHARS_URLENCODER = "-_.!~*'()@:$&,;=";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String SAFE_PLUS_RESERVED_CHARS_URLENCODER = "-_.!~*'()@:$&,;=+/?";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String SAFEUSERINFOCHARS_URLENCODER = "-_.!~*'():$&,;=";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String SAFEQUERYSTRINGCHARS_URLENCODER = "-_.!~*'()@:$,;/?:";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */   private static final char[] URI_ESCAPED_SPACE = { '+' };
/*     */   
/*  99 */   private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean plusForSpace;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean[] safeOctets;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PercentEscaper(String safeChars, boolean plusForSpace)
/*     */   {
/* 124 */     if (safeChars.matches(".*[0-9A-Za-z].*")) {
/* 125 */       throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 130 */     if ((plusForSpace) && (safeChars.contains(" "))) {
/* 131 */       throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
/*     */     }
/*     */     
/* 134 */     if (safeChars.contains("%")) {
/* 135 */       throw new IllegalArgumentException("The '%' character cannot be specified as 'safe'");
/*     */     }
/* 137 */     this.plusForSpace = plusForSpace;
/* 138 */     this.safeOctets = createSafeOctets(safeChars);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean[] createSafeOctets(String safeChars)
/*     */   {
/* 147 */     int maxChar = 122;
/* 148 */     char[] safeCharArray = safeChars.toCharArray();
/* 149 */     for (char c : safeCharArray) {
/* 150 */       maxChar = Math.max(c, maxChar);
/*     */     }
/* 152 */     boolean[] octets = new boolean[maxChar + 1];
/* 153 */     for (int c = 48; c <= 57; c++) {
/* 154 */       octets[c] = true;
/*     */     }
/* 156 */     for (int c = 65; c <= 90; c++) {
/* 157 */       octets[c] = true;
/*     */     }
/* 159 */     for (int c = 97; c <= 122; c++) {
/* 160 */       octets[c] = true;
/*     */     }
/* 162 */     for (char c : safeCharArray) {
/* 163 */       octets[c] = true;
/*     */     }
/* 165 */     return octets;
/*     */   }
/*     */   
/*     */   protected int nextEscapeIndex(CharSequence csq, int index, int end)
/*     */   {
/* 174 */     for (; 
/*     */         
/*     */ 
/*     */ 
/* 174 */         index < end; index++) {
/* 175 */       char c = csq.charAt(index);
/* 176 */       if ((c >= this.safeOctets.length) || (this.safeOctets[c] == 0)) {
/*     */         break;
/*     */       }
/*     */     }
/* 180 */     return index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String escape(String s)
/*     */   {
/* 189 */     int slen = s.length();
/* 190 */     for (int index = 0; index < slen; index++) {
/* 191 */       char c = s.charAt(index);
/* 192 */       if ((c >= this.safeOctets.length) || (this.safeOctets[c] == 0)) {
/* 193 */         return escapeSlow(s, index);
/*     */       }
/*     */     }
/* 196 */     return s;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected char[] escape(int cp)
/*     */   {
/* 206 */     if ((cp < this.safeOctets.length) && (this.safeOctets[cp] != 0))
/* 207 */       return null;
/* 208 */     if ((cp == 32) && (this.plusForSpace))
/* 209 */       return URI_ESCAPED_SPACE;
/* 210 */     if (cp <= 127)
/*     */     {
/*     */ 
/* 213 */       char[] dest = new char[3];
/* 214 */       dest[0] = '%';
/* 215 */       dest[2] = UPPER_HEX_DIGITS[(cp & 0xF)];
/* 216 */       dest[1] = UPPER_HEX_DIGITS[(cp >>> 4)];
/* 217 */       return dest; }
/* 218 */     if (cp <= 2047)
/*     */     {
/*     */ 
/* 221 */       char[] dest = new char[6];
/* 222 */       dest[0] = '%';
/* 223 */       dest[3] = '%';
/* 224 */       dest[5] = UPPER_HEX_DIGITS[(cp & 0xF)];
/* 225 */       cp >>>= 4;
/* 226 */       dest[4] = UPPER_HEX_DIGITS[(0x8 | cp & 0x3)];
/* 227 */       cp >>>= 2;
/* 228 */       dest[2] = UPPER_HEX_DIGITS[(cp & 0xF)];
/* 229 */       cp >>>= 4;
/* 230 */       dest[1] = UPPER_HEX_DIGITS[(0xC | cp)];
/* 231 */       return dest; }
/* 232 */     if (cp <= 65535)
/*     */     {
/*     */ 
/* 235 */       char[] dest = new char[9];
/* 236 */       dest[0] = '%';
/* 237 */       dest[1] = 'E';
/* 238 */       dest[3] = '%';
/* 239 */       dest[6] = '%';
/* 240 */       dest[8] = UPPER_HEX_DIGITS[(cp & 0xF)];
/* 241 */       cp >>>= 4;
/* 242 */       dest[7] = UPPER_HEX_DIGITS[(0x8 | cp & 0x3)];
/* 243 */       cp >>>= 2;
/* 244 */       dest[5] = UPPER_HEX_DIGITS[(cp & 0xF)];
/* 245 */       cp >>>= 4;
/* 246 */       dest[4] = UPPER_HEX_DIGITS[(0x8 | cp & 0x3)];
/* 247 */       cp >>>= 2;
/* 248 */       dest[2] = UPPER_HEX_DIGITS[cp];
/* 249 */       return dest; }
/* 250 */     if (cp <= 1114111) {
/* 251 */       dest = new char[12];
/*     */       
/*     */ 
/* 254 */       dest[0] = '%';
/* 255 */       dest[1] = 'F';
/* 256 */       dest[3] = '%';
/* 257 */       dest[6] = '%';
/* 258 */       dest[9] = '%';
/* 259 */       dest[11] = UPPER_HEX_DIGITS[(cp & 0xF)];
/* 260 */       cp >>>= 4;
/* 261 */       dest[10] = UPPER_HEX_DIGITS[(0x8 | cp & 0x3)];
/* 262 */       cp >>>= 2;
/* 263 */       dest[8] = UPPER_HEX_DIGITS[(cp & 0xF)];
/* 264 */       cp >>>= 4;
/* 265 */       dest[7] = UPPER_HEX_DIGITS[(0x8 | cp & 0x3)];
/* 266 */       cp >>>= 2;
/* 267 */       dest[5] = UPPER_HEX_DIGITS[(cp & 0xF)];
/* 268 */       cp >>>= 4;
/* 269 */       dest[4] = UPPER_HEX_DIGITS[(0x8 | cp & 0x3)];
/* 270 */       cp >>>= 2;
/* 271 */       dest[2] = UPPER_HEX_DIGITS[(cp & 0x7)];
/* 272 */       return dest;
/*     */     }
/*     */     
/* 275 */     char[] dest = cp;throw new IllegalArgumentException(43 + "Invalid unicode character value " + dest);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\escape\PercentEscaper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */