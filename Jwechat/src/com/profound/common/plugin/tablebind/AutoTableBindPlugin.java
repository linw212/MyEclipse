package com.profound.common.plugin.tablebind;

import java.io.File;
import java.util.List;

import javax.sql.DataSource;

import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;
import com.profound.common.annotations.TableBind;
import com.profound.common.kit.ClassSearcher;

public class AutoTableBindPlugin extends ActiveRecordPlugin {

	protected final Log logger = Log.getLog(getClass());

    @SuppressWarnings("rawtypes")
    private List<Class<? extends Model>> excludeClasses = Lists.newArrayList();
    private List<String> includeJars = Lists.newArrayList();
    private boolean autoScan = true;
    private boolean includeAllJarsInLib = false;
    private List<String> scanPackages = Lists.newArrayList();
    private INameStyle nameStyle;
    private String configName;
    private String classpath = PathKit.getRootClassPath();
    private String libDir = PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + "lib";

    public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider) {
        this(DbKit.MAIN_CONFIG_NAME, dataSourceProvider, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(String configName, IDataSourceProvider dataSourceProvider) {
        this(configName, dataSourceProvider, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, int transactionLevel) {
        this(DbKit.MAIN_CONFIG_NAME, dataSourceProvider, transactionLevel, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(String configName, IDataSourceProvider dataSourceProvider, int transactionLevel) {
        this(configName, dataSourceProvider, transactionLevel, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, INameStyle nameStyle) {
        super(DbKit.MAIN_CONFIG_NAME, dataSourceProvider);
        this.nameStyle = nameStyle;
        this.configName=DbKit.MAIN_CONFIG_NAME;
    }

    public AutoTableBindPlugin(String configName, IDataSourceProvider dataSourceProvider, INameStyle nameStyle) {
        super(configName, dataSourceProvider);
        this.nameStyle = nameStyle;
        this.configName=configName;
    }

    public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, int transactionLevel, INameStyle nameStyle) {
        super(DbKit.MAIN_CONFIG_NAME, dataSourceProvider, transactionLevel);
        this.nameStyle = nameStyle;
        this.configName=DbKit.MAIN_CONFIG_NAME;
    }

    public AutoTableBindPlugin(String configName, IDataSourceProvider dataSourceProvider, int transactionLevel, INameStyle nameStyle) {
        super(configName, dataSourceProvider, transactionLevel);
        this.nameStyle = nameStyle;
        this.configName=configName;
    }

    public AutoTableBindPlugin(DataSource dataSource) {
        this(DbKit.MAIN_CONFIG_NAME, dataSource, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(String configName, DataSource dataSource) {
        this(configName, dataSource, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(DataSource dataSource, int transactionLevel) {
        this(DbKit.MAIN_CONFIG_NAME, dataSource, transactionLevel, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(String configName, DataSource dataSource, int transactionLevel) {
        this(configName, dataSource, transactionLevel, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(DataSource dataSource, INameStyle nameStyle) {
        super(DbKit.MAIN_CONFIG_NAME, dataSource);
        this.nameStyle = nameStyle;
        this.configName=DbKit.MAIN_CONFIG_NAME;
    }

    public AutoTableBindPlugin(String configName, DataSource dataSource, INameStyle nameStyle) {
        super(configName, dataSource);
        this.nameStyle = nameStyle;
        this.configName=configName;
    }

    public AutoTableBindPlugin(DataSource dataSource, int transactionLevel, INameStyle nameStyle) {
        super(DbKit.MAIN_CONFIG_NAME, dataSource, transactionLevel);
        this.nameStyle = nameStyle;
        this.configName=DbKit.MAIN_CONFIG_NAME;
    }

    public AutoTableBindPlugin(String configName, DataSource dataSource, int transactionLevel, INameStyle nameStyle) {
        super(configName, dataSource, transactionLevel);
        this.nameStyle = nameStyle;
        this.configName=configName;
    }

    /**
     * 添加需要扫描的包，默认为扫描所有包
     *
     * @param packages
     * @return
     */
    public AutoTableBindPlugin addScanPackages(String... packages) {
        for (String pkg : packages) {
            scanPackages.add(pkg);
        }
        return this;
    }

    @SuppressWarnings("rawtypes")
	public AutoTableBindPlugin addExcludeClasses(Class<? extends Model>... clazzes) {
        for (Class<? extends Model> clazz : clazzes) {
            excludeClasses.add(clazz);
        }
        return this;
    }

    @SuppressWarnings("rawtypes")
    public AutoTableBindPlugin addExcludeClasses(List<Class<? extends Model>> clazzes) {
        if (clazzes != null) {
            excludeClasses.addAll(clazzes);
        }
        return this;
    }

    public AutoTableBindPlugin addJars(List<String> jars) {
        if (jars != null) {
            includeJars.addAll(jars);
        }
        return this;
    }

    public AutoTableBindPlugin addJars(String... jars) {
        if (jars != null) {
            for (String jar : jars) {
                includeJars.add(jar);
            }
        }
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public boolean start() {
        List<Class<? extends Model>> modelClasses = ClassSearcher.of(Model.class).libDir(libDir).classpath(classpath)
                .scanPackages(scanPackages).injars(includeJars).includeAllJarsInLib(includeAllJarsInLib).search();
        TableBind tb;
        for (Class modelClass : modelClasses) {
            if (excludeClasses.contains(modelClass)) {
                continue;
            }
            tb = (TableBind) modelClass.getAnnotation(TableBind.class);
            String tableName;
            if (tb == null) {
                if (!autoScan) {
                    continue;
                }
                tableName = nameStyle.name(modelClass.getSimpleName());
                this.addMapping(tableName, modelClass);
                logger.debug(configName + " addMapping(" + tableName + ", " + modelClass.getName() + ")");
            } else {
                String tbConfName = tb.configName();
                if (StrKit.notBlank(tbConfName) && !tbConfName.equals(configName)) continue;
                if (StrKit.isBlank(tbConfName) && !configName.equals("main")) continue;
                tableName = tb.tableName();
                if (StrKit.notBlank(tb.pkName())) {
                    this.addMapping(tableName, tb.pkName(), modelClass);
                    logger.debug(configName + " addMapping(" + tableName + ", " + tb.pkName() + "," + modelClass.getName() + ")");
                } else {
                    this.addMapping(tableName, modelClass);
                    logger.debug(configName + " addMapping(" + tableName + ", " + modelClass.getName() + ")");
                }
            }
        }
        return super.start();
    }

    @Override
    public boolean stop() {
        return super.stop();
    }

    public AutoTableBindPlugin autoScan(boolean autoScan) {
        this.autoScan = autoScan;
        return this;
    }

    public AutoTableBindPlugin classpath(String classpath) {
        this.classpath = classpath;
        return this;
    }

    public AutoTableBindPlugin libDir(String libDir) {
        this.libDir = libDir;
        return this;
    }
    public AutoTableBindPlugin includeAllJarsInLib(boolean includeAllJarsInLib) {
        this.includeAllJarsInLib = includeAllJarsInLib;
        return this;
    }
}
