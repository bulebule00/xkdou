/**
 * Copyright (c) 2012 All Rights Reserved.
 */
package com.fredzhu.childredhome.core;

import com.fredzhu.childredhome.model.ChildrenModel;
import com.fredzhu.childredhome.util.PropertiesHelp;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

/**
 *                       
 * @Filename: CoreConfig.java
 *
 * @Description: 
 *
 * @Version: 1.0
 *
 * @Author: fred
 *
 * @Email: me@fengsage.com
 *
 *       
 * @History:<br>
 *<li>Author: fred</li>
 *<li>Date: 2013-1-4</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public class CoreConfig extends JFinalConfig {

    /**模板地址*/
    public static final String  TEMPATE_PATH = "/WEB-INF/templates";

    private static final String DEV_MODE     = PropertiesHelp.getProperty("dev.mode");

    /**
     * @param me
     * @see com.jfinal.config.JFinalConfig#configConstant(com.jfinal.config.Constants)
     */
    @Override
    public void configConstant(Constants me) {
        if (!Boolean.parseBoolean(DEV_MODE)) {
            me.setDevMode(true);
        }
        me.setViewType(ViewType.VELOCITY);
        me.setError404View(TEMPATE_PATH + "/404.html");
    }

    /**
     * @param me
     * @see com.jfinal.config.JFinalConfig#configRoute(com.jfinal.config.Routes)
     */
    @Override
    public void configRoute(Routes me) {
        new RoutesConfig().configRoute(me);
    }

    /**
     * @param me
     * @see com.jfinal.config.JFinalConfig#configPlugin(com.jfinal.config.Plugins)
     */
    @Override
    public void configPlugin(Plugins me) {
        C3p0Plugin c3p0Plugin = new C3p0Plugin(
            PropertiesHelp.getProperty("prop.jdbc.children.wrt.url"),
            PropertiesHelp.getProperty("prop.jdbc.children.wrt.username"),
            PropertiesHelp.getProperty("prop.jdbc.children.wrt.password"));
        me.add(c3p0Plugin);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
        me.add(arp);

        //
        arp.addMapping("xkd_child_info", ChildrenModel.class);
    }

    /**
     * @param me
     * @see com.jfinal.config.JFinalConfig#configInterceptor(com.jfinal.config.Interceptors)
     */
    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new PermissionBuild());//用于首次访问构建权限,此接口每次调用会消耗一些额外的资源,上线权限晚上后,可以注释此处
        me.add(new PermissionChecker());//权限检查
    }

    /**
     * @param me
     * @see com.jfinal.config.JFinalConfig#configHandler(com.jfinal.config.Handlers)
     */
    @Override
    public void configHandler(Handlers me) {
    }

}
