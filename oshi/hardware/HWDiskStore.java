/*     */ package oshi.hardware;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
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
/*     */ public class HWDiskStore
/*     */   implements Serializable, Comparable<HWDiskStore>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String model;
/*     */   private String name;
/*     */   private String serial;
/*     */   private long size;
/*     */   private long reads;
/*     */   private long readBytes;
/*     */   private long writes;
/*     */   private long writeBytes;
/*     */   private long transferTime;
/*     */   private HWPartition[] partitions;
/*     */   private long timeStamp;
/*     */   
/*     */   public HWDiskStore()
/*     */   {
/*  54 */     this("", "", "", 0L, 0L, 0L, 0L, 0L, 0L, new HWPartition[0], 0L);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HWDiskStore(String name, String model, String serial, long size, long reads, long readBytes, long writes, long writeBytes, long transferTime, HWPartition[] partitions, long timeStamp)
/*     */   {
/*  85 */     setName(name);
/*  86 */     setModel(model);
/*  87 */     setSerial(serial);
/*  88 */     setSize(size);
/*  89 */     setReads(reads);
/*  90 */     setReadBytes(readBytes);
/*  91 */     setWrites(writes);
/*  92 */     setWriteBytes(writeBytes);
/*  93 */     setTransferTime(transferTime);
/*  94 */     setPartitions(partitions);
/*  95 */     setTimeStamp(timeStamp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 102 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getModel()
/*     */   {
/* 109 */     return this.model;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSerial()
/*     */   {
/* 116 */     return this.serial;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getSize()
/*     */   {
/* 123 */     return this.size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getReads()
/*     */   {
/* 130 */     return this.reads;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getReadBytes()
/*     */   {
/* 137 */     return this.readBytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getWrites()
/*     */   {
/* 144 */     return this.writes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getWriteBytes()
/*     */   {
/* 151 */     return this.writeBytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getTransferTime()
/*     */   {
/* 158 */     return this.transferTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HWPartition[] getPartitions()
/*     */   {
/* 165 */     return this.partitions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getTimeStamp()
/*     */   {
/* 172 */     return this.timeStamp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/* 180 */     this.name = (name == null ? "" : name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setModel(String model)
/*     */   {
/* 188 */     this.model = (model == null ? "" : model);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSerial(String serial)
/*     */   {
/* 196 */     this.serial = (serial == null ? "" : serial);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSize(long size)
/*     */   {
/* 204 */     this.size = size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReads(long reads)
/*     */   {
/* 212 */     this.reads = reads;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReadBytes(long readBytes)
/*     */   {
/* 220 */     this.readBytes = readBytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWrites(long writes)
/*     */   {
/* 228 */     this.writes = writes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWriteBytes(long writeBytes)
/*     */   {
/* 236 */     this.writeBytes = writeBytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTransferTime(long transferTime)
/*     */   {
/* 244 */     this.transferTime = transferTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPartitions(HWPartition[] partitions)
/*     */   {
/* 252 */     this.partitions = partitions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeStamp(long timeStamp)
/*     */   {
/* 260 */     this.timeStamp = timeStamp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(HWDiskStore store)
/*     */   {
/* 269 */     return getName().compareTo(store.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 277 */     int prime = 31;
/* 278 */     int result = 1;
/* 279 */     result = 31 * result + (this.model == null ? 0 : this.model.hashCode());
/* 280 */     result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
/* 281 */     result = 31 * result + Arrays.hashCode(this.partitions);
/* 282 */     result = 31 * result + (this.serial == null ? 0 : this.serial.hashCode());
/* 283 */     result = 31 * result + (int)(this.size ^ this.size >>> 32);
/* 284 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 292 */     if (this == obj) {
/* 293 */       return true;
/*     */     }
/* 295 */     if (obj == null) {
/* 296 */       return false;
/*     */     }
/* 298 */     if (!(obj instanceof HWDiskStore)) {
/* 299 */       return false;
/*     */     }
/* 301 */     HWDiskStore other = (HWDiskStore)obj;
/* 302 */     if (this.model == null) {
/* 303 */       if (other.model != null) {
/* 304 */         return false;
/*     */       }
/* 306 */     } else if (!this.model.equals(other.model)) {
/* 307 */       return false;
/*     */     }
/* 309 */     if (this.name == null) {
/* 310 */       if (other.name != null) {
/* 311 */         return false;
/*     */       }
/* 313 */     } else if (!this.name.equals(other.name)) {
/* 314 */       return false;
/*     */     }
/* 316 */     if (!Arrays.equals(this.partitions, other.partitions)) {
/* 317 */       return false;
/*     */     }
/* 319 */     if (this.serial == null) {
/* 320 */       if (other.serial != null) {
/* 321 */         return false;
/*     */       }
/* 323 */     } else if (!this.serial.equals(other.serial)) {
/* 324 */       return false;
/*     */     }
/* 326 */     return this.size == other.size;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\HWDiskStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */