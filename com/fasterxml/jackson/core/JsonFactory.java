/*      */ package com.fasterxml.jackson.core;
/*      */ 
/*      */ import com.fasterxml.jackson.core.format.InputAccessor;
/*      */ import com.fasterxml.jackson.core.format.MatchStrength;
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.io.InputDecorator;
/*      */ import com.fasterxml.jackson.core.io.OutputDecorator;
/*      */ import com.fasterxml.jackson.core.io.SerializedString;
/*      */ import com.fasterxml.jackson.core.io.UTF8Writer;
/*      */ import com.fasterxml.jackson.core.json.ByteSourceJsonBootstrapper;
/*      */ import com.fasterxml.jackson.core.json.PackageVersion;
/*      */ import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
/*      */ import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
/*      */ import com.fasterxml.jackson.core.json.WriterBasedJsonGenerator;
/*      */ import com.fasterxml.jackson.core.sym.BytesToNameCanonicalizer;
/*      */ import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
/*      */ import com.fasterxml.jackson.core.util.BufferRecycler;
/*      */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*      */ import java.io.CharArrayReader;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.StringReader;
/*      */ import java.io.Writer;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.net.URL;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JsonFactory
/*      */   implements Versioned, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 3306684576057132431L;
/*      */   public static final String FORMAT_NAME_JSON = "JSON";
/*      */   
/*      */   public static enum Feature
/*      */   {
/*   79 */     INTERN_FIELD_NAMES(true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   89 */     CANONICALIZE_FIELD_NAMES(true), 
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
/*  105 */     FAIL_ON_SYMBOL_HASH_OVERFLOW(true);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final boolean _defaultState;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static int collectDefaults()
/*      */     {
/*  119 */       int flags = 0;
/*  120 */       for (Feature f : values()) {
/*  121 */         if (f.enabledByDefault()) flags |= f.getMask();
/*      */       }
/*  123 */       return flags;
/*      */     }
/*      */     
/*  126 */     private Feature(boolean defaultState) { this._defaultState = defaultState; }
/*      */     
/*  128 */     public boolean enabledByDefault() { return this._defaultState; }
/*  129 */     public boolean enabledIn(int flags) { return (flags & getMask()) != 0; }
/*  130 */     public int getMask() { return 1 << ordinal(); }
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
/*      */ 
/*      */ 
/*      */ 
/*  148 */   protected static final int DEFAULT_FACTORY_FEATURE_FLAGS = ;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  154 */   protected static final int DEFAULT_PARSER_FEATURE_FLAGS = JsonParser.Feature.collectDefaults();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  160 */   protected static final int DEFAULT_GENERATOR_FEATURE_FLAGS = JsonGenerator.Feature.collectDefaults();
/*      */   
/*  162 */   private static final SerializableString DEFAULT_ROOT_VALUE_SEPARATOR = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
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
/*  175 */   protected static final ThreadLocal<SoftReference<BufferRecycler>> _recyclerRef = new ThreadLocal();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  183 */   protected final transient CharsToNameCanonicalizer _rootCharSymbols = CharsToNameCanonicalizer.createRoot();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  192 */   protected final transient BytesToNameCanonicalizer _rootByteSymbols = BytesToNameCanonicalizer.createRoot();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectCodec _objectCodec;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  212 */   protected int _factoryFeatures = DEFAULT_FACTORY_FEATURE_FLAGS;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  217 */   protected int _parserFeatures = DEFAULT_PARSER_FEATURE_FLAGS;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  222 */   protected int _generatorFeatures = DEFAULT_GENERATOR_FEATURE_FLAGS;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected CharacterEscapes _characterEscapes;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected InputDecorator _inputDecorator;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected OutputDecorator _outputDecorator;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  250 */   protected SerializableString _rootValueSeparator = DEFAULT_ROOT_VALUE_SEPARATOR;
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
/*      */ 
/*  268 */   public JsonFactory() { this(null); }
/*      */   
/*  270 */   public JsonFactory(ObjectCodec oc) { this._objectCodec = oc; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonFactory(JsonFactory src, ObjectCodec codec)
/*      */   {
/*  279 */     this._objectCodec = null;
/*  280 */     this._factoryFeatures = src._factoryFeatures;
/*  281 */     this._parserFeatures = src._parserFeatures;
/*  282 */     this._generatorFeatures = src._generatorFeatures;
/*  283 */     this._characterEscapes = src._characterEscapes;
/*  284 */     this._inputDecorator = src._inputDecorator;
/*  285 */     this._outputDecorator = src._outputDecorator;
/*  286 */     this._rootValueSeparator = src._rootValueSeparator;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory copy()
/*      */   {
/*  310 */     _checkInvalidCopy(JsonFactory.class);
/*      */     
/*  312 */     return new JsonFactory(this, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _checkInvalidCopy(Class<?> exp)
/*      */   {
/*  321 */     if (getClass() != exp) {
/*  322 */       throw new IllegalStateException("Failed copy(): " + getClass().getName() + " (version: " + version() + ") does not override copy(); it has to");
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
/*      */   protected Object readResolve()
/*      */   {
/*  339 */     return new JsonFactory(this, this._objectCodec);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean requiresPropertyOrdering()
/*      */   {
/*  363 */     return false;
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
/*      */   public boolean canHandleBinaryNatively()
/*      */   {
/*  377 */     return false;
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
/*      */   public boolean canUseCharArrays()
/*      */   {
/*  391 */     return true;
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
/*      */ 
/*      */ 
/*      */   public boolean canUseSchema(FormatSchema schema)
/*      */   {
/*  410 */     String ourFormat = getFormatName();
/*  411 */     return (ourFormat != null) && (ourFormat.equals(schema.getSchemaType()));
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
/*      */   public String getFormatName()
/*      */   {
/*  427 */     if (getClass() == JsonFactory.class) {
/*  428 */       return "JSON";
/*      */     }
/*  430 */     return null;
/*      */   }
/*      */   
/*      */   public MatchStrength hasFormat(InputAccessor acc)
/*      */     throws IOException
/*      */   {
/*  436 */     if (getClass() == JsonFactory.class) {
/*  437 */       return hasJSONFormat(acc);
/*      */     }
/*  439 */     return null;
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
/*      */   public boolean requiresCustomCodec()
/*      */   {
/*  456 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected MatchStrength hasJSONFormat(InputAccessor acc)
/*      */     throws IOException
/*      */   {
/*  465 */     return ByteSourceJsonBootstrapper.hasJSONFormat(acc);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Version version()
/*      */   {
/*  476 */     return PackageVersion.VERSION;
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
/*      */   public final JsonFactory configure(Feature f, boolean state)
/*      */   {
/*  490 */     return state ? enable(f) : disable(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory enable(Feature f)
/*      */   {
/*  498 */     this._factoryFeatures |= f.getMask();
/*  499 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory disable(Feature f)
/*      */   {
/*  507 */     this._factoryFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*  508 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final boolean isEnabled(Feature f)
/*      */   {
/*  515 */     return (this._factoryFeatures & f.getMask()) != 0;
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
/*      */   public final JsonFactory configure(JsonParser.Feature f, boolean state)
/*      */   {
/*  529 */     return state ? enable(f) : disable(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory enable(JsonParser.Feature f)
/*      */   {
/*  537 */     this._parserFeatures |= f.getMask();
/*  538 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory disable(JsonParser.Feature f)
/*      */   {
/*  546 */     this._parserFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*  547 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final boolean isEnabled(JsonParser.Feature f)
/*      */   {
/*  554 */     return (this._parserFeatures & f.getMask()) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public InputDecorator getInputDecorator()
/*      */   {
/*  562 */     return this._inputDecorator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonFactory setInputDecorator(InputDecorator d)
/*      */   {
/*  569 */     this._inputDecorator = d;
/*  570 */     return this;
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
/*      */   public final JsonFactory configure(JsonGenerator.Feature f, boolean state)
/*      */   {
/*  584 */     return state ? enable(f) : disable(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory enable(JsonGenerator.Feature f)
/*      */   {
/*  593 */     this._generatorFeatures |= f.getMask();
/*  594 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory disable(JsonGenerator.Feature f)
/*      */   {
/*  602 */     this._generatorFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*  603 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final boolean isEnabled(JsonGenerator.Feature f)
/*      */   {
/*  610 */     return (this._generatorFeatures & f.getMask()) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public CharacterEscapes getCharacterEscapes()
/*      */   {
/*  617 */     return this._characterEscapes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonFactory setCharacterEscapes(CharacterEscapes esc)
/*      */   {
/*  624 */     this._characterEscapes = esc;
/*  625 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public OutputDecorator getOutputDecorator()
/*      */   {
/*  633 */     return this._outputDecorator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonFactory setOutputDecorator(OutputDecorator d)
/*      */   {
/*  640 */     this._outputDecorator = d;
/*  641 */     return this;
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
/*      */   public JsonFactory setRootValueSeparator(String sep)
/*      */   {
/*  654 */     this._rootValueSeparator = (sep == null ? null : new SerializedString(sep));
/*  655 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getRootValueSeparator()
/*      */   {
/*  662 */     return this._rootValueSeparator == null ? null : this._rootValueSeparator.getValue();
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
/*      */   public JsonFactory setCodec(ObjectCodec oc)
/*      */   {
/*  679 */     this._objectCodec = oc;
/*  680 */     return this;
/*      */   }
/*      */   
/*  683 */   public ObjectCodec getCodec() { return this._objectCodec; }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(File f)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  707 */     IOContext ctxt = _createContext(f, true);
/*  708 */     InputStream in = new FileInputStream(f);
/*  709 */     return _createParser(_decorate(in, ctxt), ctxt);
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
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(URL url)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  729 */     IOContext ctxt = _createContext(url, true);
/*  730 */     InputStream in = _optimizedStreamFromURL(url);
/*  731 */     return _createParser(_decorate(in, ctxt), ctxt);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(InputStream in)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  752 */     IOContext ctxt = _createContext(in, false);
/*  753 */     return _createParser(_decorate(in, ctxt), ctxt);
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
/*      */ 
/*      */   public JsonParser createParser(Reader r)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  772 */     IOContext ctxt = _createContext(r, false);
/*  773 */     return _createParser(_decorate(r, ctxt), ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(byte[] data)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  783 */     IOContext ctxt = _createContext(data, true);
/*  784 */     if (this._inputDecorator != null) {
/*  785 */       InputStream in = this._inputDecorator.decorate(ctxt, data, 0, data.length);
/*  786 */       if (in != null) {
/*  787 */         return _createParser(in, ctxt);
/*      */       }
/*      */     }
/*  790 */     return _createParser(data, 0, data.length, ctxt);
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
/*      */   public JsonParser createParser(byte[] data, int offset, int len)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  804 */     IOContext ctxt = _createContext(data, true);
/*      */     
/*  806 */     if (this._inputDecorator != null) {
/*  807 */       InputStream in = this._inputDecorator.decorate(ctxt, data, offset, len);
/*  808 */       if (in != null) {
/*  809 */         return _createParser(in, ctxt);
/*      */       }
/*      */     }
/*  812 */     return _createParser(data, offset, len, ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(String content)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  822 */     int strLen = content.length();
/*      */     
/*  824 */     if ((this._inputDecorator != null) || (strLen > 32768) || (!canUseCharArrays()))
/*      */     {
/*      */ 
/*  827 */       return createParser(new StringReader(content));
/*      */     }
/*  829 */     IOContext ctxt = _createContext(content, true);
/*  830 */     char[] buf = ctxt.allocTokenBuffer(strLen);
/*  831 */     content.getChars(0, strLen, buf, 0);
/*  832 */     return _createParser(buf, 0, strLen, ctxt, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(char[] content)
/*      */     throws IOException
/*      */   {
/*  842 */     return createParser(content, 0, content.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(char[] content, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  851 */     if (this._inputDecorator != null) {
/*  852 */       return createParser(new CharArrayReader(content, offset, len));
/*      */     }
/*  854 */     return _createParser(content, offset, len, _createContext(content, true), false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(File f)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  881 */     return createParser(f);
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
/*      */ 
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(URL url)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  901 */     return createParser(url);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(InputStream in)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  923 */     return createParser(in);
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
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(Reader r)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  942 */     return createParser(r);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(byte[] data)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  952 */     return createParser(data);
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
/*      */   public JsonParser createJsonParser(byte[] data, int offset, int len)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  967 */     return createParser(data, offset, len);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(String content)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  978 */     return createParser(content);
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
/*      */ 
/*      */   public JsonGenerator createGenerator(OutputStream out, JsonEncoding enc)
/*      */     throws IOException
/*      */   {
/* 1013 */     IOContext ctxt = _createContext(out, false);
/* 1014 */     ctxt.setEncoding(enc);
/* 1015 */     if (enc == JsonEncoding.UTF8) {
/* 1016 */       return _createUTF8Generator(_decorate(out, ctxt), ctxt);
/*      */     }
/* 1018 */     Writer w = _createWriter(out, enc, ctxt);
/* 1019 */     return _createGenerator(_decorate(w, ctxt), ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator createGenerator(OutputStream out)
/*      */     throws IOException
/*      */   {
/* 1031 */     return createGenerator(out, JsonEncoding.UTF8);
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
/*      */ 
/*      */   public JsonGenerator createGenerator(Writer w)
/*      */     throws IOException
/*      */   {
/* 1050 */     IOContext ctxt = _createContext(w, false);
/* 1051 */     return _createGenerator(_decorate(w, ctxt), ctxt);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator createGenerator(File f, JsonEncoding enc)
/*      */     throws IOException
/*      */   {
/* 1072 */     OutputStream out = new FileOutputStream(f);
/*      */     
/* 1074 */     IOContext ctxt = _createContext(out, true);
/* 1075 */     ctxt.setEncoding(enc);
/* 1076 */     if (enc == JsonEncoding.UTF8) {
/* 1077 */       return _createUTF8Generator(_decorate(out, ctxt), ctxt);
/*      */     }
/* 1079 */     Writer w = _createWriter(out, enc, ctxt);
/* 1080 */     return _createGenerator(_decorate(w, ctxt), ctxt);
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
/*      */   @Deprecated
/*      */   public JsonGenerator createJsonGenerator(OutputStream out, JsonEncoding enc)
/*      */     throws IOException
/*      */   {
/* 1113 */     return createGenerator(out, enc);
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
/*      */ 
/*      */   @Deprecated
/*      */   public JsonGenerator createJsonGenerator(Writer out)
/*      */     throws IOException
/*      */   {
/* 1133 */     return createGenerator(out);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonGenerator createJsonGenerator(OutputStream out)
/*      */     throws IOException
/*      */   {
/* 1146 */     return createGenerator(out, JsonEncoding.UTF8);
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
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonGenerator createJsonGenerator(File f, JsonEncoding enc)
/*      */     throws IOException
/*      */   {
/* 1167 */     return createGenerator(f, enc);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonParser _createParser(InputStream in, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1191 */     return new ByteSourceJsonBootstrapper(ctxt, in).constructParser(this._parserFeatures, this._objectCodec, this._rootByteSymbols, this._rootCharSymbols, this._factoryFeatures);
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
/*      */   protected JsonParser _createParser(Reader r, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1208 */     return new ReaderBasedJsonParser(ctxt, this._parserFeatures, r, this._objectCodec, this._rootCharSymbols.makeChild(this._factoryFeatures));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonParser _createParser(char[] data, int offset, int len, IOContext ctxt, boolean recyclable)
/*      */     throws IOException
/*      */   {
/* 1220 */     return new ReaderBasedJsonParser(ctxt, this._parserFeatures, null, this._objectCodec, this._rootCharSymbols.makeChild(this._factoryFeatures), data, offset, offset + len, recyclable);
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
/*      */   protected JsonParser _createParser(byte[] data, int offset, int len, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1238 */     return new ByteSourceJsonBootstrapper(ctxt, data, offset, len).constructParser(this._parserFeatures, this._objectCodec, this._rootByteSymbols, this._rootCharSymbols, this._factoryFeatures);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonGenerator _createGenerator(Writer out, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1261 */     WriterBasedJsonGenerator gen = new WriterBasedJsonGenerator(ctxt, this._generatorFeatures, this._objectCodec, out);
/*      */     
/* 1263 */     if (this._characterEscapes != null) {
/* 1264 */       gen.setCharacterEscapes(this._characterEscapes);
/*      */     }
/* 1266 */     SerializableString rootSep = this._rootValueSeparator;
/* 1267 */     if (rootSep != DEFAULT_ROOT_VALUE_SEPARATOR) {
/* 1268 */       gen.setRootValueSeparator(rootSep);
/*      */     }
/* 1270 */     return gen;
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
/*      */   protected JsonGenerator _createUTF8Generator(OutputStream out, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1284 */     UTF8JsonGenerator gen = new UTF8JsonGenerator(ctxt, this._generatorFeatures, this._objectCodec, out);
/*      */     
/* 1286 */     if (this._characterEscapes != null) {
/* 1287 */       gen.setCharacterEscapes(this._characterEscapes);
/*      */     }
/* 1289 */     SerializableString rootSep = this._rootValueSeparator;
/* 1290 */     if (rootSep != DEFAULT_ROOT_VALUE_SEPARATOR) {
/* 1291 */       gen.setRootValueSeparator(rootSep);
/*      */     }
/* 1293 */     return gen;
/*      */   }
/*      */   
/*      */   protected Writer _createWriter(OutputStream out, JsonEncoding enc, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1299 */     if (enc == JsonEncoding.UTF8) {
/* 1300 */       return new UTF8Writer(ctxt, out);
/*      */     }
/*      */     
/* 1303 */     return new OutputStreamWriter(out, enc.getJavaName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final InputStream _decorate(InputStream in, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1316 */     if (this._inputDecorator != null) {
/* 1317 */       InputStream in2 = this._inputDecorator.decorate(ctxt, in);
/* 1318 */       if (in2 != null) {
/* 1319 */         return in2;
/*      */       }
/*      */     }
/* 1322 */     return in;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final Reader _decorate(Reader in, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1329 */     if (this._inputDecorator != null) {
/* 1330 */       Reader in2 = this._inputDecorator.decorate(ctxt, in);
/* 1331 */       if (in2 != null) {
/* 1332 */         return in2;
/*      */       }
/*      */     }
/* 1335 */     return in;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final OutputStream _decorate(OutputStream out, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1342 */     if (this._outputDecorator != null) {
/* 1343 */       OutputStream out2 = this._outputDecorator.decorate(ctxt, out);
/* 1344 */       if (out2 != null) {
/* 1345 */         return out2;
/*      */       }
/*      */     }
/* 1348 */     return out;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final Writer _decorate(Writer out, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1355 */     if (this._outputDecorator != null) {
/* 1356 */       Writer out2 = this._outputDecorator.decorate(ctxt, out);
/* 1357 */       if (out2 != null) {
/* 1358 */         return out2;
/*      */       }
/*      */     }
/* 1361 */     return out;
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
/*      */   public BufferRecycler _getBufferRecycler()
/*      */   {
/* 1378 */     SoftReference<BufferRecycler> ref = (SoftReference)_recyclerRef.get();
/* 1379 */     BufferRecycler br = ref == null ? null : (BufferRecycler)ref.get();
/*      */     
/* 1381 */     if (br == null) {
/* 1382 */       br = new BufferRecycler();
/* 1383 */       _recyclerRef.set(new SoftReference(br));
/*      */     }
/* 1385 */     return br;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected IOContext _createContext(Object srcRef, boolean resourceManaged)
/*      */   {
/* 1393 */     return new IOContext(_getBufferRecycler(), srcRef, resourceManaged);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected InputStream _optimizedStreamFromURL(URL url)
/*      */     throws IOException
/*      */   {
/* 1402 */     if ("file".equals(url.getProtocol()))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1409 */       String host = url.getHost();
/* 1410 */       if ((host == null) || (host.length() == 0))
/*      */       {
/* 1412 */         String path = url.getPath();
/* 1413 */         if (path.indexOf('%') < 0) {
/* 1414 */           return new FileInputStream(url.getPath());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1420 */     return url.openStream();
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\JsonFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */