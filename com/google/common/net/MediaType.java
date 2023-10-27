/*      */ package com.google.common.net;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.CharMatcher;
/*      */ import com.google.common.base.Charsets;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Joiner.MapJoiner;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableCollection;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableListMultimap;
/*      */ import com.google.common.collect.ImmutableListMultimap.Builder;
/*      */ import com.google.common.collect.ImmutableMultiset;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Multimap;
/*      */ import com.google.common.collect.Multimaps;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.google.errorprone.annotations.Immutable;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Immutable
/*      */ @Beta
/*      */ @GwtCompatible
/*      */ public final class MediaType
/*      */ {
/*      */   private static final String CHARSET_ATTRIBUTE = "charset";
/*   81 */   private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of("charset", Ascii.toLowerCase(Charsets.UTF_8.name()));
/*      */   
/*      */ 
/*      */ 
/*   85 */   private static final CharMatcher TOKEN_MATCHER = CharMatcher.ascii()
/*   86 */     .and(CharMatcher.javaIsoControl().negate())
/*   87 */     .and(CharMatcher.isNot(' '))
/*   88 */     .and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
/*      */   
/*   90 */   private static final CharMatcher QUOTED_TEXT_MATCHER = CharMatcher.ascii().and(CharMatcher.noneOf("\"\\\r"));
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   96 */   private static final CharMatcher LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
/*      */   
/*      */   private static final String APPLICATION_TYPE = "application";
/*      */   
/*      */   private static final String AUDIO_TYPE = "audio";
/*      */   
/*      */   private static final String IMAGE_TYPE = "image";
/*      */   
/*      */   private static final String TEXT_TYPE = "text";
/*      */   private static final String VIDEO_TYPE = "video";
/*      */   private static final String WILDCARD = "*";
/*  107 */   private static final Map<MediaType, MediaType> KNOWN_TYPES = Maps.newHashMap();
/*      */   
/*      */   private static MediaType createConstant(String type, String subtype)
/*      */   {
/*  111 */     MediaType mediaType = addKnownType(new MediaType(type, subtype, ImmutableListMultimap.of()));
/*  112 */     mediaType.parsedCharset = Optional.absent();
/*  113 */     return mediaType;
/*      */   }
/*      */   
/*      */   private static MediaType createConstantUtf8(String type, String subtype) {
/*  117 */     MediaType mediaType = addKnownType(new MediaType(type, subtype, UTF_8_CONSTANT_PARAMETERS));
/*  118 */     mediaType.parsedCharset = Optional.of(Charsets.UTF_8);
/*  119 */     return mediaType;
/*      */   }
/*      */   
/*      */   private static MediaType addKnownType(MediaType mediaType) {
/*  123 */     KNOWN_TYPES.put(mediaType, mediaType);
/*  124 */     return mediaType;
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
/*  137 */   public static final MediaType ANY_TYPE = createConstant("*", "*");
/*  138 */   public static final MediaType ANY_TEXT_TYPE = createConstant("text", "*");
/*  139 */   public static final MediaType ANY_IMAGE_TYPE = createConstant("image", "*");
/*  140 */   public static final MediaType ANY_AUDIO_TYPE = createConstant("audio", "*");
/*  141 */   public static final MediaType ANY_VIDEO_TYPE = createConstant("video", "*");
/*  142 */   public static final MediaType ANY_APPLICATION_TYPE = createConstant("application", "*");
/*      */   
/*      */ 
/*      */ 
/*  146 */   public static final MediaType CACHE_MANIFEST_UTF_8 = createConstantUtf8("text", "cache-manifest");
/*  147 */   public static final MediaType CSS_UTF_8 = createConstantUtf8("text", "css");
/*  148 */   public static final MediaType CSV_UTF_8 = createConstantUtf8("text", "csv");
/*  149 */   public static final MediaType HTML_UTF_8 = createConstantUtf8("text", "html");
/*  150 */   public static final MediaType I_CALENDAR_UTF_8 = createConstantUtf8("text", "calendar");
/*  151 */   public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8("text", "plain");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  158 */   public static final MediaType TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8("text", "javascript");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  165 */   public static final MediaType TSV_UTF_8 = createConstantUtf8("text", "tab-separated-values");
/*      */   
/*  167 */   public static final MediaType VCARD_UTF_8 = createConstantUtf8("text", "vcard");
/*  168 */   public static final MediaType WML_UTF_8 = createConstantUtf8("text", "vnd.wap.wml");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  175 */   public static final MediaType XML_UTF_8 = createConstantUtf8("text", "xml");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  183 */   public static final MediaType VTT_UTF_8 = createConstantUtf8("text", "vtt");
/*      */   
/*      */ 
/*  186 */   public static final MediaType BMP = createConstant("image", "bmp");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  197 */   public static final MediaType CRW = createConstant("image", "x-canon-crw");
/*      */   
/*  199 */   public static final MediaType GIF = createConstant("image", "gif");
/*  200 */   public static final MediaType ICO = createConstant("image", "vnd.microsoft.icon");
/*  201 */   public static final MediaType JPEG = createConstant("image", "jpeg");
/*  202 */   public static final MediaType PNG = createConstant("image", "png");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  221 */   public static final MediaType PSD = createConstant("image", "vnd.adobe.photoshop");
/*      */   
/*  223 */   public static final MediaType SVG_UTF_8 = createConstantUtf8("image", "svg+xml");
/*  224 */   public static final MediaType TIFF = createConstant("image", "tiff");
/*  225 */   public static final MediaType WEBP = createConstant("image", "webp");
/*      */   
/*      */ 
/*  228 */   public static final MediaType MP4_AUDIO = createConstant("audio", "mp4");
/*  229 */   public static final MediaType MPEG_AUDIO = createConstant("audio", "mpeg");
/*  230 */   public static final MediaType OGG_AUDIO = createConstant("audio", "ogg");
/*  231 */   public static final MediaType WEBM_AUDIO = createConstant("audio", "webm");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  239 */   public static final MediaType L16_AUDIO = createConstant("audio", "l16");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  247 */   public static final MediaType L24_AUDIO = createConstant("audio", "l24");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  255 */   public static final MediaType BASIC_AUDIO = createConstant("audio", "basic");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  263 */   public static final MediaType AAC_AUDIO = createConstant("audio", "aac");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  271 */   public static final MediaType VORBIS_AUDIO = createConstant("audio", "vorbis");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  280 */   public static final MediaType WMA_AUDIO = createConstant("audio", "x-ms-wma");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  289 */   public static final MediaType WAX_AUDIO = createConstant("audio", "x-ms-wax");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  297 */   public static final MediaType VND_REAL_AUDIO = createConstant("audio", "vnd.rn-realaudio");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  305 */   public static final MediaType VND_WAVE_AUDIO = createConstant("audio", "vnd.wave");
/*      */   
/*      */ 
/*  308 */   public static final MediaType MP4_VIDEO = createConstant("video", "mp4");
/*  309 */   public static final MediaType MPEG_VIDEO = createConstant("video", "mpeg");
/*  310 */   public static final MediaType OGG_VIDEO = createConstant("video", "ogg");
/*  311 */   public static final MediaType QUICKTIME = createConstant("video", "quicktime");
/*  312 */   public static final MediaType WEBM_VIDEO = createConstant("video", "webm");
/*  313 */   public static final MediaType WMV = createConstant("video", "x-ms-wmv");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  322 */   public static final MediaType FLV_VIDEO = createConstant("video", "x-flv");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  331 */   public static final MediaType THREE_GPP_VIDEO = createConstant("video", "3gpp");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  340 */   public static final MediaType THREE_GPP2_VIDEO = createConstant("video", "3gpp2");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  348 */   public static final MediaType APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
/*      */   
/*  350 */   public static final MediaType ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
/*  351 */   public static final MediaType BZIP2 = createConstant("application", "x-bzip2");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  358 */   public static final MediaType DART_UTF_8 = createConstantUtf8("application", "dart");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  366 */   public static final MediaType APPLE_PASSBOOK = createConstant("application", "vnd.apple.pkpass");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  376 */   public static final MediaType EOT = createConstant("application", "vnd.ms-fontobject");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  387 */   public static final MediaType EPUB = createConstant("application", "epub+zip");
/*      */   
/*      */ 
/*  390 */   public static final MediaType FORM_DATA = createConstant("application", "x-www-form-urlencoded");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  399 */   public static final MediaType KEY_ARCHIVE = createConstant("application", "pkcs12");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  411 */   public static final MediaType APPLICATION_BINARY = createConstant("application", "binary");
/*      */   
/*  413 */   public static final MediaType GZIP = createConstant("application", "x-gzip");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  421 */   public static final MediaType HAL_JSON = createConstant("application", "hal+json");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  429 */   public static final MediaType JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
/*      */   
/*  431 */   public static final MediaType JSON_UTF_8 = createConstantUtf8("application", "json");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  440 */   public static final MediaType MANIFEST_JSON_UTF_8 = createConstantUtf8("application", "manifest+json");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  446 */   public static final MediaType KML = createConstant("application", "vnd.google-earth.kml+xml");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  452 */   public static final MediaType KMZ = createConstant("application", "vnd.google-earth.kmz");
/*      */   
/*      */ 
/*  455 */   public static final MediaType MBOX = createConstant("application", "mbox");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  464 */   public static final MediaType APPLE_MOBILE_CONFIG = createConstant("application", "x-apple-aspen-config");
/*      */   
/*  466 */   public static final MediaType MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
/*      */   
/*  468 */   public static final MediaType MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
/*  469 */   public static final MediaType MICROSOFT_WORD = createConstant("application", "msword");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  477 */   public static final MediaType WASM_APPLICATION = createConstant("application", "wasm");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  485 */   public static final MediaType NACL_APPLICATION = createConstant("application", "x-nacl");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  495 */   public static final MediaType NACL_PORTABLE_APPLICATION = createConstant("application", "x-pnacl");
/*      */   
/*  497 */   public static final MediaType OCTET_STREAM = createConstant("application", "octet-stream");
/*      */   
/*  499 */   public static final MediaType OGG_CONTAINER = createConstant("application", "ogg");
/*      */   
/*  501 */   public static final MediaType OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
/*      */   
/*      */ 
/*  504 */   public static final MediaType OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
/*      */   
/*      */ 
/*  507 */   public static final MediaType OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
/*      */   
/*  509 */   public static final MediaType OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
/*      */   
/*  511 */   public static final MediaType OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
/*      */   
/*  513 */   public static final MediaType OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
/*      */   
/*  515 */   public static final MediaType OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
/*  516 */   public static final MediaType PDF = createConstant("application", "pdf");
/*  517 */   public static final MediaType POSTSCRIPT = createConstant("application", "postscript");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  524 */   public static final MediaType PROTOBUF = createConstant("application", "protobuf");
/*      */   
/*  526 */   public static final MediaType RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
/*  527 */   public static final MediaType RTF_UTF_8 = createConstantUtf8("application", "rtf");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  538 */   public static final MediaType SFNT = createConstant("application", "font-sfnt");
/*      */   
/*      */ 
/*  541 */   public static final MediaType SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
/*  542 */   public static final MediaType SKETCHUP = createConstant("application", "vnd.sketchup.skp");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  555 */   public static final MediaType SOAP_XML_UTF_8 = createConstantUtf8("application", "soap+xml");
/*      */   
/*  557 */   public static final MediaType TAR = createConstant("application", "x-tar");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  567 */   public static final MediaType WOFF = createConstant("application", "font-woff");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  575 */   public static final MediaType WOFF2 = createConstant("application", "font-woff2");
/*      */   
/*  577 */   public static final MediaType XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  585 */   public static final MediaType XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
/*      */   
/*  587 */   public static final MediaType ZIP = createConstant("application", "zip");
/*      */   private final String type;
/*      */   private final String subtype;
/*      */   private final ImmutableListMultimap<String, String> parameters;
/*      */   @LazyInit
/*      */   private String toString;
/*      */   @LazyInit
/*      */   private int hashCode;
/*      */   @LazyInit
/*      */   private Optional<Charset> parsedCharset;
/*      */   
/*      */   private MediaType(String type, String subtype, ImmutableListMultimap<String, String> parameters)
/*      */   {
/*  600 */     this.type = type;
/*  601 */     this.subtype = subtype;
/*  602 */     this.parameters = parameters;
/*      */   }
/*      */   
/*      */   public String type()
/*      */   {
/*  607 */     return this.type;
/*      */   }
/*      */   
/*      */   public String subtype()
/*      */   {
/*  612 */     return this.subtype;
/*      */   }
/*      */   
/*      */   public ImmutableListMultimap<String, String> parameters()
/*      */   {
/*  617 */     return this.parameters;
/*      */   }
/*      */   
/*      */   private Map<String, ImmutableMultiset<String>> parametersAsMap() {
/*  621 */     Maps.transformValues(this.parameters
/*  622 */       .asMap(), new Function()
/*      */       {
/*      */         public ImmutableMultiset<String> apply(Collection<String> input)
/*      */         {
/*  626 */           return ImmutableMultiset.copyOf(input);
/*      */         }
/*      */       });
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
/*      */   public Optional<Charset> charset()
/*      */   {
/*  641 */     Optional<Charset> local = this.parsedCharset;
/*  642 */     if (local == null) {
/*  643 */       String value = null;
/*  644 */       local = Optional.absent();
/*  645 */       for (UnmodifiableIterator localUnmodifiableIterator = this.parameters.get("charset").iterator(); localUnmodifiableIterator.hasNext();) { String currentValue = (String)localUnmodifiableIterator.next();
/*  646 */         if (value == null) {
/*  647 */           value = currentValue;
/*  648 */           local = Optional.of(Charset.forName(value));
/*  649 */         } else if (!value.equals(currentValue)) {
/*  650 */           throw new IllegalStateException("Multiple charset values defined: " + value + ", " + currentValue);
/*      */         }
/*      */       }
/*      */       
/*  654 */       this.parsedCharset = local;
/*      */     }
/*  656 */     return local;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public MediaType withoutParameters()
/*      */   {
/*  664 */     return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MediaType withParameters(Multimap<String, String> parameters)
/*      */   {
/*  673 */     return create(this.type, this.subtype, parameters);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MediaType withParameters(String attribute, Iterable<String> values)
/*      */   {
/*  684 */     Preconditions.checkNotNull(attribute);
/*  685 */     Preconditions.checkNotNull(values);
/*  686 */     String normalizedAttribute = normalizeToken(attribute);
/*  687 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/*  688 */     for (Object localObject = this.parameters.entries().iterator(); ((Iterator)localObject).hasNext();) { Map.Entry<String, String> entry = (Map.Entry)((Iterator)localObject).next();
/*  689 */       String key = (String)entry.getKey();
/*  690 */       if (!normalizedAttribute.equals(key)) {
/*  691 */         builder.put(key, entry.getValue());
/*      */       }
/*      */     }
/*  694 */     for (localObject = values.iterator(); ((Iterator)localObject).hasNext();) { String value = (String)((Iterator)localObject).next();
/*  695 */       builder.put(normalizedAttribute, normalizeParameterValue(normalizedAttribute, value));
/*      */     }
/*  697 */     MediaType mediaType = new MediaType(this.type, this.subtype, builder.build());
/*      */     
/*  699 */     if (!normalizedAttribute.equals("charset")) {
/*  700 */       mediaType.parsedCharset = this.parsedCharset;
/*      */     }
/*      */     
/*  703 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MediaType withParameter(String attribute, String value)
/*      */   {
/*  715 */     return withParameters(attribute, ImmutableSet.of(value));
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
/*      */   public MediaType withCharset(Charset charset)
/*      */   {
/*  728 */     Preconditions.checkNotNull(charset);
/*  729 */     MediaType withCharset = withParameter("charset", charset.name());
/*      */     
/*  731 */     withCharset.parsedCharset = Optional.of(charset);
/*  732 */     return withCharset;
/*      */   }
/*      */   
/*      */   public boolean hasWildcard()
/*      */   {
/*  737 */     return ("*".equals(this.type)) || ("*".equals(this.subtype));
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
/*      */   public boolean is(MediaType mediaTypeRange)
/*      */   {
/*  770 */     return ((mediaTypeRange.type.equals("*")) || (mediaTypeRange.type.equals(this.type))) && 
/*  771 */       ((mediaTypeRange.subtype.equals("*")) || (mediaTypeRange.subtype.equals(this.subtype))) && 
/*  772 */       (this.parameters.entries().containsAll(mediaTypeRange.parameters.entries()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static MediaType create(String type, String subtype)
/*      */   {
/*  782 */     MediaType mediaType = create(type, subtype, ImmutableListMultimap.of());
/*  783 */     mediaType.parsedCharset = Optional.absent();
/*  784 */     return mediaType;
/*      */   }
/*      */   
/*      */   private static MediaType create(String type, String subtype, Multimap<String, String> parameters)
/*      */   {
/*  789 */     Preconditions.checkNotNull(type);
/*  790 */     Preconditions.checkNotNull(subtype);
/*  791 */     Preconditions.checkNotNull(parameters);
/*  792 */     String normalizedType = normalizeToken(type);
/*  793 */     String normalizedSubtype = normalizeToken(subtype);
/*  794 */     Preconditions.checkArgument(
/*  795 */       (!"*".equals(normalizedType)) || ("*".equals(normalizedSubtype)), "A wildcard type cannot be used with a non-wildcard subtype");
/*      */     
/*  797 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/*  798 */     for (Map.Entry<String, String> entry : parameters.entries()) {
/*  799 */       String attribute = normalizeToken((String)entry.getKey());
/*  800 */       builder.put(attribute, normalizeParameterValue(attribute, (String)entry.getValue()));
/*      */     }
/*  802 */     MediaType mediaType = new MediaType(normalizedType, normalizedSubtype, builder.build());
/*      */     
/*  804 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static MediaType createApplicationType(String subtype)
/*      */   {
/*  813 */     return create("application", subtype);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static MediaType createAudioType(String subtype)
/*      */   {
/*  822 */     return create("audio", subtype);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static MediaType createImageType(String subtype)
/*      */   {
/*  831 */     return create("image", subtype);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static MediaType createTextType(String subtype)
/*      */   {
/*  840 */     return create("text", subtype);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static MediaType createVideoType(String subtype)
/*      */   {
/*  849 */     return create("video", subtype);
/*      */   }
/*      */   
/*      */   private static String normalizeToken(String token) {
/*  853 */     Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(token));
/*  854 */     return Ascii.toLowerCase(token);
/*      */   }
/*      */   
/*      */   private static String normalizeParameterValue(String attribute, String value) {
/*  858 */     return "charset".equals(attribute) ? Ascii.toLowerCase(value) : value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static MediaType parse(String input)
/*      */   {
/*  867 */     Preconditions.checkNotNull(input);
/*  868 */     Tokenizer tokenizer = new Tokenizer(input);
/*      */     try {
/*  870 */       String type = tokenizer.consumeToken(TOKEN_MATCHER);
/*  871 */       tokenizer.consumeCharacter('/');
/*  872 */       String subtype = tokenizer.consumeToken(TOKEN_MATCHER);
/*  873 */       ImmutableListMultimap.Builder<String, String> parameters = ImmutableListMultimap.builder();
/*  874 */       while (tokenizer.hasMore()) {
/*  875 */         tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/*  876 */         tokenizer.consumeCharacter(';');
/*  877 */         tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/*  878 */         String attribute = tokenizer.consumeToken(TOKEN_MATCHER);
/*  879 */         tokenizer.consumeCharacter('=');
/*      */         String value;
/*  881 */         if ('"' == tokenizer.previewChar()) {
/*  882 */           tokenizer.consumeCharacter('"');
/*  883 */           StringBuilder valueBuilder = new StringBuilder();
/*  884 */           while ('"' != tokenizer.previewChar()) {
/*  885 */             if ('\\' == tokenizer.previewChar()) {
/*  886 */               tokenizer.consumeCharacter('\\');
/*  887 */               valueBuilder.append(tokenizer.consumeCharacter(CharMatcher.ascii()));
/*      */             } else {
/*  889 */               valueBuilder.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
/*      */             }
/*      */           }
/*  892 */           String value = valueBuilder.toString();
/*  893 */           tokenizer.consumeCharacter('"');
/*      */         } else {
/*  895 */           value = tokenizer.consumeToken(TOKEN_MATCHER);
/*      */         }
/*  897 */         parameters.put(attribute, value);
/*      */       }
/*  899 */       return create(type, subtype, parameters.build());
/*      */     } catch (IllegalStateException e) {
/*  901 */       throw new IllegalArgumentException("Could not parse '" + input + "'", e);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Tokenizer {
/*      */     final String input;
/*  907 */     int position = 0;
/*      */     
/*      */     Tokenizer(String input) {
/*  910 */       this.input = input;
/*      */     }
/*      */     
/*      */     String consumeTokenIfPresent(CharMatcher matcher) {
/*  914 */       Preconditions.checkState(hasMore());
/*  915 */       int startPosition = this.position;
/*  916 */       this.position = matcher.negate().indexIn(this.input, startPosition);
/*  917 */       return hasMore() ? this.input.substring(startPosition, this.position) : this.input.substring(startPosition);
/*      */     }
/*      */     
/*      */     String consumeToken(CharMatcher matcher) {
/*  921 */       int startPosition = this.position;
/*  922 */       String token = consumeTokenIfPresent(matcher);
/*  923 */       Preconditions.checkState(this.position != startPosition);
/*  924 */       return token;
/*      */     }
/*      */     
/*      */     char consumeCharacter(CharMatcher matcher) {
/*  928 */       Preconditions.checkState(hasMore());
/*  929 */       char c = previewChar();
/*  930 */       Preconditions.checkState(matcher.matches(c));
/*  931 */       this.position += 1;
/*  932 */       return c;
/*      */     }
/*      */     
/*      */     char consumeCharacter(char c) {
/*  936 */       Preconditions.checkState(hasMore());
/*  937 */       Preconditions.checkState(previewChar() == c);
/*  938 */       this.position += 1;
/*  939 */       return c;
/*      */     }
/*      */     
/*      */     char previewChar() {
/*  943 */       Preconditions.checkState(hasMore());
/*  944 */       return this.input.charAt(this.position);
/*      */     }
/*      */     
/*      */     boolean hasMore() {
/*  948 */       return (this.position >= 0) && (this.position < this.input.length());
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean equals(Object obj)
/*      */   {
/*  954 */     if (obj == this)
/*  955 */       return true;
/*  956 */     if ((obj instanceof MediaType)) {
/*  957 */       MediaType that = (MediaType)obj;
/*  958 */       if ((this.type.equals(that.type)) && 
/*  959 */         (this.subtype.equals(that.subtype))) {}
/*  958 */       return 
/*      */       
/*      */ 
/*  961 */         parametersAsMap().equals(that.parametersAsMap());
/*      */     }
/*  963 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  970 */     int h = this.hashCode;
/*  971 */     if (h == 0) {
/*  972 */       h = Objects.hashCode(new Object[] { this.type, this.subtype, parametersAsMap() });
/*  973 */       this.hashCode = h;
/*      */     }
/*  975 */     return h;
/*      */   }
/*      */   
/*  978 */   private static final Joiner.MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  987 */     String result = this.toString;
/*  988 */     if (result == null) {
/*  989 */       result = computeToString();
/*  990 */       this.toString = result;
/*      */     }
/*  992 */     return result;
/*      */   }
/*      */   
/*      */   private String computeToString() {
/*  996 */     StringBuilder builder = new StringBuilder().append(this.type).append('/').append(this.subtype);
/*  997 */     if (!this.parameters.isEmpty()) {
/*  998 */       builder.append("; ");
/*      */       
/* 1000 */       Multimap<String, String> quotedParameters = Multimaps.transformValues(this.parameters, new Function()
/*      */       {
/*      */ 
/*      */         public String apply(String value)
/*      */         {
/*      */ 
/* 1005 */           return MediaType.TOKEN_MATCHER.matchesAllOf(value) ? value : MediaType.escapeAndQuote(value);
/*      */         }
/* 1007 */       });
/* 1008 */       PARAMETER_JOINER.appendTo(builder, quotedParameters.entries());
/*      */     }
/* 1010 */     return builder.toString();
/*      */   }
/*      */   
/*      */   private static String escapeAndQuote(String value) {
/* 1014 */     StringBuilder escaped = new StringBuilder(value.length() + 16).append('"');
/* 1015 */     for (int i = 0; i < value.length(); i++) {
/* 1016 */       char ch = value.charAt(i);
/* 1017 */       if ((ch == '\r') || (ch == '\\') || (ch == '"')) {
/* 1018 */         escaped.append('\\');
/*      */       }
/* 1020 */       escaped.append(ch);
/*      */     }
/* 1022 */     return '"';
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\net\MediaType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */