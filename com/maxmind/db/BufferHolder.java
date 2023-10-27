/*    */ package com.maxmind.db;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.RandomAccessFile;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.channels.FileChannel.MapMode;
/*    */ 
/*    */ final class BufferHolder
/*    */ {
/*    */   private final ByteBuffer buffer;
/*    */   
/*    */   BufferHolder(File database, Reader.FileMode mode)
/*    */     throws IOException
/*    */   {
/* 19 */     RandomAccessFile file = new RandomAccessFile(database, "r");
/* 20 */     boolean threw = true;
/*    */     try {
/* 22 */       FileChannel channel = file.getChannel();
/* 23 */       if (mode == Reader.FileMode.MEMORY) {
/* 24 */         this.buffer = ByteBuffer.wrap(new byte[(int)channel.size()]);
/* 25 */         if (channel.read(this.buffer) != this.buffer.capacity())
/*    */         {
/* 27 */           throw new IOException("Unable to read " + database.getName() + " into memory. Unexpected end of stream.");
/*    */         }
/*    */       }
/*    */       else {
/* 31 */         this.buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
/*    */       }
/* 33 */       threw = false; return;
/*    */     }
/*    */     finally {
/*    */       try {
/* 37 */         file.close();
/*    */ 
/*    */       }
/*    */       catch (IOException e)
/*    */       {
/* 42 */         if (!threw) {
/* 43 */           throw e;
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   BufferHolder(InputStream stream)
/*    */     throws IOException
/*    */   {
/* 57 */     if (null == stream) {
/* 58 */       throw new NullPointerException("Unable to use a NULL InputStream");
/*    */     }
/* 60 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 61 */     byte[] bytes = new byte['䀀'];
/*    */     int br;
/* 63 */     while (-1 != (br = stream.read(bytes))) {
/* 64 */       baos.write(bytes, 0, br);
/*    */     }
/* 66 */     this.buffer = ByteBuffer.wrap(baos.toByteArray());
/*    */   }
/*    */   
/*    */   BufferHolder(ByteBuffer buffer)
/*    */   {
/* 71 */     this.buffer = buffer;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   synchronized ByteBuffer get()
/*    */   {
/* 79 */     return this.buffer.duplicate();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\db\BufferHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */