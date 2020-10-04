package ioc;

public class SimpleIOCTest {

    public static void main(String[] args) throws Exception {
        String location = SimpleIOC.class.getClass().getClassLoader().getResource("ioc/ioc.xml").getFile();
        SimpleIOC bf = new SimpleIOC(location);
        Thread A=new Thread(()->{
             Dumbbell dumbbell=(Dumbbell)bf.getBean("dumbbell");
             System.out.println("A妹拿到了"+dumbbell.getBrand()+"哑铃");
             System.out.println("A妹使用"+dumbbell.getDiscus().getWeight()+"kg重锻炼了~");
        });
        Thread B=new Thread(()->{
            Dumbbell dumbbell=(Dumbbell)bf.getBean("dumbbell");
            System.out.println("B哥拿到了"+dumbbell.getBrand()+"哑铃");
            System.out.println("B哥使用"+dumbbell.getDiscus().getWeight()+"kg重锻炼了~");
        });
        A.start();
        B.start();
    }
}
