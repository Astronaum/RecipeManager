package dp.grp4.exceptions;

public class DBException extends Exception{
    private final int code;
    public DBException(String message,int code){
        super(message);
        this.code=code;
    }
    public int getCode(){
        return this.code;
    }
}
