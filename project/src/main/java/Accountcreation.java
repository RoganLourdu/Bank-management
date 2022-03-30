import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
public class Accountcreation {
	@SuppressWarnings("finally")
	public static boolean acccreation(String uid,String password,String otp,String firstdeposit) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();
		Logger logger = Logger.getLogger(Accountcreation.class);
		String path="/home/rogan-zstk272/eclipse-workspace/project/src/main/java/log4j.properties";
		PropertyConfigurator.configure(path);
		Connection con=null;
		boolean state = false;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank ","root","");
			String[] arr=newaccnumanduid(con);
			String[] dataarray=getvalues(con, logger, uid, otp, password);
			state=signUP(con, logger,dataarray,arr,firstdeposit, password);
			if (state) {
				Mail.SendMail(dataarray[1],"Your Account confirmed Succesfully","Your Ragan Bank service account number is "+arr[1]);
			}
			
		} catch (Exception e) {
			logger.debug(formatter.format(date)+" Some error on create connection or any problem on there  "+e);
			state=false;
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				logger.debug(formatter.format(date)+" Some problem while close connection "+e);
			}
			return state;
		}
	}
	protected static boolean signUP(Connection con,Logger logger,String[] dataarray,String[] arr,String firstdeposit,String password)  {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();
		try {
            PreparedStatement ps =con.prepareStatement("insert into Details (uid,username,email,dob,pnumber,gender,Anumber,accNumber,password) values(?,?,?,?,?,?,?,?,?)");
            ps.setLong(1,Integer.parseInt(arr[0]));
            ps.setString(2,dataarray[0]);
            ps.setString(3,dataarray[1]);
            ps.setString(4,dataarray[2]);
            ps.setString(5,dataarray[3]);
            ps.setString(6,dataarray[4]);
            ps.setString(7,dataarray[5]);
            ps.setString(8,arr[1]);
            if (!Encryption.encryption(password).equals(dataarray[6])) {
            	logger.info(Encryption.encryption(password)+" "+dataarray[6]);
            	logger.debug(!Encryption.encryption(password).equals(dataarray[6]));
            	return false;
			}
            ps.setString(9,Encryption.encryption(password));
            int rs=ps.executeUpdate();
            if (rs>=1) {
               boolean trans=newTransaction(con,logger,arr[1],firstdeposit,dataarray[1],dataarray[0]);
               if (trans) {
            	   logger.info(formatter.format(date)+" Succssfully account created for Account number = "+arr[1]+" and email = "+dataarray[1]+" and username = "+dataarray[0]);
            	   return true;
               }
               else if (!trans){
                   PreparedStatement stmt=con.prepareStatement("DELETE FROM Details WHERE accNumber=?");
                   stmt.setString(1,arr[1]);
                   stmt.executeUpdate();
                   logger.error(formatter.format(date)+" Some error on add first transaction for email = "+dataarray[1]+" and username = "+dataarray[0]);
                   return false;
               }
            }
            else {
            	logger.error(formatter.format(date)+" Some error on add all values for email = "+dataarray[1]+" and username = "+dataarray[0]);
				return false;
			}
		}
         catch (NumberFormatException s){
            	logger.error(formatter.format(date)+" That throws Number Format Exception uid ");
         }
        catch (Exception e) {
        	logger.debug(formatter.format(date)+"some problem going on there the problems to debug "+e);
        	return false;
        }
		return false;
	}
	public static String[] newaccnumanduid(Connection con) throws Exception{
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	    Accountcreation date = new Accountcreation();
		Logger logger = Logger.getLogger(Accountcreation.class);
		String path="/home/rogan-zstk272/eclipse-workspace/project/src/main/java/log4j.properties";
		PropertyConfigurator.configure(path);
		try {
	    String[] arr=new String[2];
	    PreparedStatement stmt = con.prepareStatement("select uid,accNumber from Details order by uid desc limit 1 ;");
	    ResultSet rs=stmt.executeQuery();
	    if(rs.next()) {
	        arr[0]=String.valueOf(rs.getLong(1)+1);
	        arr[1]=generatenewaccnumber(rs.getString(2));
	        if ((arr[0].matches("^\\d{1,}+$")) && (arr[1].matches("^[RBS1003A]+\\d{7}+$"))) {
				return arr;
			}
	        else {
	        	logger.error(formatter.format(date)+" Some error on create uid,accountnumber the uid "+arr[0]+" and  accountnumber "+arr[1]);
				return null;
			}
	    }
	    else {
	    	logger.error(formatter.format(date)+" The Quary for create uid,accountnumber cannot executed succesfully");
	    	return null;
		}
		}
		catch (Exception e) {
			logger.error(formatter.format(date)+" The Quary for create uid,accountnumber cannot executed succesfully. The method throw Exception the errors "+e);
			return null;
		}
	}
	public static String generatenewaccnumber(String lastaccnum){
	        String ans="RBS1003A";
	        String lastnum=String.valueOf(Integer.parseInt(lastaccnum.substring(8,15))+1);
	        int mylen=15-(8+lastnum.length());
	        for (int i=0;i<mylen;i++){
	            ans+=0;
	        }
	        ans+=lastnum;
	        return ans;
	    }
    public static boolean newTransaction(Connection con,Logger logger,String accnumber, String amount,String email,String username) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();
        try {
            PreparedStatement stmt =con.prepareStatement("insert into Transaction (accNumber,Date,Time,T_id,Credit,debit,Balance) values(?,?,?,?,?,?,?)");
            stmt.setString(1, accnumber);
            String datenow = String.valueOf(java.time.LocalDate.now());
            stmt.setString(2, datenow);
            String time = String.valueOf(java.time.LocalTime.now()).substring(0, 8);
            stmt.setString(3, time);
            int T_id=gett_id(con,logger);
            if (String.valueOf(T_id).matches("^\\d{1,}+$") ) {
             stmt.setLong(4,T_id);
            }
            stmt.setLong(5,Integer.parseInt(amount));
            stmt.setLong(6,0);
            stmt.setLong(7,Integer.parseInt(amount));
            int rs1 = stmt.executeUpdate();
            if (rs1 >= 1) {
            	logger.info(formatter.format(date)+" Succssfully first deposit finished for Account number "+accnumber);
                return true;
            }
            else {
            	logger.error(formatter.format(date)+" Some error on first transaction for email = "+email+" and username = "+username+" and account number=  "+accnumber);
            	return false;
			}
        }
        
        catch (NumberFormatException s){
        	logger.error(formatter.format(date)+" That throws Number Format Exception for Amount  = "+amount);
        }
        catch (Exception e){
        	logger.debug(formatter.format(date)+" some problem going on there the problems to debug "+e);
        }
    	return false;
    }
	public static int gett_id(Connection con,Logger logger) {
        int newT_id=0;
        PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("select T_id from Transaction order by T_id desc limit 1");
			ResultSet rs=stmt.executeQuery();
	       if(rs.next()) {
	            //print the values
	           newT_id+=rs.getLong(1);
	           newT_id=newT_id+1;
	           return newT_id;
	        }
	        else {
	        	return 0;
			}
		} catch (Exception e) {
			return 0;
		}
    }
    public static String[] getvalues(Connection con,Logger logger,String uid,String otp,String password) {
    	String[] dataarray=new String[7];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            PreparedStatement ps =con.prepareStatement("select username,email,dob,pnumber,gender,Anumber,password from Temp where uid=? and otp=? and addeddate=? and password=?");
            ps.setLong(1,Integer.parseInt(uid.substring(9,15)));
            ps.setLong(2,Integer.parseInt(otp));
            ps.setString(3,String.valueOf(java.time.LocalDate.now()));
            ps.setString(4,Encryption.encryption(password));
            ResultSet rs=ps.executeQuery();
            if (rs.next()) {
              dataarray[0]= rs.getString(1);
              dataarray[1]= rs.getString(2);
              dataarray[2]= rs.getString(3);
              dataarray[3]= rs.getString(4);
              dataarray[4]= rs.getString(5);
              dataarray[5]= rs.getString(6);
              dataarray[6]= rs.getString(7);
              logger.info(formatter.format(date)+" Succssfully values added for account create in data array for user name "+dataarray[0]+" and email = "+dataarray[1]);
            }
            else {
               logger.error(formatter.format(date)+" Some error on get all values in to dataarray for email = "+dataarray[1]+" and username = "+dataarray[0]);
            }
        }
        catch (NumberFormatException s){
            s.printStackTrace();
            logger.error(formatter.format(date)+" That throws Number Format Exception user id "+uid+" or otp "+otp +" wrong "+s);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.debug(formatter.format(date)+" some problem going on there for get values by user id the problems to debug "+e);
        }
        return dataarray;
	}
		
	}
