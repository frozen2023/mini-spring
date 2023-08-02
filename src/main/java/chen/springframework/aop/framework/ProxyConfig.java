package chen.springframework.aop.framework;

import cn.hutool.core.lang.Assert;

public class ProxyConfig {

    private boolean proxyTargetClass = false;

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    public boolean isProxyTargetClass() {
        return this.proxyTargetClass;
    }

    public void copyFrom(ProxyConfig other) {
        Assert.notNull(other, "Other ProxyConfig object must not be null");
        this.proxyTargetClass = other.proxyTargetClass;
    }
}
