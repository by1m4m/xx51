/*     */ package org.eclipse.jetty.util.resource;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.security.Permission;
/*     */ import org.eclipse.jetty.util.IO;
/*     */ import org.eclipse.jetty.util.StringUtil;
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
/*     */ 
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
/*     */ public class FileResource
/*     */   extends Resource
/*     */ {
/*  56 */   private static final Logger LOG = Log.getLogger(FileResource.class);
/*     */   
/*     */   private final File _file;
/*     */   
/*     */   private final URI _uri;
/*     */   
/*     */   private final URI _alias;
/*     */   
/*     */ 
/*     */   public FileResource(URL url)
/*     */     throws IOException, URISyntaxException
/*     */   {
/*     */     File file;
/*     */     try
/*     */     {
/*  71 */       File file = new File(url.toURI());
/*  72 */       assertValidPath(file.toString());
/*     */     }
/*     */     catch (URISyntaxException e)
/*     */     {
/*  76 */       throw e;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  80 */       if (!url.toString().startsWith("file:")) {
/*  81 */         throw new IllegalArgumentException("!file:");
/*     */       }
/*  83 */       LOG.ignore(e);
/*     */       
/*     */       try
/*     */       {
/*  87 */         String file_url = "file:" + URIUtil.encodePath(url.toString().substring(5));
/*  88 */         URI uri = new URI(file_url);
/*  89 */         File file; if (uri.getAuthority() == null) {
/*  90 */           file = new File(uri);
/*     */         } else {
/*  92 */           file = new File("//" + uri.getAuthority() + URIUtil.decodePath(url.getFile()));
/*     */         }
/*     */       } catch (Exception e2) {
/*     */         File file;
/*  96 */         LOG.ignore(e2);
/*     */         
/*  98 */         URLConnection connection = url.openConnection();
/*  99 */         Permission perm = connection.getPermission();
/* 100 */         file = new File(perm == null ? url.getFile() : perm.getName());
/*     */       }
/*     */     }
/*     */     
/* 104 */     this._file = file;
/* 105 */     this._uri = normalizeURI(this._file, url.toURI());
/* 106 */     this._alias = checkFileAlias(this._uri, this._file);
/*     */   }
/*     */   
/*     */ 
/*     */   public FileResource(URI uri)
/*     */   {
/* 112 */     File file = new File(uri);
/* 113 */     this._file = file;
/*     */     try
/*     */     {
/* 116 */       URI file_uri = this._file.toURI();
/* 117 */       this._uri = normalizeURI(this._file, uri);
/* 118 */       assertValidPath(file.toString());
/*     */       
/*     */ 
/* 121 */       if (!URIUtil.equalsIgnoreEncodings(this._uri.toASCIIString(), file_uri.toString())) {
/* 122 */         this._alias = this._file.toURI();
/*     */       } else {
/* 124 */         this._alias = checkFileAlias(this._uri, this._file);
/*     */       }
/*     */     }
/*     */     catch (URISyntaxException e) {
/* 128 */       throw new InvalidPathException(this._file.toString(), e.getMessage()) {};
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileResource(File file)
/*     */   {
/* 140 */     assertValidPath(file.toString());
/* 141 */     this._file = file;
/*     */     try
/*     */     {
/* 144 */       this._uri = normalizeURI(this._file, this._file.toURI());
/*     */     }
/*     */     catch (URISyntaxException e)
/*     */     {
/* 148 */       throw new InvalidPathException(this._file.toString(), e.getMessage()) {};
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 155 */     this._alias = checkFileAlias(this._uri, this._file);
/*     */   }
/*     */   
/*     */ 
/*     */   public FileResource(File base, String childPath)
/*     */   {
/* 161 */     String encoded = URIUtil.encodePath(childPath);
/*     */     
/* 163 */     this._file = new File(base, childPath);
/*     */     
/*     */     try
/*     */     {
/*     */       URI uri;
/*     */       
/* 169 */       if (base.isDirectory())
/*     */       {
/*     */ 
/* 172 */         uri = new URI(URIUtil.addEncodedPaths(base.toURI().toASCIIString(), encoded));
/*     */       }
/*     */       else
/*     */       {
/* 176 */         uri = new URI(base.toURI().toASCIIString() + encoded);
/*     */       }
/*     */     }
/*     */     catch (URISyntaxException e) {
/*     */       URI uri;
/* 181 */       throw new InvalidPathException(base.toString() + childPath, e.getMessage()) {};
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     URI uri;
/*     */     
/*     */ 
/* 189 */     this._uri = uri;
/* 190 */     this._alias = checkFileAlias(this._uri, this._file);
/*     */   }
/*     */   
/*     */   private static URI normalizeURI(File file, URI uri) throws URISyntaxException
/*     */   {
/* 195 */     String u = uri.toASCIIString();
/* 196 */     if (file.isDirectory())
/*     */     {
/* 198 */       if (!u.endsWith("/")) {
/* 199 */         u = u + "/";
/*     */       }
/* 201 */     } else if ((file.exists()) && (u.endsWith("/")))
/* 202 */       u = u.substring(0, u.length() - 1);
/* 203 */     return new URI(u);
/*     */   }
/*     */   
/*     */ 
/*     */   private static URI checkFileAlias(URI uri, File file)
/*     */   {
/*     */     try
/*     */     {
/* 211 */       if (!URIUtil.equalsIgnoreEncodings(uri, file.toURI()))
/*     */       {
/*     */ 
/* 214 */         return new File(uri).getAbsoluteFile().toURI();
/*     */       }
/*     */       
/* 217 */       String abs = file.getAbsolutePath();
/* 218 */       String can = file.getCanonicalPath();
/*     */       
/* 220 */       if (!abs.equals(can))
/*     */       {
/* 222 */         if (LOG.isDebugEnabled()) {
/* 223 */           LOG.debug("ALIAS abs={} can={}", new Object[] { abs, can });
/*     */         }
/* 225 */         URI alias = new File(can).toURI();
/*     */         
/* 227 */         return new URI("file://" + URIUtil.encodePath(alias.getPath()));
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 232 */       LOG.warn("bad alias for {}: {}", new Object[] { file, e.toString() });
/* 233 */       LOG.debug(e);
/*     */       try
/*     */       {
/* 236 */         return new URI("http://eclipse.org/bad/canonical/alias");
/*     */       }
/*     */       catch (Exception e2)
/*     */       {
/* 240 */         LOG.ignore(e2);
/* 241 */         throw new RuntimeException(e);
/*     */       }
/*     */     }
/*     */     
/* 245 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Resource addPath(String path)
/*     */     throws IOException, MalformedURLException
/*     */   {
/* 253 */     assertValidPath(path);
/* 254 */     path = URIUtil.canonicalPath(path);
/*     */     
/* 256 */     if (path == null) {
/* 257 */       throw new MalformedURLException();
/*     */     }
/* 259 */     if ("/".equals(path)) {
/* 260 */       return this;
/*     */     }
/* 262 */     return new FileResource(this._file, path);
/*     */   }
/*     */   
/*     */   private void assertValidPath(String path)
/*     */   {
/* 267 */     int idx = StringUtil.indexOfControlChars(path);
/* 268 */     if (idx >= 0)
/*     */     {
/* 270 */       throw new InvalidPathException(path, "Invalid Character at index " + idx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public URI getAlias()
/*     */   {
/* 278 */     return this._alias;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean exists()
/*     */   {
/* 288 */     return this._file.exists();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long lastModified()
/*     */   {
/* 298 */     return this._file.lastModified();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDirectory()
/*     */   {
/* 308 */     return ((this._file.exists()) && (this._file.isDirectory())) || (this._uri.toASCIIString().endsWith("/"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long length()
/*     */   {
/* 318 */     return this._file.length();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 329 */     return this._file.getAbsolutePath();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getFile()
/*     */   {
/* 340 */     return this._file;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 350 */     return new FileInputStream(this._file);
/*     */   }
/*     */   
/*     */ 
/*     */   public ReadableByteChannel getReadableByteChannel()
/*     */     throws IOException
/*     */   {
/* 357 */     return FileChannel.open(this._file.toPath(), new OpenOption[] { StandardOpenOption.READ });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean delete()
/*     */     throws SecurityException
/*     */   {
/* 368 */     return this._file.delete();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean renameTo(Resource dest)
/*     */     throws SecurityException
/*     */   {
/* 379 */     if ((dest instanceof FileResource)) {
/* 380 */       return this._file.renameTo(((FileResource)dest)._file);
/*     */     }
/* 382 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] list()
/*     */   {
/* 392 */     String[] list = this._file.list();
/* 393 */     if (list == null)
/* 394 */       return null;
/* 395 */     for (int i = list.length; i-- > 0; 
/*     */         
/*     */ 
/*     */ 
/* 399 */         tmp64_62[tmp64_63] = (tmp64_62[tmp64_63] + "/")) {
/*     */       label17:
/* 397 */       if ((!new File(this._file, list[i]).isDirectory()) || 
/* 398 */         (list[i].endsWith("/")))
/*     */         break label17;
/*     */     }
/* 401 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 413 */     if (this == o) {
/* 414 */       return true;
/*     */     }
/* 416 */     if ((null == o) || (!(o instanceof FileResource))) {
/* 417 */       return false;
/*     */     }
/* 419 */     FileResource f = (FileResource)o;
/* 420 */     return (f._file == this._file) || ((null != this._file) && (this._file.equals(f._file)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 430 */     return null == this._file ? super.hashCode() : this._file.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void copyTo(File destination)
/*     */     throws IOException
/*     */   {
/* 438 */     if (isDirectory())
/*     */     {
/* 440 */       IO.copyDir(getFile(), destination);
/*     */     }
/*     */     else
/*     */     {
/* 444 */       if (destination.exists())
/* 445 */         throw new IllegalArgumentException(destination + " exists");
/* 446 */       IO.copy(getFile(), destination);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isContainedIn(Resource r)
/*     */     throws MalformedURLException
/*     */   {
/* 453 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public URL getURL()
/*     */   {
/*     */     try
/*     */     {
/* 466 */       return this._uri.toURL();
/*     */     }
/*     */     catch (MalformedURLException e)
/*     */     {
/* 470 */       throw new IllegalStateException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public URI getURI()
/*     */   {
/* 477 */     return this._uri;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 483 */     return this._uri.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\resource\FileResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */