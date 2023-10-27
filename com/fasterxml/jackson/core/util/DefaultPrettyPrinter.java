/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import java.io.IOException;
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
/*     */ public class DefaultPrettyPrinter
/*     */   implements PrettyPrinter, Instantiatable<DefaultPrettyPrinter>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  28 */   public static final SerializedString DEFAULT_ROOT_VALUE_SEPARATOR = new SerializedString(" ");
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
/*  52 */   protected Indenter _arrayIndenter = FixedSpaceIndenter.instance;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */   protected Indenter _objectIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final SerializableString _rootSeparator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   protected boolean _spacesInObjectEntries = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */   protected transient int _nesting = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter()
/*     */   {
/*  91 */     this(DEFAULT_ROOT_VALUE_SEPARATOR);
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
/*     */   public DefaultPrettyPrinter(String rootSeparator)
/*     */   {
/* 106 */     this(rootSeparator == null ? null : new SerializedString(rootSeparator));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter(SerializableString rootSeparator)
/*     */   {
/* 118 */     this._rootSeparator = rootSeparator;
/*     */   }
/*     */   
/*     */   public DefaultPrettyPrinter(DefaultPrettyPrinter base) {
/* 122 */     this(base, base._rootSeparator);
/*     */   }
/*     */   
/*     */ 
/*     */   public DefaultPrettyPrinter(DefaultPrettyPrinter base, SerializableString rootSeparator)
/*     */   {
/* 128 */     this._arrayIndenter = base._arrayIndenter;
/* 129 */     this._objectIndenter = base._objectIndenter;
/* 130 */     this._spacesInObjectEntries = base._spacesInObjectEntries;
/* 131 */     this._nesting = base._nesting;
/*     */     
/* 133 */     this._rootSeparator = rootSeparator;
/*     */   }
/*     */   
/*     */   public DefaultPrettyPrinter withRootSeparator(SerializableString rootSeparator)
/*     */   {
/* 138 */     if ((this._rootSeparator == rootSeparator) || ((rootSeparator != null) && (rootSeparator.equals(this._rootSeparator))))
/*     */     {
/* 140 */       return this;
/*     */     }
/* 142 */     return new DefaultPrettyPrinter(this, rootSeparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter withRootSeparator(String rootSeparator)
/*     */   {
/* 149 */     return withRootSeparator(new SerializedString(rootSeparator));
/*     */   }
/*     */   
/*     */   public void indentArraysWith(Indenter i) {
/* 153 */     this._arrayIndenter = (i == null ? NopIndenter.instance : i);
/*     */   }
/*     */   
/*     */   public void indentObjectsWith(Indenter i) {
/* 157 */     this._objectIndenter = (i == null ? NopIndenter.instance : i);
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public void spacesInObjectEntries(boolean b)
/*     */   {
/* 164 */     this._spacesInObjectEntries = b;
/*     */   }
/*     */   
/*     */ 
/*     */   public DefaultPrettyPrinter withArrayIndenter(Indenter i)
/*     */   {
/* 170 */     if (i == null) {
/* 171 */       i = NopIndenter.instance;
/*     */     }
/* 173 */     if (this._arrayIndenter == i) {
/* 174 */       return this;
/*     */     }
/* 176 */     DefaultPrettyPrinter pp = new DefaultPrettyPrinter(this);
/* 177 */     pp._arrayIndenter = i;
/* 178 */     return pp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter withObjectIndenter(Indenter i)
/*     */   {
/* 185 */     if (i == null) {
/* 186 */       i = NopIndenter.instance;
/*     */     }
/* 188 */     if (this._objectIndenter == i) {
/* 189 */       return this;
/*     */     }
/* 191 */     DefaultPrettyPrinter pp = new DefaultPrettyPrinter(this);
/* 192 */     pp._objectIndenter = i;
/* 193 */     return pp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter withSpacesInObjectEntries()
/*     */   {
/* 205 */     return _withSpaces(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter withoutSpacesInObjectEntries()
/*     */   {
/* 217 */     return _withSpaces(false);
/*     */   }
/*     */   
/*     */   protected DefaultPrettyPrinter _withSpaces(boolean state)
/*     */   {
/* 222 */     if (this._spacesInObjectEntries == state) {
/* 223 */       return this;
/*     */     }
/* 225 */     DefaultPrettyPrinter pp = new DefaultPrettyPrinter(this);
/* 226 */     pp._spacesInObjectEntries = state;
/* 227 */     return pp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultPrettyPrinter createInstance()
/*     */   {
/* 238 */     return new DefaultPrettyPrinter(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeRootValueSeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 251 */     if (this._rootSeparator != null) {
/* 252 */       jg.writeRaw(this._rootSeparator);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeStartObject(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 260 */     jg.writeRaw('{');
/* 261 */     if (!this._objectIndenter.isInline()) {
/* 262 */       this._nesting += 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void beforeObjectEntries(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 270 */     this._objectIndenter.writeIndentation(jg, this._nesting);
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
/*     */   public void writeObjectFieldValueSeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 286 */     if (this._spacesInObjectEntries) {
/* 287 */       jg.writeRaw(" : ");
/*     */     } else {
/* 289 */       jg.writeRaw(':');
/*     */     }
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
/*     */   public void writeObjectEntrySeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 306 */     jg.writeRaw(',');
/* 307 */     this._objectIndenter.writeIndentation(jg, this._nesting);
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeEndObject(JsonGenerator jg, int nrOfEntries)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 314 */     if (!this._objectIndenter.isInline()) {
/* 315 */       this._nesting -= 1;
/*     */     }
/* 317 */     if (nrOfEntries > 0) {
/* 318 */       this._objectIndenter.writeIndentation(jg, this._nesting);
/*     */     } else {
/* 320 */       jg.writeRaw(' ');
/*     */     }
/* 322 */     jg.writeRaw('}');
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeStartArray(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 329 */     if (!this._arrayIndenter.isInline()) {
/* 330 */       this._nesting += 1;
/*     */     }
/* 332 */     jg.writeRaw('[');
/*     */   }
/*     */   
/*     */ 
/*     */   public void beforeArrayValues(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 339 */     this._arrayIndenter.writeIndentation(jg, this._nesting);
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
/*     */   public void writeArrayValueSeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 355 */     jg.writeRaw(',');
/* 356 */     this._arrayIndenter.writeIndentation(jg, this._nesting);
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeEndArray(JsonGenerator jg, int nrOfValues)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 363 */     if (!this._arrayIndenter.isInline()) {
/* 364 */       this._nesting -= 1;
/*     */     }
/* 366 */     if (nrOfValues > 0) {
/* 367 */       this._arrayIndenter.writeIndentation(jg, this._nesting);
/*     */     } else {
/* 369 */       jg.writeRaw(' ');
/*     */     }
/* 371 */     jg.writeRaw(']');
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract interface Indenter
/*     */   {
/*     */     public abstract void writeIndentation(JsonGenerator paramJsonGenerator, int paramInt)
/*     */       throws IOException;
/*     */     
/*     */     public abstract boolean isInline();
/*     */   }
/*     */   
/*     */   public static class NopIndenter
/*     */     implements DefaultPrettyPrinter.Indenter, Serializable
/*     */   {
/* 386 */     public static final NopIndenter instance = new NopIndenter();
/*     */     
/*     */     public void writeIndentation(JsonGenerator jg, int level) throws IOException
/*     */     {}
/*     */     
/*     */     public boolean isInline() {
/* 392 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class FixedSpaceIndenter
/*     */     extends DefaultPrettyPrinter.NopIndenter
/*     */   {
/* 403 */     public static final FixedSpaceIndenter instance = new FixedSpaceIndenter();
/*     */     
/*     */ 
/*     */     public void writeIndentation(JsonGenerator jg, int level)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 409 */       jg.writeRaw(' ');
/*     */     }
/*     */     
/*     */     public boolean isInline() {
/* 413 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static class Lf2SpacesIndenter
/*     */     extends DefaultIndenter
/*     */   {
/*     */     @Deprecated
/* 426 */     public static final Lf2SpacesIndenter instance = new Lf2SpacesIndenter();
/*     */     
/*     */ 
/*     */     @Deprecated
/*     */     public Lf2SpacesIndenter()
/*     */     {
/* 432 */       super(DefaultIndenter.SYS_LF);
/*     */     }
/*     */     
/*     */ 
/*     */     @Deprecated
/*     */     public Lf2SpacesIndenter(String lf)
/*     */     {
/* 439 */       super(lf);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Lf2SpacesIndenter withLinefeed(String lf)
/*     */     {
/* 448 */       if (lf.equals(getEol())) {
/* 449 */         return this;
/*     */       }
/* 451 */       return new Lf2SpacesIndenter(lf);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\util\DefaultPrettyPrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */