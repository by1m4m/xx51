/*    */ package com.fasterxml.jackson.databind.ext;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
/*    */ import java.io.StringReader;
/*    */ import javax.xml.parsers.DocumentBuilder;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Node;
/*    */ import org.xml.sax.InputSource;
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
/*    */ public abstract class DOMDeserializer<T>
/*    */   extends FromStringDeserializer<T>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 25 */   private static final DocumentBuilderFactory _parserFactory = ;
/*    */   
/* 27 */   static { _parserFactory.setNamespaceAware(true); }
/*    */   
/*    */   protected DOMDeserializer(Class<T> cls) {
/* 30 */     super(cls);
/*    */   }
/*    */   
/*    */   protected final Document parse(String value) throws IllegalArgumentException
/*    */   {
/*    */     try
/*    */     {
/* 37 */       return _parserFactory.newDocumentBuilder().parse(new InputSource(new StringReader(value)));
/*    */     } catch (Exception e) {
/* 39 */       throw new IllegalArgumentException("Failed to parse JSON String as XML: " + e.getMessage(), e);
/*    */     }
/*    */   }
/*    */   
/*    */   public abstract T _deserialize(String paramString, DeserializationContext paramDeserializationContext);
/*    */   
/*    */   public static class NodeDeserializer extends DOMDeserializer<Node>
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     public NodeDeserializer()
/*    */     {
/* 51 */       super();
/*    */     }
/*    */     
/* 54 */     public Node _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException { return parse(value); }
/*    */   }
/*    */   
/*    */   public static class DocumentDeserializer extends DOMDeserializer<Document> {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/* 60 */     public DocumentDeserializer() { super(); }
/*    */     
/*    */     public Document _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException {
/* 63 */       return parse(value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ext\DOMDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */