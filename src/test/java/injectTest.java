import kall.reflection.GetTargetArray;

public class injectTest {
    public static void main(String[] args) {
        GetTargetArray.getInstance();
        GetTargetArray.getInstance();
        System.out.println(GetTargetArray.getInstance().getArray());
    }
}
