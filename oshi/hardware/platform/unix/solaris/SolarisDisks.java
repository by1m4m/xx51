/*     */ package oshi.hardware.platform.unix.solaris;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import oshi.hardware.Disks;
/*     */ import oshi.hardware.HWDiskStore;
/*     */ import oshi.hardware.HWPartition;
/*     */ import oshi.jna.platform.unix.solaris.LibKstat.Kstat;
/*     */ import oshi.jna.platform.unix.solaris.LibKstat.KstatIO;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.ParseUtil;
/*     */ import oshi.util.platform.unix.solaris.KstatUtil;
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
/*     */ public class SolarisDisks
/*     */   implements Disks
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public HWDiskStore[] getDisks()
/*     */   {
/*  48 */     Map<String, HWDiskStore> diskMap = new HashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */     List<String> disks = ExecutingCommand.runNative("iostat -er");
/*     */     
/*     */ 
/*     */ 
/*  58 */     Map<String, String> deviceMap = new HashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */     List<String> mountpoints = ExecutingCommand.runNative("iostat -ern");
/*     */     HWDiskStore store;
/*  67 */     for (int i = 0; (i < disks.size()) && (i < mountpoints.size()); i++)
/*     */     {
/*  69 */       String disk = (String)disks.get(i);
/*  70 */       String[] diskSplit = disk.split(",");
/*  71 */       if ((diskSplit.length >= 5) && (!"device".equals(diskSplit[0])))
/*     */       {
/*     */ 
/*  74 */         store = new HWDiskStore();
/*  75 */         store.setName(diskSplit[0]);
/*  76 */         diskMap.put(diskSplit[0], store);
/*     */         
/*  78 */         String mount = (String)mountpoints.get(i);
/*  79 */         String[] mountSplit = mount.split(",");
/*  80 */         if ((mountSplit.length >= 5) && (!"device".equals(mountSplit[4])))
/*     */         {
/*     */ 
/*  83 */           deviceMap.put(diskSplit[0], mountSplit[4]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  88 */     Map<String, Integer> majorMap = new HashMap();
/*     */     
/*     */ 
/*  91 */     List<String> lshal = ExecutingCommand.runNative("lshal");
/*  92 */     String disk = "";
/*  93 */     for (String line : lshal) {
/*  94 */       if (line.startsWith("udi ")) {
/*  95 */         String udi = ParseUtil.getSingleQuoteStringValue(line);
/*  96 */         disk = udi.substring(udi.lastIndexOf('/') + 1);
/*     */       }
/*     */       else {
/*  99 */         line = line.trim();
/* 100 */         if (line.startsWith("block.major")) {
/* 101 */           majorMap.put(disk, Integer.valueOf(ParseUtil.getFirstIntValue(line)));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 106 */     disks = ExecutingCommand.runNative("iostat -Er");
/*     */     
/* 108 */     disk = "";
/* 109 */     String model = "";
/* 110 */     String vendor = "";
/* 111 */     String product = "";
/* 112 */     String serial = "";
/* 113 */     long size = 0L;
/* 114 */     for (String line : disks)
/*     */     {
/*     */ 
/*     */ 
/* 118 */       split = line.split(",");
/* 119 */       for (String keyValue : split) {
/* 120 */         keyValue = keyValue.trim();
/*     */         
/*     */ 
/* 123 */         if (diskMap.keySet().contains(keyValue))
/*     */         {
/*     */ 
/* 126 */           if (!disk.isEmpty()) {
/* 127 */             updateStore((HWDiskStore)diskMap.get(disk), model, vendor, product, serial, size, (String)deviceMap.get(disk), 
/* 128 */               ((Integer)majorMap.getOrDefault(disk, Integer.valueOf(0))).intValue());
/*     */           }
/*     */           
/* 131 */           disk = keyValue;
/* 132 */           model = "";
/* 133 */           vendor = "";
/* 134 */           product = "";
/* 135 */           serial = "";
/* 136 */           size = 0L;
/*     */ 
/*     */ 
/*     */         }
/* 140 */         else if (keyValue.startsWith("Model:")) {
/* 141 */           model = keyValue.replace("Model:", "").trim();
/* 142 */         } else if (keyValue.startsWith("Serial No:")) {
/* 143 */           serial = keyValue.replace("Serial No:", "").trim();
/* 144 */         } else if (keyValue.startsWith("Vendor:")) {
/* 145 */           vendor = keyValue.replace("Vendor:", "").trim();
/* 146 */         } else if (keyValue.startsWith("Product:")) {
/* 147 */           product = keyValue.replace("Product:", "").trim();
/* 148 */         } else if (keyValue.startsWith("Size:"))
/*     */         {
/* 150 */           String[] bytes = keyValue.split("<");
/* 151 */           if (bytes.length > 1) {
/* 152 */             bytes = bytes[1].split("\\s+");
/* 153 */             size = ParseUtil.parseLongOrDefault(bytes[0], 0L);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 158 */       if (!disk.isEmpty()) {
/* 159 */         updateStore((HWDiskStore)diskMap.get(disk), model, vendor, product, serial, size, (String)deviceMap.get(disk), 
/* 160 */           ((Integer)majorMap.getOrDefault(disk, Integer.valueOf(0))).intValue());
/*     */       }
/*     */     }
/*     */     
/*     */     String[] split;
/*     */     
/* 166 */     HWDiskStore[] results = new HWDiskStore[diskMap.keySet().size()];
/* 167 */     int index = 0;
/* 168 */     for (Object entry : diskMap.entrySet()) {
/* 169 */       LibKstat.Kstat ksp = KstatUtil.kstatLookup(null, 0, (String)((Map.Entry)entry).getKey());
/* 170 */       if ((ksp != null) && (KstatUtil.kstatRead(ksp))) {
/* 171 */         LibKstat.KstatIO data = new LibKstat.KstatIO(ksp.ks_data);
/* 172 */         ((HWDiskStore)((Map.Entry)entry).getValue()).setReads(data.reads);
/* 173 */         ((HWDiskStore)((Map.Entry)entry).getValue()).setWrites(data.writes);
/* 174 */         ((HWDiskStore)((Map.Entry)entry).getValue()).setReadBytes(data.nread);
/* 175 */         ((HWDiskStore)((Map.Entry)entry).getValue()).setWriteBytes(data.nwritten);
/*     */         
/* 177 */         ((HWDiskStore)((Map.Entry)entry).getValue()).setTransferTime(data.rtime / 1000000L);
/* 178 */         ((HWDiskStore)((Map.Entry)entry).getValue()).setTimeStamp(ksp.ks_snaptime / 1000000L);
/*     */       }
/* 180 */       results[(index++)] = ((HWDiskStore)((Map.Entry)entry).getValue());
/*     */     }
/*     */     
/* 183 */     return results;
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
/*     */   private void updateStore(HWDiskStore store, String model, String vendor, String product, String serial, long size, String mount, int major)
/*     */   {
/* 209 */     store.setModel(model.isEmpty() ? (vendor + " " + product).trim() : model);
/* 210 */     store.setSerial(serial);
/* 211 */     store.setSize(size);
/*     */     
/*     */ 
/* 214 */     List<HWPartition> partList = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 219 */     List<String> prtvotc = ExecutingCommand.runNative("prtvtoc /dev/dsk/" + mount);
/*     */     
/* 221 */     if (prtvotc.size() > 1) {
/* 222 */       int bytesPerSector = 0;
/*     */       
/*     */ 
/* 225 */       for (String line : prtvotc)
/*     */       {
/*     */ 
/* 228 */         if (line.startsWith("*")) {
/* 229 */           if (line.endsWith("bytes/sector")) {
/* 230 */             String[] split = line.split("\\s+");
/* 231 */             if (split.length > 0) {
/* 232 */               bytesPerSector = ParseUtil.parseIntOrDefault(split[1], 0);
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 239 */         else if (bytesPerSector != 0)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 245 */           String[] split = line.trim().split("\\s+");
/*     */           
/* 247 */           if ((split.length >= 6) && (!"2".equals(split[0])))
/*     */           {
/*     */ 
/* 250 */             HWPartition partition = new HWPartition();
/*     */             
/* 252 */             partition.setIdentification(mount + "s" + split[0]);
/* 253 */             partition.setMajor(major);
/* 254 */             partition.setMinor(ParseUtil.parseIntOrDefault(split[0], 0));
/*     */             
/* 256 */             switch (ParseUtil.parseIntOrDefault(split[1], 0)) {
/*     */             case 1: 
/*     */             case 24: 
/* 259 */               partition.setName("boot");
/* 260 */               break;
/*     */             case 2: 
/* 262 */               partition.setName("root");
/* 263 */               break;
/*     */             case 3: 
/* 265 */               partition.setName("swap");
/* 266 */               break;
/*     */             case 4: 
/* 268 */               partition.setName("usr");
/* 269 */               break;
/*     */             case 5: 
/* 271 */               partition.setName("backup");
/* 272 */               break;
/*     */             case 6: 
/* 274 */               partition.setName("stand");
/* 275 */               break;
/*     */             case 7: 
/* 277 */               partition.setName("var");
/* 278 */               break;
/*     */             case 8: 
/* 280 */               partition.setName("home");
/* 281 */               break;
/*     */             case 9: 
/* 283 */               partition.setName("altsctr");
/* 284 */               break;
/*     */             case 10: 
/* 286 */               partition.setName("cache");
/* 287 */               break;
/*     */             case 11: 
/* 289 */               partition.setName("reserved");
/* 290 */               break;
/*     */             case 12: 
/* 292 */               partition.setName("system");
/* 293 */               break;
/*     */             case 14: 
/* 295 */               partition.setName("public region");
/* 296 */               break;
/*     */             case 15: 
/* 298 */               partition.setName("private region");
/* 299 */               break;
/*     */             case 13: case 16: case 17: case 18: case 19: case 20: case 21: case 22: case 23: default: 
/* 301 */               partition.setName("unknown");
/*     */             }
/*     */             
/*     */             
/*     */ 
/* 306 */             switch (split[2]) {
/*     */             case "00": 
/* 308 */               partition.setType("wm");
/* 309 */               break;
/*     */             case "10": 
/* 311 */               partition.setType("rm");
/* 312 */               break;
/*     */             case "01": 
/* 314 */               partition.setType("wu");
/* 315 */               break;
/*     */             default: 
/* 317 */               partition.setType("ru");
/*     */             }
/*     */             
/*     */             
/* 321 */             partition.setSize(bytesPerSector * ParseUtil.parseLongOrDefault(split[4], 0L));
/*     */             
/* 323 */             if (split.length > 6) {
/* 324 */               partition.setMountPoint(split[6]);
/*     */             }
/* 326 */             partList.add(partition);
/*     */           } } }
/* 328 */       store.setPartitions((HWPartition[])partList.toArray(new HWPartition[partList.size()]));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\solaris\SolarisDisks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */