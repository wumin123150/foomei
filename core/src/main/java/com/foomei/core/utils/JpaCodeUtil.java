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

import com.foomei.common.collection.ListUtil;
import com.foomei.common.collection.type.Pair;
import com.foomei.common.number.NumberUtil;
import com.foomei.common.text.TextValidator;
import com.google.common.collect.Lists;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;

import com.foomei.common.collection.MapUtil;
import com.foomei.common.io.FileUtil;
import com.foomei.common.io.JdbcUtil;
import com.foomei.common.text.FreeMarkerUtil;
import com.google.common.collect.Maps;

public class JpaCodeUtil {

  // 模板路径
  private static String VM_LIST_PAGE = "src/main/resources/template/ListPage.vm";
  private static String VM_ENDPOINT = "src/main/resources/template/Endpoint.vm";
  private static String VM_CONTROLLER = "src/main/resources/template/Controller.vm";
  private static String VM_SERVICE = "src/main/resources/template/Service.vm";
  private static String VM_DAO = "src/main/resources/template/Dao.vm";
  private static String VM_DTO = "src/main/resources/template/Dto.vm";
  private static String VM_ENTITY = "src/main/resources/template/Entity.vm";

  public static final String TABLE_SQL = "SELECT table_name, table_comment FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '%s' AND table_name LIKE '%s';";
  public static final String COLUMN_SQL = "SELECT column_name, data_type, column_comment FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = '%s' AND table_name = '%s';";

