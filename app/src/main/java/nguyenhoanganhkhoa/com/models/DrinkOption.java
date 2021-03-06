package nguyenhoanganhkhoa.com.models;

public class DrinkOption {
    private String optionCate;
    private boolean isFree = true;
    private double optionAdditionPrice = 0;
    private boolean isChosen = false;
    private int valueLevel;

    public int getValueLevel() {
        return valueLevel;
    }

    public void setValueLevel(int valueLevel) {
        this.valueLevel = valueLevel;
    }

    public DrinkOption(String optionCate, int valueLevel) {
        this.optionCate = optionCate;
        this.valueLevel = valueLevel;
        this.optionAdditionPrice = optionAdditionPrice;
        this.isFree = isFree;
    }

    public String getOptionCate() {
        return optionCate;
    }

    public void setOptionCate(String optionCate) {
        this.optionCate = optionCate;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public double getOptionAdditionPrice() {
        return optionAdditionPrice;
    }

    public void setOptionAdditionPrice(double optionAdditionPrice) {
        this.optionAdditionPrice = optionAdditionPrice;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public DrinkOption(String optionCate) {
        this.optionCate = optionCate;
    }

    public DrinkOption(String optionCate, boolean isFree, double optionAdditionPrice) {
        this.optionCate = optionCate;
        this.isFree = isFree;
        this.optionAdditionPrice = optionAdditionPrice;
    }

    @Override
    public String toString() {
        return optionCate + isChosen;
    }
}
