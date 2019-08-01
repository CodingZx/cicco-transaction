package lol.cicco.service;

import lol.cicco.mapper.test1.TestMapper1;
import lol.cicco.mapper.test2.TestMapper2;
import lol.cicco.transaction.annotation.MoreTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
public class NextService {
    @Autowired
    private TestMapper1 testMapper1;
    @Autowired
    private TestMapper2 testMapper2;

    @MoreTransactional(transactionManager = {"tx1","tx2"}, rollbackFor = Exception.class)
    public void saveTestForNextService(){
        testMapper1.saveTest1("aaaa");
        testMapper2.saveTest2("bbbb");
    }

    @MoreTransactional(transactionManager = {"tx1","tx2"}, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveTestForNextServiceWithNewTx(){
        testMapper1.saveTest1("aaaa");
        testMapper2.saveTest2("bbbb");
    }
}
