package pivotal.io.batch.domain;


public class CommandUndefined extends Command{

    public CommandUndefined() {
        this.name = type.UND;
    }

    public boolean isMatching(byte[] v){
        return false;
    }



}
