package shadowrundomain;

public class Roller {
	
	public succes makeroll(int dicepool,boolean edge){
		int succes=0;
		int failed=0;
		for(int k=0;k<dicepool;k++){
			int temp =(int)(Math.random()*6)+1;
			if (temp>4){
				succes++;
				if(edge==true){
					if(temp==6){
						dicepool++;
					}	
				}
				
			}
			if (temp==1){
				failed++;
			}
			
		}
		double tempa=dicepool*1/3;
		//test
		System.out.println("succes: "+succes);
		boolean glits = false;
			int temp1 = dicepool/2;
			int temp2 = (dicepool+1)/2;
			if (temp1 == temp2 ){
				if(temp2<failed){
					glits=true;
				}
			}
			else{		
				if(temp1<failed){
					glits=true;
					
				}
			
			}
		
		
		succes tempSucces= new succes(succes, glits);
		
		
		
		return tempSucces;
		
	}
}
