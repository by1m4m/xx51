/*     */ package com.fasterxml.jackson.databind.ext;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*     */ import com.fasterxml.jackson.databind.ser.Serializers;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionalHandlerFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String PACKAGE_PREFIX_JAVAX_XML = "javax.xml.";
/*     */   private static final String SERIALIZERS_FOR_JAVAX_XML = "com.fasterxml.jackson.databind.ext.CoreXMLSerializers";
/*     */   private static final String DESERIALIZERS_FOR_JAVAX_XML = "com.fasterxml.jackson.databind.ext.CoreXMLDeserializers";
/*     */   private static final String CLASS_NAME_DOM_NODE = "org.w3c.dom.Node";
/*     */   private static final String CLASS_NAME_DOM_DOCUMENT = "org.w3c.dom.Node";
/*     */   private static final String SERIALIZER_FOR_DOM_NODE = "com.fasterxml.jackson.databind.ext.DOMSerializer";
/*     */   private static final String DESERIALIZER_FOR_DOM_DOCUMENT = "com.fasterxml.jackson.databind.ext.DOMDeserializer$DocumentDeserializer";
/*     */   private static final String DESERIALIZER_FOR_DOM_NODE = "com.fasterxml.jackson.databind.ext.DOMDeserializer$NodeDeserializer";
/*  31 */   public static final OptionalHandlerFactory instance = new OptionalHandlerFactory();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc)
/*     */   {
/*  44 */     Class<?> rawType = type.getRawClass();
/*  45 */     String className = rawType.getName();
/*     */     
/*     */ 
/*  48 */     if (doesImplement(rawType, "org.w3c.dom.Node"))
/*  49 */       return (JsonSerializer)instantiate("com.fasterxml.jackson.databind.ext.DOMSerializer");
/*     */     String factoryName;
/*  51 */     if ((className.startsWith("javax.xml.")) || (hasSupertypeStartingWith(rawType, "javax.xml."))) {
/*  52 */       factoryName = "com.fasterxml.jackson.databind.ext.CoreXMLSerializers";
/*     */     } else {
/*  54 */       return null;
/*     */     }
/*     */     String factoryName;
/*  57 */     Object ob = instantiate(factoryName);
/*  58 */     if (ob == null) {
/*  59 */       return null;
/*     */     }
/*  61 */     return ((Serializers)ob).findSerializer(config, type, beanDesc);
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonDeserializer<?> findDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/*  68 */     Class<?> rawType = type.getRawClass();
/*  69 */     String className = rawType.getName();
/*     */     
/*     */     String factoryName;
/*  72 */     if ((className.startsWith("javax.xml.")) || (hasSupertypeStartingWith(rawType, "javax.xml.")))
/*     */     {
/*  74 */       factoryName = "com.fasterxml.jackson.databind.ext.CoreXMLDeserializers";
/*  75 */     } else { if (doesImplement(rawType, "org.w3c.dom.Node"))
/*  76 */         return (JsonDeserializer)instantiate("com.fasterxml.jackson.databind.ext.DOMDeserializer$DocumentDeserializer");
/*  77 */       if (doesImplement(rawType, "org.w3c.dom.Node")) {
/*  78 */         return (JsonDeserializer)instantiate("com.fasterxml.jackson.databind.ext.DOMDeserializer$NodeDeserializer");
/*     */       }
/*  80 */       return null; }
/*     */     String factoryName;
/*  82 */     Object ob = instantiate(factoryName);
/*  83 */     if (ob == null) {
/*  84 */       return null;
/*     */     }
/*  86 */     return ((Deserializers)ob).findBeanDeserializer(type, config, beanDesc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object instantiate(String className)
/*     */   {
/*     */     try
/*     */     {
/*  98 */       return Class.forName(className).newInstance();
/*     */     }
/*     */     catch (LinkageError e) {}catch (Exception e) {}
/*     */     
/* 102 */     return null;
/*     */   }
/*     */   
/*     */   private boolean doesImplement(Class<?> actualType, String classNameToImplement)
/*     */   {
/* 107 */     for (Class<?> type = actualType; type != null; type = type.getSuperclass()) {
/* 108 */       if (type.getName().equals(classNameToImplement)) {
/* 109 */         return true;
/*     */       }
/*     */       
/* 112 */       if (hasInterface(type, classNameToImplement)) {
/* 113 */         return true;
/*     */       }
/*     */     }
/* 116 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasInterface(Class<?> type, String interfaceToImplement)
/*     */   {
/* 121 */     Class<?>[] interfaces = type.getInterfaces();
/* 122 */     for (Class<?> iface : interfaces) {
/* 123 */       if (iface.getName().equals(interfaceToImplement)) {
/* 124 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 128 */     for (Class<?> iface : interfaces) {
/* 129 */       if (hasInterface(iface, interfaceToImplement)) {
/* 130 */         return true;
/*     */       }
/*     */     }
/* 133 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean hasSupertypeStartingWith(Class<?> rawType, String prefix)
/*     */   {
/* 139 */     for (Class<?> supertype = rawType.getSuperclass(); supertype != null; supertype = supertype.getSuperclass()) {
/* 140 */       if (supertype.getName().startsWith(prefix)) {
/* 141 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 145 */     for (Class<?> cls = rawType; cls != null; cls = cls.getSuperclass()) {
/* 146 */       if (hasInterfaceStartingWith(cls, prefix)) {
/* 147 */         return true;
/*     */       }
/*     */     }
/* 150 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasInterfaceStartingWith(Class<?> type, String prefix)
/*     */   {
/* 155 */     Class<?>[] interfaces = type.getInterfaces();
/* 156 */     for (Class<?> iface : interfaces) {
/* 157 */       if (iface.getName().startsWith(prefix)) {
/* 158 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 162 */     for (Class<?> iface : interfaces) {
/* 163 */       if (hasInterfaceStartingWith(iface, prefix)) {
/* 164 */         return true;
/*     */       }
/*     */     }
/* 167 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ext\OptionalHandlerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */