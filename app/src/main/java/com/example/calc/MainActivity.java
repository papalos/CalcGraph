package com.example.calc;

/*Калькулятро версии 2.0
* Описаны только ранее незатрагиваемые моменты*/

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
/*Рефлексия*/
import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity {

    /*Защищенные чтобы были доступны во вложенном классе*/
    protected double first = 0, second = 0;
    protected String operator = "no";
    protected String result = "";
    protected int lengthString = 0;
    protected TextView Screen;
    public String memory = "";

    /*Переменные для работы со звуком*/
    protected SoundPool MyTrackPool;
    protected int click, clank, bang, boom, took, shoof, bam;
    final int MAX_STREAMS = 5;


    /*Вложенный класс слушателя*/
    private class ListenerMyButtons implements OnClickListener {


        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case (R.id.one):
                    MyTrackPool.play(click,1,1,0,0,1);
                    Screen.setText(Screen.getText().toString().concat("1")); //Правильно выполненная конкатенация, неправильную смотри в предыдущей версии
                    break;
                case (R.id.two):
                    MyTrackPool.play(clank,1,1,0,0,1);
                    Screen.setText(Screen.getText().toString().concat("2"));
                    break;
                case (R.id.three):
                    MyTrackPool.play(bang,1,1,0,0,1);
                    Screen.setText(Screen.getText().toString().concat("3"));
                    break;
                case (R.id.four):
                    MyTrackPool.play(boom,1,1,0,0,1);
                    Screen.setText(Screen.getText().toString().concat("4"));
                    break;
                case (R.id.five):
                    MyTrackPool.play(took,1,1,0,0,1);
                    Screen.setText(Screen.getText().toString().concat("5"));
                    break;
                case (R.id.six):
                    MyTrackPool.play(shoof,1,1,0,0,1);
                    Screen.setText(Screen.getText().toString().concat("6"));
                    break;
                case (R.id.seven):
                    MyTrackPool.play(bam,1,1,0,0,1);
                    Screen.setText(Screen.getText().toString().concat("7"));
                    break;
                case (R.id.eight):
                    MyTrackPool.play(click,1,1,0,0,1);
                    Screen.setText(Screen.getText().toString().concat("8"));
                    break;
                case (R.id.nine):
                    MyTrackPool.play(clank,1,1,0,0,1);
                    Screen.setText(Screen.getText().toString().concat("9"));
                    break;
                case (R.id.ziro):
                    MyTrackPool.play(bam,1,1,0,0,1);
                    Screen.setText(Screen.getText().toString().concat("0"));
                    break;
                case (R.id.plus):
                    MyTrackPool.play(bang,1,1,0,0,1);
                    if (Screen.length() == 0 || !operator.equals("no")){
                        Screen.setText(Screen.getText().toString().concat("+"));
                    }else{
                        try {
                            operation("plus", "+");
                        } catch (Exception e) {
                            Screen.setText(R.string.error);
                            return;
                        }
                    }
                    break;
                case (R.id.min):
                    MyTrackPool.play(boom,1,1,0,0,1);
                    if (Screen.length() == 0 || !operator.equals("no")) {
                        Screen.setText(Screen.getText().toString().concat("-"));
                    } else {
                        try {
                            operation("min", "-");
                        } catch (Exception e) {
                            Screen.setText(R.string.error);
                            return;
                        }
                    }
                    break;
                case (R.id.mult):
                    MyTrackPool.play(took,1,1,0,0,1);
                    try {
                        operation("mult","*");
                    }catch (Exception e){Screen.setText(R.string.error); return;}
                    break;
                case (R.id.div):
                    MyTrackPool.play(shoof,1,1,0,0,1);
                    try {
                        operation("div","/");
                    }catch (Exception e){Screen.setText(R.string.error); return;}
                    break;
                case (R.id.clean):
                    MyTrackPool.play(clank,1,1,0,0,1);
                    first = 0;
                    second = 0;
                    operator = "no";
                    result = "";
                    lengthString = 0;
                    Screen.setText("");
                    break;
                case (R.id.equal):
                    MyTrackPool.play(boom,1,1,0,0,1);
                        /*Если до нажатия кнопки в первой переменной по прежнему 0, выводим ошибку*/
                    if(first==0){Screen.setText(R.string.error); return;}
                        /*Здесь мы уже получаем не интеджер как в первом варианте калькулятора? а дабл*/
                    try{/*Если нажимается равно до набора второй переменной выводим ошибку*/
                        second = Double.parseDouble(Screen.getText().toString().substring(lengthString + 1)); //если все впорядке записываем значение второй переменной
                    }catch (Exception e){Screen.setText(R.string.error); return;}

                    switch (operator){
                            /*/Приводим к интеджеру так как при +, -, * у нас не может быть дробных чисел, и чтобы в ответе нам не мешалась нулевая дробная часть*/
                        case ("plus"): result = Integer.toString(((int)(first + second)));
                            break;
                        case ("min"): result = Integer.toString(((int)(first - second)));
                            break;
                        case ("mult"): result = Integer.toString(((int)(first * second)));
                            break;
                        case ("div"):
                                /*Если вторая переменная равна 0, выводим ошибку, делить на ноль нельзя*/
                            if(second==0){Screen.setText(R.string.error); return;}
                                /*Тут более подробно! Так как при делении мы можем получить дробное значение
                                  а интеджер откусывал у нас дробную часть в предыдущем варианте мы плоучали 5/2=2. Поправим*/
                                /*При делении двух даблов мы получим дабл, ура, но теперь мы будем получать такую картину 4/2=2.0 некрасиво, поправим*/
                            double div = first / second;
                                /*если у нас результат с дробной частью минус результат без дробной части равен нулю, значит полученное число целое*/
                            if((div-(int)div)==0){
                                    /*Тогда мы это целое приводим к целому что избавит нас от нуля после запятой и перегоняем его в стрингу и засовываем в переменную result*/
                                result = Long.toString((long)div);
                            } else {
                                    /*Если же дробная часть у нас не нулевая, оставляем все как есть*/
                                result = Double.toString(div);
                            }
                            break;
                        default: Screen.setText(R.string.error); return; //обрабатываем ошибку если равно нажато до ввода оператора
                    }
                        /*Выводим итоговый результат*/
                    Screen.setText(Screen.getText().toString().concat("=" + result));
                    break;
            }
        }
        private void operation (String operatorName, String operatorSymbol){
            lengthString = Screen.getText().toString().length();
            first = Double.parseDouble(Screen.getText().toString());
            Screen.setText(Screen.getText().toString().concat(operatorSymbol));
            operator = operatorName;
        }
    }

    /*Пользовательский метод для назначения кнопкам слушателей через цикл,
    по переданному массиву имен кнопок*/
    private void setListenerForAll(OnClickListener ListenerForAll, String titleButton[]){
        /*Считаем длинну переданного массива имен кнопок, для подсчета итераций цикла*/

        /*Берем каждый эллемент массива и получаем по его имени поле*/
        for(String name : titleButton){
            Field fieldR = null;
            try { /*Оборачиваем в обработку исключений ибо без этого нихрена не работает!*/
                fieldR =R.id.class.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            /*Обращаемся к полученному ранее полю и воспользуемся встроенным методом получим значение в интеджере
            * методу требуется передать класс чье значение мы хотим получить, но так как класс у нас статический и объектов нет
            * передаем ему null, в а помещается айдишник выбранной по имени кнопки*/
            int a = 0;
            try {
                a = fieldR.getInt(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            /*Получаем вьюшку по айдишнику преобразуем ее в Батон и устанавливаем на нее Лисенер*/
            (findViewById(a)).setOnClickListener(ListenerForAll);
        }


    }

    /*Переопределенный метод вызываемый при создании активити*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Массив названий наших кнопок для дальнейшего навешивания на них Слушателей*/
        String arrButtonNames[] = {"plus", "min", "mult", "div", "one", "two", "three", "four",
                                "five", "six", "seven", "eight", "nine", "ziro", "equal", "clean"};

        /*Получаем ссылку на текстовое поле в которое будем записывать значение, так как в отличии
        * от кнопок им нам придется пользоваться чаще*/
        Screen = (TextView) findViewById(R.id.result);

        /*Объект управляющий звуком*/
        MyTrackPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);

        /*переменные содержащие ссылки на звуковые файлы*/
        click = MyTrackPool.load(this, R.raw.a, 1);
        clank = MyTrackPool.load(this, R.raw.b, 1);
        bang = MyTrackPool.load(this, R.raw.c, 1);
        boom = MyTrackPool.load(this, R.raw.d, 1);
        took = MyTrackPool.load(this, R.raw.e, 1);
        shoof = MyTrackPool.load(this, R.raw.f, 1);
        bam = MyTrackPool.load(this, R.raw.g, 1);

        /*Создаем наш Слушатель описанный выше*/
        ListenerMyButtons MyListenerOnClick = new ListenerMyButtons();

        /*Вызов метода который дприкручивает переданный слушатель каждой кнопке по ее имени, реализацию см.ниже*/
        setListenerForAll(MyListenerOnClick, arrButtonNames);

        registerForContextMenu(Screen);


    }

    /*Метод вызываемый при нажатии кнопки вызова меню*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("menu_1");
        menu.add("menu_2");
        menu.add("menu_3");
        menu.add("menu_4");
        return super.onCreateOptionsMenu(menu);
    }

    /*Метод вызываемый перед уничтожением активити для сохранения необходимых состояний*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("OPERATOR", operator);
        outState.putDouble("FIRST", first);
        outState.putDouble("SECOND", second);
        outState.putString("RESULT", result);
        outState.putInt("LENGTH", lengthString);
        outState.putString("SCREEN",Screen.getText().toString());

    }

    /*Метод вызываемый после создания активити и перед ее стартом для востановления состояний*/
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        first = savedInstanceState.getDouble("FIRST");
        second = savedInstanceState.getDouble("SECOND");
        operator = savedInstanceState.getString("OPERATOR");
        result = savedInstanceState.getString("RESULT");
        lengthString = savedInstanceState.getInt("LENGTH");
        Screen.setText(savedInstanceState.getString("SCREEN"));
    }

    /*Метод для вызова контекстного меню*/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().toString().equals("add")){
            memory = result;

        }
        if(item.getTitle().toString().equals("edit")){
            Screen.setText(Screen.getText().toString().concat(memory));

        }
        return super.onContextItemSelected(item);
    }

}
