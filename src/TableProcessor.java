import java.lang.reflect.Field;

public class TableProcessor {
    public static void createTable(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Class is not annotated with @Table");
        }

        Table tableAnnotation = clazz.getAnnotation(Table.class);
        String tableName = tableAnnotation.name().isEmpty() ? clazz.getSimpleName() : tableAnnotation.name();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("CREATE TABLE ").append(tableName).append(" (");

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String columnName = field.getName();
            String columnType = getColumnType(field.getType());
            queryBuilder.append(columnName).append(" ").append(columnType).append(", ");
        }

        queryBuilder.setLength(queryBuilder.length() - 2);
        queryBuilder.append(");");
        System.out.println("Query generated: " + queryBuilder);
    }

    private static String getColumnType(Class<?> fieldType) {
        if (fieldType == String.class) {
            return "VARCHAR(255)";
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return "INT";
        } else if (fieldType == long.class || fieldType == Long.class) {
            return "BIGINT";
        } else if (fieldType == double.class || fieldType == Double.class) {
            return "DOUBLE";
        }

        throw new IllegalArgumentException("Unsupported field type: " + fieldType.getSimpleName());
    }

}