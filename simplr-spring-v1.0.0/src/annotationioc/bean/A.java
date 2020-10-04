package annotationioc.bean;


import annotationioc.annotation.Component;
import annotationioc.annotation.Value;

@Component("A")
public class A {
    @Value("A.store")
    private String brand;

    @Value("1000")
    private int price;

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
