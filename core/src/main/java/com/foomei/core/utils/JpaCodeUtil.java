package com.foomei.core.utils;

import com.foomei.common.collection.ListUtil;
import com.foomei.common.collection.MapUtil;
import com.foomei.common.collection.type.Pair;
import com.foomei.common.io.FileUtil;
import com.foomei.common.io.JdbcUtil;
import com.foomei.common.text.FreeMarkerUtil;
import com.foomei.common.text.TextValidator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JpaCodeUtil {

  // 模板路径
  private static String VM_FORM_PAGE = "src/main/resources/template/FormPage_";
  private static String VM_LIST_PAGE = "src/main/resources/template/ListPage_";
  private static String VM_ENDPOINT = "src/main/resources/template/Endpoint_";
  private static String VM_CONTROLLER = "src/main/resources/template/Controller_";
  private static String VM_SERVICE = "src/main/resources/template/Service.vm";
  private static String VM_DAO = "src/main/resources/template/Dao.vm";
  private static String VM_DTO = "src/main/resources/template/Dto.vm";
  private static String VM_ENTITY = "src/main/resources/template/Entity.vm";

  public static final String TABLE_SQL = "SELECT table_name, table_comment FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '%s' AND table_name LIKE '%s';";
  public static final String COLUMN_SQL = "SELECT column_name, data_type, column_comment, character_maximum_length, is_nullable, column_key FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = '%s' AND table_name = '%s';";

  public static Map<Pair<String, String>, List<Map<String, String>>> getTableDefine(String jdbcDriver, String jdbcUrl,
                                                                      String jdbcUsername, String jdbcPassword, String database, String tablePrefix) throws Exception {

    Map<Pair<String, String>, List<Map<String, String>>> tables = new LinkedHashMap<>();
    try {
      List<Map<String, String>> columns;

      // 查询定制前缀的所有表
      JdbcUtil jdbcUtil = new JdbcUtil(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
      List<Map> tableDefines = jdbcUtil.selectByParams(String.format(TABLE_SQL, database, tablePrefix + "%"), null);
      for (Map tableDefine : tableDefines) {
        String tableName = (String) tableDefine.get("TABLE_NAME");
        String tableComment = (String) tableDefine.get("TABLE_COMMENT");

        columns = new ArrayList<>();
        List<Map> columnDefines = jdbcUtil.selectByParams(String.format(COLUMN_SQL, database, tableName), null);
        for (Map columnDefine : columnDefines) {
          String columnName = (String) columnDefine.get("COLUMN_NAME");
          String columnComment = (String) columnDefine.get("COLUMN_COMMENT");
          String dataType = (String) columnDefine.get("DATA_TYPE");
          String stringLength = String.valueOf(columnDefine.get("CHARACTER_MAXIMUM_LENGTH"));
          String isNull = (String) columnDefine.get("IS_NULLABLE");
          String columnKey = (String) columnDefine.get("COLUMN_KEY");
          columns.add(MapUtil.newHashMap(new String[]{"column", "type", "comment", "stringLength", "isNull", "key"}, new String[]{columnName,
                  dataType, columnComment, stringLength, isNull, columnKey}));
        }

        tables.put(new Pair<String, String>(tableName, tableComment), columns);
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
    String tableCode = null;
    String tableName = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableCode = table.getKey().getFirst();
      tableName = table.getKey().getSecond();
      columns = table.getValue();
      if (!isExclude(excludes, tableCode) && hasColumn(columns, "id")) {
        String model = toModel(tableCode, tablePrefix);
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

          Map<String, Object> params = MapUtil.newHashMap();
          params.put("tableCode", tableCode);
          params.put("tableName", StringUtils.isNotEmpty(tableName) ? tableName : model);
          params.put("package", packageName);
          params.put("model", model);
          params.put("idStrategy", getIdStrategy(columns));
          params.put("fields", toFields(columns));
          params.put("props", toProps(columns));
          params.put("consts", toConsts(columns));
          params.put("hasImplement", hasCreateRecord || hasUpdateRecord || hasDeleteRecord);
          params.put("implements", StringUtils.join(records, ", "));
          params.put("hasCreateRecord", hasCreateRecord);
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
    String tableCode = null;
    String tableName = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableCode = table.getKey().getFirst();
      tableName = table.getKey().getSecond();
      columns = table.getValue();
      if (!isExclude(excludes, tableCode) && hasColumn(columns, "id")) {
        String model = toModel(tableCode, tablePrefix);
        String entity = entityPath + "/" + model + "Dto.java";
        // 生成dto
        File entityFile = new File(entity);
        if (!entityFile.exists()) {
          FileUtil.makesureParentDirExists(entityFile);

          Map<String, Object> params = MapUtil.newHashMap();
          params.put("tableName", StringUtils.isNotEmpty(tableName) ? tableName : model);
          params.put("package", packageName);
          params.put("model", model);
          params.put("fields", toFieldDtos(columns));
          params.put("fieldNotBlanks", toFieldNotBlanks(columns));
          params.put("fieldSizes", toFieldSizes(columns));
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
    String tableCode = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableCode = table.getKey().getFirst();
      columns = table.getValue();
      if (!isExclude(excludes, tableCode) && hasColumn(columns, "id")) {
        String model = toModel(tableCode, tablePrefix);
        String dao = daoPath + "/" + model + "Dao.java";
        // 生成dao
        File daoFile = new File(dao);
        if (!daoFile.exists()) {
          FileUtil.makesureParentDirExists(daoFile);

          Map<String, String> params = MapUtil.newHashMap();
          params.put("package", packageName);
          params.put("variable", toVariable(tableCode, tablePrefix));
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
    String tableCode = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableCode = table.getKey().getFirst();
      columns = table.getValue();
      if (!isExclude(excludes, tableCode) && hasColumn(columns, "id")) {
        String model = toModel(tableCode, tablePrefix);
        String service = servicePath + "/" + model + "Service.java";
        // 生成service
        File serviceFile = new File(service);
        if (!serviceFile.exists()) {
          FileUtil.makesureParentDirExists(serviceFile);

          Map<String, String> params = MapUtil.newHashMap();
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
                                     String packageName, String projectPath, String theme, List<String> excludes) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成Controller ==========");
    String controllerPath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/web/controller";
    String tableCode = null;
    String tableName = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableCode = table.getKey().getFirst();
      tableName = table.getKey().getSecond();
      columns = table.getValue();
      if (!isExclude(excludes, tableCode) && hasColumn(columns, "id")) {
        String model = toModel(tableCode, tablePrefix);
        String controller = controllerPath + "/" + model + "Controller.java";
        // controller
        File controllerFile = new File(controller);
        if (!controllerFile.exists()) {
          FileUtil.makesureParentDirExists(controllerFile);

          Map<String, String> params = MapUtil.newHashMap();
          params.put("folder", StringUtils.substringAfterLast(packageName, "."));
          params.put("tableName", StringUtils.isNotEmpty(tableName) ? tableName : model);
          params.put("package", packageName);
          params.put("variable", toVariable(tableCode, tablePrefix));
          params.put("model", model);
          params.put("idType", getIdType(columns));
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_CONTROLLER + theme + ".vm")), params);
          FileUtil.write(content, controllerFile);
          System.out.println(controller);
        }
      }
    }
    System.out.println("========== 结束生成Controller ==========");
  }

  public static void generateEndpoint(Map<Pair<String, String>, List<Map<String, String>>> tables, String tablePrefix,
                                        String packageName, String projectPath, String theme, List<String> excludes) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成Endpoint ==========");
    String endpointPath = projectPath + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/web/api";
    String tableCode = null;
    String tableName = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableCode = table.getKey().getFirst();
      tableName = table.getKey().getSecond();
      columns = table.getValue();
      if (!isExclude(excludes, tableCode) && hasColumn(columns, "id")) {
        String model = toModel(tableCode, tablePrefix);
        String endpoint = endpointPath + "/" + model + "Endpoint.java";
        // endpoint
        File endpointFile = new File(endpoint);
        if (!endpointFile.exists()) {
          FileUtil.makesureParentDirExists(endpointFile);

          Map<String, String> params = MapUtil.newHashMap();
          params.put("tableName", StringUtils.isNotEmpty(tableName) ? tableName : model);
          params.put("package", packageName);
          params.put("variable", toVariable(tableCode, tablePrefix));
          params.put("model", model);
          params.put("idType", getIdType(columns));
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_ENDPOINT + theme + ".vm")), params);
          FileUtil.write(content, endpointFile);
          System.out.println(endpoint);
        }
      }
    }
    System.out.println("========== 结束生成Endpoint ==========");
  }

  public static void generateListPage(Map<Pair<String, String>, List<Map<String, String>>> tables, String tablePrefix,
                                      String packageName, String projectPath, String theme, List<String> excludes) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成ListPage ==========");
    String listPagePath = projectPath + "/src/main/webapp/WEB-INF/views/" + StringUtils.substringAfterLast(packageName, ".");
    String tableCode = null;
    String tableName = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableCode = table.getKey().getFirst();
      tableName = table.getKey().getSecond();
      columns = table.getValue();
      if (!isExclude(excludes, tableCode) && hasColumn(columns, "id")) {
        String variable = toVariable(tableCode, tablePrefix);
        String listPage = listPagePath + "/" + variable + "/" + variable + "List.jsp";
        // listPage
        File listPageFile = new File(listPage);
        if (!listPageFile.exists()) {
          FileUtil.makesureParentDirExists(listPageFile);

          Map<String, Object> params = MapUtil.newHashMap();
          params.put("folder", StringUtils.substringAfterLast(packageName, "."));
          params.put("tableName", StringUtils.isNotEmpty(tableName) ? tableName : toModel(tableCode, tablePrefix));
          params.put("variable", toVariable(tableCode, tablePrefix));
          params.put("fields", toFieldLists(columns));
          params.put("fieldConsts", toFieldConsts(columns));
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_LIST_PAGE + theme + ".vm")), params);
          FileUtil.write(content, listPageFile);
          System.out.println(listPage);
        }
      }
    }
    System.out.println("========== 结束生成ListPage ==========");
  }

  public static void generateFormPage(Map<Pair<String, String>, List<Map<String, String>>> tables, String tablePrefix,
                                      String packageName, String projectPath, String theme, List<String> excludes) throws IOException {
    String localProjectPath = localProjectPath();

    System.out.println("========== 开始生成FormPage ==========");
    String formPagePath = projectPath + "/src/main/webapp/WEB-INF/views/" + StringUtils.substringAfterLast(packageName, ".");
    String tableCode = null;
    String tableName = null;
    List<Map<String, String>> columns = null;
    for (Map.Entry<Pair<String, String>, List<Map<String, String>>> table : tables.entrySet()) {
      tableCode = table.getKey().getFirst();
      tableName = table.getKey().getSecond();
      columns = table.getValue();
      if (!isExclude(excludes, tableCode) && hasColumn(columns, "id")) {
        String variable = toVariable(tableCode, tablePrefix);
        String formPage = formPagePath + "/" + variable + "/" + variable + "Form.jsp";
        // formPage
        File formPageFile = new File(formPage);
        if (!formPageFile.exists()) {
          FileUtil.makesureParentDirExists(formPageFile);

          Map<String, Object> params = MapUtil.newHashMap();
          params.put("folder", StringUtils.substringAfterLast(packageName, "."));
          params.put("tableName", StringUtils.isNotEmpty(tableName) ? tableName : toModel(tableCode, tablePrefix));
          params.put("variable", toVariable(tableCode, tablePrefix));
          params.put("fields", toFieldLists(columns));
          params.put("fieldNotBlanks", toFieldNotBlanks(columns));
          params.put("fieldSizes", toFieldSizes(columns));
          params.put("fieldConsts", toFieldConsts(columns));
          params.put("fieldSwitchs", toFieldSwitchs(columns));
          params.put("fieldLayVerifys", toFieldLayVerifys(columns));
          params.put("fieldValidateRules", toFieldValidateRules(columns));
          String content = FreeMarkerUtil.renderString(FileUtil.toString(new File(localProjectPath, VM_FORM_PAGE + theme + ".vm")), params);
          FileUtil.write(content, formPageFile);
          System.out.println(formPage);
        }
      }
    }
    System.out.println("========== 结束生成FormPage ==========");
  }

  public static void generateModule(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
                                    String database, String tablePrefix, String packageName, String projectPath, String theme, Map<String, List<String>> excludes) throws Exception {
    Map<Pair<String, String>, List<Map<String, String>>> tables = getTableDefine(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword, database, tablePrefix);

    generateEntity(tables, tablePrefix, packageName, projectPath, excludes.get("entity"));
    generateDto(tables, tablePrefix, packageName, projectPath, excludes.get("dto"));
    generateDao(tables, tablePrefix, packageName, projectPath, excludes.get("dao"));
    generateService(tables, tablePrefix, packageName, projectPath, excludes.get("service"));
    generateController(tables, tablePrefix, packageName, projectPath, theme, excludes.get("controller"));
    generateEndpoint(tables, tablePrefix, packageName, projectPath, theme, excludes.get("endpoint"));
    generateListPage(tables, tablePrefix, packageName, projectPath, theme, excludes.get("listpage"));
    generateFormPage(tables, tablePrefix, packageName, projectPath, theme, excludes.get("formpage"));
  }

  public static void generateLocal(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
                                   String database, String packageBase, String theme, Map<String, List<String>> excludes) throws Exception {
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
      generateController(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath, theme, excludes.get("controller"));
      generateEndpoint(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath, theme, excludes.get("endpoint"));
      generateListPage(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath, theme, excludes.get("listpage"));
      generateFormPage(module.getValue(), module.getKey() + "_", packageBase + "." + module.getKey(), localProjectPath, theme, excludes.get("formpage"));
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

    String theme = resource.getString("system.theme");
    String excludeController = resource.getString("exclude.controller");
    String excludeEndpoint = resource.getString("exclude.endpoint");
    String excludeService = resource.getString("exclude.service");
    String excludeDao = resource.getString("exclude.dao");
    String excludeDto = resource.getString("exclude.dto");
    String excludeEntity = resource.getString("exclude.entity");
    String excludeListPage = resource.getString("exclude.listpage");
    String excludeFormPage = resource.getString("exclude.formpage");
    Map<String, List<String>> excludes = MapUtil.newHashMap();
    excludes.put("controller", ListUtil.newArrayList(StringUtils.split(excludeController, ", ")));
    excludes.put("endpoint", ListUtil.newArrayList(StringUtils.split(excludeEndpoint, ", ")));
    excludes.put("service", ListUtil.newArrayList(StringUtils.split(excludeService, ", ")));
    excludes.put("dao", ListUtil.newArrayList(StringUtils.split(excludeDao, ", ")));
    excludes.put("dto", ListUtil.newArrayList(StringUtils.split(excludeDto, ", ")));
    excludes.put("entity", ListUtil.newArrayList(StringUtils.split(excludeEntity, ", ")));
    excludes.put("listpage", ListUtil.newArrayList(StringUtils.split(excludeListPage, ", ")));
    excludes.put("formpage", ListUtil.newArrayList(StringUtils.split(excludeFormPage, ", ")));
    generateLocal(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword, database, packageBase, theme, excludes);
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
      String columnCode = columnDefine.get("column");
      if (StringUtils.equalsIgnoreCase(columnCode, column)) {
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
      String columnCode = columnDefine.get("column");
      if (StringUtils.equalsIgnoreCase(columnCode, "id")) {
        return toDataType(columnDefine.get("type"));
      }
    }
    return "String";
  }

  public static Map<String, String> toProps(List<Map<String, String>> columnDefines) {
    Map<String, String> fields = MapUtil.newLinkedMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnCode = columnDefine.get("column");
      if (!StringUtils.equalsIgnoreCase(columnCode, "id")) {
        fields.put(toField(columnCode), StringUtils.upperCase(columnCode));
      }
    }
    return fields;
  }

  public static List<String> toConsts(List<Map<String, String>> columnDefines) {
    List<String> fields = ListUtil.newArrayList();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnCode = columnDefine.get("column");
      String consts = getText(columnDefine.get("comment"), new Pair<String, String>("{", "}"));
      String dataType = toDataType(columnDefine.get("type"));
      String unit = getText(columnDefine.get("comment"), new Pair<String, String>("[", "]"), new Pair<String, String>("【", "】"));
      if (!StringUtils.equalsIgnoreCase(columnCode, "id") && !StringUtils.equalsIgnoreCase(columnCode, "del_flag") && !StringUtils.equalsIgnoreCase(unit, "switch") && StringUtils.isNotEmpty(consts)) {
        String[] temp = StringUtils.split(consts, ", ");
        for (int i = 0; i < temp.length; i++) {
          String[] values = StringUtils.split(temp[i], ":");
          if(values.length == 2) {
            fields.add(String.format("public static final %s %s_%s = %s;//%s", dataType, StringUtils.upperCase(columnCode), values[0], StringUtils.equals(dataType, "String") ? ("\"" + values[0] + "\"") : values[0], values[1]));
          }
        }
      }
    }
    return fields;
  }

  public static Map<String, Pair<String, String>> toFields(List<Map<String, String>> columnDefines) {
    Map<String, Pair<String, String>> fields = MapUtil.newLinkedMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnCode = columnDefine.get("column");
      String comment = columnDefine.get("comment");
      String dataType = toDataType(columnDefine.get("type"));
      String unit = getText(columnDefine.get("comment"), new Pair<String, String>("[", "]"), new Pair<String, String>("【", "】"));
      if (!StringUtils.equalsIgnoreCase(columnCode, "id")) {
        fields.put(toField(columnCode), new Pair<String, String>(dataType, comment));
        if (StringUtils.equalsIgnoreCase(unit, "switch")) {
          fields.put(toField(columnCode), new Pair<String, String>("Boolean", comment));
        }
      }
    }
    return fields;
  }

  public static Map<String, Pair<String, String>> toFieldDtos(List<Map<String, String>> columnDefines) {
    Map<String, Pair<String, String>> fields = MapUtil.newLinkedMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnCode = columnDefine.get("column");
      String comment = columnDefine.get("comment");
      String dataType = toDataType(columnDefine.get("type"));
      String unit = getText(columnDefine.get("comment"), new Pair<String, String>("[", "]"), new Pair<String, String>("【", "】"));
      if (!StringUtils.equalsIgnoreCase(columnCode, "del_flag")
        && !StringUtils.equalsIgnoreCase(columnCode, "creator") && !StringUtils.equalsIgnoreCase(columnCode, "create_time")
        && !StringUtils.equalsIgnoreCase(columnCode, "updator") && !StringUtils.equalsIgnoreCase(columnCode, "update_time")) {
        fields.put(toField(columnCode), new Pair<String, String>(dataType, comment));
        if (StringUtils.equals(unit, "switch")) {
          fields.put(toField(columnCode), new Pair<String, String>("Boolean", comment));
        }
      }
    }
    return fields;
  }

  public static Map<String, Pair<String, String>> toFieldLists(List<Map<String, String>> columnDefines) {
    Map<String, Pair<String, String>> fields = MapUtil.newLinkedMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnCode = columnDefine.get("column");
      String columnName = getName(columnDefine.get("comment"));
      String unit = getText(columnDefine.get("comment"), new Pair<String, String>("[", "]"), new Pair<String, String>("【", "】"));
      if(StringUtils.isEmpty(unit)) {
        String consts = getText(columnDefine.get("comment"), new Pair<String, String>("{", "}"));
        if(StringUtils.isNotEmpty(consts)) {
          unit = "select";
        }
      }
      if(StringUtils.isEmpty(unit)) {
        String columnLength = columnDefine.get("stringLength");
        if(StringUtils.isNotEmpty(columnLength) && Integer.valueOf(columnLength) > 100) {
          unit = "textarea";
        }
      }
      if(StringUtils.isEmpty(unit)) {
        unit = toUnit(columnDefine.get("type"));
      }
      if (!StringUtils.equalsIgnoreCase(columnCode, "id") && !StringUtils.equalsIgnoreCase(columnCode, "del_flag")
        && !StringUtils.equalsIgnoreCase(columnCode, "creator") && !StringUtils.equalsIgnoreCase(columnCode, "create_time")
        && !StringUtils.equalsIgnoreCase(columnCode, "updator") && !StringUtils.equalsIgnoreCase(columnCode, "update_time")) {
        fields.put(toField(columnCode), new Pair<String, String>(unit, columnName));
      }
    }
    return fields;
  }

  public static Map<String, List<Pair<String, String>>> toFieldConsts(List<Map<String, String>> columnDefines) {
    Map<String, List<Pair<String, String>>> fields = MapUtil.newLinkedMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      String consts = getText(columnDefine.get("comment"), new Pair<String, String>("{", "}"));
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

  public static Map<String, Pair<String, String>> toFieldSwitchs(List<Map<String, String>> columnDefines) {
    Map<String, Pair<String, String>> fields = MapUtil.newLinkedMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnName = columnDefine.get("column");
      String consts = getText(columnDefine.get("comment"), new Pair<String, String>("{", "}"));
      String unit = getText(columnDefine.get("comment"), new Pair<String, String>("[", "]"), new Pair<String, String>("【", "】"));
      if (!StringUtils.equalsIgnoreCase(columnName, "id") && !StringUtils.equalsIgnoreCase(columnName, "del_flag") && StringUtils.equalsIgnoreCase(unit, "switch") && StringUtils.isNotEmpty(consts)) {
        String[] temp = StringUtils.split(consts, ", ");
        if(temp.length == 2) {
          String negative = "否";
          String plus = "是";
          for (int i = 0; i < temp.length; i++) {
            String[] values = StringUtils.split(temp[i], ":");
            if(values.length == 2) {
              if(StringUtils.equalsIgnoreCase(values[0], "0") || StringUtils.equalsIgnoreCase(values[0], "false")) {
                negative = values[1];
              }
              if(StringUtils.equalsIgnoreCase(values[0], "1") || StringUtils.equalsIgnoreCase(values[0], "true")) {
                plus = values[1];
              }
            }
          }
          fields.put(toField(columnName), new Pair<String, String>(plus, negative));
        }
      }
    }
    return fields;
  }

  public static Map<String, String> toFieldNotBlanks(List<Map<String, String>> columnDefines) {
    Map<String, String> fields = MapUtil.newLinkedMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnCode = columnDefine.get("column");
      String columnName = getName(columnDefine.get("comment"));
      String isNull = columnDefine.get("isNull");
      if (!StringUtils.equalsIgnoreCase(columnCode, "id") && StringUtils.equalsIgnoreCase(isNull, "NO")) {
        fields.put(toField(columnCode), columnName);
      }
    }
    return fields;
  }

  public static Map<String, Pair<Long, String>> toFieldSizes(List<Map<String, String>> columnDefines) {
    Map<String, Pair<Long, String>> fields = MapUtil.newLinkedMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnCode = columnDefine.get("column");
      String columnName = getName(columnDefine.get("comment"));
      String columnLength = columnDefine.get("stringLength");
      if (!StringUtils.equalsIgnoreCase(columnCode, "id") && StringUtils.isNotEmpty(columnLength)) {
        fields.put(toField(columnCode), new Pair<Long, String>(Long.valueOf(columnLength), columnName));
      }
    }
    return fields;
  }

  public static Map<String, List<String>> toFieldValidateRules(List<Map<String, String>> columnDefines) {
    Map<String, List<String>> fields = MapUtil.newLinkedMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnCode = columnDefine.get("column");
      String consts = getText(columnDefine.get("comment"), new Pair<String, String>("{", "}"));
      String dataType = toDataType(columnDefine.get("type"));
      String isNull = columnDefine.get("isNull");
      String columnLength = columnDefine.get("stringLength");
      if (!StringUtils.equalsIgnoreCase(columnCode, "id") && !StringUtils.equalsIgnoreCase(columnCode, "del_flag")
        && !StringUtils.equalsIgnoreCase(columnCode, "creator") && !StringUtils.equalsIgnoreCase(columnCode, "create_time")
        && !StringUtils.equalsIgnoreCase(columnCode, "updator") && !StringUtils.equalsIgnoreCase(columnCode, "update_time")) {
        String field = toField(columnCode);
        if (StringUtils.equalsIgnoreCase(isNull, "NO")) {
          if(fields.containsKey(field)) {
            fields.get(field).add("required: true");
          } else {
            fields.put(field, ListUtil.newArrayList("required: true"));
          }
        }

        if(StringUtils.isNotEmpty(columnLength)) {
          if(fields.containsKey(field)) {
            fields.get(field).add("maxlength: " + columnLength);
          } else {
            fields.put(field, ListUtil.newArrayList("maxlength: " + columnLength));
          }
        }

        if((StringUtils.equals(dataType, "Long") || StringUtils.equals(dataType, "Integer")) && StringUtils.isEmpty(consts) && !StringUtils.endsWith(columnCode, "_id")) {
          if(fields.containsKey(field)) {
            fields.get(field).add("digits: true");
          } else {
            fields.put(field, ListUtil.newArrayList("digits: true"));
          }
        }
      }
    }
    return fields;
  }

  public static Map<String, List<String>> toFieldLayVerifys(List<Map<String, String>> columnDefines) {
    Map<String, List<String>> fields = MapUtil.newLinkedMap();
    for (Map<String, String> columnDefine : columnDefines) {
      String columnCode = columnDefine.get("column");
      String consts = getText(columnDefine.get("comment"), new Pair<String, String>("{", "}"));
      String dataType = toDataType(columnDefine.get("type"));
      String isNull = columnDefine.get("isNull");
      String columnLength = columnDefine.get("stringLength");
      if (!StringUtils.equalsIgnoreCase(columnCode, "id") && !StringUtils.equalsIgnoreCase(columnCode, "del_flag")
        && !StringUtils.equalsIgnoreCase(columnCode, "creator") && !StringUtils.equalsIgnoreCase(columnCode, "create_time")
        && !StringUtils.equalsIgnoreCase(columnCode, "updator") && !StringUtils.equalsIgnoreCase(columnCode, "update_time")) {
        String field = toField(columnCode);

        if (StringUtils.equalsIgnoreCase(isNull, "NO")) {
          if(fields.containsKey(field)) {
            fields.get(field).add("required");
          } else {
            fields.put(field, ListUtil.newArrayList("required"));
          }
        }

        if(StringUtils.isNotEmpty(columnLength)) {
          if(fields.containsKey(field)) {
            fields.get(field).add(field + "_maxlength");
          } else {
            fields.put(field, ListUtil.newArrayList(field + "_maxlength"));
          }
        }

        if((StringUtils.equals(dataType, "Long") || StringUtils.equals(dataType, "Integer")) && StringUtils.isEmpty(consts) && !StringUtils.endsWith(columnCode, "_id")) {
          if(fields.containsKey(field)) {
            fields.get(field).add("number");
          } else {
            fields.put(field, ListUtil.newArrayList("number"));
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

  public static String toUnit(String type) {
    String unit = "text";
    switch (type) {
      case "date":
        unit = "date";
        break;
      case "datetime":
      case "timestamp":
        unit = "datetime";
        break;
      case "text":
        unit = "textarea";
        break;
      case "varchar":
      case "bigint":
      case "int":
      case "smallint":
      case "tinyint":
      case "decimal":
    }
    return unit;
  }

  public static String toDataType(String type) {
    String dataType = "String";
    switch (type) {
      case "bigint":
        dataType = "Long";
        break;
      case "int":
      case "smallint":
      case "tinyint":
        dataType = "Integer";
        break;
      case "decimal":
        dataType = "java.math.BigDecimal";
        break;
      case "date":
        dataType = "java.time.LocalDate";
        break;
      case "datetime":
      case "timestamp":
        dataType = "java.time.LocalDateTime";
        break;
      case "varchar":
      case "text":
    }
    return dataType;
  }

  public static String getText(String text, Pair<String, String>... betweens) {
    for(Pair<String, String> between : betweens) {
      String value = StringUtils.substringBetween(text, between.getFirst(), between.getSecond());
      if(StringUtils.isNotEmpty(value)) {
        return value;
      }
    }
    return null;
  }

  public static final String[] NAME_ENDS = new String[]{"(", "（", "[", "【", "{"};
  public static String getName(String text) {
    int endIndex = StringUtils.length(text);
    for(String end : NAME_ENDS) {
      int index = StringUtils.indexOf(text, end);
      if(index >= 0 && index < endIndex) {
        endIndex = index;
      }
    }
    return StringUtils.substring(text, 0, endIndex);
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
