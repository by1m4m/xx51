/*      */ package com.fasterxml.jackson.databind.introspect;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
/*      */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*      */ import com.fasterxml.jackson.databind.PropertyName;
/*      */ import com.fasterxml.jackson.databind.util.EmptyIterator;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class POJOPropertyBuilder
/*      */   extends BeanPropertyDefinition
/*      */   implements Comparable<POJOPropertyBuilder>
/*      */ {
/*      */   protected final boolean _forSerialization;
/*      */   protected final AnnotationIntrospector _annotationIntrospector;
/*      */   protected final PropertyName _name;
/*      */   protected final PropertyName _internalName;
/*      */   protected Linked<AnnotatedField> _fields;
/*      */   protected Linked<AnnotatedParameter> _ctorParameters;
/*      */   protected Linked<AnnotatedMethod> _getters;
/*      */   protected Linked<AnnotatedMethod> _setters;
/*      */   
/*      */   public POJOPropertyBuilder(PropertyName internalName, AnnotationIntrospector ai, boolean forSerialization)
/*      */   {
/*   48 */     this(internalName, internalName, ai, forSerialization);
/*      */   }
/*      */   
/*      */ 
/*      */   protected POJOPropertyBuilder(PropertyName internalName, PropertyName name, AnnotationIntrospector annotationIntrospector, boolean forSerialization)
/*      */   {
/*   54 */     this._internalName = internalName;
/*   55 */     this._name = name;
/*   56 */     this._annotationIntrospector = annotationIntrospector;
/*   57 */     this._forSerialization = forSerialization;
/*      */   }
/*      */   
/*      */   public POJOPropertyBuilder(POJOPropertyBuilder src, PropertyName newName)
/*      */   {
/*   62 */     this._internalName = src._internalName;
/*   63 */     this._name = newName;
/*   64 */     this._annotationIntrospector = src._annotationIntrospector;
/*   65 */     this._fields = src._fields;
/*   66 */     this._ctorParameters = src._ctorParameters;
/*   67 */     this._getters = src._getters;
/*   68 */     this._setters = src._setters;
/*   69 */     this._forSerialization = src._forSerialization;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public POJOPropertyBuilder withName(PropertyName newName)
/*      */   {
/*   80 */     return new POJOPropertyBuilder(this, newName);
/*      */   }
/*      */   
/*      */ 
/*      */   public POJOPropertyBuilder withSimpleName(String newSimpleName)
/*      */   {
/*   86 */     PropertyName newName = this._name.withSimpleName(newSimpleName);
/*   87 */     return newName == this._name ? this : new POJOPropertyBuilder(this, newName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int compareTo(POJOPropertyBuilder other)
/*      */   {
/*  102 */     if (this._ctorParameters != null) {
/*  103 */       if (other._ctorParameters == null) {
/*  104 */         return -1;
/*      */       }
/*  106 */     } else if (other._ctorParameters != null) {
/*  107 */       return 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  112 */     return getName().compareTo(other.getName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  123 */     return this._name == null ? null : this._name.getSimpleName();
/*      */   }
/*      */   
/*      */   public PropertyName getFullName()
/*      */   {
/*  128 */     return this._name;
/*      */   }
/*      */   
/*      */   public String getInternalName() {
/*  132 */     return this._internalName.getSimpleName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PropertyName getWrapperName()
/*      */   {
/*  141 */     AnnotatedMember member = getPrimaryMember();
/*  142 */     return (member == null) || (this._annotationIntrospector == null) ? null : this._annotationIntrospector.findWrapperName(member);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isExplicitlyIncluded()
/*      */   {
/*  156 */     return (_anyExplicits(this._fields)) || (_anyExplicits(this._getters)) || (_anyExplicits(this._setters)) || (_anyExplicits(this._ctorParameters));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isExplicitlyNamed()
/*      */   {
/*  165 */     return (_anyExplicitNames(this._fields)) || (_anyExplicitNames(this._getters)) || (_anyExplicitNames(this._setters)) || (_anyExplicitNames(this._ctorParameters));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasGetter()
/*      */   {
/*  179 */     return this._getters != null;
/*      */   }
/*      */   
/*  182 */   public boolean hasSetter() { return this._setters != null; }
/*      */   
/*      */   public boolean hasField() {
/*  185 */     return this._fields != null;
/*      */   }
/*      */   
/*  188 */   public boolean hasConstructorParameter() { return this._ctorParameters != null; }
/*      */   
/*      */   public boolean couldDeserialize()
/*      */   {
/*  192 */     return (this._ctorParameters != null) || (this._setters != null) || (this._fields != null);
/*      */   }
/*      */   
/*      */   public boolean couldSerialize()
/*      */   {
/*  197 */     return (this._getters != null) || (this._fields != null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public AnnotatedMethod getGetter()
/*      */   {
/*  204 */     Linked<AnnotatedMethod> curr = this._getters;
/*  205 */     if (curr == null) {
/*  206 */       return null;
/*      */     }
/*  208 */     Linked<AnnotatedMethod> next = curr.next;
/*  209 */     if (next == null) {
/*  210 */       return (AnnotatedMethod)curr.value;
/*      */     }
/*  213 */     for (; 
/*  213 */         next != null; next = next.next)
/*      */     {
/*      */ 
/*      */ 
/*  217 */       Class<?> currClass = ((AnnotatedMethod)curr.value).getDeclaringClass();
/*  218 */       Class<?> nextClass = ((AnnotatedMethod)next.value).getDeclaringClass();
/*  219 */       if (currClass != nextClass) {
/*  220 */         if (currClass.isAssignableFrom(nextClass)) {
/*  221 */           curr = next;
/*      */ 
/*      */         }
/*  224 */         else if (nextClass.isAssignableFrom(currClass)) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  234 */         int priNext = _getterPriority((AnnotatedMethod)next.value);
/*  235 */         int priCurr = _getterPriority((AnnotatedMethod)curr.value);
/*      */         
/*  237 */         if (priNext != priCurr) {
/*  238 */           if (priNext < priCurr) {
/*  239 */             curr = next;
/*      */           }
/*      */         }
/*      */         else {
/*  243 */           throw new IllegalArgumentException("Conflicting getter definitions for property \"" + getName() + "\": " + ((AnnotatedMethod)curr.value).getFullName() + " vs " + ((AnnotatedMethod)next.value).getFullName());
/*      */         }
/*      */       }
/*      */     }
/*  247 */     this._getters = curr.withoutNext();
/*  248 */     return (AnnotatedMethod)curr.value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public AnnotatedMethod getSetter()
/*      */   {
/*  255 */     Linked<AnnotatedMethod> curr = this._setters;
/*  256 */     if (curr == null) {
/*  257 */       return null;
/*      */     }
/*  259 */     Linked<AnnotatedMethod> next = curr.next;
/*  260 */     if (next == null) {
/*  261 */       return (AnnotatedMethod)curr.value;
/*      */     }
/*  264 */     for (; 
/*  264 */         next != null; next = next.next)
/*      */     {
/*      */ 
/*      */ 
/*  268 */       Class<?> currClass = ((AnnotatedMethod)curr.value).getDeclaringClass();
/*  269 */       Class<?> nextClass = ((AnnotatedMethod)next.value).getDeclaringClass();
/*  270 */       if (currClass != nextClass) {
/*  271 */         if (currClass.isAssignableFrom(nextClass)) {
/*  272 */           curr = next;
/*      */ 
/*      */         }
/*  275 */         else if (nextClass.isAssignableFrom(currClass)) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*  284 */         int priNext = _setterPriority((AnnotatedMethod)next.value);
/*  285 */         int priCurr = _setterPriority((AnnotatedMethod)curr.value);
/*      */         
/*  287 */         if (priNext != priCurr) {
/*  288 */           if (priNext < priCurr) {
/*  289 */             curr = next;
/*      */           }
/*      */         }
/*      */         else {
/*  293 */           throw new IllegalArgumentException("Conflicting setter definitions for property \"" + getName() + "\": " + ((AnnotatedMethod)curr.value).getFullName() + " vs " + ((AnnotatedMethod)next.value).getFullName());
/*      */         }
/*      */       }
/*      */     }
/*  297 */     this._setters = curr.withoutNext();
/*  298 */     return (AnnotatedMethod)curr.value;
/*      */   }
/*      */   
/*      */ 
/*      */   public AnnotatedField getField()
/*      */   {
/*  304 */     if (this._fields == null) {
/*  305 */       return null;
/*      */     }
/*      */     
/*  308 */     AnnotatedField field = (AnnotatedField)this._fields.value;
/*  309 */     for (Linked<AnnotatedField> next = this._fields.next; 
/*  310 */         next != null; next = next.next) {
/*  311 */       AnnotatedField nextField = (AnnotatedField)next.value;
/*  312 */       Class<?> fieldClass = field.getDeclaringClass();
/*  313 */       Class<?> nextClass = nextField.getDeclaringClass();
/*  314 */       if (fieldClass != nextClass) {
/*  315 */         if (fieldClass.isAssignableFrom(nextClass)) {
/*  316 */           field = nextField;
/*      */ 
/*      */         }
/*  319 */         else if (nextClass.isAssignableFrom(fieldClass)) {}
/*      */ 
/*      */       }
/*      */       else {
/*  323 */         throw new IllegalArgumentException("Multiple fields representing property \"" + getName() + "\": " + field.getFullName() + " vs " + nextField.getFullName());
/*      */       }
/*      */     }
/*  326 */     return field;
/*      */   }
/*      */   
/*      */ 
/*      */   public AnnotatedParameter getConstructorParameter()
/*      */   {
/*  332 */     if (this._ctorParameters == null) {
/*  333 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  343 */     Linked<AnnotatedParameter> curr = this._ctorParameters;
/*      */     do {
/*  345 */       if ((((AnnotatedParameter)curr.value).getOwner() instanceof AnnotatedConstructor)) {
/*  346 */         return (AnnotatedParameter)curr.value;
/*      */       }
/*  348 */       curr = curr.next;
/*  349 */     } while (curr != null);
/*  350 */     return (AnnotatedParameter)this._ctorParameters.value;
/*      */   }
/*      */   
/*      */   public Iterator<AnnotatedParameter> getConstructorParameters()
/*      */   {
/*  355 */     if (this._ctorParameters == null) {
/*  356 */       return EmptyIterator.instance();
/*      */     }
/*  358 */     return new MemberIterator(this._ctorParameters);
/*      */   }
/*      */   
/*      */ 
/*      */   public AnnotatedMember getAccessor()
/*      */   {
/*  364 */     AnnotatedMember m = getGetter();
/*  365 */     if (m == null) {
/*  366 */       m = getField();
/*      */     }
/*  368 */     return m;
/*      */   }
/*      */   
/*      */ 
/*      */   public AnnotatedMember getMutator()
/*      */   {
/*  374 */     AnnotatedMember m = getConstructorParameter();
/*  375 */     if (m == null) {
/*  376 */       m = getSetter();
/*  377 */       if (m == null) {
/*  378 */         m = getField();
/*      */       }
/*      */     }
/*  381 */     return m;
/*      */   }
/*      */   
/*      */   public AnnotatedMember getNonConstructorMutator()
/*      */   {
/*  386 */     AnnotatedMember m = getSetter();
/*  387 */     if (m == null) {
/*  388 */       m = getField();
/*      */     }
/*  390 */     return m;
/*      */   }
/*      */   
/*      */   public AnnotatedMember getPrimaryMember()
/*      */   {
/*  395 */     if (this._forSerialization) {
/*  396 */       return getAccessor();
/*      */     }
/*  398 */     return getMutator();
/*      */   }
/*      */   
/*      */   protected int _getterPriority(AnnotatedMethod m)
/*      */   {
/*  403 */     String name = m.getName();
/*      */     
/*  405 */     if ((name.startsWith("get")) && (name.length() > 3))
/*      */     {
/*  407 */       return 1;
/*      */     }
/*  409 */     if ((name.startsWith("is")) && (name.length() > 2)) {
/*  410 */       return 2;
/*      */     }
/*  412 */     return 3;
/*      */   }
/*      */   
/*      */   protected int _setterPriority(AnnotatedMethod m)
/*      */   {
/*  417 */     String name = m.getName();
/*  418 */     if ((name.startsWith("set")) && (name.length() > 3))
/*      */     {
/*  420 */       return 1;
/*      */     }
/*  422 */     return 2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?>[] findViews()
/*      */   {
/*  433 */     (Class[])fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public Class<?>[] withMember(AnnotatedMember member) {
/*  436 */         return POJOPropertyBuilder.this._annotationIntrospector.findViews(member);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public AnnotationIntrospector.ReferenceProperty findReferenceType()
/*      */   {
/*  443 */     (AnnotationIntrospector.ReferenceProperty)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public AnnotationIntrospector.ReferenceProperty withMember(AnnotatedMember member) {
/*  446 */         return POJOPropertyBuilder.this._annotationIntrospector.findReferenceType(member);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public boolean isTypeId()
/*      */   {
/*  453 */     Boolean b = (Boolean)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public Boolean withMember(AnnotatedMember member) {
/*  456 */         return POJOPropertyBuilder.this._annotationIntrospector.isTypeId(member);
/*      */       }
/*  458 */     });
/*  459 */     return (b != null) && (b.booleanValue());
/*      */   }
/*      */   
/*      */   public PropertyMetadata getMetadata()
/*      */   {
/*  464 */     Boolean b = _findRequired();
/*  465 */     String desc = _findDescription();
/*  466 */     Integer idx = _findIndex();
/*  467 */     String def = _findDefaultValue();
/*  468 */     if ((b == null) && (idx == null) && (def == null)) {
/*  469 */       return desc == null ? PropertyMetadata.STD_REQUIRED_OR_OPTIONAL : PropertyMetadata.STD_REQUIRED_OR_OPTIONAL.withDescription(desc);
/*      */     }
/*      */     
/*  472 */     return PropertyMetadata.construct(b.booleanValue(), desc, idx, def);
/*      */   }
/*      */   
/*      */   protected Boolean _findRequired() {
/*  476 */     Boolean b = (Boolean)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public Boolean withMember(AnnotatedMember member) {
/*  479 */         return POJOPropertyBuilder.this._annotationIntrospector.hasRequiredMarker(member);
/*      */       }
/*  481 */     });
/*  482 */     return b;
/*      */   }
/*      */   
/*      */   protected String _findDescription() {
/*  486 */     (String)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public String withMember(AnnotatedMember member) {
/*  489 */         return POJOPropertyBuilder.this._annotationIntrospector.findPropertyDescription(member);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected Integer _findIndex() {
/*  495 */     (Integer)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public Integer withMember(AnnotatedMember member) {
/*  498 */         return POJOPropertyBuilder.this._annotationIntrospector.findPropertyIndex(member);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected String _findDefaultValue() {
/*  504 */     (String)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public String withMember(AnnotatedMember member) {
/*  507 */         return POJOPropertyBuilder.this._annotationIntrospector.findPropertyDefaultValue(member);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public ObjectIdInfo findObjectIdInfo()
/*      */   {
/*  514 */     (ObjectIdInfo)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public ObjectIdInfo withMember(AnnotatedMember member) {
/*  517 */         ObjectIdInfo info = POJOPropertyBuilder.this._annotationIntrospector.findObjectIdInfo(member);
/*  518 */         if (info != null) {
/*  519 */           info = POJOPropertyBuilder.this._annotationIntrospector.findObjectReferenceInfo(member, info);
/*      */         }
/*  521 */         return info;
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public JsonInclude.Include findInclusion()
/*      */   {
/*  528 */     if (this._annotationIntrospector == null) {
/*  529 */       return null;
/*      */     }
/*  531 */     AnnotatedMember am = getAccessor();
/*  532 */     return this._annotationIntrospector.findSerializationInclusion(am, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addField(AnnotatedField a, PropertyName name, boolean explName, boolean visible, boolean ignored)
/*      */   {
/*  542 */     this._fields = new Linked(a, this._fields, name, explName, visible, ignored);
/*      */   }
/*      */   
/*      */   public void addCtor(AnnotatedParameter a, PropertyName name, boolean explName, boolean visible, boolean ignored) {
/*  546 */     this._ctorParameters = new Linked(a, this._ctorParameters, name, explName, visible, ignored);
/*      */   }
/*      */   
/*      */   public void addGetter(AnnotatedMethod a, PropertyName name, boolean explName, boolean visible, boolean ignored) {
/*  550 */     this._getters = new Linked(a, this._getters, name, explName, visible, ignored);
/*      */   }
/*      */   
/*      */   public void addSetter(AnnotatedMethod a, PropertyName name, boolean explName, boolean visible, boolean ignored) {
/*  554 */     this._setters = new Linked(a, this._setters, name, explName, visible, ignored);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addAll(POJOPropertyBuilder src)
/*      */   {
/*  563 */     this._fields = merge(this._fields, src._fields);
/*  564 */     this._ctorParameters = merge(this._ctorParameters, src._ctorParameters);
/*  565 */     this._getters = merge(this._getters, src._getters);
/*  566 */     this._setters = merge(this._setters, src._setters);
/*      */   }
/*      */   
/*      */   private static <T> Linked<T> merge(Linked<T> chain1, Linked<T> chain2)
/*      */   {
/*  571 */     if (chain1 == null) {
/*  572 */       return chain2;
/*      */     }
/*  574 */     if (chain2 == null) {
/*  575 */       return chain1;
/*      */     }
/*  577 */     return chain1.append(chain2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void addField(AnnotatedField a, String name, boolean visible, boolean ignored)
/*      */   {
/*  591 */     addField(a, _propName(name), name != null, visible, ignored);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public void addField(AnnotatedField a, String name, boolean explName, boolean visible, boolean ignored) {
/*  596 */     addField(a, _propName(name), explName, visible, ignored);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void addCtor(AnnotatedParameter a, String name, boolean visible, boolean ignored)
/*      */   {
/*  606 */     addCtor(a, _propName(name), name != null, visible, ignored);
/*      */   }
/*      */   
/*      */   @Deprecated
/*  610 */   public void addCtor(AnnotatedParameter a, String name, boolean explName, boolean visible, boolean ignored) { addCtor(a, _propName(name), explName, visible, ignored); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void addGetter(AnnotatedMethod a, String name, boolean visible, boolean ignored)
/*      */   {
/*  620 */     addGetter(a, _propName(name), name != null, visible, ignored);
/*      */   }
/*      */   
/*      */   @Deprecated
/*  624 */   public void addGetter(AnnotatedMethod a, String name, boolean explName, boolean visible, boolean ignored) { addGetter(a, _propName(name), explName, visible, ignored); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void addSetter(AnnotatedMethod a, String name, boolean visible, boolean ignored)
/*      */   {
/*  634 */     addSetter(a, _propName(name), name != null, visible, ignored);
/*      */   }
/*      */   
/*      */   @Deprecated
/*  638 */   public void addSetter(AnnotatedMethod a, String name, boolean explName, boolean visible, boolean ignored) { addSetter(a, _propName(name), explName, visible, ignored); }
/*      */   
/*      */   private PropertyName _propName(String simple)
/*      */   {
/*  642 */     return PropertyName.construct(simple, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeIgnored()
/*      */   {
/*  657 */     this._fields = _removeIgnored(this._fields);
/*  658 */     this._getters = _removeIgnored(this._getters);
/*  659 */     this._setters = _removeIgnored(this._setters);
/*  660 */     this._ctorParameters = _removeIgnored(this._ctorParameters);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeNonVisible(boolean force)
/*      */   {
/*  675 */     this._getters = _removeNonVisible(this._getters);
/*  676 */     this._ctorParameters = _removeNonVisible(this._ctorParameters);
/*      */     
/*  678 */     if ((force) || (this._getters == null)) {
/*  679 */       this._fields = _removeNonVisible(this._fields);
/*  680 */       this._setters = _removeNonVisible(this._setters);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeConstructors()
/*      */   {
/*  690 */     this._ctorParameters = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void trimByVisibility()
/*      */   {
/*  700 */     this._fields = _trimByVisibility(this._fields);
/*  701 */     this._getters = _trimByVisibility(this._getters);
/*  702 */     this._setters = _trimByVisibility(this._setters);
/*  703 */     this._ctorParameters = _trimByVisibility(this._ctorParameters);
/*      */   }
/*      */   
/*      */ 
/*      */   public void mergeAnnotations(boolean forSerialization)
/*      */   {
/*  709 */     if (forSerialization) {
/*  710 */       if (this._getters != null) {
/*  711 */         AnnotationMap ann = _mergeAnnotations(0, new Linked[] { this._getters, this._fields, this._ctorParameters, this._setters });
/*  712 */         this._getters = this._getters.withValue(((AnnotatedMethod)this._getters.value).withAnnotations(ann));
/*  713 */       } else if (this._fields != null) {
/*  714 */         AnnotationMap ann = _mergeAnnotations(0, new Linked[] { this._fields, this._ctorParameters, this._setters });
/*  715 */         this._fields = this._fields.withValue(((AnnotatedField)this._fields.value).withAnnotations(ann));
/*      */       }
/*      */     }
/*  718 */     else if (this._ctorParameters != null) {
/*  719 */       AnnotationMap ann = _mergeAnnotations(0, new Linked[] { this._ctorParameters, this._setters, this._fields, this._getters });
/*  720 */       this._ctorParameters = this._ctorParameters.withValue(((AnnotatedParameter)this._ctorParameters.value).withAnnotations(ann));
/*  721 */     } else if (this._setters != null) {
/*  722 */       AnnotationMap ann = _mergeAnnotations(0, new Linked[] { this._setters, this._fields, this._getters });
/*  723 */       this._setters = this._setters.withValue(((AnnotatedMethod)this._setters.value).withAnnotations(ann));
/*  724 */     } else if (this._fields != null) {
/*  725 */       AnnotationMap ann = _mergeAnnotations(0, new Linked[] { this._fields, this._getters });
/*  726 */       this._fields = this._fields.withValue(((AnnotatedField)this._fields.value).withAnnotations(ann));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private AnnotationMap _mergeAnnotations(int index, Linked<? extends AnnotatedMember>... nodes)
/*      */   {
/*  733 */     AnnotationMap ann = ((AnnotatedMember)nodes[index].value).getAllAnnotations();
/*  734 */     index++;
/*  735 */     for (; index < nodes.length; index++) {
/*  736 */       if (nodes[index] != null) {
/*  737 */         return AnnotationMap.merge(ann, _mergeAnnotations(index, nodes));
/*      */       }
/*      */     }
/*  740 */     return ann;
/*      */   }
/*      */   
/*      */   private <T> Linked<T> _removeIgnored(Linked<T> node)
/*      */   {
/*  745 */     if (node == null) {
/*  746 */       return node;
/*      */     }
/*  748 */     return node.withoutIgnored();
/*      */   }
/*      */   
/*      */   private <T> Linked<T> _removeNonVisible(Linked<T> node)
/*      */   {
/*  753 */     if (node == null) {
/*  754 */       return node;
/*      */     }
/*  756 */     return node.withoutNonVisible();
/*      */   }
/*      */   
/*      */   private <T> Linked<T> _trimByVisibility(Linked<T> node)
/*      */   {
/*  761 */     if (node == null) {
/*  762 */       return node;
/*      */     }
/*  764 */     return node.trimByVisibility();
/*      */   }
/*      */   
/*      */   private <T> boolean _anyExplicits(Linked<T> n)
/*      */   {
/*  775 */     for (; 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  775 */         n != null; n = n.next) {
/*  776 */       if ((n.name != null) && (n.name.hasSimpleName())) {
/*  777 */         return true;
/*      */       }
/*      */     }
/*  780 */     return false;
/*      */   }
/*      */   
/*      */   private <T> boolean _anyExplicitNames(Linked<T> n)
/*      */   {
/*  785 */     for (; n != null; n = n.next) {
/*  786 */       if ((n.name != null) && (n.isNameExplicit)) {
/*  787 */         return true;
/*      */       }
/*      */     }
/*  790 */     return false;
/*      */   }
/*      */   
/*      */   public boolean anyVisible() {
/*  794 */     return (_anyVisible(this._fields)) || (_anyVisible(this._getters)) || (_anyVisible(this._setters)) || (_anyVisible(this._ctorParameters));
/*      */   }
/*      */   
/*      */   private <T> boolean _anyVisible(Linked<T> n)
/*      */   {
/*  803 */     for (; 
/*      */         
/*      */ 
/*      */ 
/*  803 */         n != null; n = n.next) {
/*  804 */       if (n.isVisible) {
/*  805 */         return true;
/*      */       }
/*      */     }
/*  808 */     return false;
/*      */   }
/*      */   
/*      */   public boolean anyIgnorals() {
/*  812 */     return (_anyIgnorals(this._fields)) || (_anyIgnorals(this._getters)) || (_anyIgnorals(this._setters)) || (_anyIgnorals(this._ctorParameters));
/*      */   }
/*      */   
/*      */   private <T> boolean _anyIgnorals(Linked<T> n)
/*      */   {
/*  821 */     for (; 
/*      */         
/*      */ 
/*      */ 
/*  821 */         n != null; n = n.next) {
/*  822 */       if (n.isMarkedIgnored) {
/*  823 */         return true;
/*      */       }
/*      */     }
/*  826 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public String findNewName()
/*      */   {
/*  835 */     Collection<PropertyName> l = findExplicitNames();
/*  836 */     if (l == null) {
/*  837 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  841 */     if (l.size() > 1) {
/*  842 */       throw new IllegalStateException("Conflicting/ambiguous property name definitions (implicit name '" + this._name + "'): found more than one explicit name: " + l);
/*      */     }
/*      */     
/*      */ 
/*  846 */     PropertyName first = (PropertyName)l.iterator().next();
/*  847 */     if (first.equals(this._name)) {
/*  848 */       return null;
/*      */     }
/*  850 */     return first.getSimpleName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<PropertyName> findExplicitNames()
/*      */   {
/*  861 */     Set<PropertyName> renamed = null;
/*  862 */     renamed = _findExplicitNames(this._fields, renamed);
/*  863 */     renamed = _findExplicitNames(this._getters, renamed);
/*  864 */     renamed = _findExplicitNames(this._setters, renamed);
/*  865 */     renamed = _findExplicitNames(this._ctorParameters, renamed);
/*  866 */     if (renamed == null) {
/*  867 */       return Collections.emptySet();
/*      */     }
/*  869 */     return renamed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collection<POJOPropertyBuilder> explode(Collection<PropertyName> newNames)
/*      */   {
/*  882 */     HashMap<PropertyName, POJOPropertyBuilder> props = new HashMap();
/*  883 */     _explode(newNames, props, this._fields);
/*  884 */     _explode(newNames, props, this._getters);
/*  885 */     _explode(newNames, props, this._setters);
/*  886 */     _explode(newNames, props, this._ctorParameters);
/*  887 */     return props.values();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _explode(Collection<PropertyName> newNames, Map<PropertyName, POJOPropertyBuilder> props, Linked<?> accessors)
/*      */   {
/*  895 */     Linked<?> firstAcc = accessors;
/*  896 */     for (Linked<?> node = accessors; node != null; node = node.next) {
/*  897 */       PropertyName name = node.name;
/*  898 */       if ((!node.isNameExplicit) || (name == null))
/*      */       {
/*  900 */         if (node.isVisible)
/*      */         {
/*      */ 
/*      */ 
/*  904 */           throw new IllegalStateException("Conflicting/ambiguous property name definitions (implicit name '" + this._name + "'): found multiple explicit names: " + newNames + ", but also implicit accessor: " + node);
/*      */         }
/*      */       }
/*      */       else {
/*  908 */         POJOPropertyBuilder prop = (POJOPropertyBuilder)props.get(name);
/*  909 */         if (prop == null) {
/*  910 */           prop = new POJOPropertyBuilder(this._internalName, name, this._annotationIntrospector, this._forSerialization);
/*  911 */           props.put(name, prop);
/*      */         }
/*      */         
/*  914 */         if (firstAcc == this._fields) {
/*  915 */           Linked<AnnotatedField> n2 = node;
/*  916 */           prop._fields = n2.withNext(prop._fields);
/*  917 */         } else if (firstAcc == this._getters) {
/*  918 */           Linked<AnnotatedMethod> n2 = node;
/*  919 */           prop._getters = n2.withNext(prop._getters);
/*  920 */         } else if (firstAcc == this._setters) {
/*  921 */           Linked<AnnotatedMethod> n2 = node;
/*  922 */           prop._setters = n2.withNext(prop._setters);
/*  923 */         } else if (firstAcc == this._ctorParameters) {
/*  924 */           Linked<AnnotatedParameter> n2 = node;
/*  925 */           prop._ctorParameters = n2.withNext(prop._ctorParameters);
/*      */         } else {
/*  927 */           throw new IllegalStateException("Internal error: mismatched accessors, property: " + this);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private Set<PropertyName> _findExplicitNames(Linked<? extends AnnotatedMember> node, Set<PropertyName> renamed)
/*      */   {
/*  935 */     for (; node != null; node = node.next)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  942 */       if ((node.isNameExplicit) && (node.name != null))
/*      */       {
/*      */ 
/*  945 */         if (renamed == null) {
/*  946 */           renamed = new HashSet();
/*      */         }
/*  948 */         renamed.add(node.name);
/*      */       } }
/*  950 */     return renamed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  957 */     StringBuilder sb = new StringBuilder();
/*  958 */     sb.append("[Property '").append(this._name).append("'; ctors: ").append(this._ctorParameters).append(", field(s): ").append(this._fields).append(", getter(s): ").append(this._getters).append(", setter(s): ").append(this._setters);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  964 */     sb.append("]");
/*  965 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected <T> T fromMemberAnnotations(WithMember<T> func)
/*      */   {
/*  980 */     T result = null;
/*  981 */     if (this._annotationIntrospector != null) {
/*  982 */       if (this._forSerialization) {
/*  983 */         if (this._getters != null) {
/*  984 */           result = func.withMember((AnnotatedMember)this._getters.value);
/*      */         }
/*      */       } else {
/*  987 */         if (this._ctorParameters != null) {
/*  988 */           result = func.withMember((AnnotatedMember)this._ctorParameters.value);
/*      */         }
/*  990 */         if ((result == null) && (this._setters != null)) {
/*  991 */           result = func.withMember((AnnotatedMember)this._setters.value);
/*      */         }
/*      */       }
/*  994 */       if ((result == null) && (this._fields != null)) {
/*  995 */         result = func.withMember((AnnotatedMember)this._fields.value);
/*      */       }
/*      */     }
/*  998 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static abstract interface WithMember<T>
/*      */   {
/*      */     public abstract T withMember(AnnotatedMember paramAnnotatedMember);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class MemberIterator<T extends AnnotatedMember>
/*      */     implements Iterator<T>
/*      */   {
/*      */     private POJOPropertyBuilder.Linked<T> next;
/*      */     
/*      */ 
/*      */ 
/*      */     public MemberIterator(POJOPropertyBuilder.Linked<T> first)
/*      */     {
/* 1020 */       this.next = first;
/*      */     }
/*      */     
/*      */     public boolean hasNext()
/*      */     {
/* 1025 */       return this.next != null;
/*      */     }
/*      */     
/*      */     public T next()
/*      */     {
/* 1030 */       if (this.next == null) throw new NoSuchElementException();
/* 1031 */       T result = (AnnotatedMember)this.next.value;
/* 1032 */       this.next = this.next.next;
/* 1033 */       return result;
/*      */     }
/*      */     
/*      */     public void remove()
/*      */     {
/* 1038 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class Linked<T>
/*      */   {
/*      */     public final T value;
/*      */     
/*      */     public final Linked<T> next;
/*      */     
/*      */     public final PropertyName name;
/*      */     
/*      */     public final boolean isNameExplicit;
/*      */     
/*      */     public final boolean isVisible;
/*      */     
/*      */     public final boolean isMarkedIgnored;
/*      */     
/*      */ 
/*      */     public Linked(T v, Linked<T> n, PropertyName name, boolean explName, boolean visible, boolean ignored)
/*      */     {
/* 1060 */       this.value = v;
/* 1061 */       this.next = n;
/*      */       
/* 1063 */       this.name = ((name == null) || (name.isEmpty()) ? null : name);
/*      */       
/* 1065 */       if (explName) {
/* 1066 */         if (this.name == null) {
/* 1067 */           throw new IllegalArgumentException("Can not pass true for 'explName' if name is null/empty");
/*      */         }
/*      */         
/*      */ 
/* 1071 */         if (!name.hasSimpleName()) {
/* 1072 */           explName = false;
/*      */         }
/*      */       }
/*      */       
/* 1076 */       this.isNameExplicit = explName;
/* 1077 */       this.isVisible = visible;
/* 1078 */       this.isMarkedIgnored = ignored;
/*      */     }
/*      */     
/*      */     public Linked<T> withoutNext() {
/* 1082 */       if (this.next == null) {
/* 1083 */         return this;
/*      */       }
/* 1085 */       return new Linked(this.value, null, this.name, this.isNameExplicit, this.isVisible, this.isMarkedIgnored);
/*      */     }
/*      */     
/*      */     public Linked<T> withValue(T newValue) {
/* 1089 */       if (newValue == this.value) {
/* 1090 */         return this;
/*      */       }
/* 1092 */       return new Linked(newValue, this.next, this.name, this.isNameExplicit, this.isVisible, this.isMarkedIgnored);
/*      */     }
/*      */     
/*      */     public Linked<T> withNext(Linked<T> newNext) {
/* 1096 */       if (newNext == this.next) {
/* 1097 */         return this;
/*      */       }
/* 1099 */       return new Linked(this.value, newNext, this.name, this.isNameExplicit, this.isVisible, this.isMarkedIgnored);
/*      */     }
/*      */     
/*      */     public Linked<T> withoutIgnored() {
/* 1103 */       if (this.isMarkedIgnored) {
/* 1104 */         return this.next == null ? null : this.next.withoutIgnored();
/*      */       }
/* 1106 */       if (this.next != null) {
/* 1107 */         Linked<T> newNext = this.next.withoutIgnored();
/* 1108 */         if (newNext != this.next) {
/* 1109 */           return withNext(newNext);
/*      */         }
/*      */       }
/* 1112 */       return this;
/*      */     }
/*      */     
/*      */     public Linked<T> withoutNonVisible() {
/* 1116 */       Linked<T> newNext = this.next == null ? null : this.next.withoutNonVisible();
/* 1117 */       return this.isVisible ? withNext(newNext) : newNext;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Linked<T> append(Linked<T> appendable)
/*      */     {
/* 1125 */       if (this.next == null) {
/* 1126 */         return withNext(appendable);
/*      */       }
/* 1128 */       return withNext(this.next.append(appendable));
/*      */     }
/*      */     
/*      */     public Linked<T> trimByVisibility() {
/* 1132 */       if (this.next == null) {
/* 1133 */         return this;
/*      */       }
/* 1135 */       Linked<T> newNext = this.next.trimByVisibility();
/* 1136 */       if (this.name != null) {
/* 1137 */         if (newNext.name == null) {
/* 1138 */           return withNext(null);
/*      */         }
/*      */         
/* 1141 */         return withNext(newNext);
/*      */       }
/* 1143 */       if (newNext.name != null) {
/* 1144 */         return newNext;
/*      */       }
/*      */       
/* 1147 */       if (this.isVisible == newNext.isVisible) {
/* 1148 */         return withNext(newNext);
/*      */       }
/* 1150 */       return this.isVisible ? withNext(null) : newNext;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1155 */       String msg = this.value.toString() + "[visible=" + this.isVisible + ",ignore=" + this.isMarkedIgnored + ",explicitName=" + this.isNameExplicit + "]";
/*      */       
/* 1157 */       if (this.next != null) {
/* 1158 */         msg = msg + ", " + this.next.toString();
/*      */       }
/* 1160 */       return msg;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\POJOPropertyBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */