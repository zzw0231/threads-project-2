import java.util.ArrayList;
import java.util.concurrent.*;


public class p2 {
	public static long time = System.currentTimeMillis();
	public static int nstudent = 14;      
    public static int roomcap = 8;   
    public static int currentroom = 8;
    public static int groupsize =3;
    public static int currentsize =3;
    public static int remain =14;
    public static int remain2 =14;
    public static int [][]examscore=new int[nstudent][3];
	public static Semaphore intrustorarrived=new Semaphore(0);
	public static Semaphore waitforstart=new Semaphore(0);
	public static Semaphore waitforend=new Semaphore(0);
	public static Semaphore joingroup=new Semaphore(0);
	public static Semaphore allend=new Semaphore(0);
	public static Semaphore instructorleave=new Semaphore(0);
	public static Semaphore mutex=new Semaphore(1);
	public static Semaphore instructorstart=new Semaphore(0);
	public static Semaphore mutex2=new Semaphore(1);
	//initialization
	
	
	
    public static ArrayList<student> s = new ArrayList<student>(); 
	public static void main(String[] args) {
		if (args.length ==3 ) {
			nstudent = Integer.parseInt(args[0]);  //number of students 
			roomcap  = Integer.parseInt(args[1]);  //room capacity
			groupsize = Integer.parseInt(args[2]);
			remain=nstudent;
			currentroom=roomcap;
        }
        else        // If no arguments are provided (or the number of arguments is wrong) , then the program uses default values
            System.out.println("Wrong number of Arguments! (Will use default values)"+"\n"+"format: (number of student) (room capacity) (group size)"+"\n"+"example:10 10 5");
		for(int i=0;i<nstudent;i++){  //set examscore to be 0
			for(int j=0;j<2;j++){
				examscore[i][j]=0;
			}
		}
		for(int i=0;i<nstudent;i++){				//create and start the threads
			student si=new student(i);
			si.start();
			s.add(si);
			//smallPause(600);
		}
		instructor ins=new instructor();	//pass Arraylist of threads to instructor class
		ins.start();
	}
	public static void smallPause(int n){			//pause function
        try {  Thread.sleep(n); }
        catch (InterruptedException e) { e.printStackTrace();  }
    }

}
