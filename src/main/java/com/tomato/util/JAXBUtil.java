/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.tomato.util;

import com.sun.xml.bind.api.JAXBRIContext;
import com.sun.xml.bind.api.TypeReference;
import com.sun.xml.bind.v2.Messages;
import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.util.TypeCast;
import org.w3c.dom.Document;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/**
 * @since 2017年6月21日 下午7:32:12
 * @version $Id: JAXBUtil.java 19848 2016-08-01 08:00:42Z CaiBo $
 * @author CaiBo
 *
 */
public final class JAXBUtil {

	private JAXBUtil() {
		super();
	}

	private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();

	private static final Map<String, Object> properties = new HashMap<String, Object>();
	private static final ThreadLocal<Map<String, Marshaller>> MARSHALLER_HOLDER = new ThreadLocal<Map<String, Marshaller>>() {
		@Override
		protected Map<String, Marshaller> initialValue() {
			return new HashMap<String, Marshaller>(1024);
		}
	};
	private static final ThreadLocal<Map<String, Unmarshaller>> UNMARSHALLER_HOLDER = new ThreadLocal<Map<String, Unmarshaller>>() {
		@Override
		protected Map<String, Unmarshaller> initialValue() {
			return new HashMap<String, Unmarshaller>(1024);
		}
	};

	public static String generateSchema(Class<?>... classes) {
		final List<DOMResult> results = new ArrayList<DOMResult>();
		try {
			getContext(classes).generateSchema(new SchemaOutputResolver() {
				@Override
				public Result createOutput(String namespaceUri, String suggestedFileName) {
					DOMResult result = new DOMResult();
					result.setSystemId(suggestedFileName);
					results.add(result);
					return result;
				}
			});
		} catch (IOException | JAXBException e) {
			throw new RuntimeException("generate schema error", e);
		}
		DOMResult domResult = results.get(0);
		Document doc = (Document) domResult.getNode();
		Transformer transformer = null;
		StringWriter writer = new StringWriter(256);
		try {
			transformer = TRANSFORMER_FACTORY.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
		} catch (TransformerException e) {
			throw new RuntimeException("transform document error", e);
		}
		return writer.toString();
	}

	/**
	 * 
	 * @param bean
	 * @param name
	 * @param format
	 * @return
	 */
	public static String toXML(Object bean, QName name, boolean format) {
		return toXML(bean, name, format, true);
	}

	/**
	 * 
	 * @param bean
	 * @param name
	 * @param format
	 * @param discardType
	 * @return
	 */
	public static String toXML(Object bean, QName name, boolean format, boolean discardType) {
		StringWriter sw = new StringWriter();
		Class<? extends Object> type = bean.getClass();
		@SuppressWarnings({ "rawtypes", "unchecked" })
		JAXBElement root = new JAXBElement(name, discardType ? Object.class : type, bean);
		Marshaller m = getMarshaller(type);
		try {
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
			m.marshal(root, sw);
		} catch (JAXBException e) {
			throw new RuntimeException("marshal error", e);
		}
		return sw.toString();
	}

	/**
	 * 
	 * @param bean
	 * @param qname
	 * @return
	 */
	public static String toXML(Object bean, QName qname) {
		return toXML(bean, qname, false);
	}

	/**
	 * 
	 * @param returnType
	 * @param xml
	 * @return
	 */
	public static <T> T toBean(Class<T> returnType, String xml) {
		StreamSource source = new StreamSource(new StringReader(xml));
		try {
			JAXBElement<T> res = getUnmarshaller(returnType).unmarshal(source, returnType);
			return res.getValue();
		} catch (JAXBException e) {
			throw new RuntimeException("unmarshal error", e);
		}
	}

	private static Unmarshaller getUnmarshaller(Class<?>... classes) {
		String key = getKey(classes);
		Unmarshaller unmarshaller = UNMARSHALLER_HOLDER.get().get(key);
		if (unmarshaller == null) {
			try {
				unmarshaller = getContext(classes).createUnmarshaller();
			} catch (JAXBException e) {
				throw new RuntimeException("create unmarshaller error", e);
			}
			UNMARSHALLER_HOLDER.get().put(key, unmarshaller);
		}
		return unmarshaller;
	}

	private static Marshaller getMarshaller(Class<?>... classes) {
		String key = getKey(classes);
		Marshaller marshaller = MARSHALLER_HOLDER.get().get(key);
		if (marshaller == null) {
			try {
				marshaller = getContext(classes).createMarshaller();
			} catch (JAXBException e) {
				throw new RuntimeException("create marshaller error", e);
			}
			MARSHALLER_HOLDER.get().put(key, marshaller);
		}
		return marshaller;
	}

	private static JAXBContext getContext(Class<?>... classes) throws JAXBException {
		return JaxbContextFactory.createContext(properties, classes);
	}

	static final class JaxbContextFactory {

		private static final Map<String, JAXBContext> contextCache = new HashMap<String, JAXBContext>();

		@SuppressWarnings("rawtypes")
		public static JAXBContext createContext(Map<String, Object> properties, Class<?>... classes) throws JAXBException {
			// 在多Class情况下应自行注意线程锁
			synchronized (classes[0]) {
				JAXBContext result;
				result = get(classes);
				if (result != null) {
					return result;
				}
				// fool-proof check, and copy the map to make it easier to find unrecognized
				// properties.
				if (properties == null) {
					properties = Collections.emptyMap();
				} else {
					properties = new HashMap<String, Object>(properties);
				}
				String defaultNsUri = getPropertyValue(properties, JAXBRIContext.DEFAULT_NAMESPACE_REMAP, String.class);
				Boolean c14nSupport = getPropertyValue(properties, JAXBRIContext.CANONICALIZATION_SUPPORT, Boolean.class, false);
				Boolean disableSecurityProcessing = getPropertyValue(properties, JAXBRIContext.DISABLE_XML_SECURITY, Boolean.class, false);
				if (disableSecurityProcessing == null) {
					disableSecurityProcessing = false;
				}
				Boolean allNillable = getPropertyValue(properties, JAXBRIContext.TREAT_EVERYTHING_NILLABLE, Boolean.class, false);
				Boolean retainPropertyInfo = getPropertyValue(properties, JAXBRIContext.RETAIN_REFERENCE_TO_INFO, Boolean.class, false);
				Boolean supressAccessorWarnings = getPropertyValue(properties, JAXBRIContext.SUPRESS_ACCESSOR_WARNINGS, Boolean.class, false);
				Boolean improvedXsiTypeHandling = getPropertyValue(properties, JAXBRIContext.IMPROVED_XSI_TYPE_HANDLING, Boolean.class);
				if (improvedXsiTypeHandling == null) {
					String improvedXsiSystemProperty = System.getProperty(JAXBRIContext.IMPROVED_XSI_TYPE_HANDLING);
					if (improvedXsiSystemProperty == null) {
						improvedXsiTypeHandling = true;
					} else {
						improvedXsiTypeHandling = Boolean.valueOf(improvedXsiSystemProperty);
					}
				}
				Boolean xmlAccessorFactorySupport = getPropertyValue(properties, JAXBRIContext.XMLACCESSORFACTORY_SUPPORT, Boolean.class, false);
				RuntimeAnnotationReader ar = getPropertyValue(properties, JAXBRIContext.ANNOTATION_READER, RuntimeAnnotationReader.class);
				@SuppressWarnings("unchecked")
				Collection<TypeReference> tr = getPropertyValue(properties, JAXBRIContext.TYPE_REFERENCES, Collection.class,
						Collections.<TypeReference> emptyList());
				Map<Class, Class> subclassReplacements;
				try {
					subclassReplacements = TypeCast.checkedCast(getPropertyValue(properties, JAXBRIContext.SUBCLASS_REPLACEMENTS, Map.class), Class.class,
							Class.class);
				} catch (ClassCastException e) {
					throw new JAXBException(Messages.INVALID_TYPE_IN_MAP.format(), e);
				}
				if (!properties.isEmpty()) {
					throw new JAXBException(Messages.UNSUPPORTED_PROPERTY.format(properties.keySet().iterator().next()));
				}

				JAXBContextImpl.JAXBContextBuilder builder = new JAXBContextImpl.JAXBContextBuilder();
				builder.setClasses(classes);
				builder.setTypeRefs(tr);
				builder.setSubclassReplacements(subclassReplacements);
				builder.setDefaultNsUri(defaultNsUri);
				builder.setC14NSupport(c14nSupport);
				builder.setAnnotationReader(ar);
				builder.setXmlAccessorFactorySupport(xmlAccessorFactorySupport);
				builder.setAllNillable(allNillable);
				builder.setRetainPropertyInfo(retainPropertyInfo);
				builder.setSupressAccessorWarnings(supressAccessorWarnings);
				builder.setImprovedXsiTypeHandling(improvedXsiTypeHandling);
				/**
				if (ClassUtil.hasMethod(builder.getClass(), "setDisableSecurityProcessing", boolean.class)) {
					builder.setDisableSecurityProcessing(disableSecurityProcessing);
				}*/
				result = builder.build();
				put(result, classes);
				return result;
			}
		}

		/**
		 * @param properties
		 * @param keyName
		 * @param type
		 * @param defaultValue
		 * @return
		 * @throws JAXBException
		 */
		@SuppressWarnings("unchecked")
		private static <T> T getPropertyValue(Map<String, Object> properties, String keyName, Class<T> type, Object defaultValue) throws JAXBException {
			Object o = properties.get(keyName);
			if (o == null) {
				// 传入的默认值应该是类型匹配的
				return (T) defaultValue;
			}
			properties.remove(keyName);
			if (!type.isInstance(o)) {
				throw new JAXBException(Messages.INVALID_PROPERTY_VALUE.format(keyName, o));
			} else {
				return (T) o;
			}
		}

		/**
		 * @param properties
		 * @param keyName
		 * @param type
		 * @return
		 * @throws JAXBException
		 */
		private static <T> T getPropertyValue(Map<String, Object> properties, String keyName, Class<T> type) throws JAXBException {
			return getPropertyValue(properties, keyName, type, null);
		}

		private static JAXBContext get(Class<?>... classes) {
			return contextCache.get(getKey(classes));
		}

		private static void put(JAXBContext context, Class<?>... classes) {
			contextCache.put(getKey(classes), context);
		}

	}

	private static String getKey(Class<?>... classes) {
		if (classes.length == 1) {
			return classes[0].getName();
		}
		StringBuilder sb = new StringBuilder(64);
		for (Class<?> clazz : classes) {
			sb.append(clazz.getName()).append(";");
		}
		return sb.toString();
	}

}
