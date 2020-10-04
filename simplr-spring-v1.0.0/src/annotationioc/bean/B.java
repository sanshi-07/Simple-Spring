package annotationioc.bean;

import annotationioc.annotation.Autowired;
import annotationioc.annotation.Component;
import annotationioc.annotation.Value;

@Component
public class B {

    @Value("B.store")
    private String brand;

    @Value("2000")
    private int price;

    @Autowired
    private A a;

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
