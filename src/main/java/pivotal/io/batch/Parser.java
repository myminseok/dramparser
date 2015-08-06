package pivotal.io.batch;

import pivotal.io.batch.domain.Command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private String infilepath=null;
    private String outdirpath=null;
    private String outfileextension=null;


    public Parser(String infilepath, String outdirpath, String outfileextension){
        this.infilepath=infilepath;
        this.outdirpath=outdirpath;
        this.outfileextension=outfileextension;
        System.out.println("infilepath:"+infilepath);
        System.out.println("outdirpath:"+outdirpath);
        System.out.println("outfileextension:"+outfileextension);
    }

    public void execute() throws Exception{

        long lStartTime = System.currentTimeMillis();

        File infile = new File(infilepath);

        File outfiletemp = new File(outdirpath+File.separator+infile.getName()+".tmp");

        String outfilepath=outdirpath + File.separator + infile.getName() + "." + outfileextension;

        if(!infile.exists()){
            System.out.println("file not found:"+infilepath);
        }

        if(!outfiletemp.exists()){
            System.out.println("file not found:"+outdirpath);
        }

        InputStream is=null;
        FileWriter os=null;


        StateMachine sm = new StateMachine();

        Map<String, String> commandMap = new HashMap<String, String>();
        try {

            is = new FileInputStream(infile);
            os = new FileWriter(outfiletemp);
            int serial=0;
            String bigHex="";
            String bits="";
            String parsed="";
            String result;
            Command command;
            //byte[] buffer = new byte[4];
            byte[] bufferBig = new byte[4];

            boolean transit=false;

            while (is.read(bufferBig) >= 0 ) {
                serial++;
//                command= sm.getCommand(bufferBig);
//                bits= command.byteToBits();
//                bigHex=command.byteToHexs();
//                System.out.println(bigHex);
//                if(sm.ignore(command)){

//                }
//                transit = sm.transit(command);
//                if(transit){
//                    System.out.println(serial+" transit:" + sm+" / new cmd: "+command);
//                }else{
////                    System.out.println(serial+" no transit:" + sm+" / new cmd: "+command);
//                }
                bits = Command.byteToBits(bufferBig);
                bigHex = Command.bytesToHex(bufferBig);
                parsed = Command.parse(bufferBig);
                StringBuilder sb = new StringBuilder();
                sb.append(serial).append(",");
                sb.append(bits).append(",");
                sb.append(bigHex).append(",");
                sb.append(parsed).append("\n");

                result = sb.toString();
//                sb.append(sm.currentState).append(",");
//                sb.append(command.getName()).append(",");
//                sb.append(transit).append("\n");

                if(!commandMap.containsKey(bits)){
                    commandMap.put(bits, result);
                    os.write(result);
                    //System.out.println(result);

                }


            }
            for(String key: commandMap.keySet()) {
                System.out.println(key+" "+commandMap.get(key));
            }

        }finally{
            if(is!=null) is.close();
            if(os!=null) os.close();
        }

        System.out.println("renaming: " + outfiletemp.getAbsolutePath() + " to \n" + outfilepath);
        outfiletemp.renameTo(new File(outfilepath));


        long lEndTime = System.currentTimeMillis()- lStartTime;
        System.out.println(GetFormattedInterval(lEndTime));

    }


    public static String GetFormattedInterval(final long ms) {
        long millis = ms % 1000;
        long x = ms / 1000;
        long seconds = x % 60;
        x /= 60;
        long minutes = x % 60;
        x /= 60;
        long hours = x % 24;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }


//    public static void main(String[] args) throws Exception{
//
//        String infilepath="/Users/kimm5/_dev/DramParser/src/test/resources/testdata/sample";
//        String outdirpath="/Users/kimm5/_dev/DramParser/src/test/resources/out";
//        new Parser(infilepath,outdirpath).execute();
//    }

}
