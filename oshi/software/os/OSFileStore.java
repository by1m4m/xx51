/*     */ package oshi.software.os;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class OSFileStore
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String name;
/*     */   private String volume;
/*     */   private String mount;
/*     */   private String description;
/*     */   private String fsType;
/*     */   private String uuid;
/*     */   private long usableSpace;
/*     */   private long totalSpace;
/*     */   
/*     */   public OSFileStore(String newName, String newVolume, String newMount, String newDescription, String newType, String newUuid, long newUsableSpace, long newTotalSpace)
/*     */   {
/*  72 */     setName(newName);
/*  73 */     setVolume(newVolume);
/*  74 */     setMount(newMount);
/*  75 */     setDescription(newDescription);
/*  76 */     setType(newType);
/*  77 */     setUUID(newUuid);
/*  78 */     setUsableSpace(newUsableSpace);
/*  79 */     setTotalSpace(newTotalSpace);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  88 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String value)
/*     */   {
/*  98 */     this.name = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getVolume()
/*     */   {
/* 107 */     return this.volume;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVolume(String value)
/*     */   {
/* 117 */     this.volume = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMount()
/*     */   {
/* 126 */     return this.mount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMount(String value)
/*     */   {
/* 136 */     this.mount = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 145 */     return this.description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescription(String value)
/*     */   {
/* 155 */     this.description = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 164 */     return this.fsType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setType(String value)
/*     */   {
/* 174 */     this.fsType = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUUID()
/*     */   {
/* 183 */     return this.uuid;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUUID(String value)
/*     */   {
/* 193 */     this.uuid = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getUsableSpace()
/*     */   {
/* 202 */     return this.usableSpace;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUsableSpace(long value)
/*     */   {
/* 212 */     this.usableSpace = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getTotalSpace()
/*     */   {
/* 221 */     return this.totalSpace;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTotalSpace(long value)
/*     */   {
/* 231 */     this.totalSpace = value;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\OSFileStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */