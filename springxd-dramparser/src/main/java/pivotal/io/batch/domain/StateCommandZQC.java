package pivotal.io.batch.domain;

/**

 */
public class StateCommandZQC extends StateCommand{

    public static StateCommand instance=null;

    public static StateCommand getInstance(){
        if(instance==null){
            instance= new StateCommandZQC();
        }
        return instance;
    }

    public StateCommandZQC(){
        this.name= type.ZQC;

    }


    public boolean isMatching(byte[] v){

        return  this.getBit(v, INDEX.CS0.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.ACTN.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.A16.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.A15.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.A14.getBinaryIndex())==0
                ;
    }


}