  public static Map<Pair<String, String>, List<Map<String, String>>> getTableDefine(String jdbcDriver, String jdbcUrl,
                                                                      String jdbcUsername, String jdbcPassword, String database, String tablePrefix) throws Exception {

    Map<Pair<String, String>, List<Map<String, String>>> tables = new LinkedHashMap<>();
    try {
      List<Map<String, String>> columns;

      // 查询定制前缀的所有表
      JdbcUtil jdbcUtil = new JdbcUtil(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
      List<Map> tableDefines = jdbcUtil.selectByParams(String.format(TABLE_SQL, database, tablePrefix + "%"),
              null);
      for (Map tableDefine : tableDefines) {
        String tableName = (String) tableDefine.get("TABLE_NAME");
        String tableComment = (String) tableDefine.get("TABLE_COMMENT");

        columns = new ArrayList<>();
        List<Map> columnDefines = jdbcUtil.selectByParams(String.format(COLUMN_SQL, database, tableName), null);
        for (Map columnDefine : columnDefines) {
          String columnName = (String) columnDefine.get("COLUMN_NAME");
          String columnComment = (String) columnDefine.get("COLUMN_COMMENT");
          String dataType = (String) columnDefine.get("DATA_TYPE");
          columns.add(MapUtil.newHashMap(new String[]{"column", "type", "comment"}, new String[]{columnName,
                  dataType, columnComment}));
        }

        tables.put(new Pair(tableName, tableComment), columns);
      }
      jdbcUtil.release();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return tables;
  }

  public static void generateEntity(Map<Pair<String, String>, List<Map<String, String>>> tables, String tablePrefix,
                                    String packageName, String projectPath, List<String> excludes) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成Entity ==========");
    String entityPath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/entity";
    String tableName = null;
    String tableComment = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey().getFirst();
      tableComment = table.getKey().getSecond();
      columns = table.getValue();
      if (hasColumn(columns, "id")) {
        String model = toModel(tableName, tablePrefix);
        String entity = entityPath + "/" + model + ".java";
        // 生成entity
        File entityFile = new File(entity);
        if (!entityFile.exists()) {
          FileUtil.makesureParentDirExists(entityFile);

          List<String> records = ListUtil.newArrayList();
          boolean hasCreateRecord = hasColumn(columns, "creator") && hasColumn(columns, "create_time");
          boolean hasUpdateRecord = hasColumn(columns, "updator") && hasColumn(columns, "update_time");
          boolean hasDeleteRecord = hasColumn(columns, "del_flag");

          if(hasCreateRecord) {
            records.add("CreateRecord");
          }
          if(hasUpdateRecord) {
            records.add("UpdateRecord");
          }
          if(hasDeleteRecord) {
            records.add("DeleteRecord");
          }

          Map<String, Object> params = Maps.newHashMap();
          params.put("tableName", tableName);
          params.put("comment", StringUtils.isNotEmpty(tableComment) ? tableComment : model);
          params.put("package", packageName);
          params.put("model", model);
          params.put("idStrategy", getIdStrategy(columns));
          params.put("fields", toFields(columns));
          params.put("props", toProps(columns));
          params.put("consts", toConsts(columns));
          params.put("hasImplement", hasCreateRecord || hasUpdateRecord || hasDeleteRecord);
          params.put("implements", StringUtils.join(records, ", "));
          params.put("hasCreateRecord", hasCreateRecord);
          params.put("hasUpdateRecord", hasUpdateRecord);
          params.put("hasDeleteRecord", hasDeleteRecord);
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_ENTITY)),
                  params);
          FileUtil.write(content, entityFile);
          System.out.println(entity);
        }
      }
    }
    System.out.println("========== 结束生成Entity ==========");
  }

  public static void generateDto(Map<Pair<String, String>, List<Map<String, String>>> tables, String tablePrefix,
                                    String packageName, String projectPath, List<String> excludes) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成Dto ==========");
    String entityPath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/dto";
    String tableName = null;
    String tableComment = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey().getFirst();
      tableComment = table.getKey().getSecond();
      columns = table.getValue();
      if (hasColumn(columns, "id")) {
        String model = toModel(tableName, tablePrefix);
        String entity = entityPath + "/" + model + "Dto.java";
        // 生成dto
        File entityFile = new File(entity);
        if (!entityFile.exists()) {
          FileUtil.makesureParentDirExists(entityFile);

          Map<String, Object> params = Maps.newHashMap();
          params.put("comment", StringUtils.isNotEmpty(tableComment) ? tableComment : model);
          params.put("package", packageName);
          params.put("model", model);
          params.put("fields", toFieldDtos(columns));
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_DTO)),
            params);
          FileUtil.write(content, entityFile);
          System.out.println(entity);
        }
      }
    }
    System.out.println("========== 结束生成Dto ==========");
  }

  public static void generateDao(Map<Pair<String, String>, List<Map<String, String>>> tables, String tablePrefix,
                                 String packageName, String projectPath, List<String> excludes) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成Dao ==========");
    String daoPath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/dao/jpa";
    String tableName = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey().getFirst();
      columns = table.getValue();
      if (hasColumn(columns, "id")) {
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

  public static void generateService(Map<Pair<String, String>, List<Map<String, String>>> tables, String tablePrefix,
                                     String packageName, String projectPath, List<String> excludes) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成Service ==========");
    String servicePath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/service";
    String tableName = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey().getFirst();
      columns = table.getValue();
      if (hasColumn(columns, "id")) {
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

  public static void generateController(Map<Pair<String, String>, List<Map<String, String>>> tables, String tablePrefix,
                                     String packageName, String projectPath, List<String> excludes) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成Controller ==========");
    String controllerPath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/web/controller";
    String tableName = null;
    String tableComment = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey().getFirst();
      tableComment = table.getKey().getSecond();
      columns = table.getValue();
      if (!excludes.contains(tableName) && hasColumn(columns, "id")) {
        String model = toModel(tableName, tablePrefix);
        String controller = controllerPath + "/" + model + "Controller.java";
        // controller
        File controllerFile = new File(controller);
        if (!controllerFile.exists()) {
          FileUtil.makesureParentDirExists(controllerFile);

          Map<String, String> params = Maps.newHashMap();
          params.put("folder", StringUtils.substringAfterLast(packageName, "."));
          params.put("comment", StringUtils.isNotEmpty(tableComment) ? tableComment : model);
          params.put("package", packageName);
          params.put("variable", toVariable(tableName, tablePrefix));
          params.put("model", model);
          params.put("idType", getIdType(columns));
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_CONTROLLER)), params);
          FileUtil.write(content, controllerFile);
          System.out.println(controller);
        }
      }
    }
    System.out.println("========== 结束生成Controller ==========");
  }

  public static void generateEndpoint(Map<Pair<String, String>, List<Map<String, String>>> tables, String tablePrefix,
                                        String packageName, String projectPath, List<String> excludes) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成Endpoint ==========");
    String endpointPath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/web/api";
    String tableName = null;
    String tableComment = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey().getFirst();
      tableComment = table.getKey().getSecond();
      columns = table.getValue();
      if (!excludes.contains(tableName) && hasColumn(columns, "id")) {
        String model = toModel(tableName, tablePrefix);
        String endpoint = endpointPath + "/" + model + "Endpoint.java";
        // endpoint
        File endpointFile = new File(endpoint);
        if (!endpointFile.exists()) {
          FileUtil.makesureParentDirExists(endpointFile);

          Map<String, String> params = Maps.newHashMap();
          params.put("comment", StringUtils.isNotEmpty(tableComment) ? tableComment : model);
          params.put("package", packageName);
          params.put("variable", toVariable(tableName, tablePrefix));
          params.put("model", model);
          params.put("idType", getIdType(columns));
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_ENDPOINT)), params);
          FileUtil.write(content, endpointFile);
          System.out.println(endpoint);
        }
      }
    }
    System.out.println("========== 结束生成Endpoint ==========");
  }

  public static void generateListPage(Map<Pair<String, String>, List<Map<String, String>>> tables, String tablePrefix,
                                      String packageName, String projectPath, List<String> excludes) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成ListPage ==========");
    String listPagePath = projectPath + "/src/main/webapp/WEB-INF/views/" + StringUtils.substringAfterLast(packageName, ".");
    String tableName = null;
    String tableComment = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey().getFirst();
      tableComment = table.getKey().getSecond();
      columns = table.getValue();
      if (!isExclude(excludes, tableName) && hasColumn(columns, "id")) {
        String variable = toVariable(tableName, tablePrefix);
        String listPage = listPagePath + "/" + variable + "/" + variable + "List.jsp";
        // listPage
        File listPageFile = new File(listPage);
        if (!listPageFile.exists()) {
          FileUtil.makesureParentDirExists(listPageFile);

          Map<String, Object> params = Maps.newHashMap();
          params.put("folder", StringUtils.substringAfterLast(packageName, "."));
          params.put("comment", StringUtils.isNotEmpty(tableComment) ? tableComment : toModel(tableName, tablePrefix));
          params.put("variable", toVariable(tableName, tablePrefix));
          params.put("fields", toFieldLists(columns));
          params.put("consts", toFieldConsts(columns));
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_LIST_PAGE)), params);
          FileUtil.write(content, listPageFile);
          System.out.println(listPage);
        }
      }
    }
    System.out.println("========== 结束生成ListPage ==========");
  }

  public static void generateModule(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
                                    String database, String tablePrefix, String packageName, String projectPath, Map<String, List<String>> excludes) throws Exception {
    Map<Pair<String, String>, List<Map<String, String>>> tables = getTableDefine(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword, database, tablePrefix);

    generateEntity(tables, tablePrefix, packageName, projectPath, excludes.get("entity"));
    generateDto(tables, tablePrefix, packageName, projectPath, excludes.get("dto"));
    generateDao(tables, tablePrefix, packageName, projectPath, excludes.get("dao"));
    generateService(tables, tablePrefix, packageName, projectPath, excludes.get("service"));
    generateController(tables, tablePrefix, packageName, projectPath, excludes.get("controller"));
    generateEndpoint(tables, tablePrefix, packageName, projectPath, excludes.get("endpoint"));
    generateListPage(tables, tablePrefix, packageName, projectPath, excludes.get("listpage"));
  }

  public static void generateLocal(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
                                   String database, String packageBase, Map<String, List<String>> excludes) throws Exception {
    Map<Pair<String, String>, List<Map<String, String>>> tables = getTableDefine(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword, database, "");

    Map<String, Map<Pair<String, String>, List<Map<String, String>>>> modules = new LinkedHashMap<>();
    String tableName = null;
    Map<Pair<String, String>, List<Map<String, String>>> temp = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableName = table.getKey().getFirst();
      String module = StringUtils.substringBefore(tableName, "_");
      if (modules.containsKey(module)) {
        temp = modules.get(module);
        temp.put(table.getKey(), table.getValue());
      } else {
        modules.put(module, MapUtil.newHashMap(table.getKey(), table.getValue()));
      }
    }

    String localProjectPath = localProjectPath();
    for (Map.Entry<String, Map<Pair<String, String>, List<Map<String, String>>>> module : modules.entrySet()) {
      System.out.println("========== 开始生成Module(" + module + ") ==========");
      generateEntity(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath, excludes.get("entity"));
      generateDto(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath, excludes.get("dto"));
      generateDao(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath, excludes.get("dao"));
      generateService(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath, excludes.get("service"));
      generateController(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath, excludes.get("controller"));
      generateEndpoint(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath, excludes.get("endpoint"));
      generateListPage(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath, excludes.get("listpage"));
      System.out.println("========== 结束生成Module(" + module + ") ==========");
    }
  }

  public static void generateLocal(String packageBase) throws Exception {
    ResourceBundle resource = ResourceBundle.getBundle("generator");
    String jdbcDriver = resource.getString("jdbc.driver");
    String jdbcUrl = resource.getString("jdbc.url");
    String jdbcUsername = resource.getString("jdbc.username");
    String jdbcPassword = resource.getString("jdbc.password");
    String database = StringUtils.substring(jdbcUrl, StringUtils.lastIndexOf(jdbcUrl, "/") + 1, StringUtils.indexOf(jdbcUrl, "?"));

    String excludeController = resource.getString("exclude.controller");
    String excludeEndpoint = resource.getString("exclude.endpoint");
    String excludeService = resource.getString("exclude.service");
    String excludeDao = resource.getString("exclude.dao");
    String excludeDto = resource.getString("exclude.dto");
    String excludeEntity = resource.getString("exclude.entity");
    String excludeListPage = resource.getString("exclude.listpage");
    Map<String, List<String>> excludes = MapUtil.newHashMap();
    excludes.put("controller", ListUtil.newArrayList(StringUtils.split(excludeController, ", ")));
    excludes.put("endpoint", ListUtil.newArrayList(StringUtils.split(excludeEndpoint, ", ")));
    excludes.put("service", ListUtil.newArrayList(StringUtils.split(excludeService, ", ")));
    excludes.put("dao", ListUtil.newArrayList(StringUtils.split(excludeDao, ", ")));
    excludes.put("dto", ListUtil.newArrayList(StringUtils.split(excludeDto, ", ")));
    excludes.put("entity", ListUtil.newArrayList(StringUtils.split(excludeEntity, ", ")));
    excludes.put("listpage", ListUtil.newArrayList(StringUtils.split(excludeListPage, ", ")));
    generateLocal(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword, database, packageBase, excludes);
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

  public static boolean hasColumn(List<Map<String, String>> columnDefines, String column) {
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      if (StringUtils.equalsIgnoreCase(columnName, column)) {
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

  public static List<String> toConsts(List<Map<String, String>> columnDefines) {
    List<String> fields = Lists.newArrayList();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      String consts = StringUtils.substringBetween(columnDefine.get("comment"), "(", ")");
      String dataType = toType(columnDefine.get("type"));
      if (!StringUtils.equalsIgnoreCase(columnName, "id") && !StringUtils.equalsIgnoreCase(columnName, "del_flag") && StringUtils.isNotEmpty(consts)) {
        String[] temp = StringUtils.split(consts, ", ");
        for (int i = 0; i < temp.length; i++) {
          String[] values = StringUtils.split(temp[i], ":");
          if(values.length == 2) {
            fields.add(String.format("public static final %s %s_%s = %s;//%s", dataType, StringUtils.upperCase(columnName), values[0], StringUtils.equals(dataType, "String") ? ("\"" + values[0] + "\"") : values[0], values[1]));
          }
        }
      }
    }
    return fields;
  }

  public static Map<String, Pair<String, String>> toFields(List<Map<String, String>> columnDefines) {
    Map<String, Pair<String, String>> fields = Maps.newLinkedHashMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      String comment = columnDefine.get("comment");
      String dataType = columnDefine.get("type");
      if (!StringUtils.equalsIgnoreCase(columnName, "id")) {
        fields.put(toField(columnName), new Pair(toType(dataType), comment));
      }
      if (StringUtils.equalsIgnoreCase(columnName, "del_flag")) {
        fields.put(toField(columnName), new Pair("Boolean", comment));
      }
    }
    return fields;
  }

  public static Map<String, Pair<String, String>> toFieldDtos(List<Map<String, String>> columnDefines) {
    Map<String, Pair<String, String>> fields = Maps.newLinkedHashMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      String comment = columnDefine.get("comment");
      String dataType = columnDefine.get("type");
      if (!StringUtils.equalsIgnoreCase(columnName, "del_flag")
        && !StringUtils.equalsIgnoreCase(columnName, "creator") && !StringUtils.equalsIgnoreCase(columnName, "create_time")
        && !StringUtils.equalsIgnoreCase(columnName, "updator") && !StringUtils.equalsIgnoreCase(columnName, "update_time")) {
        fields.put(toField(columnName), new Pair(toType(dataType), comment));
      }
    }
    return fields;
  }

  public static Map<String, Pair<String, String>> toFieldLists(List<Map<String, String>> columnDefines) {
    Map<String, Pair<String, String>> fields = Maps.newLinkedHashMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      String comment = StringUtils.substringBefore(columnDefine.get("comment"), "(");
      String dataType = columnDefine.get("type");
      if (!StringUtils.equalsIgnoreCase(columnName, "id") && !StringUtils.equalsIgnoreCase(columnName, "del_flag")
        && !StringUtils.equalsIgnoreCase(columnName, "creator") && !StringUtils.equalsIgnoreCase(columnName, "create_time")
        && !StringUtils.equalsIgnoreCase(columnName, "updator") && !StringUtils.equalsIgnoreCase(columnName, "update_time")) {
        fields.put(toField(columnName), new Pair(toType(dataType), comment));
      }
    }
    return fields;
  }

  public static Map<String, List<Pair<String, String>>> toFieldConsts(List<Map<String, String>> columnDefines) {
    Map<String, List<Pair<String, String>>> fields = Maps.newLinkedHashMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      String consts = StringUtils.substringBetween(columnDefine.get("comment"), "(", ")");
      String dataType = toType(columnDefine.get("type"));
      if (!StringUtils.equalsIgnoreCase(columnName, "id") && !StringUtils.equalsIgnoreCase(columnName, "del_flag") && StringUtils.isNotEmpty(consts)) {
        String[] temp = StringUtils.split(consts, ", ");
        for (int i = 0; i < temp.length; i++) {
          String[] values = StringUtils.split(temp[i], ":");
          if(values.length == 2) {
            if(fields.containsKey(toField(columnName))) {
              fields.get(toField(columnName)).add(new Pair<String, String>(values[0], values[1]));
            } else {
              fields.put(toField(columnName), ListUtil.newArrayList(new Pair<String, String>(values[0], values[1])));
            }
          }
        }
      }
    }
    return fields;
  }

  public static String toModel(String tableName, String tablePrefix) {
    return StringUtils.capitalize(toCamel(StringUtils.substring(tableName, tablePrefix.length())));
  }

  public static String toVariable(String tableName, String tablePrefix) {
    return toCamel(StringUtils.substring(tableName, tablePrefix.length()));
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

  public static boolean isExclude(List<String> excludes, String tableName) {
    for (String exclude : excludes) {
      if(StringUtils.endsWithIgnoreCase(exclude, tableName) || TextValidator.isMatch(Pattern.compile(toJavaPattern(exclude)), tableName)) {
        return true;
      }
    }
    return false;
  }

  private static String toJavaPattern(String pattern) {
    String result = "^";
    char metachar[] = { '$', '^', '[', ']', '(', ')', '{', '}', '|', '*', '+', '?', '\\' };
    for (int i = 0; i < pattern.length(); i++) {
      char ch = pattern.charAt(i);
      boolean isMeta = false;
      for (int j = 0; j < metachar.length; j++) {
        if (ch == '*') {
          result += "\\w*";
          isMeta = true;
          break;
        } else if (ch == metachar[j]) {
          result += "\\" + ch;
          isMeta = true;
          break;
        }
      }
      if (!isMeta) {
        result += ch;

      }
    }
    result += "$";
    return result;
  }

}
