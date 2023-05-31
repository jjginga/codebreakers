package breakers.code.tac;

public class LabelThreeAddressCode extends ThreeAddressCode {
    public LabelThreeAddressCode(String result) {
        super("", "", "", result);
    }

    @Override
    public String toString() {
        return getResult() + ":";
    }
}
