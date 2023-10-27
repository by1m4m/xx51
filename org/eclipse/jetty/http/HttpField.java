/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.eclipse.jetty.util.StringUtil;
/*     */ import org.eclipse.jetty.util.Trie;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpField
/*     */ {
/*     */   private static final String __zeroquality = "q=0";
/*     */   private final HttpHeader _header;
/*     */   private final String _name;
/*     */   private final String _value;
/*  34 */   private int hash = 0;
/*     */   
/*     */   public HttpField(HttpHeader header, String name, String value)
/*     */   {
/*  38 */     this._header = header;
/*  39 */     this._name = name;
/*  40 */     this._value = value;
/*     */   }
/*     */   
/*     */   public HttpField(HttpHeader header, String value)
/*     */   {
/*  45 */     this(header, header.asString(), value);
/*     */   }
/*     */   
/*     */   public HttpField(HttpHeader header, HttpHeaderValue value)
/*     */   {
/*  50 */     this(header, header.asString(), value.asString());
/*     */   }
/*     */   
/*     */   public HttpField(String name, String value)
/*     */   {
/*  55 */     this((HttpHeader)HttpHeader.CACHE.get(name), name, value);
/*     */   }
/*     */   
/*     */   public HttpHeader getHeader()
/*     */   {
/*  60 */     return this._header;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  65 */     return this._name;
/*     */   }
/*     */   
/*     */   public String getValue()
/*     */   {
/*  70 */     return this._value;
/*     */   }
/*     */   
/*     */   public int getIntValue()
/*     */   {
/*  75 */     return Integer.valueOf(this._value).intValue();
/*     */   }
/*     */   
/*     */   public long getLongValue()
/*     */   {
/*  80 */     return Long.valueOf(this._value).longValue();
/*     */   }
/*     */   
/*     */   public String[] getValues()
/*     */   {
/*  85 */     if (this._value == null) {
/*  86 */       return null;
/*     */     }
/*  88 */     QuotedCSV list = new QuotedCSV(false, new String[] { this._value });
/*  89 */     return (String[])list.getValues().toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(String search)
/*     */   {
/* 101 */     if (search == null)
/* 102 */       return this._value == null;
/* 103 */     if (search.length() == 0)
/* 104 */       return false;
/* 105 */     if (this._value == null)
/* 106 */       return false;
/* 107 */     if (search.equals(this._value)) {
/* 108 */       return true;
/*     */     }
/* 110 */     search = StringUtil.asciiToLowerCase(search);
/*     */     
/* 112 */     int state = 0;
/* 113 */     int match = 0;
/* 114 */     int param = 0;
/*     */     
/* 116 */     for (int i = 0; i < this._value.length(); i++)
/*     */     {
/* 118 */       char c = this._value.charAt(i);
/* 119 */       switch (state)
/*     */       {
/*     */       case 0: 
/* 122 */         switch (c)
/*     */         {
/*     */         case '"': 
/* 125 */           match = 0;
/* 126 */           state = 2;
/* 127 */           break;
/*     */         
/*     */         case ',': 
/*     */           break;
/*     */         
/*     */         case ';': 
/* 133 */           param = -1;
/* 134 */           match = -1;
/* 135 */           state = 5;
/* 136 */           break;
/*     */         
/*     */         case '\t': 
/*     */         case ' ': 
/*     */           break;
/*     */         
/*     */         default: 
/* 143 */           match = Character.toLowerCase(c) == search.charAt(0) ? 1 : -1;
/* 144 */           state = 1; }
/* 145 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case 1: 
/* 150 */         switch (c)
/*     */         {
/*     */ 
/*     */         case ',': 
/* 154 */           if (match == search.length())
/* 155 */             return true;
/* 156 */           state = 0;
/* 157 */           break;
/*     */         
/*     */         case ';': 
/* 160 */           param = match >= 0 ? 0 : -1;
/* 161 */           state = 5;
/* 162 */           break;
/*     */         
/*     */         default: 
/* 165 */           if (match > 0)
/*     */           {
/* 167 */             if (match < search.length()) {
/* 168 */               match = Character.toLowerCase(c) == search.charAt(match) ? match + 1 : -1;
/* 169 */             } else if ((c != ' ') && (c != '\t')) {
/* 170 */               match = -1;
/*     */             }
/*     */           }
/*     */           break;
/*     */         }
/*     */         
/*     */         break;
/*     */       case 2: 
/* 178 */         switch (c)
/*     */         {
/*     */         case '\\': 
/* 181 */           state = 3;
/* 182 */           break;
/*     */         
/*     */         case '"': 
/* 185 */           state = 4;
/* 186 */           break;
/*     */         
/*     */         default: 
/* 189 */           if (match >= 0)
/*     */           {
/* 191 */             if (match < search.length()) {
/* 192 */               match = Character.toLowerCase(c) == search.charAt(match) ? match + 1 : -1;
/*     */             } else
/* 194 */               match = -1;
/*     */           }
/*     */           break;
/*     */         }
/*     */         break;
/*     */       case 3: 
/* 200 */         if (match >= 0)
/*     */         {
/* 202 */           if (match < search.length()) {
/* 203 */             match = Character.toLowerCase(c) == search.charAt(match) ? match + 1 : -1;
/*     */           } else
/* 205 */             match = -1;
/*     */         }
/* 207 */         state = 2;
/* 208 */         break;
/*     */       
/*     */       case 4: 
/* 211 */         switch (c)
/*     */         {
/*     */         case '\t': 
/*     */         case ' ': 
/*     */           break;
/*     */         
/*     */         case ';': 
/* 218 */           state = 5;
/* 219 */           break;
/*     */         
/*     */ 
/*     */         case ',': 
/* 223 */           if (match == search.length())
/* 224 */             return true;
/* 225 */           state = 0;
/* 226 */           break;
/*     */         
/*     */ 
/*     */         default: 
/* 230 */           match = -1;
/*     */         }
/* 232 */         break;
/*     */       
/*     */       case 5: 
/* 235 */         switch (c)
/*     */         {
/*     */ 
/*     */         case ',': 
/* 239 */           if ((param != "q=0".length()) && (match == search.length()))
/* 240 */             return true;
/* 241 */           param = 0;
/* 242 */           state = 0;
/* 243 */           break;
/*     */         
/*     */         case '\t': 
/*     */         case ' ': 
/*     */           break;
/*     */         
/*     */         default: 
/* 250 */           if (param >= 0)
/*     */           {
/* 252 */             if (param < "q=0".length()) {
/* 253 */               param = Character.toLowerCase(c) == "q=0".charAt(param) ? param + 1 : -1;
/* 254 */             } else if ((c != '0') && (c != '.'))
/* 255 */               param = -1;
/*     */           }
/*     */           break;
/*     */         }
/*     */         
/*     */         break;
/*     */       default: 
/* 262 */         throw new IllegalStateException();
/*     */       }
/*     */       
/*     */     }
/* 266 */     return (param != "q=0".length()) && (match == search.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 273 */     String v = getValue();
/* 274 */     return getName() + ": " + (v == null ? "" : v);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSameName(HttpField field)
/*     */   {
/* 280 */     boolean sameObject = field == this;
/*     */     
/* 282 */     if (field == null)
/* 283 */       return false;
/* 284 */     if (sameObject)
/* 285 */       return true;
/* 286 */     if ((this._header != null) && (this._header == field.getHeader()))
/* 287 */       return true;
/* 288 */     if (this._name.equalsIgnoreCase(field.getName()))
/* 289 */       return true;
/* 290 */     return false;
/*     */   }
/*     */   
/*     */   private int nameHashCode()
/*     */   {
/* 295 */     int h = this.hash;
/* 296 */     int len = this._name.length();
/* 297 */     if ((h == 0) && (len > 0))
/*     */     {
/* 299 */       for (int i = 0; i < len; i++)
/*     */       {
/*     */ 
/* 302 */         char c = this._name.charAt(i);
/*     */         
/* 304 */         if ((c >= 'a') && (c <= 'z'))
/* 305 */           c = (char)(c - ' ');
/* 306 */         h = 31 * h + c;
/*     */       }
/* 308 */       this.hash = h;
/*     */     }
/* 310 */     return h;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 316 */     int vhc = Objects.hashCode(this._value);
/* 317 */     if (this._header == null)
/* 318 */       return vhc ^ nameHashCode();
/* 319 */     return vhc ^ this._header.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 325 */     if (o == this)
/* 326 */       return true;
/* 327 */     if (!(o instanceof HttpField))
/* 328 */       return false;
/* 329 */     HttpField field = (HttpField)o;
/* 330 */     if (this._header != field.getHeader())
/* 331 */       return false;
/* 332 */     if (!this._name.equalsIgnoreCase(field.getName()))
/* 333 */       return false;
/* 334 */     if ((this._value == null) && (field.getValue() != null))
/* 335 */       return false;
/* 336 */     return Objects.equals(this._value, field.getValue());
/*     */   }
/*     */   
/*     */   public static class IntValueHttpField extends HttpField
/*     */   {
/*     */     private final int _int;
/*     */     
/*     */     public IntValueHttpField(HttpHeader header, String name, String value, int intValue)
/*     */     {
/* 345 */       super(name, value);
/* 346 */       this._int = intValue;
/*     */     }
/*     */     
/*     */     public IntValueHttpField(HttpHeader header, String name, String value)
/*     */     {
/* 351 */       this(header, name, value, Integer.valueOf(value).intValue());
/*     */     }
/*     */     
/*     */     public IntValueHttpField(HttpHeader header, String name, int intValue)
/*     */     {
/* 356 */       this(header, name, Integer.toString(intValue), intValue);
/*     */     }
/*     */     
/*     */     public IntValueHttpField(HttpHeader header, int value)
/*     */     {
/* 361 */       this(header, header.asString(), value);
/*     */     }
/*     */     
/*     */ 
/*     */     public int getIntValue()
/*     */     {
/* 367 */       return this._int;
/*     */     }
/*     */     
/*     */ 
/*     */     public long getLongValue()
/*     */     {
/* 373 */       return this._int;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LongValueHttpField extends HttpField
/*     */   {
/*     */     private final long _long;
/*     */     
/*     */     public LongValueHttpField(HttpHeader header, String name, String value, long longValue)
/*     */     {
/* 383 */       super(name, value);
/* 384 */       this._long = longValue;
/*     */     }
/*     */     
/*     */     public LongValueHttpField(HttpHeader header, String name, String value)
/*     */     {
/* 389 */       this(header, name, value, Long.valueOf(value).longValue());
/*     */     }
/*     */     
/*     */     public LongValueHttpField(HttpHeader header, String name, long value)
/*     */     {
/* 394 */       this(header, name, Long.toString(value), value);
/*     */     }
/*     */     
/*     */     public LongValueHttpField(HttpHeader header, long value)
/*     */     {
/* 399 */       this(header, header.asString(), value);
/*     */     }
/*     */     
/*     */ 
/*     */     public int getIntValue()
/*     */     {
/* 405 */       return (int)this._long;
/*     */     }
/*     */     
/*     */ 
/*     */     public long getLongValue()
/*     */     {
/* 411 */       return this._long;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */