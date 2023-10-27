/*     */ package com.maxmind.db;
/*     */ 
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public final class Metadata
/*     */ {
/*     */   private final int binaryFormatMajorVersion;
/*     */   private final int binaryFormatMinorVersion;
/*     */   private final long buildEpoch;
/*     */   private final String databaseType;
/*     */   private final JsonNode description;
/*     */   private final int ipVersion;
/*     */   private final JsonNode languages;
/*     */   private final int nodeByteSize;
/*     */   private final int nodeCount;
/*     */   private final int recordSize;
/*     */   private final int searchTreeSize;
/*     */   
/*     */   Metadata(JsonNode metadata)
/*     */   {
/*  37 */     this.binaryFormatMajorVersion = metadata.get("binary_format_major_version").asInt();
/*     */     
/*  39 */     this.binaryFormatMinorVersion = metadata.get("binary_format_minor_version").asInt();
/*  40 */     this.buildEpoch = metadata.get("build_epoch").asLong();
/*  41 */     this.databaseType = metadata.get("database_type").asText();
/*  42 */     this.languages = metadata.get("languages");
/*  43 */     this.description = metadata.get("description");
/*  44 */     this.ipVersion = metadata.get("ip_version").asInt();
/*  45 */     this.nodeCount = metadata.get("node_count").asInt();
/*  46 */     this.recordSize = metadata.get("record_size").asInt();
/*  47 */     this.nodeByteSize = (this.recordSize / 4);
/*  48 */     this.searchTreeSize = (this.nodeCount * this.nodeByteSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getBinaryFormatMajorVersion()
/*     */   {
/*  55 */     return this.binaryFormatMajorVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getBinaryFormatMinorVersion()
/*     */   {
/*  62 */     return this.binaryFormatMinorVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Date getBuildDate()
/*     */   {
/*  69 */     return new Date(this.buildEpoch * 1000L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDatabaseType()
/*     */   {
/*  78 */     return this.databaseType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<String, String> getDescription()
/*     */   {
/*  85 */     (Map)new ObjectMapper().convertValue(this.description, new TypeReference() {});
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIpVersion()
/*     */   {
/*  95 */     return this.ipVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<String> getLanguages()
/*     */   {
/* 102 */     (List)new ObjectMapper().convertValue(this.languages, new TypeReference() {});
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int getNodeByteSize()
/*     */   {
/* 111 */     return this.nodeByteSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int getNodeCount()
/*     */   {
/* 118 */     return this.nodeCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int getRecordSize()
/*     */   {
/* 126 */     return this.recordSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int getSearchTreeSize()
/*     */   {
/* 133 */     return this.searchTreeSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 143 */     return "Metadata [binaryFormatMajorVersion=" + this.binaryFormatMajorVersion + ", binaryFormatMinorVersion=" + this.binaryFormatMinorVersion + ", buildEpoch=" + this.buildEpoch + ", databaseType=" + this.databaseType + ", description=" + this.description + ", ipVersion=" + this.ipVersion + ", nodeCount=" + this.nodeCount + ", recordSize=" + this.recordSize + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\db\Metadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */