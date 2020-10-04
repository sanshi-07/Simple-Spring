package annotationioc.bean;

import annotationioc.annotation.Autowired;
import annotationioc.annotation.Value;

public class C {

    @Value("C.store")
    private String brand;

    @Value("3000")
    private int price;

    @Autowired
    private A a;

    @Autowired
    private B b;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public String getBrand() {
        return brand;
    }

    public int getPrice() {
        return price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
