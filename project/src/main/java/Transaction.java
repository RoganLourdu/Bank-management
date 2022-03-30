import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Transaction {
//    protected static Connection Makeconnection() throws ClassNotFoundException, SQLException {
//    	Connection con=null;
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        return con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank ","root","");
//    }
    @SuppressWarnings("finally")
	public static boolean Trans(String accnumber,Long amount,String type) {
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
			state=pay(con, logger,accnumber,amount,type);
			
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
    public static boolean pay(Connection con,Logger logger,String accnumber,Long amount, String type) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();
    	Long transid=gett_id(con,logger);
        try {
            PreparedStatement stmt =con.prepareStatement("insert into Transaction (accNumber,Date,Time,T_id,Credit,debit,Balance) values(?,?,?,?,?,?,?)");
            stmt.setString(1, accnumber);
            String dateonly = String.valueOf(java.time.LocalDate.now());
            stmt.setString(2, dateonly);
            String time = String.valueOf(java.time.LocalTime.now()).substring(0, 8);
            stmt.setString(3, time);
            if (String.valueOf(transid).matches("^\\d+${3,}"))
                stmt.setLong(4,transid);
            if (type.equals("Credit")) {
                stmt.setLong(5, amount);
                stmt.setLong(6, 0);
                long lastbalance = findlastTransactionbalance(con,logger,accnumber);
                long balance = amount + lastbalance;
                stmt.setLong(7, balance);
            }
            if (type.equals("Debit")) {
                stmt.setLong(5, 0);
                stmt.setLong(6, amount);
                long lastbalance = findlastTransactionbalance(con,logger,accnumber);
                long balance = lastbalance-amount;
                if (debitiisvalable(balance)) {
                    stmt.setString(7,String.valueOf(balance));}
                else {
                    System.out.println("return false in debit");
                    return false;
                }
            }
            int rs1 = stmt.executeUpdate();
            if (rs1 >= 1) {
                return true;
            }
            logger.info("The transaction not valif for account number = "+accnumber);
            return false;
        }
        catch (Exception e){
        	logger.debug(formatter.format(date)+" Some problem while make transaction for acount number = "+accnumber+"and th eprblem is "+e);
            return false;
        }
    }
    public static boolean debitiisvalable(long balance){
        if (balance>=500)
         return true;
        return false;
    }
    public static long findlastTransactionbalance(Connection con,Logger logger,String accnumber)  {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();
    	try {
        long lastbalaance=0;
        PreparedStatement stmt=con.prepareStatement("select Balance from Transaction where accNumber=? order by T_id  desc limit 1");
        stmt.setString(1,accnumber);
        ResultSet rs=stmt.executeQuery();
        if(rs.next()) {
            lastbalaance=Integer.parseInt(rs.getString(1));
        }
        return lastbalaance;
    	}
        catch (Exception e) {
        	logger.debug(formatter.format(date)+" Some error while get last balance for account number  and the account number ="+accnumber+" and the error was "+e);
		}
		return 0;
    }
    public static long gett_id(Connection con,Logger logger) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date();
        long newT_id=0;
        try {
        	 PreparedStatement stmt=con.prepareStatement("select T_id from Transaction order by T_id desc limit 1");
             ResultSet rs=stmt.executeQuery();
			if(rs.next()) {
			    newT_id+=rs.getLong(1);
			    newT_id+=1;
			}
		} catch (Exception e) {
			logger.debug(formatter.format(date)+" Some error while get values Transaction id and the error was "+e);
		}
        return newT_id;
    }
}

