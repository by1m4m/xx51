/*     */ package org.eclipse.jetty.util.resource;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.DirectoryIteratorException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class PathResource
/*     */   extends Resource
/*     */ {
/*  53 */   private static final Logger LOG = Log.getLogger(PathResource.class);
/*  54 */   private static final LinkOption[] NO_FOLLOW_LINKS = { LinkOption.NOFOLLOW_LINKS };
/*  55 */   private static final LinkOption[] FOLLOW_LINKS = new LinkOption[0];
/*     */   
/*     */   private final Path path;
/*     */   private final Path alias;
/*     */   private final URI uri;
/*     */   
/*     */   private final Path checkAliasPath()
/*     */   {
/*  63 */     Path abs = this.path;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */     if (!URIUtil.equalsIgnoreEncodings(this.uri, this.path.toUri()))
/*     */     {
/*     */       try
/*     */       {
/*  78 */         return Paths.get(this.uri).toRealPath(FOLLOW_LINKS);
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (IOException ignored)
/*     */       {
/*     */ 
/*  85 */         LOG.ignore(ignored);
/*     */       }
/*     */     }
/*     */     
/*  89 */     if (!abs.isAbsolute())
/*     */     {
/*  91 */       abs = this.path.toAbsolutePath();
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  96 */       if (Files.isSymbolicLink(this.path))
/*  97 */         return this.path.getParent().resolve(Files.readSymbolicLink(this.path));
/*  98 */       if (Files.exists(this.path, new LinkOption[0]))
/*     */       {
/* 100 */         Path real = abs.toRealPath(FOLLOW_LINKS);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 136 */         int absCount = abs.getNameCount();
/* 137 */         int realCount = real.getNameCount();
/* 138 */         if (absCount != realCount)
/*     */         {
/*     */ 
/* 141 */           return real;
/*     */         }
/*     */         
/*     */ 
/* 145 */         for (int i = realCount - 1; i >= 0; i--)
/*     */         {
/* 147 */           if (!abs.getName(i).toString().equals(real.getName(i).toString()))
/*     */           {
/* 149 */             return real;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 156 */       LOG.ignore(e);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 160 */       LOG.warn("bad alias ({} {}) for {}", new Object[] { e.getClass().getName(), e.getMessage(), this.path });
/*     */     }
/* 162 */     return null;
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
/*     */   public PathResource(File file)
/*     */   {
/* 184 */     this(file.toPath());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathResource(Path path)
/*     */   {
/* 194 */     this.path = path.toAbsolutePath();
/* 195 */     assertValidPath(path);
/* 196 */     this.uri = this.path.toUri();
/* 197 */     this.alias = checkAliasPath();
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
/*     */   private PathResource(PathResource parent, String childPath)
/*     */     throws MalformedURLException
/*     */   {
/* 213 */     this.path = parent.path.getFileSystem().getPath(parent.path.toString(), new String[] { childPath });
/* 214 */     if ((isDirectory()) && (!childPath.endsWith("/")))
/* 215 */       childPath = childPath + "/";
/* 216 */     this.uri = URIUtil.addPath(parent.uri, childPath);
/* 217 */     this.alias = checkAliasPath();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathResource(URI uri)
/*     */     throws IOException
/*     */   {
/* 230 */     if (!uri.isAbsolute())
/*     */     {
/* 232 */       throw new IllegalArgumentException("not an absolute uri");
/*     */     }
/*     */     
/* 235 */     if (!uri.getScheme().equalsIgnoreCase("file"))
/*     */     {
/* 237 */       throw new IllegalArgumentException("not file: scheme");
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 243 */       path = Paths.get(uri);
/*     */     }
/*     */     catch (InvalidPathException e) {
/*     */       Path path;
/* 247 */       throw e;
/*     */     }
/*     */     catch (IllegalArgumentException e)
/*     */     {
/* 251 */       throw e;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 255 */       LOG.ignore(e);
/* 256 */       throw new IOException("Unable to build Path from: " + uri, e);
/*     */     }
/*     */     Path path;
/* 259 */     this.path = path.toAbsolutePath();
/* 260 */     this.uri = path.toUri();
/* 261 */     this.alias = checkAliasPath();
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
/*     */   public PathResource(URL url)
/*     */     throws IOException, URISyntaxException
/*     */   {
/* 285 */     this(url.toURI());
/*     */   }
/*     */   
/*     */   public Resource addPath(String subpath)
/*     */     throws IOException, MalformedURLException
/*     */   {
/* 291 */     String cpath = URIUtil.canonicalPath(subpath);
/*     */     
/* 293 */     if ((cpath == null) || (cpath.length() == 0)) {
/* 294 */       throw new MalformedURLException(subpath);
/*     */     }
/* 296 */     if ("/".equals(cpath)) {
/* 297 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 304 */     return new PathResource(this, subpath);
/*     */   }
/*     */   
/*     */ 
/*     */   private void assertValidPath(Path path)
/*     */   {
/* 310 */     String str = path.toString();
/* 311 */     int idx = StringUtil.indexOfControlChars(str);
/* 312 */     if (idx >= 0)
/*     */     {
/* 314 */       throw new InvalidPathException(str, "Invalid Character at index " + idx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean delete()
/*     */     throws SecurityException
/*     */   {
/*     */     try
/*     */     {
/* 329 */       return Files.deleteIfExists(this.path);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 333 */       LOG.ignore(e); }
/* 334 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 341 */     if (this == obj)
/*     */     {
/* 343 */       return true;
/*     */     }
/* 345 */     if (obj == null)
/*     */     {
/* 347 */       return false;
/*     */     }
/* 349 */     if (getClass() != obj.getClass())
/*     */     {
/* 351 */       return false;
/*     */     }
/* 353 */     PathResource other = (PathResource)obj;
/* 354 */     if (this.path == null)
/*     */     {
/* 356 */       if (other.path != null)
/*     */       {
/* 358 */         return false;
/*     */       }
/*     */     }
/* 361 */     else if (!this.path.equals(other.path))
/*     */     {
/* 363 */       return false;
/*     */     }
/* 365 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean exists()
/*     */   {
/* 371 */     return Files.exists(this.path, NO_FOLLOW_LINKS);
/*     */   }
/*     */   
/*     */   public File getFile()
/*     */     throws IOException
/*     */   {
/* 377 */     return this.path.toFile();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Path getPath()
/*     */   {
/* 385 */     return this.path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 395 */     if (Files.isDirectory(this.path, new LinkOption[0])) {
/* 396 */       throw new IOException(this.path + " is a directory");
/*     */     }
/* 398 */     return Files.newInputStream(this.path, new OpenOption[] { StandardOpenOption.READ });
/*     */   }
/*     */   
/*     */ 
/*     */   public String getName()
/*     */   {
/* 404 */     return this.path.toAbsolutePath().toString();
/*     */   }
/*     */   
/*     */   public ReadableByteChannel getReadableByteChannel()
/*     */     throws IOException
/*     */   {
/* 410 */     return FileChannel.open(this.path, new OpenOption[] { StandardOpenOption.READ });
/*     */   }
/*     */   
/*     */ 
/*     */   public URI getURI()
/*     */   {
/* 416 */     return this.uri;
/*     */   }
/*     */   
/*     */ 
/*     */   public URL getURL()
/*     */   {
/*     */     try
/*     */     {
/* 424 */       return this.path.toUri().toURL();
/*     */     }
/*     */     catch (MalformedURLException e) {}
/*     */     
/* 428 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 435 */     int prime = 31;
/* 436 */     int result = 1;
/* 437 */     result = 31 * result + (this.path == null ? 0 : this.path.hashCode());
/* 438 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isContainedIn(Resource r)
/*     */     throws MalformedURLException
/*     */   {
/* 445 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isDirectory()
/*     */   {
/* 451 */     return Files.isDirectory(this.path, FOLLOW_LINKS);
/*     */   }
/*     */   
/*     */ 
/*     */   public long lastModified()
/*     */   {
/*     */     try
/*     */     {
/* 459 */       FileTime ft = Files.getLastModifiedTime(this.path, FOLLOW_LINKS);
/* 460 */       return ft.toMillis();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 464 */       LOG.ignore(e); }
/* 465 */     return 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long length()
/*     */   {
/*     */     try
/*     */     {
/* 474 */       return Files.size(this.path);
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/*     */ 
/* 479 */     return 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isAlias()
/*     */   {
/* 486 */     return this.alias != null;
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
/*     */   public Path getAliasPath()
/*     */   {
/* 500 */     return this.alias;
/*     */   }
/*     */   
/*     */ 
/*     */   public URI getAlias()
/*     */   {
/* 506 */     return this.alias == null ? null : this.alias.toUri();
/*     */   }
/*     */   
/*     */   public String[] list()
/*     */   {
/*     */     try {
/* 512 */       DirectoryStream<Path> dir = Files.newDirectoryStream(this.path);Throwable localThrowable3 = null;
/*     */       try {
/* 514 */         List<String> entries = new ArrayList();
/* 515 */         for (Iterator localIterator = dir.iterator(); localIterator.hasNext();) { entry = (Path)localIterator.next();
/*     */           
/* 517 */           String name = entry.getFileName().toString();
/*     */           
/* 519 */           if (Files.isDirectory(entry, new LinkOption[0]))
/*     */           {
/* 521 */             name = name + "/";
/*     */           }
/*     */           
/* 524 */           entries.add(name); }
/*     */         Path entry;
/* 526 */         int size = entries.size();
/* 527 */         return (String[])entries.toArray(new String[size]);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 512 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 528 */         if (dir != null) { if (localThrowable3 != null) try { dir.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { dir.close();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 537 */       return null;
/*     */     }
/*     */     catch (DirectoryIteratorException e)
/*     */     {
/* 531 */       LOG.debug(e);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 535 */       LOG.debug(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean renameTo(Resource dest)
/*     */     throws SecurityException
/*     */   {
/* 543 */     if ((dest instanceof PathResource))
/*     */     {
/* 545 */       PathResource destRes = (PathResource)dest;
/*     */       try
/*     */       {
/* 548 */         Path result = Files.move(this.path, destRes.path, new CopyOption[0]);
/* 549 */         return Files.exists(result, NO_FOLLOW_LINKS);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 553 */         LOG.ignore(e);
/* 554 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 559 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void copyTo(File destination)
/*     */     throws IOException
/*     */   {
/* 566 */     if (isDirectory())
/*     */     {
/* 568 */       IO.copyDir(this.path.toFile(), destination);
/*     */     }
/*     */     else
/*     */     {
/* 572 */       Files.copy(this.path, destination.toPath(), new CopyOption[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 579 */     return this.uri.toASCIIString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\resource\PathResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */