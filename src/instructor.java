import java.util.Random;
public class instructor extends Thread{
	public static long time = System.currentTimeMillis();
	public static boolean flag=false;
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
		}
	instructor(){
		setName("instructor");
	}
	public synchronized void exam(){
		student.nexam++;
	}
	public void run() {
		gosleep(randomint(0,300));
		msg("instrutor arrived \n");
		while(student.nexam<3){
			gosleep(randomint(500,1000));
			for(int i=0;i<p2.roomcap;i++){//arrive signal
				p2.intrustorarrived.release();
			}
			try {
	            p2.instructorstart.acquire();//wait for student enter the class
            } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
			exam();	//exam start, add index
			msg("exam: "+student.nexam+" begin \n");
			gosleep(randomint(500,1000));
			for(int i=0;i<p2.waitforstart.getQueueLength();i++){	//signal exam start
				p2.waitforstart.release();
			}
			msg("students are taking the exam \n");
			gosleep(5000);
			msg("exam: "+student.nexam+" end \n");
			for(int i=0;i<p2.waitforend.getQueueLength();i++){		//signal exam end
				p2.waitforend.release();
			}
			msg("break time \n");
			p2.currentroom=p2.roomcap;
			gosleep(randomint(500,1000)); //break			
		}
		gosleep(randomint(500,1000));
		msg("all exam finished."+"\n");		//grades print out		
		for(int i=0;i<p2.nstudent;i++){
			msg(""+p2.s.get(i).getName()+" grades are: "+p2.examscore[i][0]+" "+p2.examscore[i][1]+" "+p2.examscore[i][2]+"\n");
		}
		for(int i=0;i<p2.nstudent;i++){		//signal all exams are finished
			p2.allend.release();
		}
		flag=true;		//let those students who were waiting the queue of last exam out
		int z=p2.intrustorarrived.getQueueLength();
		for(int i=0;i<z;i++){
			p2.intrustorarrived.release();
		}
		for(int i=0;i<z;i++){
			p2.waitforstart.release();
		}
		for(int i=0;i<z;i++){
			p2.waitforend.release();
		}
		
		try {	//wait for all student leave
	        p2.instructorleave.acquire();
        } catch (InterruptedException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		msg("instrutor leave \n");
	}
	public void gosleep(int n){		//small function to avoid every try catch of sleep method
        try {  Thread.sleep(n); }
        catch (InterruptedException e) { /*e.printStackTrace();*/}
    }
	public static int randomint(int min, int max) {		//random integer function
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
