package annotationioc;

import annotationioc.annotation.Autowired;
import annotationioc.annotation.Component;
import annotationioc.annotation.Value;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

    private static Map<String, Object> beansMap = new ConcurrentHashMap<>();

    public ApplicationContext(Class<?>... Classes) {
        this.register(Classes);
    }

    /*
     *对传入的需要解析的class文件，逐个加载成bean实例
     */
    public  void register(Class<?>[] annotatedClasses) {
        Class[] var2 = annotatedClasses;
        int var3 = annotatedClasses.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Class<?> annotatedClass = var2[var4];
            this.registerBean(annotatedClass);
        }
    }


    /*
     *开始注册bean，并分析这个bean是否为@component标注，标注了则默认为单例模式
     * 并存入beanmap
     */
    protected  <T> T  registerBean(Class<?> annotatedClass) {
        try {
            Component component = annotatedClass.getAnnotation(Component.class);
            if(component==null) {
                return createInstance(annotatedClass);
            }
            String beanName = "";
            if(component.value().equals("")){
                beanName = annotatedClass.getSimpleName();
            }else {
                beanName = component.value();
            }
            Object obj = beansMap.get(beanName);
            if(obj != null) {
                return (T) obj;
            }
            synchronized (annotatedClass) {
                obj = beansMap.get(beanName);
                if(obj == null) {
                    obj = createInstance(annotatedClass);
                    beansMap.put(beanName, obj);
                }
            }
            return (T) obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * 完成实例化，并解析注解
     */
    private  <T> T createInstance(Class<?> cls) throws Exception {
        T obj = (T) cls.newInstance();
        Field[] fields = cls.getDeclaredFields();
        for(Field f : fields) {
            if(f.isAnnotationPresent(Value.class)) {
                if(!f.isAccessible()) {
                    f.setAccessible(true);
                }
                Value value=f.getAnnotation(Value.class);
                this.setFieldValue(f,obj,value.value());
            }
            if(f.isAnnotationPresent(Autowired.class)) {
                if(!f.isAccessible()) {
                    f.setAccessible(true);
                }
                Class<?> fieldCls = f.getType();
                f.set(obj, this.registerBean(fieldCls));
            }
        }
        return obj;
    }

    /**
     *完成对属性类型判断，并填充对应类型的值
     *这里少了一个异常输出。
     */
    private  void setFieldValue(Field f, Object obj, String value)
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

    /*
     * 获取bean，如果已经初始化的bean直接从beanmap返回，
     * 没有初始化的bean，阔以通过再加载，生成实例返回
     */
    public  <T> T getBean(String name) throws Exception {
        if (beansMap.get(name)==null){
               try {
                   Class cls=Class.forName(name);
                   return this.registerBean(cls);
               }catch (Exception e){
                   throw new Exception("No bean named '"+name+ "'available,you can input 'package.ClassName' to get the bean");
               }

        }
        return (T) beansMap.get(name);
    }


}
