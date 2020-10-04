package dynamicproxy;

/*
 * I's a simple JDKProxy
 * @author:he sanshi
 */
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SimpleJDKDynamicProxyDemo {
    //A工厂完成对A商品的实际生产
    static interface FactoryA {
        public void produceA();
    }
    //B工厂完成对B商品的实际生产
    static class FactoryAImpl implements FactoryA  {
        @Override
        public void produceA() {
            System.out.println("工厂A完成了对商品A的生产、装配、运输");
        }
    }

    static interface FactoryB {
        public void produceB();
    }

    static class FactoryBImpl implements FactoryB  {
        @Override
        public void produceB() { System.out.println("工厂B完成了对商品B的生产、装配、运输"); }
    }

    //看起来这里将充当我们的代理商店
    static class SimpleInvocationHandler implements InvocationHandler {
        private Object realObj;
        public SimpleInvocationHandler(Object realObj) {
            this.realObj = realObj;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            System.out.println("您选购了"+ method.getName()+"商品");
            System.out.println("进入 " + realObj.getClass()
                    .getSimpleName() + "::" + method.getName());
            Object result = method.invoke(realObj, args);
            System.out.println("完成 " + realObj.getClass()
                    .getSimpleName() + "::" + method.getName());
            System.out.println("您完成了"+ method.getName()+"商品的购买");
            return result;
        }
    }

    //这里是获取我们的动态代理商店的
    private static <T> T getProxy(Class<T> intf, T realObj) {
        return (T) Proxy.newProxyInstance(intf.getClassLoader(),
                new Class<?>[] { intf }, new SimpleInvocationHandler(realObj));
    }

    public static void main(String[] args) throws Exception {
        FactoryA a = new FactoryAImpl();
        FactoryA aProxy = getProxy(FactoryA.class, a);
        aProxy.produceA();
        FactoryB b = new FactoryBImpl();
        FactoryB bProxy = getProxy(FactoryB.class, b);
        bProxy.produceB();
    }
}
