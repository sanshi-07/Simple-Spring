package ioc;

/* description: 这是一个实体类，哑铃
 * author:he sanshi
 * date:20.10.01
 */


public class Dumbbell {
    private int dumbbell_id;
    private String brand;
    private Discus discus;

    public void setDiscus(Discus discus) {
        this.discus = discus;
    }

    public Discus getDiscus() {
        return discus;
    }

    public void setDumbbell_id(int dumbbell_id) {
        this.dumbbell_id = dumbbell_id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }



    public int getDumbbell_id() {
        return dumbbell_id;
    }

    public String getBrand() {
        return brand;
    }


}
