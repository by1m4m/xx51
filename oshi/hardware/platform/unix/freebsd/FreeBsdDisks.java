/*     */ package oshi.hardware.platform.unix.freebsd;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import oshi.hardware.Disks;
/*     */ import oshi.hardware.HWDiskStore;
/*     */ import oshi.hardware.HWPartition;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.ParseUtil;
/*     */ import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FreeBsdDisks
/*     */   implements Disks
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  46 */   private static final Pattern MOUNT_PATTERN = Pattern.compile("/dev/(\\S+p\\d+) on (\\S+) .*");
/*     */   
/*     */ 
/*  49 */   private static final Map<String, HWDiskStore> diskMap = new HashMap();
/*     */   
/*  51 */   private static final Map<String, String> mountMap = new HashMap();
/*     */   
/*     */ 
/*     */   public HWDiskStore[] getDisks()
/*     */   {
/*  56 */     mountMap.clear();
/*  57 */     for (String mnt : ExecutingCommand.runNative("mount")) {
/*  58 */       Matcher m = MOUNT_PATTERN.matcher(mnt);
/*  59 */       if (m.matches()) {
/*  60 */         mountMap.put(m.group(1), m.group(2));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  65 */     diskMap.clear();
/*  66 */     Object devices = Arrays.asList(BsdSysctlUtil.sysctl("kern.disks", "").split("\\s+"));
/*     */     
/*     */ 
/*  69 */     List<HWPartition> partList = new ArrayList();
/*     */     
/*     */ 
/*  72 */     List<String> disks = ExecutingCommand.runNative("iostat -Ix");
/*  73 */     long timeStamp = System.currentTimeMillis();
/*  74 */     for (String line : disks) {
/*  75 */       split = line.split("\\s+");
/*  76 */       if ((split.length >= 7) && (((List)devices).contains(split[0])))
/*     */       {
/*     */ 
/*  79 */         HWDiskStore store = new HWDiskStore();
/*  80 */         store.setName(split[0]);
/*  81 */         store.setReads(ParseUtil.parseDoubleOrDefault(split[1], 0.0D));
/*  82 */         store.setWrites(ParseUtil.parseDoubleOrDefault(split[2], 0.0D));
/*     */         
/*  84 */         store.setReadBytes((ParseUtil.parseDoubleOrDefault(split[3], 0.0D) * 1024.0D));
/*  85 */         store.setWriteBytes((ParseUtil.parseDoubleOrDefault(split[4], 0.0D) * 1024.0D));
/*     */         
/*  87 */         store.setTransferTime((ParseUtil.parseDoubleOrDefault(split[6], 0.0D) * 1000.0D));
/*  88 */         store.setTimeStamp(timeStamp);
/*  89 */         diskMap.put(split[0], store);
/*     */       }
/*     */     }
/*     */     
/*  93 */     Object geom = ExecutingCommand.runNative("geom disk list");
/*     */     
/*  95 */     HWDiskStore store = null;
/*  96 */     for (String[] split = ((List)geom).iterator(); split.hasNext();) { line = (String)split.next();
/*  97 */       if (line.startsWith("Geom name:"))
/*     */       {
/*  99 */         if (store != null) {
/* 100 */           setPartitions(store, partList);
/*     */         }
/* 102 */         String device = line.substring(line.lastIndexOf(' ') + 1);
/*     */         
/* 104 */         if (((List)devices).contains(device)) {
/* 105 */           store = (HWDiskStore)diskMap.get(device);
/*     */           
/*     */ 
/* 108 */           if (store == null) {
/* 109 */             store = new HWDiskStore();
/* 110 */             store.setName(device);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 116 */       if (store != null)
/*     */       {
/*     */ 
/* 119 */         line = line.trim();
/* 120 */         if (line.startsWith("Mediasize:")) {
/* 121 */           String[] split = line.split("\\s+");
/* 122 */           if (split.length > 1) {
/* 123 */             store.setSize(ParseUtil.parseLongOrDefault(split[1], 0L));
/*     */           }
/*     */         }
/* 126 */         if (line.startsWith("descr:")) {
/* 127 */           store.setModel(line.replace("descr:", "").trim());
/*     */         }
/* 129 */         if (line.startsWith("ident:")) {
/* 130 */           store.setSerial(line.replace("ident:", "").replace("(null)", "").trim());
/*     */         }
/*     */       }
/*     */     }
/*     */     String line;
/* 135 */     geom = ExecutingCommand.runNative("geom part list");
/* 136 */     store = null;
/* 137 */     HWPartition partition = null;
/* 138 */     for (String line : (List)geom) {
/* 139 */       line = line.trim();
/* 140 */       if (line.startsWith("Geom name:")) {
/* 141 */         String device = line.substring(line.lastIndexOf(' ') + 1);
/*     */         
/* 143 */         if (((List)devices).contains(device)) {
/* 144 */           store = (HWDiskStore)diskMap.get(device);
/*     */           
/*     */ 
/* 147 */           if (store == null) {
/* 148 */             store = new HWDiskStore();
/* 149 */             store.setName(device);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 155 */       if (store != null)
/*     */       {
/*     */ 
/* 158 */         if (line.contains("Name:"))
/*     */         {
/* 160 */           if (partition != null) {
/* 161 */             partList.add(partition);
/* 162 */             partition = null;
/*     */           }
/*     */           
/*     */ 
/* 166 */           String part = line.substring(line.lastIndexOf(' ') + 1);
/* 167 */           if (part.startsWith(store.getName()))
/*     */           {
/* 169 */             partition = new HWPartition();
/* 170 */             partition.setIdentification(part);
/* 171 */             partition.setName(part);
/* 172 */             partition.setMountPoint((String)mountMap.getOrDefault(part, ""));
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 177 */         if (partition != null)
/*     */         {
/*     */ 
/* 180 */           String[] split = line.split("\\s+");
/* 181 */           if (split.length >= 2)
/*     */           {
/*     */ 
/* 184 */             if (line.startsWith("Mediasize:")) {
/* 185 */               partition.setSize(ParseUtil.parseLongOrDefault(split[1], 0L));
/* 186 */             } else if (line.startsWith("rawuuid:")) {
/* 187 */               partition.setUuid(split[1]);
/* 188 */             } else if (line.startsWith("type:"))
/* 189 */               partition.setType(split[1]); }
/*     */         }
/*     */       }
/*     */     }
/* 193 */     if (store != null) {
/* 194 */       setPartitions(store, partList);
/*     */     }
/*     */     
/*     */ 
/* 198 */     List<HWDiskStore> diskList = new ArrayList(diskMap.keySet().size());
/* 199 */     diskList.addAll(diskMap.values());
/* 200 */     Collections.sort(diskList);
/*     */     
/* 202 */     return (HWDiskStore[])diskList.toArray(new HWDiskStore[diskList.size()]);
/*     */   }
/*     */   
/*     */   private void setPartitions(HWDiskStore store, List<HWPartition> partList) {
/* 206 */     HWPartition[] partitions = new HWPartition[partList.size()];
/* 207 */     int index = 0;
/* 208 */     Collections.sort(partList);
/* 209 */     for (HWPartition partition : partList)
/*     */     {
/*     */ 
/* 212 */       partition.setMinor(
/* 213 */         ParseUtil.parseIntOrDefault(ExecutingCommand.getFirstAnswer("stat -f %i /dev/" + partition.getName()), 0));
/* 214 */       partitions[(index++)] = partition;
/*     */     }
/* 216 */     partList.clear();
/* 217 */     store.setPartitions(partitions);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\freebsd\FreeBsdDisks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */