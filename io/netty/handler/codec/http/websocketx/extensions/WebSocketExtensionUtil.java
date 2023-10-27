/*    */ package io.netty.handler.codec.http.websocketx.extensions;
/*    */ 
/*    */ import io.netty.handler.codec.http.HttpHeaderNames;
/*    */ import io.netty.handler.codec.http.HttpHeaderValues;
/*    */ import io.netty.handler.codec.http.HttpHeaders;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WebSocketExtensionUtil
/*    */ {
/*    */   private static final String EXTENSION_SEPARATOR = ",";
/*    */   private static final String PARAMETER_SEPARATOR = ";";
/*    */   private static final char PARAMETER_EQUAL = '=';
/* 37 */   private static final Pattern PARAMETER = Pattern.compile("^([^=]+)(=[\\\"]?([^\\\"]+)[\\\"]?)?$");
/*    */   
/*    */   static boolean isWebsocketUpgrade(HttpHeaders headers) {
/* 40 */     return (headers.containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true)) && 
/* 41 */       (headers.contains(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET, true));
/*    */   }
/*    */   
/*    */   public static List<WebSocketExtensionData> extractExtensions(String extensionHeader) {
/* 45 */     String[] rawExtensions = extensionHeader.split(",");
/* 46 */     if (rawExtensions.length > 0) {
/* 47 */       List<WebSocketExtensionData> extensions = new ArrayList(rawExtensions.length);
/* 48 */       for (String rawExtension : rawExtensions) {
/* 49 */         String[] extensionParameters = rawExtension.split(";");
/* 50 */         String name = extensionParameters[0].trim();
/*    */         Map<String, String> parameters;
/* 52 */         if (extensionParameters.length > 1) {
/* 53 */           Map<String, String> parameters = new HashMap(extensionParameters.length - 1);
/* 54 */           for (int i = 1; i < extensionParameters.length; i++) {
/* 55 */             String parameter = extensionParameters[i].trim();
/* 56 */             Matcher parameterMatcher = PARAMETER.matcher(parameter);
/* 57 */             if ((parameterMatcher.matches()) && (parameterMatcher.group(1) != null)) {
/* 58 */               parameters.put(parameterMatcher.group(1), parameterMatcher.group(3));
/*    */             }
/*    */           }
/*    */         } else {
/* 62 */           parameters = Collections.emptyMap();
/*    */         }
/* 64 */         extensions.add(new WebSocketExtensionData(name, parameters));
/*    */       }
/* 66 */       return extensions;
/*    */     }
/* 68 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   static String appendExtension(String currentHeaderValue, String extensionName, Map<String, String> extensionParameters)
/*    */   {
/* 76 */     StringBuilder newHeaderValue = new StringBuilder(currentHeaderValue != null ? currentHeaderValue.length() : extensionName.length() + 1);
/* 77 */     if ((currentHeaderValue != null) && (!currentHeaderValue.trim().isEmpty())) {
/* 78 */       newHeaderValue.append(currentHeaderValue);
/* 79 */       newHeaderValue.append(",");
/*    */     }
/* 81 */     newHeaderValue.append(extensionName);
/* 82 */     for (Map.Entry<String, String> extensionParameter : extensionParameters.entrySet()) {
/* 83 */       newHeaderValue.append(";");
/* 84 */       newHeaderValue.append((String)extensionParameter.getKey());
/* 85 */       if (extensionParameter.getValue() != null) {
/* 86 */         newHeaderValue.append('=');
/* 87 */         newHeaderValue.append((String)extensionParameter.getValue());
/*    */       }
/*    */     }
/* 90 */     return newHeaderValue.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\websocketx\extensions\WebSocketExtensionUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */