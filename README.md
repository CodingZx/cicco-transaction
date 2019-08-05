# cicco-transaction
扩展SpringBoot事务, 支持多数据源事务控制

### 使用方式
- 使用MoreTransactional注解
```java
    @MoreTransactional(transactionManager = {"tx1","tx2"}, rollbackFor = Exception.class)
    public void save(){
        // do something...
    }
  
```
### 说明
使用SpringAOP进行扩展, 事务处理逻辑见[TransactionProcessor](https://github.com/CodingZx/cicco-transaction/blob/master/src/main/java/lol/cicco/transaction/processor/TransactionProcessor.java)

#### 示例详见[单元测试](https://github.com/CodingZx/cicco-transaction/tree/master/src/test)