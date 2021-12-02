package beans;

public class BasicService extends Node {
    public BasicService(TCMap TCMap) {
        setTCMap(TCMap);
        setType(Type.BS);
    }
    public TCMap getTCS(){
        return super.tcMap;
    };

    @Override
    protected void setType(Type type) {
        super.type= Type.BS;
    }

}
