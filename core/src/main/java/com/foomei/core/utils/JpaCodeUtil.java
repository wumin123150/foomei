package com.foomei.core.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.foomei.common.collection.MapUtil;
import com.foomei.common.io.FileUtil;
import com.foomei.common.io.JdbcUtil;
import com.foomei.common.text.FreeMarkerUtil;
import com.google.common.collect.Maps;

public class JpaCodeUtil {

  // Service模板路径
  private static String VM_SERVICE = "src/main/resources/template/Service.vm";
  private static String VM_DAO = "src/main/resources/template/Dao.vm";
  private static String VM_ENTITY = "src/main/resources/template/Entity.vm";

  public static final String TABLE_SQL = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '%s' AND table_name LIKE '%s';";
  public static final String COLUMN_SQL = "SELECT column_name, data_type FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = '%s' AND table_name = '%s';";

  public static Map<String, List<Map<String, String>>> getTableDefine(String jdbcDriver, String jdbcUrl,
                                                                      String jdbcUsername, String jdbcPassword, String database, String tablePrefix) throws Exception {

    Map<String, List<Map<String, String>>> tables = new LinkedHashMap<>();
    try {
      List<Map<String, String>> columns;

      // 查询定制前缀的所有表
      JdbcUtil jdbcUtil = new JdbcUtil(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
      List<Map> tableDefines = jdbcUtil.selectByParams(String.format(TABLE_SQL, database, tablePrefix + "%"),
              null);
      for (Map tableDefine : tableDefines) {
        String tableName = (String) tableDefine.get("TABLE_NAME");

        columns = new ArrayList<>();
        List<Map> columnDefines = jdbcUtil.selectByParams(String.format(COLUMN_SQL, database, tableName), null);
        for (Map columnDefine : columnDefines) {
          String columnName = (String) columnDefine.get("COLUMN_NAME");
          String dataType = (String) columnDefine.get("DATA_TYPE");
          columns.add(MapUtil.newHashMap(new String[]{"column", "type"}, new String[]{columnName,
                  dataType}));
        }

        tables.put(tableName, columns);
      }
      jdbcUtil.release();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return tables;
  }

  public static void generateEntity(Map<String, List<Map<String, String>>> tables, String tablePrefix,
                                    String packageName, String projectPath) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成Entity ==========");
    String entityPath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/entity";
    String tableName = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<String, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey();
      columns = table.getValue();
      if (hasId(columns)) {
        String model = toModel(tableName, tablePrefix);
        String entity = entityPath + "/" + model + ".java";
        // 生成entity
        File entityFile = new File(entity);
        if (!entityFile.exists()) {
          FileUtil.makesureParentDirExists(entityFile);

          Map<String, Object> params = Maps.newHashMap();
          params.put("tableName", tableName);
          params.put("package", packageName);
          params.put("model", model);
          params.put("idStrategy", getIdStrategy(columns));
          params.put("fields", toFields(columns));
          params.put("props", toProps(columns));
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_ENTITY)),
                  params);
          FileUtil.write(content, entityFile);
          System.out.println(entity);
        }
      }
    }
    System.out.println("========== 结束生成Entity ==========");
  }

  public static void generateDao(Map<String, List<Map<String, String>>> tables, String tablePrefix,
                                 String packageName, String projectPath) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成Dao ==========");
    String daoPath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/dao/jpa";
    String tableName = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<String, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey();
      columns = tables.get(tableName);
      if (hasId(columns)) {
        String model = toModel(tableName, tablePrefix);
        String dao = daoPath + "/" + model + "Dao.java";
        // 生成dao
        File daoFile = new File(dao);
        if (!daoFile.exists()) {
          FileUtil.makesureParentDirExists(daoFile);

          Map<String, String> params = Maps.newHashMap();
          params.put("package", packageName);
          params.put("model", model);
          params.put("idType", getIdType(columns));
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_DAO)), params);
          FileUtil.write(content, daoFile);
          System.out.println(dao);
        }
      }
    }
    System.out.println("========== 结束生成Dao ==========");
  }

  public static void generateService(Map<String, List<Map<String, String>>> tables, String tablePrefix,
                                     String packageName, String projectPath) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成Service ==========");
    String servicePath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/service";
    String tableName = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<String, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey();
      columns = tables.get(tableName);
      if (hasId(columns)) {
        String model = toModel(tableName, tablePrefix);
        String service = servicePath + "/" + model + "Service.java";
        // 生成service
        File serviceFile = new File(service);
        if (!serviceFile.exists()) {
          FileUtil.makesureParentDirExists(serviceFile);

          Map<String, String> params = Maps.newHashMap();
          params.put("package", packageName);
          params.put("model", model);
          params.put("idType", getIdType(columns));
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_SERVICE)), params);
          FileUtil.write(content, serviceFile);
          System.out.println(service);
        }
      }
    }
    System.out.println("========== 结束生成Service ==========");
  }

  public static void generateModule(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
                                    String database, String tablePrefix, String packageName, String projectPath) throws Exception {
    Map<String, List<Map<String, String>>> tables = getTableDefine(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword, database, tablePrefix);

    generateEntity(tables, tablePrefix, packageName, projectPath);
    generateDao(tables, tablePrefix, packageName, projectPath);
    generateService(tables, tablePrefix, packageName, projectPath);
  }

  public static void generateLocal(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
                                   String database, String packageBase) throws Exception {
    Map<String, List<Map<String, String>>> tables = getTableDefine(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword, database, "");

    Map<String, Map<String, List<Map<String, String>>>> modules = new LinkedHashMap<>();
    String tableName = null;
    Map<String, List<Map<String, String>>> temp = null;
    for (Map.Entry<String, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey();
      String module = StringUtils.substringBefore(tableName, "_");
      if (modules.containsKey(module)) {
        temp = modules.get(module);
        temp.put(tableName, tables.get(tableName));
      } else {
        modules.put(module, MapUtil.newHashMap(tableName, tables.get(tableName)));
      }
    }

    String localProjectPath = localProjectPath();
    for (Map.Entry<String, Map<String, List<Map<String, String>>>> module : modules.entrySet()) {
      System.out.println("========== 开始生成Module(" + module + ") ==========");
      generateEntity(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath);
      generateDao(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath);
      generateService(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath);
      System.out.println("========== 结束生成Module(" + module + ") ==========");
    }
  }

  public static void generateLocal(String packageBase) throws Exception {
    ResourceBundle resource = ResourceBundle.getBundle("application");
    String jdbcDriver = resource.getString("jdbc.driver");
    String jdbcUrl = resource.getString("jdbc.url");
    String jdbcUsername = resource.getString("jdbc.username");
    String jdbcPassword = resource.getString("jdbc.password");
    String database = StringUtils.substring(jdbcUrl, StringUtils.lastIndexOf(jdbcUrl, "/") + 1, StringUtils.indexOf(jdbcUrl, "?"));
    generateLocal(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword, database, packageBase);
  }

  public static void main(String[] args) throws Exception {
    generateLocal("com.foomei");
  }

  public static String localProjectPath() {
    String classPath = JpaCodeUtil.class.getResource("/").getPath();
    return StringUtils.substring(classPath, 1, classPath.length() - "target/classes/".length());
  }

  public static void localResource(String path) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
    System.out.println(resourceBundle.getString("jdbc.driver"));
  }

  public static boolean hasId(List<Map<String, String>> columnDefines) {
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      if (StringUtils.equalsIgnoreCase(columnName, "id")) {
        return true;
      }
    }
    return false;
  }

  public static String getIdStrategy(List<Map<String, String>> columnDefines) {
    return StringUtils.equals("Long", getIdType(columnDefines)) ? "Id" : "Uuid";
  }

  public static String getIdType(List<Map<String, String>> columnDefines) {
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      if (StringUtils.equalsIgnoreCase(columnName, "id")) {
        return toType(columnDefine.get("type"));
      }
    }
    return "String";
  }

  public static Map<String, String> toProps(List<Map<String, String>> columnDefines) {
    Map<String, String> fields = Maps.newLinkedHashMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      if (!StringUtils.equalsIgnoreCase(columnName, "id")) {
        fields.put(toField(columnName), StringUtils.upperCase(columnName));
      }
    }
    return fields;
  }

  public static Map<String, String> toFields(List<Map<String, String>> columnDefines) {
    Map<String, String> fields = Maps.newLinkedHashMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      String dataType = columnDefine.get("type");
      if (!StringUtils.equalsIgnoreCase(columnName, "id")) {
        fields.put(toField(columnName), toType(dataType));
      }
    }
    return fields;
  }

  public static String toModel(String tableName, String tablePrefix) {
    return StringUtils.capitalize(toCamel(StringUtils.substring(tableName, tablePrefix.length())));
  }

  public static String toField(String columnName) {
    return toCamel(columnName);
  }

  public static String toType(String dataType) {
    String type = "String";
    switch (dataType) {
      case "bigint":
        type = "Long";
        break;
      case "int":
      case "smallint":
      case "tinyint":
        type = "Integer";
        break;
      case "decimal":
        type = "java.math.BigDecimal";
        break;
      case "date":
      case "datetime":
        type = "java.util.Date";
        break;
      case "varchar":
      case "text":
    }
    return type;
  }

  public static String toCamel(String text) {
    Matcher matcher = Pattern.compile("_[a-z]").matcher(text);
    StringBuilder builder = new StringBuilder(text);
    for (int i = 0; matcher.find(); i++) {
      builder.replace(matcher.start() - i, matcher.end() - i, matcher.group().substring(1).toUpperCase());
    }
    if (Character.isUpperCase(builder.charAt(0))) {
      builder.replace(0, 1, String.valueOf(Character.toLowerCase(builder.charAt(0))));
    }
    return builder.toString();
  }

}
