/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QuotedCSV
/*     */   implements Iterable<String>
/*     */ {
/*     */   private static enum State
/*     */   {
/*  37 */     VALUE,  PARAM_NAME,  PARAM_VALUE;
/*     */     private State() {} }
/*  39 */   protected final List<String> _values = new ArrayList();
/*     */   
/*     */   protected final boolean _keepQuotes;
/*     */   
/*     */   public QuotedCSV(String... values)
/*     */   {
/*  45 */     this(true, values);
/*     */   }
/*     */   
/*     */ 
/*     */   public QuotedCSV(boolean keepQuotes, String... values)
/*     */   {
/*  51 */     this._keepQuotes = keepQuotes;
/*  52 */     for (String v : values) {
/*  53 */       addValue(v);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addValue(String value)
/*     */   {
/*  62 */     if (value == null) {
/*  63 */       return;
/*     */     }
/*  65 */     StringBuffer buffer = new StringBuffer();
/*     */     
/*  67 */     int l = value.length();
/*  68 */     State state = State.VALUE;
/*  69 */     boolean quoted = false;
/*  70 */     boolean sloshed = false;
/*  71 */     int nws_length = 0;
/*  72 */     int last_length = 0;
/*  73 */     int value_length = -1;
/*  74 */     int param_name = -1;
/*  75 */     int param_value = -1;
/*     */     
/*  77 */     for (int i = 0; i <= l; i++)
/*     */     {
/*  79 */       char c = i == l ? '\000' : value.charAt(i);
/*     */       
/*     */ 
/*  82 */       if ((quoted) && (c != 0))
/*     */       {
/*  84 */         if (sloshed) {
/*  85 */           sloshed = false;
/*     */         }
/*     */         else {
/*  88 */           switch (c)
/*     */           {
/*     */           case '\\': 
/*  91 */             sloshed = true;
/*  92 */             if (this._keepQuotes) break;
/*  93 */             break;
/*     */           
/*     */           case '"': 
/*  96 */             quoted = false;
/*  97 */             if (!this._keepQuotes) {
/*     */               continue;
/*     */             }
/*     */           }
/*     */           
/*     */         }
/* 103 */         buffer.append(c);
/* 104 */         nws_length = buffer.length();
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 109 */         switch (c)
/*     */         {
/*     */         case '\t': 
/*     */         case ' ': 
/* 113 */           if (buffer.length() > last_length) {
/* 114 */             buffer.append(c);
/*     */           }
/*     */           break;
/*     */         case '"': 
/* 118 */           quoted = true;
/* 119 */           if (this._keepQuotes)
/*     */           {
/* 121 */             if ((state == State.PARAM_VALUE) && (param_value < 0))
/* 122 */               param_value = nws_length;
/* 123 */             buffer.append(c);
/*     */           }
/* 125 */           else if ((state == State.PARAM_VALUE) && (param_value < 0)) {
/* 126 */             param_value = nws_length; }
/* 127 */           nws_length = buffer.length();
/* 128 */           break;
/*     */         
/*     */         case ';': 
/* 131 */           buffer.setLength(nws_length);
/* 132 */           if (state == State.VALUE)
/*     */           {
/* 134 */             parsedValue(buffer);
/* 135 */             value_length = buffer.length();
/*     */           }
/*     */           else {
/* 138 */             parsedParam(buffer, value_length, param_name, param_value); }
/* 139 */           nws_length = buffer.length();
/* 140 */           param_name = param_value = -1;
/* 141 */           buffer.append(c);
/* 142 */           nws_length++;last_length = nws_length;
/* 143 */           state = State.PARAM_NAME;
/* 144 */           break;
/*     */         
/*     */         case '\000': 
/*     */         case ',': 
/* 148 */           if (nws_length > 0)
/*     */           {
/* 150 */             buffer.setLength(nws_length);
/* 151 */             switch (state)
/*     */             {
/*     */             case VALUE: 
/* 154 */               parsedValue(buffer);
/* 155 */               value_length = buffer.length();
/* 156 */               break;
/*     */             case PARAM_NAME: 
/*     */             case PARAM_VALUE: 
/* 159 */               parsedParam(buffer, value_length, param_name, param_value);
/*     */             }
/*     */             
/* 162 */             this._values.add(buffer.toString());
/*     */           }
/* 164 */           buffer.setLength(0);
/* 165 */           last_length = 0;
/* 166 */           nws_length = 0;
/* 167 */           value_length = param_name = param_value = -1;
/* 168 */           state = State.VALUE;
/* 169 */           break;
/*     */         
/*     */         case '=': 
/* 172 */           switch (state)
/*     */           {
/*     */ 
/*     */           case VALUE: 
/* 176 */             value_length = param_name = 0;
/* 177 */             buffer.setLength(nws_length);
/* 178 */             String param = buffer.toString();
/* 179 */             buffer.setLength(0);
/* 180 */             parsedValue(buffer);
/* 181 */             value_length = buffer.length();
/* 182 */             buffer.append(param);
/* 183 */             buffer.append(c);
/* 184 */             nws_length++;last_length = nws_length;
/* 185 */             state = State.PARAM_VALUE;
/* 186 */             break;
/*     */           
/*     */           case PARAM_NAME: 
/* 189 */             buffer.setLength(nws_length);
/* 190 */             buffer.append(c);
/* 191 */             nws_length++;last_length = nws_length;
/* 192 */             state = State.PARAM_VALUE;
/* 193 */             break;
/*     */           
/*     */           case PARAM_VALUE: 
/* 196 */             if (param_value < 0)
/* 197 */               param_value = nws_length;
/* 198 */             buffer.append(c);
/* 199 */             nws_length = buffer.length();
/*     */           }
/*     */           
/* 202 */           break;
/*     */         
/*     */ 
/*     */         default: 
/* 206 */           switch (state)
/*     */           {
/*     */ 
/*     */           case VALUE: 
/* 210 */             buffer.append(c);
/* 211 */             nws_length = buffer.length();
/* 212 */             break;
/*     */           
/*     */ 
/*     */ 
/*     */           case PARAM_NAME: 
/* 217 */             if (param_name < 0)
/* 218 */               param_name = nws_length;
/* 219 */             buffer.append(c);
/* 220 */             nws_length = buffer.length();
/* 221 */             break;
/*     */           
/*     */ 
/*     */ 
/*     */           case PARAM_VALUE: 
/* 226 */             if (param_value < 0)
/* 227 */               param_value = nws_length;
/* 228 */             buffer.append(c);
/* 229 */             nws_length = buffer.length();
/*     */           }
/*     */           
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */           break;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void parsedValue(StringBuffer buffer) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void parsedParam(StringBuffer buffer, int valueLength, int paramName, int paramValue) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 259 */     return this._values.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 264 */     return this._values.isEmpty();
/*     */   }
/*     */   
/*     */   public List<String> getValues()
/*     */   {
/* 269 */     return this._values;
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<String> iterator()
/*     */   {
/* 275 */     return this._values.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */   public static String unquote(String s)
/*     */   {
/* 281 */     int l = s.length();
/* 282 */     if ((s == null) || (l == 0)) {
/* 283 */       return s;
/*     */     }
/*     */     
/* 286 */     for (int i = 0; 
/* 287 */         i < l; i++)
/*     */     {
/* 289 */       char c = s.charAt(i);
/* 290 */       if (c == '"')
/*     */         break;
/*     */     }
/* 293 */     if (i == l) {
/* 294 */       return s;
/*     */     }
/* 296 */     boolean quoted = true;
/* 297 */     boolean sloshed = false;
/* 298 */     StringBuffer buffer = new StringBuffer();
/* 299 */     buffer.append(s, 0, i);
/* 300 */     i++;
/* 301 */     for (; i < l; i++)
/*     */     {
/* 303 */       char c = s.charAt(i);
/* 304 */       if (quoted)
/*     */       {
/* 306 */         if (sloshed)
/*     */         {
/* 308 */           buffer.append(c);
/* 309 */           sloshed = false;
/*     */         }
/* 311 */         else if (c == '"') {
/* 312 */           quoted = false;
/* 313 */         } else if (c == '\\') {
/* 314 */           sloshed = true;
/*     */         } else {
/* 316 */           buffer.append(c);
/*     */         }
/* 318 */       } else if (c == '"') {
/* 319 */         quoted = true;
/*     */       } else
/* 321 */         buffer.append(c);
/*     */     }
/* 323 */     return buffer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 329 */     List<String> list = new ArrayList();
/* 330 */     for (String s : this)
/* 331 */       list.add(s);
/* 332 */     return list.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\QuotedCSV.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */