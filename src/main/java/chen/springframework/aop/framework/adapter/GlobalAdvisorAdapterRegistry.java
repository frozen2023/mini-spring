package chen.springframework.aop.framework.adapter;

public class GlobalAdvisorAdapterRegistry {

    private GlobalAdvisorAdapterRegistry() {
    }

    private static AdvisorAdapterRegistry instance = new DefaultAdvisorAdapterRegistry();

    public static AdvisorAdapterRegistry getInstance() {
        return instance;
    }

    static void reset() {
        instance = new DefaultAdvisorAdapterRegistry();
    }

}
