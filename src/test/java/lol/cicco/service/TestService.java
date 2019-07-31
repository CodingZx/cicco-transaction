package lol.cicco.service;

import lol.cicco.mapper.test1.TestMapper1;
import lol.cicco.mapper.test2.TestMapper2;
import lol.cicco.transaction.annotation.MoreTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    private TestMapper1 testMapper1;
    @Autowired
    private TestMapper2 testMapper2;

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public void saveTestForSpringTx(){

        testMapper1.saveTest1("aaaa");
        testMapper2.saveTest2("bbbb");

        throw new RuntimeException();
    }

    public void saveTestForNonTx(){
        testMapper1.saveTest1("aaaa");
        testMapper2.saveTest2("bbbb");
        throw new RuntimeException();
    }

    @MoreTransactional(transactionManager = {"tx1","tx2"}, rollbackFor = Exception.class)
    public void saveTestForMoreTx(){
        testMapper1.saveTest1("aaaa");
        testMapper2.saveTest2("bbbb");
        throw new RuntimeException();
    }

    public int findTest1Count(){
        return testMapper1.findCount();
    }

    public int findTest2Count(){
        return testMapper2.findCount();
    }

    public void deleteData(){
        testMapper1.deleteData();
        testMapper2.deleteData();
    }

}
