/*     */ package org.eclipse.jetty.websocket.api.extensions;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.eclipse.jetty.websocket.api.util.QuoteUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtensionConfig
/*     */ {
/*     */   private final String name;
/*     */   private final Map<String, String> parameters;
/*     */   
/*     */   public static ExtensionConfig parse(String parameterizedName)
/*     */   {
/*  45 */     return new ExtensionConfig(parameterizedName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<ExtensionConfig> parseEnum(Enumeration<String> valuesEnum)
/*     */   {
/*  57 */     List<ExtensionConfig> configs = new ArrayList();
/*     */     
/*  59 */     if (valuesEnum != null)
/*     */     {
/*  61 */       while (valuesEnum.hasMoreElements())
/*     */       {
/*  63 */         Iterator<String> extTokenIter = QuoteUtil.splitAt((String)valuesEnum.nextElement(), ",");
/*  64 */         while (extTokenIter.hasNext())
/*     */         {
/*  66 */           String extToken = (String)extTokenIter.next();
/*  67 */           configs.add(parse(extToken));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  72 */     return configs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<ExtensionConfig> parseList(String... rawSecWebSocketExtensions)
/*     */   {
/*  84 */     List<ExtensionConfig> configs = new ArrayList();
/*     */     
/*  86 */     for (String rawValue : rawSecWebSocketExtensions)
/*     */     {
/*  88 */       Iterator<String> extTokenIter = QuoteUtil.splitAt(rawValue, ",");
/*  89 */       while (extTokenIter.hasNext())
/*     */       {
/*  91 */         String extToken = (String)extTokenIter.next();
/*  92 */         configs.add(parse(extToken));
/*     */       }
/*     */     }
/*     */     
/*  96 */     return configs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toHeaderValue(List<ExtensionConfig> configs)
/*     */   {
/* 108 */     if ((configs == null) || (configs.isEmpty()))
/*     */     {
/* 110 */       return null;
/*     */     }
/* 112 */     StringBuilder parameters = new StringBuilder();
/* 113 */     boolean needsDelim = false;
/* 114 */     for (ExtensionConfig ext : configs)
/*     */     {
/* 116 */       if (needsDelim)
/*     */       {
/* 118 */         parameters.append(", ");
/*     */       }
/* 120 */       parameters.append(ext.getParameterizedName());
/* 121 */       needsDelim = true;
/*     */     }
/* 123 */     return parameters.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExtensionConfig(ExtensionConfig copy)
/*     */   {
/* 135 */     this.name = copy.name;
/* 136 */     this.parameters = new HashMap();
/* 137 */     this.parameters.putAll(copy.parameters);
/*     */   }
/*     */   
/*     */   public ExtensionConfig(String parameterizedName)
/*     */   {
/* 142 */     Iterator<String> extListIter = QuoteUtil.splitAt(parameterizedName, ";");
/* 143 */     this.name = ((String)extListIter.next());
/* 144 */     this.parameters = new HashMap();
/*     */     
/*     */ 
/* 147 */     while (extListIter.hasNext())
/*     */     {
/* 149 */       String extParam = (String)extListIter.next();
/* 150 */       Iterator<String> extParamIter = QuoteUtil.splitAt(extParam, "=");
/* 151 */       String key = ((String)extParamIter.next()).trim();
/* 152 */       String value = null;
/* 153 */       if (extParamIter.hasNext())
/*     */       {
/* 155 */         value = (String)extParamIter.next();
/*     */       }
/* 157 */       this.parameters.put(key, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 163 */     return this.name;
/*     */   }
/*     */   
/*     */   public final int getParameter(String key, int defValue)
/*     */   {
/* 168 */     String val = (String)this.parameters.get(key);
/* 169 */     if (val == null)
/*     */     {
/* 171 */       return defValue;
/*     */     }
/* 173 */     return Integer.valueOf(val).intValue();
/*     */   }
/*     */   
/*     */   public final String getParameter(String key, String defValue)
/*     */   {
/* 178 */     String val = (String)this.parameters.get(key);
/* 179 */     if (val == null)
/*     */     {
/* 181 */       return defValue;
/*     */     }
/* 183 */     return val;
/*     */   }
/*     */   
/*     */   public final String getParameterizedName()
/*     */   {
/* 188 */     StringBuilder str = new StringBuilder();
/* 189 */     str.append(this.name);
/* 190 */     for (String param : this.parameters.keySet())
/*     */     {
/* 192 */       str.append(';');
/* 193 */       str.append(param);
/* 194 */       String value = (String)this.parameters.get(param);
/* 195 */       if (value != null)
/*     */       {
/* 197 */         str.append('=');
/* 198 */         QuoteUtil.quoteIfNeeded(str, value, ";=");
/*     */       }
/*     */     }
/* 201 */     return str.toString();
/*     */   }
/*     */   
/*     */   public final Set<String> getParameterKeys()
/*     */   {
/* 206 */     return this.parameters.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Map<String, String> getParameters()
/*     */   {
/* 216 */     return this.parameters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void init(ExtensionConfig other)
/*     */   {
/* 227 */     this.parameters.clear();
/* 228 */     this.parameters.putAll(other.parameters);
/*     */   }
/*     */   
/*     */   public final void setParameter(String key)
/*     */   {
/* 233 */     this.parameters.put(key, null);
/*     */   }
/*     */   
/*     */   public final void setParameter(String key, int value)
/*     */   {
/* 238 */     this.parameters.put(key, Integer.toString(value));
/*     */   }
/*     */   
/*     */   public final void setParameter(String key, String value)
/*     */   {
/* 243 */     this.parameters.put(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 249 */     return getParameterizedName();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\extensions\ExtensionConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */