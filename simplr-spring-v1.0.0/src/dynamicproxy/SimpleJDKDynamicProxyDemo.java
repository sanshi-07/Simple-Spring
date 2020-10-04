package dynamicproxy;

/*
  I's a simple JDKProxy
 */
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SimpleJDKDynamicProxyDemo {
    static interface IService {
        public void sayHello();
    }
    static class RealService implements IService {
        @Override
        public void sayHello() {
            System.out.println("hello");
        }
    }
    static class SimpleInvocationHandler implements InvocationHandler {
        private Object realObj;
        public SimpleInvocationHandler(Object realObj) {
            this.realObj = realObj;
        }
        @Override
        public Object invoke(Object proxy, Method method,
                             Object[] args) throws Throwable {
            System.out.println("entering " + method.getName());
            Object result = method.invoke(realObj, args);
            System.out.println("leaving " + method.getName());
            return result;
        }

    }
    public static void main(String[] args) {
        IService realService = new RealService();
        IService proxyService = (IService) Proxy.newProxyInstance(
                IService.class.getClassLoader(), new Class<?>[] { IService.class },
                new SimpleInvocationHandler(realService));
        proxyService.sayHello();
    }
}

/*
 * 对上面的包装
 */
 class GeneralProxyDemo {
    static interface IServiceA {
        public void sayHello();
    }
    static class ServiceAImpl implements IServiceA {
        @Override
        public void sayHello() {
            System.out.println("hello");
        }
    }
    static interface IServiceB {
        public void fly();
    }
    static class ServiceBImpl implements IServiceB {
        @Override
        public void fly() {
            System.out.println("flying");
        }
    }
    static class SimpleInvocationHandler implements InvocationHandler {
        private Object realObj;
        public SimpleInvocationHandler(Object realObj) {
            this.realObj = realObj;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            System.out.println("entering " + realObj.getClass()
                    .getSimpleName() + "::" + method.getName());
            Object result = method.invoke(realObj, args);
            System.out.println("leaving " + realObj.getClass()
                    .getSimpleName() + "::" + method.getName());
            return result;
        }
    }
    private static <T> T getProxy(Class<T> intf, T realObj) {
        return (T) Proxy.newProxyInstance(intf.getClassLoader(),
                new Class<?>[] { intf }, new SimpleInvocationHandler(realObj));
    }
    public static void main(String[] args) throws Exception {
        IServiceA a = new ServiceAImpl();
        IServiceA aProxy = getProxy(IServiceA.class, a);
        aProxy.sayHello();
        IServiceB b = new ServiceBImpl();
        IServiceB bProxy = getProxy(IServiceB.class, b);
        bProxy.fly();
    }
}
