package Annotation;

public class Data {
    @Ann.Ok
    public int field1;

    @Ann.Ok
    public int field2;

    @Ann.Ugly(k = 10)
    public int field3;

    @Ann.Ugly
    public int field4;

    public int field5;

    public Data(int f1, int f2, int f3, int f4, int f5) {
        this.field1 = f1;
        this.field2 = f2;
        this.field3 = f3;
        this.field4 = f4;
        this.field5 = f5;
    }
}
