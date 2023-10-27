/*     */ package org.eclipse.jetty.client.util;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.AccessDeniedException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
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
/*     */ public class PathContentProvider
/*     */   extends AbstractTypedContentProvider
/*     */ {
/*  48 */   private static final Logger LOG = Log.getLogger(PathContentProvider.class);
/*     */   private final Path filePath;
/*     */   private final long fileSize;
/*     */   private final int bufferSize;
/*     */   private ByteBufferPool bufferPool;
/*     */   
/*     */   public PathContentProvider(Path filePath)
/*     */     throws IOException
/*     */   {
/*  57 */     this(filePath, 4096);
/*     */   }
/*     */   
/*     */   public PathContentProvider(Path filePath, int bufferSize) throws IOException
/*     */   {
/*  62 */     this("application/octet-stream", filePath, bufferSize);
/*     */   }
/*     */   
/*     */   public PathContentProvider(String contentType, Path filePath) throws IOException
/*     */   {
/*  67 */     this(contentType, filePath, 4096);
/*     */   }
/*     */   
/*     */   public PathContentProvider(String contentType, Path filePath, int bufferSize) throws IOException
/*     */   {
/*  72 */     super(contentType);
/*  73 */     if (!Files.isRegularFile(filePath, new LinkOption[0]))
/*  74 */       throw new NoSuchFileException(filePath.toString());
/*  75 */     if (!Files.isReadable(filePath))
/*  76 */       throw new AccessDeniedException(filePath.toString());
/*  77 */     this.filePath = filePath;
/*  78 */     this.fileSize = Files.size(filePath);
/*  79 */     this.bufferSize = bufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getLength()
/*     */   {
/*  85 */     return this.fileSize;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isReproducible()
/*     */   {
/*  91 */     return true;
/*     */   }
/*     */   
/*     */   public ByteBufferPool getByteBufferPool()
/*     */   {
/*  96 */     return this.bufferPool;
/*     */   }
/*     */   
/*     */   public void setByteBufferPool(ByteBufferPool byteBufferPool)
/*     */   {
/* 101 */     this.bufferPool = byteBufferPool;
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<ByteBuffer> iterator()
/*     */   {
/* 107 */     return new PathIterator(null);
/*     */   }
/*     */   
/*     */   private class PathIterator implements Iterator<ByteBuffer>, Closeable
/*     */   {
/*     */     private ByteBuffer buffer;
/*     */     private SeekableByteChannel channel;
/*     */     private long position;
/*     */     
/*     */     private PathIterator() {}
/*     */     
/*     */     public boolean hasNext() {
/* 119 */       return this.position < PathContentProvider.this.getLength();
/*     */     }
/*     */     
/*     */ 
/*     */     public ByteBuffer next()
/*     */     {
/*     */       try
/*     */       {
/* 127 */         if (this.channel == null)
/*     */         {
/*     */ 
/*     */ 
/* 131 */           this.buffer = (PathContentProvider.this.bufferPool == null ? ByteBuffer.allocateDirect(PathContentProvider.this.bufferSize) : PathContentProvider.this.bufferPool.acquire(PathContentProvider.this.bufferSize, true));
/* 132 */           this.channel = Files.newByteChannel(PathContentProvider.this.filePath, new OpenOption[] { StandardOpenOption.READ });
/* 133 */           if (PathContentProvider.LOG.isDebugEnabled()) {
/* 134 */             PathContentProvider.LOG.debug("Opened file {}", new Object[] { PathContentProvider.this.filePath });
/*     */           }
/*     */         }
/* 137 */         this.buffer.clear();
/* 138 */         int read = this.channel.read(this.buffer);
/* 139 */         if (read < 0) {
/* 140 */           throw new NoSuchElementException();
/*     */         }
/* 142 */         if (PathContentProvider.LOG.isDebugEnabled()) {
/* 143 */           PathContentProvider.LOG.debug("Read {} bytes from {}", new Object[] { Integer.valueOf(read), PathContentProvider.this.filePath });
/*     */         }
/* 145 */         this.position += read;
/*     */         
/* 147 */         this.buffer.flip();
/* 148 */         return this.buffer;
/*     */       }
/*     */       catch (NoSuchElementException x)
/*     */       {
/* 152 */         close();
/* 153 */         throw x;
/*     */       }
/*     */       catch (Throwable x)
/*     */       {
/* 157 */         close();
/* 158 */         throw ((NoSuchElementException)new NoSuchElementException().initCause(x));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void close()
/*     */     {
/*     */       try
/*     */       {
/* 167 */         if ((PathContentProvider.this.bufferPool != null) && (this.buffer != null))
/* 168 */           PathContentProvider.this.bufferPool.release(this.buffer);
/* 169 */         if (this.channel != null) {
/* 170 */           this.channel.close();
/*     */         }
/*     */       }
/*     */       catch (Throwable x) {
/* 174 */         PathContentProvider.LOG.ignore(x);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\util\PathContentProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */