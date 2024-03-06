import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public enum Column implements Columnable {
    Customer("Customer"),
    BankName("Bank Name"),
    AccountNumber("Account Number"),
    Amount("Available Amount");

    private String columnName;

    private static int[] realOrder; // массив, который показывает порядок отображения

    private Column(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Задает новый порядок отображения колонок, который хранится в массиве realOrder.
     * realOrder[индекс в энуме] = порядок отображения; -1, если колонка не отображается.
     *
     * @param newOrder новая последовательность колонок, в которой они будут отображаться в таблице
     * @throws IllegalArgumentException при дубликате колонки
     */
    public static void configureColumns(Column... newOrder) {
        realOrder = new int[values().length];
        for (Column column : values()) {
            realOrder[column.ordinal()] = -1;
            boolean isFound = false;

            for (int i = 0; i < newOrder.length; i++) {
                if (column == newOrder[i]) {
                    if (isFound) {
                        throw new IllegalArgumentException("Column '" + column.columnName + "' is already configured.");
                    }
                    realOrder[column.ordinal()] = i;
                    isFound = true;
                }
            }
        }
    }

    /**
     * Вычисляет и возвращает список отображаемых колонок в сконфигурированом порядке (см. метод configureColumns)
     * Используется поле realOrder.
     *
     * @return список колонок
     */
    public static List<Column> getVisibleColumns() {
        List<Column> result = new LinkedList<>();
        for (int i = 0; i < values().length; i++) {      //           заполняем лист null
            result.add(null);
        }
        for (int i = 0; i < realOrder.length; i++) {
            if(realOrder[i] == -1) {                     //           проходимся по realOrder, и смотрим на значения, если равно -1, пропускаем.
                continue;
            }
            Column[] array = Column.values();            //           иначе получаем массив из нашего Enum, у которого забираем Enum с порядком равным i,
                                                         //           и ставил его за место null при помощи метода set.
            result.set(realOrder[i], array[i]);
        }
        result.removeAll(Collections.singleton(null));   //           избавляемся от null
        return result;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public boolean isShown() {
        return realOrder != null && realOrder[ordinal()] != -1; // смотри, что стоит на месте порядка данного столбца, если -1 то колонка не видна
    }

    @Override
    public void hide() { // тут мы скрываем нужный нам enum и сдвигаем все остальные в списке
        int oldOrdinal = realOrder[ordinal()];
        if (oldOrdinal == -1) return;
        realOrder[ordinal()] = -1;
        for (int i = 0; i < realOrder.length; i++) {
            int currentIndex = realOrder[i];
            if (currentIndex != -1 && currentIndex > oldOrdinal) {
                realOrder[i] -= 1;
            }
        }
    }
}
