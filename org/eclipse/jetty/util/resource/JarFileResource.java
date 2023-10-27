/*     */ package org.eclipse.jetty.util.resource;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import org.eclipse.jetty.util.URIUtil;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JarFileResource
/*     */   extends JarResource
/*     */ {
/*  39 */   private static final Logger LOG = Log.getLogger(JarFileResource.class);
/*     */   
/*     */   private JarFile _jarFile;
/*     */   private File _file;
/*     */   private String[] _list;
/*     */   private JarEntry _entry;
/*     */   private boolean _directory;
/*     */   private String _jarUrl;
/*     */   private String _path;
/*     */   private boolean _exists;
/*     */   
/*     */   protected JarFileResource(URL url)
/*     */   {
/*  52 */     super(url);
/*     */   }
/*     */   
/*     */ 
/*     */   protected JarFileResource(URL url, boolean useCaches)
/*     */   {
/*  58 */     super(url, useCaches);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized void close()
/*     */   {
/*  65 */     this._exists = false;
/*  66 */     this._list = null;
/*  67 */     this._entry = null;
/*  68 */     this._file = null;
/*     */     
/*     */ 
/*  71 */     if (!getUseCaches())
/*     */     {
/*  73 */       if (this._jarFile != null)
/*     */       {
/*     */         try
/*     */         {
/*  77 */           if (LOG.isDebugEnabled())
/*  78 */             LOG.debug("Closing JarFile " + this._jarFile.getName(), new Object[0]);
/*  79 */           this._jarFile.close();
/*     */         }
/*     */         catch (IOException ioe)
/*     */         {
/*  83 */           LOG.ignore(ioe);
/*     */         }
/*     */       }
/*     */     }
/*  87 */     this._jarFile = null;
/*  88 */     super.close();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected synchronized boolean checkConnection()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 23	org/eclipse/jetty/util/resource/JarResource:checkConnection	()Z
/*     */     //   4: pop
/*     */     //   5: aload_0
/*     */     //   6: getfield 24	org/eclipse/jetty/util/resource/JarFileResource:_jarConnection	Ljava/net/JarURLConnection;
/*     */     //   9: ifnonnull +56 -> 65
/*     */     //   12: aload_0
/*     */     //   13: aconst_null
/*     */     //   14: putfield 5	org/eclipse/jetty/util/resource/JarFileResource:_entry	Ljava/util/jar/JarEntry;
/*     */     //   17: aload_0
/*     */     //   18: aconst_null
/*     */     //   19: putfield 6	org/eclipse/jetty/util/resource/JarFileResource:_file	Ljava/io/File;
/*     */     //   22: aload_0
/*     */     //   23: aconst_null
/*     */     //   24: putfield 8	org/eclipse/jetty/util/resource/JarFileResource:_jarFile	Ljava/util/jar/JarFile;
/*     */     //   27: aload_0
/*     */     //   28: aconst_null
/*     */     //   29: putfield 4	org/eclipse/jetty/util/resource/JarFileResource:_list	[Ljava/lang/String;
/*     */     //   32: goto +33 -> 65
/*     */     //   35: astore_1
/*     */     //   36: aload_0
/*     */     //   37: getfield 24	org/eclipse/jetty/util/resource/JarFileResource:_jarConnection	Ljava/net/JarURLConnection;
/*     */     //   40: ifnonnull +23 -> 63
/*     */     //   43: aload_0
/*     */     //   44: aconst_null
/*     */     //   45: putfield 5	org/eclipse/jetty/util/resource/JarFileResource:_entry	Ljava/util/jar/JarEntry;
/*     */     //   48: aload_0
/*     */     //   49: aconst_null
/*     */     //   50: putfield 6	org/eclipse/jetty/util/resource/JarFileResource:_file	Ljava/io/File;
/*     */     //   53: aload_0
/*     */     //   54: aconst_null
/*     */     //   55: putfield 8	org/eclipse/jetty/util/resource/JarFileResource:_jarFile	Ljava/util/jar/JarFile;
/*     */     //   58: aload_0
/*     */     //   59: aconst_null
/*     */     //   60: putfield 4	org/eclipse/jetty/util/resource/JarFileResource:_list	[Ljava/lang/String;
/*     */     //   63: aload_1
/*     */     //   64: athrow
/*     */     //   65: aload_0
/*     */     //   66: getfield 8	org/eclipse/jetty/util/resource/JarFileResource:_jarFile	Ljava/util/jar/JarFile;
/*     */     //   69: ifnull +7 -> 76
/*     */     //   72: iconst_1
/*     */     //   73: goto +4 -> 77
/*     */     //   76: iconst_0
/*     */     //   77: ireturn
/*     */     // Line number table:
/*     */     //   Java source line #97	-> byte code offset #0
/*     */     //   Java source line #101	-> byte code offset #5
/*     */     //   Java source line #103	-> byte code offset #12
/*     */     //   Java source line #104	-> byte code offset #17
/*     */     //   Java source line #105	-> byte code offset #22
/*     */     //   Java source line #106	-> byte code offset #27
/*     */     //   Java source line #101	-> byte code offset #35
/*     */     //   Java source line #103	-> byte code offset #43
/*     */     //   Java source line #104	-> byte code offset #48
/*     */     //   Java source line #105	-> byte code offset #53
/*     */     //   Java source line #106	-> byte code offset #58
/*     */     //   Java source line #109	-> byte code offset #65
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	78	0	this	JarFileResource
/*     */     //   35	29	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	5	35	finally
/*     */   }
/*     */   
/*     */   protected synchronized void newConnection()
/*     */     throws IOException
/*     */   {
/* 118 */     super.newConnection();
/*     */     
/* 120 */     this._entry = null;
/* 121 */     this._file = null;
/* 122 */     this._jarFile = null;
/* 123 */     this._list = null;
/*     */     
/* 125 */     int sep = this._urlString.lastIndexOf("!/");
/* 126 */     this._jarUrl = this._urlString.substring(0, sep + 2);
/* 127 */     this._path = URIUtil.decodePath(this._urlString.substring(sep + 2));
/* 128 */     if (this._path.length() == 0)
/* 129 */       this._path = null;
/* 130 */     this._jarFile = this._jarConnection.getJarFile();
/* 131 */     this._file = new File(this._jarFile.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean exists()
/*     */   {
/* 143 */     if (this._exists) {
/* 144 */       return true;
/*     */     }
/* 146 */     if (this._urlString.endsWith("!/"))
/*     */     {
/* 148 */       String file_url = this._urlString.substring(4, this._urlString.length() - 2);
/* 149 */       try { return newResource(file_url).exists();
/* 150 */       } catch (Exception e) { LOG.ignore(e);return false;
/*     */       }
/*     */     }
/* 153 */     boolean check = checkConnection();
/*     */     
/*     */ 
/* 156 */     if ((this._jarUrl != null) && (this._path == null))
/*     */     {
/*     */ 
/* 159 */       this._directory = check;
/* 160 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 165 */     boolean close_jar_file = false;
/* 166 */     JarFile jar_file = null;
/* 167 */     if (check)
/*     */     {
/* 169 */       jar_file = this._jarFile;
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 175 */         JarURLConnection c = (JarURLConnection)new URL(this._jarUrl).openConnection();
/* 176 */         c.setUseCaches(getUseCaches());
/* 177 */         jar_file = c.getJarFile();
/* 178 */         close_jar_file = !getUseCaches();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 182 */         LOG.ignore(e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 187 */     if ((jar_file != null) && (this._entry == null) && (!this._directory))
/*     */     {
/*     */ 
/* 190 */       JarEntry entry = jar_file.getJarEntry(this._path);
/* 191 */       if (entry == null)
/*     */       {
/*     */ 
/* 194 */         this._exists = false;
/*     */       }
/* 196 */       else if (entry.isDirectory())
/*     */       {
/* 198 */         this._directory = true;
/* 199 */         this._entry = entry;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 204 */         JarEntry directory = jar_file.getJarEntry(this._path + '/');
/* 205 */         if (directory != null)
/*     */         {
/* 207 */           this._directory = true;
/* 208 */           this._entry = directory;
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 213 */           this._directory = false;
/* 214 */           this._entry = entry;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 219 */     if ((close_jar_file) && (jar_file != null))
/*     */     {
/*     */       try
/*     */       {
/* 223 */         jar_file.close();
/*     */       }
/*     */       catch (IOException ioe)
/*     */       {
/* 227 */         LOG.ignore(ioe);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 232 */     this._exists = ((this._directory) || (this._entry != null));
/* 233 */     return this._exists;
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
/*     */   public boolean isDirectory()
/*     */   {
/* 246 */     return (this._urlString.endsWith("/")) || ((exists()) && (this._directory));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long lastModified()
/*     */   {
/* 256 */     if ((checkConnection()) && (this._file != null))
/*     */     {
/* 258 */       if ((exists()) && (this._entry != null))
/* 259 */         return this._entry.getTime();
/* 260 */       return this._file.lastModified();
/*     */     }
/* 262 */     return -1L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized String[] list()
/*     */   {
/* 269 */     if ((isDirectory()) && (this._list == null))
/*     */     {
/* 271 */       List<String> list = null;
/*     */       try
/*     */       {
/* 274 */         list = listEntries();
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 284 */         LOG.warn("Retrying list:" + e, new Object[0]);
/* 285 */         LOG.debug(e);
/* 286 */         close();
/* 287 */         list = listEntries();
/*     */       }
/*     */       
/* 290 */       if (list != null)
/*     */       {
/* 292 */         this._list = new String[list.size()];
/* 293 */         list.toArray(this._list);
/*     */       }
/*     */     }
/* 296 */     return this._list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private List<String> listEntries()
/*     */   {
/* 303 */     checkConnection();
/*     */     
/* 305 */     ArrayList<String> list = new ArrayList(32);
/* 306 */     JarFile jarFile = this._jarFile;
/* 307 */     if (jarFile == null)
/*     */     {
/*     */       try
/*     */       {
/* 311 */         JarURLConnection jc = (JarURLConnection)new URL(this._jarUrl).openConnection();
/* 312 */         jc.setUseCaches(getUseCaches());
/* 313 */         jarFile = jc.getJarFile();
/*     */ 
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 318 */         e.printStackTrace();
/* 319 */         LOG.ignore(e);
/*     */       }
/* 321 */       if (jarFile == null) {
/* 322 */         throw new IllegalStateException();
/*     */       }
/*     */     }
/* 325 */     Enumeration<JarEntry> e = jarFile.entries();
/* 326 */     String dir = this._urlString.substring(this._urlString.lastIndexOf("!/") + 2);
/* 327 */     while (e.hasMoreElements())
/*     */     {
/* 329 */       JarEntry entry = (JarEntry)e.nextElement();
/* 330 */       String name = entry.getName().replace('\\', '/');
/* 331 */       if ((name.startsWith(dir)) && (name.length() != dir.length()))
/*     */       {
/*     */ 
/*     */ 
/* 335 */         String listName = name.substring(dir.length());
/* 336 */         int dash = listName.indexOf('/');
/* 337 */         if (dash >= 0)
/*     */         {
/*     */ 
/*     */ 
/* 341 */           if ((dash != 0) || (listName.length() != 1))
/*     */           {
/*     */ 
/*     */ 
/* 345 */             if (dash == 0) {
/* 346 */               listName = listName.substring(dash + 1, listName.length());
/*     */             } else {
/* 348 */               listName = listName.substring(0, dash + 1);
/*     */             }
/* 350 */             if (list.contains(listName)) {}
/*     */           }
/*     */         }
/*     */         else
/* 354 */           list.add(listName);
/*     */       }
/*     */     }
/* 357 */     return list;
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
/*     */   public long length()
/*     */   {
/* 371 */     if (isDirectory()) {
/* 372 */       return -1L;
/*     */     }
/* 374 */     if (this._entry != null) {
/* 375 */       return this._entry.getSize();
/*     */     }
/* 377 */     return -1L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Resource getNonCachingResource(Resource resource)
/*     */   {
/* 389 */     if (!(resource instanceof JarFileResource)) {
/* 390 */       return resource;
/*     */     }
/* 392 */     JarFileResource oldResource = (JarFileResource)resource;
/*     */     
/* 394 */     JarFileResource newResource = new JarFileResource(oldResource.getURL(), false);
/* 395 */     return newResource;
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
/*     */   public boolean isContainedIn(Resource resource)
/*     */     throws MalformedURLException
/*     */   {
/* 409 */     String string = this._urlString;
/* 410 */     int index = string.lastIndexOf("!/");
/* 411 */     if (index > 0)
/* 412 */       string = string.substring(0, index);
/* 413 */     if (string.startsWith("jar:"))
/* 414 */       string = string.substring(4);
/* 415 */     URL url = new URL(string);
/* 416 */     return url.sameFile(resource.getURI().toURL());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\resource\JarFileResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */