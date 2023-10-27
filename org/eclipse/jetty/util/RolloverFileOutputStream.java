/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDate;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RolloverFileOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private static Timer __rollover;
/*     */   static final String YYYY_MM_DD = "yyyy_mm_dd";
/*     */   static final String ROLLOVER_FILE_DATE_FORMAT = "yyyy_MM_dd";
/*     */   static final String ROLLOVER_FILE_BACKUP_FORMAT = "HHmmssSSS";
/*     */   static final int ROLLOVER_FILE_RETAIN_DAYS = 31;
/*     */   private OutputStream _out;
/*     */   private RollTask _rollTask;
/*     */   private SimpleDateFormat _fileBackupFormat;
/*     */   private SimpleDateFormat _fileDateFormat;
/*     */   private String _filename;
/*     */   private File _file;
/*     */   private boolean _append;
/*     */   private int _retainDays;
/*     */   
/*     */   public RolloverFileOutputStream(String filename)
/*     */     throws IOException
/*     */   {
/*  75 */     this(filename, true, 31);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RolloverFileOutputStream(String filename, boolean append)
/*     */     throws IOException
/*     */   {
/*  88 */     this(filename, append, 31);
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
/*     */   public RolloverFileOutputStream(String filename, boolean append, int retainDays)
/*     */     throws IOException
/*     */   {
/* 104 */     this(filename, append, retainDays, TimeZone.getDefault());
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
/*     */   public RolloverFileOutputStream(String filename, boolean append, int retainDays, TimeZone zone)
/*     */     throws IOException
/*     */   {
/* 122 */     this(filename, append, retainDays, zone, null, null, ZonedDateTime.now(zone.toZoneId()));
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
/*     */   public RolloverFileOutputStream(String filename, boolean append, int retainDays, TimeZone zone, String dateFormat, String backupFormat)
/*     */     throws IOException
/*     */   {
/* 145 */     this(filename, append, retainDays, zone, dateFormat, backupFormat, ZonedDateTime.now(zone.toZoneId()));
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
/*     */   RolloverFileOutputStream(String filename, boolean append, int retainDays, TimeZone zone, String dateFormat, String backupFormat, ZonedDateTime now)
/*     */     throws IOException
/*     */   {
/* 159 */     if (dateFormat == null)
/* 160 */       dateFormat = "yyyy_MM_dd";
/* 161 */     this._fileDateFormat = new SimpleDateFormat(dateFormat);
/*     */     
/* 163 */     if (backupFormat == null)
/* 164 */       backupFormat = "HHmmssSSS";
/* 165 */     this._fileBackupFormat = new SimpleDateFormat(backupFormat);
/*     */     
/* 167 */     this._fileBackupFormat.setTimeZone(zone);
/* 168 */     this._fileDateFormat.setTimeZone(zone);
/*     */     
/* 170 */     if (filename != null)
/*     */     {
/* 172 */       filename = filename.trim();
/* 173 */       if (filename.length() == 0)
/* 174 */         filename = null;
/*     */     }
/* 176 */     if (filename == null) {
/* 177 */       throw new IllegalArgumentException("Invalid filename");
/*     */     }
/* 179 */     this._filename = filename;
/* 180 */     this._append = append;
/* 181 */     this._retainDays = retainDays;
/*     */     
/*     */ 
/* 184 */     setFile(now);
/*     */     
/* 186 */     synchronized (RolloverFileOutputStream.class)
/*     */     {
/* 188 */       if (__rollover == null) {
/* 189 */         __rollover = new Timer(RolloverFileOutputStream.class.getName(), true);
/*     */       }
/*     */     }
/*     */     
/* 193 */     scheduleNextRollover(now);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ZonedDateTime toMidnight(ZonedDateTime now)
/*     */   {
/* 205 */     return now.toLocalDate().atStartOfDay(now.getZone()).plus(1L, ChronoUnit.DAYS);
/*     */   }
/*     */   
/*     */ 
/*     */   private void scheduleNextRollover(ZonedDateTime now)
/*     */   {
/* 211 */     this._rollTask = new RollTask(null);
/*     */     
/* 213 */     ZonedDateTime midnight = toMidnight(now);
/*     */     
/*     */ 
/* 216 */     long delay = midnight.toInstant().toEpochMilli() - now.toInstant().toEpochMilli();
/* 217 */     synchronized (RolloverFileOutputStream.class)
/*     */     {
/* 219 */       __rollover.schedule(this._rollTask, delay);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getFilename()
/*     */   {
/* 226 */     return this._filename;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDatedFilename()
/*     */   {
/* 232 */     if (this._file == null)
/* 233 */       return null;
/* 234 */     return this._file.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getRetainDays()
/*     */   {
/* 240 */     return this._retainDays;
/*     */   }
/*     */   
/*     */ 
/*     */   void setFile(ZonedDateTime now)
/*     */     throws IOException
/*     */   {
/* 247 */     File oldFile = null;
/* 248 */     File newFile = null;
/* 249 */     File backupFile = null;
/* 250 */     synchronized (this)
/*     */     {
/*     */ 
/* 253 */       File file = new File(this._filename);
/* 254 */       this._filename = file.getCanonicalPath();
/* 255 */       file = new File(this._filename);
/* 256 */       File dir = new File(file.getParent());
/* 257 */       if ((!dir.isDirectory()) || (!dir.canWrite())) {
/* 258 */         throw new IOException("Cannot write log directory " + dir);
/*     */       }
/*     */       
/* 261 */       String filename = file.getName();
/* 262 */       int datePattern = filename.toLowerCase(Locale.ENGLISH).indexOf("yyyy_mm_dd");
/* 263 */       if (datePattern >= 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 268 */         file = new File(dir, filename.substring(0, datePattern) + this._fileDateFormat.format(new Date(now.toInstant().toEpochMilli())) + filename.substring(datePattern + "yyyy_mm_dd".length()));
/*     */       }
/*     */       
/* 271 */       if ((file.exists()) && (!file.canWrite())) {
/* 272 */         throw new IOException("Cannot write log file " + file);
/*     */       }
/*     */       
/* 275 */       if ((this._out == null) || (datePattern >= 0))
/*     */       {
/*     */ 
/* 278 */         oldFile = this._file;
/* 279 */         this._file = file;
/* 280 */         newFile = this._file;
/*     */         
/* 282 */         OutputStream oldOut = this._out;
/* 283 */         if (oldOut != null) {
/* 284 */           oldOut.close();
/*     */         }
/* 286 */         if ((!this._append) && (file.exists()))
/*     */         {
/* 288 */           backupFile = new File(file.toString() + "." + this._fileBackupFormat.format(new Date(now.toInstant().toEpochMilli())));
/* 289 */           renameFile(file, backupFile);
/*     */         }
/* 291 */         this._out = new FileOutputStream(file.toString(), this._append);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 296 */     if (newFile != null) {
/* 297 */       rollover(oldFile, backupFile, newFile);
/*     */     }
/*     */   }
/*     */   
/*     */   private void renameFile(File src, File dest) throws IOException
/*     */   {
/* 303 */     if (!src.renameTo(dest))
/*     */     {
/*     */       try
/*     */       {
/*     */ 
/* 308 */         Files.move(src.toPath(), dest.toPath(), new CopyOption[0]);
/*     */ 
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 313 */         Files.copy(src.toPath(), dest.toPath(), new CopyOption[0]);
/*     */         
/* 315 */         Files.deleteIfExists(src.toPath());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void rollover(File oldFile, File backupFile, File newFile) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void removeOldFiles(ZonedDateTime now)
/*     */   {
/* 333 */     if (this._retainDays > 0)
/*     */     {
/*     */ 
/* 336 */       long expired = now.minus(this._retainDays, ChronoUnit.DAYS).toInstant().toEpochMilli();
/*     */       
/* 338 */       File file = new File(this._filename);
/* 339 */       File dir = new File(file.getParent());
/* 340 */       String fn = file.getName();
/* 341 */       int s = fn.toLowerCase(Locale.ENGLISH).indexOf("yyyy_mm_dd");
/* 342 */       if (s < 0)
/* 343 */         return;
/* 344 */       String prefix = fn.substring(0, s);
/* 345 */       String suffix = fn.substring(s + "yyyy_mm_dd".length());
/*     */       
/* 347 */       String[] logList = dir.list();
/* 348 */       for (int i = 0; i < logList.length; i++)
/*     */       {
/* 350 */         fn = logList[i];
/* 351 */         if ((fn.startsWith(prefix)) && (fn.indexOf(suffix, prefix.length()) >= 0))
/*     */         {
/* 353 */           File f = new File(dir, fn);
/* 354 */           if (f.lastModified() < expired)
/*     */           {
/* 356 */             f.delete();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void write(int b)
/*     */     throws IOException
/*     */   {
/* 367 */     synchronized (this)
/*     */     {
/* 369 */       this._out.write(b);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void write(byte[] buf)
/*     */     throws IOException
/*     */   {
/* 378 */     synchronized (this)
/*     */     {
/* 380 */       this._out.write(buf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void write(byte[] buf, int off, int len)
/*     */     throws IOException
/*     */   {
/* 389 */     synchronized (this)
/*     */     {
/* 391 */       this._out.write(buf, off, len);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 399 */     synchronized (this)
/*     */     {
/* 401 */       this._out.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_1
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 71	org/eclipse/jetty/util/RolloverFileOutputStream:_out	Ljava/io/OutputStream;
/*     */     //   8: invokevirtual 72	java/io/OutputStream:close	()V
/*     */     //   11: aload_0
/*     */     //   12: aconst_null
/*     */     //   13: putfield 71	org/eclipse/jetty/util/RolloverFileOutputStream:_out	Ljava/io/OutputStream;
/*     */     //   16: aload_0
/*     */     //   17: aconst_null
/*     */     //   18: putfield 42	org/eclipse/jetty/util/RolloverFileOutputStream:_file	Ljava/io/File;
/*     */     //   21: goto +16 -> 37
/*     */     //   24: astore_2
/*     */     //   25: aload_0
/*     */     //   26: aconst_null
/*     */     //   27: putfield 71	org/eclipse/jetty/util/RolloverFileOutputStream:_out	Ljava/io/OutputStream;
/*     */     //   30: aload_0
/*     */     //   31: aconst_null
/*     */     //   32: putfield 42	org/eclipse/jetty/util/RolloverFileOutputStream:_file	Ljava/io/File;
/*     */     //   35: aload_2
/*     */     //   36: athrow
/*     */     //   37: aload_1
/*     */     //   38: monitorexit
/*     */     //   39: goto +8 -> 47
/*     */     //   42: astore_3
/*     */     //   43: aload_1
/*     */     //   44: monitorexit
/*     */     //   45: aload_3
/*     */     //   46: athrow
/*     */     //   47: ldc 3
/*     */     //   49: dup
/*     */     //   50: astore_1
/*     */     //   51: monitorenter
/*     */     //   52: aload_0
/*     */     //   53: getfield 37	org/eclipse/jetty/util/RolloverFileOutputStream:_rollTask	Lorg/eclipse/jetty/util/RolloverFileOutputStream$RollTask;
/*     */     //   56: ifnull +11 -> 67
/*     */     //   59: aload_0
/*     */     //   60: getfield 37	org/eclipse/jetty/util/RolloverFileOutputStream:_rollTask	Lorg/eclipse/jetty/util/RolloverFileOutputStream$RollTask;
/*     */     //   63: invokevirtual 94	org/eclipse/jetty/util/RolloverFileOutputStream$RollTask:cancel	()Z
/*     */     //   66: pop
/*     */     //   67: aload_1
/*     */     //   68: monitorexit
/*     */     //   69: goto +10 -> 79
/*     */     //   72: astore 4
/*     */     //   74: aload_1
/*     */     //   75: monitorexit
/*     */     //   76: aload 4
/*     */     //   78: athrow
/*     */     //   79: return
/*     */     // Line number table:
/*     */     //   Java source line #410	-> byte code offset #0
/*     */     //   Java source line #414	-> byte code offset #4
/*     */     //   Java source line #418	-> byte code offset #11
/*     */     //   Java source line #419	-> byte code offset #16
/*     */     //   Java source line #420	-> byte code offset #21
/*     */     //   Java source line #418	-> byte code offset #24
/*     */     //   Java source line #419	-> byte code offset #30
/*     */     //   Java source line #421	-> byte code offset #37
/*     */     //   Java source line #423	-> byte code offset #47
/*     */     //   Java source line #425	-> byte code offset #52
/*     */     //   Java source line #427	-> byte code offset #59
/*     */     //   Java source line #429	-> byte code offset #67
/*     */     //   Java source line #430	-> byte code offset #79
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	80	0	this	RolloverFileOutputStream
/*     */     //   2	42	1	Ljava/lang/Object;	Object
/*     */     //   24	12	2	localObject1	Object
/*     */     //   42	4	3	localObject2	Object
/*     */     //   72	5	4	localObject3	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	11	24	finally
/*     */     //   4	39	42	finally
/*     */     //   42	45	42	finally
/*     */     //   52	69	72	finally
/*     */     //   72	76	72	finally
/*     */   }
/*     */   
/*     */   private class RollTask
/*     */     extends TimerTask
/*     */   {
/*     */     private RollTask() {}
/*     */     
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/* 440 */         ZonedDateTime now = ZonedDateTime.now(RolloverFileOutputStream.this._fileDateFormat.getTimeZone().toZoneId());
/* 441 */         RolloverFileOutputStream.this.setFile(now);
/* 442 */         RolloverFileOutputStream.this.removeOldFiles(now);
/* 443 */         RolloverFileOutputStream.this.scheduleNextRollover(now);
/*     */ 
/*     */       }
/*     */       catch (Throwable t)
/*     */       {
/* 448 */         t.printStackTrace(System.err);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\RolloverFileOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */