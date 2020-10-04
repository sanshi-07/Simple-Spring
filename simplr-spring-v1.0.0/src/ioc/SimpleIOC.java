package ioc;

/* description: 这是一个IOC的实现类
 * 1.加载xml配置文件，遍历其中的标签
 * 2.获取标签中的id和class属性，加载class属性对应的类，并创建bean
 * 3.遍历标签中的标签，获取属性值，并将属性值填充到bean中
 * 4.将bean注册到bean容器中
 *
 * author:he sanshi
 * date:20.10.01
 */



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class SimpleIOC {

    private Map<String, Object> beanMap = new HashMap<String, Object>();

    public SimpleIOC(String location) throws Exception {
        loadBeans(location);
    }

    public Object getBean(String name) {
        Object bean = beanMap.get(name);
        if (null == bean) {
            throw new IllegalArgumentException("There is no bean with name:" + name);
        }
        return bean;
    }


    private void registerBean(String id, Object bean) {
        beanMap.put(id, bean);
    }

    private void loadBeans(String location) throws Exception {
        // 加载xml配置文件
        InputStream inputStream = new FileInputStream(location);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(inputStream);
        Element root = doc.getDocumentElement();
        NodeList nodes = root.getChildNodes();

        // 遍历<bean>标签
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                String id = ele.getAttribute("id");
                String className = ele.getAttribute("class");
                // 加载beanClass
                Class beanClass = null;
                try {
                    beanClass = Class.forName(className);
                } catch (Exception e) {
                    return ;
                }

                // 创建bean
                Object bean = beanClass.newInstance();
                // 遍历<propertiy>标签
                NodeList propertyNodes = ele.getElementsByTagName("property");
                for (int j = 0; j < propertyNodes.getLength(); j++) {
                    Node propertyNode = propertyNodes.item(j);
                    if (propertyNode instanceof Element) {
                        Element propertyElement = (Element) propertyNode;
                        String name = propertyElement.getAttribute("name");
                        String value = propertyElement.getAttribute("value");

                        // 利用反射将bean相关字段访问权限设为可访问
                        Field declaredField = bean.getClass().getDeclaredField(name);
                        declaredField.setAccessible(true);
                        if (value != null && value.length() > 0) {
                            // 将相关属性值填充到相关字段中
                            setFieldValue(declaredField, bean, value);
                        } else {
                            String ref = propertyElement.getAttribute("ref");
                            if (null == ref || 0 == ref.length()) {
                                throw new IllegalArgumentException("ref config error");
                            }
                            // 将引用填充到相关字段中，前提是该引用已加入beanmap容器中
                            declaredField.set(bean, getBean(ref));
                        }
                        // 将bean注册到bean容器中
                        registerBean(id, bean);
                    }
                }
            }
        }
    }

    /**
     *完成对属性类型判断，并填充对应类型的值
     *这里少了一个异常输出。
     */
    private static void setFieldValue(Field f, Object obj, String value)
            throws Exception {
        Class<?> type = f.getType();
        if(type == int.class) {
            f.setInt(obj, Integer.parseInt(value));
        } else if(type == byte.class) {
            f.setByte(obj, Byte.parseByte(value));
        } else if(type == short.class) {
            f.setShort(obj, Short.parseShort(value));
        } else if(type == long.class) {
            f.setLong(obj, Long.parseLong(value));
        } else if(type == float.class) {
            f.setFloat(obj, Float.parseFloat(value));
        } else if(type == double.class) {
            f.setDouble(obj, Double.parseDouble(value));
        } else if(type == char.class) {
            f.setChar(obj, value.charAt(0));
        } else if(type == boolean.class) {
            f.setBoolean(obj, Boolean.parseBoolean(value));
        } else if(type == String.class) {
            f.set(obj, value);
        } else {
            Constructor<?> ctor = type.getConstructor(
                    new Class[] { String.class });
            f.set(obj, ctor.newInstance(value));
        }
    }

}
