/*      */ package com.fasterxml.jackson.databind.introspect;
/*      */ 
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ public final class AnnotatedClass extends Annotated
/*      */ {
/*   15 */   private static final AnnotationMap[] NO_ANNOTATION_MAPS = new AnnotationMap[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Class<?> _class;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final List<Class<?>> _superTypes;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final AnnotationIntrospector _annotationIntrospector;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ClassIntrospector.MixInResolver _mixInResolver;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Class<?> _primaryMixIn;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AnnotationMap _classAnnotations;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   71 */   protected boolean _creatorsResolved = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AnnotatedConstructor _defaultConstructor;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List<AnnotatedConstructor> _constructors;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List<AnnotatedMethod> _creatorMethods;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AnnotatedMethodMap _memberMethods;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List<AnnotatedField> _fields;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private AnnotatedClass(Class<?> cls, List<Class<?>> superTypes, AnnotationIntrospector aintr, ClassIntrospector.MixInResolver mir, AnnotationMap classAnnotations)
/*      */   {
/*  115 */     this._class = cls;
/*  116 */     this._superTypes = superTypes;
/*  117 */     this._annotationIntrospector = aintr;
/*  118 */     this._mixInResolver = mir;
/*  119 */     this._primaryMixIn = (this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(this._class));
/*      */     
/*  121 */     this._classAnnotations = classAnnotations;
/*      */   }
/*      */   
/*      */   public AnnotatedClass withAnnotations(AnnotationMap ann)
/*      */   {
/*  126 */     return new AnnotatedClass(this._class, this._superTypes, this._annotationIntrospector, this._mixInResolver, ann);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AnnotatedClass construct(Class<?> cls, AnnotationIntrospector aintr, ClassIntrospector.MixInResolver mir)
/*      */   {
/*  138 */     return new AnnotatedClass(cls, com.fasterxml.jackson.databind.util.ClassUtil.findSuperTypes(cls, null), aintr, mir, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AnnotatedClass constructWithoutSuperTypes(Class<?> cls, AnnotationIntrospector aintr, ClassIntrospector.MixInResolver mir)
/*      */   {
/*  150 */     return new AnnotatedClass(cls, java.util.Collections.emptyList(), aintr, mir, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> getAnnotated()
/*      */   {
/*  161 */     return this._class;
/*      */   }
/*      */   
/*  164 */   public int getModifiers() { return this._class.getModifiers(); }
/*      */   
/*      */   public String getName() {
/*  167 */     return this._class.getName();
/*      */   }
/*      */   
/*      */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*      */   {
/*  172 */     if (this._classAnnotations == null) {
/*  173 */       resolveClassAnnotations();
/*      */     }
/*  175 */     return this._classAnnotations.get(acls);
/*      */   }
/*      */   
/*      */   public java.lang.reflect.Type getGenericType()
/*      */   {
/*  180 */     return this._class;
/*      */   }
/*      */   
/*      */   public Class<?> getRawType()
/*      */   {
/*  185 */     return this._class;
/*      */   }
/*      */   
/*      */   public Iterable<Annotation> annotations()
/*      */   {
/*  190 */     if (this._classAnnotations == null) {
/*  191 */       resolveClassAnnotations();
/*      */     }
/*  193 */     return this._classAnnotations.annotations();
/*      */   }
/*      */   
/*      */   protected AnnotationMap getAllAnnotations()
/*      */   {
/*  198 */     if (this._classAnnotations == null) {
/*  199 */       resolveClassAnnotations();
/*      */     }
/*  201 */     return this._classAnnotations;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public com.fasterxml.jackson.databind.util.Annotations getAnnotations()
/*      */   {
/*  211 */     if (this._classAnnotations == null) {
/*  212 */       resolveClassAnnotations();
/*      */     }
/*  214 */     return this._classAnnotations;
/*      */   }
/*      */   
/*      */   public boolean hasAnnotations() {
/*  218 */     if (this._classAnnotations == null) {
/*  219 */       resolveClassAnnotations();
/*      */     }
/*  221 */     return this._classAnnotations.size() > 0;
/*      */   }
/*      */   
/*      */   public AnnotatedConstructor getDefaultConstructor()
/*      */   {
/*  226 */     if (!this._creatorsResolved) {
/*  227 */       resolveCreators();
/*      */     }
/*  229 */     return this._defaultConstructor;
/*      */   }
/*      */   
/*      */   public List<AnnotatedConstructor> getConstructors()
/*      */   {
/*  234 */     if (!this._creatorsResolved) {
/*  235 */       resolveCreators();
/*      */     }
/*  237 */     return this._constructors;
/*      */   }
/*      */   
/*      */   public List<AnnotatedMethod> getStaticMethods()
/*      */   {
/*  242 */     if (!this._creatorsResolved) {
/*  243 */       resolveCreators();
/*      */     }
/*  245 */     return this._creatorMethods;
/*      */   }
/*      */   
/*      */   public Iterable<AnnotatedMethod> memberMethods()
/*      */   {
/*  250 */     if (this._memberMethods == null) {
/*  251 */       resolveMemberMethods();
/*      */     }
/*  253 */     return this._memberMethods;
/*      */   }
/*      */   
/*      */   public int getMemberMethodCount()
/*      */   {
/*  258 */     if (this._memberMethods == null) {
/*  259 */       resolveMemberMethods();
/*      */     }
/*  261 */     return this._memberMethods.size();
/*      */   }
/*      */   
/*      */   public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes)
/*      */   {
/*  266 */     if (this._memberMethods == null) {
/*  267 */       resolveMemberMethods();
/*      */     }
/*  269 */     return this._memberMethods.find(name, paramTypes);
/*      */   }
/*      */   
/*      */   public int getFieldCount() {
/*  273 */     if (this._fields == null) {
/*  274 */       resolveFields();
/*      */     }
/*  276 */     return this._fields.size();
/*      */   }
/*      */   
/*      */   public Iterable<AnnotatedField> fields()
/*      */   {
/*  281 */     if (this._fields == null) {
/*  282 */       resolveFields();
/*      */     }
/*  284 */     return this._fields;
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
/*      */ 
/*      */   private void resolveClassAnnotations()
/*      */   {
/*  300 */     this._classAnnotations = new AnnotationMap();
/*      */     
/*  302 */     if (this._annotationIntrospector != null)
/*      */     {
/*  304 */       if (this._primaryMixIn != null) {
/*  305 */         _addClassMixIns(this._classAnnotations, this._class, this._primaryMixIn);
/*      */       }
/*      */       
/*  308 */       _addAnnotationsIfNotPresent(this._classAnnotations, this._class.getDeclaredAnnotations());
/*      */       
/*      */ 
/*  311 */       for (Class<?> cls : this._superTypes)
/*      */       {
/*  313 */         _addClassMixIns(this._classAnnotations, cls);
/*  314 */         _addAnnotationsIfNotPresent(this._classAnnotations, cls.getDeclaredAnnotations());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  323 */       _addClassMixIns(this._classAnnotations, Object.class);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resolveCreators()
/*      */   {
/*  334 */     List<AnnotatedConstructor> constructors = null;
/*  335 */     Constructor<?>[] declaredCtors = this._class.getDeclaredConstructors();
/*  336 */     for (Constructor<?> ctor : declaredCtors) {
/*  337 */       if (ctor.getParameterTypes().length == 0) {
/*  338 */         this._defaultConstructor = _constructConstructor(ctor, true);
/*      */       } else {
/*  340 */         if (constructors == null) {
/*  341 */           constructors = new ArrayList(Math.max(10, declaredCtors.length));
/*      */         }
/*  343 */         constructors.add(_constructConstructor(ctor, false));
/*      */       }
/*      */     }
/*  346 */     if (constructors == null) {
/*  347 */       this._constructors = java.util.Collections.emptyList();
/*      */     } else {
/*  349 */       this._constructors = constructors;
/*      */     }
/*      */     
/*  352 */     if ((this._primaryMixIn != null) && (
/*  353 */       (this._defaultConstructor != null) || (!this._constructors.isEmpty()))) {
/*  354 */       _addConstructorMixIns(this._primaryMixIn);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  363 */     if (this._annotationIntrospector != null) {
/*  364 */       if ((this._defaultConstructor != null) && 
/*  365 */         (this._annotationIntrospector.hasIgnoreMarker(this._defaultConstructor))) {
/*  366 */         this._defaultConstructor = null;
/*      */       }
/*      */       
/*  369 */       if (this._constructors != null)
/*      */       {
/*  371 */         int i = this._constructors.size(); for (;;) { i--; if (i < 0) break;
/*  372 */           if (this._annotationIntrospector.hasIgnoreMarker((AnnotatedMember)this._constructors.get(i))) {
/*  373 */             this._constructors.remove(i);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  378 */     List<AnnotatedMethod> creatorMethods = null;
/*      */     
/*      */ 
/*  381 */     for (Method m : this._class.getDeclaredMethods())
/*  382 */       if (Modifier.isStatic(m.getModifiers()))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  387 */         if (creatorMethods == null) {
/*  388 */           creatorMethods = new ArrayList(8);
/*      */         }
/*  390 */         creatorMethods.add(_constructCreatorMethod(m));
/*      */       }
/*  392 */     if (creatorMethods == null) {
/*  393 */       this._creatorMethods = java.util.Collections.emptyList();
/*      */     } else {
/*  395 */       this._creatorMethods = creatorMethods;
/*      */       
/*  397 */       if (this._primaryMixIn != null) {
/*  398 */         _addFactoryMixIns(this._primaryMixIn);
/*      */       }
/*      */       
/*  401 */       if (this._annotationIntrospector != null)
/*      */       {
/*  403 */         int i = this._creatorMethods.size(); for (;;) { i--; if (i < 0) break;
/*  404 */           if (this._annotationIntrospector.hasIgnoreMarker((AnnotatedMember)this._creatorMethods.get(i))) {
/*  405 */             this._creatorMethods.remove(i);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  410 */     this._creatorsResolved = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resolveMemberMethods()
/*      */   {
/*  421 */     this._memberMethods = new AnnotatedMethodMap();
/*  422 */     AnnotatedMethodMap mixins = new AnnotatedMethodMap();
/*      */     
/*  424 */     _addMemberMethods(this._class, this._memberMethods, this._primaryMixIn, mixins);
/*      */     
/*      */ 
/*  427 */     for (Class<?> cls : this._superTypes) {
/*  428 */       Class<?> mixin = this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(cls);
/*  429 */       _addMemberMethods(cls, this._memberMethods, mixin, mixins);
/*      */     }
/*      */     
/*  432 */     if (this._mixInResolver != null) {
/*  433 */       Class<?> mixin = this._mixInResolver.findMixInClassFor(Object.class);
/*  434 */       if (mixin != null) {
/*  435 */         _addMethodMixIns(this._class, this._memberMethods, mixin, mixins);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  445 */     if ((this._annotationIntrospector != null) && 
/*  446 */       (!mixins.isEmpty())) {
/*  447 */       java.util.Iterator<AnnotatedMethod> it = mixins.iterator();
/*  448 */       while (it.hasNext()) {
/*  449 */         AnnotatedMethod mixIn = (AnnotatedMethod)it.next();
/*      */         try {
/*  451 */           Method m = Object.class.getDeclaredMethod(mixIn.getName(), mixIn.getRawParameterTypes());
/*  452 */           if (m != null) {
/*  453 */             AnnotatedMethod am = _constructMethod(m);
/*  454 */             _addMixOvers(mixIn.getAnnotated(), am, false);
/*  455 */             this._memberMethods.add(am);
/*      */           }
/*      */         }
/*      */         catch (Exception e) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resolveFields()
/*      */   {
/*  470 */     Map<String, AnnotatedField> foundFields = _findFields(this._class, null);
/*  471 */     if ((foundFields == null) || (foundFields.size() == 0)) {
/*  472 */       this._fields = java.util.Collections.emptyList();
/*      */     } else {
/*  474 */       this._fields = new ArrayList(foundFields.size());
/*  475 */       this._fields.addAll(foundFields.values());
/*      */     }
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
/*      */ 
/*      */ 
/*      */   protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask)
/*      */   {
/*  493 */     if (this._mixInResolver != null) {
/*  494 */       _addClassMixIns(annotations, toMask, this._mixInResolver.findMixInClassFor(toMask));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask, Class<?> mixin)
/*      */   {
/*  501 */     if (mixin == null) {
/*  502 */       return;
/*      */     }
/*      */     
/*  505 */     _addAnnotationsIfNotPresent(annotations, mixin.getDeclaredAnnotations());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  514 */     for (Class<?> parent : com.fasterxml.jackson.databind.util.ClassUtil.findSuperTypes(mixin, toMask)) {
/*  515 */       _addAnnotationsIfNotPresent(annotations, parent.getDeclaredAnnotations());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addConstructorMixIns(Class<?> mixin)
/*      */   {
/*  527 */     MemberKey[] ctorKeys = null;
/*  528 */     int ctorCount = this._constructors == null ? 0 : this._constructors.size();
/*  529 */     for (Constructor<?> ctor : mixin.getDeclaredConstructors()) {
/*  530 */       if (ctor.getParameterTypes().length == 0) {
/*  531 */         if (this._defaultConstructor != null) {
/*  532 */           _addMixOvers(ctor, this._defaultConstructor, false);
/*      */         }
/*      */       } else {
/*  535 */         if (ctorKeys == null) {
/*  536 */           ctorKeys = new MemberKey[ctorCount];
/*  537 */           for (int i = 0; i < ctorCount; i++) {
/*  538 */             ctorKeys[i] = new MemberKey(((AnnotatedConstructor)this._constructors.get(i)).getAnnotated());
/*      */           }
/*      */         }
/*  541 */         MemberKey key = new MemberKey(ctor);
/*      */         
/*  543 */         for (int i = 0; i < ctorCount; i++) {
/*  544 */           if (key.equals(ctorKeys[i]))
/*      */           {
/*      */ 
/*  547 */             _addMixOvers(ctor, (AnnotatedConstructor)this._constructors.get(i), true);
/*  548 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _addFactoryMixIns(Class<?> mixin) {
/*  556 */     MemberKey[] methodKeys = null;
/*  557 */     int methodCount = this._creatorMethods.size();
/*      */     
/*  559 */     for (Method m : mixin.getDeclaredMethods()) {
/*  560 */       if (Modifier.isStatic(m.getModifiers()))
/*      */       {
/*      */ 
/*  563 */         if (m.getParameterTypes().length != 0)
/*      */         {
/*      */ 
/*  566 */           if (methodKeys == null) {
/*  567 */             methodKeys = new MemberKey[methodCount];
/*  568 */             for (int i = 0; i < methodCount; i++) {
/*  569 */               methodKeys[i] = new MemberKey(((AnnotatedMethod)this._creatorMethods.get(i)).getAnnotated());
/*      */             }
/*      */           }
/*  572 */           MemberKey key = new MemberKey(m);
/*  573 */           for (int i = 0; i < methodCount; i++) {
/*  574 */             if (key.equals(methodKeys[i]))
/*      */             {
/*      */ 
/*  577 */               _addMixOvers(m, (AnnotatedMethod)this._creatorMethods.get(i), true);
/*  578 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addMemberMethods(Class<?> cls, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns)
/*      */   {
/*  593 */     if (mixInCls != null) {
/*  594 */       _addMethodMixIns(cls, methods, mixInCls, mixIns);
/*      */     }
/*  596 */     if (cls == null) {
/*  597 */       return;
/*      */     }
/*      */     
/*      */ 
/*  601 */     for (Method m : cls.getDeclaredMethods()) {
/*  602 */       if (_isIncludableMemberMethod(m))
/*      */       {
/*      */ 
/*  605 */         AnnotatedMethod old = methods.find(m);
/*  606 */         if (old == null) {
/*  607 */           AnnotatedMethod newM = _constructMethod(m);
/*  608 */           methods.add(newM);
/*      */           
/*  610 */           old = mixIns.remove(m);
/*  611 */           if (old != null) {
/*  612 */             _addMixOvers(old.getAnnotated(), newM, false);
/*      */           }
/*      */           
/*      */         }
/*      */         else
/*      */         {
/*  618 */           _addMixUnders(m, old);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  627 */           if ((old.getDeclaringClass().isInterface()) && (!m.getDeclaringClass().isInterface())) {
/*  628 */             methods.add(old.withMethod(m));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _addMethodMixIns(Class<?> targetClass, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns)
/*      */   {
/*  637 */     List<Class<?>> parents = new ArrayList();
/*  638 */     parents.add(mixInCls);
/*  639 */     com.fasterxml.jackson.databind.util.ClassUtil.findSuperTypes(mixInCls, targetClass, parents);
/*  640 */     for (Class<?> mixin : parents) {
/*  641 */       for (Method m : mixin.getDeclaredMethods()) {
/*  642 */         if (_isIncludableMemberMethod(m))
/*      */         {
/*      */ 
/*  645 */           AnnotatedMethod am = methods.find(m);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  650 */           if (am != null) {
/*  651 */             _addMixUnders(m, am);
/*      */ 
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*  658 */             am = mixIns.find(m);
/*  659 */             if (am != null) {
/*  660 */               _addMixUnders(m, am);
/*      */             } else {
/*  662 */               mixIns.add(_constructMethod(m));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
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
/*      */   protected Map<String, AnnotatedField> _findFields(Class<?> c, Map<String, AnnotatedField> fields)
/*      */   {
/*  682 */     Class<?> parent = c.getSuperclass();
/*  683 */     if (parent != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  688 */       fields = _findFields(parent, fields);
/*  689 */       for (Field f : c.getDeclaredFields())
/*      */       {
/*  691 */         if (_isIncludableField(f))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  699 */           if (fields == null) {
/*  700 */             fields = new java.util.LinkedHashMap();
/*      */           }
/*  702 */           fields.put(f.getName(), _constructField(f));
/*      */         }
/*      */       }
/*  705 */       if (this._mixInResolver != null) {
/*  706 */         Class<?> mixin = this._mixInResolver.findMixInClassFor(c);
/*  707 */         if (mixin != null) {
/*  708 */           _addFieldMixIns(parent, mixin, fields);
/*      */         }
/*      */       }
/*      */     }
/*  712 */     return fields;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addFieldMixIns(Class<?> targetClass, Class<?> mixInCls, Map<String, AnnotatedField> fields)
/*      */   {
/*  723 */     List<Class<?>> parents = new ArrayList();
/*  724 */     parents.add(mixInCls);
/*  725 */     com.fasterxml.jackson.databind.util.ClassUtil.findSuperTypes(mixInCls, targetClass, parents);
/*  726 */     for (Class<?> mixin : parents) {
/*  727 */       for (Field mixinField : mixin.getDeclaredFields())
/*      */       {
/*  729 */         if (_isIncludableField(mixinField))
/*      */         {
/*      */ 
/*  732 */           String name = mixinField.getName();
/*      */           
/*  734 */           AnnotatedField maskedField = (AnnotatedField)fields.get(name);
/*  735 */           if (maskedField != null) {
/*  736 */             _addOrOverrideAnnotations(maskedField, mixinField.getDeclaredAnnotations());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
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
/*      */   protected AnnotatedMethod _constructMethod(Method m)
/*      */   {
/*  754 */     if (this._annotationIntrospector == null) {
/*  755 */       return new AnnotatedMethod(this, m, _emptyAnnotationMap(), null);
/*      */     }
/*  757 */     return new AnnotatedMethod(this, m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), null);
/*      */   }
/*      */   
/*      */   protected AnnotatedConstructor _constructConstructor(Constructor<?> ctor, boolean defaultCtor)
/*      */   {
/*  762 */     if (this._annotationIntrospector == null) {
/*  763 */       return new AnnotatedConstructor(this, ctor, _emptyAnnotationMap(), _emptyAnnotationMaps(ctor.getParameterTypes().length));
/*      */     }
/*  765 */     if (defaultCtor) {
/*  766 */       return new AnnotatedConstructor(this, ctor, _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), null);
/*      */     }
/*  768 */     Annotation[][] paramAnns = ctor.getParameterAnnotations();
/*  769 */     int paramCount = ctor.getParameterTypes().length;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  775 */     AnnotationMap[] resolvedAnnotations = null;
/*  776 */     if (paramCount != paramAnns.length)
/*      */     {
/*      */ 
/*      */ 
/*  780 */       Class<?> dc = ctor.getDeclaringClass();
/*      */       
/*  782 */       if ((dc.isEnum()) && (paramCount == paramAnns.length + 2)) {
/*  783 */         Annotation[][] old = paramAnns;
/*  784 */         paramAnns = new Annotation[old.length + 2][];
/*  785 */         System.arraycopy(old, 0, paramAnns, 2, old.length);
/*  786 */         resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
/*  787 */       } else if (dc.isMemberClass())
/*      */       {
/*  789 */         if (paramCount == paramAnns.length + 1)
/*      */         {
/*  791 */           Annotation[][] old = paramAnns;
/*  792 */           paramAnns = new Annotation[old.length + 1][];
/*  793 */           System.arraycopy(old, 0, paramAnns, 1, old.length);
/*  794 */           resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
/*      */         }
/*      */       }
/*  797 */       if (resolvedAnnotations == null) {
/*  798 */         throw new IllegalStateException("Internal error: constructor for " + ctor.getDeclaringClass().getName() + " has mismatch: " + paramCount + " parameters; " + paramAnns.length + " sets of annotations");
/*      */       }
/*      */     }
/*      */     else {
/*  802 */       resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
/*      */     }
/*  804 */     return new AnnotatedConstructor(this, ctor, _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), resolvedAnnotations);
/*      */   }
/*      */   
/*      */ 
/*      */   protected AnnotatedMethod _constructCreatorMethod(Method m)
/*      */   {
/*  810 */     if (this._annotationIntrospector == null) {
/*  811 */       return new AnnotatedMethod(this, m, _emptyAnnotationMap(), _emptyAnnotationMaps(m.getParameterTypes().length));
/*      */     }
/*  813 */     return new AnnotatedMethod(this, m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), _collectRelevantAnnotations(m.getParameterAnnotations()));
/*      */   }
/*      */   
/*      */ 
/*      */   protected AnnotatedField _constructField(Field f)
/*      */   {
/*  819 */     if (this._annotationIntrospector == null) {
/*  820 */       return new AnnotatedField(this, f, _emptyAnnotationMap());
/*      */     }
/*  822 */     return new AnnotatedField(this, f, _collectRelevantAnnotations(f.getDeclaredAnnotations()));
/*      */   }
/*      */   
/*      */   private AnnotationMap _emptyAnnotationMap() {
/*  826 */     return new AnnotationMap();
/*      */   }
/*      */   
/*      */   private AnnotationMap[] _emptyAnnotationMaps(int count) {
/*  830 */     if (count == 0) {
/*  831 */       return NO_ANNOTATION_MAPS;
/*      */     }
/*  833 */     AnnotationMap[] maps = new AnnotationMap[count];
/*  834 */     for (int i = 0; i < count; i++) {
/*  835 */       maps[i] = _emptyAnnotationMap();
/*      */     }
/*  837 */     return maps;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _isIncludableMemberMethod(Method m)
/*      */   {
/*  848 */     if (Modifier.isStatic(m.getModifiers())) {
/*  849 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  855 */     if ((m.isSynthetic()) || (m.isBridge())) {
/*  856 */       return false;
/*      */     }
/*      */     
/*  859 */     int pcount = m.getParameterTypes().length;
/*  860 */     return pcount <= 2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean _isIncludableField(Field f)
/*      */   {
/*  868 */     if (f.isSynthetic()) {
/*  869 */       return false;
/*      */     }
/*      */     
/*  872 */     int mods = f.getModifiers();
/*  873 */     if ((Modifier.isStatic(mods)) || (Modifier.isTransient(mods))) {
/*  874 */       return false;
/*      */     }
/*  876 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AnnotationMap[] _collectRelevantAnnotations(Annotation[][] anns)
/*      */   {
/*  887 */     int len = anns.length;
/*  888 */     AnnotationMap[] result = new AnnotationMap[len];
/*  889 */     for (int i = 0; i < len; i++) {
/*  890 */       result[i] = _collectRelevantAnnotations(anns[i]);
/*      */     }
/*  892 */     return result;
/*      */   }
/*      */   
/*      */   protected AnnotationMap _collectRelevantAnnotations(Annotation[] anns)
/*      */   {
/*  897 */     AnnotationMap annMap = new AnnotationMap();
/*  898 */     _addAnnotationsIfNotPresent(annMap, anns);
/*  899 */     return annMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _addAnnotationsIfNotPresent(AnnotationMap result, Annotation[] anns)
/*      */   {
/*  908 */     if (anns != null) {
/*  909 */       List<Annotation[]> bundles = null;
/*  910 */       for (Annotation ann : anns)
/*      */       {
/*  912 */         boolean wasNotPresent = result.addIfNotPresent(ann);
/*  913 */         if ((wasNotPresent) && (_isAnnotationBundle(ann))) {
/*  914 */           if (bundles == null) {
/*  915 */             bundles = new java.util.LinkedList();
/*      */           }
/*  917 */           bundles.add(ann.annotationType().getDeclaredAnnotations());
/*      */         }
/*      */       }
/*  920 */       if (bundles != null) {
/*  921 */         for (Annotation[] annotations : bundles) {
/*  922 */           _addAnnotationsIfNotPresent(result, annotations);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void _addAnnotationsIfNotPresent(AnnotatedMember target, Annotation[] anns)
/*      */   {
/*  930 */     if (anns != null) {
/*  931 */       List<Annotation[]> bundles = null;
/*  932 */       for (Annotation ann : anns)
/*      */       {
/*  934 */         boolean wasNotPresent = target.addIfNotPresent(ann);
/*  935 */         if ((wasNotPresent) && (_isAnnotationBundle(ann))) {
/*  936 */           if (bundles == null) {
/*  937 */             bundles = new java.util.LinkedList();
/*      */           }
/*  939 */           bundles.add(ann.annotationType().getDeclaredAnnotations());
/*      */         }
/*      */       }
/*  942 */       if (bundles != null) {
/*  943 */         for (Annotation[] annotations : bundles) {
/*  944 */           _addAnnotationsIfNotPresent(target, annotations);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void _addOrOverrideAnnotations(AnnotatedMember target, Annotation[] anns)
/*      */   {
/*  952 */     if (anns != null) {
/*  953 */       List<Annotation[]> bundles = null;
/*  954 */       for (Annotation ann : anns)
/*      */       {
/*  956 */         boolean wasModified = target.addOrOverride(ann);
/*  957 */         if ((wasModified) && (_isAnnotationBundle(ann))) {
/*  958 */           if (bundles == null) {
/*  959 */             bundles = new java.util.LinkedList();
/*      */           }
/*  961 */           bundles.add(ann.annotationType().getDeclaredAnnotations());
/*      */         }
/*      */       }
/*  964 */       if (bundles != null) {
/*  965 */         for (Annotation[] annotations : bundles) {
/*  966 */           _addOrOverrideAnnotations(target, annotations);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addMixOvers(Constructor<?> mixin, AnnotatedConstructor target, boolean addParamAnnotations)
/*      */   {
/*  979 */     _addOrOverrideAnnotations(target, mixin.getDeclaredAnnotations());
/*  980 */     if (addParamAnnotations) {
/*  981 */       Annotation[][] pa = mixin.getParameterAnnotations();
/*  982 */       int i = 0; for (int len = pa.length; i < len; i++) {
/*  983 */         for (Annotation a : pa[i]) {
/*  984 */           target.addOrOverrideParam(i, a);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addMixOvers(Method mixin, AnnotatedMethod target, boolean addParamAnnotations)
/*      */   {
/*  997 */     _addOrOverrideAnnotations(target, mixin.getDeclaredAnnotations());
/*  998 */     if (addParamAnnotations) {
/*  999 */       Annotation[][] pa = mixin.getParameterAnnotations();
/* 1000 */       int i = 0; for (int len = pa.length; i < len; i++) {
/* 1001 */         for (Annotation a : pa[i]) {
/* 1002 */           target.addOrOverrideParam(i, a);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addMixUnders(Method src, AnnotatedMethod target)
/*      */   {
/* 1013 */     _addAnnotationsIfNotPresent(target, src.getDeclaredAnnotations());
/*      */   }
/*      */   
/*      */   private final boolean _isAnnotationBundle(Annotation ann) {
/* 1017 */     return (this._annotationIntrospector != null) && (this._annotationIntrospector.isAnnotationBundle(ann));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1028 */     return "[AnnotedClass " + this._class.getName() + "]";
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/* 1033 */     return this._class.getName().hashCode();
/*      */   }
/*      */   
/*      */   public boolean equals(Object o)
/*      */   {
/* 1038 */     if (o == this) return true;
/* 1039 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 1040 */     return ((AnnotatedClass)o)._class == this._class;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */