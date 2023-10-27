/*     */ package oshi.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class FileUtil
/*     */ {
/*  40 */   private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);
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
/*     */   public static List<String> readFile(String filename)
/*     */   {
/*  56 */     return readFile(filename, true);
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
/*     */   public static List<String> readFile(String filename, boolean reportError)
/*     */   {
/*  72 */     if (new File(filename).exists()) {
/*  73 */       LOG.debug("Reading file {}", filename);
/*     */       try {
/*  75 */         return Files.readAllLines(Paths.get(filename, new String[0]), StandardCharsets.UTF_8);
/*     */       } catch (IOException e) {
/*  77 */         if (reportError) {
/*  78 */           LOG.error("Error reading file {}. {}", filename, e);
/*     */         }
/*     */       }
/*  81 */     } else if (reportError) {
/*  82 */       LOG.warn("File not found: {}", filename);
/*     */     }
/*  84 */     return new ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long getLongFromFile(String filename)
/*     */   {
/*  96 */     LOG.debug("Reading file {}", filename);
/*  97 */     List<String> read = readFile(filename, false);
/*  98 */     if (!read.isEmpty()) {
/*  99 */       LOG.trace("Read {}", read.get(0));
/* 100 */       return ParseUtil.parseLongOrDefault((String)read.get(0), 0L);
/*     */     }
/* 102 */     return 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getIntFromFile(String filename)
/*     */   {
/* 114 */     LOG.debug("Reading file {}", filename);
/*     */     try {
/* 116 */       List<String> read = readFile(filename, false);
/* 117 */       if (!read.isEmpty()) {
/* 118 */         LOG.trace("Read {}", read.get(0));
/* 119 */         return Integer.parseInt((String)read.get(0));
/*     */       }
/*     */     } catch (NumberFormatException ex) {
/* 122 */       LOG.debug("Unable to read value from {}. {}", filename, ex);
/*     */     }
/* 124 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getStringFromFile(String filename)
/*     */   {
/* 136 */     LOG.debug("Reading file {}", filename);
/* 137 */     List<String> read = readFile(filename, false);
/* 138 */     if (!read.isEmpty()) {
/* 139 */       LOG.trace("Read {}", read.get(0));
/* 140 */       return (String)read.get(0);
/*     */     }
/* 142 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] getSplitFromFile(String filename)
/*     */   {
/* 154 */     LOG.debug("Reading file {}", filename);
/* 155 */     List<String> read = readFile(filename, false);
/* 156 */     if (!read.isEmpty()) {
/* 157 */       LOG.trace("Read {}", read.get(0));
/* 158 */       return ((String)read.get(0)).split("\\s+");
/*     */     }
/* 160 */     return new String[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Map<String, String> getKeyValueMapFromFile(String filename, String separator)
/*     */   {
/* 172 */     Map<String, String> map = new HashMap();
/* 173 */     LOG.debug("Reading file {}", filename);
/* 174 */     List<String> lines = readFile(filename, false);
/* 175 */     for (String line : lines) {
/* 176 */       String[] parts = line.split(separator);
/* 177 */       if (parts.length == 2) {
/* 178 */         map.put(parts[0], parts[1].trim());
/*     */       }
/*     */     }
/* 181 */     return map;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\util\FileUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */