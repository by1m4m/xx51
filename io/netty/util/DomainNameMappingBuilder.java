/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ public final class DomainNameMappingBuilder<V>
/*     */ {
/*     */   private final V defaultValue;
/*     */   private final Map<String, V> map;
/*     */   
/*     */   public DomainNameMappingBuilder(V defaultValue)
/*     */   {
/*  43 */     this(4, defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DomainNameMappingBuilder(int initialCapacity, V defaultValue)
/*     */   {
/*  54 */     this.defaultValue = ObjectUtil.checkNotNull(defaultValue, "defaultValue");
/*  55 */     this.map = new LinkedHashMap(initialCapacity);
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
/*     */   public DomainNameMappingBuilder<V> add(String hostname, V output)
/*     */   {
/*  71 */     this.map.put(ObjectUtil.checkNotNull(hostname, "hostname"), ObjectUtil.checkNotNull(output, "output"));
/*  72 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DomainNameMapping<V> build()
/*     */   {
/*  82 */     return new ImmutableDomainNameMapping(this.defaultValue, this.map, null);
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class ImmutableDomainNameMapping<V>
/*     */     extends DomainNameMapping<V>
/*     */   {
/*     */     private static final String REPR_HEADER = "ImmutableDomainNameMapping(default: ";
/*     */     
/*     */     private static final String REPR_MAP_OPENING = ", map: {";
/*     */     
/*     */     private static final String REPR_MAP_CLOSING = "})";
/*     */     
/*  95 */     private static final int REPR_CONST_PART_LENGTH = "ImmutableDomainNameMapping(default: "
/*  96 */       .length() + ", map: {".length() + "})".length();
/*     */     
/*     */     private final String[] domainNamePatterns;
/*     */     private final V[] values;
/*     */     private final Map<String, V> map;
/*     */     
/*     */     private ImmutableDomainNameMapping(V defaultValue, Map<String, V> map)
/*     */     {
/* 104 */       super(defaultValue);
/*     */       
/* 106 */       Set<Map.Entry<String, V>> mappings = map.entrySet();
/* 107 */       int numberOfMappings = mappings.size();
/* 108 */       this.domainNamePatterns = new String[numberOfMappings];
/* 109 */       this.values = ((Object[])new Object[numberOfMappings]);
/*     */       
/* 111 */       Map<String, V> mapCopy = new LinkedHashMap(map.size());
/* 112 */       int index = 0;
/* 113 */       for (Map.Entry<String, V> mapping : mappings) {
/* 114 */         String hostname = normalizeHostname((String)mapping.getKey());
/* 115 */         V value = mapping.getValue();
/* 116 */         this.domainNamePatterns[index] = hostname;
/* 117 */         this.values[index] = value;
/* 118 */         mapCopy.put(hostname, value);
/* 119 */         index++;
/*     */       }
/*     */       
/* 122 */       this.map = Collections.unmodifiableMap(mapCopy);
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public DomainNameMapping<V> add(String hostname, V output)
/*     */     {
/* 128 */       throw new UnsupportedOperationException("Immutable DomainNameMapping does not support modification after initial creation");
/*     */     }
/*     */     
/*     */ 
/*     */     public V map(String hostname)
/*     */     {
/* 134 */       if (hostname != null) {
/* 135 */         hostname = normalizeHostname(hostname);
/*     */         
/* 137 */         int length = this.domainNamePatterns.length;
/* 138 */         for (int index = 0; index < length; index++) {
/* 139 */           if (matches(this.domainNamePatterns[index], hostname)) {
/* 140 */             return (V)this.values[index];
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 145 */       return (V)this.defaultValue;
/*     */     }
/*     */     
/*     */     public Map<String, V> asMap()
/*     */     {
/* 150 */       return this.map;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 155 */       String defaultValueStr = this.defaultValue.toString();
/*     */       
/* 157 */       int numberOfMappings = this.domainNamePatterns.length;
/* 158 */       if (numberOfMappings == 0) {
/* 159 */         return "ImmutableDomainNameMapping(default: " + defaultValueStr + ", map: {" + "})";
/*     */       }
/*     */       
/* 162 */       String pattern0 = this.domainNamePatterns[0];
/* 163 */       String value0 = this.values[0].toString();
/* 164 */       int oneMappingLength = pattern0.length() + value0.length() + 3;
/* 165 */       int estimatedBufferSize = estimateBufferSize(defaultValueStr.length(), numberOfMappings, oneMappingLength);
/*     */       
/*     */ 
/* 168 */       StringBuilder sb = new StringBuilder(estimatedBufferSize).append("ImmutableDomainNameMapping(default: ").append(defaultValueStr).append(", map: {");
/*     */       
/* 170 */       appendMapping(sb, pattern0, value0);
/* 171 */       for (int index = 1; index < numberOfMappings; index++) {
/* 172 */         sb.append(", ");
/* 173 */         appendMapping(sb, index);
/*     */       }
/*     */       
/* 176 */       return "})";
/*     */     }
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
/*     */     private static int estimateBufferSize(int defaultValueLength, int numberOfMappings, int estimatedMappingLength)
/*     */     {
/* 192 */       return REPR_CONST_PART_LENGTH + defaultValueLength + (int)(estimatedMappingLength * numberOfMappings * 1.1D);
/*     */     }
/*     */     
/*     */     private StringBuilder appendMapping(StringBuilder sb, int mappingIndex)
/*     */     {
/* 197 */       return appendMapping(sb, this.domainNamePatterns[mappingIndex], this.values[mappingIndex].toString());
/*     */     }
/*     */     
/*     */     private static StringBuilder appendMapping(StringBuilder sb, String domainNamePattern, String value) {
/* 201 */       return sb.append(domainNamePattern).append('=').append(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\DomainNameMappingBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */