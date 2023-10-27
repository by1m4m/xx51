/*     */ package org.eclipse.jetty.xml;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Stack;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.eclipse.jetty.util.LazyList;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.resource.Resource;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlParser
/*     */ {
/*  59 */   private static final Logger LOG = Log.getLogger(XmlParser.class);
/*     */   
/*  61 */   private Map<String, URL> _redirectMap = new HashMap();
/*     */   private SAXParser _parser;
/*     */   private Map<String, ContentHandler> _observerMap;
/*  64 */   private Stack<ContentHandler> _observers = new Stack();
/*     */   
/*     */   private String _xpath;
/*     */   
/*     */   private Object _xpaths;
/*     */   
/*     */   private String _dtd;
/*     */   
/*     */ 
/*     */   public XmlParser()
/*     */   {
/*  75 */     SAXParserFactory factory = SAXParserFactory.newInstance();
/*  76 */     boolean validating_dft = factory.getClass().toString().startsWith("org.apache.xerces.");
/*  77 */     String validating_prop = System.getProperty("org.eclipse.jetty.xml.XmlParser.Validating", validating_dft ? "true" : "false");
/*  78 */     boolean validating = Boolean.valueOf(validating_prop).booleanValue();
/*  79 */     setValidating(validating);
/*     */   }
/*     */   
/*     */ 
/*     */   public XmlParser(boolean validating)
/*     */   {
/*  85 */     setValidating(validating);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setValidating(boolean validating)
/*     */   {
/*     */     try
/*     */     {
/*  93 */       SAXParserFactory factory = SAXParserFactory.newInstance();
/*  94 */       factory.setValidating(validating);
/*  95 */       this._parser = factory.newSAXParser();
/*     */       
/*     */       try
/*     */       {
/*  99 */         if (validating) {
/* 100 */           this._parser.getXMLReader().setFeature("http://apache.org/xml/features/validation/schema", validating);
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 104 */         if (validating) {
/* 105 */           LOG.warn("Schema validation may not be supported: ", e);
/*     */         } else {
/* 107 */           LOG.ignore(e);
/*     */         }
/*     */       }
/* 110 */       this._parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", validating);
/* 111 */       this._parser.getXMLReader().setFeature("http://xml.org/sax/features/namespaces", true);
/* 112 */       this._parser.getXMLReader().setFeature("http://xml.org/sax/features/namespace-prefixes", false);
/*     */       try
/*     */       {
/* 115 */         if (validating) {
/* 116 */           this._parser.getXMLReader().setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", validating);
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 120 */         LOG.warn(e.getMessage(), new Object[0]);
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 125 */       LOG.warn("EXCEPTION ", e);
/* 126 */       throw new Error(e.toString());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isValidating()
/*     */   {
/* 133 */     return this._parser.isValidating();
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void redirectEntity(String name, URL entity)
/*     */   {
/* 139 */     if (entity != null) {
/* 140 */       this._redirectMap.put(name, entity);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getXpath()
/*     */   {
/* 150 */     return this._xpath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setXpath(String xpath)
/*     */   {
/* 162 */     this._xpath = xpath;
/* 163 */     StringTokenizer tok = new StringTokenizer(xpath, "| ");
/* 164 */     while (tok.hasMoreTokens()) {
/* 165 */       this._xpaths = LazyList.add(this._xpaths, tok.nextToken());
/*     */     }
/*     */   }
/*     */   
/*     */   public String getDTD()
/*     */   {
/* 171 */     return this._dtd;
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
/*     */   public synchronized void addContentHandler(String trigger, ContentHandler observer)
/*     */   {
/* 185 */     if (this._observerMap == null)
/* 186 */       this._observerMap = new HashMap();
/* 187 */     this._observerMap.put(trigger, observer);
/*     */   }
/*     */   
/*     */   public synchronized Node parse(InputSource source)
/*     */     throws IOException, SAXException
/*     */   {
/* 193 */     this._dtd = null;
/* 194 */     Handler handler = new Handler();
/* 195 */     XMLReader reader = this._parser.getXMLReader();
/* 196 */     reader.setContentHandler(handler);
/* 197 */     reader.setErrorHandler(handler);
/* 198 */     reader.setEntityResolver(handler);
/* 199 */     if (LOG.isDebugEnabled())
/* 200 */       LOG.debug("parsing: sid=" + source.getSystemId() + ",pid=" + source.getPublicId(), new Object[0]);
/* 201 */     this._parser.parse(source, handler);
/* 202 */     if (handler._error != null)
/* 203 */       throw handler._error;
/* 204 */     Node doc = (Node)handler._top.get(0);
/* 205 */     handler.clear();
/* 206 */     return doc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Node parse(String url)
/*     */     throws IOException, SAXException
/*     */   {
/* 219 */     if (LOG.isDebugEnabled())
/* 220 */       LOG.debug("parse: " + url, new Object[0]);
/* 221 */     return parse(new InputSource(url));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Node parse(File file)
/*     */     throws IOException, SAXException
/*     */   {
/* 234 */     if (LOG.isDebugEnabled())
/* 235 */       LOG.debug("parse: " + file, new Object[0]);
/* 236 */     return parse(new InputSource(Resource.toURL(file).toString()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Node parse(InputStream in)
/*     */     throws IOException, SAXException
/*     */   {
/* 249 */     this._dtd = null;
/* 250 */     Handler handler = new Handler();
/* 251 */     XMLReader reader = this._parser.getXMLReader();
/* 252 */     reader.setContentHandler(handler);
/* 253 */     reader.setErrorHandler(handler);
/* 254 */     reader.setEntityResolver(handler);
/* 255 */     this._parser.parse(new InputSource(in), handler);
/* 256 */     if (handler._error != null)
/* 257 */       throw handler._error;
/* 258 */     Node doc = (Node)handler._top.get(0);
/* 259 */     handler.clear();
/* 260 */     return doc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected InputSource resolveEntity(String pid, String sid)
/*     */   {
/* 267 */     if (LOG.isDebugEnabled()) {
/* 268 */       LOG.debug("resolveEntity(" + pid + ", " + sid + ")", new Object[0]);
/*     */     }
/* 270 */     if ((sid != null) && (sid.endsWith(".dtd"))) {
/* 271 */       this._dtd = sid;
/*     */     }
/* 273 */     URL entity = null;
/* 274 */     if (pid != null)
/* 275 */       entity = (URL)this._redirectMap.get(pid);
/* 276 */     if (entity == null)
/* 277 */       entity = (URL)this._redirectMap.get(sid);
/* 278 */     if (entity == null)
/*     */     {
/* 280 */       String dtd = sid;
/* 281 */       if (dtd.lastIndexOf('/') >= 0) {
/* 282 */         dtd = dtd.substring(dtd.lastIndexOf('/') + 1);
/*     */       }
/* 284 */       if (LOG.isDebugEnabled())
/* 285 */         LOG.debug("Can't exact match entity in redirect map, trying " + dtd, new Object[0]);
/* 286 */       entity = (URL)this._redirectMap.get(dtd);
/*     */     }
/*     */     
/* 289 */     if (entity != null)
/*     */     {
/*     */       try
/*     */       {
/* 293 */         InputStream in = entity.openStream();
/* 294 */         if (LOG.isDebugEnabled())
/* 295 */           LOG.debug("Redirected entity " + sid + " --> " + entity, new Object[0]);
/* 296 */         InputSource is = new InputSource(in);
/* 297 */         is.setSystemId(sid);
/* 298 */         return is;
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 302 */         LOG.ignore(e);
/*     */       }
/*     */     }
/* 305 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private class NoopHandler
/*     */     extends DefaultHandler
/*     */   {
/*     */     XmlParser.Handler _next;
/*     */     int _depth;
/*     */     
/*     */     NoopHandler(XmlParser.Handler next)
/*     */     {
/* 317 */       this._next = next;
/*     */     }
/*     */     
/*     */ 
/*     */     public void startElement(String uri, String localName, String qName, Attributes attrs)
/*     */       throws SAXException
/*     */     {
/* 324 */       this._depth += 1;
/*     */     }
/*     */     
/*     */ 
/*     */     public void endElement(String uri, String localName, String qName)
/*     */       throws SAXException
/*     */     {
/* 331 */       if (this._depth == 0) {
/* 332 */         XmlParser.this._parser.getXMLReader().setContentHandler(this._next);
/*     */       } else {
/* 334 */         this._depth -= 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class Handler
/*     */     extends DefaultHandler
/*     */   {
/* 342 */     XmlParser.Node _top = new XmlParser.Node(null, null, null);
/*     */     SAXParseException _error;
/* 344 */     private XmlParser.Node _context = this._top;
/*     */     private XmlParser.NoopHandler _noop;
/*     */     
/*     */     Handler()
/*     */     {
/* 349 */       this._noop = new XmlParser.NoopHandler(XmlParser.this, this);
/*     */     }
/*     */     
/*     */ 
/*     */     void clear()
/*     */     {
/* 355 */       this._top = null;
/* 356 */       this._error = null;
/* 357 */       this._context = null;
/*     */     }
/*     */     
/*     */ 
/*     */     public void startElement(String uri, String localName, String qName, Attributes attrs)
/*     */       throws SAXException
/*     */     {
/* 364 */       String name = null;
/* 365 */       if (XmlParser.this._parser.isNamespaceAware()) {
/* 366 */         name = localName;
/*     */       }
/* 368 */       if ((name == null) || ("".equals(name))) {
/* 369 */         name = qName;
/*     */       }
/* 371 */       XmlParser.Node node = new XmlParser.Node(this._context, name, attrs);
/*     */       
/*     */ 
/*     */ 
/* 375 */       if (XmlParser.this._xpaths != null)
/*     */       {
/* 377 */         String path = node.getPath();
/* 378 */         boolean match = false;
/* 379 */         for (int i = LazyList.size(XmlParser.this._xpaths); (!match) && (i-- > 0);)
/*     */         {
/* 381 */           String xpath = (String)LazyList.get(XmlParser.this._xpaths, i);
/*     */           
/* 383 */           match = (path.equals(xpath)) || ((xpath.startsWith(path)) && (xpath.length() > path.length()) && (xpath.charAt(path.length()) == '/'));
/*     */         }
/*     */         
/* 386 */         if (match)
/*     */         {
/* 388 */           this._context.add(node);
/* 389 */           this._context = node;
/*     */         }
/*     */         else
/*     */         {
/* 393 */           XmlParser.this._parser.getXMLReader().setContentHandler(this._noop);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 398 */         this._context.add(node);
/* 399 */         this._context = node;
/*     */       }
/*     */       
/* 402 */       ContentHandler observer = null;
/* 403 */       if (XmlParser.this._observerMap != null)
/* 404 */         observer = (ContentHandler)XmlParser.this._observerMap.get(name);
/* 405 */       XmlParser.this._observers.push(observer);
/*     */       
/* 407 */       for (int i = 0; i < XmlParser.this._observers.size(); i++) {
/* 408 */         if (XmlParser.this._observers.get(i) != null) {
/* 409 */           ((ContentHandler)XmlParser.this._observers.get(i)).startElement(uri, localName, qName, attrs);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void endElement(String uri, String localName, String qName) throws SAXException
/*     */     {
/* 416 */       this._context = this._context._parent;
/* 417 */       for (int i = 0; i < XmlParser.this._observers.size(); i++)
/* 418 */         if (XmlParser.this._observers.get(i) != null)
/* 419 */           ((ContentHandler)XmlParser.this._observers.get(i)).endElement(uri, localName, qName);
/* 420 */       XmlParser.this._observers.pop();
/*     */     }
/*     */     
/*     */ 
/*     */     public void ignorableWhitespace(char[] buf, int offset, int len)
/*     */       throws SAXException
/*     */     {
/* 427 */       for (int i = 0; i < XmlParser.this._observers.size(); i++) {
/* 428 */         if (XmlParser.this._observers.get(i) != null) {
/* 429 */           ((ContentHandler)XmlParser.this._observers.get(i)).ignorableWhitespace(buf, offset, len);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void characters(char[] buf, int offset, int len) throws SAXException
/*     */     {
/* 436 */       this._context.add(new String(buf, offset, len));
/* 437 */       for (int i = 0; i < XmlParser.this._observers.size(); i++) {
/* 438 */         if (XmlParser.this._observers.get(i) != null) {
/* 439 */           ((ContentHandler)XmlParser.this._observers.get(i)).characters(buf, offset, len);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void warning(SAXParseException ex)
/*     */     {
/* 446 */       XmlParser.LOG.debug("EXCEPTION ", ex);
/* 447 */       XmlParser.LOG.warn("WARNING@" + getLocationString(ex) + " : " + ex.toString(), new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void error(SAXParseException ex)
/*     */       throws SAXException
/*     */     {
/* 455 */       if (this._error == null)
/* 456 */         this._error = ex;
/* 457 */       XmlParser.LOG.debug("EXCEPTION ", ex);
/* 458 */       XmlParser.LOG.warn("ERROR@" + getLocationString(ex) + " : " + ex.toString(), new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*     */     public void fatalError(SAXParseException ex)
/*     */       throws SAXException
/*     */     {
/* 465 */       this._error = ex;
/* 466 */       XmlParser.LOG.debug("EXCEPTION ", ex);
/* 467 */       XmlParser.LOG.warn("FATAL@" + getLocationString(ex) + " : " + ex.toString(), new Object[0]);
/* 468 */       throw ex;
/*     */     }
/*     */     
/*     */ 
/*     */     private String getLocationString(SAXParseException ex)
/*     */     {
/* 474 */       return ex.getSystemId() + " line:" + ex.getLineNumber() + " col:" + ex.getColumnNumber();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public InputSource resolveEntity(String pid, String sid)
/*     */     {
/* 481 */       return XmlParser.this.resolveEntity(pid, sid);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class Attribute
/*     */   {
/*     */     private String _name;
/*     */     
/*     */ 
/*     */     private String _value;
/*     */     
/*     */ 
/*     */     Attribute(String n, String v)
/*     */     {
/* 497 */       this._name = n;
/* 498 */       this._value = v;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 503 */       return this._name;
/*     */     }
/*     */     
/*     */     public String getValue()
/*     */     {
/* 508 */       return this._value;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Node
/*     */     extends AbstractList<Object>
/*     */   {
/*     */     Node _parent;
/*     */     
/*     */     private ArrayList<Object> _list;
/*     */     
/*     */     private String _tag;
/*     */     
/*     */     private XmlParser.Attribute[] _attrs;
/* 523 */     private boolean _lastString = false;
/*     */     
/*     */     private String _path;
/*     */     
/*     */     Node(Node parent, String tag, Attributes attrs)
/*     */     {
/* 529 */       this._parent = parent;
/* 530 */       this._tag = tag;
/*     */       
/* 532 */       if (attrs != null)
/*     */       {
/* 534 */         this._attrs = new XmlParser.Attribute[attrs.getLength()];
/* 535 */         for (int i = 0; i < attrs.getLength(); i++)
/*     */         {
/* 537 */           String name = attrs.getLocalName(i);
/* 538 */           if ((name == null) || (name.equals("")))
/* 539 */             name = attrs.getQName(i);
/* 540 */           this._attrs[i] = new XmlParser.Attribute(name, attrs.getValue(i));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public Node getParent()
/*     */     {
/* 548 */       return this._parent;
/*     */     }
/*     */     
/*     */ 
/*     */     public String getTag()
/*     */     {
/* 554 */       return this._tag;
/*     */     }
/*     */     
/*     */ 
/*     */     public String getPath()
/*     */     {
/* 560 */       if (this._path == null)
/*     */       {
/* 562 */         if ((getParent() != null) && (getParent().getTag() != null)) {
/* 563 */           this._path = (getParent().getPath() + "/" + this._tag);
/*     */         } else
/* 565 */           this._path = ("/" + this._tag);
/*     */       }
/* 567 */       return this._path;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public XmlParser.Attribute[] getAttributes()
/*     */     {
/* 577 */       return this._attrs;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getAttribute(String name)
/*     */     {
/* 589 */       return getAttribute(name, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getAttribute(String name, String dft)
/*     */     {
/* 602 */       if ((this._attrs == null) || (name == null))
/* 603 */         return dft;
/* 604 */       for (int i = 0; i < this._attrs.length; i++)
/* 605 */         if (name.equals(this._attrs[i].getName()))
/* 606 */           return this._attrs[i].getValue();
/* 607 */       return dft;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int size()
/*     */     {
/* 617 */       if (this._list != null)
/* 618 */         return this._list.size();
/* 619 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Object get(int i)
/*     */     {
/* 631 */       if (this._list != null)
/* 632 */         return this._list.get(i);
/* 633 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Node get(String tag)
/*     */     {
/* 645 */       if (this._list != null)
/*     */       {
/* 647 */         for (int i = 0; i < this._list.size(); i++)
/*     */         {
/* 649 */           Object o = this._list.get(i);
/* 650 */           if ((o instanceof Node))
/*     */           {
/* 652 */             Node n = (Node)o;
/* 653 */             if (tag.equals(n._tag))
/* 654 */               return n;
/*     */           }
/*     */         }
/*     */       }
/* 658 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void add(int i, Object o)
/*     */     {
/* 665 */       if (this._list == null)
/* 666 */         this._list = new ArrayList();
/* 667 */       if ((o instanceof String))
/*     */       {
/* 669 */         if (this._lastString)
/*     */         {
/* 671 */           int last = this._list.size() - 1;
/* 672 */           this._list.set(last, (String)this._list.get(last) + o);
/*     */         }
/*     */         else {
/* 675 */           this._list.add(i, o); }
/* 676 */         this._lastString = true;
/*     */       }
/*     */       else
/*     */       {
/* 680 */         this._lastString = false;
/* 681 */         this._list.add(i, o);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void clear()
/*     */     {
/* 689 */       if (this._list != null)
/* 690 */         this._list.clear();
/* 691 */       this._list = null;
/*     */     }
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
/*     */     public String getString(String tag, boolean tags, boolean trim)
/*     */     {
/* 705 */       Node node = get(tag);
/* 706 */       if (node == null)
/* 707 */         return null;
/* 708 */       String s = node.toString(tags);
/* 709 */       if ((s != null) && (trim))
/* 710 */         s = s.trim();
/* 711 */       return s;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public synchronized String toString()
/*     */     {
/* 718 */       return toString(true);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public synchronized String toString(boolean tag)
/*     */     {
/* 730 */       StringBuilder buf = new StringBuilder();
/* 731 */       toString(buf, tag);
/* 732 */       return buf.toString();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public synchronized String toString(boolean tag, boolean trim)
/*     */     {
/* 745 */       String s = toString(tag);
/* 746 */       if ((s != null) && (trim))
/* 747 */         s = s.trim();
/* 748 */       return s;
/*     */     }
/*     */     
/*     */ 
/*     */     private synchronized void toString(StringBuilder buf, boolean tag)
/*     */     {
/* 754 */       if (tag)
/*     */       {
/* 756 */         buf.append("<");
/* 757 */         buf.append(this._tag);
/*     */         
/* 759 */         if (this._attrs != null)
/*     */         {
/* 761 */           for (int i = 0; i < this._attrs.length; i++)
/*     */           {
/* 763 */             buf.append(' ');
/* 764 */             buf.append(this._attrs[i].getName());
/* 765 */             buf.append("=\"");
/* 766 */             buf.append(this._attrs[i].getValue());
/* 767 */             buf.append("\"");
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 772 */       if (this._list != null)
/*     */       {
/* 774 */         if (tag)
/* 775 */           buf.append(">");
/* 776 */         for (int i = 0; i < this._list.size(); i++)
/*     */         {
/* 778 */           Object o = this._list.get(i);
/* 779 */           if (o != null)
/*     */           {
/* 781 */             if ((o instanceof Node)) {
/* 782 */               ((Node)o).toString(buf, tag);
/*     */             } else
/* 784 */               buf.append(o.toString()); }
/*     */         }
/* 786 */         if (tag)
/*     */         {
/* 788 */           buf.append("</");
/* 789 */           buf.append(this._tag);
/* 790 */           buf.append(">");
/*     */         }
/*     */       }
/* 793 */       else if (tag) {
/* 794 */         buf.append("/>");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Iterator<Node> iterator(final String tag)
/*     */     {
/* 806 */       new Iterator()
/*     */       {
/* 808 */         int c = 0;
/*     */         
/*     */         XmlParser.Node _node;
/*     */         
/*     */ 
/*     */         public boolean hasNext()
/*     */         {
/* 815 */           if (this._node != null)
/* 816 */             return true;
/* 817 */           while ((XmlParser.Node.this._list != null) && (this.c < XmlParser.Node.this._list.size()))
/*     */           {
/* 819 */             Object o = XmlParser.Node.this._list.get(this.c);
/* 820 */             if ((o instanceof XmlParser.Node))
/*     */             {
/* 822 */               XmlParser.Node n = (XmlParser.Node)o;
/* 823 */               if (tag.equals(n._tag))
/*     */               {
/* 825 */                 this._node = n;
/* 826 */                 return true;
/*     */               }
/*     */             }
/* 829 */             this.c += 1;
/*     */           }
/* 831 */           return false;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */         public XmlParser.Node next()
/*     */         {
/*     */           try
/*     */           {
/* 840 */             if (hasNext())
/* 841 */               return this._node;
/* 842 */             throw new NoSuchElementException();
/*     */           }
/*     */           finally
/*     */           {
/* 846 */             this._node = null;
/* 847 */             this.c += 1;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */         public void remove()
/*     */         {
/* 855 */           throw new UnsupportedOperationException("Not supported");
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\xml\XmlParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */