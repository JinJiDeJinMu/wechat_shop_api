package template;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
@TransactionConfiguration(transactionManager="txManager",defaultRollback=true)
@Transactional//应用事务，这样测试就不会在数据库中留下痕迹
public class BaseJunit4Test {

    @Autowired
    public ${table.serviceName} ${table.entityPath}Service;

　　@Test 
　　public void add(){

　　}

　　@Test
　　public void delete(){

　　}

　　@Test
　　public void update(){

　　}

　　@Test
　　public void select(){

　　}
}