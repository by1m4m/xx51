/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InetAddressSet
/*     */   extends AbstractSet<String>
/*     */   implements Set<String>, Predicate<InetAddress>
/*     */ {
/*  52 */   private Map<String, InetPattern> _patterns = new HashMap();
/*     */   
/*     */ 
/*     */   public boolean add(String pattern)
/*     */   {
/*  57 */     return this._patterns.put(pattern, newInetRange(pattern)) == null;
/*     */   }
/*     */   
/*     */   protected InetPattern newInetRange(String pattern)
/*     */   {
/*  62 */     if (pattern == null) {
/*  63 */       return null;
/*     */     }
/*  65 */     int slash = pattern.lastIndexOf('/');
/*  66 */     int dash = pattern.lastIndexOf('-');
/*     */     try
/*     */     {
/*  69 */       if (slash >= 0) {
/*  70 */         return new CidrInetRange(pattern, InetAddress.getByName(pattern.substring(0, slash).trim()), StringUtil.toInt(pattern, slash + 1));
/*     */       }
/*  72 */       if (dash >= 0) {
/*  73 */         return new MinMaxInetRange(pattern, InetAddress.getByName(pattern.substring(0, dash).trim()), InetAddress.getByName(pattern.substring(dash + 1).trim()));
/*     */       }
/*  75 */       return new SingletonInetRange(pattern, InetAddress.getByName(pattern));
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */       try
/*     */       {
/*  81 */         if ((slash < 0) && (dash > 0)) {
/*  82 */           return new LegacyInetRange(pattern);
/*     */         }
/*     */       }
/*     */       catch (Exception e2) {
/*  86 */         e.addSuppressed(e2);
/*     */       }
/*  88 */       throw new IllegalArgumentException("Bad pattern: " + pattern, e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean remove(Object pattern)
/*     */   {
/*  95 */     return this._patterns.remove(pattern) != null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<String> iterator()
/*     */   {
/* 101 */     return this._patterns.keySet().iterator();
/*     */   }
/*     */   
/*     */ 
/*     */   public int size()
/*     */   {
/* 107 */     return this._patterns.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean test(InetAddress address)
/*     */   {
/* 114 */     if (address == null)
/* 115 */       return false;
/* 116 */     byte[] raw = address.getAddress();
/* 117 */     for (InetPattern pattern : this._patterns.values())
/* 118 */       if (pattern.test(address, raw))
/* 119 */         return true;
/* 120 */     return false;
/*     */   }
/*     */   
/*     */   static abstract class InetPattern
/*     */   {
/*     */     final String _pattern;
/*     */     
/*     */     InetPattern(String pattern)
/*     */     {
/* 129 */       this._pattern = pattern;
/*     */     }
/*     */     
/*     */ 
/*     */     abstract boolean test(InetAddress paramInetAddress, byte[] paramArrayOfByte);
/*     */     
/*     */     public String toString()
/*     */     {
/* 137 */       return this._pattern;
/*     */     }
/*     */   }
/*     */   
/*     */   static class SingletonInetRange extends InetAddressSet.InetPattern
/*     */   {
/*     */     final InetAddress _address;
/*     */     
/*     */     public SingletonInetRange(String pattern, InetAddress address) {
/* 146 */       super();
/* 147 */       this._address = address;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean test(InetAddress address, byte[] raw)
/*     */     {
/* 153 */       return this._address.equals(address);
/*     */     }
/*     */   }
/*     */   
/*     */   static class MinMaxInetRange
/*     */     extends InetAddressSet.InetPattern
/*     */   {
/*     */     final int[] _min;
/*     */     final int[] _max;
/*     */     
/*     */     public MinMaxInetRange(String pattern, InetAddress min, InetAddress max)
/*     */     {
/* 165 */       super();
/*     */       
/* 167 */       byte[] raw_min = min.getAddress();
/* 168 */       byte[] raw_max = max.getAddress();
/* 169 */       if (raw_min.length != raw_max.length) {
/* 170 */         throw new IllegalArgumentException("Cannot mix IPv4 and IPv6: " + pattern);
/*     */       }
/* 172 */       if (raw_min.length == 4)
/*     */       {
/*     */ 
/* 175 */         int count = 0;
/* 176 */         for (char c : pattern.toCharArray())
/* 177 */           if (c == '.')
/* 178 */             count++;
/* 179 */         if (count != 6) {
/* 180 */           throw new IllegalArgumentException("Legacy pattern: " + pattern);
/*     */         }
/*     */       }
/* 183 */       this._min = new int[raw_min.length];
/* 184 */       this._max = new int[raw_min.length];
/*     */       
/* 186 */       for (int i = 0; i < this._min.length; i++)
/*     */       {
/* 188 */         this._min[i] = (0xFF & raw_min[i]);
/* 189 */         this._max[i] = (0xFF & raw_max[i]);
/*     */       }
/*     */       
/* 192 */       for (int i = 0; i < this._min.length; i++)
/*     */       {
/* 194 */         if (this._min[i] > this._max[i])
/* 195 */           throw new IllegalArgumentException("min is greater than max: " + pattern);
/* 196 */         if (this._min[i] < this._max[i]) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean test(InetAddress item, byte[] raw)
/*     */     {
/* 204 */       if (raw.length != this._min.length) {
/* 205 */         return false;
/*     */       }
/* 207 */       boolean min_ok = false;
/* 208 */       boolean max_ok = false;
/*     */       
/* 210 */       for (int i = 0; i < this._min.length; i++)
/*     */       {
/* 212 */         int r = 0xFF & raw[i];
/* 213 */         if (!min_ok)
/*     */         {
/* 215 */           if (r < this._min[i])
/* 216 */             return false;
/* 217 */           if (r > this._min[i])
/* 218 */             min_ok = true;
/*     */         }
/* 220 */         if (!max_ok)
/*     */         {
/* 222 */           if (r > this._max[i])
/* 223 */             return false;
/* 224 */           if (r < this._max[i]) {
/* 225 */             max_ok = true;
/*     */           }
/*     */         }
/* 228 */         if ((min_ok) && (max_ok)) {
/*     */           break;
/*     */         }
/*     */       }
/* 232 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static class CidrInetRange
/*     */     extends InetAddressSet.InetPattern
/*     */   {
/*     */     final byte[] _raw;
/*     */     final int _octets;
/*     */     final int _mask;
/*     */     final int _masked;
/*     */     
/*     */     public CidrInetRange(String pattern, InetAddress address, int cidr)
/*     */     {
/* 246 */       super();
/* 247 */       this._raw = address.getAddress();
/* 248 */       this._octets = (cidr / 8);
/* 249 */       this._mask = (0xFF & 255 << 8 - cidr % 8);
/* 250 */       this._masked = (this._mask == 0 ? 0 : this._raw[this._octets] & this._mask);
/*     */       
/* 252 */       if (cidr > this._raw.length * 8) {
/* 253 */         throw new IllegalArgumentException("CIDR too large: " + pattern);
/*     */       }
/* 255 */       if ((this._mask != 0) && ((0xFF & this._raw[this._octets]) != this._masked)) {
/* 256 */         throw new IllegalArgumentException("CIDR bits non zero: " + pattern);
/*     */       }
/* 258 */       for (int o = this._octets + (this._mask == 0 ? 0 : 1); o < this._raw.length; o++) {
/* 259 */         if (this._raw[o] != 0) {
/* 260 */           throw new IllegalArgumentException("CIDR bits non zero: " + pattern);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean test(InetAddress item, byte[] raw) {
/* 266 */       if (raw.length != this._raw.length) {
/* 267 */         return false;
/*     */       }
/* 269 */       for (int o = 0; o < this._octets; o++) {
/* 270 */         if (this._raw[o] != raw[o])
/* 271 */           return false;
/*     */       }
/* 273 */       if ((this._mask != 0) && ((raw[this._octets] & this._mask) != this._masked))
/* 274 */         return false;
/* 275 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static class LegacyInetRange extends InetAddressSet.InetPattern
/*     */   {
/* 281 */     int[] _min = new int[4];
/* 282 */     int[] _max = new int[4];
/*     */     
/*     */     public LegacyInetRange(String pattern)
/*     */     {
/* 286 */       super();
/*     */       
/* 288 */       String[] parts = pattern.split("\\.");
/* 289 */       if (parts.length != 4) {
/* 290 */         throw new IllegalArgumentException("Bad legacy pattern: " + pattern);
/*     */       }
/* 292 */       for (int i = 0; i < 4; i++)
/*     */       {
/* 294 */         String part = parts[i].trim();
/* 295 */         int dash = part.indexOf('-');
/* 296 */         if (dash < 0) {
/* 297 */           this._min[i] = (this._max[i] = Integer.parseInt(part));
/*     */         }
/*     */         else {
/* 300 */           this._min[i] = (dash == 0 ? 0 : StringUtil.toInt(part, 0));
/* 301 */           this._max[i] = (dash == part.length() - 1 ? 'ÿ' : StringUtil.toInt(part, dash + 1));
/*     */         }
/*     */         
/* 304 */         if ((this._min[i] < 0) || (this._min[i] > this._max[i]) || (this._max[i] > 255)) {
/* 305 */           throw new IllegalArgumentException("Bad legacy pattern: " + pattern);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean test(InetAddress item, byte[] raw)
/*     */     {
/* 312 */       if (raw.length != 4) {
/* 313 */         return false;
/*     */       }
/* 315 */       for (int i = 0; i < 4; i++) {
/* 316 */         if (((0xFF & raw[i]) < this._min[i]) || ((0xFF & raw[i]) > this._max[i]))
/* 317 */           return false;
/*     */       }
/* 319 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\InetAddressSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */