/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum HttpCompliance
/*     */ {
/*  56 */   LEGACY(sectionsBySpec("0,METHOD_CASE_SENSITIVE")), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  61 */   RFC2616_LEGACY(sectionsBySpec("RFC2616,-FIELD_COLON,-METHOD_CASE_SENSITIVE")), 
/*     */   
/*     */ 
/*  64 */   RFC2616(sectionsBySpec("RFC2616")), 
/*     */   
/*     */ 
/*  67 */   RFC7230_LEGACY(sectionsBySpec("RFC7230,-METHOD_CASE_SENSITIVE")), 
/*     */   
/*     */ 
/*  70 */   RFC7230(sectionsBySpec("RFC7230")), 
/*     */   
/*     */ 
/*  73 */   CUSTOM0(
/*  74 */     sectionsByProperty("CUSTOM0")), 
/*     */   
/*  76 */   CUSTOM1(
/*  77 */     sectionsByProperty("CUSTOM1")), 
/*     */   
/*  79 */   CUSTOM2(
/*  80 */     sectionsByProperty("CUSTOM2")), 
/*     */   
/*  82 */   CUSTOM3(
/*  83 */     sectionsByProperty("CUSTOM3"));
/*     */   
/*     */   private static final Logger LOG;
/*     */   
/*     */   private static EnumSet<HttpComplianceSection> sectionsByProperty(String property) {
/*  88 */     String s = System.getProperty(HttpCompliance.class.getName() + property);
/*  89 */     return sectionsBySpec(s == null ? "*" : s);
/*     */   }
/*     */   
/*     */ 
/*     */   static EnumSet<HttpComplianceSection> sectionsBySpec(String spec)
/*     */   {
/*  95 */     String[] elements = spec.split("\\s*,\\s*");
/*  96 */     int i = 0;
/*     */     EnumSet<HttpComplianceSection> sections;
/*  98 */     EnumSet<HttpComplianceSection> sections; EnumSet<HttpComplianceSection> sections; switch (elements[i])
/*     */     {
/*     */     case "0": 
/* 101 */       EnumSet<HttpComplianceSection> sections = EnumSet.noneOf(HttpComplianceSection.class);
/* 102 */       i++;
/* 103 */       break;
/*     */     
/*     */     case "*": 
/* 106 */       i++;
/* 107 */       sections = EnumSet.allOf(HttpComplianceSection.class);
/* 108 */       break;
/*     */     
/*     */     case "RFC2616": 
/* 111 */       EnumSet<HttpComplianceSection> sections = EnumSet.complementOf(EnumSet.of(HttpComplianceSection.NO_FIELD_FOLDING, HttpComplianceSection.NO_HTTP_0_9));
/*     */       
/*     */ 
/* 114 */       i++;
/* 115 */       break;
/*     */     
/*     */     case "RFC7230": 
/* 118 */       i++;
/* 119 */       sections = EnumSet.allOf(HttpComplianceSection.class);
/* 120 */       break;
/*     */     
/*     */     default: 
/* 123 */       sections = EnumSet.noneOf(HttpComplianceSection.class);
/*     */     }
/*     */     
/*     */     
/* 127 */     while (i < elements.length)
/*     */     {
/* 129 */       String element = elements[(i++)];
/* 130 */       boolean exclude = element.startsWith("-");
/* 131 */       if (exclude)
/* 132 */         element = element.substring(1);
/* 133 */       HttpComplianceSection section = HttpComplianceSection.valueOf(element);
/* 134 */       if (section == null)
/*     */       {
/* 136 */         LOG.warn("Unknown section '" + element + "' in HttpCompliance spec: " + spec, new Object[0]);
/*     */ 
/*     */       }
/* 139 */       else if (exclude) {
/* 140 */         sections.remove(section);
/*     */       } else {
/* 142 */         sections.add(section);
/*     */       }
/*     */     }
/*     */     
/* 146 */     return sections;
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*  85 */     LOG = Log.getLogger(HttpParser.class);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 149 */     __required = new HashMap();
/*     */     
/*     */ 
/* 152 */     for (HttpComplianceSection section : HttpComplianceSection.values())
/*     */     {
/* 154 */       for (HttpCompliance compliance : values())
/*     */       {
/* 156 */         if (compliance.sections().contains(section))
/*     */         {
/* 158 */           __required.put(section, compliance);
/* 159 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HttpCompliance requiredCompliance(HttpComplianceSection section)
/*     */   {
/* 171 */     return (HttpCompliance)__required.get(section);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private HttpCompliance(EnumSet<HttpComplianceSection> sections)
/*     */   {
/* 178 */     this._sections = sections;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final Map<HttpComplianceSection, HttpCompliance> __required;
/*     */   
/*     */   private final EnumSet<HttpComplianceSection> _sections;
/*     */   
/*     */   public EnumSet<HttpComplianceSection> sections()
/*     */   {
/* 189 */     return this._sections;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpCompliance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */