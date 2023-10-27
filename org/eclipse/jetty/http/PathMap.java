/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.function.Predicate;
/*     */ import org.eclipse.jetty.util.ArrayTernaryTrie;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class PathMap<O>
/*     */   extends HashMap<String, O>
/*     */ {
/*  82 */   private static String __pathSpecSeparators = ":,";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setPathSpecSeparators(String s)
/*     */   {
/*  93 */     __pathSpecSeparators = s;
/*     */   }
/*     */   
/*     */ 
/*  97 */   Trie<MappedEntry<O>> _prefixMap = new ArrayTernaryTrie(false);
/*  98 */   Trie<MappedEntry<O>> _suffixMap = new ArrayTernaryTrie(false);
/*  99 */   final Map<String, MappedEntry<O>> _exactMap = new HashMap();
/*     */   
/* 101 */   List<MappedEntry<O>> _defaultSingletonList = null;
/* 102 */   MappedEntry<O> _prefixDefault = null;
/* 103 */   MappedEntry<O> _default = null;
/* 104 */   boolean _nodefault = false;
/*     */   
/*     */ 
/*     */   public PathMap()
/*     */   {
/* 109 */     this(11);
/*     */   }
/*     */   
/*     */ 
/*     */   public PathMap(boolean noDefault)
/*     */   {
/* 115 */     this(11, noDefault);
/*     */   }
/*     */   
/*     */ 
/*     */   public PathMap(int capacity)
/*     */   {
/* 121 */     this(capacity, false);
/*     */   }
/*     */   
/*     */ 
/*     */   private PathMap(int capacity, boolean noDefault)
/*     */   {
/* 127 */     super(capacity);
/* 128 */     this._nodefault = noDefault;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathMap(Map<String, ? extends O> dictMap)
/*     */   {
/* 138 */     putAll(dictMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public O put(String pathSpec, O object)
/*     */   {
/* 150 */     if ("".equals(pathSpec.trim()))
/*     */     {
/* 152 */       MappedEntry<O> entry = new MappedEntry("", object);
/* 153 */       entry.setMapped("");
/* 154 */       this._exactMap.put("", entry);
/* 155 */       return (O)super.put("", object);
/*     */     }
/*     */     
/* 158 */     StringTokenizer tok = new StringTokenizer(pathSpec, __pathSpecSeparators);
/* 159 */     O old = null;
/*     */     
/* 161 */     while (tok.hasMoreTokens())
/*     */     {
/* 163 */       String spec = tok.nextToken();
/*     */       
/* 165 */       if ((!spec.startsWith("/")) && (!spec.startsWith("*."))) {
/* 166 */         throw new IllegalArgumentException("PathSpec " + spec + ". must start with '/' or '*.'");
/*     */       }
/* 168 */       old = super.put(spec, object);
/*     */       
/*     */ 
/* 171 */       MappedEntry<O> entry = new MappedEntry(spec, object);
/*     */       
/* 173 */       if (entry.getKey().equals(spec))
/*     */       {
/* 175 */         if (spec.equals("/*")) {
/* 176 */           this._prefixDefault = entry;
/* 177 */         } else if (spec.endsWith("/*"))
/*     */         {
/* 179 */           String mapped = spec.substring(0, spec.length() - 2);
/* 180 */           entry.setMapped(mapped);
/* 181 */           while (!this._prefixMap.put(mapped, entry)) {
/* 182 */             this._prefixMap = new ArrayTernaryTrie((ArrayTernaryTrie)this._prefixMap, 1.5D);
/*     */           }
/* 184 */         } else if (spec.startsWith("*."))
/*     */         {
/* 186 */           String suffix = spec.substring(2);
/* 187 */           while (!this._suffixMap.put(suffix, entry)) {
/* 188 */             this._suffixMap = new ArrayTernaryTrie((ArrayTernaryTrie)this._suffixMap, 1.5D);
/*     */           }
/* 190 */         } else if (spec.equals("/"))
/*     */         {
/* 192 */           if (this._nodefault) {
/* 193 */             this._exactMap.put(spec, entry);
/*     */           }
/*     */           else {
/* 196 */             this._default = entry;
/* 197 */             this._defaultSingletonList = Collections.singletonList(this._default);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 202 */           entry.setMapped(spec);
/* 203 */           this._exactMap.put(spec, entry);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 208 */     return old;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public O match(String path)
/*     */   {
/* 218 */     MappedEntry<O> entry = getMatch(path);
/* 219 */     if (entry != null)
/* 220 */       return (O)entry.getValue();
/* 221 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MappedEntry<O> getMatch(String path)
/*     */   {
/* 232 */     if (path == null) {
/* 233 */       return null;
/*     */     }
/* 235 */     int l = path.length();
/*     */     
/* 237 */     MappedEntry<O> entry = null;
/*     */     
/*     */ 
/* 240 */     if ((l == 1) && (path.charAt(0) == '/'))
/*     */     {
/* 242 */       entry = (MappedEntry)this._exactMap.get("");
/* 243 */       if (entry != null) {
/* 244 */         return entry;
/*     */       }
/*     */     }
/*     */     
/* 248 */     entry = (MappedEntry)this._exactMap.get(path);
/* 249 */     if (entry != null) {
/* 250 */       return entry;
/*     */     }
/*     */     
/* 253 */     int i = l;
/* 254 */     Trie<MappedEntry<O>> prefix_map = this._prefixMap;
/* 255 */     while (i >= 0)
/*     */     {
/* 257 */       entry = (MappedEntry)prefix_map.getBest(path, 0, i);
/* 258 */       if (entry == null)
/*     */         break;
/* 260 */       String key = entry.getKey();
/* 261 */       if ((key.length() - 2 >= path.length()) || (path.charAt(key.length() - 2) == '/'))
/* 262 */         return entry;
/* 263 */       i = key.length() - 3;
/*     */     }
/*     */     
/*     */ 
/* 267 */     if (this._prefixDefault != null) {
/* 268 */       return this._prefixDefault;
/*     */     }
/*     */     
/* 271 */     i = 0;
/* 272 */     Trie<MappedEntry<O>> suffix_map = this._suffixMap;
/* 273 */     while ((i = path.indexOf('.', i + 1)) > 0)
/*     */     {
/* 275 */       entry = (MappedEntry)suffix_map.get(path, i + 1, l - i - 1);
/* 276 */       if (entry != null) {
/* 277 */         return entry;
/*     */       }
/*     */     }
/*     */     
/* 281 */     return this._default;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<? extends Map.Entry<String, O>> getMatches(String path)
/*     */   {
/* 293 */     List<MappedEntry<O>> entries = new ArrayList();
/*     */     
/* 295 */     if (path == null)
/* 296 */       return entries;
/* 297 */     if (path.length() == 0) {
/* 298 */       return this._defaultSingletonList;
/*     */     }
/*     */     
/* 301 */     MappedEntry<O> entry = (MappedEntry)this._exactMap.get(path);
/* 302 */     if (entry != null) {
/* 303 */       entries.add(entry);
/*     */     }
/*     */     
/* 306 */     int l = path.length();
/* 307 */     int i = l;
/* 308 */     Trie<MappedEntry<O>> prefix_map = this._prefixMap;
/* 309 */     while (i >= 0)
/*     */     {
/* 311 */       entry = (MappedEntry)prefix_map.getBest(path, 0, i);
/* 312 */       if (entry == null)
/*     */         break;
/* 314 */       String key = entry.getKey();
/* 315 */       if ((key.length() - 2 >= path.length()) || (path.charAt(key.length() - 2) == '/')) {
/* 316 */         entries.add(entry);
/*     */       }
/* 318 */       i = key.length() - 3;
/*     */     }
/*     */     
/*     */ 
/* 322 */     if (this._prefixDefault != null) {
/* 323 */       entries.add(this._prefixDefault);
/*     */     }
/*     */     
/* 326 */     i = 0;
/* 327 */     Trie<MappedEntry<O>> suffix_map = this._suffixMap;
/* 328 */     while ((i = path.indexOf('.', i + 1)) > 0)
/*     */     {
/* 330 */       entry = (MappedEntry)suffix_map.get(path, i + 1, l - i - 1);
/* 331 */       if (entry != null) {
/* 332 */         entries.add(entry);
/*     */       }
/*     */     }
/*     */     
/* 336 */     if ("/".equals(path))
/*     */     {
/* 338 */       entry = (MappedEntry)this._exactMap.get("");
/* 339 */       if (entry != null) {
/* 340 */         entries.add(entry);
/*     */       }
/*     */     }
/*     */     
/* 344 */     if (this._default != null) {
/* 345 */       entries.add(this._default);
/*     */     }
/* 347 */     return entries;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsMatch(String path)
/*     */   {
/* 359 */     MappedEntry<?> match = getMatch(path);
/* 360 */     return (match != null) && (!match.equals(this._default));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public O remove(Object pathSpec)
/*     */   {
/* 367 */     if (pathSpec != null)
/*     */     {
/* 369 */       String spec = (String)pathSpec;
/* 370 */       if (spec.equals("/*")) {
/* 371 */         this._prefixDefault = null;
/* 372 */       } else if (spec.endsWith("/*")) {
/* 373 */         this._prefixMap.remove(spec.substring(0, spec.length() - 2));
/* 374 */       } else if (spec.startsWith("*.")) {
/* 375 */         this._suffixMap.remove(spec.substring(2));
/* 376 */       } else if (spec.equals("/"))
/*     */       {
/* 378 */         this._default = null;
/* 379 */         this._defaultSingletonList = null;
/*     */       }
/*     */       else {
/* 382 */         this._exactMap.remove(spec);
/*     */       } }
/* 384 */     return (O)super.remove(pathSpec);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 391 */     this._exactMap.clear();
/* 392 */     this._prefixMap = new ArrayTernaryTrie(false);
/* 393 */     this._suffixMap = new ArrayTernaryTrie(false);
/* 394 */     this._default = null;
/* 395 */     this._defaultSingletonList = null;
/* 396 */     this._prefixDefault = null;
/* 397 */     super.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean match(String pathSpec, String path)
/*     */   {
/* 408 */     return match(pathSpec, path, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean match(String pathSpec, String path, boolean noDefault)
/*     */   {
/* 420 */     if (pathSpec.length() == 0) {
/* 421 */       return "/".equals(path);
/*     */     }
/* 423 */     char c = pathSpec.charAt(0);
/* 424 */     if (c == '/')
/*     */     {
/* 426 */       if (((!noDefault) && (pathSpec.length() == 1)) || (pathSpec.equals(path))) {
/* 427 */         return true;
/*     */       }
/* 429 */       if (isPathWildcardMatch(pathSpec, path)) {
/* 430 */         return true;
/*     */       }
/* 432 */     } else if (c == '*') {
/* 433 */       return path.regionMatches(path.length() - pathSpec.length() + 1, pathSpec, 1, pathSpec
/* 434 */         .length() - 1); }
/* 435 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static boolean isPathWildcardMatch(String pathSpec, String path)
/*     */   {
/* 442 */     int cpl = pathSpec.length() - 2;
/* 443 */     if ((pathSpec.endsWith("/*")) && (path.regionMatches(0, pathSpec, 0, cpl)))
/*     */     {
/* 445 */       if ((path.length() == cpl) || ('/' == path.charAt(cpl)))
/* 446 */         return true;
/*     */     }
/* 448 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String pathMatch(String pathSpec, String path)
/*     */   {
/* 460 */     char c = pathSpec.charAt(0);
/*     */     
/* 462 */     if (c == '/')
/*     */     {
/* 464 */       if (pathSpec.length() == 1) {
/* 465 */         return path;
/*     */       }
/* 467 */       if (pathSpec.equals(path)) {
/* 468 */         return path;
/*     */       }
/* 470 */       if (isPathWildcardMatch(pathSpec, path)) {
/* 471 */         return path.substring(0, pathSpec.length() - 2);
/*     */       }
/* 473 */     } else if (c == '*')
/*     */     {
/* 475 */       if (path.regionMatches(path.length() - (pathSpec.length() - 1), pathSpec, 1, pathSpec
/* 476 */         .length() - 1))
/* 477 */         return path;
/*     */     }
/* 479 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String pathInfo(String pathSpec, String path)
/*     */   {
/* 490 */     if ("".equals(pathSpec)) {
/* 491 */       return path;
/*     */     }
/* 493 */     char c = pathSpec.charAt(0);
/*     */     
/* 495 */     if (c == '/')
/*     */     {
/* 497 */       if (pathSpec.length() == 1) {
/* 498 */         return null;
/*     */       }
/* 500 */       boolean wildcard = isPathWildcardMatch(pathSpec, path);
/*     */       
/*     */ 
/* 503 */       if ((pathSpec.equals(path)) && (!wildcard)) {
/* 504 */         return null;
/*     */       }
/* 506 */       if (wildcard)
/*     */       {
/* 508 */         if (path.length() == pathSpec.length() - 2)
/* 509 */           return null;
/* 510 */         return path.substring(pathSpec.length() - 2);
/*     */       }
/*     */     }
/* 513 */     return null;
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
/*     */   public static String relativePath(String base, String pathSpec, String path)
/*     */   {
/* 528 */     String info = pathInfo(pathSpec, path);
/* 529 */     if (info == null) {
/* 530 */       info = path;
/*     */     }
/* 532 */     if (info.startsWith("./"))
/* 533 */       info = info.substring(2);
/* 534 */     if (base.endsWith("/")) {
/* 535 */       if (info.startsWith("/")) {
/* 536 */         path = base + info.substring(1);
/*     */       } else {
/* 538 */         path = base + info;
/*     */       }
/* 540 */     } else if (info.startsWith("/")) {
/* 541 */       path = base + info;
/*     */     } else
/* 543 */       path = base + "/" + info;
/* 544 */     return path;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class MappedEntry<O>
/*     */     implements Map.Entry<String, O>
/*     */   {
/*     */     private final String key;
/*     */     
/*     */     private final O value;
/*     */     private String mapped;
/*     */     
/*     */     MappedEntry(String key, O value)
/*     */     {
/* 558 */       this.key = key;
/* 559 */       this.value = value;
/*     */     }
/*     */     
/*     */ 
/*     */     public String getKey()
/*     */     {
/* 565 */       return this.key;
/*     */     }
/*     */     
/*     */ 
/*     */     public O getValue()
/*     */     {
/* 571 */       return (O)this.value;
/*     */     }
/*     */     
/*     */ 
/*     */     public O setValue(O o)
/*     */     {
/* 577 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 583 */       return this.key + "=" + this.value;
/*     */     }
/*     */     
/*     */     public String getMapped()
/*     */     {
/* 588 */       return this.mapped;
/*     */     }
/*     */     
/*     */     void setMapped(String mapped)
/*     */     {
/* 593 */       this.mapped = mapped;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PathSet extends AbstractSet<String> implements Predicate<String>
/*     */   {
/* 599 */     private final PathMap<Boolean> _map = new PathMap();
/*     */     
/*     */ 
/*     */     public Iterator<String> iterator()
/*     */     {
/* 604 */       return this._map.keySet().iterator();
/*     */     }
/*     */     
/*     */ 
/*     */     public int size()
/*     */     {
/* 610 */       return this._map.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean add(String item)
/*     */     {
/* 616 */       return this._map.put(item, Boolean.TRUE) == null;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean remove(Object item)
/*     */     {
/* 622 */       return this._map.remove(item) != null;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean contains(Object o)
/*     */     {
/* 628 */       return this._map.containsKey(o);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean test(String s)
/*     */     {
/* 634 */       return this._map.containsMatch(s);
/*     */     }
/*     */     
/*     */     public boolean containsMatch(String s)
/*     */     {
/* 639 */       return this._map.containsMatch(s);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\PathMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */