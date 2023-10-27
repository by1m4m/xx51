/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonPointer;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ public class ObjectNode
/*     */   extends ContainerNode<ObjectNode>
/*     */ {
/*     */   protected final Map<String, JsonNode> _children;
/*     */   
/*     */   public ObjectNode(JsonNodeFactory nc)
/*     */   {
/*  30 */     super(nc);
/*  31 */     this._children = new LinkedHashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ObjectNode(JsonNodeFactory nc, Map<String, JsonNode> kids)
/*     */   {
/*  38 */     super(nc);
/*  39 */     this._children = kids;
/*     */   }
/*     */   
/*     */   protected JsonNode _at(JsonPointer ptr)
/*     */   {
/*  44 */     return get(ptr.getMatchingProperty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode deepCopy()
/*     */   {
/*  55 */     ObjectNode ret = new ObjectNode(this._nodeFactory);
/*     */     
/*  57 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/*  58 */       ret._children.put(entry.getKey(), ((JsonNode)entry.getValue()).deepCopy());
/*     */     }
/*  60 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNodeType getNodeType()
/*     */   {
/*  71 */     return JsonNodeType.OBJECT;
/*     */   }
/*     */   
/*  74 */   public JsonToken asToken() { return JsonToken.START_OBJECT; }
/*     */   
/*     */   public int size()
/*     */   {
/*  78 */     return this._children.size();
/*     */   }
/*     */   
/*     */   public Iterator<JsonNode> elements()
/*     */   {
/*  83 */     return this._children.values().iterator();
/*     */   }
/*     */   
/*     */   public JsonNode get(int index) {
/*  87 */     return null;
/*     */   }
/*     */   
/*     */   public JsonNode get(String fieldName) {
/*  91 */     return (JsonNode)this._children.get(fieldName);
/*     */   }
/*     */   
/*     */   public Iterator<String> fieldNames()
/*     */   {
/*  96 */     return this._children.keySet().iterator();
/*     */   }
/*     */   
/*     */   public JsonNode path(int index)
/*     */   {
/* 101 */     return MissingNode.getInstance();
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonNode path(String fieldName)
/*     */   {
/* 107 */     JsonNode n = (JsonNode)this._children.get(fieldName);
/* 108 */     if (n != null) {
/* 109 */       return n;
/*     */     }
/* 111 */     return MissingNode.getInstance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<Map.Entry<String, JsonNode>> fields()
/*     */   {
/* 120 */     return this._children.entrySet().iterator();
/*     */   }
/*     */   
/*     */   public ObjectNode with(String propertyName)
/*     */   {
/* 125 */     JsonNode n = (JsonNode)this._children.get(propertyName);
/* 126 */     if (n != null) {
/* 127 */       if ((n instanceof ObjectNode)) {
/* 128 */         return (ObjectNode)n;
/*     */       }
/* 130 */       throw new UnsupportedOperationException("Property '" + propertyName + "' has value that is not of type ObjectNode (but " + n.getClass().getName() + ")");
/*     */     }
/*     */     
/*     */ 
/* 134 */     ObjectNode result = objectNode();
/* 135 */     this._children.put(propertyName, result);
/* 136 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayNode withArray(String propertyName)
/*     */   {
/* 142 */     JsonNode n = (JsonNode)this._children.get(propertyName);
/* 143 */     if (n != null) {
/* 144 */       if ((n instanceof ArrayNode)) {
/* 145 */         return (ArrayNode)n;
/*     */       }
/* 147 */       throw new UnsupportedOperationException("Property '" + propertyName + "' has value that is not of type ArrayNode (but " + n.getClass().getName() + ")");
/*     */     }
/*     */     
/*     */ 
/* 151 */     ArrayNode result = arrayNode();
/* 152 */     this._children.put(propertyName, result);
/* 153 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode findValue(String fieldName)
/*     */   {
/* 165 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 166 */       if (fieldName.equals(entry.getKey())) {
/* 167 */         return (JsonNode)entry.getValue();
/*     */       }
/* 169 */       JsonNode value = ((JsonNode)entry.getValue()).findValue(fieldName);
/* 170 */       if (value != null) {
/* 171 */         return value;
/*     */       }
/*     */     }
/* 174 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 180 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 181 */       if (fieldName.equals(entry.getKey())) {
/* 182 */         if (foundSoFar == null) {
/* 183 */           foundSoFar = new ArrayList();
/*     */         }
/* 185 */         foundSoFar.add(entry.getValue());
/*     */       } else {
/* 187 */         foundSoFar = ((JsonNode)entry.getValue()).findValues(fieldName, foundSoFar);
/*     */       }
/*     */     }
/* 190 */     return foundSoFar;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> findValuesAsText(String fieldName, List<String> foundSoFar)
/*     */   {
/* 196 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 197 */       if (fieldName.equals(entry.getKey())) {
/* 198 */         if (foundSoFar == null) {
/* 199 */           foundSoFar = new ArrayList();
/*     */         }
/* 201 */         foundSoFar.add(((JsonNode)entry.getValue()).asText());
/*     */       } else {
/* 203 */         foundSoFar = ((JsonNode)entry.getValue()).findValuesAsText(fieldName, foundSoFar);
/*     */       }
/*     */     }
/*     */     
/* 207 */     return foundSoFar;
/*     */   }
/*     */   
/*     */ 
/*     */   public ObjectNode findParent(String fieldName)
/*     */   {
/* 213 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 214 */       if (fieldName.equals(entry.getKey())) {
/* 215 */         return this;
/*     */       }
/* 217 */       JsonNode value = ((JsonNode)entry.getValue()).findParent(fieldName);
/* 218 */       if (value != null) {
/* 219 */         return (ObjectNode)value;
/*     */       }
/*     */     }
/* 222 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 228 */     for (Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
/* 229 */       if (fieldName.equals(entry.getKey())) {
/* 230 */         if (foundSoFar == null) {
/* 231 */           foundSoFar = new ArrayList();
/*     */         }
/* 233 */         foundSoFar.add(this);
/*     */       } else {
/* 235 */         foundSoFar = ((JsonNode)entry.getValue()).findParents(fieldName, foundSoFar);
/*     */       }
/*     */     }
/*     */     
/* 239 */     return foundSoFar;
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
/*     */   public void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 256 */     jg.writeStartObject();
/* 257 */     for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
/* 258 */       jg.writeFieldName((String)en.getKey());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 264 */       ((BaseJsonNode)en.getValue()).serialize(jg, provider);
/*     */     }
/* 266 */     jg.writeEndObject();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 274 */     typeSer.writeTypePrefixForObject(this, jg);
/* 275 */     for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
/* 276 */       jg.writeFieldName((String)en.getKey());
/* 277 */       ((BaseJsonNode)en.getValue()).serialize(jg, provider);
/*     */     }
/* 279 */     typeSer.writeTypeSuffixForObject(this, jg);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode set(String fieldName, JsonNode value)
/*     */   {
/* 306 */     if (value == null) {
/* 307 */       value = nullNode();
/*     */     }
/* 309 */     this._children.put(fieldName, value);
/* 310 */     return this;
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
/*     */   public JsonNode setAll(Map<String, ? extends JsonNode> properties)
/*     */   {
/* 325 */     for (Map.Entry<String, ? extends JsonNode> en : properties.entrySet()) {
/* 326 */       JsonNode n = (JsonNode)en.getValue();
/* 327 */       if (n == null) {
/* 328 */         n = nullNode();
/*     */       }
/* 330 */       this._children.put(en.getKey(), n);
/*     */     }
/* 332 */     return this;
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
/*     */   public JsonNode setAll(ObjectNode other)
/*     */   {
/* 347 */     this._children.putAll(other._children);
/* 348 */     return this;
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
/*     */   public JsonNode replace(String fieldName, JsonNode value)
/*     */   {
/* 365 */     if (value == null) {
/* 366 */       value = nullNode();
/*     */     }
/* 368 */     return (JsonNode)this._children.put(fieldName, value);
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
/*     */   public JsonNode without(String fieldName)
/*     */   {
/* 381 */     this._children.remove(fieldName);
/* 382 */     return this;
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
/*     */   public ObjectNode without(Collection<String> fieldNames)
/*     */   {
/* 397 */     this._children.keySet().removeAll(fieldNames);
/* 398 */     return this;
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
/*     */ 
/*     */   @Deprecated
/*     */   public JsonNode put(String fieldName, JsonNode value)
/*     */   {
/* 422 */     if (value == null) {
/* 423 */       value = nullNode();
/*     */     }
/* 425 */     return (JsonNode)this._children.put(fieldName, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode remove(String fieldName)
/*     */   {
/* 436 */     return (JsonNode)this._children.remove(fieldName);
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
/*     */   public ObjectNode remove(Collection<String> fieldNames)
/*     */   {
/* 449 */     this._children.keySet().removeAll(fieldNames);
/* 450 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode removeAll()
/*     */   {
/* 462 */     this._children.clear();
/* 463 */     return this;
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
/*     */   @Deprecated
/*     */   public JsonNode putAll(Map<String, ? extends JsonNode> properties)
/*     */   {
/* 478 */     return setAll(properties);
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
/*     */   @Deprecated
/*     */   public JsonNode putAll(ObjectNode other)
/*     */   {
/* 493 */     return setAll(other);
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
/*     */   public ObjectNode retain(Collection<String> fieldNames)
/*     */   {
/* 506 */     this._children.keySet().retainAll(fieldNames);
/* 507 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode retain(String... fieldNames)
/*     */   {
/* 519 */     return retain(Arrays.asList(fieldNames));
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
/*     */   public ArrayNode putArray(String fieldName)
/*     */   {
/* 541 */     ArrayNode n = arrayNode();
/* 542 */     _put(fieldName, n);
/* 543 */     return n;
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
/*     */   public ObjectNode putObject(String fieldName)
/*     */   {
/* 559 */     ObjectNode n = objectNode();
/* 560 */     _put(fieldName, n);
/* 561 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ObjectNode putPOJO(String fieldName, Object pojo)
/*     */   {
/* 568 */     return _put(fieldName, pojoNode(pojo));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode putNull(String fieldName)
/*     */   {
/* 576 */     this._children.put(fieldName, nullNode());
/* 577 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, short v)
/*     */   {
/* 586 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, Short v)
/*     */   {
/* 596 */     return _put(fieldName, v == null ? nullNode() : numberNode(v.shortValue()));
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
/*     */   public ObjectNode put(String fieldName, int v)
/*     */   {
/* 610 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, Integer v)
/*     */   {
/* 620 */     return _put(fieldName, v == null ? nullNode() : numberNode(v.intValue()));
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
/*     */   public ObjectNode put(String fieldName, long v)
/*     */   {
/* 634 */     return _put(fieldName, numberNode(v));
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
/*     */   public ObjectNode put(String fieldName, Long v)
/*     */   {
/* 650 */     return _put(fieldName, v == null ? nullNode() : numberNode(v.longValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, float v)
/*     */   {
/* 660 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, Float v)
/*     */   {
/* 670 */     return _put(fieldName, v == null ? nullNode() : numberNode(v.floatValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, double v)
/*     */   {
/* 680 */     return _put(fieldName, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, Double v)
/*     */   {
/* 690 */     return _put(fieldName, v == null ? nullNode() : numberNode(v.doubleValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, BigDecimal v)
/*     */   {
/* 700 */     return _put(fieldName, v == null ? nullNode() : numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, String v)
/*     */   {
/* 710 */     return _put(fieldName, v == null ? nullNode() : textNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, boolean v)
/*     */   {
/* 720 */     return _put(fieldName, booleanNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, Boolean v)
/*     */   {
/* 730 */     return _put(fieldName, v == null ? nullNode() : booleanNode(v.booleanValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode put(String fieldName, byte[] v)
/*     */   {
/* 740 */     return _put(fieldName, v == null ? nullNode() : binaryNode(v));
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
/*     */   public boolean equals(Object o)
/*     */   {
/* 753 */     if (o == this) return true;
/* 754 */     if (o == null) return false;
/* 755 */     if ((o instanceof ObjectNode)) {
/* 756 */       return _childrenEqual((ObjectNode)o);
/*     */     }
/* 758 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _childrenEqual(ObjectNode other)
/*     */   {
/* 766 */     return this._children.equals(other._children);
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 772 */     return this._children.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 778 */     StringBuilder sb = new StringBuilder(32 + (size() << 4));
/* 779 */     sb.append("{");
/* 780 */     int count = 0;
/* 781 */     for (Map.Entry<String, JsonNode> en : this._children.entrySet()) {
/* 782 */       if (count > 0) {
/* 783 */         sb.append(",");
/*     */       }
/* 785 */       count++;
/* 786 */       TextNode.appendQuoted(sb, (String)en.getKey());
/* 787 */       sb.append(':');
/* 788 */       sb.append(((JsonNode)en.getValue()).toString());
/*     */     }
/* 790 */     sb.append("}");
/* 791 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectNode _put(String fieldName, JsonNode value)
/*     */   {
/* 802 */     this._children.put(fieldName, value);
/* 803 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\node\ObjectNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */