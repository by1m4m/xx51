/*      */ package org.eclipse.jetty.xml;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.StringReader;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.net.InetAddress;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.net.UnknownHostException;
/*      */ import java.nio.file.Path;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Queue;
/*      */ import java.util.ServiceLoader;
/*      */ import java.util.Set;
/*      */ import org.eclipse.jetty.util.LazyList;
/*      */ import org.eclipse.jetty.util.Loader;
/*      */ import org.eclipse.jetty.util.MultiException;
/*      */ import org.eclipse.jetty.util.StringUtil;
/*      */ import org.eclipse.jetty.util.TypeUtil;
/*      */ import org.eclipse.jetty.util.component.LifeCycle;
/*      */ import org.eclipse.jetty.util.log.Log;
/*      */ import org.eclipse.jetty.util.log.Logger;
/*      */ import org.eclipse.jetty.util.resource.Resource;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class XmlConfiguration
/*      */ {
/*   83 */   private static final Logger LOG = Log.getLogger(XmlConfiguration.class);
/*   84 */   private static final Class<?>[] __primitives = { Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE };
/*      */   
/*   86 */   private static final Class<?>[] __boxedPrimitives = { Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class };
/*      */   
/*   88 */   private static final Class<?>[] __supportedCollections = { ArrayList.class, HashSet.class, Queue.class, List.class, Set.class, Collection.class };
/*      */   
/*   90 */   private static final Iterable<ConfigurationProcessorFactory> __factoryLoader = ServiceLoader.load(ConfigurationProcessorFactory.class);
/*   91 */   private static final XmlParser __parser = initParser();
/*      */   
/*      */   private static XmlParser initParser() {
/*   94 */     ClassLoader loader = XmlConfiguration.class.getClassLoader();
/*   95 */     XmlParser parser = new XmlParser();
/*   96 */     URL config60 = loader.getResource("org/eclipse/jetty/xml/configure_6_0.dtd");
/*   97 */     URL config76 = loader.getResource("org/eclipse/jetty/xml/configure_7_6.dtd");
/*   98 */     URL config90 = loader.getResource("org/eclipse/jetty/xml/configure_9_0.dtd");
/*   99 */     URL config93 = loader.getResource("org/eclipse/jetty/xml/configure_9_3.dtd");
/*  100 */     parser.redirectEntity("configure.dtd", config90);
/*  101 */     parser.redirectEntity("configure_1_0.dtd", config60);
/*  102 */     parser.redirectEntity("configure_1_1.dtd", config60);
/*  103 */     parser.redirectEntity("configure_1_2.dtd", config60);
/*  104 */     parser.redirectEntity("configure_1_3.dtd", config60);
/*  105 */     parser.redirectEntity("configure_6_0.dtd", config60);
/*  106 */     parser.redirectEntity("configure_7_6.dtd", config76);
/*  107 */     parser.redirectEntity("configure_9_0.dtd", config90);
/*  108 */     parser.redirectEntity("configure_9_3.dtd", config93);
/*      */     
/*  110 */     parser.redirectEntity("http://jetty.mortbay.org/configure.dtd", config93);
/*  111 */     parser.redirectEntity("http://jetty.eclipse.org/configure.dtd", config93);
/*  112 */     parser.redirectEntity("http://www.eclipse.org/jetty/configure.dtd", config93);
/*      */     
/*  114 */     parser.redirectEntity("-//Mort Bay Consulting//DTD Configure//EN", config93);
/*  115 */     parser.redirectEntity("-//Jetty//Configure//EN", config93);
/*      */     
/*  117 */     return parser;
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
/*      */   public void setJettyStandardIdsAndProperties(Object server, Resource webapp)
/*      */   {
/*      */     try
/*      */     {
/*  138 */       if (server != null) {
/*  139 */         getIdMap().put("Server", server);
/*      */       }
/*  141 */       Resource home = Resource.newResource(System.getProperty("jetty.home", "."));
/*  142 */       getProperties().put("jetty.home", home.toString());
/*  143 */       getProperties().put("jetty.home.uri", normalizeURI(home.getURI().toString()));
/*      */       
/*  145 */       Resource base = Resource.newResource(System.getProperty("jetty.base", home.toString()));
/*  146 */       getProperties().put("jetty.base", base.toString());
/*  147 */       getProperties().put("jetty.base.uri", normalizeURI(base.getURI().toString()));
/*      */       
/*  149 */       if (webapp != null)
/*      */       {
/*  151 */         getProperties().put("jetty.webapp", webapp.toString());
/*  152 */         getProperties().put("jetty.webapps", webapp.getFile().toPath().getParent().toString());
/*  153 */         getProperties().put("jetty.webapps.uri", normalizeURI(webapp.getURI().toString()));
/*      */       }
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  158 */       LOG.warn(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public static String normalizeURI(String uri)
/*      */   {
/*  164 */     if (uri.endsWith("/"))
/*  165 */       return uri.substring(0, uri.length() - 1);
/*  166 */     return uri;
/*      */   }
/*      */   
/*  169 */   private final Map<String, Object> _idMap = new HashMap();
/*  170 */   private final Map<String, String> _propertyMap = new HashMap();
/*      */   
/*      */ 
/*      */   private final URL _url;
/*      */   
/*      */ 
/*      */   private final String _dtd;
/*      */   
/*      */   private ConfigurationProcessor _processor;
/*      */   
/*      */ 
/*      */   public XmlConfiguration(URL configuration)
/*      */     throws SAXException, IOException
/*      */   {
/*  184 */     synchronized (__parser)
/*      */     {
/*  186 */       this._url = configuration;
/*  187 */       setConfig(__parser.parse(configuration.toString()));
/*  188 */       this._dtd = __parser.getDTD();
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
/*      */   public XmlConfiguration(String configuration)
/*      */     throws SAXException, IOException
/*      */   {
/*  202 */     configuration = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<!DOCTYPE Configure PUBLIC \"-//Jetty//Configure//EN\" \"http://eclipse.org/jetty/configure.dtd\">" + configuration;
/*      */     
/*  204 */     InputSource source = new InputSource(new StringReader(configuration));
/*  205 */     synchronized (__parser)
/*      */     {
/*  207 */       this._url = null;
/*  208 */       setConfig(__parser.parse(source));
/*  209 */       this._dtd = __parser.getDTD();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public XmlConfiguration(InputStream configuration)
/*      */     throws SAXException, IOException
/*      */   {
/*  222 */     InputSource source = new InputSource(configuration);
/*  223 */     synchronized (__parser)
/*      */     {
/*  225 */       this._url = null;
/*  226 */       setConfig(__parser.parse(source));
/*  227 */       this._dtd = __parser.getDTD();
/*      */     }
/*      */   }
/*      */   
/*      */   private void setConfig(XmlParser.Node config)
/*      */   {
/*  233 */     if ("Configure".equals(config.getTag()))
/*      */     {
/*  235 */       this._processor = new JettyXmlConfiguration(null);
/*      */     }
/*  237 */     else if (__factoryLoader != null)
/*      */     {
/*  239 */       for (ConfigurationProcessorFactory factory : __factoryLoader)
/*      */       {
/*  241 */         this._processor = factory.getConfigurationProcessor(this._dtd, config.getTag());
/*  242 */         if (this._processor != null) {
/*      */           break;
/*      */         }
/*      */       }
/*  246 */       if (this._processor == null) {
/*  247 */         throw new IllegalStateException("Unknown configuration type: " + config.getTag() + " in " + this);
/*      */       }
/*      */     }
/*      */     else {
/*  251 */       throw new IllegalArgumentException("Unknown XML tag:" + config.getTag());
/*      */     }
/*  253 */     this._processor.init(this._url, config, this);
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
/*      */   public Map<String, Object> getIdMap()
/*      */   {
/*  273 */     return this._idMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map<String, String> getProperties()
/*      */   {
/*  284 */     return this._propertyMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object configure(Object obj)
/*      */     throws Exception
/*      */   {
/*  297 */     return this._processor.configure(obj);
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
/*      */   public Object configure()
/*      */     throws Exception
/*      */   {
/*  311 */     return this._processor.configure();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void initializeDefaults(Object object) {}
/*      */   
/*      */ 
/*      */ 
/*      */   private static class JettyXmlConfiguration
/*      */     implements ConfigurationProcessor
/*      */   {
/*      */     private String _url;
/*      */     
/*      */ 
/*      */     XmlParser.Node _root;
/*      */     
/*      */ 
/*      */     XmlConfiguration _configuration;
/*      */     
/*      */ 
/*      */ 
/*      */     public void init(URL url, XmlParser.Node root, XmlConfiguration configuration)
/*      */     {
/*  336 */       this._url = (url == null ? null : url.toString());
/*  337 */       this._root = root;
/*  338 */       this._configuration = configuration;
/*      */     }
/*      */     
/*      */ 
/*      */     public Object configure(Object obj)
/*      */       throws Exception
/*      */     {
/*  345 */       Class<?> oClass = nodeClass(this._root);
/*  346 */       if ((oClass != null) && (!oClass.isInstance(obj)))
/*      */       {
/*  348 */         String loaders = oClass.getClassLoader() == obj.getClass().getClassLoader() ? "" : "Object Class and type Class are from different loaders.";
/*  349 */         throw new IllegalArgumentException("Object of class '" + obj.getClass().getCanonicalName() + "' is not of type '" + oClass.getCanonicalName() + "'. " + loaders + " in " + this._url);
/*      */       }
/*  351 */       String id = this._root.getAttribute("id");
/*  352 */       if (id != null)
/*  353 */         this._configuration.getIdMap().put(id, obj);
/*  354 */       configure(obj, this._root, 0);
/*  355 */       return obj;
/*      */     }
/*      */     
/*      */     public Object configure()
/*      */       throws Exception
/*      */     {
/*  361 */       Class<?> oClass = nodeClass(this._root);
/*      */       
/*  363 */       String id = this._root.getAttribute("id");
/*  364 */       Object obj = id == null ? null : this._configuration.getIdMap().get(id);
/*      */       
/*  366 */       int index = 0;
/*  367 */       if ((obj == null) && (oClass != null))
/*      */       {
/*  369 */         index = this._root.size();
/*  370 */         Map<String, Object> namedArgMap = new HashMap();
/*      */         
/*  372 */         List<Object> arguments = new LinkedList();
/*  373 */         for (int i = 0; i < this._root.size(); i++)
/*      */         {
/*  375 */           Object o = this._root.get(i);
/*  376 */           if (!(o instanceof String))
/*      */           {
/*      */ 
/*      */ 
/*  380 */             XmlParser.Node node = (XmlParser.Node)o;
/*      */             
/*  382 */             if (!node.getTag().equals("Arg"))
/*      */             {
/*  384 */               index = i;
/*  385 */               break;
/*      */             }
/*      */             
/*      */ 
/*  389 */             String namedAttribute = node.getAttribute("name");
/*  390 */             Object value = value(obj, (XmlParser.Node)o);
/*  391 */             if (namedAttribute != null)
/*  392 */               namedArgMap.put(namedAttribute, value);
/*  393 */             arguments.add(value);
/*      */           }
/*      */         }
/*      */         
/*      */         try
/*      */         {
/*  399 */           if (namedArgMap.size() > 0) {
/*  400 */             obj = TypeUtil.construct(oClass, arguments.toArray(), namedArgMap);
/*      */           } else {
/*  402 */             obj = TypeUtil.construct(oClass, arguments.toArray());
/*      */           }
/*      */         }
/*      */         catch (NoSuchMethodException x) {
/*  406 */           throw new IllegalStateException(String.format("No constructor %s(%s,%s) in %s", new Object[] { oClass, arguments, namedArgMap, this._url }));
/*      */         }
/*      */       }
/*  409 */       if (id != null) {
/*  410 */         this._configuration.getIdMap().put(id, obj);
/*      */       }
/*  412 */       this._configuration.initializeDefaults(obj);
/*  413 */       configure(obj, this._root, index);
/*  414 */       return obj;
/*      */     }
/*      */     
/*      */     private static Class<?> nodeClass(XmlParser.Node node) throws ClassNotFoundException
/*      */     {
/*  419 */       String className = node.getAttribute("class");
/*  420 */       if (className == null) {
/*  421 */         return null;
/*      */       }
/*  423 */       return Loader.loadClass(className);
/*      */     }
/*      */     
/*      */     public void configure(Object obj, XmlParser.Node cfg, int i)
/*      */       throws Exception
/*      */     {
/*  438 */       for (; 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  438 */           i < cfg.size(); i++)
/*      */       {
/*  440 */         Object o = cfg.get(i);
/*  441 */         if (!(o instanceof String))
/*      */         {
/*  443 */           XmlParser.Node node = (XmlParser.Node)o;
/*  444 */           if (!"Arg".equals(node.getTag()))
/*      */             break;
/*  446 */           XmlConfiguration.LOG.warn("Ignored arg: " + node, new Object[0]);
/*      */         }
/*      */       }
/*  453 */       for (; 
/*      */           
/*      */ 
/*      */ 
/*  453 */           i < cfg.size(); i++)
/*      */       {
/*  455 */         Object o = cfg.get(i);
/*  456 */         if (!(o instanceof String))
/*      */         {
/*  458 */           XmlParser.Node node = (XmlParser.Node)o;
/*      */           
/*      */           try
/*      */           {
/*  462 */             String tag = node.getTag();
/*  463 */             switch (tag)
/*      */             {
/*      */             case "Set": 
/*  466 */               set(obj, node);
/*  467 */               break;
/*      */             case "Put": 
/*  469 */               put(obj, node);
/*  470 */               break;
/*      */             case "Call": 
/*  472 */               call(obj, node);
/*  473 */               break;
/*      */             case "Get": 
/*  475 */               get(obj, node);
/*  476 */               break;
/*      */             case "New": 
/*  478 */               newObj(obj, node);
/*  479 */               break;
/*      */             case "Array": 
/*  481 */               newArray(obj, node);
/*  482 */               break;
/*      */             case "Map": 
/*  484 */               newMap(obj, node);
/*  485 */               break;
/*      */             case "Ref": 
/*  487 */               refObj(obj, node);
/*  488 */               break;
/*      */             case "Property": 
/*  490 */               propertyObj(node);
/*  491 */               break;
/*      */             case "SystemProperty": 
/*  493 */               systemPropertyObj(node);
/*  494 */               break;
/*      */             case "Env": 
/*  496 */               envObj(node);
/*  497 */               break;
/*      */             default: 
/*  499 */               throw new IllegalStateException("Unknown tag: " + tag + " in " + this._url);
/*      */             }
/*      */           }
/*      */           catch (Exception e)
/*      */           {
/*  504 */             XmlConfiguration.LOG.warn("Config error at " + node, new Object[] { e.toString() + " in " + this._url });
/*  505 */             throw e;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void set(Object obj, XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/*  519 */       String attr = node.getAttribute("name");
/*  520 */       String name = "set" + attr.substring(0, 1).toUpperCase(Locale.ENGLISH) + attr.substring(1);
/*  521 */       Object value = value(obj, node);
/*  522 */       Object[] arg = { value };
/*      */       
/*      */ 
/*  525 */       Class<?> oClass = nodeClass(node);
/*  526 */       if (oClass != null) {
/*  527 */         obj = null;
/*      */       } else {
/*  529 */         oClass = obj.getClass();
/*      */       }
/*  531 */       Class<?>[] vClass = { Object.class };
/*      */       
/*  533 */       if (value != null) {
/*  534 */         vClass[0] = value.getClass();
/*      */       }
/*  536 */       if (XmlConfiguration.LOG.isDebugEnabled()) {
/*  537 */         XmlConfiguration.LOG.debug("XML " + (obj != null ? obj.toString() : oClass.getName()) + "." + name + "(" + value + ")", new Object[0]);
/*      */       }
/*  539 */       final MultiException me = new MultiException();
/*      */       
/*      */ 
/*      */       try
/*      */       {
/*  544 */         Method set = oClass.getMethod(name, vClass);
/*  545 */         set.invoke(obj, arg);
/*  546 */         return;
/*      */       }
/*      */       catch (IllegalArgumentException|IllegalAccessException|NoSuchMethodException e)
/*      */       {
/*  550 */         XmlConfiguration.LOG.ignore(e);
/*  551 */         me.add(e);
/*      */         
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/*  557 */           Field type = vClass[0].getField("TYPE");
/*  558 */           vClass[0] = ((Class)type.get(null));
/*  559 */           Method set = oClass.getMethod(name, vClass);
/*  560 */           set.invoke(obj, arg);
/*  561 */           return;
/*      */         }
/*      */         catch (NoSuchFieldException|IllegalArgumentException|IllegalAccessException|NoSuchMethodException e)
/*      */         {
/*  565 */           XmlConfiguration.LOG.ignore(e);
/*  566 */           me.add(e);
/*      */           
/*      */ 
/*      */ 
/*      */           try
/*      */           {
/*  572 */             Field field = oClass.getField(attr);
/*  573 */             if (Modifier.isPublic(field.getModifiers()))
/*      */             {
/*  575 */               field.set(obj, value);
/*  576 */               return;
/*      */             }
/*      */           }
/*      */           catch (NoSuchFieldException e)
/*      */           {
/*  581 */             XmlConfiguration.LOG.ignore(e);
/*  582 */             me.add(e);
/*      */           }
/*      */           
/*      */ 
/*  586 */           Method[] sets = oClass.getMethods();
/*  587 */           Method set = null;
/*  588 */           String types = null;
/*  589 */           for (int s = 0; (sets != null) && (s < sets.length); s++)
/*      */           {
/*  591 */             if (sets[s].getParameterCount() == 1)
/*      */             {
/*  593 */               Class<?>[] paramTypes = sets[s].getParameterTypes();
/*  594 */               if (name.equals(sets[s].getName()))
/*      */               {
/*  596 */                 types = types + "," + paramTypes[0].getName();
/*      */                 
/*      */                 try
/*      */                 {
/*  600 */                   set = sets[s];
/*  601 */                   sets[s].invoke(obj, arg);
/*  602 */                   return;
/*      */                 }
/*      */                 catch (IllegalArgumentException|IllegalAccessException e)
/*      */                 {
/*  606 */                   XmlConfiguration.LOG.ignore(e);
/*  607 */                   me.add(e);
/*      */                   
/*      */ 
/*      */                   try
/*      */                   {
/*  612 */                     for (Class<?> c : XmlConfiguration.__supportedCollections) {
/*  613 */                       if (paramTypes[0].isAssignableFrom(c))
/*      */                       {
/*  615 */                         sets[s].invoke(obj, new Object[] { convertArrayToCollection(value, c) });
/*  616 */                         return;
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                   catch (IllegalAccessException e) {
/*  621 */                     XmlConfiguration.LOG.ignore(e);
/*  622 */                     me.add(e);
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*  628 */           if (set != null)
/*      */           {
/*      */             try
/*      */             {
/*  632 */               Class<?> sClass = set.getParameterTypes()[0];
/*  633 */               if (sClass.isPrimitive())
/*      */               {
/*  635 */                 for (int t = 0; t < XmlConfiguration.__primitives.length; t++)
/*      */                 {
/*  637 */                   if (sClass.equals(XmlConfiguration.__primitives[t]))
/*      */                   {
/*  639 */                     sClass = XmlConfiguration.__boxedPrimitives[t];
/*  640 */                     break;
/*      */                   }
/*      */                 }
/*      */               }
/*  644 */               Constructor<?> cons = sClass.getConstructor(vClass);
/*  645 */               arg[0] = cons.newInstance(arg);
/*  646 */               this._configuration.initializeDefaults(arg[0]);
/*  647 */               set.invoke(obj, arg);
/*  648 */               return;
/*      */             }
/*      */             catch (NoSuchMethodException|IllegalAccessException|InstantiationException e)
/*      */             {
/*  652 */               XmlConfiguration.LOG.ignore(e);
/*  653 */               me.add(e);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  658 */           String message = oClass + "." + name + "(" + vClass[0] + ")";
/*  659 */           if (types != null)
/*  660 */             message = message + ". Found setters for " + types;
/*  661 */           throw new NoSuchMethodException(message) {};
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private static Collection<?> convertArrayToCollection(Object array, Class<?> collectionType)
/*      */     {
/*  677 */       Collection<?> collection = null;
/*  678 */       if (array.getClass().isArray())
/*      */       {
/*  680 */         if (collectionType.isAssignableFrom(ArrayList.class)) {
/*  681 */           collection = convertArrayToArrayList(array);
/*  682 */         } else if (collectionType.isAssignableFrom(HashSet.class))
/*  683 */           collection = new HashSet(convertArrayToArrayList(array));
/*      */       }
/*  685 */       if (collection == null)
/*  686 */         throw new IllegalArgumentException("Can't convert \"" + array.getClass() + "\" to " + collectionType);
/*  687 */       return collection;
/*      */     }
/*      */     
/*      */     private static ArrayList<Object> convertArrayToArrayList(Object array)
/*      */     {
/*  692 */       int length = Array.getLength(array);
/*  693 */       ArrayList<Object> list = new ArrayList(length);
/*  694 */       for (int i = 0; i < length; i++)
/*  695 */         list.add(Array.get(array, i));
/*  696 */       return list;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void put(Object obj, XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/*  706 */       if (!(obj instanceof Map)) {
/*  707 */         throw new IllegalArgumentException("Object for put is not a Map: " + obj);
/*      */       }
/*  709 */       Map<Object, Object> map = (Map)obj;
/*      */       
/*  711 */       String name = node.getAttribute("name");
/*  712 */       Object value = value(obj, node);
/*  713 */       map.put(name, value);
/*  714 */       if (XmlConfiguration.LOG.isDebugEnabled()) {
/*  715 */         XmlConfiguration.LOG.debug("XML " + obj + ".put(" + name + "," + value + ")", new Object[0]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private Object get(Object obj, XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/*  725 */       Class<?> oClass = nodeClass(node);
/*  726 */       if (oClass != null) {
/*  727 */         obj = null;
/*      */       } else {
/*  729 */         oClass = obj.getClass();
/*      */       }
/*  731 */       String name = node.getAttribute("name");
/*  732 */       String id = node.getAttribute("id");
/*  733 */       if (XmlConfiguration.LOG.isDebugEnabled()) {
/*  734 */         XmlConfiguration.LOG.debug("XML get " + name, new Object[0]);
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  739 */         if ("class".equalsIgnoreCase(name)) {
/*  740 */           obj = oClass;
/*      */         }
/*      */         else
/*      */         {
/*  744 */           Method method = oClass.getMethod("get" + name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1), (Class[])null);
/*  745 */           obj = method.invoke(obj, (Object[])null);
/*      */         }
/*  747 */         if (id != null)
/*  748 */           this._configuration.getIdMap().put(id, obj);
/*  749 */         configure(obj, node, 0);
/*      */       }
/*      */       catch (NoSuchMethodException nsme)
/*      */       {
/*      */         try
/*      */         {
/*  755 */           Field field = oClass.getField(name);
/*  756 */           obj = field.get(obj);
/*  757 */           configure(obj, node, 0);
/*      */         }
/*      */         catch (NoSuchFieldException nsfe)
/*      */         {
/*  761 */           throw nsme;
/*      */         }
/*      */       }
/*  764 */       return obj;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Object call(Object obj, XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/*  776 */       AttrOrElementNode aoeNode = new AttrOrElementNode(obj, node, new String[] { "Id", "Name", "Class", "Arg" });
/*  777 */       String id = aoeNode.getString("Id");
/*  778 */       String name = aoeNode.getString("Name");
/*  779 */       String clazz = aoeNode.getString("Class");
/*  780 */       List<Object> args = aoeNode.getList("Arg");
/*      */       
/*      */ 
/*      */ 
/*  784 */       if (clazz != null)
/*      */       {
/*      */ 
/*  787 */         Class<?> oClass = Loader.loadClass(clazz);
/*  788 */         obj = null;
/*      */       } else { Class<?> oClass;
/*  790 */         if (obj != null)
/*      */         {
/*  792 */           oClass = obj.getClass();
/*      */         }
/*      */         else
/*  795 */           throw new IllegalArgumentException(node.toString()); }
/*      */       Class<?> oClass;
/*  797 */       if (XmlConfiguration.LOG.isDebugEnabled()) {
/*  798 */         XmlConfiguration.LOG.debug("XML call " + name, new Object[0]);
/*      */       }
/*      */       try
/*      */       {
/*  802 */         Object nobj = TypeUtil.call(oClass, name, obj, args.toArray(new Object[args.size()]));
/*  803 */         if (id != null)
/*  804 */           this._configuration.getIdMap().put(id, nobj);
/*  805 */         configure(nobj, node, aoeNode.getNext());
/*  806 */         return nobj;
/*      */       }
/*      */       catch (NoSuchMethodException e)
/*      */       {
/*  810 */         IllegalStateException ise = new IllegalStateException("No Method: " + node + " on " + oClass);
/*  811 */         ise.initCause(e);
/*  812 */         throw ise;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Object newObj(Object obj, XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/*  826 */       AttrOrElementNode aoeNode = new AttrOrElementNode(obj, node, new String[] { "Id", "Class", "Arg" });
/*  827 */       String id = aoeNode.getString("Id");
/*  828 */       String clazz = aoeNode.getString("Class");
/*  829 */       List<XmlParser.Node> argNodes = aoeNode.getNodes("Arg");
/*      */       
/*  831 */       if (XmlConfiguration.LOG.isDebugEnabled()) {
/*  832 */         XmlConfiguration.LOG.debug("XML new " + clazz, new Object[0]);
/*      */       }
/*  834 */       Class<?> oClass = Loader.loadClass(clazz);
/*      */       
/*      */ 
/*  837 */       Map<String, Object> namedArgMap = new HashMap();
/*  838 */       List<Object> arguments = new LinkedList();
/*  839 */       for (XmlParser.Node child : argNodes)
/*      */       {
/*  841 */         String namedAttribute = child.getAttribute("name");
/*  842 */         Object value = value(obj, child);
/*  843 */         if (namedAttribute != null)
/*      */         {
/*      */ 
/*  846 */           namedArgMap.put(namedAttribute, value);
/*      */         }
/*      */         
/*  849 */         arguments.add(value);
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*      */         Object nobj;
/*  855 */         if (namedArgMap.size() > 0)
/*      */         {
/*  857 */           XmlConfiguration.LOG.debug("using named mapping", new Object[0]);
/*  858 */           nobj = TypeUtil.construct(oClass, arguments.toArray(), namedArgMap);
/*      */         }
/*      */         else
/*      */         {
/*  862 */           XmlConfiguration.LOG.debug("using normal mapping", new Object[0]);
/*  863 */           nobj = TypeUtil.construct(oClass, arguments.toArray());
/*      */         }
/*      */       }
/*      */       catch (NoSuchMethodException e) {
/*      */         Object nobj;
/*  868 */         throw new IllegalStateException("No suitable constructor: " + node + " on " + obj);
/*      */       }
/*      */       Object nobj;
/*  871 */       if (id != null) {
/*  872 */         this._configuration.getIdMap().put(id, nobj);
/*      */       }
/*  874 */       this._configuration.initializeDefaults(nobj);
/*  875 */       configure(nobj, node, aoeNode.getNext());
/*  876 */       return nobj;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Object refObj(Object obj, XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/*  886 */       String refid = node.getAttribute("refid");
/*  887 */       if (refid == null)
/*  888 */         refid = node.getAttribute("id");
/*  889 */       obj = this._configuration.getIdMap().get(refid);
/*  890 */       if ((obj == null) && (node.size() > 0))
/*  891 */         throw new IllegalStateException("No object for refid=" + refid);
/*  892 */       configure(obj, node, 0);
/*  893 */       return obj;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private Object newArray(Object obj, XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/*  901 */       AttrOrElementNode aoeNode = new AttrOrElementNode(obj, node, new String[] { "Id", "Type", "Item" });
/*  902 */       String id = aoeNode.getString("Id");
/*  903 */       String type = aoeNode.getString("Type");
/*  904 */       List<XmlParser.Node> items = aoeNode.getNodes("Item");
/*      */       
/*      */ 
/*  907 */       Class<?> aClass = Object.class;
/*  908 */       if (type != null)
/*      */       {
/*  910 */         aClass = TypeUtil.fromName(type);
/*  911 */         if (aClass == null)
/*      */         {
/*  913 */           switch (type)
/*      */           {
/*      */           case "String": 
/*  916 */             aClass = String.class;
/*  917 */             break;
/*      */           case "URL": 
/*  919 */             aClass = URL.class;
/*  920 */             break;
/*      */           case "InetAddress": 
/*  922 */             aClass = InetAddress.class;
/*  923 */             break;
/*      */           default: 
/*  925 */             aClass = Loader.loadClass(type);
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*      */       
/*  931 */       Object al = null;
/*      */       
/*  933 */       for (XmlParser.Node item : items)
/*      */       {
/*  935 */         String nid = item.getAttribute("id");
/*  936 */         Object v = value(obj, item);
/*  937 */         al = LazyList.add(al, (v == null) && (aClass.isPrimitive()) ? Integer.valueOf(0) : v);
/*  938 */         if (nid != null) {
/*  939 */           this._configuration.getIdMap().put(nid, v);
/*      */         }
/*      */       }
/*  942 */       Object array = LazyList.toArray(al, aClass);
/*  943 */       if (id != null)
/*  944 */         this._configuration.getIdMap().put(id, array);
/*  945 */       return array;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private Object newMap(Object obj, XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/*  953 */       AttrOrElementNode aoeNode = new AttrOrElementNode(node, new String[] { "Id", "Entry" });
/*  954 */       String id = aoeNode.getString("Id");
/*  955 */       List<XmlParser.Node> entries = aoeNode.getNodes("Entry");
/*      */       
/*  957 */       Map<Object, Object> map = new HashMap();
/*  958 */       if (id != null) {
/*  959 */         this._configuration.getIdMap().put(id, map);
/*      */       }
/*  961 */       for (XmlParser.Node entry : entries)
/*      */       {
/*  963 */         if (!entry.getTag().equals("Entry")) {
/*  964 */           throw new IllegalStateException("Not an Entry");
/*      */         }
/*  966 */         XmlParser.Node key = null;
/*  967 */         XmlParser.Node value = null;
/*      */         
/*  969 */         for (Object object : entry)
/*      */         {
/*  971 */           if (!(object instanceof String))
/*      */           {
/*  973 */             XmlParser.Node item = (XmlParser.Node)object;
/*  974 */             if (!item.getTag().equals("Item"))
/*  975 */               throw new IllegalStateException("Not an Item");
/*  976 */             if (key == null) {
/*  977 */               key = item;
/*      */             } else
/*  979 */               value = item;
/*      */           }
/*      */         }
/*  982 */         if ((key == null) || (value == null))
/*  983 */           throw new IllegalStateException("Missing Item in Entry");
/*  984 */         String kid = key.getAttribute("id");
/*  985 */         String vid = value.getAttribute("id");
/*      */         
/*  987 */         Object k = value(obj, key);
/*  988 */         Object v = value(obj, value);
/*  989 */         map.put(k, v);
/*      */         
/*  991 */         if (kid != null)
/*  992 */           this._configuration.getIdMap().put(kid, k);
/*  993 */         if (vid != null) {
/*  994 */           this._configuration.getIdMap().put(vid, v);
/*      */         }
/*      */       }
/*  997 */       return map;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Object propertyObj(XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/* 1009 */       AttrOrElementNode aoeNode = new AttrOrElementNode(node, new String[] { "Id", "Name", "Deprecated", "Default" });
/* 1010 */       String id = aoeNode.getString("Id");
/* 1011 */       String name = aoeNode.getString("Name", true);
/* 1012 */       List<Object> deprecated = aoeNode.getList("Deprecated");
/* 1013 */       String dftValue = aoeNode.getString("Default");
/*      */       
/*      */ 
/* 1016 */       Map<String, String> properties = this._configuration.getProperties();
/* 1017 */       String value = (String)properties.get(name);
/*      */       
/*      */ 
/*      */ 
/* 1021 */       String alternate = null;
/* 1022 */       if (!deprecated.isEmpty())
/*      */       {
/* 1024 */         for (Object d : deprecated)
/*      */         {
/* 1026 */           String v = (String)properties.get(StringUtil.valueOf(d));
/* 1027 */           if (v != null)
/*      */           {
/* 1029 */             if (value == null) {
/* 1030 */               XmlConfiguration.LOG.warn("Property '{}' is deprecated, use '{}' instead", new Object[] { d, name });
/*      */             } else
/* 1032 */               XmlConfiguration.LOG.warn("Property '{}' is deprecated, value from '{}' used", new Object[] { d, name });
/*      */           }
/* 1034 */           if (alternate == null) {
/* 1035 */             alternate = v;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1040 */       if (value == null) {
/* 1041 */         value = alternate;
/*      */       }
/*      */       
/* 1044 */       if (value == null) {
/* 1045 */         value = dftValue;
/*      */       }
/*      */       
/* 1048 */       if (id != null)
/* 1049 */         this._configuration.getIdMap().put(id, value);
/* 1050 */       return value;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Object systemPropertyObj(XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/* 1062 */       AttrOrElementNode aoeNode = new AttrOrElementNode(node, new String[] { "Id", "Name", "Deprecated", "Default" });
/* 1063 */       String id = aoeNode.getString("Id");
/* 1064 */       String name = aoeNode.getString("Name", true);
/* 1065 */       List<Object> deprecated = aoeNode.getList("Deprecated");
/* 1066 */       String dftValue = aoeNode.getString("Default");
/*      */       
/*      */ 
/* 1069 */       String value = System.getProperty(name);
/*      */       
/*      */ 
/* 1072 */       String alternate = null;
/* 1073 */       if (!deprecated.isEmpty())
/*      */       {
/* 1075 */         for (Object d : deprecated)
/*      */         {
/* 1077 */           String v = System.getProperty(StringUtil.valueOf(d));
/* 1078 */           if (v != null)
/*      */           {
/* 1080 */             if (value == null) {
/* 1081 */               XmlConfiguration.LOG.warn("SystemProperty '{}' is deprecated, use '{}' instead", new Object[] { d, name });
/*      */             } else
/* 1083 */               XmlConfiguration.LOG.warn("SystemProperty '{}' is deprecated, value from '{}' used", new Object[] { d, name });
/*      */           }
/* 1085 */           if (alternate == null) {
/* 1086 */             alternate = v;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1091 */       if (value == null) {
/* 1092 */         value = alternate;
/*      */       }
/*      */       
/* 1095 */       if (value == null) {
/* 1096 */         value = dftValue;
/*      */       }
/*      */       
/* 1099 */       if (id != null) {
/* 1100 */         this._configuration.getIdMap().put(id, value);
/*      */       }
/* 1102 */       return value;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Object envObj(XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/* 1114 */       AttrOrElementNode aoeNode = new AttrOrElementNode(node, new String[] { "Id", "Name", "Deprecated", "Default" });
/* 1115 */       String id = aoeNode.getString("Id");
/* 1116 */       String name = aoeNode.getString("Name", true);
/* 1117 */       List<Object> deprecated = aoeNode.getList("Deprecated");
/* 1118 */       String dftValue = aoeNode.getString("Default");
/*      */       
/*      */ 
/* 1121 */       String value = System.getenv(name);
/*      */       
/*      */ 
/* 1124 */       if ((value == null) && (!deprecated.isEmpty()))
/*      */       {
/* 1126 */         for (Object d : deprecated)
/*      */         {
/* 1128 */           value = System.getenv(StringUtil.valueOf(d));
/* 1129 */           if (value != null)
/*      */           {
/* 1131 */             XmlConfiguration.LOG.warn("Property '{}' is deprecated, use '{}' instead", new Object[] { d, name });
/* 1132 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1138 */       if (value == null) {
/* 1139 */         value = dftValue;
/*      */       }
/*      */       
/* 1142 */       if (id != null) {
/* 1143 */         this._configuration.getIdMap().put(id, value);
/*      */       }
/* 1145 */       return value;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Object value(Object obj, XmlParser.Node node)
/*      */       throws Exception
/*      */     {
/* 1157 */       String type = node.getAttribute("type");
/*      */       
/*      */ 
/* 1160 */       String ref = node.getAttribute("ref");
/* 1161 */       Object value; StringBuilder buf; int i; Object value; if (ref != null)
/*      */       {
/* 1163 */         value = this._configuration.getIdMap().get(ref);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1168 */         if (node.size() == 0)
/*      */         {
/* 1170 */           if ("String".equals(type))
/* 1171 */             return "";
/* 1172 */           return null;
/*      */         }
/*      */         
/*      */ 
/* 1176 */         int first = 0;
/* 1177 */         int last = node.size() - 1;
/*      */         
/*      */ 
/* 1180 */         if ((type == null) || (!"String".equals(type)))
/*      */         {
/*      */ 
/*      */ 
/* 1184 */           while (first <= last)
/*      */           {
/* 1186 */             Object item = node.get(first);
/* 1187 */             if (!(item instanceof String))
/*      */               break;
/* 1189 */             item = ((String)item).trim();
/* 1190 */             if (((String)item).length() > 0)
/*      */               break;
/* 1192 */             first++;
/*      */           }
/*      */           
/*      */ 
/* 1196 */           while (first < last)
/*      */           {
/* 1198 */             Object item = node.get(last);
/* 1199 */             if (!(item instanceof String))
/*      */               break;
/* 1201 */             item = ((String)item).trim();
/* 1202 */             if (((String)item).length() > 0)
/*      */               break;
/* 1204 */             last--;
/*      */           }
/*      */           
/*      */ 
/* 1208 */           if (first > last)
/* 1209 */             return null;
/*      */         }
/*      */         Object value;
/* 1212 */         if (first == last)
/*      */         {
/* 1214 */           value = itemValue(obj, node.get(first));
/*      */         }
/*      */         else
/*      */         {
/* 1218 */           buf = new StringBuilder();
/* 1219 */           for (i = first; i <= last; i++)
/*      */           {
/* 1221 */             Object item = node.get(i);
/* 1222 */             buf.append(itemValue(obj, item));
/*      */           }
/* 1224 */           value = buf.toString();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1229 */       if (value == null)
/*      */       {
/* 1231 */         if ("String".equals(type))
/* 1232 */           return "";
/* 1233 */         return null;
/*      */       }
/*      */       
/*      */ 
/* 1237 */       if (type == null)
/*      */       {
/* 1239 */         if ((value instanceof String))
/* 1240 */           return ((String)value).trim();
/* 1241 */         return value;
/*      */       }
/*      */       
/* 1244 */       if (isTypeMatchingClass(type, String.class)) {
/* 1245 */         return value.toString();
/*      */       }
/* 1247 */       Class<?> pClass = TypeUtil.fromName(type);
/* 1248 */       if (pClass != null) {
/* 1249 */         return TypeUtil.valueOf(pClass, value.toString());
/*      */       }
/* 1251 */       if (isTypeMatchingClass(type, URL.class))
/*      */       {
/* 1253 */         if ((value instanceof URL)) {
/* 1254 */           return value;
/*      */         }
/*      */         try {
/* 1257 */           return new URL(value.toString());
/*      */         }
/*      */         catch (MalformedURLException e)
/*      */         {
/* 1261 */           throw new InvocationTargetException(e);
/*      */         }
/*      */       }
/*      */       
/* 1265 */       if (isTypeMatchingClass(type, InetAddress.class))
/*      */       {
/* 1267 */         if ((value instanceof InetAddress)) {
/* 1268 */           return value;
/*      */         }
/*      */         try {
/* 1271 */           return InetAddress.getByName(value.toString());
/*      */         }
/*      */         catch (UnknownHostException e)
/*      */         {
/* 1275 */           throw new InvocationTargetException(e);
/*      */         }
/*      */       }
/*      */       
/* 1279 */       for (Class<?> collectionClass : XmlConfiguration.__supportedCollections)
/*      */       {
/* 1281 */         if (isTypeMatchingClass(type, collectionClass)) {
/* 1282 */           return convertArrayToCollection(value, collectionClass);
/*      */         }
/*      */       }
/* 1285 */       throw new IllegalStateException("Unknown type " + type);
/*      */     }
/*      */     
/*      */     private static boolean isTypeMatchingClass(String type, Class<?> classToMatch)
/*      */     {
/* 1290 */       return (classToMatch.getSimpleName().equalsIgnoreCase(type)) || (classToMatch.getName().equals(type));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private Object itemValue(Object obj, Object item)
/*      */       throws Exception
/*      */     {
/* 1299 */       if ((item instanceof String)) {
/* 1300 */         return item;
/*      */       }
/* 1302 */       XmlParser.Node node = (XmlParser.Node)item;
/* 1303 */       String tag = node.getTag();
/* 1304 */       if ("Call".equals(tag))
/* 1305 */         return call(obj, node);
/* 1306 */       if ("Get".equals(tag))
/* 1307 */         return get(obj, node);
/* 1308 */       if ("New".equals(tag))
/* 1309 */         return newObj(obj, node);
/* 1310 */       if ("Ref".equals(tag))
/* 1311 */         return refObj(obj, node);
/* 1312 */       if ("Array".equals(tag))
/* 1313 */         return newArray(obj, node);
/* 1314 */       if ("Map".equals(tag))
/* 1315 */         return newMap(obj, node);
/* 1316 */       if ("Property".equals(tag))
/* 1317 */         return propertyObj(node);
/* 1318 */       if ("SystemProperty".equals(tag))
/* 1319 */         return systemPropertyObj(node);
/* 1320 */       if ("Env".equals(tag)) {
/* 1321 */         return envObj(node);
/*      */       }
/* 1323 */       XmlConfiguration.LOG.warn("Unknown value tag: " + node, new Throwable());
/* 1324 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */     private class AttrOrElementNode
/*      */     {
/*      */       final Object _obj;
/*      */       final XmlParser.Node _node;
/* 1332 */       final Set<String> _elements = new HashSet();
/*      */       final int _next;
/*      */       
/*      */       AttrOrElementNode(XmlParser.Node node, String... elements)
/*      */       {
/* 1337 */         this(null, node, elements);
/*      */       }
/*      */       
/*      */       AttrOrElementNode(Object obj, XmlParser.Node node, String... elements)
/*      */       {
/* 1342 */         this._obj = obj;
/* 1343 */         this._node = node;
/* 1344 */         for (String e : elements) {
/* 1345 */           this._elements.add(e);
/*      */         }
/* 1347 */         int next = 0;
/* 1348 */         for (Object o : this._node)
/*      */         {
/* 1350 */           if ((o instanceof String))
/*      */           {
/* 1352 */             if (((String)o).trim().length() != 0)
/*      */               break;
/* 1354 */             next++;
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 1360 */             if (!(o instanceof XmlParser.Node)) {
/*      */               break;
/*      */             }
/* 1363 */             XmlParser.Node n = (XmlParser.Node)o;
/* 1364 */             if (!this._elements.contains(n.getTag())) {
/*      */               break;
/*      */             }
/* 1367 */             next++;
/*      */           } }
/* 1369 */         this._next = next;
/*      */       }
/*      */       
/*      */       public int getNext()
/*      */       {
/* 1374 */         return this._next;
/*      */       }
/*      */       
/*      */       public String getString(String elementName) throws Exception
/*      */       {
/* 1379 */         return StringUtil.valueOf(get(elementName, false));
/*      */       }
/*      */       
/*      */       public String getString(String elementName, boolean manditory) throws Exception
/*      */       {
/* 1384 */         return StringUtil.valueOf(get(elementName, manditory));
/*      */       }
/*      */       
/*      */       public Object get(String elementName, boolean manditory) throws Exception
/*      */       {
/* 1389 */         String attrName = StringUtil.asciiToLowerCase(elementName);
/* 1390 */         String attr = this._node.getAttribute(attrName);
/* 1391 */         Object value = attr;
/*      */         
/* 1393 */         for (int i = 0; i < this._next; i++)
/*      */         {
/* 1395 */           Object o = this._node.get(i);
/* 1396 */           if ((o instanceof XmlParser.Node))
/*      */           {
/* 1398 */             XmlParser.Node n = (XmlParser.Node)o;
/* 1399 */             if (elementName.equals(n.getTag()))
/*      */             {
/* 1401 */               if (attr != null) {
/* 1402 */                 throw new IllegalStateException("Cannot have attr '" + attrName + "' and element '" + elementName + "'");
/*      */               }
/* 1404 */               value = XmlConfiguration.JettyXmlConfiguration.this.value(this._obj, n);
/* 1405 */               break;
/*      */             }
/*      */           }
/*      */         }
/* 1409 */         if ((manditory) && (value == null)) {
/* 1410 */           throw new IllegalStateException("Must have attr '" + attrName + "' or element '" + elementName + "'");
/*      */         }
/* 1412 */         return value;
/*      */       }
/*      */       
/*      */       public List<Object> getList(String elementName) throws Exception
/*      */       {
/* 1417 */         return getList(elementName, false);
/*      */       }
/*      */       
/*      */       public List<Object> getList(String elementName, boolean manditory) throws Exception
/*      */       {
/* 1422 */         String attrName = StringUtil.asciiToLowerCase(elementName);
/* 1423 */         List<Object> values = new ArrayList();
/*      */         
/* 1425 */         String attr = this._node.getAttribute(attrName);
/* 1426 */         if (attr != null) {
/* 1427 */           values.addAll(StringUtil.csvSplit(null, attr, 0, attr.length()));
/*      */         }
/*      */         
/* 1430 */         for (int i = 0; i < this._next; i++)
/*      */         {
/* 1432 */           Object o = this._node.get(i);
/* 1433 */           if ((o instanceof XmlParser.Node))
/*      */           {
/* 1435 */             XmlParser.Node n = (XmlParser.Node)o;
/*      */             
/* 1437 */             if (elementName.equals(n.getTag()))
/*      */             {
/* 1439 */               if (attr != null) {
/* 1440 */                 throw new IllegalStateException("Cannot have attr '" + attrName + "' and element '" + elementName + "'");
/*      */               }
/* 1442 */               values.add(XmlConfiguration.JettyXmlConfiguration.this.value(this._obj, n));
/*      */             }
/*      */           }
/*      */         }
/* 1446 */         if ((manditory) && (values.isEmpty())) {
/* 1447 */           throw new IllegalStateException("Must have attr '" + attrName + "' or element '" + elementName + "'");
/*      */         }
/* 1449 */         return values;
/*      */       }
/*      */       
/*      */       public List<XmlParser.Node> getNodes(String elementName) throws Exception
/*      */       {
/* 1454 */         String attrName = StringUtil.asciiToLowerCase(elementName);
/* 1455 */         List<XmlParser.Node> values = new ArrayList();
/*      */         
/* 1457 */         String attr = this._node.getAttribute(attrName);
/* 1458 */         if (attr != null)
/*      */         {
/* 1460 */           for (String a : StringUtil.csvSplit(null, attr, 0, attr.length()))
/*      */           {
/*      */ 
/* 1463 */             XmlParser.Node n = new XmlParser.Node(null, elementName, null);
/* 1464 */             n.add(a);
/* 1465 */             values.add(n);
/*      */           }
/*      */         }
/*      */         
/* 1469 */         for (int i = 0; i < this._next; i++)
/*      */         {
/* 1471 */           Object o = this._node.get(i);
/* 1472 */           if ((o instanceof XmlParser.Node))
/*      */           {
/* 1474 */             XmlParser.Node n = (XmlParser.Node)o;
/*      */             
/* 1476 */             if (elementName.equals(n.getTag()))
/*      */             {
/* 1478 */               if (attr != null) {
/* 1479 */                 throw new IllegalStateException("Cannot have attr '" + attrName + "' and element '" + elementName + "'");
/*      */               }
/* 1481 */               values.add(n);
/*      */             }
/*      */           }
/*      */         }
/* 1485 */         return values;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String... args)
/*      */     throws Exception
/*      */   {
/*      */     try
/*      */     {
/* 1511 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public Void run()
/*      */           throws Exception
/*      */         {
/* 1516 */           Properties properties = null;
/*      */           
/*      */ 
/*      */           try
/*      */           {
/* 1521 */             Class<?> config = XmlConfiguration.class.getClassLoader().loadClass("org.eclipse.jetty.start.Config");
/* 1522 */             properties = (Properties)config.getMethod("getProperties", new Class[0]).invoke(null, new Object[0]);
/* 1523 */             XmlConfiguration.LOG.debug("org.eclipse.jetty.start.Config properties = {}", new Object[] { properties });
/*      */           }
/*      */           catch (NoClassDefFoundError|ClassNotFoundException e)
/*      */           {
/* 1527 */             XmlConfiguration.LOG.ignore(e);
/*      */           }
/*      */           catch (Exception e)
/*      */           {
/* 1531 */             XmlConfiguration.LOG.warn(e);
/*      */           }
/*      */           
/*      */ 
/* 1535 */           if (properties == null)
/*      */           {
/*      */ 
/* 1538 */             properties = new Properties();
/* 1539 */             properties.putAll(System.getProperties());
/*      */           }
/*      */           
/*      */ 
/* 1543 */           for (String arg : XmlConfiguration.this)
/*      */           {
/* 1545 */             if (arg.indexOf('=') >= 0)
/*      */             {
/* 1547 */               int i = arg.indexOf('=');
/* 1548 */               properties.put(arg.substring(0, i), arg.substring(i + 1));
/*      */             }
/* 1550 */             else if (arg.toLowerCase(Locale.ENGLISH).endsWith(".properties")) {
/* 1551 */               properties.load(Resource.newResource(arg).getInputStream());
/*      */             }
/*      */           }
/*      */           
/* 1555 */           XmlConfiguration last = null;
/* 1556 */           Object objects = new ArrayList(XmlConfiguration.this.length);
/* 1557 */           for (int i = 0; i < XmlConfiguration.this.length; i++)
/*      */           {
/* 1559 */             if ((!XmlConfiguration.this[i].toLowerCase(Locale.ENGLISH).endsWith(".properties")) && (XmlConfiguration.this[i].indexOf('=') < 0))
/*      */             {
/* 1561 */               XmlConfiguration configuration = new XmlConfiguration(Resource.newResource(XmlConfiguration.this[i]).getURI().toURL());
/* 1562 */               if (last != null)
/* 1563 */                 configuration.getIdMap().putAll(last.getIdMap());
/* 1564 */               if (properties.size() > 0)
/*      */               {
/* 1566 */                 Map<String, String> props = new HashMap();
/* 1567 */                 for (Object key : properties.keySet())
/*      */                 {
/* 1569 */                   props.put(key.toString(), String.valueOf(properties.get(key)));
/*      */                 }
/* 1571 */                 configuration.getProperties().putAll(props);
/*      */               }
/*      */               
/* 1574 */               Object obj = configuration.configure();
/* 1575 */               if ((obj != null) && (!((List)objects).contains(obj)))
/* 1576 */                 ((List)objects).add(obj);
/* 1577 */               last = configuration;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1582 */           for (Object obj : (List)objects)
/*      */           {
/* 1584 */             if ((obj instanceof LifeCycle))
/*      */             {
/* 1586 */               LifeCycle lc = (LifeCycle)obj;
/* 1587 */               if (!lc.isRunning()) {
/* 1588 */                 lc.start();
/*      */               }
/*      */             }
/*      */           }
/* 1592 */           return null;
/*      */         }
/*      */       });
/*      */     }
/*      */     catch (Error|Exception e)
/*      */     {
/* 1598 */       LOG.warn(e);
/* 1599 */       throw e;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\xml\XmlConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */