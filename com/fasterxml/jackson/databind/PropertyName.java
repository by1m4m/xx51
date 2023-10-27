/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import com.fasterxml.jackson.core.util.InternCache;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import java.io.Serializable;
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
/*     */ public class PropertyName
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String _USE_DEFAULT = "";
/*     */   private static final String _NO_NAME = "";
/*  28 */   public static final PropertyName USE_DEFAULT = new PropertyName("", null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  35 */   public static final PropertyName NO_NAME = new PropertyName(new String(""), null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _simpleName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _namespace;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SerializableString _encodedSimple;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyName(String simpleName)
/*     */   {
/*  60 */     this(simpleName, null);
/*     */   }
/*     */   
/*     */   public PropertyName(String simpleName, String namespace)
/*     */   {
/*  65 */     this._simpleName = (simpleName == null ? "" : simpleName);
/*  66 */     this._namespace = namespace;
/*     */   }
/*     */   
/*     */   protected Object readResolve()
/*     */   {
/*  71 */     if ((this._simpleName == null) || ("".equals(this._simpleName))) {
/*  72 */       return USE_DEFAULT;
/*     */     }
/*  74 */     if ((this._simpleName.equals("")) && (this._namespace == null)) {
/*  75 */       return NO_NAME;
/*     */     }
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public static PropertyName construct(String simpleName, String ns)
/*     */   {
/*  82 */     if (simpleName == null) {
/*  83 */       simpleName = "";
/*     */     }
/*  85 */     if ((ns == null) && (simpleName.length() == 0)) {
/*  86 */       return USE_DEFAULT;
/*     */     }
/*  88 */     return new PropertyName(simpleName, ns);
/*     */   }
/*     */   
/*     */   public PropertyName internSimpleName()
/*     */   {
/*  93 */     if (this._simpleName.length() == 0) {
/*  94 */       return this;
/*     */     }
/*  96 */     String interned = InternCache.instance.intern(this._simpleName);
/*  97 */     if (interned == this._simpleName) {
/*  98 */       return this;
/*     */     }
/* 100 */     return new PropertyName(interned, this._namespace);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyName withSimpleName(String simpleName)
/*     */   {
/* 110 */     if (simpleName == null) {
/* 111 */       simpleName = "";
/*     */     }
/* 113 */     if (simpleName.equals(this._simpleName)) {
/* 114 */       return this;
/*     */     }
/* 116 */     return new PropertyName(simpleName, this._namespace);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyName withNamespace(String ns)
/*     */   {
/* 124 */     if (ns == null) {
/* 125 */       if (this._namespace == null) {
/* 126 */         return this;
/*     */       }
/* 128 */     } else if (ns.equals(this._namespace)) {
/* 129 */       return this;
/*     */     }
/* 131 */     return new PropertyName(this._simpleName, ns);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSimpleName()
/*     */   {
/* 141 */     return this._simpleName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializableString simpleAsEncoded(MapperConfig<?> config)
/*     */   {
/* 151 */     SerializableString sstr = this._encodedSimple;
/* 152 */     if (sstr == null) {
/* 153 */       if (config == null) {
/* 154 */         sstr = new SerializedString(this._simpleName);
/*     */       } else {
/* 156 */         sstr = config.compileString(this._simpleName);
/*     */       }
/* 158 */       this._encodedSimple = sstr;
/*     */     }
/* 160 */     return sstr;
/*     */   }
/*     */   
/*     */   public String getNamespace() {
/* 164 */     return this._namespace;
/*     */   }
/*     */   
/*     */   public boolean hasSimpleName() {
/* 168 */     return this._simpleName.length() > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasSimpleName(String str)
/*     */   {
/* 175 */     if (str == null) {
/* 176 */       return this._simpleName == null;
/*     */     }
/* 178 */     return str.equals(this._simpleName);
/*     */   }
/*     */   
/*     */   public boolean hasNamespace() {
/* 182 */     return this._namespace != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 194 */     return (this._namespace == null) && (this._simpleName.isEmpty());
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
/* 206 */     if (o == this) return true;
/* 207 */     if (o == null) { return false;
/*     */     }
/*     */     
/*     */ 
/* 211 */     if (o.getClass() != getClass()) { return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 216 */     PropertyName other = (PropertyName)o;
/* 217 */     if (this._simpleName == null) {
/* 218 */       if (other._simpleName != null) return false;
/* 219 */     } else if (!this._simpleName.equals(other._simpleName)) {
/* 220 */       return false;
/*     */     }
/* 222 */     if (this._namespace == null) {
/* 223 */       return null == other._namespace;
/*     */     }
/* 225 */     return this._namespace.equals(other._namespace);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 230 */     if (this._namespace == null) {
/* 231 */       return this._simpleName.hashCode();
/*     */     }
/* 233 */     return this._namespace.hashCode() ^ this._simpleName.hashCode();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 238 */     if (this._namespace == null) {
/* 239 */       return this._simpleName;
/*     */     }
/* 241 */     return "{" + this._namespace + "}" + this._simpleName;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\PropertyName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */