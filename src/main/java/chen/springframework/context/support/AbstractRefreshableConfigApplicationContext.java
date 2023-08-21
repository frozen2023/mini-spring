package chen.springframework.context.support;

import cn.hutool.core.lang.Assert;

public abstract class AbstractRefreshableConfigApplicationContext extends AbstractRefreshableApplicationContext {

    private String[] configLocations;

    public void setConfigLocation(String location) {
        setConfigLocations(new String[]{location});
    }

    public void setConfigLocations(String[] locations) {
        if (locations != null) {
            Assert.noNullElements(locations, "Config locations must not be null");
            this.configLocations = locations;
        }
        else {
            this.configLocations = null;
        }
    }

    protected String[] getConfigLocations() {
        return (this.configLocations != null ? this.configLocations : getDefaultConfigLocations());
    }

    protected String[] getDefaultConfigLocations() {
        return null;
    }

}
