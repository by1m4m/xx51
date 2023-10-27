/*     */ package org.eclipse.jetty.util.resource;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.channels.ReadableByteChannel;
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
/*     */ public class URLResource
/*     */   extends Resource
/*     */ {
/*  39 */   private static final Logger LOG = Log.getLogger(URLResource.class);
/*     */   
/*     */   protected final URL _url;
/*     */   protected final String _urlString;
/*     */   protected URLConnection _connection;
/*  44 */   protected InputStream _in = null;
/*  45 */   transient boolean _useCaches = Resource.__defaultUseCaches;
/*     */   
/*     */ 
/*     */   protected URLResource(URL url, URLConnection connection)
/*     */   {
/*  50 */     this._url = url;
/*  51 */     this._urlString = this._url.toExternalForm();
/*  52 */     this._connection = connection;
/*     */   }
/*     */   
/*     */ 
/*     */   protected URLResource(URL url, URLConnection connection, boolean useCaches)
/*     */   {
/*  58 */     this(url, connection);
/*  59 */     this._useCaches = useCaches;
/*     */   }
/*     */   
/*     */ 
/*     */   protected synchronized boolean checkConnection()
/*     */   {
/*  65 */     if (this._connection == null) {
/*     */       try
/*     */       {
/*  68 */         this._connection = this._url.openConnection();
/*  69 */         this._connection.setUseCaches(this._useCaches);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/*  73 */         LOG.ignore(e);
/*     */       }
/*     */     }
/*  76 */     return this._connection != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void close()
/*     */   {
/*  85 */     if (this._in != null) {
/*     */       try {
/*  87 */         this._in.close(); } catch (IOException e) { LOG.ignore(e); }
/*  88 */       this._in = null;
/*     */     }
/*     */     
/*  91 */     if (this._connection != null) {
/*  92 */       this._connection = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean exists()
/*     */   {
/*     */     try
/*     */     {
/* 104 */       synchronized (this)
/*     */       {
/* 106 */         if ((checkConnection()) && (this._in == null)) {
/* 107 */           this._in = this._connection.getInputStream();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 112 */       LOG.ignore(e);
/*     */     }
/* 114 */     return this._in != null;
/*     */   }
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
/* 126 */     return (exists()) && (this._urlString.endsWith("/"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long lastModified()
/*     */   {
/* 137 */     if (checkConnection())
/* 138 */       return this._connection.getLastModified();
/* 139 */     return -1L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long length()
/*     */   {
/* 150 */     if (checkConnection())
/* 151 */       return this._connection.getContentLength();
/* 152 */     return -1L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public URL getURL()
/*     */   {
/* 162 */     return this._url;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getFile()
/*     */     throws IOException
/*     */   {
/* 174 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 184 */     return this._url.toExternalForm();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 196 */     return getInputStream(true);
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
/*     */   protected synchronized InputStream getInputStream(boolean resetConnection)
/*     */     throws IOException
/*     */   {
/* 214 */     if (!checkConnection()) {
/* 215 */       throw new IOException("Invalid resource");
/*     */     }
/*     */     try {
/*     */       InputStream in;
/* 219 */       if (this._in != null)
/*     */       {
/* 221 */         in = this._in;
/* 222 */         this._in = null;
/* 223 */         return in;
/*     */       }
/* 225 */       return this._connection.getInputStream();
/*     */     }
/*     */     finally
/*     */     {
/* 229 */       if (resetConnection)
/*     */       {
/* 231 */         this._connection = null;
/* 232 */         if (LOG.isDebugEnabled()) { LOG.debug("Connection nulled", new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ReadableByteChannel getReadableByteChannel()
/*     */     throws IOException
/*     */   {
/* 241 */     return null;
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
/* 252 */     throw new SecurityException("Delete not supported");
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
/* 263 */     throw new SecurityException("RenameTo not supported");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] list()
/*     */   {
/* 273 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Resource addPath(String path)
/*     */     throws IOException, MalformedURLException
/*     */   {
/* 285 */     if (path == null) {
/* 286 */       return null;
/*     */     }
/* 288 */     path = URIUtil.canonicalPath(path);
/*     */     
/* 290 */     return newResource(URIUtil.addEncodedPaths(this._url.toExternalForm(), URIUtil.encodePath(path)), this._useCaches);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 297 */     return this._urlString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 304 */     return this._urlString.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 311 */     return ((o instanceof URLResource)) && (this._urlString.equals(((URLResource)o)._urlString));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean getUseCaches()
/*     */   {
/* 317 */     return this._useCaches;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isContainedIn(Resource containingResource)
/*     */     throws MalformedURLException
/*     */   {
/* 324 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\resource\URLResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */