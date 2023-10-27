/*     */ package oshi.hardware;
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
/*     */ public class HWPartition
/*     */   implements Serializable, Comparable<HWPartition>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String identification;
/*     */   private String name;
/*     */   private String type;
/*     */   private String uuid;
/*     */   private long size;
/*     */   private int major;
/*     */   private int minor;
/*     */   private String mountPoint;
/*     */   
/*     */   public HWPartition(String identification, String name, String type, String uuid, long size, int major, int minor, String mountPoint)
/*     */   {
/*  66 */     setIdentification(identification);
/*  67 */     setName(name);
/*  68 */     setType(type);
/*  69 */     setUuid(uuid);
/*  70 */     setSize(size);
/*  71 */     setMajor(major);
/*  72 */     setMinor(minor);
/*  73 */     setMountPoint(mountPoint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HWPartition()
/*     */   {
/*  80 */     this("", "", "", "", 0L, 0, 0, "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getIdentification()
/*     */   {
/*  87 */     return this.identification;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  94 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 101 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getUuid()
/*     */   {
/* 108 */     return this.uuid;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getSize()
/*     */   {
/* 115 */     return this.size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMajor()
/*     */   {
/* 122 */     return this.major;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMinor()
/*     */   {
/* 129 */     return this.minor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getMountPoint()
/*     */   {
/* 136 */     return this.mountPoint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIdentification(String identification)
/*     */   {
/* 144 */     this.identification = (identification == null ? "" : identification);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/* 152 */     this.name = (name == null ? "" : name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setType(String type)
/*     */   {
/* 160 */     this.type = (type == null ? "" : type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUuid(String uuid)
/*     */   {
/* 168 */     this.uuid = (uuid == null ? "" : uuid);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSize(long size)
/*     */   {
/* 176 */     this.size = size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMajor(int major)
/*     */   {
/* 184 */     this.major = major;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMinor(int minor)
/*     */   {
/* 192 */     this.minor = minor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMountPoint(String mountPoint)
/*     */   {
/* 200 */     this.mountPoint = (mountPoint == null ? "" : mountPoint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(HWPartition part)
/*     */   {
/* 209 */     return getIdentification().compareTo(part.getIdentification());
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 214 */     int prime = 31;
/* 215 */     int result = 1;
/* 216 */     result = 31 * result + (this.identification == null ? 0 : this.identification.hashCode());
/* 217 */     result = 31 * result + this.major;
/* 218 */     result = 31 * result + this.minor;
/* 219 */     result = 31 * result + (this.mountPoint == null ? 0 : this.mountPoint.hashCode());
/* 220 */     result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
/* 221 */     result = 31 * result + (int)(this.size ^ this.size >>> 32);
/* 222 */     result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
/* 223 */     result = 31 * result + (this.uuid == null ? 0 : this.uuid.hashCode());
/* 224 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 229 */     if (this == obj) {
/* 230 */       return true;
/*     */     }
/* 232 */     if (obj == null) {
/* 233 */       return false;
/*     */     }
/* 235 */     if (!(obj instanceof HWPartition)) {
/* 236 */       return false;
/*     */     }
/* 238 */     HWPartition other = (HWPartition)obj;
/* 239 */     if (this.identification == null) {
/* 240 */       if (other.identification != null) {
/* 241 */         return false;
/*     */       }
/* 243 */     } else if (!this.identification.equals(other.identification)) {
/* 244 */       return false;
/*     */     }
/* 246 */     if (this.major != other.major) {
/* 247 */       return false;
/*     */     }
/* 249 */     if (this.minor != other.minor) {
/* 250 */       return false;
/*     */     }
/* 252 */     if (this.mountPoint == null) {
/* 253 */       if (other.mountPoint != null) {
/* 254 */         return false;
/*     */       }
/* 256 */     } else if (!this.mountPoint.equals(other.mountPoint)) {
/* 257 */       return false;
/*     */     }
/* 259 */     if (this.name == null) {
/* 260 */       if (other.name != null) {
/* 261 */         return false;
/*     */       }
/* 263 */     } else if (!this.name.equals(other.name)) {
/* 264 */       return false;
/*     */     }
/* 266 */     if (this.size != other.size) {
/* 267 */       return false;
/*     */     }
/* 269 */     if (this.type == null) {
/* 270 */       if (other.type != null) {
/* 271 */         return false;
/*     */       }
/* 273 */     } else if (!this.type.equals(other.type)) {
/* 274 */       return false;
/*     */     }
/* 276 */     if (this.uuid == null) {
/* 277 */       if (other.uuid != null) {
/* 278 */         return false;
/*     */       }
/* 280 */     } else if (!this.uuid.equals(other.uuid)) {
/* 281 */       return false;
/*     */     }
/* 283 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\HWPartition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */