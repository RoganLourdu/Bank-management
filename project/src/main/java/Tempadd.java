import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Tempadd {
	public static boolean acountconfirm(String username,String email,String dob,String gender,String phonenumber,String Aid,String password) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Logger logger = Logger.getLogger(Tempadd.class);
        String path="/home/rogan-zstk272/eclipse-workspace/project/src/main/java/log4j.properties";
        PropertyConfigurator.configure(path);
        boolean state = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank ","root","");
            state=addvalues(con,logger, username, email, dob, gender, phonenumber, Aid,password);
            con.close();
        } catch (Exception e) {
            logger.debug(formatter.format(date)+" Some error on create connection or any problem on there  "+e);
            state=false;
        }
        return state;
    }
    protected static boolean addvalues(Connection con,Logger logger,String username,String email,String dob,String gender,String phonenumber,String Aid,String password) {
    	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Date date = new Date();
         long otp =0;
        try{
            PreparedStatement ps =con.prepareStatement("insert into Temp (username,email,dob,pnumber,gender,Anumber,password,addeddate,otp) values(?,?,?,?,?,?,?,?,?)");
            ps.setString(1,username);
            ps.setString(2,email);
            ps.setString(3,dob);
            ps.setString(4,phonenumber);
            ps.setString(5,gender);
            ps.setString(6,Aid);
            ps.setString(7,Encryption.encryption(password));
            ps.setString(8,String.valueOf(java.time.LocalDate.now()));
            otp=Integer.parseInt(getRandomNumberString());
            ps.setLong(9,otp);
            int rs=ps.executeUpdate();
            long uidvalue=uid(con, logger, username, email, dob, gender, phonenumber, Aid, password, otp);
            if (rs>=1 && uidvalue!=0) {
                logger.info(formatter.format(date)+" Succssfully account created for email = "+email+" and username = "+username+" and user id created the id = "+uidvalue);
                boolean otpsendstate=Mail.SendMail(email,"Account confirmation","This is the otp for your account confirmation "+otp+" and thats your tempravory id "+generatededuid(uidvalue));
                if (otpsendstate) {
                	logger.info(formatter.format(date)+" Successfully values sended for account confirmation to email = "+email+" and user name = "+username+" here user temporary id = "+ uidvalue);
                    return true;
                }
                else {
                	logger.debug(formatter.format(date)+" Some problem for send values for account confirmation send to email = "+email+" and user name = "+username);
                    return false;
                }
            }
            else {
            	logger.debug(formatter.format(date)+" some problem for add values in the tempravory databases for accccount confirmation the values email = "+email+" user name = "+username+" date of birth = "+dob+" phone number = "+phonenumber+" Aaddar number = "+Aid+" and generate otp = "+otp+"added date = "+java.time.LocalDate.now()+" or some problrm on uid the user id = "+uidvalue);
                return false;
            }
        } catch (NumberFormatException n) {
        	logger.debug(formatter.format(date)+" Some error on make otp as a Integer the otp is "+otp+" its throw Number Format Exception and the errors "+n);
            return false;
        }
        catch (Exception e) {
        	logger.debug(formatter.format(date)+" some problem going on there the problems to debug "+e);
            return false;
        }
    }
    public static String generatededuid(long uid) {
        String val="RBS1000TP";
        int len=15-(9+String.valueOf(uid).length());
        for (int i=0;i<len;i++){
            val+=0;
        }
        val+=uid;
        return val;
    }
    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
    	 public static long uid(Connection con,Logger logger,String username,String email,String dob,String gender,String phonenumber,String Aid,String password,long otp) {
    		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	     Date date = new Date();  
    		 try {
    	            PreparedStatement ps = con.prepareStatement("select uid from Temp where username=? and email=? and dob=? and pnumber=? and gender=? and Anumber=? and password=? and addeddate=? and otp=?");
    	            ps.setString(1, username);
    	            ps.setString(2, email);
    	            ps.setString(3, dob);
    	            ps.setString(4, phonenumber);
    	            ps.setString(5, gender);
    	            ps.setString(6, Aid);
    	            ps.setString(7,Encryption.encryption(password));
    	            ps.setString(8,String.valueOf(java.time.LocalDate.now()));
    	            ps.setLong(9, otp);
    	            ResultSet rs= ps.executeQuery();
    	            if (rs.next()){
    	                return rs.getLong(1);
    	            }
    	            else {
    	            	logger.error(formatter.format(date)+" Some error on the the statement return false in get user id ");
    	            	 return 0;
					}
    	        }
    	        catch (Exception e){
    	        	logger.debug(formatter.format(date)+" Some problem for get user id you want to dubug it "+e);
    	        	 return 0;
    	        }
    	    }
}
