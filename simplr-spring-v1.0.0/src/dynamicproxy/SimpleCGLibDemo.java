package dynamicproxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;

public class SimpleCGLibDemo {
    static class FactoryA {
        public void produceA() {
            System.out.println("工厂A完成了对商品A的生产、装配、运输");
        }
    }

    static class FactoryB {
        public void produceB() {
            System.out.println("工厂B完成了对商品B的生产、装配、运输");
        }
    }

    static class SimpleInterceptor implements MethodInterceptor {
        @Override
        public Object intercept(Object object, Method method,
                                Object[] args, MethodProxy proxy) throws Throwable {
            System.out.println("您选购了"+ method.getName()+"商品");
            System.out.println("entering " + method.getName());
            Object result = proxy.invokeSuper(object, args);
            System.out.println("leaving " + method.getName());
            System.out.println("您完成了"+ method.getName()+"商品的购买");
            return result;
        }
    }
    private static <T> T getProxy(Class<T> cls) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new SimpleInterceptor());
        return (T) enhancer.create();
    }

    public static void main(String[] args) throws Exception {
        //我们想要购买FactoryA生产的produce
        FactoryA proxyA = getProxy(FactoryA.class);
        proxyA.produceA();
        //我们想要购买FactoryB生产的produce
        FactoryB proxyB = getProxy(FactoryB.class);
        proxyB.produceB();
    }
}