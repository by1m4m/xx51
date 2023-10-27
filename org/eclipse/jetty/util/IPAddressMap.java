/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ /**
/*     */  * @deprecated
/*     */  */
/*     */ public class IPAddressMap<TYPE>
/*     */   extends HashMap<String, TYPE>
/*     */ {
/*  49 */   private final HashMap<String, IPAddrPattern> _patterns = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IPAddressMap()
/*     */   {
/*  56 */     super(11);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IPAddressMap(int capacity)
/*     */   {
/*  66 */     super(capacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TYPE put(String addrSpec, TYPE object)
/*     */     throws IllegalArgumentException
/*     */   {
/*  79 */     if ((addrSpec == null) || (addrSpec.trim().length() == 0)) {
/*  80 */       throw new IllegalArgumentException("Invalid IP address pattern: " + addrSpec);
/*     */     }
/*  82 */     String spec = addrSpec.trim();
/*  83 */     if (this._patterns.get(spec) == null) {
/*  84 */       this._patterns.put(spec, new IPAddrPattern(spec));
/*     */     }
/*  86 */     return (TYPE)super.put(spec, object);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TYPE get(Object key)
/*     */   {
/*  98 */     return (TYPE)super.get(key);
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
/*     */   public TYPE match(String addr)
/*     */   {
/* 111 */     Map.Entry<String, TYPE> entry = getMatch(addr);
/* 112 */     return entry == null ? null : entry.getValue();
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
/*     */   public Map.Entry<String, TYPE> getMatch(String addr)
/*     */   {
/* 125 */     if (addr != null)
/*     */     {
/* 127 */       for (Map.Entry<String, TYPE> entry : super.entrySet())
/*     */       {
/* 129 */         if (((IPAddrPattern)this._patterns.get(entry.getKey())).match(addr))
/*     */         {
/* 131 */           return entry;
/*     */         }
/*     */       }
/*     */     }
/* 135 */     return null;
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
/*     */   public Object getLazyMatches(String addr)
/*     */   {
/* 148 */     if (addr == null) {
/* 149 */       return LazyList.getList(super.entrySet());
/*     */     }
/* 151 */     Object entries = null;
/* 152 */     for (Map.Entry<String, TYPE> entry : super.entrySet())
/*     */     {
/* 154 */       if (((IPAddrPattern)this._patterns.get(entry.getKey())).match(addr))
/*     */       {
/* 156 */         entries = LazyList.add(entries, entry);
/*     */       }
/*     */     }
/* 159 */     return entries;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class IPAddrPattern
/*     */   {
/* 171 */     private final IPAddressMap.OctetPattern[] _octets = new IPAddressMap.OctetPattern[4];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public IPAddrPattern(String value)
/*     */       throws IllegalArgumentException
/*     */     {
/* 182 */       if ((value == null) || (value.trim().length() == 0)) {
/* 183 */         throw new IllegalArgumentException("Invalid IP address pattern: " + value);
/*     */       }
/*     */       try
/*     */       {
/* 187 */         StringTokenizer parts = new StringTokenizer(value, ".");
/*     */         
/*     */ 
/* 190 */         for (int idx = 0; idx < 4; idx++)
/*     */         {
/* 192 */           String part = parts.hasMoreTokens() ? parts.nextToken().trim() : "0-255";
/*     */           
/* 194 */           int len = part.length();
/* 195 */           if ((len == 0) && (parts.hasMoreTokens())) {
/* 196 */             throw new IllegalArgumentException("Invalid IP address pattern: " + value);
/*     */           }
/* 198 */           this._octets[idx] = new IPAddressMap.OctetPattern(len == 0 ? "0-255" : part);
/*     */         }
/*     */       }
/*     */       catch (IllegalArgumentException ex)
/*     */       {
/* 203 */         throw new IllegalArgumentException("Invalid IP address pattern: " + value, ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean match(String value)
/*     */       throws IllegalArgumentException
/*     */     {
/* 219 */       if ((value == null) || (value.trim().length() == 0)) {
/* 220 */         throw new IllegalArgumentException("Invalid IP address: " + value);
/*     */       }
/*     */       try
/*     */       {
/* 224 */         StringTokenizer parts = new StringTokenizer(value, ".");
/*     */         
/* 226 */         boolean result = true;
/* 227 */         for (int idx = 0; idx < 4; idx++)
/*     */         {
/* 229 */           if (!parts.hasMoreTokens()) {
/* 230 */             throw new IllegalArgumentException("Invalid IP address: " + value);
/*     */           }
/* 232 */           if (!(result &= this._octets[idx].match(parts.nextToken())))
/*     */             break;
/*     */         }
/* 235 */         return result;
/*     */       }
/*     */       catch (IllegalArgumentException ex)
/*     */       {
/* 239 */         throw new IllegalArgumentException("Invalid IP address: " + value, ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class OctetPattern
/*     */     extends BitSet
/*     */   {
/* 253 */     private final BitSet _mask = new BitSet(256);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public OctetPattern(String octetSpec)
/*     */       throws IllegalArgumentException
/*     */     {
/*     */       try
/*     */       {
/* 267 */         if (octetSpec != null)
/*     */         {
/* 269 */           String spec = octetSpec.trim();
/* 270 */           if (spec.length() == 0)
/*     */           {
/* 272 */             this._mask.set(0, 255);
/*     */           }
/*     */           else
/*     */           {
/* 276 */             StringTokenizer parts = new StringTokenizer(spec, ",");
/* 277 */             while (parts.hasMoreTokens())
/*     */             {
/* 279 */               String part = parts.nextToken().trim();
/* 280 */               if (part.length() > 0)
/*     */               {
/* 282 */                 if (part.indexOf('-') < 0)
/*     */                 {
/* 284 */                   Integer value = Integer.valueOf(part);
/* 285 */                   this._mask.set(value.intValue());
/*     */                 }
/*     */                 else
/*     */                 {
/* 289 */                   int low = 0;int high = 255;
/*     */                   
/* 291 */                   String[] bounds = part.split("-", -2);
/* 292 */                   if (bounds.length != 2)
/*     */                   {
/* 294 */                     throw new IllegalArgumentException("Invalid octet spec: " + octetSpec);
/*     */                   }
/*     */                   
/* 297 */                   if (bounds[0].length() > 0)
/*     */                   {
/* 299 */                     low = Integer.parseInt(bounds[0]);
/*     */                   }
/* 301 */                   if (bounds[1].length() > 0)
/*     */                   {
/* 303 */                     high = Integer.parseInt(bounds[1]);
/*     */                   }
/*     */                   
/* 306 */                   if (low > high)
/*     */                   {
/* 308 */                     throw new IllegalArgumentException("Invalid octet spec: " + octetSpec);
/*     */                   }
/*     */                   
/* 311 */                   this._mask.set(low, high + 1);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (NumberFormatException ex)
/*     */       {
/* 320 */         throw new IllegalArgumentException("Invalid octet spec: " + octetSpec, ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean match(String value)
/*     */       throws IllegalArgumentException
/*     */     {
/* 335 */       if ((value == null) || (value.trim().length() == 0)) {
/* 336 */         throw new IllegalArgumentException("Invalid octet: " + value);
/*     */       }
/*     */       try
/*     */       {
/* 340 */         int number = Integer.parseInt(value);
/* 341 */         return match(number);
/*     */       }
/*     */       catch (NumberFormatException ex)
/*     */       {
/* 345 */         throw new IllegalArgumentException("Invalid octet: " + value);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean match(int number)
/*     */       throws IllegalArgumentException
/*     */     {
/* 360 */       if ((number < 0) || (number > 255)) {
/* 361 */         throw new IllegalArgumentException("Invalid octet: " + number);
/*     */       }
/* 363 */       return this._mask.get(number);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\IPAddressMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */