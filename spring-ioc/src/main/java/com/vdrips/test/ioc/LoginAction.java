package com.vdrips.test.ioc;

/**
 * Created by baixf on 2017/3/15.
 * 定义了一个Logic 类型的变量 logic, 在LoginAction并没有对logic 进行实例化，而只有他对应的setter/getter方法，因为我们这里使用的是Spring的依赖注入的方式
 */
public class LoginAction {
    private ILogic logic;

    public LoginAction(){

    }
    public LoginAction(ILogic logic) {
        this.logic = logic;
    }

    public void execute() {
        String name = logic.getName();
        System.out.println("my name is:" + name);
    }

    public ILogic getLogic() {
        return logic;
    }

    public void setLogic(ILogic logic) {
        this.logic = logic;
    }

    public void executeInterface() {
        try {
            Object obj = Class.forName("com.vdrips.test.ioc.LogicImpl")
                    .newInstance();
            logic = (ILogic) obj;
            String name = logic.getName();
            System.out.print("My Name Is " + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
