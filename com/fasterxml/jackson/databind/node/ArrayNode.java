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
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayNode
/*     */   extends ContainerNode<ArrayNode>
/*     */ {
/*  23 */   private final List<JsonNode> _children = new ArrayList();
/*     */   
/*  25 */   public ArrayNode(JsonNodeFactory nc) { super(nc); }
/*     */   
/*     */   protected JsonNode _at(JsonPointer ptr)
/*     */   {
/*  29 */     return get(ptr.getMatchingIndex());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode deepCopy()
/*     */   {
/*  37 */     ArrayNode ret = new ArrayNode(this._nodeFactory);
/*     */     
/*  39 */     for (JsonNode element : this._children) {
/*  40 */       ret._children.add(element.deepCopy());
/*     */     }
/*  42 */     return ret;
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
/*  53 */     return JsonNodeType.ARRAY;
/*     */   }
/*     */   
/*  56 */   public JsonToken asToken() { return JsonToken.START_ARRAY; }
/*     */   
/*     */   public int size()
/*     */   {
/*  60 */     return this._children.size();
/*     */   }
/*     */   
/*     */   public Iterator<JsonNode> elements()
/*     */   {
/*  65 */     return this._children.iterator();
/*     */   }
/*     */   
/*     */   public JsonNode get(int index)
/*     */   {
/*  70 */     if ((index >= 0) && (index < this._children.size())) {
/*  71 */       return (JsonNode)this._children.get(index);
/*     */     }
/*  73 */     return null;
/*     */   }
/*     */   
/*     */   public JsonNode get(String fieldName) {
/*  77 */     return null;
/*     */   }
/*     */   
/*  80 */   public JsonNode path(String fieldName) { return MissingNode.getInstance(); }
/*     */   
/*     */   public JsonNode path(int index)
/*     */   {
/*  84 */     if ((index >= 0) && (index < this._children.size())) {
/*  85 */       return (JsonNode)this._children.get(index);
/*     */     }
/*  87 */     return MissingNode.getInstance();
/*     */   }
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
/*  99 */     List<JsonNode> c = this._children;
/* 100 */     int size = c.size();
/* 101 */     jg.writeStartArray(size);
/* 102 */     for (int i = 0; i < size; i++)
/*     */     {
/*     */ 
/* 105 */       ((BaseJsonNode)c.get(i)).serialize(jg, provider);
/*     */     }
/* 107 */     jg.writeEndArray();
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 114 */     typeSer.writeTypePrefixForArray(this, jg);
/* 115 */     for (JsonNode n : this._children) {
/* 116 */       ((BaseJsonNode)n).serialize(jg, provider);
/*     */     }
/* 118 */     typeSer.writeTypeSuffixForArray(this, jg);
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
/* 130 */     for (JsonNode node : this._children) {
/* 131 */       JsonNode value = node.findValue(fieldName);
/* 132 */       if (value != null) {
/* 133 */         return value;
/*     */       }
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 142 */     for (JsonNode node : this._children) {
/* 143 */       foundSoFar = node.findValues(fieldName, foundSoFar);
/*     */     }
/* 145 */     return foundSoFar;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> findValuesAsText(String fieldName, List<String> foundSoFar)
/*     */   {
/* 151 */     for (JsonNode node : this._children) {
/* 152 */       foundSoFar = node.findValuesAsText(fieldName, foundSoFar);
/*     */     }
/* 154 */     return foundSoFar;
/*     */   }
/*     */   
/*     */ 
/*     */   public ObjectNode findParent(String fieldName)
/*     */   {
/* 160 */     for (JsonNode node : this._children) {
/* 161 */       JsonNode parent = node.findParent(fieldName);
/* 162 */       if (parent != null) {
/* 163 */         return (ObjectNode)parent;
/*     */       }
/*     */     }
/* 166 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 172 */     for (JsonNode node : this._children) {
/* 173 */       foundSoFar = node.findParents(fieldName, foundSoFar);
/*     */     }
/* 175 */     return foundSoFar;
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
/*     */   public JsonNode set(int index, JsonNode value)
/*     */   {
/* 197 */     if (value == null) {
/* 198 */       value = nullNode();
/*     */     }
/* 200 */     if ((index < 0) || (index >= this._children.size())) {
/* 201 */       throw new IndexOutOfBoundsException("Illegal index " + index + ", array size " + size());
/*     */     }
/* 203 */     return (JsonNode)this._children.set(index, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(JsonNode value)
/*     */   {
/* 213 */     if (value == null) {
/* 214 */       value = nullNode();
/*     */     }
/* 216 */     _add(value);
/* 217 */     return this;
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
/*     */   public ArrayNode addAll(ArrayNode other)
/*     */   {
/* 230 */     this._children.addAll(other._children);
/* 231 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode addAll(Collection<? extends JsonNode> nodes)
/*     */   {
/* 243 */     this._children.addAll(nodes);
/* 244 */     return this;
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
/*     */   public ArrayNode insert(int index, JsonNode value)
/*     */   {
/* 258 */     if (value == null) {
/* 259 */       value = nullNode();
/*     */     }
/* 261 */     _insert(index, value);
/* 262 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode remove(int index)
/*     */   {
/* 274 */     if ((index >= 0) && (index < this._children.size())) {
/* 275 */       return (JsonNode)this._children.remove(index);
/*     */     }
/* 277 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode removeAll()
/*     */   {
/* 289 */     this._children.clear();
/* 290 */     return this;
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
/*     */   public ArrayNode addArray()
/*     */   {
/* 307 */     ArrayNode n = arrayNode();
/* 308 */     _add(n);
/* 309 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode addObject()
/*     */   {
/* 320 */     ObjectNode n = objectNode();
/* 321 */     _add(n);
/* 322 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode addPOJO(Object value)
/*     */   {
/* 333 */     if (value == null) {
/* 334 */       addNull();
/*     */     } else {
/* 336 */       _add(pojoNode(value));
/*     */     }
/* 338 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode addNull()
/*     */   {
/* 348 */     _add(nullNode());
/* 349 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(int v)
/*     */   {
/* 358 */     _add(numberNode(v));
/* 359 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(Integer value)
/*     */   {
/* 369 */     if (value == null) {
/* 370 */       return addNull();
/*     */     }
/* 372 */     return _add(numberNode(value.intValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(long v)
/*     */   {
/* 380 */     return _add(numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(Long value)
/*     */   {
/* 389 */     if (value == null) {
/* 390 */       return addNull();
/*     */     }
/* 392 */     return _add(numberNode(value.longValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(float v)
/*     */   {
/* 401 */     return _add(numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(Float value)
/*     */   {
/* 411 */     if (value == null) {
/* 412 */       return addNull();
/*     */     }
/* 414 */     return _add(numberNode(value.floatValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(double v)
/*     */   {
/* 423 */     return _add(numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(Double value)
/*     */   {
/* 433 */     if (value == null) {
/* 434 */       return addNull();
/*     */     }
/* 436 */     return _add(numberNode(value.doubleValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(BigDecimal v)
/*     */   {
/* 445 */     if (v == null) {
/* 446 */       return addNull();
/*     */     }
/* 448 */     return _add(numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(String v)
/*     */   {
/* 457 */     if (v == null) {
/* 458 */       return addNull();
/*     */     }
/* 460 */     return _add(textNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(boolean v)
/*     */   {
/* 469 */     return _add(booleanNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(Boolean value)
/*     */   {
/* 479 */     if (value == null) {
/* 480 */       return addNull();
/*     */     }
/* 482 */     return _add(booleanNode(value.booleanValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode add(byte[] v)
/*     */   {
/* 492 */     if (v == null) {
/* 493 */       return addNull();
/*     */     }
/* 495 */     return _add(binaryNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insertArray(int index)
/*     */   {
/* 506 */     ArrayNode n = arrayNode();
/* 507 */     _insert(index, n);
/* 508 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectNode insertObject(int index)
/*     */   {
/* 520 */     ObjectNode n = objectNode();
/* 521 */     _insert(index, n);
/* 522 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insertPOJO(int index, Object value)
/*     */   {
/* 533 */     if (value == null) {
/* 534 */       return insertNull(index);
/*     */     }
/* 536 */     return _insert(index, pojoNode(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insertNull(int index)
/*     */   {
/* 547 */     _insert(index, nullNode());
/* 548 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, int v)
/*     */   {
/* 558 */     _insert(index, numberNode(v));
/* 559 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, Integer value)
/*     */   {
/* 569 */     if (value == null) {
/* 570 */       insertNull(index);
/*     */     } else {
/* 572 */       _insert(index, numberNode(value.intValue()));
/*     */     }
/* 574 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, long v)
/*     */   {
/* 584 */     return _insert(index, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, Long value)
/*     */   {
/* 594 */     if (value == null) {
/* 595 */       return insertNull(index);
/*     */     }
/* 597 */     return _insert(index, numberNode(value.longValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, float v)
/*     */   {
/* 607 */     return _insert(index, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, Float value)
/*     */   {
/* 617 */     if (value == null) {
/* 618 */       return insertNull(index);
/*     */     }
/* 620 */     return _insert(index, numberNode(value.floatValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, double v)
/*     */   {
/* 630 */     return _insert(index, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, Double value)
/*     */   {
/* 640 */     if (value == null) {
/* 641 */       return insertNull(index);
/*     */     }
/* 643 */     return _insert(index, numberNode(value.doubleValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, BigDecimal v)
/*     */   {
/* 653 */     if (v == null) {
/* 654 */       return insertNull(index);
/*     */     }
/* 656 */     return _insert(index, numberNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, String v)
/*     */   {
/* 666 */     if (v == null) {
/* 667 */       return insertNull(index);
/*     */     }
/* 669 */     return _insert(index, textNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, boolean v)
/*     */   {
/* 679 */     return _insert(index, booleanNode(v));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, Boolean value)
/*     */   {
/* 689 */     if (value == null) {
/* 690 */       return insertNull(index);
/*     */     }
/* 692 */     return _insert(index, booleanNode(value.booleanValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode insert(int index, byte[] v)
/*     */   {
/* 703 */     if (v == null) {
/* 704 */       return insertNull(index);
/*     */     }
/* 706 */     return _insert(index, binaryNode(v));
/*     */   }
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
/* 718 */     if (o == this) return true;
/* 719 */     if (o == null) return false;
/* 720 */     if ((o instanceof ArrayNode)) {
/* 721 */       return this._children.equals(((ArrayNode)o)._children);
/*     */     }
/* 723 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean _childrenEqual(ArrayNode other)
/*     */   {
/* 730 */     return this._children.equals(other._children);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 735 */     return this._children.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 741 */     StringBuilder sb = new StringBuilder(16 + (size() << 4));
/* 742 */     sb.append('[');
/* 743 */     int i = 0; for (int len = this._children.size(); i < len; i++) {
/* 744 */       if (i > 0) {
/* 745 */         sb.append(',');
/*     */       }
/* 747 */       sb.append(((JsonNode)this._children.get(i)).toString());
/*     */     }
/* 749 */     sb.append(']');
/* 750 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ArrayNode _add(JsonNode node)
/*     */   {
/* 760 */     this._children.add(node);
/* 761 */     return this;
/*     */   }
/*     */   
/*     */   protected ArrayNode _insert(int index, JsonNode node)
/*     */   {
/* 766 */     if (index < 0) {
/* 767 */       this._children.add(0, node);
/* 768 */     } else if (index >= this._children.size()) {
/* 769 */       this._children.add(node);
/*     */     } else {
/* 771 */       this._children.add(index, node);
/*     */     }
/* 773 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\node\ArrayNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */