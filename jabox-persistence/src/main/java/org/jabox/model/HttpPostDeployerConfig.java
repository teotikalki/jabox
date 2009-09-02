package org.jabox.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(HttpPostDeployerPlugin.ID)
public class HttpPostDeployerConfig extends DeployerConfig
{
    public HttpPostDeployerConfig()
    {
        pluginId = HttpPostDeployerPlugin.ID;
    }

    @Column(nullable = false)
    public String url;
}