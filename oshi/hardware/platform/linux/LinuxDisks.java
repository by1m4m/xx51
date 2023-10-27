/*     */ package oshi.hardware.platform.linux;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.Disks;
/*     */ import oshi.hardware.HWDiskStore;
/*     */ import oshi.jna.platform.linux.Udev.UdevDevice;
/*     */ import oshi.util.FileUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinuxDisks
/*     */   implements Disks
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  45 */   private static final Logger LOG = LoggerFactory.getLogger(LinuxDisks.class);
/*     */   
/*     */   private static final int SECTORSIZE = 512;
/*     */   
/*  49 */   private final Map<String, String> mountsMap = new HashMap();
/*     */   
/*     */   /* Error */
/*     */   public HWDiskStore[] getDisks()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_1
/*     */     //   2: aload_0
/*     */     //   3: invokespecial 5	oshi/hardware/platform/linux/LinuxDisks:updateMountsMap	()V
/*     */     //   6: aconst_null
/*     */     //   7: astore_3
/*     */     //   8: aconst_null
/*     */     //   9: astore 4
/*     */     //   11: aconst_null
/*     */     //   12: astore 5
/*     */     //   14: new 6	java/util/ArrayList
/*     */     //   17: dup
/*     */     //   18: invokespecial 7	java/util/ArrayList:<init>	()V
/*     */     //   21: astore_2
/*     */     //   22: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   25: invokeinterface 9 1 0
/*     */     //   30: astore_3
/*     */     //   31: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   34: aload_3
/*     */     //   35: invokeinterface 10 2 0
/*     */     //   40: astore 5
/*     */     //   42: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   45: aload 5
/*     */     //   47: ldc 11
/*     */     //   49: invokeinterface 12 3 0
/*     */     //   54: pop
/*     */     //   55: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   58: aload 5
/*     */     //   60: invokeinterface 13 2 0
/*     */     //   65: pop
/*     */     //   66: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   69: aload 5
/*     */     //   71: invokeinterface 14 2 0
/*     */     //   76: astore 6
/*     */     //   78: aload 6
/*     */     //   80: astore 7
/*     */     //   82: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   85: aload_3
/*     */     //   86: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   89: aload 6
/*     */     //   91: invokeinterface 15 2 0
/*     */     //   96: invokeinterface 16 3 0
/*     */     //   101: astore 4
/*     */     //   103: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   106: aload 4
/*     */     //   108: invokeinterface 17 2 0
/*     */     //   113: ldc 18
/*     */     //   115: invokevirtual 19	java/lang/String:startsWith	(Ljava/lang/String;)Z
/*     */     //   118: ifne +413 -> 531
/*     */     //   121: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   124: aload 4
/*     */     //   126: invokeinterface 17 2 0
/*     */     //   131: ldc 20
/*     */     //   133: invokevirtual 19	java/lang/String:startsWith	(Ljava/lang/String;)Z
/*     */     //   136: ifeq +6 -> 142
/*     */     //   139: goto +392 -> 531
/*     */     //   142: ldc 21
/*     */     //   144: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   147: aload 4
/*     */     //   149: invokeinterface 22 2 0
/*     */     //   154: invokevirtual 23	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   157: ifeq +147 -> 304
/*     */     //   160: new 24	oshi/hardware/HWDiskStore
/*     */     //   163: dup
/*     */     //   164: invokespecial 25	oshi/hardware/HWDiskStore:<init>	()V
/*     */     //   167: astore_1
/*     */     //   168: aload_1
/*     */     //   169: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   172: aload 4
/*     */     //   174: invokeinterface 17 2 0
/*     */     //   179: invokevirtual 26	oshi/hardware/HWDiskStore:setName	(Ljava/lang/String;)V
/*     */     //   182: aload_1
/*     */     //   183: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   186: aload 4
/*     */     //   188: ldc 27
/*     */     //   190: invokeinterface 28 3 0
/*     */     //   195: ifnonnull +8 -> 203
/*     */     //   198: ldc 29
/*     */     //   200: goto +15 -> 215
/*     */     //   203: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   206: aload 4
/*     */     //   208: ldc 27
/*     */     //   210: invokeinterface 28 3 0
/*     */     //   215: invokevirtual 30	oshi/hardware/HWDiskStore:setModel	(Ljava/lang/String;)V
/*     */     //   218: aload_1
/*     */     //   219: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   222: aload 4
/*     */     //   224: ldc 31
/*     */     //   226: invokeinterface 28 3 0
/*     */     //   231: ifnonnull +8 -> 239
/*     */     //   234: ldc 29
/*     */     //   236: goto +15 -> 251
/*     */     //   239: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   242: aload 4
/*     */     //   244: ldc 31
/*     */     //   246: invokeinterface 28 3 0
/*     */     //   251: invokevirtual 32	oshi/hardware/HWDiskStore:setSerial	(Ljava/lang/String;)V
/*     */     //   254: aload_1
/*     */     //   255: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   258: aload 4
/*     */     //   260: ldc 33
/*     */     //   262: invokeinterface 34 3 0
/*     */     //   267: lconst_0
/*     */     //   268: invokestatic 35	oshi/util/ParseUtil:parseLongOrDefault	(Ljava/lang/String;J)J
/*     */     //   271: ldc2_w 37
/*     */     //   274: lmul
/*     */     //   275: invokevirtual 39	oshi/hardware/HWDiskStore:setSize	(J)V
/*     */     //   278: aload_1
/*     */     //   279: iconst_0
/*     */     //   280: anewarray 40	oshi/hardware/HWPartition
/*     */     //   283: invokevirtual 41	oshi/hardware/HWDiskStore:setPartitions	([Loshi/hardware/HWPartition;)V
/*     */     //   286: aload_0
/*     */     //   287: aload_1
/*     */     //   288: aload 4
/*     */     //   290: invokespecial 42	oshi/hardware/platform/linux/LinuxDisks:computeDiskStats	(Loshi/hardware/HWDiskStore;Loshi/jna/platform/linux/Udev$UdevDevice;)V
/*     */     //   293: aload_2
/*     */     //   294: aload_1
/*     */     //   295: invokeinterface 43 2 0
/*     */     //   300: pop
/*     */     //   301: goto +230 -> 531
/*     */     //   304: ldc 44
/*     */     //   306: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   309: aload 4
/*     */     //   311: invokeinterface 22 2 0
/*     */     //   316: invokevirtual 23	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   319: ifeq +212 -> 531
/*     */     //   322: aload_1
/*     */     //   323: ifnull +208 -> 531
/*     */     //   326: aload_1
/*     */     //   327: invokevirtual 45	oshi/hardware/HWDiskStore:getPartitions	()[Loshi/hardware/HWPartition;
/*     */     //   330: arraylength
/*     */     //   331: iconst_1
/*     */     //   332: iadd
/*     */     //   333: anewarray 40	oshi/hardware/HWPartition
/*     */     //   336: astore 8
/*     */     //   338: aload_1
/*     */     //   339: invokevirtual 45	oshi/hardware/HWDiskStore:getPartitions	()[Loshi/hardware/HWPartition;
/*     */     //   342: iconst_0
/*     */     //   343: aload 8
/*     */     //   345: iconst_0
/*     */     //   346: aload_1
/*     */     //   347: invokevirtual 45	oshi/hardware/HWDiskStore:getPartitions	()[Loshi/hardware/HWPartition;
/*     */     //   350: arraylength
/*     */     //   351: invokestatic 46	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
/*     */     //   354: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   357: aload 4
/*     */     //   359: invokeinterface 17 2 0
/*     */     //   364: astore 9
/*     */     //   366: aload 8
/*     */     //   368: aload 8
/*     */     //   370: arraylength
/*     */     //   371: iconst_1
/*     */     //   372: isub
/*     */     //   373: new 40	oshi/hardware/HWPartition
/*     */     //   376: dup
/*     */     //   377: aload 9
/*     */     //   379: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   382: aload 4
/*     */     //   384: invokeinterface 47 2 0
/*     */     //   389: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   392: aload 4
/*     */     //   394: ldc 48
/*     */     //   396: invokeinterface 28 3 0
/*     */     //   401: ifnonnull +8 -> 409
/*     */     //   404: ldc 44
/*     */     //   406: goto +15 -> 421
/*     */     //   409: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   412: aload 4
/*     */     //   414: ldc 48
/*     */     //   416: invokeinterface 28 3 0
/*     */     //   421: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   424: aload 4
/*     */     //   426: ldc 49
/*     */     //   428: invokeinterface 28 3 0
/*     */     //   433: ifnonnull +8 -> 441
/*     */     //   436: ldc 50
/*     */     //   438: goto +15 -> 453
/*     */     //   441: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   444: aload 4
/*     */     //   446: ldc 49
/*     */     //   448: invokeinterface 28 3 0
/*     */     //   453: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   456: aload 4
/*     */     //   458: ldc 33
/*     */     //   460: invokeinterface 34 3 0
/*     */     //   465: lconst_0
/*     */     //   466: invokestatic 35	oshi/util/ParseUtil:parseLongOrDefault	(Ljava/lang/String;J)J
/*     */     //   469: ldc2_w 37
/*     */     //   472: lmul
/*     */     //   473: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   476: aload 4
/*     */     //   478: ldc 51
/*     */     //   480: invokeinterface 28 3 0
/*     */     //   485: iconst_0
/*     */     //   486: invokestatic 52	oshi/util/ParseUtil:parseIntOrDefault	(Ljava/lang/String;I)I
/*     */     //   489: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   492: aload 4
/*     */     //   494: ldc 53
/*     */     //   496: invokeinterface 28 3 0
/*     */     //   501: iconst_0
/*     */     //   502: invokestatic 52	oshi/util/ParseUtil:parseIntOrDefault	(Ljava/lang/String;I)I
/*     */     //   505: aload_0
/*     */     //   506: getfield 4	oshi/hardware/platform/linux/LinuxDisks:mountsMap	Ljava/util/Map;
/*     */     //   509: aload 9
/*     */     //   511: ldc 50
/*     */     //   513: invokeinterface 54 3 0
/*     */     //   518: checkcast 55	java/lang/String
/*     */     //   521: invokespecial 56	oshi/hardware/HWPartition:<init>	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JIILjava/lang/String;)V
/*     */     //   524: aastore
/*     */     //   525: aload_1
/*     */     //   526: aload 8
/*     */     //   528: invokevirtual 41	oshi/hardware/HWDiskStore:setPartitions	([Loshi/hardware/HWPartition;)V
/*     */     //   531: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   534: aload 7
/*     */     //   536: invokeinterface 57 2 0
/*     */     //   541: astore 6
/*     */     //   543: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   546: aload 4
/*     */     //   548: invokeinterface 58 2 0
/*     */     //   553: goto +43 -> 596
/*     */     //   556: astore 8
/*     */     //   558: getstatic 60	oshi/hardware/platform/linux/LinuxDisks:LOG	Lorg/slf4j/Logger;
/*     */     //   561: ldc 61
/*     */     //   563: invokeinterface 62 2 0
/*     */     //   568: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   571: aload 4
/*     */     //   573: invokeinterface 58 2 0
/*     */     //   578: goto +21 -> 599
/*     */     //   581: astore 10
/*     */     //   583: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   586: aload 4
/*     */     //   588: invokeinterface 58 2 0
/*     */     //   593: aload 10
/*     */     //   595: athrow
/*     */     //   596: goto -518 -> 78
/*     */     //   599: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   602: aload 5
/*     */     //   604: invokeinterface 63 2 0
/*     */     //   609: getstatic 8	oshi/jna/platform/linux/Udev:INSTANCE	Loshi/jna/platform/linux/Udev;
/*     */     //   612: aload_3
/*     */     //   613: invokeinterface 64 2 0
/*     */     //   618: aload_2
/*     */     //   619: aload_2
/*     */     //   620: invokeinterface 65 1 0
/*     */     //   625: anewarray 24	oshi/hardware/HWDiskStore
/*     */     //   628: invokeinterface 66 2 0
/*     */     //   633: checkcast 67	[Loshi/hardware/HWDiskStore;
/*     */     //   636: areturn
/*     */     // Line number table:
/*     */     //   Java source line #53	-> byte code offset #0
/*     */     //   Java source line #56	-> byte code offset #2
/*     */     //   Java source line #58	-> byte code offset #6
/*     */     //   Java source line #59	-> byte code offset #8
/*     */     //   Java source line #60	-> byte code offset #11
/*     */     //   Java source line #64	-> byte code offset #14
/*     */     //   Java source line #66	-> byte code offset #22
/*     */     //   Java source line #67	-> byte code offset #31
/*     */     //   Java source line #68	-> byte code offset #42
/*     */     //   Java source line #69	-> byte code offset #55
/*     */     //   Java source line #71	-> byte code offset #66
/*     */     //   Java source line #74	-> byte code offset #78
/*     */     //   Java source line #75	-> byte code offset #82
/*     */     //   Java source line #76	-> byte code offset #91
/*     */     //   Java source line #75	-> byte code offset #96
/*     */     //   Java source line #77	-> byte code offset #103
/*     */     //   Java source line #78	-> byte code offset #126
/*     */     //   Java source line #80	-> byte code offset #142
/*     */     //   Java source line #81	-> byte code offset #160
/*     */     //   Java source line #82	-> byte code offset #168
/*     */     //   Java source line #85	-> byte code offset #182
/*     */     //   Java source line #86	-> byte code offset #210
/*     */     //   Java source line #85	-> byte code offset #215
/*     */     //   Java source line #87	-> byte code offset #218
/*     */     //   Java source line #88	-> byte code offset #246
/*     */     //   Java source line #87	-> byte code offset #251
/*     */     //   Java source line #90	-> byte code offset #254
/*     */     //   Java source line #91	-> byte code offset #262
/*     */     //   Java source line #90	-> byte code offset #268
/*     */     //   Java source line #92	-> byte code offset #278
/*     */     //   Java source line #93	-> byte code offset #286
/*     */     //   Java source line #94	-> byte code offset #293
/*     */     //   Java source line #95	-> byte code offset #304
/*     */     //   Java source line #98	-> byte code offset #326
/*     */     //   Java source line #99	-> byte code offset #338
/*     */     //   Java source line #100	-> byte code offset #354
/*     */     //   Java source line #101	-> byte code offset #366
/*     */     //   Java source line #102	-> byte code offset #384
/*     */     //   Java source line #103	-> byte code offset #396
/*     */     //   Java source line #104	-> byte code offset #416
/*     */     //   Java source line #105	-> byte code offset #428
/*     */     //   Java source line #106	-> byte code offset #448
/*     */     //   Java source line #107	-> byte code offset #460
/*     */     //   Java source line #109	-> byte code offset #480
/*     */     //   Java source line #111	-> byte code offset #496
/*     */     //   Java source line #113	-> byte code offset #513
/*     */     //   Java source line #114	-> byte code offset #525
/*     */     //   Java source line #116	-> byte code offset #531
/*     */     //   Java source line #121	-> byte code offset #543
/*     */     //   Java source line #122	-> byte code offset #553
/*     */     //   Java source line #117	-> byte code offset #556
/*     */     //   Java source line #118	-> byte code offset #558
/*     */     //   Java source line #121	-> byte code offset #568
/*     */     //   Java source line #125	-> byte code offset #599
/*     */     //   Java source line #126	-> byte code offset #609
/*     */     //   Java source line #128	-> byte code offset #618
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	637	0	this	LinuxDisks
/*     */     //   1	525	1	store	HWDiskStore
/*     */     //   21	599	2	result	List<HWDiskStore>
/*     */     //   7	606	3	handle	oshi.jna.platform.linux.Udev.UdevHandle
/*     */     //   9	578	4	device	Udev.UdevDevice
/*     */     //   12	591	5	enumerate	oshi.jna.platform.linux.Udev.UdevEnumerate
/*     */     //   76	466	6	entry	oshi.jna.platform.linux.Udev.UdevListEntry
/*     */     //   80	455	7	oldEntry	oshi.jna.platform.linux.Udev.UdevListEntry
/*     */     //   596	1	7	oldEntry	oshi.jna.platform.linux.Udev.UdevListEntry
/*     */     //   336	191	8	partArray	oshi.hardware.HWPartition[]
/*     */     //   556	3	8	ex	NullPointerException
/*     */     //   364	146	9	name	String
/*     */     //   581	13	10	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   78	543	556	java/lang/NullPointerException
/*     */     //   78	543	581	finally
/*     */     //   556	568	581	finally
/*     */     //   581	583	581	finally
/*     */   }
/*     */   
/*     */   private void updateMountsMap()
/*     */   {
/* 132 */     this.mountsMap.clear();
/* 133 */     List<String> mounts = FileUtil.readFile("/proc/self/mounts");
/* 134 */     for (String mount : mounts) {
/* 135 */       String[] split = mount.split("\\s+");
/* 136 */       if ((split.length >= 2) && (split[0].startsWith("/dev/")))
/*     */       {
/*     */ 
/* 139 */         this.mountsMap.put(split[0], split[1]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void computeDiskStats(HWDiskStore store, Udev.UdevDevice disk) {
/* 145 */     LinuxBlockDevStats stats = new LinuxBlockDevStats(store.getName(), disk);
/* 146 */     store.setTimeStamp(System.currentTimeMillis());
/*     */     
/*     */ 
/* 149 */     store.setReads(stats.read_ops);
/* 150 */     store.setReadBytes(stats.read_512bytes * 512L);
/* 151 */     store.setWrites(stats.write_ops);
/* 152 */     store.setWriteBytes(stats.write_512bytes * 512L);
/* 153 */     store.setTransferTime(stats.active_ms);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\linux\LinuxDisks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */