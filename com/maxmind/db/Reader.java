/*     */ package com.maxmind.db;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Reader
/*     */   implements Closeable
/*     */ {
/*     */   private static final int DATA_SECTION_SEPARATOR_SIZE = 16;
/*  19 */   private static final byte[] METADATA_START_MARKER = { -85, -51, -17, 77, 97, 120, 77, 105, 110, 100, 46, 99, 111, 109 };
/*     */   
/*     */ 
/*     */ 
/*     */   private final int ipV4Start;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Metadata metadata;
/*     */   
/*     */ 
/*     */   private final AtomicReference<BufferHolder> bufferHolderReference;
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum FileMode
/*     */   {
/*  36 */     MEMORY_MAPPED, 
/*     */     
/*     */ 
/*     */ 
/*  40 */     MEMORY;
/*     */     
/*     */ 
/*     */ 
/*     */     private FileMode() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public Reader(File database)
/*     */     throws IOException
/*     */   {
/*  51 */     this(database, FileMode.MEMORY_MAPPED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Reader(InputStream source)
/*     */     throws IOException
/*     */   {
/*  62 */     this(new BufferHolder(source), "<InputStream>");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Reader(File database, FileMode fileMode)
/*     */     throws IOException
/*     */   {
/*  74 */     this(new BufferHolder(database, fileMode), database.getName());
/*     */   }
/*     */   
/*     */   private Reader(BufferHolder bufferHolder, String name) throws IOException {
/*  78 */     this.bufferHolderReference = new AtomicReference(bufferHolder);
/*     */     
/*     */ 
/*  81 */     ByteBuffer buffer = bufferHolder.get();
/*  82 */     int start = findMetadataStart(buffer, name);
/*     */     
/*  84 */     Decoder metadataDecoder = new Decoder(buffer, start);
/*  85 */     this.metadata = new Metadata(metadataDecoder.decode(start).getNode());
/*     */     
/*  87 */     this.ipV4Start = findIpV4StartNode(buffer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode get(InetAddress ipAddress)
/*     */     throws IOException
/*     */   {
/*  98 */     ByteBuffer buffer = getBufferHolder().get();
/*  99 */     int pointer = findAddressInTree(buffer, ipAddress);
/* 100 */     if (pointer == 0) {
/* 101 */       return null;
/*     */     }
/* 103 */     return resolveDataPointer(buffer, pointer);
/*     */   }
/*     */   
/*     */   private BufferHolder getBufferHolder() throws ClosedDatabaseException {
/* 107 */     BufferHolder bufferHolder = (BufferHolder)this.bufferHolderReference.get();
/* 108 */     if (bufferHolder == null) {
/* 109 */       throw new ClosedDatabaseException();
/*     */     }
/* 111 */     return bufferHolder;
/*     */   }
/*     */   
/*     */   private int findAddressInTree(ByteBuffer buffer, InetAddress address) throws InvalidDatabaseException
/*     */   {
/* 116 */     byte[] rawAddress = address.getAddress();
/*     */     
/* 118 */     int bitLength = rawAddress.length * 8;
/* 119 */     int record = startNode(bitLength);
/*     */     
/* 121 */     for (int i = 0; i < bitLength; i++) {
/* 122 */       if (record >= this.metadata.getNodeCount()) {
/*     */         break;
/*     */       }
/* 125 */       int b = 0xFF & rawAddress[(i / 8)];
/* 126 */       int bit = 0x1 & b >> 7 - i % 8;
/* 127 */       record = readNode(buffer, record, bit);
/*     */     }
/* 129 */     if (record == this.metadata.getNodeCount())
/*     */     {
/* 131 */       return 0; }
/* 132 */     if (record > this.metadata.getNodeCount())
/*     */     {
/* 134 */       return record;
/*     */     }
/* 136 */     throw new InvalidDatabaseException("Something bad happened");
/*     */   }
/*     */   
/*     */ 
/*     */   private int startNode(int bitLength)
/*     */   {
/* 142 */     if ((this.metadata.getIpVersion() == 6) && (bitLength == 32)) {
/* 143 */       return this.ipV4Start;
/*     */     }
/*     */     
/*     */ 
/* 147 */     return 0;
/*     */   }
/*     */   
/*     */   private int findIpV4StartNode(ByteBuffer buffer) throws InvalidDatabaseException
/*     */   {
/* 152 */     if (this.metadata.getIpVersion() == 4) {
/* 153 */       return 0;
/*     */     }
/*     */     
/* 156 */     int node = 0;
/* 157 */     for (int i = 0; (i < 96) && (node < this.metadata.getNodeCount()); i++) {
/* 158 */       node = readNode(buffer, node, 0);
/*     */     }
/* 160 */     return node;
/*     */   }
/*     */   
/*     */   private int readNode(ByteBuffer buffer, int nodeNumber, int index) throws InvalidDatabaseException
/*     */   {
/* 165 */     int baseOffset = nodeNumber * this.metadata.getNodeByteSize();
/*     */     
/* 167 */     switch (this.metadata.getRecordSize()) {
/*     */     case 24: 
/* 169 */       buffer.position(baseOffset + index * 3);
/* 170 */       return Decoder.decodeInteger(buffer, 0, 3);
/*     */     case 28: 
/* 172 */       int middle = buffer.get(baseOffset + 3);
/*     */       
/* 174 */       if (index == 0) {
/* 175 */         middle = (0xF0 & middle) >>> 4;
/*     */       } else {
/* 177 */         middle = 0xF & middle;
/*     */       }
/* 179 */       buffer.position(baseOffset + index * 4);
/* 180 */       return Decoder.decodeInteger(buffer, middle, 3);
/*     */     case 32: 
/* 182 */       buffer.position(baseOffset + index * 4);
/* 183 */       return Decoder.decodeInteger(buffer, 0, 4);
/*     */     }
/*     */     
/* 186 */     throw new InvalidDatabaseException("Unknown record size: " + this.metadata.getRecordSize());
/*     */   }
/*     */   
/*     */ 
/*     */   private JsonNode resolveDataPointer(ByteBuffer buffer, int pointer)
/*     */     throws IOException
/*     */   {
/* 193 */     int resolved = pointer - this.metadata.getNodeCount() + this.metadata.getSearchTreeSize();
/*     */     
/* 195 */     if (resolved >= buffer.capacity()) {
/* 196 */       throw new InvalidDatabaseException("The MaxMind DB file's search tree is corrupt: contains pointer larger than the database.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 203 */     Decoder decoder = new Decoder(buffer, this.metadata.getSearchTreeSize() + 16);
/*     */     
/* 205 */     return decoder.decode(resolved).getNode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int findMetadataStart(ByteBuffer buffer, String databaseName)
/*     */     throws InvalidDatabaseException
/*     */   {
/* 218 */     int fileSize = buffer.capacity();
/*     */     
/*     */     label80:
/* 221 */     for (int i = 0; i < fileSize - METADATA_START_MARKER.length + 1; i++) {
/* 222 */       for (int j = 0; j < METADATA_START_MARKER.length; j++) {
/* 223 */         byte b = buffer.get(fileSize - i - j - 1);
/* 224 */         if (b != METADATA_START_MARKER[(METADATA_START_MARKER.length - j - 1)]) {
/*     */           break label80;
/*     */         }
/*     */       }
/*     */       
/* 229 */       return fileSize - i;
/*     */     }
/* 231 */     throw new InvalidDatabaseException("Could not find a MaxMind DB metadata marker in this file (" + databaseName + "). Is this a valid MaxMind DB file?");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Metadata getMetadata()
/*     */   {
/* 240 */     return this.metadata;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 250 */     this.bufferHolderReference.set(null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\db\Reader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */