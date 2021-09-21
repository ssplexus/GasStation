/**
 * Класс автоколонки
 */
public class GasStation
{
    // Константы марок топлива
    public static final String FUEL_AI_92 = "Аи-92";
    public static final String FUEL_AI_95 = "Аи-95";

    // Константа лимит резервуара
    private static final int TANK_LIMIT = 1000;
    // Журнал действий
    private static StringBuilder gasStationLog;
    // Число колонок
    private static int stationsCnt = 0;

    // Количество топлива в резервуарах
    private static int tank_ai_92 = 1000;
    private static int tank_ai_95 = 1000;

    // Номер колонки
    private final int stationNum;
    // Лимит колонки
    private int stationLimit;

    GasStation()
    {
        this(100);
    }
    GasStation(int limit)
    {
        this.stationLimit = limit;
        stationNum = ++stationsCnt;
        // Если объект журнала не создан, то создаём
        if(gasStationLog == null) gasStationLog = new StringBuilder();
    }

    /**
     *  Метод получения номера колонки
     * @return - возвращает номер колонки
     */
    public int getStationNum()
    {
        return stationNum;
    }

    /**
     * Статический метод вывода информации об остатках топлива в резервуарах
     */
    public static void getTanksRem()
    {
        System.out.println("В резервуарах осталось:");
        System.out.println(String.format("%s - %d литров", FUEL_AI_92, tank_ai_92));
        System.out.println(String.format("%s - %d литров", FUEL_AI_95, tank_ai_95));
    }

    /**
     * Метод для осуществления заправки автомобиля
     *
     * @param _args - массив входных аргументов:
     *              1 - марка топлива (92 или 95)
     *              2 - номер колонки
     *              3 - количество топлива
     * @return - результат отработки метода
     */
    public boolean gasFilling(int _args[])
    {
        // Если какой-либо из аргументов = 0, то выход с отрицательным результатом
        if(_args[0] <= 0 || _args[1] <= 0 || _args[2] <= 0) return false;
        // Флаг марки топлива Аи-92
        boolean isAi92;
        // Если запрос не для этой колонки, то выход с отрицательным результатом
        if(_args[1] != stationNum)  return false;

        // Если марка топлива не Аи-92, значит считаем, что это Аи-95
        isAi92 = _args[0] == 92 ? true : false;

        String msg;
        int val = _args[2];

        // Получение значения резервуара запрашиваемой марки топлива во временную переменную
        int tank = (isAi92?tank_ai_92:tank_ai_95);

        // Если запрошено больше чем установленный порог для колонки
        if(val > stationLimit)
        {
            val = stationLimit;
            msg = String.format("На %d колонке установлен лимит в %d литров\n",stationNum, stationLimit);
            System.out.print(msg);
            stationLogger(msg);
        }

        // Если запрошено больше чем есть в резервуаре
        if(val > tank)
        {
            val = tank;
            tank = 0;
        }
        else
            tank -= val;

        // Обновление значения количества топлива в резервуаре
        if(isAi92)
            tank_ai_92 = tank;
        else
            tank_ai_95 = tank;

        // Если запрошено больше чем есть в резервуаре
        if(tank == 0 && val == 0)
            msg = String.format("Невозможно осуществить заправку, в резервуаре %s нет топлива\n", (isAi92)?FUEL_AI_92:FUEL_AI_95);
        // Если запрошено ровно столько сколько было в резервуаре
        else if (tank ==0)
            msg = String.format("На %d колонке заправлено %d литров из %d, резервуар %s пуст\n", getStationNum(), val, _args[2], (isAi92)?FUEL_AI_92:FUEL_AI_95);
        else
            msg = String.format("На %d колонке заправлено %d литров топлива %s\nв резервуаре осталось %d литров\n", getStationNum(), val, (isAi92)?FUEL_AI_92:FUEL_AI_95, tank);

        System.out.print(msg);
        stationLogger(msg);

        return true;
    }

/**
 * Статический метод для осуществления пополнения резервуара
 *
 * @param _args - массив входных аргументов:
 *              1 - марка топлива (92 или 95)
 *              2 - не используется
 *              3 - количество топлива
 * @return - результат отработки метода
 */
    public static boolean tankFilling(int _args[])
    {
        // Если какой-либо из аргументов = 0, то выход с отрицательным результатом
        if(_args[0] <= 0 || _args[2] <= 0) return false;
        // Флаг марки топлива Аи-92
        boolean isAi92;
        // Если марка топлива не Аи-92, значит считаем, что это Аи-95
        isAi92 = _args[0] == 92 ? true : false;

        String msg;
        int val = _args[2];

        // Получение значения резервуара запрашиваемой марки топлива во временную переменную
        int tank = (isAi92?tank_ai_92:tank_ai_95);

        // Если превышение лимита резервуара
        if(val + tank > TANK_LIMIT)
        {
            val = TANK_LIMIT - tank;
            tank = TANK_LIMIT;
        }
        else
            tank += val;

        // Обновление значения количества топлива в резервуаре
        if(isAi92)
            tank_ai_92 = tank;
        else
            tank_ai_95 = tank;

        if(tank == TANK_LIMIT)
            msg = String.format("Резервуар %s переполнен, залито %d литров из %d\n", isAi92?FUEL_AI_92:FUEL_AI_95, val, _args[2]);
        else
            msg = String.format("Резервуар %s пополнен на %d литров\n", isAi92?FUEL_AI_92:FUEL_AI_95, _args[2]);

        System.out.print(msg);
        stationLogger(msg);

        return true;
    }

    /**
     *  Статический метод для записи в журнал действий
     * @param _msg - записываемое действие
     */
    public static void stationLogger(String _msg)
    {
        gasStationLog.append(_msg);
    }

    /**
     * Статический метод для вывода журнала
     */
    public static void printLog()
    {
        System.out.println(gasStationLog);
    }
}
