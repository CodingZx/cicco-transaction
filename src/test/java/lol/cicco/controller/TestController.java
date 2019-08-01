package lol.cicco.controller;

import lol.cicco.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/count1")
    public int findTest1Count(){
        return testService.findTest1Count();
    }

    @GetMapping("/count2")
    public int findTest2Count(){
        return testService.findTest2Count();
    }

    @PostMapping("/saveForNonTx")
    public boolean saveTestForNonTx(){
        try {
            testService.saveTestForNonTx();
        }catch (Exception ignore){}
        return true;
    }

    @PostMapping("/saveTestForSpringTx")
    public boolean saveTestForSpringTx(){
        try {
            testService.saveTestForSpringTx();
        }catch (Exception ignore){}
        return true;
    }

    @PostMapping("/saveTestForMoreTx")
    public boolean saveTestForMoreTx(){
        try {
            testService.saveTestForMoreTx();
        }catch (Exception ignore){}
        return true;
    }


    @PostMapping("/saveTestForNext")
    public boolean saveTestForNext(){
        try {
            testService.saveTestForNextService();
        }catch (Exception ignore){}
        return true;
    }

    @PostMapping("/saveTestForNextWithNewTx")
    public boolean saveTestForNextWithNewTx(){
        try {
            testService.saveTestForNextServiceWithNewTx();
        }catch (Exception ignore){}
        return true;
    }

    @PostMapping("/saveTestForOtherException")
    public boolean saveTestForOtherException(){
        try {
            testService.saveTestForOtherException();
        }catch (Exception ignore){}
        return true;
    }

    @PostMapping("/saveTestForOtherExceptionAndRollback")
    public boolean saveTestForOtherExceptionAndRollback(){
        try {
            testService.saveTestForOtherExceptionAndRollback();
        }catch (Exception ignore){}
        return true;
    }

    @DeleteMapping("/flushdb")
    public boolean deleteData(){
        testService.deleteData();
        return true;
    }
}
