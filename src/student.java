import java.util.Random;


public class student extends Thread{	
	public static long time = System.currentTimeMillis();
	public int id;
	public int examtake=0;	//how many exam that student has took yet;
	public static int nexam=0; //the index of exam
	public static int size=0;
	public static int groupi=1;
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
		}
	public student(int i){
		id=i;
		setName("student-"+id);
	}

	public synchronized void group(){
		p2.currentsize--;
	}
	public synchronized void resetsize(){
		p2.currentsize=p2.groupsize;
	}
	public synchronized void remain(){
		p2.remain--;
	}
	public synchronized void remain2(){
		p2.remain2--;
	}
	public synchronized void room(){
		p2.currentroom--;
	}
	public void run() {
		gosleep(randomint(0,300));
		msg("I'm in the school\n");
		while(nexam<3&&examtake!=2){	//loop if 3 exam not finished and have not take 2 exam yet
			try {
	            p2.intrustorarrived.acquire();		//wait for instructor
	            if(instructor.flag){				//if student missed last exam, just let him out and wait for leave
	            	break;
	            }
	            msg("I'm in the class\n");	            
	            p2.mutex2.acquire();
	            room();
	            if(p2.currentroom==0){
	            	p2.instructorstart.release();
	            }
	            p2.mutex2.release();
            } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
			try {								
	            p2.waitforstart.acquire();
            } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            } 
			try {
	            p2.waitforend.acquire();
            } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
			if(instructor.flag){
            	break;
            }
			p2.examscore[id][nexam-1]=randomint(10,100);		//random score
			examtake++;
			gosleep(randomint(0,100)); //leave and take a break
		}
		try {
	        p2.allend.acquire();		//wait for all exam to be done
        } catch (InterruptedException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		if(p2.currentsize!=0&&p2.remain>p2.groupsize){		//join group if the number of student is greater than the group size
			group();
			try {
	            p2.joingroup.acquire();	    
            } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
		}
		else{												//when group full, release them, then let self join the group
			for(int i=0;i<p2.joingroup.getQueueLength();i++){
				p2.joingroup.release();		
				remain();
			}
			if(p2.remain>1){// if self is the last one, just leave
			group();
			resetsize();
			try {
	            p2.joingroup.acquire();
            } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
			}
			}
		}
		try {
	        p2.mutex.acquire();
        } catch (InterruptedException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
        }
		remain2();
		msg("I'm leaving with group: "+groupi+"\n");
		msg("there are "+p2.remain2+" students remain \n");
		size++;
		if(size==p2.groupsize){
			size=0;
			groupi++;
		}
		if(p2.remain2==0){
			p2.instructorleave.release();
		}
		p2.mutex.release();
		
		
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
