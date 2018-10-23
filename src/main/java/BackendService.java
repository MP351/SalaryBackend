import SocksService.SService;

public class BackendService {
    public static void main(String[] args) {

        Thread serverThread = new Thread(new SService());
        serverThread.start();
        //System.out.println(manager.writeIntoIncome((long) 1515618545, 1000));
        //System.out.println(manager.getSumForEnroll(new EnrollPeriod("2018-8")));
        //manager.proceed(25000, System.currentTimeMillis());
        //System.out.println(manager.getSumForEnroll(new EnrollPeriod("2018-2")));


    }
}
