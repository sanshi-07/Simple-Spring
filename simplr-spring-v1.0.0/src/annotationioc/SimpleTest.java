package annotationioc;

import annotationioc.bean.A;
import annotationioc.bean.B;
import annotationioc.bean.C;

public class SimpleTest {
    public static void main(String[] args) throws Exception{
        ApplicationContext context=new ApplicationContext(A.class, B.class);
        A a=context.getBean("A");
        System.out.println("这说A组件价格："+a.getPrice());
        //这里的b1和b2输出一样，因为他们是单例模式的
        B b1=context.getBean("B");
        System.out.println(b1.getA().getBrand());
        B b2=context.getBean("B");
        System.out.println(b2.getA().getBrand());
        C c=context.getBean("AnnotationIOC.bean.C");
        System.out.println("这是C需要的组件来源："+c.getA().getBrand()+c.getB().getBrand());
        C c1=context.getBean("AnnotationIOC.bean");
    }
}
